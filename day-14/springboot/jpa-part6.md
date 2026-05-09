# ***JPA in Spring Boot – Part 6 (Association Mapping, Cascade, Fetch & Serialization)***

---

# 🔥 Association Types in JPA

---

## 📌 What are Associations?

```
Defines relationship between entities (tables)
```

---

## 📊 Types

- OneToOne  
- OneToMany  
- ManyToOne  
- ManyToMany  

---

# 🔥 OneToOne – Unidirectional

---

## 📌 Concept

```
One entity → one entity
Only ONE side holds reference
```

---

## 💻 Example

### Parent Entity

```java
@Entity
@Table(name = "user_details")
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private UserAddress userAddress;
}
```

---

### Child Entity

```java
@Entity
@Table(name = "user_address")
public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;
    private String city;
    private String state;
    private String country;
    private String pinCode;
}
```

---

## 🧠 DB Behavior

```
Foreign Key → user_details.address_id
```

---

# 🔥 @JoinColumn

---

## 📌 Purpose

```
Control foreign key mapping
```

---

## 💻 Example

```java
@JoinColumn(name = "address_id", referencedColumnName = "id")
```

---

# 🔥 Composite Key Mapping

---

## 💻 Example

```java
@JoinColumns({
    @JoinColumn(name = "street", referencedColumnName = "street"),
    @JoinColumn(name = "pin", referencedColumnName = "pinCode")
})
```

---

## 🧠 Rule

```
Map ALL columns of composite key
```

---

# 🔥 Cascade Types (CRITICAL)

---

## 📌 Problem Without Cascade

```
Parent operation DOES NOT affect child
Manual handling required
```

---

# 🔥 CascadeType.PERSIST

---

## 📌 Behavior

```
Saving parent → saves child automatically
```

---

## 💻 Example

```java
@OneToOne(cascade = CascadeType.PERSIST)
```

---

# 🔥 CascadeType.MERGE

---

## 📌 Behavior

```
Updating parent → updates child
```

---

## ⚠️ Important

```
Only PERSIST:
Insert works
Update does NOT update child
```

---

## ✅ Fix

```java
cascade = {CascadeType.PERSIST, CascadeType.MERGE}
```

---

# 🔥 CascadeType.REMOVE

---

## 📌 Behavior

```
Deleting parent → deletes child
```

---

# 🔥 CascadeType.REFRESH

---

## 📌 Behavior

```
Reload entity from DB (bypass cache)
```

---

# 🔥 CascadeType.DETACH

---

## 📌 Behavior

```
Removes entity from persistence context
Stops tracking
```

---

# 🔥 CascadeType.ALL

---

## 📌 Shortcut

```
Includes all cascade operations
```

---

# 🔥 Fetch Types

---

# 🔥 EAGER vs LAZY

---

## 📌 EAGER Loading

```
Child loaded immediately with parent
```

---

## 📌 Default For

- OneToOne  
- ManyToOne  

---

## 📌 LAZY Loading

```
Child loaded only when accessed
```

---

## 📌 Default For

- OneToMany  
- ManyToMany  

---

## 💻 Example

```java
@OneToOne(fetch = FetchType.LAZY)
private UserAddress userAddress;
```

---

# 🔥 Lazy Loading Problem (VERY IMPORTANT)

---

## 📌 Issue

```
GET API fails due to lazy loading
```

---

## 🧠 Why?

```
Hibernate returns proxy
Jackson tries serialization
Data not loaded → failure
```

---

# 🔥 Solutions

---

## ✅ 1. @JsonIgnore

```java
@JsonIgnore
private UserAddress userAddress;
```

---

## ❌ Problem

```
Field removed from response
```

---

## ✅ 2. DTO (BEST PRACTICE)

---

## 💻 Example

```java
public class UserDetailsDTO {

    private Long id;
    private String name;
    private String address;

    public UserDetailsDTO(UserDetails user) {
        this.id = user.getId();
        this.name = user.getName();
        this.address = user.getUserAddress() != null
            ? user.getUserAddress().getStreet()
            : null;
    }
}
```

---

# 🔥 OneToOne – Bidirectional

---

## 📌 Concept

```
Both entities reference each other
```

---

## 💻 Example

### Parent (Owning Side)

```java
@Entity
public class UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private UserAddress userAddress;
}
```

---

### Child (Inverse Side)

```java
@Entity
public class UserAddress {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(mappedBy = "userAddress")
    private UserDetails userDetails;
}
```

---

## 🧠 Key Concept

| Side | Role |
|------|------|
| Owning | Holds foreign key |
| Inverse | No foreign key |

---

# 🔥 Infinite Recursion Problem

---

## 📌 Problem

```
User → Address → User → Address → ...
Infinite loop during JSON serialization
```

---

# 🔥 Solution 1 → JsonManagedReference / JsonBackReference

---

## 💻 Example

### Parent

```java
@JsonManagedReference
private UserAddress userAddress;
```

---

### Child

```java
@JsonBackReference
private UserDetails userDetails;
```

---

## 🧠 Behavior

```
Managed → serialized
Back → ignored
```

---

# 🔥 Solution 2 → JsonIdentityInfo (BEST)

---

## 💻 Example

```java
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id"
)
```

---

## 🧠 Behavior

```
Avoids duplicate serialization using ID
```

---

# 🔥 Flow Diagram

---

```
Client → Controller → Service → Repository
                     ↓
              EntityManager
                     ↓
          Persistence Context

Fetch:
EAGER → immediate load
LAZY → load on access

Serialization:
LAZY → proxy → error
Fix → DTO / JsonIgnore / JsonIdentityInfo
```

---

# 🔥 Real Flow

---

## INSERT

```
Parent + Child → saved together (Cascade.PERSIST)
```

---

## UPDATE

```
Without MERGE → child not updated
With MERGE → child updated
```

---

## DELETE

```
Cascade.REMOVE → deletes both
```

---

## GET

```
EAGER → works but heavy
LAZY → efficient but needs handling
```

---

# 🎯 Interview Questions

---

## 🔹 What is owning side?

Entity holding foreign key

---

## 🔹 What is mappedBy?

Defines inverse side

---

## 🔹 Default fetch types?

- OneToOne → EAGER  
- OneToMany → LAZY  

---

## 🔹 Why lazy fails in API?

Proxy + serialization issue

---

## 🔹 Best solution?

DTO

---

## 🔹 Difference PERSIST vs MERGE?

Insert vs Update

---

## 🔹 Cascade.ALL?

All operations cascade

---

# 🚀 Final Summary

---

```
OneToOne:
- Uni → one side reference
- Bi → both sides

Cascade:
PERSIST → insert
MERGE → update
REMOVE → delete

Fetch:
EAGER → immediate
LAZY → on-demand

Problems:
Lazy → serialization issue
Bidirectional → recursion

Solutions:
DTO (best)
JsonIgnore
JsonManagedReference
JsonIdentityInfo
```