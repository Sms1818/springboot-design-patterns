# ***Response & HTTP Status Codes in Spring Boot***

---

# 🔥 What is an HTTP Response?

---

## 📌 Definition

An **HTTP Response** is what the server sends back to the client after processing a request.

---

## 🧠 Response Structure

Every response contains:

- **Status Code** → Result of request (200, 404, 500, etc.)
- **Headers** → Metadata (optional)
- **Body** → Actual data returned 

---

## 🧠 Example

```http
Status: 200 OK
Headers: Content-Type: application/json
Body:
{
  "name": "John"
}
```

---

# 🔥 ResponseEntity in Spring Boot

---

## 📌 What is ResponseEntity?

`ResponseEntity<T>` is used to **customize HTTP response**:

- Status code  
- Headers  
- Body  

---

## 💻 Basic Example

```java
@GetMapping("/user")
public ResponseEntity<String> getUser() {
    return ResponseEntity.ok("User fetched successfully");
}
```

---

## 💻 With Headers

```java
@GetMapping("/user")
public ResponseEntity<String> getUser() {

    HttpHeaders headers = new HttpHeaders();
    headers.add("Custom-Header", "Value");

    return ResponseEntity
            .status(HttpStatus.OK)
            .headers(headers)
            .body("User fetched");
}
```

---

## 🧠 Builder Pattern Concept

```
status() → headers() → body()
```

👉 `body()` should always be last (builder pattern)  

---

# 🔥 Response Without Body

---

## 💻 Example

```java
@DeleteMapping("/user/{id}")
public ResponseEntity<Void> deleteUser() {
    return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
}
```

---

# 🔥 @ResponseBody vs @RestController

---

## 📌 @ResponseBody

- Converts return value into **HTTP response body**

---

## 📌 @RestController

- Combines:
```
@Controller + @ResponseBody
```

👉 Automatically applies response body behavior 

---

## ❌ Common Mistake

```java
@Controller
public class UserController {

    @GetMapping("/user")
    public String getUser() {
        return "XYZ"; // ❌ treated as view name
    }
}
```

👉 Spring tries to find HTML view "XYZ"

---

## ✅ Correct

```java
@RestController
public class UserController {

    @GetMapping("/user")
    public String getUser() {
        return "XYZ"; // returned as response
    }
}
```

---

# 🔥 HTTP Status Code Categories

---

## 🧠 Overview

```
1xx → Informational
2xx → Success
3xx → Redirection
4xx → Client Errors
5xx → Server Errors
```

---

# 🔥 2xx – Success Responses

---

## 📌 Meaning

Request is successfully processed.

---

## 🔹 200 OK

Used when:
- GET successful
- Update successful

```java
return ResponseEntity.ok(user);
```

---

## 🔹 201 Created

Used when:
- Resource is created

```java
@PostMapping("/user")
public ResponseEntity<User> createUser() {
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
}
```

---

## 🔹 202 Accepted

Used when:
- Request accepted but not processed yet
- Example: batch processing

---

## 🔹 204 No Content

Used when:
- Operation successful
- No response body needed

---

## 🔹 206 Partial Content

Used when:
- Partial success (bulk operations)

Example:
```
100 users processed
95 success, 5 failed
```

---

# 🔥 3xx – Redirection

---

## 📌 Meaning

Client must take additional action.

---

## 🔹 301 Moved Permanently

- Old API → New API  
- Client redirected  

---

## 🔹 308 Permanent Redirect

- Same as 301  
- BUT HTTP method does NOT change 

---

## 🔹 304 Not Modified

Used in caching:

Flow:
```
Client → sends last-modified time
Server → checks resource
If unchanged → return 304
```

👉 No data sent → saves bandwidth 

---

# 🔥 4xx – Client Errors

---

## 📌 Meaning

Client made a mistake.

---

## 🔹 400 Bad Request

- Missing fields  
- Invalid input  

---

## 🔹 401 Unauthorized

- Authentication required  
- Token missing/invalid  

---

## 🔹 403 Forbidden

- Authenticated but NOT allowed  

Example:
```
User is logged in but not ADMIN
```

---

## 🔹 404 Not Found

- Resource does not exist  

---

## 🔹 405 Method Not Allowed

- Wrong HTTP method used  

Example:
```
POST on GET endpoint
```

---

## 🔹 422 Unprocessable Entity

- Business validation failed  

Example:
```
Country not supported
```

---

## 🔹 429 Too Many Requests

- Rate limiting exceeded  

Example:
```
Max 10 requests/min
User sends 11th → 429
```

---

# 🔥 5xx – Server Errors

---

## 📌 Meaning

Server failed to process valid request.

---

## 🔹 500 Internal Server Error

- Generic error  
- Unexpected exception  

---

## 🔹 501 Not Implemented

- API not developed yet  

---

## 🔹 502 Bad Gateway

- Proxy error  

Example:
```
Nginx cannot connect to backend
```

---

# 🔥 1xx – Informational

---

## 🔹 100 Continue

Flow:

```
Client → sends headers
Server → validates
Server → returns 100 Continue
Client → sends actual request
```

👉 Used in large uploads 

---

# 🔥 Real World API Design

---

## 📌 Example: Create User API

```java
@PostMapping("/users")
public ResponseEntity<User> createUser(@RequestBody User user) {

    if(user.getName() == null) {
        return ResponseEntity.badRequest().build();
    }

    User savedUser = service.save(user);

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(savedUser);
}
```

---

## 📌 Example: Get User

```java
@GetMapping("/users/{id}")
public ResponseEntity<User> getUser(@PathVariable int id) {

    Optional<User> user = service.findById(id);

    if(user.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    return ResponseEntity.ok(user.get());
}
```

---

# 🔥 Best Practices

---

- Always return proper status code  
- Do NOT always return 200  
- Use 201 for create  
- Use 204 for delete  
- Use 400 for validation errors  
- Use 500 only for unexpected errors  

---

# 🎯 Interview Questions

---

## 🔹 What is ResponseEntity?

Used to customize HTTP response (status, headers, body)

---

## 🔹 Difference between @Controller and @RestController?

| @Controller | @RestController |
|---|---|
| Returns view | Returns JSON |
| Needs @ResponseBody | Included automatically |

---

## 🔹 When to use 200 vs 201?

- 200 → success  
- 201 → resource created  

---

## 🔹 401 vs 403?

- 401 → Not authenticated  
- 403 → Not authorized  

---

## 🔹 What is 204?

Success but no response body  

---

## 🔹 What is 429?

Rate limit exceeded  

---

# 🚀 Final Summary

---

```
Response = Status + Headers + Body
Use ResponseEntity for control
Choose correct status code always
Good APIs = Correct semantics
```