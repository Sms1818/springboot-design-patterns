# ***Filters vs Interceptors in Spring Boot***

---

# 🔥 What is a Filter?

---

## 📌 Definition

A **Filter** intercepts HTTP **Request and Response before they reach the Servlet**. :contentReference[oaicite:0]{index=0}

---

## 🧠 Core Idea

```
Client → Filter → Servlet → Response
```

---

## 🎯 Purpose

- Works at **Servlet level**
- Independent of Spring
- Applies to **all requests**
- Used for **generic logic**

---

# 🔥 What is an Interceptor?

---

## 📌 Definition

An **Interceptor** intercepts HTTP **Request and Response before they reach the Controller**. :contentReference[oaicite:1]{index=1}

---

## 🧠 Core Idea

```
Client → DispatcherServlet → Interceptor → Controller → Response
```

---

## 🎯 Purpose

- Works at **Spring MVC level**
- Applies to **controller mappings**
- Used for **Spring-specific logic**

---

# 🔥 Understanding the Flow

---

## 📌 Servlet Concept

A **Servlet** is a Java class that:
- Accepts request  
- Processes it  
- Returns response :contentReference[oaicite:2]{index=2}  

Spring provides **DispatcherServlet**:
- Handles all requests (`/*`)  
- Routes to controllers  

---

## 🧠 Full Request Flow

```
Client
   ↓
Filter Chain
   ↓
DispatcherServlet
   ↓
Interceptor
   ↓
Controller
   ↓
Response
```

---

# 🔥 Filter vs Interceptor

---

| Feature | Filter | Interceptor |
|---|---|---|
| Level | Servlet | Spring MVC |
| Executes | Before Servlet | Before Controller |
| Scope | All requests | Controller-specific |
| Dependency | Independent of Spring | Spring-specific |
| Use case | Logging, CORS, encoding | Auth, business rules |

---

# 🔥 Multiple Interceptors

---

## 📌 Key Concept

- You can add **multiple interceptors**
- Execution order = **order of registration**
- If `preHandle()` returns false:
  - Next interceptor ❌  
  - Controller ❌ :contentReference[oaicite:3]{index=3}  

---

## 💻 Example

```java
@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Autowired
    MyCustomInterceptor1 interceptor1;

    @Autowired
    MyCustomInterceptor2 interceptor2;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(interceptor1)
                .addPathPatterns("/api/*")
                .excludePathPatterns("/api/updateUser", "/api/deleteUser");

        registry.addInterceptor(interceptor2)
                .addPathPatterns("/api/*")
                .excludePathPatterns("/api/updateUser");
    }
}
```

---

# 🔥 Filters Implementation

---

## 💻 Create Filter

```java
public class MyFilter1 implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        System.out.println("MyFilter1 inside");

        chain.doFilter(request, response);

        System.out.println("MyFilter1 completed");
    }
}
```

---

## 💻 Second Filter

```java
public class MyFilter2 implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        System.out.println("MyFilter2 inside");

        chain.doFilter(request, response);

        System.out.println("MyFilter2 completed");
    }
}
```

---

## 💻 Register Filters

```java
@Configuration
public class AppConfig {

    @Bean
    public FilterRegistrationBean<MyFilter1> filter1() {
        FilterRegistrationBean<MyFilter1> bean = new FilterRegistrationBean<>();
        bean.setFilter(new MyFilter1());
        bean.addUrlPatterns("/*");
        bean.setOrder(2);
        return bean;
    }

    @Bean
    public FilterRegistrationBean<MyFilter2> filter2() {
        FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>();
        bean.setFilter(new MyFilter2());
        bean.addUrlPatterns("/*");
        bean.setOrder(1);
        return bean;
    }
}
```

---

# 🔥 Ordering Concept

---

## 🧠 Filters

- Lower order → executes first  
- Example:
  - Order 1 → first  
  - Order 2 → second  

---

## 🧠 Interceptors

- Order based on **registration sequence**

---

# 🔥 Execution Flow (Combined)

---

```
Client Request
        ↓
Filter 2
        ↓
Filter 1
        ↓
DispatcherServlet
        ↓
Interceptor 1 (preHandle)
        ↓
Interceptor 2 (preHandle)
        ↓
Controller
        ↓
Interceptor 2 (postHandle)
        ↓
Interceptor 1 (postHandle)
        ↓
Response
        ↓
Filter 1 complete
        ↓
Filter 2 complete
```

---

# 🔥 When to Use What?

---

## Use Filter

- Logging (global)
- CORS
- Encoding
- Security at servlet level

---

## Use Interceptor

- Authentication
- Authorization
- Controller-level logic
- API-specific validation

---

# 🔥 Advantages

---

## Filter

- Works for all requests  
- Framework independent  
- Early interception  

---

## Interceptor

- More control in Spring  
- Access to controller info  
- Fine-grained logic  

---

# 🔥 Common Mistakes

---

- Mixing filter and interceptor logic incorrectly  
- Wrong order configuration  
- Forgetting `chain.doFilter()` in filters  
- Returning false in `preHandle()` unintentionally  

---

# 🎯 Interview Questions

---

## 🔹 What is a Filter?

A Filter intercepts request/response before reaching the servlet.

---

## 🔹 What is an Interceptor?

An Interceptor intercepts request/response before reaching the controller.

---

## 🔹 Filter vs Interceptor?

| Filter | Interceptor |
|---|---|
| Servlet level | Spring MVC level |
| Before DispatcherServlet | After DispatcherServlet |
| Works on all requests | Works on controllers |

---

## 🔹 Can we use both together?

Yes. Filters execute first, then interceptors.

---

## 🔹 What happens if preHandle() returns false?

- Controller will not execute  
- Next interceptor will not execute  

---

## 🔹 How is filter ordering controlled?

Using `setOrder()` in `FilterRegistrationBean`.

---

## 🔹 How is interceptor ordering controlled?

By order of registration in `addInterceptors()`.

---

# 🚀 Final Summary

---

```
Filter → Servlet level → Global interception
Interceptor → Spring level → Controller interception
Both together → Complete request control
```