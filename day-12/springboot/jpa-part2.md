# ***JPA in Spring Boot – Part 2 (Full Lifecycle + Internal Flow)***

---

# 🔥 What is JPA?

---

## 📌 Definition

JPA is a specification that allows interaction with databases using Java objects instead of SQL.

---

# 🔥 JPA Infrastructure Lifecycle (VERY IMPORTANT)

---

## 📌 Full Bootstrapping Flow

```
Application Starts
        ↓
Reads application.properties
        ↓
Creates Persistence Unit
        ↓
Creates EntityManagerFactory (EMF)
        ↓
Application Ready
```

---

## 🔥 1️⃣ Persistence Unit

---

## 📌 What is it?

A configuration block that contains:

- Database details
- Entity classes
- Hibernate configuration

---

## 🧠 In Spring Boot

Defined via:

```
application.properties
```

---

## 🔹 Contains

```
DB URL
Driver
Dialect
Entities
```

---

# 🔥 2️⃣ EntityManagerFactory (EMF)

---

## 📌 What is it?

Factory that creates EntityManager instances

---

## 🧠 Characteristics

```
One per application
Heavy object
Thread-safe
```

---

## 📌 Role

```
Creates → EntityManager
```

---

# 🔥 3️⃣ EntityManager (EM)

---

## 📌 What is it?

Main interface used to perform DB operations

---

## 🧠 Characteristics

```
Multiple instances
Lightweight
NOT thread-safe
```

---

## 📌 Lifecycle

```
Request starts → EntityManager created
Request ends → EntityManager destroyed
```

---

# 🔥 JPA Runtime Workflow

---

## 📌 Full Execution Flow

```
Controller
   ↓
Service
   ↓
Repository (JpaRepository)
   ↓
EntityManager
   ↓
Persistence Context
   ↓
Hibernate
   ↓
JDBC
   ↓
Database
```

---

# 🔥 Persistence Context (CORE)

---

## 📌 Definition

Memory space inside EntityManager that:

- Stores entities
- Tracks changes
- Syncs with DB

---

## 🧠 Core Idea

```
Persistence Context = First Level Cache + Change Tracker
```

---

# 🔥 Persistence Context Lifecycle

---

## 📌 Flow Diagram

```
EntityManager Created
        ↓
Persistence Context Created
        ↓
Entity Loaded / Persisted
        ↓
Entity Becomes Managed
        ↓
Changes Tracked (Dirty Checking)
        ↓
Flush Triggered
        ↓
SQL Generated
        ↓
Transaction Commit
        ↓
Persistence Context Cleared
```

---

# 🔥 Entity Lifecycle (INSIDE Persistence Context)

---

## 📌 States

```
Transient → Managed → Detached → Removed
```

---

## 1️⃣ Transient

```
New Object
Not in DB
Not tracked
```

---

## 2️⃣ Managed (MOST IMPORTANT)

```
Inside Persistence Context
Tracked automatically
```

---

## 3️⃣ Detached

```
Removed from context
No tracking
```

---

## 4️⃣ Removed

```
Marked for deletion
```

---

# 🔥 Dirty Checking (HOW CHANGES ARE TRACKED)

---

## 📌 Flow

```
Entity Loaded
        ↓
Snapshot Stored
        ↓
Object Modified
        ↓
Compare Snapshot vs Current
        ↓
If changed → UPDATE SQL generated
```

---

# 🔥 EntityManager Operations

---

## 📌 Methods

```
persist() → INSERT
find()    → SELECT
merge()   → UPDATE
remove()  → DELETE
```

---

# 🔥 Example Flow (CRITICAL)

---

## 💻 Code

```java
@Transactional
public void updateUser() {

    User user = entityManager.find(User.class, 1);

    user.setName("John");

}
```

---

## 🧠 What Happens

```
DB → Entity loaded
        ↓
Stored in Persistence Context
        ↓
Object modified
        ↓
Dirty checking detects change
        ↓
SQL generated at commit
```

---

# 🔥 Flush vs Commit

---

## 📌 Flush

```
Generates SQL
Does NOT commit
```

---

## 📌 Commit

```
Executes SQL permanently
```

---

## 🧠 Flow

```
Change Object
   ↓
Flush → SQL Generated
   ↓
Commit → Data Saved
```

---

# 🔥 First Level Cache

---

## 📌 Behavior

```
Same entity fetched twice
        ↓
Second call comes from cache
```

---

# 🔥 Full JPA Lifecycle (END-TO-END)

---

```
Application Starts
        ↓
Persistence Unit Created
        ↓
EntityManagerFactory Created
        ↓
Request Comes
        ↓
EntityManager Created
        ↓
Persistence Context Created
        ↓
Entity Loaded / Saved
        ↓
Entity Managed
        ↓
Changes Tracked
        ↓
Flush
        ↓
Commit
        ↓
DB Updated
        ↓
EntityManager Closed
```

---

# 🔥 Key Takeaways

---

```
Persistence Unit → Configuration
EntityManagerFactory → Factory
EntityManager → Worker
Persistence Context → Brain
Hibernate → SQL Generator
JDBC → Executor
Transaction → Commit handler
```

---

# 🎯 Interview Questions

---

## 🔹 What is Persistence Unit?

Configuration that initializes JPA setup.

---

## 🔹 What is EntityManagerFactory?

Factory that creates EntityManager.

---

## 🔹 Is EntityManager thread-safe?

❌ No

---

## 🔹 What is Persistence Context?

Memory that tracks entities and manages lifecycle.

---

## 🔹 What is Dirty Checking?

Automatic detection of changes.

---

## 🔹 What happens at transaction commit?

SQL is executed and DB updated.

---

# 🚀 Final Summary

---

```
Persistence Unit → Setup
EMF → Factory
EM → Executes
PC → Tracks
Hibernate → Converts
JDBC → Executes
DB → Stores
```