# ***JPA in Spring Boot – Part 4 (Second-Level Cache)***

---

# 🔥 Why Second-Level Cache?

---

## 📌 Problem with First-Level Cache

From Part 3:

```
Request 1 → EntityManager1 → Cache1
Request 2 → EntityManager2 → Cache2
```

👉 Cache is NOT shared

---

## 🧠 Result

```
Every new HTTP request → DB hit again
```

---

## 📌 Example Flow

```
1st Request → INSERT → DB Hit
2nd Request → GET → SELECT query
3rd Request → GET → SELECT query again ❌
```

---

# 🔥 Solution → Second-Level Cache (L2)

---

## 📌 Definition

Second-Level Cache is a cache shared across:

```
Multiple EntityManagers
Multiple Requests
```

---

## 🧠 Core Idea

```
EntityManager → First-Level Cache
Application → Second-Level Cache
```

---

# 🔥 L1 vs L2 Cache

---

| Feature | First-Level Cache | Second-Level Cache |
|---|---|---|
| Scope | EntityManager | Application |
| Shared | ❌ No | ✅ Yes |
| Lifetime | Per request | Across requests |
| Default | Enabled | Disabled |

---

# 🔥 How L2 Cache Works

---

## 📌 Flow Diagram

```
Request 1:
EntityManager1 → L1 Cache → DB → Store in L2 Cache

Request 2:
EntityManager2 → L1 Cache (empty)
        ↓
Check L2 Cache
        ↓
Cache Hit → Return (No DB)
```

---

## 🧠 Key Insight

```
L1 Cache → Fastest (per request)
L2 Cache → Shared (cross request)
DB → Last option
```

---

# 🔥 Dependencies (VERY IMPORTANT)

---

## 📌 pom.xml

```xml
<dependency>
    <groupId>org.ehcache</groupId>
    <artifactId>ehcache</artifactId>
    <version>3.10.8</version>
</dependency>

<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-jcache</artifactId>
    <version>6.5.2.Final</version>
</dependency>

<dependency>
    <groupId>javax.cache</groupId>
    <artifactId>cache-api</artifactId>
    <version>1.1.1</version>
</dependency>
```

---

## 🧠 Why These?

- Ehcache → Actual cache provider  
- hibernate-jcache → Hibernate integration  
- cache-api → Standard interface  

---

# 🔥 Configuration

---

## 📌 application.properties

```properties
spring.jpa.properties.hibernate.cache.use_second_level_cache=true
spring.jpa.properties.hibernate.cache.region.factory_class=org.hibernate.cache.jcache.JCacheRegionFactory
spring.jpa.properties.javax.cache.provider=org.ehcache.jsr107.EhcacheCachingProvider
logging.level.org.hibernate.cache.spi=DEBUG
```

---

# 🔥 Enable Cache on Entity

---

## 💻 Example

```java
@Entity
@Cache(
    usage = CacheConcurrencyStrategy.READ_WRITE,
    region = "userDetailsCache"
)
public class UserDetails {
    
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String email;
}
```

---

# 🔥 Cache Flow (VERY IMPORTANT)

---

## 📌 Insert Operation

```
INSERT → Direct DB Hit
(No cache involved)
```

---

## 📌 First GET

```
Check L2 Cache → MISS
        ↓
Fetch from DB
        ↓
Store in L2 Cache
```

---

## 📌 Second GET

```
Check L2 Cache → HIT
        ↓
Return from cache (No DB)
```

---

# 🔥 Region Concept

---

## 📌 What is Region?

Logical grouping of cached data

---

## 🧠 Why Needed?

Each region can have:

- Different TTL
- Different size
- Different eviction policy

---

## 💻 Example

```java
@Cache(region = "userDetailsCache")
```

---

# 🔥 ehcache.xml Configuration

---

## 📌 File Location

```
src/main/resources/ehcache.xml
```

---

## 💻 Example

```xml
<ehcache>

    <cache alias="userDetailsCache"
           maxElementsInMemory="100"
           timeToLiveSeconds="60"
           evictionStrategy="LIFO" />

    <cache alias="orderDetailsCache"
           maxElementsInMemory="1000"
           timeToLiveSeconds="200"
           evictionStrategy="FIFO" />

</ehcache>
```

---

# 🔥 Cache Concurrency Strategies (VERY IMPORTANT)

---

## 📌 1. READ_ONLY

---

### 🧠 Use Case

```
Static Data (No updates)
```

---

### 🚨 Rule

```
Update → Exception
```

---

---

## 📌 2. READ_WRITE (Most Used)

---

### 🧠 Behavior

```
Read → Shared Lock
Write → Exclusive Lock
```

---

### 📌 Flow

```
Read → Cache Hit
Update → Lock cache → Update DB → Update cache
```

---

---

## 📌 3. NONSTRICT_READ_WRITE

---

### 🧠 Behavior

```
No strict locking
```

---

### 📌 Flow

```
Update → Cache invalidated
(No immediate update)
```

---

### 🚨 Risk

```
May return stale data
```

---

---

## 📌 4. TRANSACTIONAL

---

### 🧠 Behavior

```
Strong consistency
Uses transaction-level cache
```

---

### 📌 Flow

```
Read + Write locks
Fully synchronized with DB
```

---

---

# 🔥 Update Flow (IMPORTANT)

---

## 📌 READ_WRITE Strategy

```
Transaction Start
        ↓
Acquire Lock
        ↓
Update DB
        ↓
Update Cache
        ↓
Release Lock
```

---

# 🔥 When to Use L2 Cache

---

## ✅ Good For

- Read-heavy systems  
- Rare updates  
- Static/reference data  

---

## ❌ Avoid When

- Frequent updates  
- Real-time consistency required  

---

# 🔥 Key Takeaways

---

```
L1 Cache → Per request
L2 Cache → Shared across requests
Insert → DB only
First GET → DB + Cache
Next GET → Cache Hit
```

---

# 🎯 Interview Questions

---

## 🔹 What is Second-Level Cache?

Shared cache across multiple EntityManagers.

---

## 🔹 Difference L1 vs L2?

L1 → per request  
L2 → application-wide  

---

## 🔹 Why L2 needed?

To avoid repeated DB hits across requests.

---

## 🔹 What is Region?

Logical group of cache entries.

---

## 🔹 Which strategy is most used?

READ_WRITE

---

## 🔹 Risk of NONSTRICT?

Stale data

---

## 🔹 Does INSERT go to cache?

❌ No, directly DB

---

# 🚀 Final Summary

---

```
L1 Cache → Request scoped
L2 Cache → Application scoped
DB → Last fallback

Enable L2 → Dependencies + Config + @Cache
```