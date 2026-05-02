# ***Aspect Oriented Programming in Spring Boot***

---

# 🔥 What is AOP

---

## 📌 Definition

Aspect Oriented Programming is a programming paradigm that allows you to **separate cross-cutting concerns from business logic**.

AOP allows you to intercept method execution and execute additional logic before, after, or around it.

---

## 🧠 Core Idea

```text id="aop001"
Intercept method execution and add extra behavior
```

---

```text id="aop002"
Method call → Intercept → Advice → Method → Advice → Return
```

---

## 🧠 Simple Meaning

```text id="aop003"
AOP = Add logic without modifying original code
```

---

# 🔥 Why Do We Need AOP

---

## 📌 Problem

Applications require common functionalities:

```text id="aop004"
Logging
Security
Transactions
Validation
```

---

## ❌ Without AOP

```java id="aop005"
public void getUser() {
    log();
    validate();
    businessLogic();
    log();
}
```

---

## 🚨 Issues

```text id="aop006"
Code duplication
Scattered logic
Hard to maintain
```

---

# 🔥 AOP Solution

---

```text id="aop007"
Move cross-cutting logic into a separate class (Aspect)
```

---

# 🔥 Key Concepts

---

## Aspect

```text id="aop008"
Class containing cross-cutting logic
```

---

## Advice

```text id="aop009"
Action executed at a specific point
```

---

## Pointcut

```text id="aop010"
Expression that defines WHERE advice runs
```

---

## Join Point

```text id="aop011"
Actual method execution point
```

---

# 🔥 Types of Advice

---

## 1 Before Advice

---

```java id="aop012"
@Before("execution(* com.example.service.*.*(..))")
public void before() {
    System.out.println("Before method");
}
```

---

## 🧠 Meaning

```text id="aop013"
Runs before method execution
```

---

---

## 2 After Advice

---

```java id="aop014"
@After("execution(* com.example.service.*.*(..))")
public void after() {
    System.out.println("After method");
}
```

---

## 🧠 Meaning

```text id="aop015"
Runs after method execution (even if exception occurs)
```

---

---

## 3 After Returning

---

```java id="aop016"
@AfterReturning(
    pointcut = "execution(* com.example.service.*.*(..))",
    returning = "result"
)
public void afterReturning(Object result) {
    System.out.println("Returned: " + result);
}
```

---

## 🧠 Meaning

```text id="aop017"
Runs only when method executes successfully
```

---

---

## 4 After Throwing

---

```java id="aop018"
@AfterThrowing(
    pointcut = "execution(* com.example.service.*.*(..))",
    throwing = "ex"
)
public void afterThrowing(Exception ex) {
    System.out.println("Exception: " + ex.getMessage());
}
```

---

## 🧠 Meaning

```text id="aop019"
Runs only when exception occurs
```

---

---

## 5 Around Advice

---

```java id="aop020"
@Around("execution(* com.example.service.*.*(..))")
public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

    System.out.println("Before");

    Object result = joinPoint.proceed();

    System.out.println("After");

    return result;
}
```

---

## 🧠 Meaning

```text id="aop021"
Controls full execution (before + after)
```

---

# 🔥 Types of Pointcuts

---

## 📌 Definition

```text id="aop022"
Pointcut decides which methods should be intercepted
```

---

---

## 1 Execution

---

```java id="aop023"
execution(* com.example.service.UserService.getUser(..))
```

---

## 🧠 Meaning

```text id="aop024"
Match specific method
```

---

## Wildcard Example

```java id="aop025"
execution(* com.example.service.*.*(..))
```

---

```text id="aop026"
All methods in package
```

---

---

## 2 Within

---

```java id="aop027"
within(com.example.service.*)
```

---

## 🧠 Meaning

```text id="aop028"
All methods inside package/class
```

---

---

## 3 This

---

```java id="aop029"
this(com.example.service.UserService)
```

---

## 🧠 Meaning

```text id="aop030"
Matches proxy type
```

---

---

## 4 Target

---

```java id="aop031"
target(com.example.service.UserService)
```

---

## 🧠 Meaning

```text id="aop032"
Matches actual object type
```

---

---

## 5 Args

---

```java id="aop033"
args(String)
```

---

## 🧠 Meaning

```text id="aop034"
Matches method arguments
```

---

---

## 6 Annotation

---

```java id="aop035"
@annotation(com.example.Loggable)
```

---

## 🧠 Meaning

```text id="aop036"
Matches methods with annotation
```

---

---

## 7 Within Annotation

---

```java id="aop037"
@within(org.springframework.stereotype.Service)
```

---

## 🧠 Meaning

```text id="aop038"
Matches classes with annotation
```

---

---

## 8 Args Annotation

---

```java id="aop039"
@args(com.example.MyAnnotation)
```

---

## 🧠 Meaning

```text id="aop040"
Matches annotated parameters
```

---

---

# 🔥 Combining Pointcuts

---

## AND

```java id="aop041"
execution(* com.example.service.*.*(..)) && args(String)
```

---

## OR

```java id="aop042"
execution(* com.example.service.*.*(..)) || args(String)
```

---

---

# 🔥 Named Pointcuts

---

```java id="aop043"
@Pointcut("execution(* com.example.service.*.*(..))")
public void serviceMethods() {}
```

---

```java id="aop044"
@Before("serviceMethods()")
```

---

# 🔥 Internal Working

---

# 🧠 Step 1 Startup Phase

---

```text id="aop045"
Spring scans @Aspect classes
Parses pointcuts
Finds matching beans
Creates proxy
```

---

# 🧠 Step 2 Proxy Creation

---

```text id="aop046"
Spring replaces actual bean with proxy
```

---

## Types

```text id="aop047"
JDK Proxy → Interface present
CGLIB → No interface
```

---

# 🔥 Runtime Flow

---

```text id="aop048"
Controller → Proxy → Advice → Method → Advice → Response
```

---

# 🔥 Debug Style Execution Example

---

## Code

```java id="aop049"
userService.getUser();
```

---

## Actual Flow

```text id="aop050"
1. Controller calls userService
2. userService is proxy
3. Proxy checks pointcut
4. Executes Before advice
5. Calls actual method
6. Executes After advice
7. Returns result
```

---

## Output

```text id="aop051"
Before method
Inside getUser()
After method
```

---

# 🔥 Internal Proxy Example

---

```java id="aop052"
class ProxyUserService {

    UserService real = new UserService();

    public String getUser() {

        System.out.println("Before method");

        String result = real.getUser();

        System.out.println("After method");

        return result;
    }
}
```

---

# 🔥 Important Rule

---

```text id="aop053"
AOP works only through Spring beans
```

---

## ❌ Not Working

```java id="aop054"
this.getUser();
```

---

## Why

```text id="aop055"
Proxy is bypassed
```

---

# 🔥 Advantages

---

```text id="aop056"
Cleaner code
Reusable logic
Separation of concerns
```

---

# 🔥 Disadvantages

---

```text id="aop057"
Hard to debug
Complex internally
Proxy overhead
```

---

# 🔥 Interview Questions

---

## What is AOP

```text id="aop058"
Separates cross-cutting concerns from business logic
```

---

## How AOP works

```text id="aop059"
Spring creates proxy objects and intercepts method calls
```

---

## What is Pointcut

```text id="aop060"
Expression defining where advice runs
```

---

# 🔥 Memory Trick

---

```text id="aop061"
AOP = Proxy + Pointcut + Advice
```

---

# 🚀 Final Summary

---

* AOP intercepts method execution
* Advice defines behavior
* Pointcut defines where
* Proxy enables interception
* Works only through Spring beans

---

```text id="aop062"
AOP = Intercept → Execute → Continue
```

---
