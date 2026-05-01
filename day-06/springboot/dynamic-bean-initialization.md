# ***Dynamic Bean Initialization***

---

# 🔥 **What is the Problem?**

---

## 📌 Scenario

You have an interface:

```java
public interface Order {
    void createOrder();
}
```

And multiple implementations:

```java
@Component
public class OnlineOrder implements Order {}

@Component
public class OfflineOrder implements Order {}
```

---

## ❌ Problem in Controller

```java
@RestController
public class UserController {

    private final Order order;

    public UserController(Order order) {
        this.order = order;
    }
}
```

---

## 🚨 What is Wrong?

```text
Multiple beans of type Order exist
Spring gets confused → which one to inject?
```

---

## 💥 Error

```text
UnsatisfiedDependencyException
```

---


---

# 🔥 **Solution 1: @Qualifier**

---

## Fix

```java
public UserController(@Qualifier("onlineOrder") Order order) {
    this.order = order;
}
```

---

## 🧠 Meaning

```text
Explicitly tell Spring which bean to inject
```

---

## ❌ Problem

```text
Tightly coupled
Hardcoded selection
Not scalable
```

---

# 🔥 **Solution 2: Conditional Bean (@Bean + @Value)**

---

## Example

```java
@Bean
public Order createOrder(@Value("${isOnlineOrder}") boolean isOnlineOrder) {

    if (isOnlineOrder) {
        return new OnlineOrder();
    } else {
        return new OfflineOrder();
    }
}
```

---


---

## 🧠 Meaning

```text
Decision based on configuration
```

---

## ❌ Problem

```text
Still uses if-else ❌
Not extensible ❌
Violates Open/Closed Principle ❌
```

---

# 🔥 **Final Solution: Strategy Pattern + Map (BEST APPROACH)**

---

# 🎯 Core Idea

```text
Remove if-else
Use Map for dynamic selection
Each class decides its own identity
```

---

# 🧠 Design Pattern Used

👉 **Strategy Pattern**

```text
Different behaviors (Order types)
→ encapsulated in different classes
→ selected at runtime
```

---

# 🔥 **Implementation**

---

## Step 1: Interface

```java
public interface Order {
    void createOrder();
    String getType();
}
```

---

## Step 2: Implementations

```java
@Component
public class OnlineOrder implements Order {

    @Override
    public void createOrder() {
        System.out.println("Creating Online Order");
    }

    @Override
    public String getType() {
        return "ONLINE";
    }
}
```

---

```java
@Component
public class OfflineOrder implements Order {

    @Override
    public void createOrder() {
        System.out.println("Creating Offline Order");
    }

    @Override
    public String getType() {
        return "OFFLINE";
    }
}
```

---

# 🔥 Step 3: Strategy Map (Factory)

---

```java
@Component
public class OrderFactory {

    private final Map<String, Order> orderMap = new HashMap<>();

    public OrderFactory(List<Order> orders) {
        for (Order order : orders) {
            orderMap.put(order.getType(), order);
        }
    }

    public Order getOrder(String type) {
        return orderMap.get(type.toUpperCase());
    }
}
```

---

# 🔥 Step 4: Controller

---

```java
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderFactory factory;

    public OrderController(OrderFactory factory) {
        this.factory = factory;
    }

    @PostMapping("/{type}")
    public String createOrder(@PathVariable String type) {
        Order order = factory.getOrder(type);
        order.createOrder();
        return "Order Created";
    }
}
```

---

# 🔥 **How This Works**

---

## Step-by-Step Flow

```text
Application starts
        ↓
Spring finds all @Component Order beans
        ↓
Creates:
    OnlineOrder
    OfflineOrder
        ↓
Injects List<Order> into factory
        ↓
Factory builds Map:
    ONLINE → OnlineOrder
    OFFLINE → OfflineOrder
```

---

## Runtime

```text
User sends: /orders/ONLINE
        ↓
Factory.getOrder("ONLINE")
        ↓
Returns OnlineOrder
        ↓
createOrder() executed
```

---

# 🔥 **Why This is Best**

---

## ✅ No if-else

```text
Clean code
```

---

## ✅ Open/Closed Principle

```text
Add new Order type → no changes needed
```

---

## ✅ Loose Coupling

```text
Controller doesn't know implementations
```

---

## ✅ Scalable

```text
Just add new class
```

---

# 🔥 **Relation with Factory Method**

---

```text
Factory handles object selection
Strategy handles behavior
```

---

## 🧠 Combined Understanding

```text
Factory + Strategy = Dynamic behavior selection
```

---

# 🔥 **When to Use This Pattern**

---

Use when:

```text
Multiple implementations exist
Selection depends on runtime input
You want to avoid if-else
System should be extensible
```

---

# 🔥 **When NOT to Use**

---

Avoid when:

```text
Only one implementation exists
Logic is simple
No runtime variation needed
```

---

# 🔥 **Common Mistakes**

---

## ❌ Using if-else instead of Map

---

## ❌ Not using getType()

---

## ❌ Hardcoding values

---

## ❌ Not handling invalid type

---

# 🔥 **Interview Questions**

---

## 🔹 What problem does this solve?

```text
Multiple bean injection ambiguity + dynamic behavior selection
```

---

## 🔹 Why use Map?

```text
Provides O(1) lookup and removes conditional logic
```

---

## 🔹 Which design pattern is this?

```text
Strategy Pattern + Factory Method
```

---

## 🔹 Why better than @Qualifier?

```text
No hardcoding, dynamic selection, scalable
```

---

# 🔥 **Memory Trick**

---

```text
Qualifier → Hardcoded selection ❌

If-Else → Conditional logic ❌

Map + Strategy → Dynamic & scalable ✅
```

---

# 🚀 **Final Summary**

---

* Multiple beans cause injection conflict
* @Qualifier solves but not scalable
* @Bean + @Value works but uses if-else
* Strategy Pattern + Map is best solution
* Spring injects all implementations automatically
* Factory builds map once at startup
* Runtime selection becomes fast and clean

---

```text
Best Practice = Strategy Pattern + Map + Spring DI
```

---
