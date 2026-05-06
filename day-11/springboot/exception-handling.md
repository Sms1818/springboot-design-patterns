# ***Exception Handling in Spring Boot***

---

# 🔥 What is Exception Handling?

---

## 📌 Definition

Exception Handling is the mechanism of handling runtime errors so that:
- Application does not crash  
- Proper HTTP response is returned  
- Error details are controlled  

---

## 🧠 Core Idea

```
Exception occurs → Spring processes → Response returned
```

---

# 🔥 Default Behavior (IMPORTANT)

---

## 📌 Without Handling

```java
@GetMapping("/get-user")
public String getUser() {
    throw new NullPointerException("Test exception");
}
```

---

## 🚨 Output

- Status → 500 (Internal Server Error)  
- Message → Default (not your custom message)  

---

## 🧠 Why?

Because Spring internally handles exceptions using a **resolver chain**.

---

# 🔥 Internal Exception Flow (VERY IMPORTANT)

---

## 📌 Flow Diagram

```
Exception Thrown
        ↓
DispatcherServlet
        ↓
ExceptionHandlerExceptionResolver
        ↓
ResponseStatusExceptionResolver
        ↓
DefaultHandlerExceptionResolver
        ↓
DefaultErrorAttributes
        ↓
HTTP Response Sent
```

---

## 🧠 Explanation

- Spring tries multiple resolvers in order  
- First matching resolver handles the exception  
- If none match → default response is generated  

---

# 🔥 Why Custom Exception Not Working?

---

## ❌ Example

```java
throw new CustomException(HttpStatus.BAD_REQUEST, "UserID missing");
```

---

## 🚨 Problem

Still returns:
- Status → 500  
- Message → Default  

---

## 🧠 Reason

- You are NOT returning `ResponseEntity`  
- Spring doesn’t know how to map your exception  
- Default handler generates response  

---

# 🔥 Manual Handling (NOT RECOMMENDED)

---

```java
@GetMapping("/get-user")
public ResponseEntity<?> getUser() {
    try {
        throw new CustomException(HttpStatus.BAD_REQUEST, "UserID missing");
    } 
    catch (CustomException e) {
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    } 
    catch (Exception e) {
        return new ResponseEntity<>("Internal Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

---

## ❌ Problem

- Repetitive  
- Not scalable  
- Poor design  

---

# 🔥 Custom Exception Class

---

```java
public class CustomException extends RuntimeException {

    private HttpStatus status;
    private String message;

    public CustomException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
```

---

# 🔥 Proper Way: @ExceptionHandler

---

## 📌 Controller Level Handling

```java
@RestController
@RequestMapping("/api/")
public class UserController {

    @GetMapping("/get-user")
    public ResponseEntity<?> getUser() {
        throw new CustomException(HttpStatus.BAD_REQUEST, "UserID missing");
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
}
```

---

## 🧠 Key Points

- Handles specific exception  
- Applies only to that controller  
- Cleaner than try-catch  

---

# 🔥 Multiple Exception Handling

---

## 📌 Separate Handlers

```java
@ExceptionHandler(CustomException.class)
public ResponseEntity<String> handleCustom(CustomException ex) {
    return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
}

@ExceptionHandler(IllegalArgumentException.class)
public ResponseEntity<String> handleIllegal(IllegalArgumentException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
}
```

---

## 📌 Single Handler

```java
@ExceptionHandler({CustomException.class, IllegalArgumentException.class})
public ResponseEntity<String> handleAll(Exception ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
}
```

---

# 🔥 Custom Error Response

---

```java
public class ErrorResponse {

    private Date timestamp;
    private String message;
    private int status;

    public ErrorResponse(Date timestamp, String message, int status) {
        this.timestamp = timestamp;
        this.message = message;
        this.status = status;
    }
}
```

---

```java
@ExceptionHandler(CustomException.class)
public ResponseEntity<Object> handleCustom(CustomException ex) {

    ErrorResponse error = new ErrorResponse(
        new Date(),
        ex.getMessage(),
        ex.getStatus().value()
    );

    return new ResponseEntity<>(error, ex.getStatus());
}
```

---

# 🔥 Global Exception Handling (IMPORTANT)

---

## 💻 @ControllerAdvice

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustom(CustomException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntime(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

---

## 🧠 Priority

```
Controller Level Handler > Global Handler
```

---

# 🔥 Exception Resolver Types

---

## 1️⃣ ExceptionHandlerExceptionResolver

- Handles:
  - @ExceptionHandler  
  - @ControllerAdvice  

---

## 2️⃣ ResponseStatusExceptionResolver

---

## 📌 What It Handles

`ResponseStatusExceptionResolver` handles exceptions annotated with `@ResponseStatus`.

It is mainly used when we want to map an exception directly to an HTTP status code.

---

# 🔥 Use Case 1: @ResponseStatus on Custom Exception Class

---

## 💻 Example

```java
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomException extends RuntimeException {

    public CustomException(String message) {
        super(message);
    }
}
```

```java
@RestController
@RequestMapping("/api/")
public class UserController {

    @GetMapping("/get-user")
    public ResponseEntity<?> getUser() {
        throw new CustomException("UserID is missing");
    }
}
```

---

## 🧠 Output

```text
HTTP Status → 400 Bad Request
```

---

## ⚠️ Important

The status code comes from `@ResponseStatus`.

---

# 🔥 Use Case 2: @ResponseStatus with reason

---

## 💻 Example

```java
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid Request Passed")
public class CustomException extends RuntimeException {

    public CustomException(String message) {
        super(message);
    }
}
```

```java
@RestController
@RequestMapping("/api/")
public class UserController {

    @GetMapping("/get-user")
    public ResponseEntity<?> getUser() {
        throw new CustomException("UserID is missing");
    }
}
```

---

## 🧠 Output

```text
HTTP Status → 400 Bad Request
Reason      → Invalid Request Passed
```

---

## ⚠️ Important

When `reason` is provided, Spring may send the reason message in the error response depending on error configuration.

---

# 🔥 Use Case 3: @ResponseStatus on @ExceptionHandler Method

---

## 💻 Example

```java
@RestController
@RequestMapping("/api/")
public class UserController {

    @GetMapping("/get-user")
    public ResponseEntity<?> getUser() {
        throw new CustomException("UserID is missing");
    }

    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleCustomException(CustomException ex) {
        return ex.getMessage();
    }
}
```

---

## 🧠 Important

Here, exception is handled by:

```text
ExceptionHandlerExceptionResolver
```

Not by:

```text
ResponseStatusExceptionResolver
```

Because `@ExceptionHandler` gets priority.

---

# 🔥 Use Case 4: @ExceptionHandler Returning ResponseEntity + @ResponseStatus Together

---

## ❌ Confusing Example

```java
@ExceptionHandler(CustomException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)
public ResponseEntity<String> handleCustomException(CustomException ex) {
    return new ResponseEntity<>("Forbidden response", HttpStatus.FORBIDDEN);
}
```

---

## 🚨 Problem

There are two status codes:

```text
@ResponseStatus → 400 BAD_REQUEST
ResponseEntity  → 403 FORBIDDEN
```

---

## 🧠 Result

`ResponseEntity` status usually becomes the final response status because it directly builds the response.

---

## ✅ Best Practice

Do NOT mix `@ResponseStatus` and `ResponseEntity` in the same handler.

Choose one approach.

---

# 🔥 Use Case 5: @ExceptionHandler + @ResponseStatus with void Method

---

## 💻 Example

```java
@ExceptionHandler(CustomException.class)
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid Request Sent")
public void handleCustomException(CustomException ex) {
    // no response body
}
```

---

## 🧠 Output

```text
HTTP Status → 400 Bad Request
Reason      → Invalid Request Sent
Body        → Default error response
```

---

## ✅ When This Makes Sense

Use this when:
- You only want to set status
- You do not want custom response body

---

# 🔥 Use Case 6: @ExceptionHandler Manually Sending Error

---

## 💻 Example

```java
@ExceptionHandler(CustomException.class)
public void handleCustomException(CustomException ex,
                                  HttpServletResponse response)
        throws IOException {

    response.sendError(HttpStatus.FORBIDDEN.value(), "You are not authorized");
}
```

---

## 🧠 Output

```text
HTTP Status → 403 Forbidden
Message     → You are not authorized
```

---

## ⚠️ Important

Here you are manually controlling the response using `HttpServletResponse`.

---

# 🔥 ResponseStatusExceptionResolver Summary

---

| Use Case | Resolver / Behavior |
|---|---|
| `@ResponseStatus` on Exception class | ResponseStatusExceptionResolver |
| `@ResponseStatus(reason = "...")` | ResponseStatusExceptionResolver |
| `@ResponseStatus` on `@ExceptionHandler` | ExceptionHandlerExceptionResolver |
| `@ExceptionHandler` returns ResponseEntity | ResponseEntity controls response |
| `@ExceptionHandler` void + @ResponseStatus | Status comes from @ResponseStatus |
| `response.sendError()` | Manual response control |

---

## 🧠 Memory Trick

```text
@ResponseStatus on Exception class → ResponseStatusExceptionResolver
@ResponseStatus on ExceptionHandler → ExceptionHandlerExceptionResolver
ResponseEntity → Direct response control
```

---

## 3️⃣ DefaultHandlerExceptionResolver

Handles:

- Spring internal exceptions  
- Example:
  - MethodNotAllowed  
  - ResourceNotFound  

---

# 🔥 @ResponseStatus

---

## 📌 On Class

```java
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomException extends RuntimeException {}
```

---

## 📌 With Reason

```java
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid Request")
```

---

## ⚠️ Important Rule

- If used with @ExceptionHandler → handler takes priority  
- Avoid mixing unnecessarily  

---

# 🔥 DefaultErrorAttributes

---

## 📌 When Used?

- No handler found  
- Exception not handled  

---

## 🧠 Behavior

- Generates default JSON response  
- Adds:
  - timestamp  
  - status  
  - error  

---

# 🔥 application.properties Trick

---

```properties
server.error.include-message=always
```

👉 Ensures error message is visible in response  

---

# 🔥 Best Practices

---

- Use @ControllerAdvice for global handling  
- Use custom exception classes  
- Return structured response (DTO)  
- Avoid try-catch in controllers  
- Avoid mixing multiple mechanisms  

---

# 🔥 When to Use What?

---

| Scenario | Approach |
|---|---|
| Simple APIs | Default |
| Custom error message | @ExceptionHandler |
| Large apps | @ControllerAdvice |
| Static mapping | @ResponseStatus |

---

# 🎯 Interview Questions

---

## 🔹 What is Exception Handling?

Mechanism to handle runtime errors and return proper HTTP response.

---

## 🔹 What are Exception Resolvers?

- ExceptionHandlerExceptionResolver  
- ResponseStatusExceptionResolver  
- DefaultHandlerExceptionResolver  

---

## 🔹 What is @ExceptionHandler?

Handles exceptions at controller level.

---

## 🔹 What is @ControllerAdvice?

Global exception handler across all controllers.

---

## 🔹 Which has higher priority?

Controller > Global  

---

## 🔹 Why custom exception not working?

Spring doesn’t know how to convert it into HTTP response.

---

## 🔹 @ResponseStatus vs @ExceptionHandler?

| @ResponseStatus | @ExceptionHandler |
|---|---|
| Static mapping | Dynamic handling |
| Less flexible | More control |

---

# 🚀 Final Summary

---

```
Exception → Resolver Chain → Response
@ControllerAdvice → Global control
@ExceptionHandler → Specific control
Avoid mixing strategies unnecessarily
```