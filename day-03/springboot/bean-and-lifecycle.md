# ***Spring Bean & Bean Lifecycle***

---

# 🔥 **What is a Bean in Spring Boot?**

---

## 📌 Definition

A Bean is a **Java object that is managed by the Spring IoC Container**.

---

## 🧠 Core Idea

```text
Normal Object → You create using new

Bean → Spring creates, manages, and injects
```

---

```text
Application Start → IoC Container → Creates Beans → Manages Lifecycle
```

---

## 🧠 Simple Meaning

```text
Bean = Object managed by Spring
```

---

---

# 🔥 **Why Do We Need Beans?**

---

## ❌ Without Beans

```java
UserService service = new UserService();
```

Problems:

```text
Manual object creation
Tight coupling
Hard to manage dependencies
Hard to test
```

---

## ✅ With Beans

```java
@Autowired
UserService service;
```

Benefits:

```text
Spring manages object
Loose coupling
Automatic dependency injection
Centralized lifecycle management
```

---

---

# 🔥 **What is IoC Container?**

---

## 📌 Definition

IoC (Inversion of Control) Container is responsible for:

```text
Creating beans
Managing beans
Injecting dependencies
Destroying beans
```

---

## 🧠 Flow

```text
Application Start
        ↓
IoC Container starts
        ↓
Beans created
        ↓
Dependencies injected
        ↓
Beans used
        ↓
Beans destroyed
```

---

---

# 🔥 **How to Create a Bean?**

---

## ✅ Two Ways

```text
1. @Component Annotation
2. @Bean Annotation
```

---

---

# 🔥 **1. Using @Component**

---

## 📌 Definition

Marks a class as a Spring Bean.

---

## 🧠 Simple Meaning

```text
@Component → Spring will create object automatically
```

---

## Example

```java
@Component
public class User {

    private String username;
    private String email;

}
```

---

## 🧠 What Happens Internally?

```text
Spring scans package
        ↓
Finds @Component
        ↓
Creates object (bean)
        ↓
Stores in IoC container
```

---

## ⚠️ Important

Spring internally does:

```java
new User()
```

---

---

# 🔥 **Problem with @Component**

---

## ❌ Case

```java
@Component
public class User {

    private String username;
    private String email;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
```

---

## 🚨 What Happens?

```text
Application fails to start ❌
```

---

## ❓ Why?

```text
Spring does not know what values to pass in constructor
```

---

---

# 🔥 **Solution → @Bean Annotation**

---

## 📌 Definition

Used to manually define bean creation logic.

---

## 🧠 Simple Meaning

```text
@Bean → You tell Spring HOW to create object
```

---

## Example

```java
@Configuration
public class AppConfig {

    @Bean
    public User createUser() {
        return new User("defaultUser", "default@email.com");
    }
}
```

---

## 🧠 What Happens?

```text
Spring calls createUser()
        ↓
Object created manually
        ↓
Stored as Bean
```

---

---

# 🔥 **Multiple Beans of Same Type**

---

## Example

```java
@Bean
public User user1() {
    return new User("user1", "email1");
}

@Bean
public User user2() {
    return new User("user2", "email2");
}
```

---

## 🧠 Result

```text
Spring creates 2 beans
```

---

---

# 🔥 **How Spring Finds Beans?**

---

## 🔹 Method 1: Component Scanning

```java
@SpringBootApplication
@ComponentScan("com.example")
```

---

## 🧠 Meaning

```text
Spring scans packages
Finds @Component, @Service, @Repository
Creates beans automatically
```

---

## 🔹 Method 2: @Bean in Configuration

```java
@Configuration
public class AppConfig {
    @Bean
    public User user() {
        return new User();
    }
}
```

---

---

# 🔥 **When Are Beans Created?**

---

## 🧠 Two Types

```text
1. Eager Initialization
2. Lazy Initialization
```

---

## 🔹 Eager (Default)

```text
Bean created at application startup
```

---

Example:

```text
Singleton beans are eager by default
```

---

## 🔹 Lazy

```text
Bean created only when needed
```

---

Example:

```java
@Lazy
@Component
public class Order {
    public Order() {
        System.out.println("Initializing Order");
    }
}
```

---

---

# 🔥 **Bean Lifecycle**

---

## 🧠 Complete Flow

```text
Application Start
        ↓
IoC Container Started
        ↓
Bean Created (Constructor)
        ↓
Dependency Injection
        ↓
@PostConstruct
        ↓
Bean Ready to Use
        ↓
Application Running
        ↓
@PreDestroy
        ↓
Bean Destroyed
```

---

---

# 🔥 **Lifecycle Explained Step-by-Step**

---

## 🔹 Step 1: IoC Container Starts

```text
Spring Boot starts
IoC container initialized
```

---

## 🔹 Step 2: Bean Creation

```java
@Component
class User {
    public User() {
        System.out.println("User created");
    }
}
```

---

## 🔹 Step 3: Dependency Injection

```java
@Autowired
Order order;
```

---

## 🔹 Step 4: @PostConstruct

```java
@PostConstruct
public void init() {
    System.out.println("Bean initialized");
}
```

---

## 🔹 Step 5: Bean Usage

```text
Application uses bean
Business logic executes
```

---

## 🔹 Step 6: @PreDestroy

```java
@PreDestroy
public void destroy() {
    System.out.println("Bean destroyed");
}
```

---

---

# 🔥 **Complete Example**

---

```java
@Component
public class User {

    public User() {
        System.out.println("Constructor called");
    }

    @PostConstruct
    public void init() {
        System.out.println("Bean initialized");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Bean destroyed");
    }
}
```

---

## 🧠 Output Flow

```text
Application starts
Constructor called
Bean initialized
(Application running)
Bean destroyed
```

---

---

# 🔥 **Real-World Analogy**

---

## 🏢 Office Example

```text
Employee joins (Bean created)
        ↓
Laptop assigned (Dependency Injection)
        ↓
Orientation done (@PostConstruct)
        ↓
Employee works (Usage)
        ↓
Exit process (@PreDestroy)
```

---

---

# 🔥 **When to Use @Component vs @Bean**

---

| Use Case | Annotation |
|---|---|
| Simple class | @Component |
| Custom creation logic | @Bean |
| External library class | @Bean |
| Constructor needs parameters | @Bean |

---

---

# 🔥 **Common Mistakes**

---

## ❌ Using constructor with @Component

```text
Spring doesn't know what to inject
```

---

## ❌ Forgetting @ComponentScan

```text
Bean not detected
```

---

## ❌ Creating objects manually

```java
new User()
```

Instead:

```java
@Autowired User user;
```

---

---

# 🔥 **Interview Questions**

---

## 🔹 What is a Bean?

```text
A Java object managed by Spring IoC container
```

---

## 🔹 What is IoC?

```text
Spring controls object creation instead of developer
```

---

## 🔹 Difference between @Component and @Bean?

```text
@Component → auto-detected

@Bean → manually defined
```

---

## 🔹 What is Bean Lifecycle?

```text
Creation → Injection → Initialization → Usage → Destruction
```

---

## 🔹 What is @PostConstruct?

```text
Method called after bean initialization
```

---

## 🔹 What is @PreDestroy?

```text
Method called before bean destruction
```

---

# 🔥 **Memory Trick**

---

```text
@Component → Auto bean

@Bean → Manual bean

@PostConstruct → After creation

@PreDestroy → Before destroy
```

---

# 🚀 **Final Summary**

---

* Bean = Spring-managed object  
* IoC Container manages lifecycle  
* Two ways to create bean → @Component, @Bean  
* Beans can be eager or lazy  
* Lifecycle includes creation → injection → init → destroy  

---

```text
Spring Bean = Managed Object + Lifecycle + Dependency Injection
```

---

👉 This is the foundation of Spring Boot architecture