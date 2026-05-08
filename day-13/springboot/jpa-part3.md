# ***JPA in Spring Boot – Part 3 (First-Level Cache & EntityManager Scope)***

---

# 🔥 What is First-Level Cache?

---

## 📌 Definition

First-Level Cache is the cache maintained inside the **Persistence Context (EntityManager)**.

---

## 🧠 Core Idea

```
Same EntityManager → Same Persistence Context → Same Cache
```

---

## 📌 Meaning

- If same entity is fetched multiple times  
- Within same EntityManager  

👉 DB hit happens only once  

---

# 🔥 Example (VERY IMPORTANT)

---

## 💻 Code Flow

```java
UserDetails output = entityManager.find(UserDetails.class, 1L);
UserDetails output2 = entityManager.find(UserDetails.class, 1L);
```

---

## 🧠 Behavior

```
First call  → DB Hit
Second call → Cache Hit (from Persistence Context)
```

---

# 🔥 Why This Happens?

---

Because:

```
Persistence Context stores entity
        ↓
Next find() checks cache first
        ↓
If found → return without DB call
```

---

# 🔥 Real Application Flow (IMPORTANT)

---

## 📌 Controller

```java
@GetMapping("/test-jpa")
public UserDetails getUser() {

    UserDetails user = new UserDetails("xyx", "xyz@example.com");

    userDetailsService.saveUser(user);

    UserDetails output = userDetailsService.getUser(1L);

    return output;
}
```

---

## 📌 Service

```java
public void saveUser(UserDetails user) {
    userDetailsRepository.save(user);
}

public UserDetails getUser(Long id) {
    return userDetailsRepository.findById(id).get();
}
```

---

## 🧠 Key Insight

👉 Both operations use SAME EntityManager (same request)

👉 So:

```
save() → stores entity in Persistence Context
findById() → returns from cache (no DB hit)
```

---

# 🔥 Important Rule

---

```
Same HTTP Request → Same EntityManager → Same Cache
```

---

# 🔥 EntityManager Scope (VERY IMPORTANT)

---

## 📌 Rule

```
One HTTP Request → One EntityManager
```

---

## 🧠 Meaning

- All DB operations in same request
- Share same Persistence Context

---

## 📌 New Request

```
New HTTP Request → New EntityManager → New Cache
```

---

# 🔥 EntityManager Lifecycle

---

## 📌 Flow

```
HTTP Request Starts
        ↓
EntityManager Created
        ↓
Persistence Context Created
        ↓
DB Operations (save/find)
        ↓
Transaction Commit
        ↓
EntityManager Closed
```

---

# 🔥 Manual Example (From PDF Concept)

---

## 💻 Code

```java
EntityManager em = entityManagerFactory.createEntityManager();

em.getTransaction().begin();

em.persist(user);

em.find(UserDetails.class, 1L);
em.find(UserDetails.class, 1L);

em.getTransaction().commit();

em.close();
```

---

## 🧠 Output

```
Only one SELECT query executed
Second find() is cache hit
```

---

# 🔥 Multiple Sessions Example

---

## 📌 Session 1

```
EntityManager1 → Cache1
find(id=1) → DB Hit
find(id=1) → Cache Hit
```

---

## 📌 Session 2

```
EntityManager2 → Cache2
find(id=1) → DB Hit again
```

---

## 🧠 Key Insight

```
Cache is NOT shared across EntityManagers
```

---

# 🔥 Internal Spring Flow (Simplified)

---

## 📌 What Spring Does Automatically

From DispatcherServlet + OpenEntityManagerInView concept:

```
Request Starts
        ↓
Spring creates EntityManager
        ↓
Binds it to current thread
        ↓
All repository calls use same EntityManager
        ↓
Transaction handled
        ↓
EntityManager closed at end
```

---

## 🧠 Meaning

👉 You never manually create EntityManager in Spring Boot

👉 Spring manages:

- Creation  
- Binding  
- Closing  

---

# 🔥 Cache Hit Scenario (VERY IMPORTANT)

---

## 💻 Example

```java
userRepository.save(user);
userRepository.findById(1L);
```

---

## 🧠 Result

```
save() → puts entity in Persistence Context
findById() → returns from cache
```

---

## 🚨 No SQL SELECT executed

---

# 🔥 When Cache Does NOT Work

---

## ❌ Case 1: New Request

```
Request1 → Cache1
Request2 → Cache2
```

---

## ❌ Case 2: Different EntityManager

```
EntityManager1 ≠ EntityManager2
```

---

## ❌ Case 3: Detached Entity

```
entityManager.detach(entity);
```

---

# 🔥 First-Level Cache vs Second-Level Cache

---

| Feature | First-Level Cache | Second-Level Cache |
|---|---|---|
| Scope | EntityManager | Application |
| Default | Enabled | Disabled |
| Sharing | No | Yes |
| Lifetime | Per request | Across requests |

---

# 🔥 Key Takeaways

---

```
Persistence Context = First-Level Cache
Same EntityManager = Same Cache
Different Request = New Cache
Cache avoids repeated DB calls
```

---

# 🎯 Interview Questions

---

## 🔹 What is First-Level Cache?

Cache inside Persistence Context.

---

## 🔹 Scope of First-Level Cache?

Per EntityManager (per request).

---

## 🔹 When cache hit happens?

When same entity is fetched again in same request.

---

## 🔹 Does cache work across requests?

❌ No

---

## 🔹 Why no second DB call?

Because entity is already in Persistence Context.

---

## 🔹 What is EntityManager scope?

One per HTTP request.

---

## 🔹 Who manages EntityManager in Spring?

Spring (automatically).

---

# 🚀 Final Summary

---

```
First-Level Cache → Inside Persistence Context
EntityManager → Owns cache
Same request → Cache hit
Different request → DB hit
Spring → Manages EntityManager automatically
```