# ***Bean Scopes in Spring Boot***

---

# 🔥 **What is Bean Scope?**

---

## 📌 Definition

Bean Scope defines **how long a Spring bean object will live** and **how many instances of that bean Spring will create**.

---

## 🧠 Simple Meaning

```text
Bean Scope = Life and visibility of a Spring bean
```

---

## 🧠 Core Idea

```text
Same bean class can behave differently based on scope.

Spring decides:
How many objects to create
When to create object
When to destroy object
Who can reuse object
```

---

# 🔥 **Types of Bean Scopes**

---

```text
1. Singleton
2. Prototype
3. Request
4. Session
```

---

# 🔥 **1. Singleton Scope**

---

## 📌 Definition

Singleton is the default scope in Spring.

Spring creates **only one object** for the entire Spring IoC container.

---

## 🧠 Simple Meaning

```text
One bean object shared everywhere
```

---

## Example

```java
@Component
public class UserService {

    public UserService() {
        System.out.println("UserService object created");
    }
}
```

```java
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
}
```

---

## What Happens?

```text
Application starts
        ↓
Spring creates one UserService object
        ↓
Same object is reused everywhere
```

---

## Important Points

```text
Default scope
Only one instance per IoC container
Created eagerly at application startup
Same object reused for every request
```

---

# 🔥 **Singleton Example with HashCode**

---

```java
@Component
public class UserService {

    public UserService() {
        System.out.println("UserService initialized");
    }

    public void printHashCode() {
        System.out.println("UserService hashCode: " + this.hashCode());
    }
}
```

```java
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/fetchUser")
    public String fetchUser() {
        userService.printHashCode();
        return "User fetched";
    }
}
```

---

## Output

```text
UserService initialized

First request:
UserService hashCode: 12345

Second request:
UserService hashCode: 12345

Third request:
UserService hashCode: 12345
```

---

## 🧠 Meaning

```text
Same hashCode = Same object
```

---

# 🔥 **2. Prototype Scope**

---

## 📌 Definition

Prototype scope creates a **new bean object every time it is requested from Spring container**.

---

## 🧠 Simple Meaning

```text
Every request for bean = new object
```

---

## Example

```java
@Component
@Scope("prototype")
public class UserService {

    public UserService() {
        System.out.println("UserService object created");
    }
}
```

---

## Important Points

```text
New object is created every time bean is requested
Lazily initialized
Spring creates the object but does not fully manage destruction
```

---

# 🔥 **Prototype with Controller**

---

```java
@Component
@Scope("prototype")
public class UserService {

    public UserService() {
        System.out.println("UserService initialized");
    }

    public void printHashCode() {
        System.out.println("UserService hashCode: " + this.hashCode());
    }
}
```

```java
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/fetchUser")
    public String fetchUser() {
        userService.printHashCode();
        return "User fetched";
    }
}
```

---

## ⚠️ Important Trap

If prototype bean is injected into a singleton controller:

```text
Controller is singleton
Prototype bean is injected only once when controller is created
So same prototype object may be reused through that singleton
```

---

## Why?

```text
Singleton controller is created once
At that time Spring injects one prototype object
After that controller keeps same reference
```

---

# 🔥 **Correct Way to Use Prototype Inside Singleton**

---

Use `ObjectProvider`.

```java
@RestController
@RequestMapping("/api")
public class UserController {

    private final ObjectProvider<UserService> userServiceProvider;

    public UserController(ObjectProvider<UserService> userServiceProvider) {
        this.userServiceProvider = userServiceProvider;
    }

    @GetMapping("/fetchUser")
    public String fetchUser() {
        UserService userService = userServiceProvider.getObject();
        userService.printHashCode();
        return "User fetched";
    }
}
```

---

## Now What Happens?

```text
Each API call
        ↓
getObject() asks Spring for new prototype bean
        ↓
New UserService object created
```

---

# 🔥 **3. Request Scope**

---

## 📌 Definition

Request scope creates a **new bean object for every HTTP request**.

---

## 🧠 Simple Meaning

```text
One HTTP request = One bean object
```

---

## Example

```java
@Component
@Scope(value = "request")
public class UserService {

    public UserService() {
        System.out.println("UserService initialized");
    }
}
```

---

## Important Points

```text
Created only when HTTP request is active
One object per request
Destroyed after request completes
Lazily initialized
Used only in web applications
```

---

# 🔥 **Request Scope Problem with Singleton Controller**

---

Controller is singleton by default.

```java
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
}
```

If `UserService` is request scoped, Spring may face a problem:

```text
Controller is created at application startup
But request scoped bean needs active HTTP request
At startup, no HTTP request exists
```

---

# 🔥 **Solution: Scoped Proxy**

---

```java
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService {

    public UserService() {
        System.out.println("UserService initialized");
    }
}
```

---

## What Scoped Proxy Does

```text
Spring injects a proxy object into singleton controller
Actual request-scoped object is created only when HTTP request comes
```

---

## Flow

```text
Application starts
        ↓
Singleton controller created
        ↓
Proxy injected instead of real request bean
        ↓
HTTP request comes
        ↓
Actual request bean created
        ↓
Request completes
        ↓
Request bean destroyed
```

---

# 🔥 **4. Session Scope**

---

## 📌 Definition

Session scope creates a **new bean object for every HTTP session**.

---

## 🧠 Simple Meaning

```text
One user session = One bean object
```

---

## Example

```java
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSession {

    public UserSession() {
        System.out.println("UserSession object created");
    }
}
```

---

## Important Points

```text
New object is created for each HTTP session
Same object reused within same session
Lazily initialized
Destroyed when session expires
Useful for user-specific data
```

---

# 🔥 **Session Scope Flow**

---

```text
User A opens application
        ↓
Session A created
        ↓
UserSession bean created for User A

User A calls multiple APIs
        ↓
Same UserSession bean reused

User B opens application
        ↓
Session B created
        ↓
New UserSession bean created for User B
```

---

# 🔥 **Singleton vs Prototype vs Request vs Session**

---

| Scope     | Object Created     | Reused For         | Created When        |
| --------- | ------------------ | ------------------ | ------------------- |
| Singleton | Once               | Entire application | Application startup |
| Prototype | Every bean request | Not reused         | When requested      |
| Request   | Every HTTP request | Same request only  | During HTTP request |
| Session   | Every HTTP session | Same user session  | When session starts |

---

# 🔥 **Real-World Analogy**

---

## Singleton

```text
One office printer shared by everyone
```

---

## Prototype

```text
New form printed every time someone asks
```

---

## Request

```text
One token for one customer visit
```

---

## Session

```text
One shopping cart for one logged-in user session
```

---

# 🔥 **When to Use Which Scope**

---

| Requirement                    | Scope     |
| ------------------------------ | --------- |
| Stateless service classes      | Singleton |
| New object every time          | Prototype |
| Data only for one HTTP request | Request   |
| Data for one user session      | Session   |

---

# 🔥 **Common Mistakes**

---

## ❌ Mistake 1: Thinking Singleton means Java Singleton Pattern

Spring singleton means:

```text
One object per Spring IoC container
```

Java Singleton means:

```text
One object per JVM/classloader
```

---

## ❌ Mistake 2: Injecting Prototype into Singleton Directly

```text
Prototype object is created only once during singleton creation
```

Use:

```text
ObjectProvider
```

---

## ❌ Mistake 3: Injecting Request Scope Without Proxy

```text
No active request exists during singleton bean creation
```

Use:

```java
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
```

---

## ❌ Mistake 4: Storing User Data in Singleton Bean

Bad:

```java
@Component
public class UserService {
    private String currentUser;
}
```

Because singleton is shared by all users.

---

# 🔥 **Interview Questions**

---

## What is Bean Scope?

```text
Bean scope defines how many bean objects Spring creates and how long they live.
```

---

## What is default scope in Spring?

```text
Singleton.
```

---

## What is singleton scope?

```text
Only one bean object is created per Spring IoC container.
```

---

## What is prototype scope?

```text
A new bean object is created every time it is requested from Spring.
```

---

## What is request scope?

```text
A new bean object is created for every HTTP request.
```

---

## What is session scope?

```text
A new bean object is created for every HTTP session.
```

---

## Difference between singleton and prototype?

```text
Singleton creates one shared object.
Prototype creates a new object whenever requested.
```

---

## Why do we need proxy for request/session scope?

```text
Because singleton beans are created at startup, but request/session beans need an active HTTP request/session.
Proxy delays actual bean creation until request/session is available.
```

---

## Is Spring singleton same as Java singleton?

```text
No. Spring singleton is one object per IoC container, while Java singleton is usually one object per JVM.
```

---

# 🔥 **Memory Trick**

---

```text
Singleton → One object for app

Prototype → New object every time

Request → New object per HTTP request

Session → New object per user session
```

---

# 🚀 **Final Summary**

---

* Bean scope controls bean object creation and lifetime.
* Singleton is default.
* Singleton creates one shared bean per IoC container.
* Prototype creates a new bean whenever requested.
* Request creates one bean per HTTP request.
* Session creates one bean per HTTP session.
* Request and session scoped beans usually need scoped proxy when injected into singleton beans.
* Do not store user-specific mutable data inside singleton beans.

---

```text
Bean Scope = Object Lifetime + Object Sharing Rule
```

---
