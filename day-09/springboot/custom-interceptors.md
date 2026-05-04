# ***Custom Interceptors in Spring Boot***

---

# 🔥 What is an Interceptor?

---

## 📌 Definition

An **Interceptor** is a component that intercepts HTTP requests **before or after reaching the controller**.

---

## 🧠 Core Idea

Client Request → Interceptor → Controller → Interceptor → Response

---

## 🎯 Use Cases

Logging
Authentication
Authorization
Caching
Request validation


---

# 🔥 Types of Interceptors

---


1. Before Controller (HandlerInterceptor)
2. After Controller (AOP + Custom Annotation)


---

# 🔥 1. Interceptor Before Controller

---

## 📌 Flow


Client → DispatcherServlet → Interceptor → Controller → Response


---

## 💻 Example Controller

```java 
@RestController
@RequestMapping("/api/")
public class UserController {

    @Autowired
    User user;

    @GetMapping("/getUser")
    public String getUser() {
        user.getUser();
        return "success";
    }
}
```


---

## 💻 Create Custom Interceptor

```java
@Component
public class MyCustomInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        System.out.println("Inside preHandle()");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                          HttpServletResponse response,
                          Object handler,
                          ModelAndView modelAndView) throws Exception {

        System.out.println("Inside postHandle()");
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                               HttpServletResponse response,
                               Object handler,
                               Exception ex) throws Exception {

        System.out.println("Inside afterCompletion()");
    }
}
```

---

## 🧠 Methods Explained

preHandle()        → Before controller execution
postHandle()       → After controller execution
afterCompletion()  → After response is sent


---

## 💻 Register Interceptor

```java
@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Autowired
    MyCustomInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(interceptor)
                .addPathPatterns("/api/*")
                .excludePathPatterns("/api/deleteUser");
    }
}
```

---

## 🧠 Flow Summary

Request comes
        ↓
preHandle()
        ↓
Controller executes
        ↓
postHandle()
        ↓
Response sent
        ↓
afterCompletion()


---

# 🔥 2. Interceptor After Controller (Using AOP)

---

## 📌 Why Needed?


We want interception only for specific methods
(using custom annotations)


---

# 🔥 Step 1: Create Custom Annotation

---

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyCustomAnnotation {
}
```

---

## 🧠 Important Concepts

### Target


Defines where annotation can be used
(METHOD, CLASS, FIELD etc.)


### Retention


SOURCE   → Removed at compile time
CLASS    → Present in .class but not runtime
RUNTIME  → Available at runtime (required for AOP)


---

# 🔥 Custom Annotation with Fields

---

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyCustomAnnotation {

    String name() default "";
    int count() default 0;
}
```

---

## 💻 Usage

```java
@Component
public class User {

    @MyCustomAnnotation(name = "user", count = 1)
    public void getUser() {
        System.out.println("Getting user");
    }
}
```

---

# 🔥 Step 2: Create AOP Interceptor

---

```java
@Aspect
@Component
public class MyCustomInterceptor {

    @Around("@annotation(MyCustomAnnotation)")
    public Object intercept(ProceedingJoinPoint joinPoint) throws Throwable {

        System.out.println("Before method execution");

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        if (method.isAnnotationPresent(MyCustomAnnotation.class)) {
            MyCustomAnnotation annotation =
                    method.getAnnotation(MyCustomAnnotation.class);

            System.out.println("Annotation value: " + annotation.name());
        }

        Object result = joinPoint.proceed();

        System.out.println("After method execution");

        return result;
    }
}
```

---

## 🧠 Flow

Method called
        ↓
AOP Interceptor
        ↓
Before logic
        ↓
Actual method
        ↓
After logic


---

# 🔥 HandlerInterceptor vs AOP

---

## 🔹 HandlerInterceptor


Before Controller
Works on URL patterns
HTTP level interception


---

## 🔹 AOP Interceptor

After Controller
Works on methods
Annotation-based interception


---

# 🔥 Complete Request Flow

---


Client Request
        ↓
DispatcherServlet
        ↓
HandlerInterceptor (preHandle)
        ↓
Controller
        ↓
AOP Interceptor (@Around)
        ↓
Business Logic
        ↓
AOP After
        ↓
HandlerInterceptor (postHandle)
        ↓
Response
        ↓
afterCompletion

---

# 🔥 When to Use What?

---

## Use HandlerInterceptor


Logging
Authentication
Headers validation
Request tracking


---

## Use AOP


Business logic interception
Custom annotations
Method-level control


---

# 🔥 Advantages

---


Clean separation of concerns
Reusable logic
Centralized handling
Improved logging and security

---

# 🔥 Common Mistakes

---


Forgetting to register interceptor
Returning false in preHandle()
Using wrong RetentionPolicy
Mixing AOP and interceptor incorrectly


---

# 🔥 Memory Trick

---


Interceptor = Request level
AOP = Method level


---

# 🎯 Interview Questions

---

## 🔹 What is an Interceptor in Spring Boot?


An Interceptor is used to intercept HTTP requests before or after they reach the controller.
It is commonly used for logging, authentication, authorization, request validation, and tracking.


---

## 🔹 Which interface is used to create an interceptor in Spring Boot?


HandlerInterceptor interface is used to create a custom interceptor.


---

## 🔹 What are the main methods of HandlerInterceptor?


preHandle()
postHandle()
afterCompletion()


---

## 🔹 What is preHandle()?


preHandle() is executed before the controller method is called.

If it returns true:
Request continues to the controller.

If it returns false:
Request stops and controller is not executed.


---

## 🔹 What is postHandle()?


postHandle() is executed after the controller method is executed but before the response is completed.


---

## 🔹 What is afterCompletion()?


afterCompletion() is executed after the request is completed and response is sent.
It is commonly used for cleanup, logging, and exception tracking.


---

## 🔹 How do we register a custom interceptor?


By implementing WebMvcConfigurer and overriding addInterceptors() method.


---

## 🔹 What is addPathPatterns()?


addPathPatterns() defines the URL patterns where the interceptor should be applied.


---

## 🔹 What is excludePathPatterns()?


excludePathPatterns() defines the URL patterns where the interceptor should not be applied.


---

## 🔹 Difference between HandlerInterceptor and AOP?

| HandlerInterceptor | AOP |
|---|---|
| Works at HTTP request level | Works at method level |
| Used before/after controller | Used around business methods |
| URL pattern based | Annotation or pointcut based |
| Good for authentication/logging | Good for method-level logic |

---

## 🔹 Can Interceptor replace Filter?


No. Filter works before DispatcherServlet.
Interceptor works after DispatcherServlet and before Controller.


---

## 🔹 Filter vs Interceptor?

| Filter | Interceptor |
|---|---|
| Servlet level | Spring MVC level |
| Executes before DispatcherServlet | Executes after DispatcherServlet |
| Works on all requests | Works on controller mappings |
| Used for low-level request processing | Used for Spring MVC request processing |

---

## 🔹 Why do we use custom annotations with AOP?


Custom annotations help apply interceptor logic only to selected methods.
This avoids applying logic globally.


---

## 🔹 Why is RetentionPolicy.RUNTIME needed?


Because AOP needs to read the annotation at runtime.
If annotation is not available at runtime, interceptor logic will not work.


---

## 🔹 What happens if preHandle() returns false?


The request stops immediately.
Controller method will not execute.


---

## 🔹 Common use cases of Interceptors?


Authentication
Authorization
Logging
Request tracking
Header validation
Audit logging
Performance monitoring


---

# 🚀 Final Summary

---


HandlerInterceptor → Before/After Controller
AOP → Around Method Execution
Custom Annotation → Selective Interception


