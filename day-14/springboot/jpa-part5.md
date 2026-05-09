# ***JPA in Spring Boot – Part 5 (DDL, Mapping, Keys & Generation)***

---

# 🔥 spring.jpa.hibernate.ddl-auto

---

## 📌 What is it?

Controls how Hibernate manages DB schema

---

## 📊 Modes (VERY IMPORTANT)

| Value | Create | Update | Delete | Use Case |
|---|---|---|---|---|
| none | ❌ | ❌ | ❌ | Production |
| update | ✅ | ✅ | ❌ | Development |
| validate | ❌ | ❌ | ❌ | Schema validation |
| create | ✅ | ✅ | ✅ | Testing |
| create-drop | ✅ | ✅ | ✅ (on shutdown) | In-memory DB |

---

## 🧠 Behavior Summary

```
none → Do nothing
update → Add missing columns (no delete)
validate → Check mismatch → throw exception
create → Drop + recreate schema
create-drop → Drop on shutdown
```

---

# 🔥 Mapping Classes to Tables

---

## 📌 @Table Annotation

Optional annotation to map entity → table

---

## 🧠 Default Behavior

```
UserDetails → USER_DETAILS
(CamelCase → UPPER_SNAKE_CASE)
```

---

## 💻 Basic Example

```java
@Entity
@Table(name = "USER_DETAILS")
public class UserDetails {
}
```

---

## 📌 With Schema

```java
@Table(name = "USER_DETAILS", schema = "ONBOARDING")
```

---

## 📌 Unique Constraints

```java
@Table(
    name = "USER_DETAILS",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "phone"),
        @UniqueConstraint(columnNames = {"name", "email"})
    }
)
```

---

## 📌 Indexes

```java
@Table(
    indexes = {
        @Index(name = "idx_phone", columnList = "phone"),
        @Index(name = "idx_name_email", columnList = "name,email")
    }
)
```

---

# 🔥 @Column Annotation

---

## 📌 Purpose

Customize column properties

---

## 💻 Example

```java
@Column(
    name = "full_name",
    unique = true,
    nullable = false,
    length = 255
)
private String name;
```

---

## 🧠 If Not Defined

```
JPA uses default mapping
```

---

# 🔥 Primary Key in JPA

---

## 📌 Rules

- Must be unique  
- Cannot be null  
- Only ONE @Id per entity  

---

## 💻 Example

```java
@Id
private Long id;
```

---

# 🔥 Composite Primary Key

---

## 📌 When Needed?

```
Multiple columns together form primary key
```

---

# 🔥 Approach 1 → @IdClass

---

## 💻 Entity

```java
@Entity
@IdClass(UserDetailsCK.class)
public class UserDetails {

    @Id
    private String name;

    @Id
    private String address;

    private String phone;
}
```

---

## 💻 Key Class

```java
public class UserDetailsCK implements Serializable {

    private String name;
    private String address;

    public UserDetailsCK() {}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof UserDetailsCK)) return false;

        UserDetailsCK other = (UserDetailsCK) obj;
        return name.equals(other.name) &&
               address.equals(other.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address);
    }
}
```

---

# 🔥 Approach 2 → @EmbeddedId

---

## 💻 Entity

```java
@Entity
public class UserDetails {

    @EmbeddedId
    private UserDetailsCK id;

    private String phone;
}
```

---

## 💻 Key Class

```java
@Embeddable
public class UserDetailsCK implements Serializable {

    private String name;
    private String address;

    // equals + hashCode
}
```

---

# 🔥 Why equals() & hashCode()?

---

## 🧠 Reason

```
JPA uses caching (HashMap internally)
Primary key = key
```

👉 Wrong implementation → cache issues

---

# 🔥 Why Serializable?

---

## 🧠 Reason

```
Composite keys are custom objects
Needed for:
- Caching
- Network transfer
- Distributed systems
```

---

# 🔥 @GeneratedValue (AUTO ID)

---

## 📌 Purpose

Auto-generate primary key

---

## ⚠️ Note

```
Only for SINGLE primary key
(Not for composite keys)
```

---

# 🔥 Generation Strategies

---

## 📌 1. IDENTITY

---

### 🧠 Behavior

```
DB auto-increment
Each insert → DB generates ID
```

---

### 💻 Example

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

---

### 🚨 Problem

```
Each insert → DB call required
```

---

# 🔥 2. SEQUENCE (BEST PRACTICE)

---

## 🧠 Behavior

```
Uses DB sequence
Hibernate can cache IDs
```

---

## 💻 Example

```java
@Id
@GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "user_seq"
)
@SequenceGenerator(
    name = "user_seq",
    sequenceName = "db_seq_name",
    initialValue = 100,
    allocationSize = 5
)
private Long id;
```

---

## 📌 Important Parameters

- name → internal reference  
- sequenceName → DB sequence  
- initialValue → start value  
- allocationSize → batch size  

---

## 🧠 Flow

```
allocationSize = 5

Hibernate fetches 5 IDs at once:
100,101,102,103,104

After 5 inserts → next DB call
```

---

# 🔥 SEQUENCE vs IDENTITY

---

## ✅ Advantages of SEQUENCE

- Better performance (less DB calls)  
- ID caching possible  
- Works across multiple tables  
- More control (start, increment)  
- Better portability  

---

# 🔥 3. TABLE Strategy (NOT RECOMMENDED)

---

## 🧠 Behavior

```
Separate table for ID generation
```

---

## 🚨 Problems

- Extra SELECT + UPDATE queries  
- Locking issues  
- Performance bottleneck  

---

# 🔥 Internal Flow (SEQUENCE)

---

```
App Start
   ↓
Fetch 5 IDs from DB
   ↓
Use locally (no DB hit)
   ↓
After 5 → fetch next batch
```

---



# 🎯 Interview Questions

---

## 🔹 What is ddl-auto?

Controls schema generation

---

## 🔹 Difference: update vs validate?

update → modifies schema  
validate → only checks  

---

## 🔹 Default table naming?

CamelCase → UPPER_SNAKE_CASE  

---

## 🔹 Composite key approaches?

@IdClass and @EmbeddedId  

---

## 🔹 Why equals/hashCode needed?

Used in caching (HashMap)

---

## 🔹 Best generation strategy?

SEQUENCE  

---

## 🔹 Why SEQUENCE over IDENTITY?

Less DB calls, better performance  

---

## 🔹 Does @GeneratedValue work for composite key?

❌ No  

---

# 🚀 Final Summary

---

```
ddl-auto → Schema control
@Table → Table mapping
@Column → Column customization
@Id → Primary key
Composite Key → IdClass / EmbeddedId
@GeneratedValue → Auto ID

Best Practice → Use SEQUENCE
```