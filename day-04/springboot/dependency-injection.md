# ***Dependency Injection in Spring Boot***

---

# 🔥 **What is Dependency Injection?**

---

## 📌 Definition

Dependency Injection is a technique where an object receives its required dependencies from an external source instead of creating them by itself.

---

## 🧠 Core Idea

```text
Do not create dependency manually using new.

Instead:

Spring Container creates the dependency
Spring Container injects it where needed
```

---

## Simple Meaning

```text
Dependency Injection = Give required object from outside
```

---

# 🔥 **What is a Dependency?**

---

## Example

```java
public class UserService {

    private OrderService orderService;

}
```

Here:

```text
UserService depends on OrderService
```

So:

```text
OrderService is a dependency of UserService
```

---

# 🔥 **Problem Without Dependency Injection**

---

```java
public class UserService {

    private OrderService orderService = new OrderService();

    public void processUser() {
        orderService.createOrder();
    }
}
```

---

## ❌ What is Wrong Here?

```text
UserService is tightly coupled with OrderService
UserService creates its own dependency
Hard to test
Hard to replace implementation
Violates Dependency Inversion Principle
```

---

# 🔥 **Main Problem**

---

```text
The class that USES the dependency is also CREATING the dependency
```

That means one class is doing two jobs:

```text
1. Business logic
2. Object creation
```

---

# 🔥 **Dependency Injection Solution**

---

Instead of this:

```java
private OrderService orderService = new OrderService();
```

We do this:

```java
private OrderService orderService;
```

And Spring injects the object.

---

## Spring Boot Example

```java
@Component
public class OrderService {

    public void createOrder() {
        System.out.println("Order created");
    }
}
```

```java
@Component
public class UserService {

    private final OrderService orderService;

    public UserService(OrderService orderService) {
        this.orderService = orderService;
    }

    public void processUser() {
        orderService.createOrder();
    }
}
```

---

# 🔥 **How Spring Injects Dependency**

---

```text
Application starts
        ↓
Spring scans classes
        ↓
Finds @Component classes
        ↓
Creates beans
        ↓
Checks dependencies
        ↓
Injects required bean
        ↓
Application uses ready object
```

---

# 🔥 **Dependency Injection and DIP**

---

## DIP = Dependency Inversion Principle

```text
High-level modules should not depend on low-level modules.
Both should depend on abstraction.
```

---

## ❌ Bad Code

```java
public class UserService {

    private OnlineOrderService orderService = new OnlineOrderService();

}
```

Problem:

```text
UserService depends directly on concrete class
```

---

## ✅ Better Code

```java
public interface OrderService {
    void createOrder();
}
```

```java
@Component
public class OnlineOrderService implements OrderService {

    public void createOrder() {
        System.out.println("Online order created");
    }
}
```

```java
@Component
public class UserService {

    private final OrderService orderService;

    public UserService(OrderService orderService) {
        this.orderService = orderService;
    }
}
```

Now:

```text
UserService depends on abstraction
```

---

# 🔥 **Types of Dependency Injection**

---

```text
1. Field Injection
2. Setter Injection
3. Constructor Injection
```

---

# 🔥 **1. Field Injection**

---

## Definition

Dependency is injected directly into the field.

---

## Example

```java
@Component
public class UserService {

    @Autowired
    private OrderService orderService;

}
```

---

## How It Works

```text
Spring creates UserService
Spring finds @Autowired field
Spring injects OrderService using reflection
```

---

## ✅ Advantage

```text
Very simple
Less code
Easy for beginners
```

---

## ❌ Disadvantages

```text
Cannot use final fields
Hard to unit test
Can cause NullPointerException if object is created manually
Hidden dependency
Not recommended for professional code
```

---

## Problem Example

```java
UserService userService = new UserService();
userService.processUser();
```

This can cause:

```text
NullPointerException
```

Because Spring did not inject the dependency.

---

# 🔥 **2. Setter Injection**

---

## Definition

Dependency is injected using setter method.

---

## Example

```java
@Component
public class UserService {

    private OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
}
```

---

## ✅ Advantages

```text
Dependency can be changed later
Useful for optional dependencies
Easy to pass mock during testing
```

---

## ❌ Disadvantages

```text
Object can exist without required dependency
Field cannot be final
Less safe than constructor injection
```

---

# 🔥 **3. Constructor Injection**

---

## Definition

Dependency is injected through constructor.

---

## Example

```java
@Component
public class UserService {

    private final OrderService orderService;

    public UserService(OrderService orderService) {
        this.orderService = orderService;
    }
}
```

---

## ✅ Why Constructor Injection is Recommended

```text
Mandatory dependencies are provided at object creation time
Object is fully initialized
Supports final fields
Avoids NullPointerException
Best for unit testing
Fails fast if dependency is missing
```

---

## Important Point

If there is only one constructor:

```text
@Autowired is optional
```

So this is enough:

```java
public UserService(OrderService orderService) {
    this.orderService = orderService;
}
```

---

# 🔥 **Field vs Setter vs Constructor Injection**

---

| Type | Best For | Recommended? |
|---|---|---|
| Field Injection | Quick demos | ❌ No |
| Setter Injection | Optional dependency | ⚠️ Sometimes |
| Constructor Injection | Required dependency | ✅ Yes |

---

# 🔥 **Reflection Concept**

---

## What is Reflection?

Reflection is a Java feature that allows a program to inspect and modify classes, fields, methods, and constructors at runtime.

---

## Simple Meaning

```text
Reflection = Java can look inside a class at runtime
```

---

## Example

```java
Class<?> clazz = UserService.class;

Field[] fields = clazz.getDeclaredFields();

for (Field field : fields) {
    System.out.println(field.getName());
}
```

---

# 🔥 **How Spring Uses Reflection**

---

Spring uses reflection heavily for:

```text
Scanning annotations
Creating objects
Injecting dependencies
Calling lifecycle methods
Reading constructors
Accessing private fields
```

---

## Example

```java
@Component
public class UserService {

    @Autowired
    private OrderService orderService;

}
```

Even though `orderService` is private, Spring can inject it.

---

## How?

Spring roughly does:

```java
Field field = UserService.class.getDeclaredField("orderService");
field.setAccessible(true);
field.set(userServiceObject, orderServiceObject);
```

---

## Meaning

```text
Spring breaks normal private access using reflection
and injects dependency internally
```

---

# 🔥 **Reflection in Field Injection**

---

Field injection depends heavily on reflection.

```java
@Autowired
private OrderService orderService;
```

Spring:

```text
Finds private field
Makes it accessible
Injects object
```

---

## That is why field injection is harder to test manually

Because if you write:

```java
UserService userService = new UserService();
```

Spring reflection does not run.

So dependency remains:

```text
null
```

---

# 🔥 **Reflection in Constructor Injection**

---

For constructor injection:

```java
public UserService(OrderService orderService) {
    this.orderService = orderService;
}
```

Spring uses reflection to:

```text
Find constructor
Check constructor parameters
Find matching bean
Call constructor with dependency
```

---

## Rough Internal Flow

```text
Find UserService constructor
        ↓
Constructor needs OrderService
        ↓
Find OrderService bean
        ↓
Call constructor using reflection
        ↓
Create UserService bean
```

---

# 🔥 **Dependency Injection Lifecycle**

---

```text
Application Start
        ↓
IoC Container Starts
        ↓
Bean Created
        ↓
Dependencies Injected
        ↓
@PostConstruct
        ↓
Bean Ready to Use
```

---

# 🔥 **Circular Dependency**

---

## Problem

Circular dependency happens when two beans depend on each other.

---

## Example

```java
@Component
public class OrderService {

    @Autowired
    private InvoiceService invoiceService;
}
```

```java
@Component
public class InvoiceService {

    @Autowired
    private OrderService orderService;
}
```

---

## Problem

```text
OrderService needs InvoiceService
InvoiceService needs OrderService
Spring gets stuck
Application may fail to start
```

---

# 🔥 **How to Fix Circular Dependency**

---

## ✅ Best Solution: Refactor Code

Move common logic into a third class.

```text
OrderService → CommonService
InvoiceService → CommonService
```

---

## ⚠️ Temporary Solution: @Lazy

```java
@Component
public class InvoiceService {

    private final OrderService orderService;

    public InvoiceService(@Lazy OrderService orderService) {
        this.orderService = orderService;
    }
}
```

---

## Meaning

```text
Do not create dependency immediately
Create proxy and initialize later when needed
```

---

# 🔥 **Unsatisfied Dependency**

---

## Problem

Unsatisfied dependency happens when Spring cannot find a bean to inject.

---

## Example

```java
@Component
public class UserService {

    private final OrderService orderService;

    public UserService(OrderService orderService) {
        this.orderService = orderService;
    }
}
```

But `OrderService` implementation is missing `@Component`.

---

## Error

```text
No qualifying bean of type 'OrderService' available
```

---

# 🔥 **Multiple Beans Problem**

---

## Problem

```java
public interface OrderService {
    void createOrder();
}
```

```java
@Component
public class OnlineOrderService implements OrderService {
    public void createOrder() {
        System.out.println("Online order");
    }
}
```

```java
@Component
public class OfflineOrderService implements OrderService {
    public void createOrder() {
        System.out.println("Offline order");
    }
}
```

Now Spring sees:

```text
2 beans of OrderService type
```

Spring gets confused.

---

# 🔥 **Solution 1: @Primary**

---

```java
@Primary
@Component
public class OnlineOrderService implements OrderService {

    public void createOrder() {
        System.out.println("Online order");
    }
}
```

Now Spring chooses this by default.

---

# 🔥 **Solution 2: @Qualifier**

---

```java
@Component
public class UserService {

    private final OrderService orderService;

    public UserService(@Qualifier("offlineOrderService") OrderService orderService) {
        this.orderService = orderService;
    }
}
```

---

## Meaning

```text
@Qualifier tells Spring exactly which bean to inject
```

---

# 🔥 **Real-World Example**

---

## Payment Interface

```java
public interface PaymentService {
    void pay(double amount);
}
```

---

## UPI Payment

```java
@Component
public class UpiPaymentService implements PaymentService {

    public void pay(double amount) {
        System.out.println("Paid using UPI: " + amount);
    }
}
```

---

## Card Payment

```java
@Component
public class CardPaymentService implements PaymentService {

    public void pay(double amount) {
        System.out.println("Paid using Card: " + amount);
    }
}
```

---

## Payment Processor

```java
@Component
public class PaymentProcessor {

    private final PaymentService paymentService;

    public PaymentProcessor(@Qualifier("upiPaymentService") PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void process(double amount) {
        paymentService.pay(amount);
    }
}
```

---

# 🔥 **When Should You Use Dependency Injection?**

---

Use DI when:

```text
One class depends on another class
You want loose coupling
You want easy testing
You want Spring to manage dependencies
You want to follow clean architecture
```

---

# 🔥 **When NOT to Use Dependency Injection**

---

Avoid DI when:

```text
Object is a simple value object
Class has no dependency
Object is temporary/local
You are creating DTOs/entities manually
```

Example:

```java
UserDto userDto = new UserDto();
```

This is okay.

Not every object needs to be a bean.

---

# 🔥 **How to Identify Dependency Injection Problem**

---

Ask:

---

## Question 1

```text
Am I using new keyword to create service/repository/helper class?
```

---

## Question 2

```text
Is my class tightly coupled to concrete implementation?
```

---

## Question 3

```text
Will testing require mocking this dependency?
```

---

## Question 4

```text
Can implementation change in future?
```

---

If yes:

```text
Use Dependency Injection
```

---

# 🔥 **How to Reach Dependency Injection**

---

## Stage 1: Direct Object Creation

```java
OrderService orderService = new OrderService();
```

---

## Stage 2: Problem Appears

```text
Hard to test
Hard to replace
Tight coupling
```

---

## Stage 3: Create Abstraction

```java
interface OrderService {
    void createOrder();
}
```

---

## Stage 4: Let Spring Manage Implementation

```java
@Component
class OnlineOrderService implements OrderService {}
```

---

## Stage 5: Inject Dependency

```java
public UserService(OrderService orderService) {
    this.orderService = orderService;
}
```

---

# 🔥 **Advantages**

---

```text
Loose coupling
Better testing
Cleaner code
Follows Dependency Inversion Principle
Centralized object management
Easier maintenance
```

---

# 🔥 **Disadvantages**

---

```text
Can feel complex initially
Runtime errors if bean missing
Too many beans can confuse beginners
Circular dependency issues
Requires understanding Spring container
```

---

# 🔥 **Common Mistakes**

---

## ❌ Using field injection everywhere

Prefer constructor injection.

---

## ❌ Creating Spring beans manually

```java
UserService userService = new UserService();
```

This bypasses Spring.

---

## ❌ Forgetting @Component

Spring cannot inject a class it does not manage.

---

## ❌ Multiple implementations without @Qualifier

Spring will not know which bean to inject.

---

## ❌ Circular dependency

Avoid classes depending on each other directly.

---

# 🔥 **Interview Questions**

---

## What is Dependency Injection?

```text
Dependency Injection is a technique where dependencies are provided from outside instead of being created inside the class.
```

---

## Why do we use Dependency Injection?

```text
To reduce tight coupling, improve testability, and follow clean architecture.
```

---

## What are the types of DI?

```text
Field Injection
Setter Injection
Constructor Injection
```

---

## Which injection is recommended?

```text
Constructor injection is recommended.
```

---

## Why constructor injection?

```text
It ensures mandatory dependencies are available during object creation and supports final fields.
```

---

## What is @Autowired?

```text
@Autowired tells Spring to inject the required bean automatically.
```

---

## What is @Qualifier?

```text
@Qualifier tells Spring exactly which bean to inject when multiple beans of same type exist.
```

---

## What is @Primary?

```text
@Primary marks one bean as default when multiple candidates are available.
```

---

## What is circular dependency?

```text
When two or more beans depend on each other directly.
```

---

## How does Spring inject private fields?

```text
Spring uses Java Reflection to access private fields and inject dependencies.
```

---

# 🔥 **Memory Trick**

---

```text
DI = Do not create dependency, Inject dependency
```

---

```text
Constructor Injection = Best Practice
Field Injection = Easy but risky
Setter Injection = Optional dependency
```

---

# 🚀 **Final Summary**

---

* Dependency Injection removes tight coupling.
* Spring IoC container creates and manages beans.
* Dependencies are injected automatically.
* Constructor injection is recommended.
* Field injection uses reflection internally.
* Reflection allows Spring to inspect classes and inject private fields.
* @Primary and @Qualifier solve multiple bean confusion.
* @Lazy can help with circular dependency, but refactoring is better.

---

```text
Dependency Injection = Loose Coupling + Better Testing + Clean Architecture
```

---