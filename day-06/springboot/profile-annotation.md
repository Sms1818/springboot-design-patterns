# ***Profile Annotation in Spring Boot***

---

# 🔥 What is Profile Annotation

---

## 📌 Definition

Profile annotation is used to **create beans or load configurations only for a specific environment**.

---

## 🧠 Core Idea

```text
Environment → Spring → Loads specific configuration and beans
```

---

## 🧠 Simple Meaning

```text
Profile = Environment-based configuration
```

---

# 🔥 Why Do We Need Profiles

---

## 📌 Problem

Different environments need different configurations:

```text
Development
Testing
Production
```

---

## Example from Notes

```text
Dev Machine → devUsername / devPassword
QA Machine → qaUsername / qaPassword
Production → prodUsername / prodPassword
```

---


## 🧠 Other Differences

```text
URL and port
Timeout values
Retry logic
Throttle limits
```

---

# 🔥 Traditional Approach

---

```text
Put everything in application.properties
```

---

## ❌ Problem

```text
Cannot handle multiple environments properly
Becomes messy
Not scalable
```

---

# 🔥 Solution Profiles

---

## 📌 Concept

```text
Different property files for different environments
```

---

## Structure

```text
application.properties
application-dev.properties
application-qa.properties
application-prod.properties
```

---

## 🧠 Meaning

```text
Spring loads file based on active profile
```

---

# 🔥 How Profiles Work

---

## application.properties

```text
spring.profiles.active=dev
```

---

## What Happens

```text
Spring loads:
application.properties
+
application-dev.properties
```

---

# 🔥 Example

---

## application-dev.properties

```text
username=devUsername
password=devPassword
```

---

## application-qa.properties

```text
username=qaUsername
password=qaPassword
```

---

## application-prod.properties

```text
username=prodUsername
password=prodPassword
```

---

# 🔥 Running Application with Profile

---

## Using Command Line

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

---

## Alternative

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

---

📌 As shown in notes, profile can be passed during startup 

---

# 🔥 Using Profile Annotation

---

## Example

```java
@Component
@Profile("dev")
public class DevDatabaseConfig {}
```

---

```java
@Component
@Profile("prod")
public class ProdDatabaseConfig {}
```

---

## 🧠 What Happens

```text
If active profile = dev
→ Only DevDatabaseConfig bean is created

If active profile = prod
→ Only ProdDatabaseConfig bean is created
```

---

# 🔥 Multiple Profiles

---

## Example

```text
spring.profiles.active=prod,qa
```

---

## 🧠 Meaning

```text
Spring loads multiple profile configurations together
```

---

📌 As shown in notes, multiple profiles can be activated at the same time 

---

# 🔥 Real Use Case

---

## Scenario

```text
Two applications share same codebase
But require different beans
```

---

## Example

```java
@Component
@Profile("app1")
public class App1Service {}
```

---

```java
@Component
@Profile("app2")
public class App2Service {}
```

---

## application.properties

```text
spring.profiles.active=app1
```

---

## 🧠 Result

```text
Only App1Service bean is created
```

---

📌 As shown in notes, profile helps isolate beans for different applications 

---

# 🔥 How This Works Internally

---

## Flow

```text
Application starts
        ↓
Spring reads active profile
        ↓
Filters beans based on @Profile
        ↓
Loads only matching beans
```

---

# 🔥 Advantages

---

```text
Clean separation of environments
Better configuration management
Avoid unnecessary beans
Flexible deployment
```

---

# 🔥 Disadvantages

---

```text
Misconfiguration can cause issues
Multiple profiles increase complexity
Hard to manage if overused
```

---

# 🔥 When to Use Profiles

---

Use when:

```text
Different environments require different configs
Different beans needed per environment
Same codebase used across environments
```

---

# 🔥 When Not to Use

---

Avoid when:

```text
Only one environment exists
No variation in configuration
```

---

# 🔥 Comparison with Conditional On Property

---

```text
Profile → Environment-based selection

ConditionalOnProperty → Property-based selection
```

---

## 🧠 Understanding

```text
Use Profile for environment switching

Use ConditionalOnProperty for feature toggling
```

---

# 🔥 Interview Questions

---

## What is Profile annotation

```text
It is used to load beans and configurations based on the active environment.
```

---

## How to activate profile

```text
Using spring.profiles.active property or command line
```

---

## Can multiple profiles be active

```text
Yes
```

---

## Profile vs ConditionalOnProperty

```text
Profile is environment-based
ConditionalOnProperty is configuration-based
```

---

# 🔥 Memory Trick

---

```text
Profile = Environment switch
```

---

# 🚀 Final Summary

---

* Profiles manage environment-specific configurations
* Separate property files for each environment
* Beans can be conditionally created using @Profile
* Active profile decides what gets loaded
* Supports multiple environments and applications

---

```text
Profile = Load config and beans based on environment
```

---
