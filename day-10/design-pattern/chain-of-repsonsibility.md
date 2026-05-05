# ***Chain of Responsibility Design Pattern (CoR)***

---

# 🔥 What is Chain of Responsibility?

---

## 📌 Definition

**Chain of Responsibility (CoR)** is a behavioral design pattern that allows a request to pass through a chain of handlers.  

Each handler decides:

- Whether to process the request  
- Or pass it to the next handler  

👉 This continues until:
- A handler processes it  
- OR the chain ends 

---

## 🧠 Core Idea

```
Request → Handler1 → Handler2 → Handler3 → ...
```

---

## 🎯 Key Concept

- Decouples sender and receiver  
- Each handler works independently  
- Order of processing can be controlled  

---

# 🔥 Real World Analogy

---

## 📞 Tech Support Example

```
User → Auto Bot → Support Agent → Senior Engineer
```

- Bot tries → fails  
- Agent tries → fails  
- Engineer solves  

👉 Request travels through chain until handled  

---

# 🔥 Problem Without CoR

---

## 📌 Scenario: Order Processing System

You need:

- Authentication check  
- Authorization check  
- Data validation  
- Rate limiting  
- Caching  

---

## ❌ Without Pattern

```java
if(authenticated) {
    if(authorised) {
        if(valid) {
            if(notRateLimited) {
                // process request
            }
        }
    }
}
```

---

## 🚨 Problems

- Deep nested code  
- Hard to maintain  
- Not reusable  
- Adding new logic breaks code  

---

# 🔥 Solution Using CoR

---

## 🧠 Idea

Break each logic into separate handlers:

- AuthHandler  
- ValidationHandler  
- RateLimitHandler  
- CacheHandler  

---

## 📌 Flow

```
Request
  ↓
AuthHandler
  ↓
ValidationHandler
  ↓
RateLimitHandler
  ↓
Controller
```

---

# 🔥 Structure

---

## 🔹 1. Handler Interface

Defines common method

---

## 🔹 2. Base Handler (Optional)

- Stores reference to next handler  
- Default forwarding logic  

---

## 🔹 3. Concrete Handlers

- Actual processing logic  
- Decide:
  - Handle or pass  

---

## 🔹 4. Client

- Builds chain  
- Sends request  

---

# 🔥 Implementation (Spring Boot Style)

---

## 💻 Step 1: Handler Interface

```java
public interface Handler {
    void setNext(Handler next);
    void handle(Request request);
}
```

---

## 💻 Step 2: Base Handler

```java
public abstract class BaseHandler implements Handler {

    protected Handler next;

    @Override
    public void setNext(Handler next) {
        this.next = next;
    }

    protected void next(Request request) {
        if (next != null) {
            next.handle(request);
        }
    }
}
```

---

## 💻 Step 3: Concrete Handlers

### 🔹 Authentication Handler

```java
public class AuthHandler extends BaseHandler {

    @Override
    public void handle(Request request) {

        if (!request.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        System.out.println("Auth Passed");
        next(request);
    }
}
```

---

### 🔹 Validation Handler

```java
public class ValidationHandler extends BaseHandler {

    @Override
    public void handle(Request request) {

        if (request.getData() == null) {
            throw new RuntimeException("Invalid data");
        }

        System.out.println("Validation Passed");
        next(request);
    }
}
```

---

### 🔹 Rate Limit Handler

```java
public class RateLimitHandler extends BaseHandler {

    @Override
    public void handle(Request request) {

        if (request.isRateLimited()) {
            throw new RuntimeException("Too many requests");
        }

        System.out.println("Rate Limit Passed");
        next(request);
    }
}
```

---

## 💻 Request Object

```java
public class Request {

    private boolean authenticated;
    private boolean rateLimited;
    private String data;

    // getters & setters
}
```

---

## 💻 Step 4: Client (Build Chain)

```java
public class Client {

    public static void main(String[] args) {

        Handler auth = new AuthHandler();
        Handler validation = new ValidationHandler();
        Handler rateLimit = new RateLimitHandler();

        auth.setNext(validation);
        validation.setNext(rateLimit);

        Request request = new Request();
        request.setAuthenticated(true);
        request.setData("Sample Data");
        request.setRateLimited(false);

        auth.handle(request);
    }
}
```

---

## 🧠 Output

```
Auth Passed
Validation Passed
Rate Limit Passed
```

---

# 🔥 Alternative Approach

---

## 📌 Single Handler Processes

Sometimes:

- Only ONE handler processes request  
- Others are skipped  

Example:

```
Event → Button → Panel → Dialog
```

👉 First capable handler handles it  

---

# 🔥 Real World Use Cases

---

- Spring Security filter chain  
- Logging pipeline  
- Exception handling chain  
- Middleware systems  
- API gateway processing  

---

# 🔥 Advantages

---

- Loose coupling  
- Easy to extend  
- Follows Single Responsibility Principle  
- Order control  

---

# 🔥 Disadvantages

---

- Request may go unhandled  
- Debugging becomes harder  
- Performance overhead (long chains)  

---

# 🔥 When to Use

---

Use when:

- Multiple handlers needed  
- Order matters  
- Logic may change dynamically  
- You want reusable components  

---

# 🔥 Comparison with Other Patterns

---

## CoR vs Decorator

| CoR | Decorator |
|---|---|
| Can stop chain | Cannot stop flow |
| Independent handlers | Adds behavior |
| Conditional execution | Always executes |

---

## CoR vs Observer

| CoR | Observer |
|---|---|
| Sequential processing | Broadcast |
| One handler may process | All observers notified |

---

# 🎯 Interview Questions

---

## 🔹 What is Chain of Responsibility?

A pattern where request is passed through multiple handlers until processed.

---

## 🔹 Key benefit?

Decouples sender from receiver.

---

## 🔹 When to stop chain?

- When request is handled  
- Or explicitly stopped  

---

## 🔹 Real world example?

Spring Security filter chain  

---

## 🔹 Can order change?

Yes, dynamically at runtime  

---

# 🚀 Final Summary

---

```
CoR = Request flows through chain of handlers
Each handler decides → process or pass
Clean, flexible, scalable design
```