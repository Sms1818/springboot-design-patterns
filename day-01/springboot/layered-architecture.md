# ***Layered Architecture in Spring Boot – Complete Deep Guide***

---

# 🔥 **What is Layered Architecture?**

---

## **Definition**

Layered Architecture is a way of organizing code into separate layers where each layer has a specific responsibility.

---

## 🧠 **Core Idea**

```text
Client → Controller → Service → Repository → Database
```

---

👉 Each layer does only one job  
👉 Code becomes clean and maintainable  
👉 Easy to test, debug, and extend  

---

# 🔥 **Main Layers**

---

```text
Controller Layer
Service Layer
Repository Layer
Database Layer
```

Along with supporting components:

```text
DTO
Entity
Utility
Configuration
```

---

# 🔥 **1. Controller Layer**

---

## **Definition**

Controller Layer handles incoming HTTP requests from clients.

---

## 🎯 Responsibility

```text
Receive request
Validate basic input
Call service layer
Return response
```

---

## ❌ Controller should NOT contain

```text
Business logic
Database queries
Complex calculations
Entity manipulation logic
```

---

## Example

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto requestDto) {
        return userService.createUser(requestDto);
    }

    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
```

---

## 🧠 Simple Meaning

```text
Controller = Entry gate of application
```

---

# 🔥 **2. Service Layer**

---

## **Definition**

Service Layer contains the main business logic of the application.

---

## 🎯 Responsibility

```text
Apply business rules
Call repository layer
Handle validations
Convert DTO to Entity
Convert Entity to DTO
Coordinate multiple operations
```

---

## Example

```java
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto createUser(UserRequestDto requestDto) {

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(requestDto.getName());
        user.setEmail(requestDto.getEmail());

        User savedUser = userRepository.save(user);

        return new UserResponseDto(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );
    }

    public UserResponseDto getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
```

---

## 🧠 Simple Meaning

```text
Service = Brain of application
```

---

# 🔥 **3. Repository Layer**

---

## **Definition**

Repository Layer communicates with the database.

---

## 🎯 Responsibility

```text
Save data
Fetch data
Update data
Delete data
Run database queries
```

---

## Example

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
```

---

## 🧠 Simple Meaning

```text
Repository = Database helper
```

---

# 🔥 **4. Entity**

---

## **Definition**

Entity represents a database table in Java code.

---

## Example

```java
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    public User() {
    }

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // getters and setters
}
```

---

## Mapping

```text
User class → users table
id field   → id column
name field → name column
email      → email column
```

---

## 🧠 Simple Meaning

```text
Entity = Java representation of database table
```

---

# 🔥 **5. DTO**

---

## **Definition**

DTO means Data Transfer Object.

It is used to transfer data between client and application without exposing Entity directly.

---

## Why DTO?

```text
Hide sensitive fields
Avoid exposing database structure
Control request/response format
Make API clean
```

---

## Request DTO

```java
public class UserRequestDto {

    private String name;
    private String email;

    // getters and setters
}
```

---

## Response DTO

```java
public class UserResponseDto {

    private Long id;
    private String name;
    private String email;

    public UserResponseDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // getters and setters
}
```

---

## 🧠 Simple Meaning

```text
DTO = Data carrier between layers/API
```

---

# 🔥 **6. Utility Layer**

---

## **Definition**

Utility classes contain reusable helper methods.

---

## Example

```java
public class EmailUtil {

    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }
}
```

---

## Usage

```java
if (!EmailUtil.isValidEmail(requestDto.getEmail())) {
    throw new RuntimeException("Invalid email");
}
```

---

## 🧠 Simple Meaning

```text
Utility = Reusable helper logic
```

---

# 🔥 **7. Configuration Layer**

---

## **Definition**

Configuration classes contain application-level setup.

---

## Example

```java
@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
```

---

## 🧠 Simple Meaning

```text
Configuration = Custom setup for Spring Boot application
```

---

# 🔥 **Complete Request Flow**

---

```text
Client sends request
        ↓
Controller receives request
        ↓
Controller calls Service
        ↓
Service applies business logic
        ↓
Service calls Repository
        ↓
Repository talks to Database
        ↓
Database returns data
        ↓
Repository returns Entity
        ↓
Service converts Entity to DTO
        ↓
Controller returns response
        ↓
Client receives response
```

---

# 🔥 **Real Example Flow**

---

## API Request

```http
POST /api/users
```

```json
{
  "name": "Rahul",
  "email": "rahul@gmail.com"
}
```

---

## Flow

```text
UserController.createUser()
        ↓
UserService.createUser()
        ↓
UserRepository.existsByEmail()
        ↓
UserRepository.save()
        ↓
UserResponseDto returned
```

---

## API Response

```json
{
  "id": 1,
  "name": "Rahul",
  "email": "rahul@gmail.com"
}
```

---

# 🔥 **Why Use Layered Architecture?**

---

```text
Clean separation of responsibility
Easy to debug
Easy to test
Easy to maintain
Easy to scale
Professional project structure
```

---

# 🔥 **Folder Structure**

---

```text
src/main/java/com/example/demo
│
├── controller
│   └── UserController.java
│
├── service
│   └── UserService.java
│
├── repository
│   └── UserRepository.java
│
├── entity
│   └── User.java
│
├── dto
│   ├── UserRequestDto.java
│   └── UserResponseDto.java
│
├── util
│   └── EmailUtil.java
│
├── config
│   └── AppConfig.java
│
└── DemoApplication.java
```

---

# 🔥 **Common Mistakes**

---

## ❌ Writing business logic in Controller

Bad:

```java
@PostMapping
public User createUser(@RequestBody User user) {
    if (user.getEmail().contains("@")) {
        return userRepository.save(user);
    }
    throw new RuntimeException("Invalid email");
}
```

Good:

```text
Controller should call Service
Service should contain business logic
```

---

## ❌ Calling Repository directly from Controller

Bad:

```java
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
}
```

Good:

```text
Controller → Service → Repository
```

---

## ❌ Exposing Entity directly

Bad:

```java
public User getUser() {
    return userRepository.findById(1L).get();
}
```

Good:

```java
public UserResponseDto getUser() {
    return userService.getUserById(1L);
}
```

---

# 🔥 **Final Summary**

---

* Controller handles request and response
* Service contains business logic
* Repository talks to database
* Entity maps to database table
* DTO transfers clean data
* Utility stores reusable helper logic
* Configuration stores application setup

---

```text
Layered Architecture = Clean Code + Separation of Responsibility
```

---