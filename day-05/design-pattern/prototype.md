# ***Prototype Design Pattern***

---

# 🔥 **What is Prototype Design Pattern?**

---

## 📌 Definition

Prototype is a **creational design pattern** that allows creating **new objects by copying existing objects**, instead of creating them from scratch.

---

## 🧠 Core Idea

```text
Clone existing object instead of creating new object
```

---

```text
Client → Existing Object → clone() → New Object
```

---

## 🧠 Simple Meaning

```text
Prototype = Copy object instead of creating it
```

---

# 🔥 **Why Do We Need Prototype?**

---

## ❌ Problem Without Prototype

Imagine creating a complex object:

```java
User user1 = new User();
user1.setName("John");
user1.setRole("Admin");
user1.setPermissions(loadPermissions());
user1.setSettings(loadSettings());
```

---

Now you need a similar object:

```java
User user2 = new User();
user2.setName("John");
user2.setRole("Admin");
user2.setPermissions(loadPermissions());
user2.setSettings(loadSettings());
```

---

## 🚨 What is Wrong?

```text
Repetitive initialization
Complex setup repeated
Time-consuming object creation
Error-prone
```

---

## 🎯 Real Problem

```text
Object creation is expensive and complex
```

---

# 🔥 **Prototype Solution**

---

Instead of creating again:

```java
User user2 = new User(...);
```

We do:

```java
User user2 = user1.clone();
```

---

## 🧠 Meaning

```text
Reuse existing object configuration by copying it
```

---

# 🔥 **Real-World Analogy**

---

## 🧬 Cell Division

---

```text
One cell → divides → creates identical cell
```

---

👉 Original = Prototype
👉 New cell = Clone

---

# 🔥 **Prototype Structure**

---

```text
Prototype Interface → clone()
Concrete Prototype → implements clone()
Client → uses clone()
(Optional) Prototype Registry → stores reusable objects
```

---

---

# 🔥 **Basic Java Implementation**

---

## 📦 Prototype Interface

```java
public interface Shape {
    Shape clone();
}
```

---

## 📦 Concrete Prototype

```java
public class Circle implements Shape {

    private int radius;
    private String color;

    public Circle(int radius, String color) {
        this.radius = radius;
        this.color = color;
    }

    // Copy constructor
    public Circle(Circle source) {
        this.radius = source.radius;
        this.color = source.color;
    }

    @Override
    public Shape clone() {
        return new Circle(this);
    }
}
```

---

## 🧪 Usage

```java
public class Main {
    public static void main(String[] args) {

        Circle c1 = new Circle(10, "Red");

        Circle c2 = (Circle) c1.clone();

        System.out.println(c1);
        System.out.println(c2);
    }
}
```

---

# 🔥 **How This Works**

---

## Step 1

```java
Circle c1 = new Circle(10, "Red");
```

---

## Step 2

```java
Circle c2 = c1.clone();
```

---

## Step 3

```java
new Circle(this);
```

---

## Step 4

```text
New object created with same data
```

---

👉 Final:

```text
c1 ≠ c2 (different objects)
c1 == c2 data (same values)
```

---

# 🔥 **Deep Copy vs Shallow Copy**

---

## 🔹 Shallow Copy

```text
Copies references
```

---

Example:

```java
user1.address == user2.address ❌
```

---

## 🔹 Deep Copy

```text
Copies actual objects
```

---

Example:

```java
user1.address != user2.address ✅
```

---

## 🧠 Important

```text
Prototype pattern often requires deep copy
```

---

# 🔥 **Prototype Registry (Advanced Concept)**

---

Instead of creating objects manually:

```text
Store ready-made objects and clone them
```

---

## Example

```java
class ShapeCache {

    private static Map<String, Shape> cache = new HashMap<>();

    static {
        cache.put("circle", new Circle(10, "Red"));
    }

    public static Shape getShape(String type) {
        return cache.get(type).clone();
    }
}
```

---

## Usage

```java
Shape shape = ShapeCache.getShape("circle");
```

---

## 🧠 Meaning

```text
Pre-built objects reused via cloning
```

---

# 🔥 **When Should You Use Prototype?**

---

## ✅ Use Case 1: Expensive Object Creation

```text
Object creation takes time/resources
```

---

## ✅ Use Case 2: Complex Initialization

```text
Too many fields to initialize
```

---

## ✅ Use Case 3: Similar Objects Needed

```text
Objects differ slightly
```

---

## ✅ Use Case 4: Unknown Object Type

```text
Only interface is known
```

---

# 🔥 **When NOT to Use Prototype**

---

## ❌ Avoid when:

```text
Object is simple
Cloning is complex
Deep copy is difficult
```

---

# 🔥 **How to Identify Prototype Problem**

---

## 🔹 Question 1

```text
Is object creation expensive?
```

---

## 🔹 Question 2

```text
Are we repeating initialization logic?
```

---

## 🔹 Question 3

```text
Do we need similar objects frequently?
```

---

👉 If YES → Use Prototype

---

# 🔥 **Factory Method vs Prototype**

---

| Feature  | Factory Method        | Prototype          |
| -------- | --------------------- | ------------------ |
| Purpose  | Create object         | Copy object        |
| Approach | new keyword           | clone()            |
| Focus    | Object creation logic | Object duplication |

---

## 🧠 Simple Understanding

```text
Factory Method = Which object to create

Prototype = Copy existing object
```

---

# 🔥 **Prototype vs Builder**

---

| Feature | Prototype        | Builder                   |
| ------- | ---------------- | ------------------------- |
| Purpose | Copy object      | Build object step-by-step |
| Focus   | Duplication      | Construction              |
| Output  | Copy of existing | Newly built object        |

---

---

# 🔥 **Advantages**

---

✔ Faster object creation
✔ Avoids repeated initialization
✔ Reduces code duplication
✔ Works with unknown object types
✔ Simplifies complex object creation

---

# 🔥 **Disadvantages**

---

❌ Deep cloning can be complex
❌ Hard to handle circular references
❌ Debugging cloned objects can be tricky

---

# 🔥 **Common Mistakes**

---

## ❌ Not implementing deep copy

---

## ❌ Using prototype for simple objects

---

## ❌ Forgetting clone() override

---

---

# 🔥 **Real Java Examples**

---

Prototype is available in Java:

```java
Object.clone()
Cloneable interface
```

---

Example:

```java
ArrayList list2 = (ArrayList) list1.clone();
```

---

👉 Object copying

---

# 🔥 **Interview Questions**

---

## 🔹 What is Prototype Pattern?

```text
A creational design pattern that creates objects by cloning existing ones.
```

---

## 🔹 Why use Prototype?

```text
To avoid expensive object creation and repeated initialization.
```

---

## 🔹 Difference between shallow and deep copy?

```text
Shallow copies references, deep copies actual objects.
```

---

## 🔹 Prototype vs Factory?

```text
Factory creates object, Prototype copies object.
```

---

---

# 🔥 **Memory Trick**

---

```text
Prototype = Photocopy machine
```

---

---

# 🚀 **Final Summary**

---

* Prototype creates objects by cloning
* Avoids repeated initialization
* Useful for complex and expensive objects
* Supports deep and shallow copy
* Works with unknown object types

---

```text
Prototype = Copy instead of Create
```

---
