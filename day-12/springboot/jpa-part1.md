# ***JPA in Spring Boot – Part 1 (Foundation)***

---

# 🔥 Before JPA – Understanding JDBC

---

## 📌 What is JDBC?

**JDBC (Java Database Connectivity)** is an API used to:
- Connect to database  
- Execute SQL queries  
- Process results  

---

## 🧠 Core Idea

```
Java Application → JDBC → DB Driver → Database
```

---

## 📌 Example Drivers

- MySQL → Connector/J → `com.mysql.cj.jdbc.Driver`  
- PostgreSQL → PostgreSQL Driver → `org.postgresql.Driver`  
- H2 → In-memory DB → `org.h2.Driver` 

---

# 🔥 JDBC Without Spring Boot

---

## 💻 Database Connection

```java
public class DatabaseConnection {

    public Connection getConnection() {
        try {
            Class.forName("org.h2.Driver");

            return DriverManager.getConnection(
                "jdbc:h2:mem:userDB",
                "sa",
                ""
            );
        } catch (Exception e) {
            // handle exception
        }
        return null;
    }
}
```

---

## 💻 DAO Example

```java
public class UserDAO {

    public void createUser(String userName, int age) {
        try {
            Connection connection = new DatabaseConnection().getConnection();

            String sql = "INSERT INTO users(user_name, age) VALUES (?, ?)";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, userName);
            ps.setInt(2, age);

            ps.executeUpdate();

        } catch (Exception e) {
            // handle exception
        } finally {
            // close connection
        }
    }
}
```

---

# 🚨 Problems with JDBC (VERY IMPORTANT)

---

## ❌ Boilerplate Code

- Connection creation  
- Driver loading  
- Exception handling  
- Closing resources  
- Manual connection pooling 

---

## ❌ Repetitive Work

```
Connection
Statement
PreparedStatement
ResultSet
```

All handled manually every time.

---

## ❌ Poor Maintainability

- Hard to scale  
- Error-prone  
- Tight coupling with DB  

---

# 🔥 JDBC with Spring Boot (JdbcTemplate)

---

## 📌 Dependency

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
```

---

## 💻 Repository

```java
@Repository
public class UserRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void insertUser(String name, int age) {
        String query = "INSERT INTO users(user_name, age) VALUES (?, ?)";
        jdbcTemplate.update(query, name, age);
    }

    public List<User> getUsers() {
        String query = "SELECT * FROM users";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            User user = new User();
            user.setUserId(rs.getInt("user_id"));
            user.setUserName(rs.getString("user_name"));
            user.setAge(rs.getInt("age"));
            return user;
        });
    }
}
```

---

# 🔥 What Spring Solves

---

## ✅ Driver Loading

- Automatically handled at startup  

---

## ✅ Connection Management

- Created automatically when needed  

---

## ✅ Resource Handling

- Connections closed automatically  

---

## ✅ Exception Handling

Instead of generic:

```
SQLException
```

You get:

- DuplicateKeyException  
- QueryTimeoutException  

---

## ✅ Connection Pooling

Spring Boot uses:

```
HikariCP (default)
```

---

## 💻 Config Example

```properties
spring.datasource.url=jdbc:h2:mem:userDB
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
```

---

# 🔥 JdbcTemplate Important Methods

---

## 📌 update()

Used for:

- Insert  
- Update  
- Delete  

```java
jdbcTemplate.update("INSERT INTO users(user_name, age) VALUES (?, ?)", "John", 25);
```

---

## 📌 query()

Returns multiple rows

```java
List<User> users = jdbcTemplate.query("SELECT * FROM users", (rs, rowNum) -> {
    User user = new User();
    user.setUserId(rs.getInt("user_id"));
    return user;
});
```

---

## 📌 queryForObject()

Single row or value

```java
User user = jdbcTemplate.queryForObject(
    "SELECT * FROM users WHERE user_id=?",
    new Object[]{1},
    User.class
);
```

---

## 📌 queryForList()

Single column

```java
List<String> names = jdbcTemplate.queryForList(
    "SELECT user_name FROM users",
    String.class
);
```

---

# 🔥 Still a Problem Exists

---

Even with JdbcTemplate:

- SQL is still manual  
- Mapping is manual  
- Object ↔ Table mapping not automatic  

---

# 🔥 Enter JPA

---

## 📌 What is JPA?

**JPA (Java Persistence API)** is an abstraction over JDBC.

---

## 🧠 Core Idea

```
Java Object ↔ JPA ↔ Hibernate ↔ JDBC ↔ DB
```

---

## 📌 Key Point

- JPA is an **interface/specification**  
- Hibernate is its **implementation**  

---

# 🔥 ORM Concept (IMPORTANT)

---

## 📌 What is ORM?

**Object Relational Mapping**

Maps:

```
Java Class → Table
Object → Row
Fields → Columns
```

---

## 🧠 Example

```java
class User {
    int id;
    String name;
}
```

➡️ becomes

```
TABLE users (id, name)
```

---

# 🔥 Why JPA is Needed?

---

## ❌ Without JPA

- Manual SQL  
- Manual mapping  
- Tight coupling  

---

## ✅ With JPA

- No SQL required (mostly)  
- Automatic mapping  
- Clean code  
- Better abstraction  

---

# 🔥 Architecture Flow

---

## 📌 Full Flow

```
Application
   ↓
JPA (Interface)
   ↓
Hibernate (Implementation)
   ↓
JDBC
   ↓
Database
```

---

## 🧠 Key Insight

- JPA defines rules  
- Hibernate executes them  

---

# 🔥 JDBC vs JPA

---

| Feature | JDBC | JPA |
|---|---|---|
| SQL Writing | Manual | Mostly automatic |
| Mapping | Manual | Automatic |
| Boilerplate | High | Low |
| Abstraction | Low | High |
| Maintainability | Poor | High |

---

# 🔥 Advantages of JPA

---

- Reduces boilerplate code  
- Cleaner architecture  
- Faster development  
- Better maintainability  
- Database independent  

---

# 🔥 When NOT to Use JPA

---

- High-performance critical queries  
- Complex reporting queries  
- Full control required  

---

# 🎯 Interview Questions

---

## 🔹 What is JDBC?

API for database connectivity in Java.

---

## 🔹 Problems with JDBC?

- Boilerplate code  
- Manual resource handling  
- Tight coupling  

---

## 🔹 What is JdbcTemplate?

Spring utility class to reduce JDBC boilerplate.

---

## 🔹 What is JPA?

Java Persistence API – abstraction over JDBC.

---

## 🔹 Is JPA implementation?

❌ No  
✔ It is a specification  

---

## 🔹 What is Hibernate?

Implementation of JPA.

---

## 🔹 What is ORM?

Mapping between Java objects and database tables.

---

## 🔹 Why JPA over JDBC?

- Less code  
- Better abstraction  
- Easier maintenance  

---

# 🚀 Final Summary

---

```
JDBC → Low-level, manual work
JdbcTemplate → Reduced boilerplate
JPA → High-level abstraction (best approach)
Hibernate → JPA implementation
```