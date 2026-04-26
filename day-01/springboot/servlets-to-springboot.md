# 🔥 Servlet → Spring → Spring Boot

---

# 🚀 Why Learn This?

* Before Spring Boot, web apps were built using **Servlets**
* Spring introduced **better structure & dependency management**
* Spring Boot simplified everything with **auto-configuration**

---

# 🌐 What is a Servlet?

## 📌 Definition

A **Servlet** is a Java class used to **handle HTTP requests and generate responses**.

---

## 🧠 Core Flow

```
Client Request → Servlet → Response
```

---

## ⚙️ Runs On

* Servlet Container (e.g., **Tomcat**)

---

# 🏗️ Servlet Architecture

```
Client → Servlet Container → Servlet → Response
```

---

## 🔄 Request Flow

1. Client sends HTTP request
2. Servlet Container (Tomcat) receives it
3. Uses `web.xml` or annotations to map request
4. Calls appropriate Servlet
5. Servlet processes request
6. Sends response back

---

# 💻 Servlet Example

```java
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/demo")
public class DemoServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req,
                         HttpServletResponse res) {

        System.out.println("Handling Request");
    }
}
```

---

# ❌ Problems with Servlets

### 1. Heavy XML Configuration

* `web.xml` becomes large and complex

### 2. Tight Coupling

```java
User user = new User();
```

* Hard to modify
* Hard to test

---

### 3. No Dependency Management

* Manual object creation
* No lifecycle control

---

### 4. Difficult Testing

* Hard to mock dependencies

---

### 5. Complex REST Handling

* Manual request parsing
* Manual response creation

---

# 🌱 Spring Framework (Solution)

---

## 🧠 What Spring Solves

✔ Removes XML complexity → uses annotations
✔ Introduces **IoC (Inversion of Control)**
✔ Enables **Dependency Injection (DI)**

---

# 🔥 Dependency Injection

---

## ❌ Without DI (Tight Coupling)

```java
class Payment {
    User user = new User();
}
```

---

## ✅ With DI (Loose Coupling)

```java
@Component
class Payment {

    @Autowired
    User user;
}
```

---

## 🧠 Explanation

* `@Component` → registers bean in container
* `@Autowired` → injects dependency

---

# 🌐 Spring MVC Architecture

```
Client → DispatcherServlet → Controller → Service → Response
```

---

## 🔄 Flow

1. Request hits **DispatcherServlet**
2. Finds matching controller
3. Spring creates object (IoC)
4. Method is executed
5. Response returned

---

# 💻 Spring MVC Example

```java
@Controller
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    PaymentService service;

    @GetMapping("/pay")
    public String getPayment() {
        return service.getDetails();
    }
}
```

---

# ❌ Problems with Spring (Still Exists)

### 1. Too Much Configuration

* DispatcherServlet setup
* Component scanning

---

### 2. Dependency Management Issues

* Manual version handling
* Compatibility problems

---

### 3. WAR Deployment

* Requires external server (Tomcat)

---

# 🚀 Spring Boot (Final Solution)

---

## 🧠 What Spring Boot Solves

✔ Automatic configuration
✔ Built-in dependency management
✔ Embedded server

---

# 💻 Spring Boot Example

```java
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
```

---

# 🌐 Spring Boot REST Example

```java
@RestController
@RequestMapping("/api")
public class MyController {

    @GetMapping("/hello")
    public String getData() {
        return "Hello from Spring Boot";
    }
}
```

---

# 🔑 Key Concepts

---

## 🔹 Convention over Configuration

* Default configs provided
* Override only when needed

---

## 🔹 Starter Dependencies

Example:

```
spring-boot-starter-web
```

---

## 🔹 Embedded Server

```
Run directly → java -jar app.jar
```

---

# ⚖️ Servlet vs Spring vs Spring Boot

| Feature              | Servlet   | Spring   | Spring Boot |
| -------------------- | --------- | -------- | ----------- |
| Configuration        | Heavy XML | Moderate | Minimal     |
| Dependency Injection | ❌         | ✔        | ✔           |
| Server               | External  | External | Embedded    |
| Setup                | Complex   | Moderate | Easy        |
| Development Speed    | Slow      | Faster   | Very Fast   |

---

# ✅ Advantages of Spring Boot

* Minimal configuration
* Faster development
* Embedded server
* Production-ready setup
* Easy REST API creation

---

# 🎯 Interview Questions

---

## 🔹 What is a Servlet?

A Java class that handles HTTP requests and responses.

---

## 🔹 What is IoC?

Control of object creation is handled by the container.

---

## 🔹 What is Dependency Injection?

Providing dependencies instead of creating them manually.

---

## 🔹 Spring vs Spring Boot?

* Spring → Framework
* Spring Boot → Simplified version of Spring

---

# 🚀 Final Summary

```
Servlet → Low-level request handling  
Spring → Adds structure + DI  
Spring Boot → Removes complexity  
```

---

👉 This represents the **evolution of Java web development**
