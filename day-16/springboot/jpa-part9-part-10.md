# ***JPA in Spring Boot – Part 9 & 10 (Native Query, Criteria API & Specification API)***

---

# 🔥 Native Query in JPA

---

# 📌 What is Native Query?

---

Native Query means:

```text
Writing plain SQL queries directly
inside JPA
```

Instead of:

```text
JPQL (Entity based)
```

we directly write:

```sql
SELECT * FROM table_name
```

---

# 🧠 Core Idea

---

```text
Spring Boot
    ↓
JPA
    ↓
Direct SQL Query
    ↓
Database
```

---

# 🔥 Why Native Query?

---

## ✅ Advantages

- Supports complex SQL
- Supports DB-specific features
- Faster for bulk operations
- Supports advanced joins
- Supports JSONB, LATERAL JOIN etc.
- Useful when JPQL becomes difficult

---

# ❌ Disadvantages

---

## ⚠️ Database Dependent

```text
MySQL query may fail in PostgreSQL
```

---

## ⚠️ No JPA Abstraction

No:

- Entity lifecycle management
- Lazy loading optimization
- Persistence context advantages
- Database independence

---

# 🔥 Native Query Example

---

# 💻 Repository

```java
@Repository
public interface UserDetailsRepository
        extends JpaRepository<UserDetails, Long> {

    @Query(
            value = """
            SELECT *
            FROM user_details
            WHERE user_name = :userFirstName
            """,
            nativeQuery = true
    )
    List<UserDetails>
    getUserDetailsByNameNativeQuery(
            @Param("userFirstName")
            String userName
    );
}
```

---

# 🧠 Important

---

If:

```sql
SELECT *
```

returns all columns,

then JPA automatically maps:

```text
DB columns → Entity fields
```

---

# 🔥 Problem with Partial Fields

---

# ❌ Example

```sql
SELECT user_name, phone
FROM user_details
```

---

## 💻 Repository

```java
@Query(
        value = """
        SELECT user_name, phone
        FROM user_details
        WHERE user_name = :userFirstName
        """,
        nativeQuery = true
)
List<UserDetails>
getUserDetailsByNameNativeQuery(
        @Param("userFirstName")
        String userName
);
```

---

# ❌ Why It Fails?

---

Because:

```text
Entity expects all fields
```

But query returns:

```text
Only partial columns
```

So JPA cannot map automatically.

---

# 🔥 Solution 1 → SqlResultSetMapping

---

# 📌 Best for DTO Mapping

---

# 💻 Entity

```java
@Entity
@Table(name = "user_details")

@NamedNativeQuery(
        name = "UserDetails.getUserDetailsByName",
        query = """
        SELECT user_name, phone
        FROM user_details
        WHERE user_name = :userFirstName
        """,
        resultSetMapping = "UserDTOMapping"
)

@SqlResultSetMapping(
        name = "UserDTOMapping",
        classes = @ConstructorResult(
                targetClass = UserDTO.class,
                columns = {
                        @ColumnResult(
                                name = "user_name",
                                type = String.class
                        ),
                        @ColumnResult(
                                name = "phone",
                                type = String.class
                        )
                }
        )
)

public class UserDetails {

    @Id
    @GeneratedValue(strategy =
            GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "user_name")
    private String name;

    private String phone;
}
```

---

# 💻 DTO

```java
public class UserDTO {

    private String userName;
    private String phone;

    public UserDTO(
            String userName,
            String phone
    ) {
        this.userName = userName;
        this.phone = phone;
    }
}
```

---

# 💻 Repository

```java
@Repository
public interface UserDetailsRepository
        extends JpaRepository<UserDetails, Long> {

    @Query(
            name = "UserDetails.getUserDetailsByName",
            nativeQuery = true
    )
    List<UserDTO>
    getUserDetailsByNameNativeQuery(
            @Param("userFirstName")
            String userName
    );
}
```

---

# 🔥 Solution 2 → Manual Mapping

---

# 💻 Repository

```java
@Query(
        value = """
        SELECT user_name, phone
        FROM user_details
        WHERE user_name = :userFirstName
        """,
        nativeQuery = true
)
List<Object[]>
getUserDetailsByNameNativeQuery(
        @Param("userFirstName")
        String userName
);
```

---

# 💻 Service

```java
public List<UserDTO>
getUserDetailsByNameNativeQuery(String name) {

    List<Object[]> results =
            userDetailsRepository
                    .getUserDetailsByNameNativeQuery(name);

    return results.stream()
            .map(obj -> new UserDTO(
                    (String) obj[0],
                    (String) obj[1]
            ))
            .collect(Collectors.toList());
}
```

---

# 🔥 Dynamic Native Query

---

# 📌 Why Needed?

---

When filters are dynamic.

Example:

```text
Sometimes name filter
Sometimes city filter
Sometimes phone filter
```

---

# 💻 Service

```java
@Service
public class UserDetailsService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<UserDTO>
    getUserDetailsByNameNativeQuery(
            String userName
    ) {

        StringBuilder queryBuilder =
                new StringBuilder(
                        """
                        SELECT
                            ud.user_name AS user_name,
                            ud.phone AS phone,
                            ua.city AS city
                        """
                );

        queryBuilder.append(
                "FROM user_details ud "
        );

        queryBuilder.append(
                """
                JOIN user_address ua
                ON ud.user_address_id = ua.id
                """
        );

        queryBuilder.append("WHERE 1=1 ");

        List<Object> parameters =
                new ArrayList<>();

        if(userName != null &&
                !userName.isEmpty()) {

            queryBuilder.append(
                    "AND ud.user_name = ? "
            );

            parameters.add(userName);
        }

        Query nativeQuery =
                entityManager.createNativeQuery(
                        queryBuilder.toString()
                );

        for(int i = 0;
            i < parameters.size();
            i++) {

            nativeQuery.setParameter(
                    i + 1,
                    parameters.get(i)
            );
        }

        List<Object[]> result =
                nativeQuery.getResultList();

        return UserDTO.mapResultToDTO(result);
    }
}
```

---

# 🧠 Why "WHERE 1=1"?

---

Because dynamic conditions become easier.

```sql
WHERE 1=1
AND name = ?
AND city = ?
```

---

# 🔥 Pagination & Sorting in Native Query

---

# 🔥 Way 1 → Manual Pagination

---

# 💻 Example

```java
queryBuilder.append(
        "ORDER BY ud.user_name DESC"
);

int size = 5;
int page = 0;

queryBuilder.append(
        " LIMIT ? OFFSET ? "
);

parameters.add(size);
parameters.add(page * size);
```

---

# 🧠 Generated SQL

---

```sql
ORDER BY user_name DESC
LIMIT 5 OFFSET 0
```

---

# 🔥 Way 2 → Pageable

---

# 💻 Service

```java
Pageable pageableObj =
        PageRequest.of(
                0,
                5,
                Sort.by("phone").descending()
        );

return userDetailsRepository
        .getUserDetailsByNameNativeQuery(
                name,
                pageableObj
        );
```

---

# 💻 Repository

```java
@Query(
        value = """
        SELECT *
        FROM user_details ud
        WHERE ud.user_name = :userName
        """,
        nativeQuery = true
)
List<UserDetails>
getUserDetailsByNameNativeQuery(
        @Param("userName")
        String userName,
        Pageable pageable
);
```

---

# 🔥 Criteria API

---

# 📌 What is Criteria API?

---

Criteria API allows:

```text
Dynamic
Type-safe
Programmatic Query Building
```

without writing SQL manually.

---

# 🧠 Why Criteria API?

---

Because Native Query:

```text
DB dependent
```

But Criteria API:

```text
Uses JPA abstraction
```

---

# 🔥 Criteria API Hierarchy

---

```text
EntityManager
      ↓
CriteriaBuilder
      ↓
CriteriaQuery
      ↓
Root
      ↓
Predicate
      ↓
TypedQuery
      ↓
Result
```

---

# 🔥 Meaning of Each Component

---

| Component | Purpose |
|---|---|
| CriteriaBuilder | Creates query objects |
| CriteriaQuery | Defines query structure |
| Root | Represents table/entity |
| Predicate | WHERE conditions |
| TypedQuery | Executes query |

---

# 🔥 Controller Example

---

# 💻 Controller

```java
@GetMapping("/user/{phone}")
public List<UserDetails>
getUserDetailsByPhoneCriteriaAPI(
        @PathVariable Long phone
) {
    return userDetailsService
            .getUserDetailsByPhoneCriteriaAPI(
                    phone
            );
}
```

---

# 🔥 Basic Criteria API Query

---

# 💻 Service

```java
@Service
public class UserDetailsService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<UserDetails>
    getUserDetailsByPhoneCriteriaAPI(
            Long phoneNo
    ) {

        CriteriaBuilder cb =
                entityManager
                        .getCriteriaBuilder();

        CriteriaQuery<UserDetails> crQuery =
                cb.createQuery(
                        UserDetails.class
                );

        Root<UserDetails> user =
                crQuery.from(
                        UserDetails.class
                );

        crQuery.select(user);

        Predicate predicate =
                cb.equal(
                        user.get("phone"),
                        phoneNo
                );

        crQuery.where(predicate);

        TypedQuery<UserDetails> query =
                entityManager
                        .createQuery(crQuery);

        return query.getResultList();
    }
}
```

---

# 🔥 Generated SQL

---

```sql
SELECT *
FROM user_details
WHERE phone = ?
```

---

# 🔥 Comparison Operators

---

| Method | SQL Equivalent |
|---|---|
| cb.equal() | = |
| cb.notEqual() | <> |
| cb.gt() | > |
| cb.ge() | >= |
| cb.lt() | < |
| cb.le() | <= |

---

# 💻 Examples

```java
cb.equal(user.get("phone"), 123);

cb.notEqual(user.get("phone"), 123);

cb.gt(user.get("phone"), 123);

cb.ge(user.get("phone"), 123);

cb.lt(user.get("phone"), 123);

cb.le(user.get("phone"), 123);
```

---

# 🔥 Logical Operators

---

| Method | SQL Equivalent |
|---|---|
| cb.and() | AND |
| cb.or() | OR |
| cb.not() | NOT |

---

# 💻 Example

```java
Predicate predicate1 =
        cb.equal(
                user.get("phone"),
                phoneNo
        );

Predicate predicate2 =
        cb.notEqual(
                user.get("name"),
                "AA"
        );

Predicate finalPredicate =
        cb.and(
                predicate1,
                predicate2
        );

crQuery.where(finalPredicate);
```

---

# 🔥 String Operations

---

| Method | SQL Equivalent |
|---|---|
| cb.like() | LIKE |
| cb.notLike() | NOT LIKE |

---

# 💻 Example

```java
cb.like(
        user.get("name"),
        "S%"
);
```

---

# 🧠 SQL

```sql
WHERE name LIKE 'S%'
```

---

# 🔥 Collection Operations

---

# 💻 IN Query

```java
cb.in(user.get("phone"))
        .value(11)
        .value(7);
```

---

# 🧠 SQL

```sql
WHERE phone IN (11, 7)
```

---

# 🔥 Select Multiple Fields

---

# 💻 Example

```java
CriteriaQuery<Object[]> crQuery =
        cb.createQuery(Object[].class);

Root<UserDetails> user =
        crQuery.from(UserDetails.class);

crQuery.multiselect(
        user.get("name"),
        user.get("phone")
);
```

---

# 🔥 Mapping Result

---

# 💻 Example

```java
List<Object[]> results =
        query.getResultList();

for(Object[] row : results) {

    String name =
            (String) row[0];

    Long phone =
            (Long) row[1];

    UserDTO dto =
            new UserDTO(name, phone);
}
```

---

# 🔥 JOIN in Criteria API

---

# 💻 Example

```java
Join<UserDetails, UserAddress> address =
        user.join(
                "userAddress",
                JoinType.INNER
        );
```

---

# 💻 Select Data from Both Tables

```java
crQuery.multiselect(
        user.get("name"),
        address.get("city")
);
```

---

# 🔥 Pagination & Sorting

---

# 💻 Sorting

```java
crQuery.orderBy(
        cb.desc(user.get("name"))
);
```

---

# 💻 Pagination

```java
query.setFirstResult(0);

query.setMaxResults(5);
```

---

# 🧠 Meaning

---

```text
setFirstResult → OFFSET

setMaxResults → LIMIT
```

---

# 🔥 Specification API

---

# 📌 Why Specification API?

---

Criteria API has two major problems:

---

# ❌ Problem 1 → Code Duplicity

---

Same Predicate reused in:

- Multiple methods
- Multiple services
- Multiple classes

---

# ❌ Problem 2 → Boilerplate Code

---

Too much manual handling:

- CriteriaBuilder
- CriteriaQuery
- Root
- Predicate
- TypedQuery

---

# 🔥 Solution → Specification API

---

# 🧠 Core Idea

---

Move reusable filter logic into:

```java
Specification<T>
```

---

# 🔥 Specification Interface Methods

---

| Method | SQL Equivalent |
|---|---|
| toPredicate() | WHERE |
| and() | AND |
| or() | OR |
| not() | NOT |

---

# 🔥 Specification Class

---

# 💻 Example

```java
public class UserSpecification {

    public static
    Specification<UserDetails>
    equalsPhone(Long phoneNo) {

        return (root, query, cb) -> {

            return cb.equal(
                    root.get("phone"),
                    phoneNo
            );
        };
    }
}
```

---

# 🔥 Service Using Specification

---

# 💻 Example

```java
Specification<UserDetails> specification =
        UserSpecification.equalsPhone(
                phoneNo
        );

Predicate predicate =
        specification.toPredicate(
                userRoot,
                crQuery,
                cb
        );

crQuery.where(predicate);
```

---

# 🔥 JpaSpecificationExecutor

---

# 💻 Repository

```java
@Repository
public interface UserDetailsRepository
        extends JpaRepository<UserDetails, Long>,
        JpaSpecificationExecutor<UserDetails> {

}
```

---

# 🔥 Why JpaSpecificationExecutor?

---

It automatically handles:

- Query creation
- Predicate application
- Sorting
- Pagination
- Execution

---

# 🔥 Reusable Specifications

---

# 💻 Example

```java
public class UserSpecification {

    public static
    Specification<UserDetails>
    equalsPhone(Long phoneNo) {

        return (root, query, cb) ->

                cb.equal(
                        root.get("phone"),
                        phoneNo
                );
    }

    public static
    Specification<UserDetails>
    likeName(String name) {

        return (root, query, cb) ->

                cb.like(
                        root.get("name"),
                        "%" + name + "%"
                );
    }

    public static
    Specification<UserDetails>
    joinAddress() {

        return (root, query, cb) -> {

            root.join(
                    "userAddress",
                    JoinType.INNER
            );

            return null;
        };
    }
}
```

---

# 🔥 Combining Specifications

---

# 💻 Example

```java
Specification<UserDetails> result =

        Specification
                .where(
                        UserSpecification.joinAddress()
                )

                .and(
                        UserSpecification.equalsPhone(123L)
                )

                .and(
                        UserSpecification.likeName("AA")
                );

return userDetailsRepository.findAll(result);
```

---

# 🧠 Generated SQL

---

```sql
SELECT *
FROM user_details
INNER JOIN user_address
WHERE phone = 123
AND name LIKE '%AA%'
```

---

# 🔥 Criteria API vs Specification API

---

| Criteria API | Specification API |
|---|---|
| Verbose | Cleaner |
| Boilerplate heavy | Minimal code |
| Less reusable | Highly reusable |
| Manual query execution | Framework handles it |
| Hard to scale | Easy to scale |

---

# 🎯 Interview Questions

---

# 🔹 What is Native Query?

Direct SQL query execution inside JPA.

---

# 🔹 Difference:
JPQL vs Native Query?

| JPQL | Native Query |
|---|---|
| Entity based | SQL based |
| DB independent | DB dependent |
| JPA abstraction | Direct DB interaction |

---

# 🔹 Why Criteria API?

To build:

```text
Dynamic
Type-safe
Queries
```

without raw SQL.

---

# 🔹 What is Predicate?

Represents:

```sql
WHERE condition
```

---

# 🔹 Why Specification API?

To avoid:

- Code duplicity
- Boilerplate code

---

# 🔹 Why JpaSpecificationExecutor?

Spring internally handles:

- CriteriaBuilder
- Query creation
- Predicate execution

---

# 🔹 Difference:
Criteria API vs Specification API?

Specification API is cleaner and reusable.

---

# 🚀 Final Summary

---

```text
Native Query:
- Direct SQL
- DB dependent
- Supports complex queries

Criteria API:
- Dynamic query builder
- Type-safe
- JPA abstraction

Specification API:
- Reusable predicates
- Cleaner architecture
- Solves boilerplate issue

JpaSpecificationExecutor:
- Auto query execution
- Supports pagination & sorting
```
