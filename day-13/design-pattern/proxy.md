# ***Proxy Design Pattern***

---

# 🔥 What is Proxy Design Pattern?

---

## 📌 Definition

Proxy is a structural design pattern that provides a placeholder or substitute object for another real object. A proxy controls access to the original object, allowing you to perform something either before or after the request gets through to the original object.

---

## 🧠 Core Idea

Instead of directly accessing the real object:

```text
Client → Proxy → Real Object
```

The proxy controls access to the original object.

---

# 🔥 Why Do We Need Proxy?

---

## 📌 Problem

Sometimes real objects are:

- Heavyweight
- Expensive to create
- Remote
- Sensitive
- Slow
- Need security/logging/caching

---

## 🧠 Example

```text
Database query is expensive
```

Without proxy:

```text
Every request → DB hit
```

This causes:

- Slow performance
- High resource usage

---

# 🔥 Solution

---

Proxy sits between:

```text
Client ↔ Real Object
```

It can:

- Cache
- Validate
- Secure
- Log
- Delay object creation

Before delegating to actual object.

---

# 🔥 Real-Life Analogy

---

## 💳 Credit Card Example

```text
Credit Card = Proxy
Bank Account = Real Object
```

You use card instead of directly handling cash.

---

# 🔥 Structure of Proxy Pattern

---

## 📌 Flow Diagram

```text
                ┌──────────────┐
                │    Client    │
                └──────┬───────┘
                       │
                       ▼
              ┌────────────────┐
              │     Proxy      │
              └──────┬─────────┘
                     │
                     ▼
              ┌────────────────┐
              │  Real Service  │
              └────────────────┘
```

---

# 🔥 Components

---

## 1️⃣ Service Interface

Common contract for:

- Proxy
- Real Service

---

## 2️⃣ Real Service

Actual business logic.

---

## 3️⃣ Proxy

Controls access before delegating.

---

## 4️⃣ Client

Uses interface without knowing whether object is real or proxy.

---

# 🔥 Simple Java Example

---

# 📌 Step 1: Interface

```java
public interface PaymentService {

    void pay();
}
```

---

# 📌 Step 2: Real Object

```java
public class RealPaymentService implements PaymentService {

    @Override
    public void pay() {
        System.out.println("Payment processed");
    }
}
```

---

# 📌 Step 3: Proxy

```java
public class PaymentProxy implements PaymentService {

    private RealPaymentService realPaymentService;

    @Override
    public void pay() {

        System.out.println("Checking access...");

        if(realPaymentService == null) {
            realPaymentService = new RealPaymentService();
        }

        realPaymentService.pay();

        System.out.println("Logging payment...");
    }
}
```

---

# 📌 Step 4: Client

```java
public class Main {

    public static void main(String[] args) {

        PaymentService service = new PaymentProxy();

        service.pay();
    }
}
```

---

# 🔥 Output

---

```text
Checking access...
Payment processed
Logging payment...
```

---

# 🔥 Internal Flow

---

```text
Client calls Proxy
        ↓
Proxy performs extra work
        ↓
Proxy delegates to Real Object
        ↓
Result returned
```

---

# 🔥 Types of Proxy

---

# 📌 1️⃣ Virtual Proxy

---

## 🧠 Purpose

Lazy initialization.

---

## 📌 Use Case

Heavy object creation delayed until needed.

---

## 💻 Example

```text
Large image loading
PDF loading
Video loading
```

---

# 📌 2️⃣ Protection Proxy

---

## 🧠 Purpose

Access control.

---

## 📌 Use Case

Authorization checks.

---

## 💻 Example

```text
Admin APIs
Role-based access
```

---

# 📌 3️⃣ Caching Proxy

---

## 🧠 Purpose

Avoid repeated expensive calls.

---

## 📌 Use Case

Database/API caching.

---

## 💻 Example

```text
Same API request
        ↓
Return cached result
```

---

# 📌 4️⃣ Logging Proxy

---

## 🧠 Purpose

Track requests.

---

## 📌 Use Case

Audit logs.

---

---

# 📌 5️⃣ Remote Proxy

---

## 🧠 Purpose

Handle remote service communication.

---

## 📌 Use Case

Microservices / RPC.

---

# 🔥 Caching Proxy Flow

---

```text
Client Request
        ↓
Check Cache
   ↓         ↓
Hit         Miss
 ↓            ↓
Return      Call Real Service
Cache       Store Result
```

---

# 🔥 Advantages

---

## ✅ Pros

- Lazy initialization
- Security layer
- Logging support
- Caching support
- Open/Closed Principle
- No modification in real object

---

# 🔥 Disadvantages

---

## ❌ Cons

- More classes
- Added complexity
- Extra indirection

---

# 🔥 Proxy vs Decorator

---

| Feature | Proxy | Decorator |
|---|---|---|
| Purpose | Control access | Add behavior |
| Focus | Access management | Feature enhancement |
| Real object creation | Often managed | Client manages |

---

# 🔥 Proxy vs Adapter

---

| Feature | Proxy | Adapter |
|---|---|---|
| Interface | Same | Different |
| Purpose | Control access | Compatibility |

---

# 🔥 Proxy in Spring Boot 

---

# 📌 Spring AOP Uses Proxy Internally

---

## 🧠 Core Idea

Spring creates proxy objects for beans.

---

## 📌 Flow

```text
Client
   ↓
Spring Proxy
   ↓
Actual Bean
```

---

# 🔥 Example

---

## 💻 Code

```java
@Service
public class PaymentService {

    @Transactional
    public void processPayment() {
        System.out.println("Payment Done");
    }
}
```

---

## 🧠 What Spring Does Internally

```text
Creates Proxy for PaymentService
        ↓
Proxy handles:
- Transaction start
- Method execution
- Commit/Rollback
```

---

# 🔥 Spring Features Using Proxy

---

| Feature | Uses Proxy |
|---|---|
| @Transactional | ✅ |
| @Async | ✅ |
| AOP | ✅ |
| Security | ✅ |
| Lazy Loading | ✅ |

---

# 🔥 Why Self Invocation Fails in Spring?

---

## 💻 Example

```java
public void method1() {
    method2();
}

@Transactional
public void method2() {}
```

---

## 🚨 Problem

```text
Internal call bypasses proxy
```

---

## 🧠 Correct Flow

```text
External Call
    ↓
Proxy
    ↓
Transactional Logic
```

---

# 🔥 Dynamic Proxy vs Static Proxy

---

| Type | Meaning |
|---|---|
| Static Proxy | Manually created |
| Dynamic Proxy | Created at runtime |

---

# 🔥 Spring Uses

---

## 📌 JDK Dynamic Proxy

Used when interface exists.

---

## 📌 CGLIB Proxy

Used when no interface exists.

---

# 🔥 Interview Questions

---

## 🔹 What is Proxy Pattern?

A placeholder object that controls access to another object.

---

## 🔹 Why use Proxy?

Security, logging, caching, lazy loading.

---

## 🔹 Difference between Proxy and Decorator?

Proxy controls access, Decorator adds behavior.

---

## 🔹 How Spring uses Proxy?

Spring AOP creates proxies around beans.

---

## 🔹 Why @Transactional fails on self-invocation?

Because proxy is bypassed.

---

## 🔹 Difference between JDK Proxy and CGLIB?

JDK → interface-based  
CGLIB → subclass-based

---

# 🚀 Final Summary

---

```text
Proxy = Middle layer between client and real object

Client
   ↓
Proxy
   ↓
Real Object
```

---

## 🧠 Key Uses

```text
Security
Caching
Logging
Lazy loading
Transactions
AOP
```

---

# 🔥 Memory Trick

---

```text
Proxy = Control Access
Decorator = Add Features
Adapter = Convert Interface
```