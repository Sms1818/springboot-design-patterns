# ***Abstract Factory Design Pattern***

---

# 🔥 **What is Abstract Factory Design Pattern?**

---

## **Definition**

Abstract Factory is a **creational design pattern** that provides an interface for creating **families of related objects**, without specifying their concrete classes.

---

## 🧠 **Core Idea**

```text
Factory Method → creates ONE object

Abstract Factory → creates FAMILY of related objects
```

---

```text
Client → Abstract Factory → Multiple Product Objects
```

---

👉 Client does not know concrete classes  
👉 Client only depends on interfaces  
👉 Factory ensures all objects belong to SAME FAMILY  

---

# 🔥 **Simple Meaning**

---

```text
Abstract Factory = Factory of related objects
```

---

Example:

```text
ModernFactory → ModernChair + ModernSofa + ModernTable

VictorianFactory → VictorianChair + VictorianSofa + VictorianTable
```

---

👉 Client only talks to:

```text
Chair, Sofa, Table (interfaces)
```

---

# 🔥 **Why Do We Need Abstract Factory?**

---

## ❌ Problem Without Abstract Factory

You are building a furniture app.

Initially:

```java
Chair chair = new ModernChair();
Sofa sofa = new ModernSofa();
```

Works fine.

---

## 🚨 New Requirement

Now you support multiple styles:

```text
Modern
Victorian
ArtDeco
```

---

Your code becomes:

```java
Chair chair = new ModernChair();
Sofa sofa = new VictorianSofa(); // ❌ mismatch
```

---

## ❌ What is Wrong?

---

```text
Objects are NOT compatible
```

---

Problems:

```text
Mixing product variants
UI inconsistency
Hard to maintain
Tight coupling
Too many condition checks
```

---

# 🔥 **The Main Problem**

---

```text
We need multiple objects that must always MATCH each other
```

---

Example:

```text
Chair + Sofa + Table must be SAME STYLE
```

---

# 🔥 **Abstract Factory Solution**

---

Abstract Factory says:

```text
Create a factory that produces FULL FAMILY of objects
```

---

Instead of:

```java
new ModernChair()
new VictorianSofa()
```

We do:

```java
FurnitureFactory factory = new ModernFactory();

factory.createChair();
factory.createSofa();
```

---

👉 Now mismatch is IMPOSSIBLE

---

# 🔥 **Real-World Analogy**

---

## 🍔 Food Brand Example

---

### McDonald's Factory

```text
Burger + Fries + Coke (All McD style)
```

---

### Dominos Factory

```text
Pizza + Garlic Bread + Coke (All Dominos style)
```

---

## ❌ You NEVER do:

```text
Burger (McD) + Garlic Bread (Dominos) ❌
```

---

👉 Each brand = Abstract Factory

---

# 🔥 **Abstract Factory Structure**

---

```text
Abstract Products
        ↑
Concrete Products (Variants)

Abstract Factory
        ↑
Concrete Factories
```

---

## 🔹 1. Abstract Products

```java
interface Chair {
    void sitOn();
}

interface Sofa {
    void lieOn();
}
```

---

## 🔹 2. Concrete Products

```java
class ModernChair implements Chair {
    public void sitOn() {
        System.out.println("Sitting on Modern Chair");
    }
}
```

```java
class VictorianChair implements Chair {
    public void sitOn() {
        System.out.println("Sitting on Victorian Chair");
    }
}
```

---

## 🔹 3. Abstract Factory

```java
interface FurnitureFactory {
    Chair createChair();
    Sofa createSofa();
}
```

---

## 🔹 4. Concrete Factories

```java
class ModernFactory implements FurnitureFactory {

    public Chair createChair() {
        return new ModernChair();
    }

    public Sofa createSofa() {
        return new ModernSofa();
    }
}
```

```java
class VictorianFactory implements FurnitureFactory {

    public Chair createChair() {
        return new VictorianChair();
    }

    public Sofa createSofa() {
        return new VictorianSofa();
    }
}
```

---

# 🔥 **Complete Java Example**

---

```java
interface Chair {
    void sitOn();
}

interface Sofa {
    void lieOn();
}
```

```java
class ModernChair implements Chair {
    public void sitOn() {
        System.out.println("Modern Chair");
    }
}

class ModernSofa implements Sofa {
    public void lieOn() {
        System.out.println("Modern Sofa");
    }
}
```

```java
class VictorianChair implements Chair {
    public void sitOn() {
        System.out.println("Victorian Chair");
    }
}

class VictorianSofa implements Sofa {
    public void lieOn() {
        System.out.println("Victorian Sofa");
    }
}
```

```java
interface FurnitureFactory {
    Chair createChair();
    Sofa createSofa();
}
```

```java
class ModernFactory implements FurnitureFactory {

    public Chair createChair() {
        return new ModernChair();
    }

    public Sofa createSofa() {
        return new ModernSofa();
    }
}
```

```java
class VictorianFactory implements FurnitureFactory {

    public Chair createChair() {
        return new VictorianChair();
    }

    public Sofa createSofa() {
        return new VictorianSofa();
    }
}
```

```java
public class Demo {

    public static void main(String[] args) {

        FurnitureFactory factory = new ModernFactory();

        Chair chair = factory.createChair();
        Sofa sofa = factory.createSofa();

        chair.sitOn();
        sofa.lieOn();
    }
}
```

---

# 🔥 **How This Code Works**

---

## Step 1

```java
FurnitureFactory factory = new ModernFactory();
```

---

## Step 2

```java
factory.createChair();
```

Returns:

```text
ModernChair
```

---

## Step 3

```java
factory.createSofa();
```

Returns:

```text
ModernSofa
```

---

👉 Both belong to SAME FAMILY

---

# 🔥 **Most Important Point**

---

```text
Factory Method → One object

Abstract Factory → Group of related objects
```

---

# 🔥 **Before vs After**

---

## ❌ Before

```java
new ModernChair()
new VictorianSofa()
```

---

## ✅ After

```java
factory.createChair()
factory.createSofa()
```

---

👉 No mismatch possible

---

# 🔥 **When Should You Use Abstract Factory?**

---

Use Abstract Factory when:

---

## ✅ Use Case 1: Multiple Related Objects

```text
Chair + Sofa + Table
Button + Checkbox
```

---

## ✅ Use Case 2: Objects Must Be Compatible

```text
Same theme / same family
```

---

## ✅ Use Case 3: System Has Variants

```text
Windows / Mac
Light theme / Dark theme
```

---

## ✅ Use Case 4: Avoid Hardcoding

```java
new WindowsButton()
new MacButton()
```

---

# 🔥 **How to Identify Abstract Factory Problem**

---

Ask:

---

## 🔹 Question 1

```text
Am I creating multiple related objects together?
```

---

## 🔹 Question 2

```text
Do these objects need to match?
```

---

## 🔹 Question 3

```text
Are there multiple variants (themes)?
```

---

## 🔹 Question 4

```text
Am I mixing objects incorrectly?
```

---

👉 If YES → Use Abstract Factory

---

# 🔥 **How to Reach Abstract Factory**

---

## Stage 1

```java
new ModernChair()
new ModernSofa()
```

---

## Stage 2

Add variants:

```text
Victorian
```

---

## Stage 3

Code becomes:

```java
if (type.equals("modern")) {
    new ModernChair();
}
```

---

## Stage 4

Multiple products + variants

---

## Stage 5

Introduce:

```text
Factory that creates FULL FAMILY
```

---

# 🔥 **Factory Method vs Abstract Factory**

---

| Feature | Factory Method | Abstract Factory |
|--------|--------------|----------------|
| Creates | One object | Multiple objects |
| Focus | Product | Product family |
| Example | Transport | Furniture set |

---

# 🔥 **Advantages**

---

✔ Ensures compatibility  
✔ Loose coupling  
✔ Scalable  
✔ Clean architecture  
✔ Follows Open/Closed Principle  

---

# 🔥 **Disadvantages**

---

❌ More classes  
❌ Complex structure  
❌ Hard to understand initially  

---

# 🔥 **Common Mistakes**

---

## ❌ Mixing variants

---

## ❌ Not using interfaces

---

## ❌ Creating objects directly

---

# 🔥 **Interview Questions**

---

## 🔹 What is Abstract Factory?

```text
Creates families of related objects without specifying concrete classes
```

---

## 🔹 Difference from Factory Method?

```text
Factory Method → single object
Abstract Factory → group of objects
```

---

## 🔹 Real-world example?

```text
UI components (Windows vs Mac)
```

---

# 🚀 **Final Summary**

---

* Abstract Factory creates families of objects  
* Ensures consistency  
* Client depends on abstraction  
* Best for scalable systems  

---

```text
Abstract Factory = Factory of Families
```

---