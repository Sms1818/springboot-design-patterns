# ***Adapter Design Pattern in Java***

---

# 🔥 What is Adapter Pattern?

---

## 📌 Definition

Adapter is a **Structural Design Pattern** that allows incompatible objects/interfaces to work together.

---

## 🧠 Core Idea

```
Client expects Interface A
        ↓
But we only have Interface B
        ↓
Adapter converts B → A
```

---

# 🔥 Real Meaning of Adapter

---

## 📌 Simple Meaning

Adapter acts as a:

```
Translator
```

between two incompatible systems.

---

# 🔥 Real-World Analogy

---

## 📌 Mobile Charger Adapter

```
Indian Charger Plug
        ↓
Power Adapter
        ↓
European Socket
```

---

## 🧠 Explanation

- Charger and socket are incompatible
- Adapter converts connection
- Both systems work together

---

# 🔥 Software Problem

---

## 📌 Existing System

```java
boolean fits(RoundPeg peg)
```

This system only understands:

```text
RoundPeg
```

---

## 📌 But We Have

```text
SquarePeg
```

---

## ❌ Direct Usage

```java
hole.fits(squarePeg);
```

---

## 🚨 Problem

```text
SquarePeg ≠ RoundPeg
```

Interfaces are incompatible.

---

# 🔥 Solution = Adapter

---

## 📌 Adapter Role

```
SquarePeg → Adapter → RoundPeg
```

---

## 🧠 Important

Adapter:
- Pretends to be RoundPeg
- Internally uses SquarePeg

---

# 🔥 Full Workflow

---

```
Client
   ↓
RoundHole.fits(RoundPeg)
   ↓
SquarePegAdapter
   ↓
SquarePeg
```

---

# 🔥 Why Adapter Extends RoundPeg?

---

## 📌 Existing Method

```java
fits(RoundPeg peg)
```

accepts only:

```text
RoundPeg
```

---

## ✅ So Adapter Does

```java
class SquarePegAdapter extends RoundPeg
```

Now adapter IS-A:

```text
RoundPeg
```

---

## 🧠 Result

```java
hole.fits(adapter);
```

works successfully.

---

# 🔥 Why Adapter Wraps SquarePeg?

---

## 📌 Inside Adapter

```java
private SquarePeg peg;
```

---

## 🧠 Meaning

Adapter:
- Looks like RoundPeg externally
- Uses SquarePeg internally

---

# 🔥 MOST IMPORTANT UNDERSTANDING

---

| Outside View | Inside Reality |
|---|---|
| RoundPeg | SquarePeg |

---

# 🔥 Object Adapter Pattern (MOST COMMON)

---

## 📌 Structure

```
Client
   ↓
Adapter implements Target Interface
   ↓
Adapter wraps Adaptee
```

---

# 🔥 Example 1: Round Peg & Square Peg

---

# 💻 RoundHole

```java
public class RoundHole {

    private double radius;

    public RoundHole(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public boolean fits(RoundPeg peg) {
        return this.radius >= peg.getRadius();
    }
}
```

---

# 💻 RoundPeg

```java
public class RoundPeg {

    private double radius;

    public RoundPeg(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }
}
```

---

# 💻 SquarePeg

```java
public class SquarePeg {

    private double width;

    public SquarePeg(double width) {
        this.width = width;
    }

    public double getWidth() {
        return width;
    }
}
```

---

# 💻 Adapter

```java
public class SquarePegAdapter extends RoundPeg {

    private SquarePeg peg;

    public SquarePegAdapter(SquarePeg peg) {
        super(0);
        this.peg = peg;
    }

    @Override
    public double getRadius() {

        return (Math.sqrt(Math.pow((peg.getWidth() / 2), 2) * 2));
    }
}
```

---

# 💻 Client Code

```java
public class Demo {

    public static void main(String[] args) {

        RoundHole hole = new RoundHole(5);

        RoundPeg roundPeg = new RoundPeg(5);

        System.out.println(hole.fits(roundPeg));

        SquarePeg squarePeg = new SquarePeg(5);

        SquarePegAdapter adapter =
                new SquarePegAdapter(squarePeg);

        System.out.println(hole.fits(adapter));
    }
}
```

---

# 🧠 Internal Flow

---

```
RoundHole expects RoundPeg
        ↓
Adapter behaves like RoundPeg
        ↓
Adapter internally uses SquarePeg
        ↓
Compatibility achieved
```

---

# 🔥 Radius Calculation Logic

---

## 📌 Why Needed?

SquarePeg has:

```text
Width
```

But RoundHole expects:

```text
Radius
```

---

## 🧠 Adapter Converts

```
Square Width → Circle Radius
```

---

# 🔥 Example 2: Payment Gateway Adapter

---

## 📌 Existing Application

Application expects:

```java
payInRupees()
```

---

## 📌 Third Party API

```java
payInDollars()
```

---

# 💻 Old Gateway

```java
public class DollarPaymentGateway {

    public void payInDollars(double amount) {
        System.out.println("Paid $" + amount);
    }
}
```

---

# 💻 Target Interface

```java
public interface PaymentProcessor {

    void payInRupees(double amount);
}
```

---

# 💻 Adapter

```java
public class PaymentAdapter implements PaymentProcessor {

    private DollarPaymentGateway gateway;

    public PaymentAdapter(DollarPaymentGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void payInRupees(double amount) {

        double dollars = amount / 83;

        gateway.payInDollars(dollars);
    }
}
```

---

# 💻 Client

```java
public class Client {

    public static void main(String[] args) {

        PaymentProcessor processor =
                new PaymentAdapter(new DollarPaymentGateway());

        processor.payInRupees(8300);
    }
}
```

---

# 🔥 Example 3: XML → JSON Adapter

---

## 📌 Problem

Your app works with:

```text
XML
```

But analytics library accepts:

```text
JSON
```

---

## 🧠 Adapter Solution

```
XML Data
    ↓
XMLToJSONAdapter
    ↓
JSON Analytics Library
```

---

# 🔥 Class Adapter vs Object Adapter

---

# 📌 Object Adapter (Recommended)

Uses:

```text
Composition
```

---

## Structure

```text
Adapter HAS-A Service
```

---

# 📌 Class Adapter

Uses:

```text
Inheritance
```

---

## Structure

```text
Adapter IS-A Client
Adapter IS-A Service
```

---

# 🔥 Object Adapter Example

---

```java
class Adapter implements Target {

    private Service service;
}
```

---

# 🔥 Class Adapter Example

---

```java
class Adapter extends Service implements Target {
}
```

---

# 🔥 Difference Between Object & Class Adapter

---

| Object Adapter | Class Adapter |
|---|---|
| Uses composition | Uses inheritance |
| Flexible | Tightly coupled |
| Preferred | Rarely used |

---

# 🔥 Real Java Examples

---

## 📌 Java Core Library Adapters

```java
Arrays.asList()
Collections.list()
Collections.enumeration()
InputStreamReader
OutputStreamWriter
```

---

# 🔥 Example: InputStreamReader

---

## 📌 Problem

```text
InputStream → byte stream
Reader → character stream
```

---

## ✅ Adapter

```java
InputStreamReader
```

converts:

```text
InputStream → Reader
```

---

# 🔥 When to Use Adapter?

---

## ✅ Use Cases

- Legacy code integration
- Third-party library integration
- Different APIs/interfaces
- Data format conversion

---

# 🔥 Advantages

---

- Reuse existing code
- Loose coupling
- No modification of old code
- Better flexibility

---

# 🔥 Disadvantages

---

- Extra complexity
- More classes
- Indirection increases

---

# 🔥 Adapter vs Decorator vs Proxy

---

| Pattern | Purpose |
|---|---|
| Adapter | Converts interface |
| Decorator | Adds behavior |
| Proxy | Controls access |

---

# 🔥 Adapter vs Facade

---

| Adapter | Facade |
|---|---|
| Makes incompatible classes work | Simplifies subsystem |
| Focuses on compatibility | Focuses on usability |

---

# 🔥 Key Takeaways

---

```
Adapter = Translator
```

---

```
Client expects A
But we have B
Adapter converts B → A
```

---

# 🎯 Interview Questions

---

## 🔹 What is Adapter Pattern?

A structural design pattern that allows incompatible interfaces to work together.

---

## 🔹 Why is Adapter called Wrapper?

Because it wraps incompatible object internally.

---

## 🔹 Why does Adapter extend RoundPeg?

To create compatibility with methods expecting RoundPeg.

---

## 🔹 Why composition is preferred?

Because it provides loose coupling.

---

## 🔹 Real-world use cases?

- Payment gateway integration
- Legacy system integration
- XML ↔ JSON conversion

---

## 🔹 Adapter vs Decorator?

| Adapter | Decorator |
|---|---|
| Changes interface | Enhances behavior |

---

# 🚀 Final Summary

---

```
Adapter Pattern solves incompatibility problem

It wraps incompatible object
and exposes compatible interface

Object Adapter → Composition
Class Adapter → Inheritance

Adapter = Translator
```