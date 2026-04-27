# ***Spring Boot Controller Annotations***

---

# 🔥 **What are Controller Annotations in Spring Boot?**

---

## **Definition**

Controller annotations in Spring Boot are used to handle incoming HTTP requests, bind request data, process client input, and return HTTP responses.

---

## 🧠 **Core Idea**

```text
Client Request → Controller Method → Service Layer → Response
```

---

👉 Controller is the entry point of REST APIs  
👉 It receives client requests  
👉 It maps URLs to Java methods  
👉 It binds request data to method parameters  
👉 It returns response back to client  

---

# 🔥 **Why Do We Need Controller Annotations?**

---

Without annotations, Spring Boot will not know:

```text
Which class handles HTTP requests
Which method should run for which URL
How to read query parameters
How to read path variables
How to read JSON request body
How to return HTTP status and response body
```

---

# 🔥 **Main Controller Annotations**

---

```text
@Controller
@RestController
@ResponseBody
@RequestMapping
@RequestParam
@PathVariable
@RequestBody
ResponseEntity
```

---

# 🔥 **1. @Controller**

---

## 📌 Definition

`@Controller` tells Spring that this class is responsible for handling incoming HTTP requests.

---

## 🧠 Simple Meaning

```text
@Controller = This class can handle web requests
```

---

## Example

```java
@Controller
public class UserController {

}
```

---

## Important Point

`@Controller` is mainly used in traditional Spring MVC applications where we return views like:

```text
HTML page
JSP page
Thymeleaf page
```

---

## Example

```java
@Controller
public class PageController {

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }
}
```

---

## What Happens Here?

```text
Spring treats "home" as a view name
It tries to resolve home.html or home.jsp
```

---

# 🔥 **2. @ResponseBody**

---

## 📌 Definition

`@ResponseBody` tells Spring that the return value of a controller method should be written directly into the HTTP response body.

---

## 🧠 Simple Meaning

```text
Do not search for view page
Return actual data directly
```

---

## Problem Without @ResponseBody

```java
@Controller
public class UserController {

    @GetMapping("/user")
    public String getUser() {
        return "Rahul";
    }
}
```

---

## What Spring Thinks

```text
"Rahul" is a view/page name
```

Spring will try to find:

```text
Rahul.html
Rahul.jsp
```

---

## Solution

```java
@Controller
public class UserController {

    @ResponseBody
    @GetMapping("/user")
    public String getUser() {
        return "Rahul";
    }
}
```

---

## Now Output

```text
Rahul
```

---

# 🔥 **3. @RestController**

---

## 📌 Definition

`@RestController` is a combination of:

```text
@Controller + @ResponseBody
```

---

## 🧠 Simple Meaning

```text
@RestController = Controller for REST APIs
```

---

## Example

```java
@RestController
public class UserController {

    @GetMapping("/user")
    public String getUser() {
        return "Rahul";
    }
}
```

---

## What Happens Here?

```text
Spring directly returns "Rahul" in HTTP response body
```

No need to write `@ResponseBody` separately.

---

## @Controller vs @RestController

| Point | @Controller | @RestController |
|---|---|---|
| Used For | Web pages/views | REST APIs |
| Returns | View name by default | Data directly |
| Needs @ResponseBody | Yes | No |
| Common In | MVC apps | Spring Boot REST APIs |

---

# 🔥 **4. @RequestMapping**

---

## 📌 Definition

`@RequestMapping` is used to map HTTP requests to controller classes or methods.

---

## 🧠 Simple Meaning

```text
@RequestMapping = Which URL should call which method
```

---

## Class-Level Mapping

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

}
```

---

## Method-Level Mapping

```java
@GetMapping("/profile")
public String getProfile() {
    return "User Profile";
}
```

---

## Final URL

```text
/api/users/profile
```

---

# 🔥 **@RequestMapping Attributes**

---

## 🔹 1. path / value

```java
@RequestMapping(value = "/fetchUser")
```

or

```java
@RequestMapping(path = "/fetchUser")
```

Both are same.

---

## 🔹 2. method

```java
@RequestMapping(value = "/fetchUser", method = RequestMethod.GET)
```

---

## 🔹 3. consumes

Used to define what request data format API accepts.

```java
@RequestMapping(
    value = "/saveUser",
    method = RequestMethod.POST,
    consumes = "application/json"
)
```

---

## 🔹 4. produces

Used to define what response data format API returns.

```java
@RequestMapping(
    value = "/fetchUser",
    method = RequestMethod.GET,
    produces = "application/json"
)
```

---

## Complete Example

```java
@RestController
@RequestMapping("/api")
public class UserController {

    @RequestMapping(
            path = "/fetchUser",
            method = RequestMethod.GET,
            consumes = "application/json",
            produces = "application/json"
    )
    public String fetchUser() {
        return "User fetched successfully";
    }
}
```

---

# 🔥 **Shortcut Annotations**

---

Instead of:

```java
@RequestMapping(value = "/users", method = RequestMethod.GET)
```

Use:

```java
@GetMapping("/users")
```

---

## Common Shortcuts

| Annotation | HTTP Method |
|---|---|
| @GetMapping | GET |
| @PostMapping | POST |
| @PutMapping | PUT |
| @PatchMapping | PATCH |
| @DeleteMapping | DELETE |

---

# 🔥 **5. @RequestParam**

---

## 📌 Definition

`@RequestParam` is used to bind query parameters from the URL to controller method parameters.

---

## 🧠 Simple Meaning

```text
@RequestParam = Read data after ? in URL
```

---

## Example URL

```text
http://localhost:8080/api/fetchUser?firstName=Rahul&lastName=Sharma&age=25
```

---

## Code

```java
@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/fetchUser")
    public String fetchUser(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam int age
    ) {
        return "First Name: " + firstName +
               ", Last Name: " + lastName +
               ", Age: " + age;
    }
}
```

---

## Output

```text
First Name: Rahul, Last Name: Sharma, Age: 25
```

---

# 🔥 **@RequestParam With Different Name**

---

If URL parameter name and Java variable name are different:

```text
/api/fetchUser?first_name=Rahul
```

Use:

```java
@GetMapping("/fetchUser")
public String fetchUser(
        @RequestParam(name = "first_name") String firstName
) {
    return firstName;
}
```

---

# 🔥 **@RequestParam Required Attribute**

---

By default, request params are required.

```java
@RequestParam String firstName
```

If missing, Spring throws error.

---

## Optional Param

```java
@GetMapping("/fetchUser")
public String fetchUser(
        @RequestParam(required = false) String firstName
) {
    return "First Name: " + firstName;
}
```

---

# 🔥 **@RequestParam Default Value**

---

```java
@GetMapping("/fetchUser")
public String fetchUser(
        @RequestParam(defaultValue = "Guest") String firstName
) {
    return "Hello " + firstName;
}
```

---

If URL does not contain `firstName`, output:

```text
Hello Guest
```

---

# 🔥 **Type Conversion in @RequestParam**

---

Spring automatically converts query parameter values.

---

## Example

```text
/api/fetchUser?age=25
```

```java
@GetMapping("/fetchUser")
public String fetchUser(@RequestParam int age) {
    return "Age is " + age;
}
```

---

Even though query params come as String, Spring converts:

```text
"25" → int 25
```

---

## Common Supported Types

```text
String
int
long
double
boolean
Integer
Long
Double
Boolean
Enum
```

---

# 🔥 **Custom Conversion Using @InitBinder**

---

## 📌 Problem

Suppose user sends:

```text
/api/fetchUser?firstName=   RAHUL
```

You want to automatically convert it to:

```text
rahul
```

---

## Solution

Use:

```text
@InitBinder + PropertyEditor
```

---

## Controller

```java
@RestController
@RequestMapping("/api")
public class UserController {

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(
                String.class,
                "firstName",
                new FirstNamePropertyEditor()
        );
    }

    @GetMapping("/fetchUser")
    public String fetchUser(@RequestParam String firstName) {
        return "First Name: " + firstName;
    }
}
```

---

## Property Editor

```java
import java.beans.PropertyEditorSupport;

public class FirstNamePropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(text.trim().toLowerCase());
    }
}
```

---

## Input

```text
/api/fetchUser?firstName=   RAHUL
```

---

## Output

```text
First Name: rahul
```

---

# 🔥 **6. @PathVariable**

---

## 📌 Definition

`@PathVariable` is used to extract values from the URL path.

---

## 🧠 Simple Meaning

```text
@PathVariable = Read data from URL path
```

---

## Example URL

```text
/api/users/10
```

Here:

```text
10 is part of path
```

---

## Code

```java
@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/users/{id}")
    public String getUserById(@PathVariable Long id) {
        return "User ID: " + id;
    }
}
```

---

## Output

```text
User ID: 10
```

---

# 🔥 **@PathVariable With Different Variable Name**

---

```java
@GetMapping("/users/{userId}")
public String getUserById(@PathVariable("userId") Long id) {
    return "User ID: " + id;
}
```

---

# 🔥 **@RequestParam vs @PathVariable**

---

| Point | @RequestParam | @PathVariable |
|---|---|---|
| Reads From | Query string | URL path |
| Example | `/users?id=10` | `/users/10` |
| Used For | Filters, optional data | Resource identity |
| Required? | Usually optional/filter | Usually required |

---

## Use @PathVariable When

```text
You are identifying a specific resource
```

Example:

```text
/users/10
/orders/100
/products/5
```

---

## Use @RequestParam When

```text
You are filtering/searching/sorting
```

Example:

```text
/users?city=Pune
/products?sort=price
/orders?status=DELIVERED
```

---

# 🔥 **7. @RequestBody**

---

## 📌 Definition

`@RequestBody` binds the HTTP request body, usually JSON, to a Java object.

---

## 🧠 Simple Meaning

```text
@RequestBody = Convert JSON request into Java object
```

---

## Example JSON Request

```json
{
  "username": "rahul",
  "email": "rahul@gmail.com"
}
```

---

## DTO / Model Class

```java
public class UserRequest {

    private String username;
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
```

---

## Controller

```java
@RestController
@RequestMapping("/api")
public class UserController {

    @PostMapping("/saveUser")
    public String saveUser(@RequestBody UserRequest userRequest) {
        return "User created: " + userRequest.getUsername()
                + ", Email: " + userRequest.getEmail();
    }
}
```

---

## What Happens Internally?

```text
JSON request body
        ↓
Jackson ObjectMapper
        ↓
Java object
        ↓
Controller method parameter
```

---

# 🔥 **@RequestBody Important Rule**

---

For `@RequestBody` to work properly:

```text
JSON field names should match Java class field names
```

---

## JSON

```json
{
  "username": "rahul"
}
```

## Java

```java
private String username;
```

---

# 🔥 **8. ResponseEntity**

---

## 📌 Definition

`ResponseEntity` represents the full HTTP response.

---

## 🧠 Simple Meaning

```text
ResponseEntity = Body + Status Code + Headers
```

---

## Simple Example

```java
@GetMapping("/fetchUser")
public ResponseEntity<String> fetchUser() {
    return ResponseEntity.ok("User fetched successfully");
}
```

---

## Output

```text
Status: 200 OK
Body: User fetched successfully
```

---

# 🔥 **ResponseEntity With Status Code**

---

```java
@PostMapping("/saveUser")
public ResponseEntity<String> saveUser(@RequestBody UserRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
            .body("User created successfully");
}
```

---

## Output

```text
Status: 201 Created
Body: User created successfully
```

---

# 🔥 **ResponseEntity With Headers**

---

```java
@GetMapping("/download")
public ResponseEntity<String> downloadFile() {

    HttpHeaders headers = new HttpHeaders();
    headers.add("Custom-Header", "SpringBoot");

    return ResponseEntity
            .ok()
            .headers(headers)
            .body("File downloaded");
}
```

---

# 🔥 **Complete Real-World Controller Example**

---

## UserRequest DTO

```java
public class UserRequest {

    private String username;
    private String email;
    private int age;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
```

---

## UserController

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User created: " + request.getUsername());
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getUserById(@PathVariable Long id) {

        return ResponseEntity
                .ok("Fetching user with id: " + id);
    }

    @GetMapping
    public ResponseEntity<String> searchUsers(
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                "Searching users from city: " + city +
                ", page: " + page +
                ", size: " + size
        );
    }
}
```

---

# 🔥 **How Request Flows in Controller**

---

## Example Request

```http
POST /api/users
Content-Type: application/json
```

```json
{
  "username": "rahul",
  "email": "rahul@gmail.com",
  "age": 25
}
```

---

## Flow

```text
Client sends request
        ↓
DispatcherServlet receives request
        ↓
Finds matching controller method
        ↓
Reads JSON body
        ↓
Converts JSON to UserRequest object
        ↓
Calls createUser()
        ↓
Returns ResponseEntity
        ↓
Client receives status + body
```

---

# 🔥 **When to Use Which Annotation?**

---

| Requirement | Annotation |
|---|---|
| Make class REST controller | @RestController |
| Map base URL | @RequestMapping |
| Handle GET request | @GetMapping |
| Handle POST request | @PostMapping |
| Read query param | @RequestParam |
| Read URL path value | @PathVariable |
| Read JSON body | @RequestBody |
| Return status + body | ResponseEntity |

---

# 🔥 **Common Mistakes**

---

## ❌ Mistake 1: Using @Controller Instead of @RestController

Bad:

```java
@Controller
public class UserController {

    @GetMapping("/user")
    public String getUser() {
        return "Rahul";
    }
}
```

Spring may search for view named `Rahul`.

Good:

```java
@RestController
public class UserController {

    @GetMapping("/user")
    public String getUser() {
        return "Rahul";
    }
}
```

---

## ❌ Mistake 2: Confusing @RequestParam and @PathVariable

Bad:

```java
@GetMapping("/users/{id}")
public String getUser(@RequestParam Long id) {
    return "User";
}
```

Good:

```java
@GetMapping("/users/{id}")
public String getUser(@PathVariable Long id) {
    return "User";
}
```

---

## ❌ Mistake 3: Forgetting @RequestBody

Bad:

```java
@PostMapping("/users")
public String createUser(UserRequest request) {
    return "User created";
}
```

Good:

```java
@PostMapping("/users")
public String createUser(@RequestBody UserRequest request) {
    return "User created";
}
```

---

## ❌ Mistake 4: Returning Only String for Real APIs

For simple learning it is okay.

But in real APIs, prefer:

```java
ResponseEntity<ApiResponse>
```

---

# 🔥 **Interview Questions**

---

## 🔹 What is @Controller?

```text
It marks a class as Spring MVC controller responsible for handling web requests.
```

---

## 🔹 What is @RestController?

```text
@RestController is a combination of @Controller and @ResponseBody.
It is used for REST APIs.
```

---

## 🔹 Difference between @Controller and @RestController?

```text
@Controller returns view by default.

@RestController returns data directly as response body.
```

---

## 🔹 What is @RequestMapping?

```text
It maps HTTP requests to controller classes or methods.
```

---

## 🔹 What is @RequestParam?

```text
It reads query parameters from the URL.
```

---

## 🔹 What is @PathVariable?

```text
It reads values from the URL path.
```

---

## 🔹 Difference between @RequestParam and @PathVariable?

```text
@RequestParam reads query parameters.

@PathVariable reads values from URL path.
```

---

## 🔹 What is @RequestBody?

```text
It converts HTTP request body, usually JSON, into a Java object.
```

---

## 🔹 What is ResponseEntity?

```text
ResponseEntity represents the complete HTTP response including status code, headers, and body.
```

---

# 🔥 **Memory Trick**

---

```text
@RestController → REST API class

@RequestMapping → URL mapping

@RequestParam → ?key=value

@PathVariable → /value

@RequestBody → JSON body

ResponseEntity → Full HTTP response
```

---

# 🚀 **Final Summary**

---

* `@Controller` handles web requests.
* `@RestController` is used for REST APIs.
* `@ResponseBody` returns data directly.
* `@RequestMapping` maps URL to methods.
* `@RequestParam` reads query parameters.
* `@PathVariable` reads URL path values.
* `@RequestBody` reads JSON body.
* `ResponseEntity` controls status, headers, and body.

---

```text
Controller annotations = Bridge between HTTP request and Java method
```

---

👉 These annotations are the foundation of every Spring Boot REST API.