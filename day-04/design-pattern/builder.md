# ***Builder Design Pattern***

---

# 🔥 **What is Builder Design Pattern?**

---

## 📌 Definition

Builder is a **creational design pattern** that allows constructing **complex objects step by step**, instead of using a large constructor.

---

## 🧠 Core Idea

```text
Separate object construction from its representation
```

---

```text
Client → Builder → Step-by-step configuration → Final Object
```

---

## 🧠 Simple Meaning

```text
Builder = Build complex objects step-by-step
```

---

# 🔥 **Why Do We Need Builder?**

---

## ❌ Problem Without Builder

Imagine creating a User object:

```java
User user = new User("John", 25, "Pune", "India", "Engineer", true, false);
```

---

## 🚨 What is Wrong?

```text
Too many constructor parameters
Hard to read
Hard to maintain
Error-prone (order matters)
Not all fields are required
```

---

## 🎯 Real Problem

```text
Object has many optional fields
Constructor becomes messy (Telescoping Constructor Problem)
```

---

# 🔥 **Telescoping Constructor Problem**

---

```java
User(String name)
User(String name, int age)
User(String name, int age, String city)
User(String name, int age, String city, String country)
```

---

❌ Problems:

```text
Too many constructors
Confusing API
Not scalable
```

---

# 🔥 **Builder Solution**

---

Builder solves this by:

```text
1. Creating object step-by-step
2. Setting only required fields
3. Finally building the object
```

---

Instead of:

```java
new User("John", 25, "Pune", "India")
```

We do:

```java
User user = new User.Builder()
                .setName("John")
                .setAge(25)
                .setCity("Pune")
                .build();
```

---

# 🔥 **Real-World Analogy**

---

## 🍔 Burger Order

---

You order a burger:

```text
Bread
Cheese (optional)
Veg/Chicken
Sauce
```

---

👉 You choose step-by-step:

```text
Add cheese?
Add sauce?
Extra toppings?
```

---

👉 Finally:

```text
Burger is built
```

---

# 🔥 **Builder Structure**

---

```text
Product (Object to be created)
Builder (Interface / Static Class)
Concrete Builder (Implementation)
(Optional) Director (Controls steps)
```

---

---

# 🔥 **Basic Java Implementation (Most Common)**

---

## 📦 Product Class

```java
public class User {

    private String name;
    private int age;
    private String city;
    private String country;

    private User(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.city = builder.city;
        this.country = builder.country;
    }

    public static class Builder {

        private String name;
        private int age;
        private String city;
        private String country;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAge(int age) {
            this.age = age;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setCountry(String country) {
            this.country = country;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
```

---

## 🧪 Usage

```java
public class Main {
    public static void main(String[] args) {

        User user = new User.Builder()
                .setName("John")
                .setAge(25)
                .setCity("Pune")
                .setCountry("India")
                .build();

        System.out.println(user);
    }
}
```

---

# 🔥 **How This Works**

---

## Step 1

```java
User.Builder builder = new User.Builder();
```

---

## Step 2

```java
builder.setName("John").setAge(25);
```

👉 Method chaining works because:

```java
return this;
```

---

## Step 3

```java
builder.build();
```

---

## Step 4

```java
new User(builder)
```

---

👉 Final object created

---

# 🔥 **Director (Advanced Concept)**

---

Builder can also have a **Director class**.

👉 Director defines:

```text
Order of steps
```

---

Example:

```java
class Director {

    public void buildBasicUser(User.Builder builder) {
        builder.setName("Default")
               .setAge(18);
    }
}
```

---

👉 Optional (not always used) 

---

# 🔥 **When Should You Use Builder?**

---

Use Builder when:

---

## ✅ Use Case 1: Too Many Parameters

```text
Constructor has many arguments
```

---

## ✅ Use Case 2: Many Optional Fields

```text
Not all fields are required
```

---

## ✅ Use Case 3: Complex Object Creation

```text
Object creation involves multiple steps
```

---

## ✅ Use Case 4: Readability Matters

```text
Clear and expressive object creation
```

---

## ✅ Use Case 5: Immutable Objects

```text
Object should not change after creation
```

---

# 🔥 **When NOT to Use Builder**

---

## ❌ Avoid when:

```text
Object is simple
Few fields exist
No optional parameters
```

---

Bad use:

```java
class Point {
    int x, y;
}
```

👉 No need for Builder

---

# 🔥 **How to Identify Builder Problem**

---

Ask:

---

## 🔹 Question 1

```text
Does object have too many parameters?
```

---

## 🔹 Question 2

```text
Are some parameters optional?
```

---

## 🔹 Question 3

```text
Is constructor confusing or hard to use?
```

---

👉 If YES → Use Builder

---

# 🔥 **Factory Method vs Builder**

---

| Feature | Factory Method                | Builder                   |
| ------- | ----------------------------- | ------------------------- |
| Purpose | Decide which object to create | Build object step-by-step |
| Focus   | Object type                   | Object construction       |
| Example | Truck vs Ship                 | User with optional fields |

---

## 🧠 Simple Understanding

```text
Factory Method = Which object?

Builder = How to build object?
```

---

# 🔥 **Builder vs Abstract Factory**

---

| Feature | Builder                  | Abstract Factory                |
| ------- | ------------------------ | ------------------------------- |
| Purpose | Build one complex object | Create multiple related objects |
| Focus   | Construction steps       | Product families                |
| Output  | Single object            | Multiple objects                |

---

---

# 🔥 **Advantages**

---

✔ Handles complex object creation
✔ Improves readability
✔ Avoids telescoping constructors
✔ Supports immutability
✔ Flexible object construction

---

# 🔥 **Disadvantages**

---

❌ More code
❌ Additional classes
❌ Slight complexity increase

---

# 🔥 **Common Mistakes**

---

## ❌ Using Builder for simple objects

---

## ❌ Not making constructor private

---

## ❌ Not returning `this` in builder methods

---

---

# 🔥 **Real Java Examples**

---

Builder pattern is used in:

```java
StringBuilder.append()
```

```java
HttpRequest.Builder
```

```java
StringBuilder builder = new StringBuilder();
builder.append("Hello").append("World");
```

---

👉 Step-by-step building 

---

# 🔥 **Interview Questions**

---

## 🔹 What is Builder Pattern?

```text
A creational pattern used to construct complex objects step by step.
```

---

## 🔹 Why use Builder?

```text
To avoid telescoping constructors and improve readability.
```

---

## 🔹 Builder vs Constructor?

```text
Constructor → all parameters at once
Builder → step-by-step construction
```

---

## 🔹 Can Builder create immutable objects?

```text
Yes
```

---

---

# 🔥 **Memory Trick**

---

```text
Builder = Step-by-step object creation
```

---

---

# 🚀 **Final Summary**

---

* Builder constructs complex objects step-by-step
* Avoids constructor explosion
* Improves readability and flexibility
* Useful when object has many optional fields

---

```text
Builder = Build object gradually, not all at once
```

---
