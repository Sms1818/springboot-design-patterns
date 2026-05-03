# 🚀 Async Annotation in Spring Boot – Part 2

---

# 🔥 Conditions for @Async to Work Properly

---

## 📌 1. Different Class 

### ❌ Wrong

```java
@Service
public class UserService {

    public void method1() {
        asyncMethod(); // self-invocation
    }

    @Async
    public void asyncMethod() {
        System.out.println("Async");
    }
}
```

### 🚨 Problem

* Proxy is bypassed
* Async will NOT work

### 🧠 Why?

* Spring AOP works using proxies
* Internal method calls are NOT intercepted

---

### ✅ Correct

```java
@Service
public class UserService {

    private final AsyncService asyncService;

    public void method1() {
        asyncService.asyncMethod(); // proxy call
    }
}
```

### 🧠 Rule

@Async method must be called from another Spring bean

---

## 🔥 2. Method Must Be Public

### ❌ Wrong

```java
@Async
private void asyncMethod() {}
```

### 🚨 Problem

* AOP proxy works only on public methods

---

### ✅ Correct

```java
@Async
public void asyncMethod() {}
```

---

# 🔥 Async and Transaction Management

---

## 📌 Core Concept

Transaction context does NOT propagate to async thread

### 🧠 Why?

* Transaction is thread-bound
* Async creates NEW thread

---

## 🔥 Use Case 1 (❌ Wrong Design)

```java
@Transactional
public void updateUser() {
    repo.update();
    asyncService.updateBalance(); // async call
}
```

### 🚨 Problem

* Transaction does NOT apply to async method
* Inconsistency possible

---

## 🔥 Use Case 2 (⚠️ Risky)

```java
@Transactional
@Async
public void updateUser() {
    // update logic
}
```

### 🚨 Problem

* New thread = new transaction
* Propagation will not behave as expected

---

## 🔥 Use Case 3 (✅ Correct Design)

```java
@Service
public class UserService {

    @Async
    public void updateUser() {
        userUtility.updateUser(); // separate transactional method
    }
}

@Service
public class UserUtility {

    @Transactional
    public void updateUser() {
        // DB updates here
    }
}
```

### ✅ Why Correct?

* Async → handles threading
* Transactional → handles DB consistency
* Separation of concerns

---

# 🔥 Async Method Return Types

---

## 📌 Supported Types

* void
* Future
* CompletableFuture

---

## 🔹 Using Future

### Service

```java
@Async
public Future<String> performTask() {
    return new AsyncResult<>("Task Done");
}
```

### Controller

```java
Future<String> result = service.performTask();
String output = result.get(); // blocking
```

### 🧠 Important

* get() blocks the thread

---

## 🔥 Future Methods

| Method       | Purpose               |
| ------------ | --------------------- |
| cancel()     | Cancel task           |
| isDone()     | Check completion      |
| get()        | Wait and fetch result |
| get(timeout) | Wait with timeout     |

---

## 🔹 Using CompletableFuture

### 📌 Definition

Advanced version of Future with chaining support

### Service

```java
@Async
public CompletableFuture<String> performTask() {
    return CompletableFuture.completedFuture("Task Done");
}
```

### Controller

```java
CompletableFuture<String> result = service.performTask();
String output = result.get();
```

### 🧠 Advantage

* Non-blocking chaining
* Better async control

---

# 🔥 Exception Handling in Async

---

## 🔹 Case 1: Method with Return Type

```java
CompletableFuture<String> future = service.method();
future.get(); // exception thrown here
```

### ✅ Handling

```java
future.exceptionally(ex -> {
    System.out.println(ex.getMessage());
    return "Fallback";
});
```

---

## 🔹 Case 2: Void Method

### 🚨 Problem

* Exception is NOT propagated to caller

### ❌ Example

```java
@Async
public void method() {
    int x = 10 / 0; // exception
}
```

### 🧠 Result

* Controller will NOT know

---

## 🔹 Solution 1: Handle Inside Method

```java
@Async
public void method() {
    try {
        int x = 10 / 0;
    } catch (Exception e) {
        System.out.println("Handled");
    }
}
```

---

## 🔹 Solution 2: AsyncUncaughtExceptionHandler

### 📌 Definition

Handles exceptions from void async methods globally

### Configuration

```java
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> {
            System.out.println("Exception in method: " + method.getName());
        };
    }
}
```

### 🧠 Flow

Async Exception → Handler → Log/Handle

---

## 🔥 Default Behavior

If not handled:

* Spring uses SimpleAsyncUncaughtExceptionHandler

### Internal Behavior

* Logs error only
* Does NOT stop application

---

## 🔥 Final Rules (VERY IMPORTANT)

* Async works via proxy
* Self-invocation fails
* Only public methods supported
* Transaction does NOT propagate
* Void async exceptions are silent
* Use CompletableFuture for better control

---

## 🧠 Memory Tricks

Async = Proxy + Thread + Executor
Transaction = Thread-bound
Async + Transaction = Separate contexts

---

## 🚀 Final Summary

* Async runs method in new thread
* Proxy is mandatory for execution
* Self-invocation breaks async
* Transaction does not flow to async thread
* Future blocks, CompletableFuture is better
* Void async exceptions are lost unless handled
* Use AsyncUncaughtExceptionHandler for global handling
* Keep async and transactional logic separated
