# ***Factory Method Design Pattern***

---

# 🔥 **What is Factory Method Design Pattern?**

---

## **Definition**

Factory Method is a **creational design pattern** that provides an interface for creating objects in a superclass, but allows subclasses to decide which class object should be created.

---

## 🧠 **Core Idea**

```text
Do not create objects directly using new everywhere.

Instead:

Client Code → Creator → Factory Method → Product Object
```

---

👉 The client does not know the exact concrete class.  
👉 The client only depends on an interface or abstract class.  
👉 Object creation logic is moved to a separate factory method.

---

# 🔥 **Simple Meaning**

---

```text
Factory Method = Let subclasses decide which object to create
```

---

Example:

```text
RoadLogistics → creates Truck
SeaLogistics  → creates Ship
AirLogistics  → creates Plane
```

But client code only talks to:

```text
Transport
```

---

# 🔥 **Why Do We Need Factory Method?**

---

## ❌ Problem Without Factory Method

Imagine you are building a logistics application.

At first, your app only supports truck delivery.

```java
class LogisticsService {

    public void deliver() {
        Truck truck = new Truck();
        truck.deliver();
    }
}
```

This looks fine.

But later, your app needs to support:

```text
Truck delivery
Ship delivery
Plane delivery
```

Now your code becomes:

```java
class LogisticsService {

    public void deliver(String type) {

        if (type.equals("truck")) {
            Truck truck = new Truck();
            truck.deliver();
        } else if (type.equals("ship")) {
            Ship ship = new Ship();
            ship.deliver();
        } else if (type.equals("plane")) {
            Plane plane = new Plane();
            plane.deliver();
        }
    }
}
```

---

## ❌ What is Wrong Here?

---

```text
Client code is tightly coupled with concrete classes
```

---

Problems:

```text
Too many if-else conditions
Hard to add new transport types
Violates Open/Closed Principle
Object creation logic mixed with business logic
Testing becomes harder
Code becomes messy as application grows
```

---

# 🔥 **The Main Problem**

---

```text
The class that USES the object is also responsible for CREATING the object
```

---

That means one class has two responsibilities:

```text
1. Business logic
2. Object creation logic
```

This violates:

```text
Single Responsibility Principle
```

---

# 🔥 **Factory Method Solution**

---

Factory Method says:

```text
Move object creation into a separate method
Let subclasses override that method
Return objects using a common interface
```

---

Instead of this:

```java
Truck truck = new Truck();
```

We do this:

```java
Transport transport = createTransport();
```

---

Now the main business logic does not care whether the object is:

```text
Truck
Ship
Plane
```

It only cares that the object can:

```text
deliver()
```

---

# 🔥 **Real-World Analogy**

---

## 🍕 Food Delivery App

Imagine you order food from an app.

You choose:

```text
Pizza
Burger
Pasta
```

You do not create the food yourself.

The restaurant kitchen decides which item to prepare.

```text
You → Order food
Kitchen → Creates actual food item
You → Receive food
```

---

In programming:

```text
Client → Requests object
Factory Method → Creates object
Client → Uses object through interface
```

---

# 🔥 **Factory Method Structure**

---

```text
Product Interface
        ↑
Concrete Products

Creator Abstract Class
        ↑
Concrete Creators
```

---

## 🔹 1. Product Interface

Defines common behavior for all products.

```java
interface Transport {
    void deliver();
}
```

---

## 🔹 2. Concrete Products

Actual objects created by factory method.

```java
class Truck implements Transport {

    @Override
    public void deliver() {
        System.out.println("Delivering by road using Truck");
    }
}
```

```java
class Ship implements Transport {

    @Override
    public void deliver() {
        System.out.println("Delivering by sea using Ship");
    }
}
```

---

## 🔹 3. Creator Abstract Class

Contains the factory method.

```java
abstract class Logistics {

    public abstract Transport createTransport();

    public void planDelivery() {
        Transport transport = createTransport();
        transport.deliver();
    }
}
```

---

## 🔹 4. Concrete Creators

Subclasses decide which object to create.

```java
class RoadLogistics extends Logistics {

    @Override
    public Transport createTransport() {
        return new Truck();
    }
}
```

```java
class SeaLogistics extends Logistics {

    @Override
    public Transport createTransport() {
        return new Ship();
    }
}
```

---

# 🔥 **Complete Java Example**

---

```java
interface Transport {
    void deliver();
}
```

```java
class Truck implements Transport {

    @Override
    public void deliver() {
        System.out.println("Delivering by road using Truck");
    }
}
```

```java
class Ship implements Transport {

    @Override
    public void deliver() {
        System.out.println("Delivering by sea using Ship");
    }
}
```

```java
abstract class Logistics {

    public abstract Transport createTransport();

    public void planDelivery() {
        Transport transport = createTransport();
        transport.deliver();
    }
}
```

```java
class RoadLogistics extends Logistics {

    @Override
    public Transport createTransport() {
        return new Truck();
    }
}
```

```java
class SeaLogistics extends Logistics {

    @Override
    public Transport createTransport() {
        return new Ship();
    }
}
```

```java
public class FactoryMethodDemo {

    public static void main(String[] args) {

        Logistics logistics = new RoadLogistics();
        logistics.planDelivery();

        logistics = new SeaLogistics();
        logistics.planDelivery();
    }
}
```

---

## ✅ Output

```text
Delivering by road using Truck
Delivering by sea using Ship
```

---

# 🔥 **How This Code Works**

---

## Step 1

```java
Logistics logistics = new RoadLogistics();
```

Here, parent reference points to child object.

---

## Step 2

```java
logistics.planDelivery();
```

The method `planDelivery()` is present in abstract class `Logistics`.

---

## Step 3

Inside `planDelivery()`:

```java
Transport transport = createTransport();
```

Since actual object is `RoadLogistics`, Java calls:

```java
RoadLogistics.createTransport()
```

---

## Step 4

```java
return new Truck();
```

So a `Truck` object is created.

---

## Step 5

```java
transport.deliver();
```

Since `transport` contains a `Truck` object, this runs:

```java
Truck.deliver()
```

---

# 🔥 **Most Important Point**

---

```text
Business logic is written once in parent class.

Object creation logic changes in child classes.
```

---

This is the power of Factory Method.

---

# 🔥 **Before Factory Method vs After Factory Method**

---

## ❌ Before

```java
if (type.equals("truck")) {
    return new Truck();
} else if (type.equals("ship")) {
    return new Ship();
}
```

Problem:

```text
Every time new type comes, existing code changes.
```

---

## ✅ After

```java
class AirLogistics extends Logistics {

    @Override
    public Transport createTransport() {
        return new Plane();
    }
}
```

Benefit:

```text
Add new class, do not modify old business logic.
```

---

# 🔥 **When Should You Use Factory Method?**

---

Use Factory Method when:

```text
You do not know beforehand which object needs to be created
```

---

## ✅ Use Case 1: Object Type Depends on Runtime Condition

Example:

```text
If user selects Road → create Truck
If user selects Sea  → create Ship
If user selects Air  → create Plane
```

---

## ✅ Use Case 2: You Want to Avoid Tight Coupling

Instead of depending on:

```java
Truck truck = new Truck();
```

Depend on:

```java
Transport transport = createTransport();
```

---

## ✅ Use Case 3: You Expect New Types in Future

Today:

```text
Truck
Ship
```

Tomorrow:

```text
Plane
Drone
Train
```

Factory Method makes extension easier.

---

## ✅ Use Case 4: Object Creation Logic is Complex

Example:

```text
Validate config
Create object
Set dependencies
Initialize resources
Return object
```

This logic should not be scattered everywhere.

---

## ✅ Use Case 5: Framework or Library Extension

Framework gives default object creation.

You override factory method to provide your own custom object.

Example:

```text
DefaultButton → SquareButton
CustomButton  → RoundButton
```

---

# 🔥 **How to Identify Factory Method Problem**

---

Ask these questions:

---

## 🔹 Question 1

```text
Am I using new keyword again and again for similar objects?
```

Example:

```java
new Truck()
new Ship()
new Plane()
```

---

## 🔹 Question 2

```text
Do these objects share a common behavior?
```

Example:

```java
deliver()
```

If yes, create interface:

```java
interface Transport {
    void deliver();
}
```

---

## 🔹 Question 3

```text
Is object creation mixed with business logic?
```

Bad:

```java
public void processOrder() {
    Truck truck = new Truck();
    truck.deliver();
}
```

Good:

```java
public void processOrder() {
    Transport transport = createTransport();
    transport.deliver();
}
```

---

## 🔹 Question 4

```text
Will new object types be added in future?
```

If yes, Factory Method can help.

---

## 🔹 Question 5

```text
Do I want client code to depend on abstraction instead of concrete classes?
```

If yes, Factory Method is suitable.

---

# 🔥 **How to Reach the Point Where Factory Method is Suitable**

---

## Stage 1: Start With Simple Code

```java
Truck truck = new Truck();
truck.deliver();
```

This is okay when the app is small.

---

## Stage 2: New Requirement Comes

Now business says:

```text
We also need Ship delivery
```

You add:

```java
if (type.equals("truck")) {
    new Truck().deliver();
} else if (type.equals("ship")) {
    new Ship().deliver();
}
```

Still manageable.

---

## Stage 3: More Types Come

Now business says:

```text
Add Plane
Add Train
Add Drone
```

Your code becomes full of:

```text
if-else
switch
new keyword
duplicate logic
```

---

## Stage 4: You Notice the Smell

Code smell:

```text
Too many conditionals deciding which object to create
```

Another smell:

```text
Business logic changes every time a new product is added
```

---

## Stage 5: Extract Common Interface

```java
interface Transport {
    void deliver();
}
```

Now all transport classes implement same interface.

---

## Stage 6: Move Creation Logic to Factory Method

```java
abstract Transport createTransport();
```

---

## Stage 7: Let Subclasses Decide

```java
class RoadLogistics extends Logistics {
    public Transport createTransport() {
        return new Truck();
    }
}
```

Now Factory Method is suitable.

---

# 🔥 **Factory Method in One Line**

---

```text
When object creation varies, but object usage remains same, use Factory Method.
```

---

# 🔥 **Another Rich Example: Notification System**

---

## Problem

You are building a notification system.

Initially:

```text
Email notification only
```

Later:

```text
SMS
WhatsApp
Push Notification
Slack
```

---

## ❌ Without Factory Method

```java
class NotificationService {

    public void notifyUser(String type, String message) {

        if (type.equals("email")) {
            EmailNotification email = new EmailNotification();
            email.send(message);
        } else if (type.equals("sms")) {
            SMSNotification sms = new SMSNotification();
            sms.send(message);
        } else if (type.equals("whatsapp")) {
            WhatsAppNotification whatsapp = new WhatsAppNotification();
            whatsapp.send(message);
        }
    }
}
```

---

## ❌ Problems

```text
Too many if-else blocks
Hard to add new notification type
NotificationService knows all concrete classes
Violates Open/Closed Principle
```

---

## ✅ With Factory Method

---

### Product Interface

```java
interface Notification {
    void send(String message);
}
```

---

### Concrete Products

```java
class EmailNotification implements Notification {

    @Override
    public void send(String message) {
        System.out.println("Sending Email: " + message);
    }
}
```

```java
class SMSNotification implements Notification {

    @Override
    public void send(String message) {
        System.out.println("Sending SMS: " + message);
    }
}
```

```java
class WhatsAppNotification implements Notification {

    @Override
    public void send(String message) {
        System.out.println("Sending WhatsApp: " + message);
    }
}
```

---

### Creator

```java
abstract class NotificationCreator {

    public abstract Notification createNotification();

    public void notifyUser(String message) {
        Notification notification = createNotification();
        notification.send(message);
    }
}
```

---

### Concrete Creators

```java
class EmailNotificationCreator extends NotificationCreator {

    @Override
    public Notification createNotification() {
        return new EmailNotification();
    }
}
```

```java
class SMSNotificationCreator extends NotificationCreator {

    @Override
    public Notification createNotification() {
        return new SMSNotification();
    }
}
```

```java
class WhatsAppNotificationCreator extends NotificationCreator {

    @Override
    public Notification createNotification() {
        return new WhatsAppNotification();
    }
}
```

---

### Client Code

```java
public class NotificationDemo {

    public static void main(String[] args) {

        NotificationCreator creator = new EmailNotificationCreator();
        creator.notifyUser("Order placed successfully");

        creator = new SMSNotificationCreator();
        creator.notifyUser("Payment received");

        creator = new WhatsAppNotificationCreator();
        creator.notifyUser("Your delivery is on the way");
    }
}
```

---

## Output

```text
Sending Email: Order placed successfully
Sending SMS: Payment received
Sending WhatsApp: Your delivery is on the way
```

---

# 🔥 **Factory Method With Spring Boot Style Example**

---

## Scenario

You have multiple payment methods:

```text
UPI
Credit Card
PayPal
Net Banking
```

All payment methods should have same behavior:

```text
pay()
```

---

## Product Interface

```java
public interface Payment {
    void pay(double amount);
}
```

---

## Concrete Products

```java
public class UpiPayment implements Payment {

    @Override
    public void pay(double amount) {
        System.out.println("Paid ₹" + amount + " using UPI");
    }
}
```

```java
public class CreditCardPayment implements Payment {

    @Override
    public void pay(double amount) {
        System.out.println("Paid ₹" + amount + " using Credit Card");
    }
}
```

```java
public class PayPalPayment implements Payment {

    @Override
    public void pay(double amount) {
        System.out.println("Paid ₹" + amount + " using PayPal");
    }
}
```

---

## Creator

```java
public abstract class PaymentProcessor {

    public abstract Payment createPayment();

    public void processPayment(double amount) {
        Payment payment = createPayment();
        payment.pay(amount);
    }
}
```

---

## Concrete Creators

```java
public class UpiPaymentProcessor extends PaymentProcessor {

    @Override
    public Payment createPayment() {
        return new UpiPayment();
    }
}
```

```java
public class CreditCardPaymentProcessor extends PaymentProcessor {

    @Override
    public Payment createPayment() {
        return new CreditCardPayment();
    }
}
```

```java
public class PayPalPaymentProcessor extends PaymentProcessor {

    @Override
    public Payment createPayment() {
        return new PayPalPayment();
    }
}
```

---

## Client

```java
public class PaymentDemo {

    public static void main(String[] args) {

        PaymentProcessor processor = new UpiPaymentProcessor();
        processor.processPayment(500);

        processor = new CreditCardPaymentProcessor();
        processor.processPayment(1200);

        processor = new PayPalPaymentProcessor();
        processor.processPayment(2000);
    }
}
```

---

# 🔥 **Where Factory Method is Used in Real Java**

---

Factory Method style is commonly seen in Java APIs where object creation is hidden behind methods.

Examples:

```java
Calendar.getInstance();
```

```java
ResourceBundle.getBundle("messages");
```

```java
NumberFormat.getInstance();
```

```java
Charset.forName("UTF-8");
```

```java
EnumSet.of(Day.MONDAY, Day.TUESDAY);
```

---

## Why These Are Factory-Like?

Because client code does not directly write:

```java
new SomeConcreteClass();
```

Instead, it asks a method:

```java
getInstance()
getBundle()
forName()
of()
```

And the method decides what object to return.

---

# 🔥 **Factory Method vs Simple Factory**

---

## Simple Factory

```text
One factory class creates objects based on condition
```

Example:

```java
class NotificationFactory {

    public static Notification createNotification(String type) {

        if (type.equals("email")) {
            return new EmailNotification();
        } else if (type.equals("sms")) {
            return new SMSNotification();
        }

        throw new IllegalArgumentException("Invalid type");
    }
}
```

---

## Factory Method

```text
Uses inheritance
Subclasses decide which object to create
```

Example:

```java
class EmailNotificationCreator extends NotificationCreator {

    public Notification createNotification() {
        return new EmailNotification();
    }
}
```

---

## Difference Table

| Point | Simple Factory | Factory Method |
|---|---|---|
| Type | Not official GoF pattern | GoF design pattern |
| Creation Logic | Usually in one class | Distributed across subclasses |
| Uses Inheritance | No | Yes |
| Flexibility | Medium | High |
| Open/Closed Principle | Often violated | Better supported |
| Complexity | Low | Medium |

---

# 🔥 **Factory Method vs Abstract Factory**

---

| Point | Factory Method | Abstract Factory |
|---|---|---|
| Creates | One product type | Family of related products |
| Example | Create Button | Create Button + Checkbox + TextField |
| Main Focus | Subclass decides product | Factory creates related objects |
| Complexity | Medium | Higher |

---

## Simple Understanding

```text
Factory Method = create one type of object
Abstract Factory = create family of related objects
```

---

# 🔥 **Factory Method vs Builder**

---

| Point | Factory Method | Builder |
|---|---|---|
| Purpose | Decide which object to create | Build complex object step-by-step |
| Best For | Product variation | Complex object construction |
| Example | Truck or Ship | User object with many optional fields |
| Focus | Object type | Object construction process |

---

# 🔥 **Advantages**

---

✔ Removes tight coupling  
✔ Follows Open/Closed Principle  
✔ Follows Single Responsibility Principle  
✔ Centralizes object creation logic  
✔ Makes code easier to extend  
✔ Client depends on abstraction  
✔ Useful for frameworks and libraries  

---

# 🔥 **Disadvantages**

---

❌ More classes are created  
❌ Code structure becomes slightly complex  
❌ Can be overkill for simple object creation  
❌ Requires inheritance  
❌ Too many subclasses may become hard to manage  

---

# 🔥 **When NOT to Use Factory Method**

---

Do not use Factory Method when:

```text
Only one object type exists
Object creation is simple
No future variation is expected
You do not need polymorphism
Simple constructor call is enough
```

---

Bad use:

```java
class User {
    private String name;
}
```

No need for:

```java
UserFactory
UserCreator
ConcreteUserCreator
```

Just use:

```java
User user = new User();
```

---

# 🔥 **Common Mistakes**

---

## ❌ Mistake 1: Creating Factory for Everything

Not every class needs a factory.

If object creation is simple, use constructor.

---

## ❌ Mistake 2: Returning Concrete Type

Bad:

```java
public Truck createTransport() {
    return new Truck();
}
```

Better:

```java
public Transport createTransport() {
    return new Truck();
}
```

---

## ❌ Mistake 3: Huge If-Else Inside Factory Method

Bad:

```java
public Transport createTransport(String type) {

    if (type.equals("truck")) {
        return new Truck();
    } else if (type.equals("ship")) {
        return new Ship();
    }

    return null;
}
```

This starts looking like Simple Factory, not Factory Method.

---

## ❌ Mistake 4: Forgetting Common Interface

Factory Method works best when all products follow common contract.

```java
interface Transport {
    void deliver();
}
```

---

# 🔥 **Interview Questions**

---

## 🔹 What is Factory Method Design Pattern?

```text
Factory Method is a creational design pattern that defines an interface for creating objects but allows subclasses to decide which object to instantiate.
```

---

## 🔹 Why do we use Factory Method?

```text
To remove tight coupling between client code and concrete classes, and to make object creation flexible and extensible.
```

---

## 🔹 Which principle does Factory Method follow?

```text
Open/Closed Principle
Single Responsibility Principle
Dependency Inversion Principle
```

---

## 🔹 What is the main benefit?

```text
Client code depends on abstraction, not concrete classes.
```

---

## 🔹 Factory Method vs Constructor?

```text
Constructor always creates a new object of same class.

Factory Method can decide which subclass object to return.
```

---

## 🔹 Factory Method vs Simple Factory?

```text
Simple Factory usually uses one class with conditions.

Factory Method uses inheritance where subclasses override the factory method.
```

---

## 🔹 Factory Method vs Abstract Factory?

```text
Factory Method creates one product.

Abstract Factory creates families of related products.
```

---

## 🔹 Does Factory Method always create new objects?

```text
No.

It can return:
- new object
- cached object
- pooled object
- singleton object
```

---

# 🔥 **Memory Trick**

---

```text
Factory Method = Object creation through overridden method
```

---

Or:

```text
Same business logic, different object creation
```

---

# 🔥 **Final Summary**

---

* Factory Method is a creational design pattern.
* It removes direct dependency on concrete classes.
* It moves object creation logic into a factory method.
* Subclasses decide which object to create.
* Client code works with interface, not implementation.
* Best when object creation varies but usage remains same.

---

```text
Factory Method = Create objects without exposing concrete creation logic
```

---

👉 Use it when your code has multiple related object types and object creation may change or grow in future.

---