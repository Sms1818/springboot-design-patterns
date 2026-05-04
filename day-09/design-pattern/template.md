# ***Template Method Design Pattern***

---

# 🔥 Definition

Template Method is a behavioral design pattern that defines the skeleton of an algorithm in a base class and allows subclasses to override specific steps without changing the overall structure.

---

# 🧠 Core Idea

```text
Define the flow once → let subclasses customize steps
```

---

# 🧠 Simple Meaning

```text
Fix the process, allow flexibility in steps
```

---

# 🔥 Problem

---

## 📌 Scenario

You are building a data processing system:

- PDF processing  
- CSV processing  
- DOC processing  

---

## 🚨 Issue

Each class contains:

```text
Same processing flow
Different implementations
```

---

## Example Flow

```text
Open file
Parse data
Analyze data
Generate report
Close file
```

---

## ❌ Problems

- Code duplication  
- Hard to maintain  
- Changes required in multiple places  
- Too many if-else conditions  

---

# 🔥 Solution

---

## 📌 Idea

Break algorithm into steps and define:

```text
Template Method → controls overall flow
Steps → implemented by subclasses
```

---

## 🧠 Structure

```text
Abstract Class:
    templateMethod() → defines algorithm

Concrete Classes:
    implement specific steps
```

---

# 🔥 Real-World Analogy

---

## 🏠 House Construction

```text
Foundation → Walls → Wiring → Plumbing → Finishing
```

You can:

```text
Change wiring
Change design
BUT cannot change overall sequence
```

---

# 🔥 Structure of Pattern

---

## 1. Abstract Class

Defines:

- Template method  
- Abstract steps  
- Optional/default steps  

---

## 2. Concrete Classes

- Implement abstract steps  
- Cannot modify template method  

---

# 🔥 Types of Steps

---

## 1. Abstract Steps

```text
Must be implemented by subclasses
```

---

## 2. Default Steps

```text
Already implemented
Can be overridden
```

---

## 3. Hooks

```text
Optional steps (empty methods)
Used for extension
```

---

# 🔥 Example (Java)

---

## Abstract Class

```java
abstract class DataProcessor {

    public final void process() {
        readData();
        processData();
        saveData();
    }

    abstract void readData();
    abstract void processData();

    void saveData() {
        System.out.println("Saving data (common logic)");
    }
}
```

---

## Concrete Class 1

```java
class CSVProcessor extends DataProcessor {

    void readData() {
        System.out.println("Reading CSV");
    }

    void processData() {
        System.out.println("Processing CSV");
    }
}
```

---

## Concrete Class 2

```java
class PDFProcessor extends DataProcessor {

    void readData() {
        System.out.println("Reading PDF");
    }

    void processData() {
        System.out.println("Processing PDF");
    }
}
```

---

## Usage

```java
public class Main {
    public static void main(String[] args) {
        DataProcessor processor = new CSVProcessor();
        processor.process();
    }
}
```

---

## 🧠 Output

```text
Reading CSV
Processing CSV
Saving data
```

---

# 🔥 Real Example (Social Network)

---

## Base Class

```java
abstract class Network {

    public boolean post(String message) {
        if (logIn()) {
            boolean result = sendData(message);
            logOut();
            return result;
        }
        return false;
    }

    abstract boolean logIn();
    abstract boolean sendData(String data);
    abstract void logOut();
}
```

---

## Facebook Implementation

```java
class Facebook extends Network {

    boolean logIn() {
        System.out.println("Login Facebook");
        return true;
    }

    boolean sendData(String data) {
        System.out.println("Posting to Facebook");
        return true;
    }

    void logOut() {
        System.out.println("Logout Facebook");
    }
}
```

---

## 🧠 Insight

```text
post() = template method
logIn/sendData/logOut = steps
```

---

# 🔥 Internal Working

---

## Flow

```text
Client calls template method
        ↓
Template method executes steps in order
        ↓
Subclasses override specific steps
        ↓
Final output produced
```

---

## Important Rule

```text
Template method should NOT be overridden
```

---

# 🔥 When to Use

---

- Multiple classes share same algorithm  
- Only few steps differ  
- Want to remove duplication  
- Want controlled extension  

---

# 🔥 Advantages

---

- Reduces code duplication  
- Centralized algorithm control  
- Easy to extend  
- Enforces structure  

---

# 🔥 Disadvantages

---

- Rigid structure  
- Hard to modify if too many steps  
- Less flexible  

---

# 🔥 Relation with Other Patterns

---

## Template vs Strategy

```text
Template → Inheritance (compile-time)
Strategy → Composition (runtime)
```

---

## Template vs Factory

```text
Factory Method is a special case of Template Method
```

---

# 🔥 Real Usage in Java

---

- InputStream / OutputStream  
- AbstractList / AbstractMap  
- HttpServlet (doGet, doPost)  

---

# 🔥 Identification Trick

---

```text
If a method calls multiple steps → Template Method
```

---

# 🔥 Memory Trick

---

```text
Template = Fixed Flow + Custom Steps
```

---

# 🚀 Final Summary

---

- Defines algorithm structure  
- Subclasses customize steps  
- Flow remains unchanged  
- Based on inheritance  
- Eliminates duplication  

---