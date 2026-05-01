# ***Conditional On Property in Spring Boot***

---

# 🔥 What is Conditional On Property

---

## 📌 Definition

Conditional On Property is a Spring Boot annotation that allows **creating beans conditionally based on application properties**.

---

## 🧠 Core Idea

```text
Bean creation depends on configuration
```

---

```text
Property value → Spring → Decides whether to create bean
```

---

## 🧠 Simple Meaning

```text
Create bean only if condition is satisfied
```

---

# 🔥 Why Do We Need This

---

## 📌 Scenario

You have two implementations:

```java
@Component
public class MySQLConnection {}
```

```java
@Component
public class NoSQLConnection {}
```

---

## 🎯 Requirement

```text
Only one bean should be created:
Either MySQLConnection OR NoSQLConnection
```

---

## ❌ Problem

```text
Spring creates both beans
Unnecessary beans in context
Wasted memory
```

---

## 🧠 Real Use Case from Notes

```text
Case 1:
Create only one bean (MySQL or NoSQL)

Case 2:
Different components need different implementations
```

---

# 🔥 Solution Conditional On Property

---

## Example

```java
@Component
@ConditionalOnProperty(name = "db.type", havingValue = "mysql")
public class MySQLConnection {}
```

---

```java
@Component
@ConditionalOnProperty(name = "db.type", havingValue = "nosql")
public class NoSQLConnection {}
```

---

## application.properties

```text
db.type=mysql
```

---

## 🧠 What Happens

```text
Spring checks property value

If db.type = mysql
→ Only MySQLConnection bean is created

If db.type = nosql
→ Only NoSQLConnection bean is created
```

---

📌 As shown in notes, only one bean is initialized based on property value 

---

# 🔥 How This Works

---

## Step-by-Step Flow

```text
Application starts
        ↓
Spring reads application.properties
        ↓
Evaluates @ConditionalOnProperty
        ↓
Checks condition
        ↓
Creates matching bean
        ↓
Skips others
```

---

# 🔥 Advantages

---

```text
Feature toggling
Avoid unnecessary beans
Save memory
Reduce application startup time
```

---

# 🔥 Disadvantages

---

```text
Misconfiguration can happen
Hard to manage when overused
Can create confusion with multiple configs
Adds complexity
```

---

# 🔥 When to Use

---

Use when:

```text
Only one implementation should be active
Behavior depends on configuration
Feature toggling is required
```

---

# 🔥 When Not to Use

---

Avoid when:

```text
Multiple implementations needed at runtime
Dynamic switching required
Logic depends on request input
```

---

# 🔥 Comparison with Other Approaches

---

## Qualifier

```text
Hardcoded selection
```

---

## If Else

```text
Manual logic
Not scalable
```

---

## Conditional On Property

```text
Configuration driven
Cleaner approach
```

---

# 🔥 Relation with Strategy Pattern

---

```text
ConditionalOnProperty → Static selection (startup time)

Strategy Pattern → Dynamic selection (runtime)
```

---

## 🧠 Understanding

```text
Use ConditionalOnProperty when selection is fixed via config

Use Strategy Pattern when selection changes at runtime
```

---

# 🔥 Real World Analogy

---

```text
Switch in config file:

Turn ON MySQL
Turn OFF NoSQL
```

---

# 🔥 Interview Questions

---

## What is Conditional On Property

```text
It is used to create beans conditionally based on application properties.
```

---

## Why use it

```text
To control bean creation using configuration instead of code.
```

---

## When should you avoid it

```text
When dynamic runtime selection is required.
```

---

# 🔥 Memory Trick

---

```text
Property decides bean creation
```

---

# 🚀 Final Summary

---

* Bean creation depends on configuration
* Only matching bean is created
* Reduces unnecessary beans
* Helps in feature toggling
* Works at application startup

---

```text
ConditionalOnProperty = Config driven bean creation
```

---
