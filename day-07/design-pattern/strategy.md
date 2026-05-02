# ***Strategy Design Pattern***

---

# 🔥 What is Strategy Design Pattern

---

## 📌 Definition

Strategy is a **behavioral design pattern** that allows you to define a **family of algorithms**, put each of them into separate classes, and make them interchangeable at runtime.

---

## 🧠 Core Idea

```text
Same task → Multiple ways to execute
```

---

```text
Context → Strategy → Executes behavior
```

---

## 🧠 Simple Meaning

```text
Strategy = Replace if-else with interchangeable behaviors
```

---

# 🔥 Why Do We Need Strategy Pattern

---

## 📌 Problem

Consider a payment system:

```java
public void pay(String type) {

    if (type.equals("UPI")) {
        // UPI logic
    } else if (type.equals("CARD")) {
        // Card logic
    } else if (type.equals("PAYPAL")) {
        // PayPal logic
    }
}
```

---

## 🚨 Issues

```text
Too many if-else conditions
Hard to maintain
Difficult to extend
Violates Open/Closed Principle
```

---

## 📌 Real Example

A navigation system supports:

```text
Car route
Walking route
Bike route
Public transport route
```

Initially simple, but later becomes complex and bloated as more routes are added. 

---

# 🔥 Strategy Solution

---

## 🧠 Idea

```text
Move each behavior into its own class
```

---

Instead of:

```text
One class → many if-else
```

Do:

```text
Many classes → one behavior each
```

---

# 🔥 Structure

---

```text
Strategy Interface → Common behavior

Concrete Strategies → Different implementations

Context → Uses strategy

Client → Chooses strategy
```

---

# 🔥 Example Implementation

---

## Step 1 Strategy Interface

```java
public interface PaymentStrategy {
    void pay(double amount);
}
```

---

## Step 2 Concrete Strategies

```java
public class UpiPayment implements PaymentStrategy {

    public void pay(double amount) {
        System.out.println("Paid ₹" + amount + " using UPI");
    }
}
```

---

```java
public class CardPayment implements PaymentStrategy {

    public void pay(double amount) {
        System.out.println("Paid ₹" + amount + " using Card");
    }
}
```

---

```java
public class PaypalPayment implements PaymentStrategy {

    public void pay(double amount) {
        System.out.println("Paid ₹" + amount + " using PayPal");
    }
}
```

---

## Step 3 Context

```java
public class PaymentContext {

    private PaymentStrategy strategy;

    public PaymentContext(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void execute(double amount) {
        strategy.pay(amount);
    }
}
```

---

## Step 4 Client

```java
public class Main {

    public static void main(String[] args) {

        PaymentStrategy strategy = new UpiPayment();

        PaymentContext context = new PaymentContext(strategy);

        context.execute(1000);
    }
}
```

---

# 🔥 How This Works

---

## Flow

```text
Client selects strategy
        ↓
Context stores strategy
        ↓
Context delegates execution to strategy
```

---

## 🧠 Important

```text
Context does NOT know which strategy it is using
```

---

# 🔥 Real-World Analogy

---

## Travel to Airport

```text
Bus
Cab
Bike
Train
```

---

## 🧠 Meaning

```text
Goal = Reach airport
Strategies = Different ways to reach
```

---

# 🔥 Key Insight

---

```text
Strategy changes behavior, not object
```

---

# 🔥 Strategy vs Factory

---

| Feature | Strategy         | Factory         |
| ------- | ---------------- | --------------- |
| Focus   | Behavior         | Object creation |
| Purpose | Choose algorithm | Create object   |
| Example | Payment methods  | Payment factory |

---

## 🧠 Understanding

```text
Factory → Which object?

Strategy → How behavior works?
```

---

# 🔥 Strategy in Spring Boot

---

## Example Using Map

```java
@Component
public class PaymentFactory {

    private final Map<String, PaymentStrategy> map = new HashMap<>();

    public PaymentFactory(List<PaymentStrategy> strategies) {
        for (PaymentStrategy strategy : strategies) {
            map.put(strategy.getClass().getSimpleName().toUpperCase(), strategy);
        }
    }

    public PaymentStrategy get(String type) {
        return map.get(type.toUpperCase());
    }
}
```

---

## Usage

```java
PaymentStrategy strategy = factory.get("UPI");
strategy.pay(1000);
```

---

## 🧠 Meaning

```text
Strategy + Map = Dynamic behavior selection
```

---

# 🔥 When to Use Strategy Pattern

---

Use when:

```text
Multiple ways to perform same task
Want to remove if-else logic
Need runtime behavior selection
```

---

# 🔥 When NOT to Use

---

Avoid when:

```text
Only one behavior exists
Logic is simple
No runtime variation needed
```

---

# 🔥 Advantages

---

```text
Removes if-else complexity
Follows Open/Closed Principle
Easy to extend
Loose coupling
```

---

# 🔥 Disadvantages

---

```text
Too many classes
Client must choose correct strategy
Slight complexity increase
```

---

# 🔥 Common Mistakes

---

```text
Using strategy for simple logic
Not abstracting behavior properly
Still using if-else inside strategies
```

---

# 🔥 Interview Questions

---

## What is Strategy Pattern

```text
A pattern that allows selecting an algorithm at runtime
```

---

## Why use Strategy

```text
To remove conditional logic and make behavior flexible
```

---

## Strategy vs State

```text
Strategy → chosen by client

State → changes internally
```

---

# 🔥 Memory Trick

---

```text
Strategy = Same work, different ways
```

---

# 🚀 Final Summary

---

* Encapsulates different behaviors
* Removes conditional logic
* Allows runtime flexibility
* Promotes clean and maintainable code

---

```text
Strategy = Replace if-else with polymorphism
```

---
