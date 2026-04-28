# ***Singleton Design Pattern***

---

# 🔥 **What is Singleton Design Pattern?**

---

## 📌 Definition

Singleton is a **creational design pattern** that ensures a class has **only one instance** and provides a **global access point** to it.

---

## 🧠 Core Idea

```text
Create ONLY ONE object and reuse it everywhere
```

---

```text
Client → getInstance() → Same Object (Always)
```

---

## 🧠 Simple Meaning

```text
Singleton = One Object for entire application
```

---

# 🔥 **Why Do We Need Singleton?**

---

## ❌ Problem Without Singleton

```java
Database db1 = new Database();
Database db2 = new Database();
```

---

## 🚨 What is Wrong?

---

```text
Multiple objects created unnecessarily
Wastes memory/resources
Inconsistent state across application
Hard to manage shared resources
```

---

## 🎯 Real Need

Some objects should be:

```text
Created once
Shared globally
Reused everywhere
```

---

---

# 🔥 **Main Problem Statement**

---

```text
We need exactly ONE instance of a class
and it should be accessible globally
```

---

---

# 🔥 **Singleton Solution**

---

Singleton solves this by:

```text
1. Making constructor private
2. Providing static method to access instance
3. Storing single instance in static variable
```

---

---

# 🔥 **Real-World Analogy**

---

## 🏛️ Government Example

```text
A country has only ONE government
```

---

👉 Everyone interacts with:

```text
Same government instance
```

---

---

# 🔥 **Structure**

---

```text
Singleton Class
   ↓
Private Constructor
   ↓
Static Instance
   ↓
getInstance() Method
```

---

---

# 🔥 **Basic Implementation (Naive Singleton)**

---

```java
public class Singleton {

    private static Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {

        if (instance == null) {
            instance = new Singleton();
        }

        return instance;
    }
}
```

---

# 🔥 **Complete Example**

---

```java
public class Singleton {

    private static Singleton instance;

    public String value;

    private Singleton(String value) {
        this.value = value;
    }

    public static Singleton getInstance(String value) {
        if (instance == null) {
            instance = new Singleton(value);
        }
        return instance;
    }
}
```

---

```java
public class Demo {

    public static void main(String[] args) {

        Singleton s1 = Singleton.getInstance("FOO");
        Singleton s2 = Singleton.getInstance("BAR");

        System.out.println(s1.value);
        System.out.println(s2.value);
    }
}
```

---

## ✅ Output

```text
FOO
FOO
```

---

## 🧠 Explanation

```text
First call creates object
Second call reuses same object
```

---

---

# 🔥 **Problem in Multithreading**

---

## ❌ Issue

```text
Multiple threads call getInstance() simultaneously
```

---

## Result

```text
Multiple objects created ❌
```

---

## Example Output

```text
FOO
BAR
```

---

👉 Singleton breaks

---

---

# 🔥 **Thread-Safe Singleton (Double Checked Locking)**

---

```java
public class Singleton {

    private static volatile Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {

        if (instance == null) {

            synchronized (Singleton.class) {

                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

---

## 🧠 Key Concepts

```text
volatile → prevents memory issues
synchronized → ensures only one thread creates object
double-check → improves performance
```

---

---

# 🔥 **Best Implementation (Bill Pugh Singleton)**

---

```java
public class BillPughSingleton {

    private BillPughSingleton() {}

    private static class Helper {
        private static final BillPughSingleton INSTANCE =
                new BillPughSingleton();
    }

    public static BillPughSingleton getInstance() {
        return Helper.INSTANCE;
    }
}
```

---

## 🧠 Why Best?

```text
Thread-safe
Lazy initialization
No synchronization overhead
Clean and efficient
```

---

---

# 🔥 **Singleton Using Enum (BEST in Java)**

---

```java
enum Singleton {

    INSTANCE;

    public void showMessage() {
        System.out.println("Singleton using Enum");
    }
}
```

---

## Usage

```java
Singleton s1 = Singleton.INSTANCE;
Singleton s2 = Singleton.INSTANCE;

System.out.println(s1 == s2);
```

---

## Output

```text
true
```

---

## 🧠 Why Enum is Best?

```text
Thread-safe
Handles serialization automatically
Prevents reflection attacks
Simplest implementation
```

---

---

# 🔥 **How Singleton Works**

---

## Flow

```text
Call getInstance()
        ↓
Check if instance exists
        ↓
If NO → create object
If YES → return existing object
```

---

---

# 🔥 **Before vs After**

---

## ❌ Before

```java
new Database()
new Database()
```

---

## ✅ After

```java
Database.getInstance()
Database.getInstance()
```

---

👉 Always same object

---

---

# 🔥 **When Should You Use Singleton?**

---

Use Singleton when:

---

## ✅ Use Case 1: Shared Resource

```text
Database connection
Thread pool
Cache
```

---

## ✅ Use Case 2: Global Access Needed

```text
Configuration manager
Logger
```

---

## ✅ Use Case 3: Expensive Object Creation

```text
Heavy initialization objects
```

---

## ✅ Use Case 4: Only One Instance Makes Sense

```text
Printer spooler
Application settings
```

---

---

# 🔥 **When NOT to Use Singleton**

---

## ❌ Avoid when:

```text
Object does not need to be global
Multiple instances are valid
You need flexibility/testing
```

---

👉 Singleton can act like:

```text
Hidden global variable ❌
```

---

---

# 🔥 **How to Identify Singleton Problem**

---

Ask:

---

## 🔹 Question 1

```text
Should there be ONLY ONE instance?
```

---

## 🔹 Question 2

```text
Should it be globally accessible?
```

---

## 🔹 Question 3

```text
Will multiple instances cause problems?
```

---

👉 If YES → Singleton fits

---

---

# 🔥 **How to Reach Singleton (Thinking Process)**

---

## Stage 1

```java
new Database()
new Database()
```

---

## Stage 2

```text
Multiple objects causing issues
```

---

## Stage 3

```text
We realize only ONE instance is needed
```

---

## Stage 4

```text
Restrict object creation
Provide global access
```

---

👉 Result → Singleton Pattern

---

---

# 🔥 **Advantages**

---

✔ Only one instance  
✔ Saves memory  
✔ Global access  
✔ Lazy initialization possible  

---

---

# 🔥 **Disadvantages**

---

❌ Violates Single Responsibility Principle  
❌ Hard to unit test  
❌ Hidden dependencies  
❌ Tight coupling  

---

---

# 🔥 **Common Mistakes**

---

## ❌ Not handling multithreading

---

## ❌ Using Singleton everywhere

---

## ❌ Treating it like global variable

---

---

# 🔥 **Interview Questions**

---

## 🔹 What is Singleton?

```text
Ensures one instance and provides global access
```

---

## 🔹 Why constructor is private?

```text
To prevent object creation using new
```

---

## 🔹 Best implementation?

```text
Enum Singleton or Bill Pugh
```

---

## 🔹 Problem with Singleton?

```text
Hard to test, tight coupling
```

---

---

# 🔥 **Memory Trick**

---

```text
ONE instance + GLOBAL access = Singleton
```

---

---

# 🚀 **Final Summary**

---

* Singleton ensures one instance  
* Provides global access  
* Used for shared resources  
* Must handle multithreading  
* Best → Enum or Bill Pugh  

---

```text
Singleton = One Object + Global Access
```

---