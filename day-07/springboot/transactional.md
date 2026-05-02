# ***Transactional in Spring Boot***

---

# 🔥 What is a Transaction

---

## 📌 Definition

A transaction is a group of database operations that should be executed as **one single unit of work**.

Either:

```text
All operations complete successfully
```

or:

```text
All operations are rolled back
```

---

## 🧠 Simple Meaning

```text
Transaction = All or Nothing
```

---

## 🧠 Core Idea

```text
BEGIN TRANSACTION

    Operation 1
    Operation 2
    Operation 3

If all operations succeed:
    COMMIT

If any operation fails:
    ROLLBACK

END TRANSACTION
```

---

# 🔥 Why Do We Need Transactions

---

## 📌 Real Problem

Suppose we are building a **bank money transfer system**.

We need to transfer ₹1000 from Account A to Account B.

The logic is:

```text
1. Debit ₹1000 from Account A
2. Credit ₹1000 to Account B
```

---

## ❌ Problem Without Transaction

```java
public void transferMoney() {

    accountRepository.debit("A", 1000);

    // Suppose application crashes here

    accountRepository.credit("B", 1000);
}
```

---

## 🚨 What Went Wrong

```text
Money deducted from Account A
But money was not credited to Account B
```

This creates **data inconsistency**.

---

## ✅ With Transaction

```java
@Transactional
public void transferMoney() {

    accountRepository.debit("A", 1000);

    accountRepository.credit("B", 1000);
}
```

Now Spring will make sure:

```text
If debit succeeds and credit succeeds → COMMIT

If debit succeeds but credit fails → ROLLBACK debit also
```

---

# 🔥 Critical Section

---

## 📌 Definition

A critical section is a part of code where **shared data is being accessed or modified**.

---

## Example

```text
Read car row with id = 1001

If status = Available

Update status = Booked
```

---

## ❌ Problem

If two users try to book the same car at the same time:

```text
User 1 reads status = Available
User 2 also reads status = Available

User 1 books the car
User 2 also books the same car
```

---

## 🚨 Result

```text
Same car gets booked twice
```

This is a concurrency problem.

---

## ✅ Transaction Helps

A transaction helps to control how data is read and modified when multiple requests are running together.

---

# 🔥 ACID Properties

---

Transactions are reliable because they follow **ACID** properties.

---

# 🔥 Atomicity

---

## 📌 Definition

Atomicity means all operations inside a transaction should be completed successfully.

If one operation fails, the entire transaction should rollback.

---

## 🧠 Simple Meaning

```text
Everything succeeds or everything fails
```

---

## Example

```java
@Transactional
public void transferMoney() {

    debitAccount();

    creditAccount();
}
```

If `creditAccount()` fails:

```text
debitAccount() will also rollback
```

---

# 🔥 Consistency

---

## 📌 Definition

Consistency means the database should remain in a valid state before and after the transaction.

---

## Example

Before transaction:

```text
Account A = ₹5000
Account B = ₹3000
Total = ₹8000
```

Transfer ₹1000 from A to B.

After transaction:

```text
Account A = ₹4000
Account B = ₹4000
Total = ₹8000
```

The total amount remains valid.

---

## ❌ Inconsistent State

```text
Account A = ₹4000
Account B = ₹3000
Total = ₹7000
```

₹1000 disappeared.

Transaction prevents this.

---

# 🔥 Isolation

---

## 📌 Definition

Isolation means multiple transactions running at the same time should not wrongly interfere with each other.

---

## Example

```text
Transaction A is booking a car
Transaction B is also booking the same car
```

Isolation decides:

```text
Can Transaction B see Transaction A's uncommitted changes?
Can both update the same row?
Should one wait?
```

Isolation levels control this behavior.

---

# 🔥 Durability

---

## 📌 Definition

Durability means once a transaction is committed, the data should not be lost even if system crashes.

---

## Example

```text
Payment transaction committed
Server crashes after commit
Server restarts
Payment data should still exist
```

---

# 🔥 What is @Transactional

---

## 📌 Definition

`@Transactional` is a Spring annotation used to tell Spring:

```text
Run this method inside a transaction
```

---

## Simple Example

```java
@Service
public class PaymentService {

    @Transactional
    public void makePayment() {

        debitAccount();

        creditMerchant();
    }
}
```

---

## What Spring Handles Automatically

```text
Start transaction
Execute method
Commit if success
Rollback if failure
Close transaction resources
```

---

# 🔥 Required Dependency

---

If we are using Spring Boot with relational database and JPA, we usually add:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

For database driver:

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>
```

or for H2:

```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
</dependency>
```

---

# 🔥 Enable Transaction Management

---

In Spring Boot, transaction management is usually auto-configured.

But conceptually, it can be enabled using:

```java
@SpringBootApplication
@EnableTransactionManagement
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

---

## 🧠 Meaning

```text
@EnableTransactionManagement tells Spring to look for @Transactional methods
and apply transaction logic using proxy-based AOP.
```

In Spring Boot, you usually do not need to add it manually because auto-configuration handles it.

---

# 🔥 Where Can We Use @Transactional

---

# 🔥 Method Level

---

## Example

```java
@Service
public class UserService {

    @Transactional
    public void updateUser() {
        // DB operations
    }

    public void fetchUser() {
        // No transaction here
    }
}
```

---

## Meaning

```text
Only updateUser method runs inside transaction
```

---

# 🔥 Class Level

---

## Example

```java
@Service
@Transactional
public class UserService {

    public void updateUser() {
        // transactional
    }

    public void deleteUser() {
        // transactional
    }

    public void fetchUser() {
        // transactional
    }
}
```

---

## Meaning

```text
All public methods inside this class become transactional
```

---

## Important Point

If method-level and class-level both exist:

```text
Method-level @Transactional gets priority
```

---

## Example

```java
@Service
@Transactional(readOnly = true)
public class UserService {

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Transactional(readOnly = false)
    public void updateUser(User user) {
        userRepository.save(user);
    }
}
```

Here:

```text
Class says read-only by default
updateUser overrides it because it needs write operation
```

---

# 🔥 How @Transactional Works Internally

---

## 📌 Most Important Concept

`@Transactional` works using **AOP proxy**.

Spring does not directly modify your method.

Instead, Spring creates a proxy around your bean.

---

## Flow

```text
Controller
    ↓
Transactional Proxy
    ↓
Transaction Interceptor
    ↓
Actual Service Method
```

---

## Step-by-Step Internal Flow

```text
Application starts
        ↓
Spring scans beans
        ↓
Finds @Transactional methods/classes
        ↓
Creates proxy object for that bean
        ↓
Controller receives proxy instead of real object
        ↓
When method is called, proxy intercepts the call
        ↓
TransactionInterceptor starts transaction
        ↓
Actual method executes
        ↓
If success → commit
        ↓
If exception → rollback
```

---

## Mental Model

Suppose you write:

```java
userService.updateUser();
```

You think this happens:

```text
Controller → UserService
```

But actually:

```text
Controller → ProxyUserService → TransactionInterceptor → Real UserService
```

---

## Internal Proxy Example

```java
class UserServiceProxy {

    private UserService realUserService;
    private PlatformTransactionManager transactionManager;

    public void updateUser() {

        TransactionStatus status =
                transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            realUserService.updateUser();

            transactionManager.commit(status);
        } catch (Exception ex) {
            transactionManager.rollback(status);
            throw ex;
        }
    }
}
```

---

## Why Proxy is Needed

Java cannot automatically insert transaction code inside your method at runtime.

Spring cannot rewrite this:

```java
public void updateUser() {
    // your code
}
```

into this:

```java
public void updateUser() {
    beginTransaction();
    // your code
    commit();
}
```

So Spring wraps your object with a proxy.

---

# 🔥 TransactionInterceptor

---

## 📌 Definition

`TransactionInterceptor` is the class that contains the around advice logic for transactions.

---

## What It Does

```text
Before method → begin transaction
Method success → commit transaction
Method failure → rollback transaction
```

---

## Conceptual Flow

```text
invokeWithinTransaction()
        ↓
Create or join transaction
        ↓
Call actual method
        ↓
Commit or rollback
```

---

# 🔥 Why Transaction Management Uses AOP

---

Because transaction is a cross-cutting concern.

Same transaction logic is needed in many service methods:

```text
Start transaction
Commit
Rollback
Close connection
```

Instead of writing this everywhere, Spring applies it using AOP.

---

# 🔥 Transaction Manager

---

## 📌 Definition

Transaction Manager is responsible for actually managing transaction operations.

It handles:

```text
getTransaction
commit
rollback
```

---

## Main Interface

```java
public interface PlatformTransactionManager {

    TransactionStatus getTransaction(TransactionDefinition definition);

    void commit(TransactionStatus status);

    void rollback(TransactionStatus status);
}
```

---

## Meaning of Methods

### getTransaction

```text
Starts a new transaction or joins an existing transaction
depending on propagation behavior.
```

### commit

```text
Commits the transaction if everything is successful.
```

### rollback

```text
Rolls back the transaction if failure occurs.
```

---

# 🔥 Transaction Manager Hierarchy

---

## Common Transaction Managers

```text
DataSourceTransactionManager
JpaTransactionManager
HibernateTransactionManager
JtaTransactionManager
```

---

# 🔥 DataSourceTransactionManager

---

## Used For

```text
Plain JDBC-based transactions
```

---

## Example

```java
@Bean
public PlatformTransactionManager transactionManager(DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
}
```

---

## When Used

```text
When we directly use JDBC or JdbcTemplate
```

---

# 🔥 JpaTransactionManager

---

## Used For

```text
JPA / Hibernate through EntityManager
```

---

## Example

```java
@Bean
public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
    return new JpaTransactionManager(emf);
}
```

---

## When Used

```text
When using Spring Data JPA repositories
```

---

# 🔥 HibernateTransactionManager

---

## Used For

```text
Native Hibernate SessionFactory transactions
```

---

## When Used

```text
Older Hibernate-specific applications
```

---

# 🔥 JtaTransactionManager

---

## Used For

```text
Distributed transactions
```

---

## Example Scenario

```text
One transaction updates:
1. Database A
2. Database B
3. Message Queue
```

All should commit or rollback together.

---

# 🔥 Local Transaction vs Distributed Transaction

---

## Local Transaction

```text
Transaction with one database/resource
```

Example:

```text
Only MySQL database update
```

---

## Distributed Transaction

```text
Transaction across multiple resources
```

Example:

```text
Update Oracle DB + publish JMS message + update MySQL DB
```

---

# 🔥 Declarative Transaction Management

---

## 📌 Definition

Declarative transaction management means using annotation/configuration to manage transactions.

---

## Example

```java
@Service
public class UserService {

    @Transactional
    public void updateUser() {

        userRepository.save(user);

        auditRepository.save(audit);
    }
}
```

---

## Meaning

```text
Developer declares transaction boundary
Spring handles actual transaction internally
```

---

## Advantages

```text
Clean code
Less boilerplate
Recommended for most cases
Easy to maintain
```

---

# 🔥 Programmatic Transaction Management

---

## 📌 Definition

Programmatic transaction management means manually writing transaction control logic in code.

---

## When to Use

Use when:

```text
You need very fine-grained transaction control
You want only part of a method inside transaction
You want transaction boundary based on complex runtime conditions
```

---

# 🔥 Programmatic Approach Using PlatformTransactionManager

---

## Example

```java
@Service
public class UserService {

    private final PlatformTransactionManager transactionManager;
    private final UserRepository userRepository;

    public UserService(
            PlatformTransactionManager transactionManager,
            UserRepository userRepository
    ) {
        this.transactionManager = transactionManager;
        this.userRepository = userRepository;
    }

    public void updateUserProgrammatically(User user) {

        DefaultTransactionDefinition definition =
                new DefaultTransactionDefinition();

        definition.setName("USER_UPDATE_TRANSACTION");
        definition.setPropagationBehavior(
                TransactionDefinition.PROPAGATION_REQUIRED
        );

        TransactionStatus status =
                transactionManager.getTransaction(definition);

        try {
            userRepository.save(user);

            transactionManager.commit(status);
        } catch (Exception ex) {
            transactionManager.rollback(status);
            throw ex;
        }
    }
}
```

---

## How This Works

```text
1. Create transaction definition
2. Ask transaction manager for transaction
3. Execute DB logic
4. Commit manually
5. Rollback manually if exception occurs
```

---

## Problem

```text
Too much boilerplate code
Easy to forget commit/rollback
Harder to read
```

---

# 🔥 Programmatic Approach Using TransactionTemplate

---

## 📌 Definition

`TransactionTemplate` is a cleaner helper class for programmatic transactions.

---

## Example

```java
@Service
public class UserService {

    private final TransactionTemplate transactionTemplate;
    private final UserRepository userRepository;

    public UserService(
            PlatformTransactionManager transactionManager,
            UserRepository userRepository
    ) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.userRepository = userRepository;
    }

    public void updateUserUsingTemplate(User user) {

        transactionTemplate.execute(status -> {

            userRepository.save(user);

            return null;
        });
    }
}
```

---

## With Rollback

```java
public void updateUserUsingTemplate(User user) {

    transactionTemplate.execute(status -> {

        try {
            userRepository.save(user);
            return null;
        } catch (Exception ex) {
            status.setRollbackOnly();
            throw ex;
        }
    });
}
```

---

## Why TransactionTemplate is Better Than Manual Approach

```text
Less boilerplate
Transaction begin/commit handled internally
Rollback can be marked using status.setRollbackOnly()
Cleaner than directly using PlatformTransactionManager
```

---

# 🔥 Declarative vs Programmatic

---

| Point | Declarative | Programmatic |
|---|---|---|
| How | `@Transactional` | Java code |
| Boilerplate | Less | More |
| Flexibility | Medium | High |
| Recommended | Yes | Only special cases |
| Readability | High | Lower |

---

# 🔥 Propagation

---

## 📌 Definition

Propagation defines how a transactional method behaves when it is called from another transactional method.

---

## 🧠 Simple Meaning

```text
If transaction already exists, should I join it, create new one, or reject it?
```

---

## Example Setup

```java
@Service
public class OrderService {

    private final PaymentService paymentService;

    @Transactional
    public void placeOrder() {
        saveOrder();
        paymentService.makePayment();
    }
}
```

```java
@Service
public class PaymentService {

    @Transactional(propagation = Propagation.REQUIRED)
    public void makePayment() {
        savePayment();
    }
}
```

The question is:

```text
Should makePayment use the transaction started by placeOrder?
Or create a new transaction?
Or run without transaction?
```

Propagation decides this.

---

# 🔥 REQUIRED

---

## 📌 Definition

If a transaction exists, use it. If not, create a new transaction.

---

## Example

```java
@Transactional(propagation = Propagation.REQUIRED)
public void makePayment() {
    savePayment();
}
```

---

## Flow When Parent Transaction Exists

```text
placeOrder transaction starts
        ↓
makePayment joins same transaction
        ↓
If any method fails
        ↓
Entire transaction rolls back
```

---

## Flow When Parent Transaction Does Not Exist

```text
makePayment called directly
        ↓
New transaction starts
```

---

## Real Use Case

Use for normal service methods.

```text
Most common and default propagation
```

---

# 🔥 REQUIRES_NEW

---

## 📌 Definition

Always creates a new transaction.

If a transaction already exists, it suspends the old transaction.

---

## Example

```java
@Transactional(propagation = Propagation.REQUIRES_NEW)
public void saveAuditLog() {
    auditRepository.save(new AuditLog());
}
```

---

## Flow

```text
Main transaction starts
        ↓
saveAuditLog called
        ↓
Main transaction suspended
        ↓
New audit transaction starts
        ↓
Audit commits independently
        ↓
Main transaction resumes
```

---

## Real Use Case

Audit logs.

Even if main transaction fails, audit log should still be saved.

---

## Example

```java
@Transactional
public void placeOrder() {

    orderRepository.save(order);

    auditService.saveAuditLog();

    throw new RuntimeException("Order failed");
}
```

If `saveAuditLog()` uses `REQUIRES_NEW`:

```text
Order rollback
Audit commit
```

---

# 🔥 SUPPORTS

---

## 📌 Definition

If transaction exists, join it. If not, run without transaction.

---

## Example

```java
@Transactional(propagation = Propagation.SUPPORTS)
public User getUser(Long id) {
    return userRepository.findById(id).orElseThrow();
}
```

---

## Flow

```text
Called from transactional method → joins transaction
Called directly → no transaction
```

---

## Real Use Case

Read operations that can work with or without transaction.

---

# 🔥 NOT_SUPPORTED

---

## 📌 Definition

Always runs without transaction.

If transaction exists, suspend it.

---

## Example

```java
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public void generateReport() {
    // long read/report operation
}
```

---

## Flow

```text
Existing transaction present
        ↓
Suspend transaction
        ↓
Run method without transaction
        ↓
Resume transaction
```

---

## Real Use Case

Long-running report generation where transaction is not required.

---

# 🔥 MANDATORY

---

## 📌 Definition

Must run inside an existing transaction.

If no transaction exists, throw exception.

---

## Example

```java
@Transactional(propagation = Propagation.MANDATORY)
public void updateInventory() {
    inventoryRepository.updateStock();
}
```

---

## Flow

```text
Called inside transaction → works
Called without transaction → exception
```

---

## Real Use Case

A method that should never be called independently.

---

# 🔥 NEVER

---

## 📌 Definition

Must run without transaction.

If transaction exists, throw exception.

---

## Example

```java
@Transactional(propagation = Propagation.NEVER)
public void callExternalApi() {
    // external API call
}
```

---

## Flow

```text
Called without transaction → works
Called inside transaction → exception
```

---

## Real Use Case

Prevent slow external calls from happening inside database transaction.

---

# 🔥 NESTED

---

## 📌 Definition

Runs inside a nested transaction using savepoint.

If nested transaction fails, rollback to savepoint, not necessarily entire outer transaction.

---

## Example

```java
@Transactional
public void placeOrder() {

    orderRepository.save(order);

    try {
        paymentService.processCoupon();
    } catch (Exception ex) {
        // coupon rollback only
    }

    orderRepository.save(order);
}
```

```java
@Transactional(propagation = Propagation.NESTED)
public void processCoupon() {
    couponRepository.applyCoupon();
    throw new RuntimeException("Coupon failed");
}
```

---

## Flow

```text
Outer transaction starts
        ↓
Savepoint created for nested transaction
        ↓
Nested method fails
        ↓
Rollback to savepoint
        ↓
Outer transaction can continue
```

---

## Important

Nested transaction support depends on transaction manager and database support.

---

# 🔥 Propagation Summary

---

| Propagation | Existing Transaction | No Existing Transaction |
|---|---|---|
| REQUIRED | Join existing | Create new |
| REQUIRES_NEW | Suspend existing and create new | Create new |
| SUPPORTS | Join existing | Run without transaction |
| NOT_SUPPORTED | Suspend existing | Run without transaction |
| MANDATORY | Join existing | Throw exception |
| NEVER | Throw exception | Run without transaction |
| NESTED | Create savepoint | Create new |

---

# 🔥 Isolation Level

---

## 📌 Definition

Isolation level controls how changes made by one transaction are visible to other transactions running in parallel.

---

## 🧠 Simple Meaning

```text
Isolation = How much one transaction is protected from another transaction
```

---

## Why Isolation Is Needed

When multiple transactions run together, these problems can happen:

```text
Dirty Read
Non-Repeatable Read
Phantom Read
```

---

# 🔥 Dirty Read

---

## 📌 Definition

Dirty read happens when one transaction reads uncommitted data of another transaction.

---

## Example

```text
T1 starts
T2 starts

T2 updates car status from FREE to BOOKED
T2 has not committed yet

T1 reads status = BOOKED

T2 rolls back

Actual status becomes FREE again
```

---

## Problem

T1 read data that never actually became permanent.

---

## Real Example

```text
User sees seat booked
But booking transaction later fails
Seat was never actually booked
```

---

# 🔥 Non-Repeatable Read

---

## 📌 Definition

Non-repeatable read happens when the same row is read multiple times inside one transaction and returns different values.

---

## Example

```text
T1 starts

T1 reads account balance = 5000

T2 updates balance = 3000
T2 commits

T1 reads same account again
Now balance = 3000
```

---

## Problem

Same query, same row, same transaction, but different result.

---

# 🔥 Phantom Read

---

## 📌 Definition

Phantom read happens when the same query is executed multiple times inside one transaction and returns different rows.

---

## Example

```text
T1 starts

T1 runs:
select * from orders where amount > 1000

Result:
2 rows

T2 inserts a new order with amount 2000
T2 commits

T1 runs same query again

Result:
3 rows
```

---

## Problem

New row appears like a phantom.

---

# 🔥 Isolation Levels

---

# 🔥 READ_UNCOMMITTED

---

## 📌 Meaning

Allows reading uncommitted data.

---

## Problems Possible

```text
Dirty Read
Non-Repeatable Read
Phantom Read
```

---

## Use Case

Rarely used.

High performance but unsafe.

---

# 🔥 READ_COMMITTED

---

## 📌 Meaning

A transaction can read only committed data.

---

## Prevents

```text
Dirty Read
```

---

## Still Allows

```text
Non-Repeatable Read
Phantom Read
```

---

## Example

If T2 updates a row but has not committed:

```text
T1 cannot read that uncommitted update
```

---

## Use Case

Common default in many relational databases.

---

# 🔥 REPEATABLE_READ

---

## 📌 Meaning

If a transaction reads a row once, repeated reads of that same row return the same value.

---

## Prevents

```text
Dirty Read
Non-Repeatable Read
```

---

## Still Allows

```text
Phantom Read
```

---

## Example

```text
T1 reads account balance = 5000
T2 updates same account and commits
T1 reads again
T1 still sees 5000
```

---

# 🔥 SERIALIZABLE

---

## 📌 Meaning

Highest isolation level.

Transactions behave as if they are executed one after another.

---

## Prevents

```text
Dirty Read
Non-Repeatable Read
Phantom Read
```

---

## Tradeoff

```text
Highest consistency
Lowest concurrency
Slower performance
More locking
```

---

# 🔥 Isolation Summary

---

| Isolation Level | Dirty Read | Non-Repeatable Read | Phantom Read | Concurrency |
|---|---|---|---|---|
| READ_UNCOMMITTED | Possible | Possible | Possible | Highest |
| READ_COMMITTED | Prevented | Possible | Possible | High |
| REPEATABLE_READ | Prevented | Prevented | Possible | Medium |
| SERIALIZABLE | Prevented | Prevented | Prevented | Lowest |

---

# 🔥 Using Isolation in Spring

---

```java
@Transactional(isolation = Isolation.READ_COMMITTED)
public void updateUser() {
    // DB operations
}
```

---

```java
@Transactional(isolation = Isolation.SERIALIZABLE)
public void bookSeat() {
    // strict consistency
}
```

---

# 🔥 Locking

---

## 📌 Definition

Locking prevents multiple transactions from modifying or reading data in unsafe ways.

---

# 🔥 Shared Lock

---

## Meaning

```text
Read lock
Multiple transactions can read
But write is blocked
```

---

## Example

```text
Transaction A reads row with shared lock
Transaction B can also read
Transaction B cannot update the row
```

---

# 🔥 Exclusive Lock

---

## Meaning

```text
Write lock
Blocks other reads/writes depending on isolation and DB rules
```

---

## Example

```text
Transaction A updates row with exclusive lock
Transaction B cannot update same row
```

---

# 🔥 Lock Compatibility

---

| Existing Lock | Another Shared Lock | Another Exclusive Lock |
|---|---|---|
| Shared Lock | Allowed | Not Allowed |
| Exclusive Lock | Not Allowed | Not Allowed |

---

# 🔥 Timeout

---

## 📌 Definition

Timeout defines maximum time a transaction is allowed to run.

---

## Example

```java
@Transactional(timeout = 5)
public void processOrder() {
    // must finish within 5 seconds
}
```

---

## Meaning

```text
If transaction takes more than 5 seconds, Spring marks it for rollback
```

---

## Use Case

```text
Avoid long-running transactions
Prevent connection holding
Protect database resources
```

---

# 🔥 Read Only Transaction

---

## 📌 Definition

Read-only transaction tells Spring and database that the method is only reading data.

---

## Example

```java
@Transactional(readOnly = true)
public List<User> getUsers() {
    return userRepository.findAll();
}
```

---

## Why Use It

```text
Performance optimization
Avoid unnecessary dirty checking in JPA
Clear intention that method should not modify data
```

---

## Important

Read-only does not always guarantee that writes are impossible.

Behavior depends on database and JPA provider.

But it is still a best practice for query methods.

---

# 🔥 Rollback Rules

---

## Default Rollback Behavior

Spring rolls back automatically for:

```text
RuntimeException
Error
```

Spring does not rollback by default for:

```text
Checked exceptions
```

---

## Runtime Exception Example

```java
@Transactional
public void updateUser() {

    userRepository.save(user);

    throw new RuntimeException("Something failed");
}
```

Result:

```text
Transaction rollback
```

---

## Checked Exception Example

```java
@Transactional
public void updateUser() throws IOException {

    userRepository.save(user);

    throw new IOException("File failed");
}
```

Result by default:

```text
Transaction may commit
```

---

## Force Rollback for Checked Exception

```java
@Transactional(rollbackFor = IOException.class)
public void updateUser() throws IOException {

    userRepository.save(user);

    throw new IOException("File failed");
}
```

---

## Avoid Rollback for Specific Exception

```java
@Transactional(noRollbackFor = IllegalArgumentException.class)
public void updateUser() {

    userRepository.save(user);

    throw new IllegalArgumentException("Validation warning");
}
```

---

# 🔥 Self Invocation Problem

---

## 📌 Problem

Transactional does not work when a method inside the same class calls another transactional method.

---

## Example

```java
@Service
public class UserService {

    public void outerMethod() {
        innerMethod();
    }

    @Transactional
    public void innerMethod() {
        // DB operation
    }
}
```

---

## Why It Fails

```text
outerMethod calls innerMethod using this.innerMethod()
Proxy is bypassed
Transaction does not start
```

---

## Correct Approach

Move transactional method to another Spring bean.

```java
@Service
public class UserService {

    private final UserTransactionService userTransactionService;

    public void outerMethod() {
        userTransactionService.innerMethod();
    }
}
```

```java
@Service
public class UserTransactionService {

    @Transactional
    public void innerMethod() {
        // DB operation
    }
}
```

Now call goes through proxy.

---

# 🔥 Public Method Rule

---

## Important

Spring proxy-based `@Transactional` works reliably on public methods.

---

## Example

```java
@Transactional
private void updateUser() {
    // transaction will not apply properly
}
```

---

## Why

```text
Spring AOP proxy intercepts external public method calls
Private methods are not called through proxy
```

---

# 🔥 Exception Swallowing Problem

---

## Problem

```java
@Transactional
public void updateUser() {

    try {
        userRepository.save(user);
        throw new RuntimeException("Failed");
    } catch (Exception ex) {
        System.out.println("Handled exception");
    }
}
```

---

## What Happens

```text
Exception is caught
Method ends normally
Spring thinks everything is successful
Transaction commits
```

---

## Correct Way

Either rethrow exception:

```java
@Transactional
public void updateUser() {

    try {
        userRepository.save(user);
        throw new RuntimeException("Failed");
    } catch (Exception ex) {
        throw ex;
    }
}
```

Or mark rollback manually:

```java
@Transactional
public void updateUser() {

    try {
        userRepository.save(user);
        throw new RuntimeException("Failed");
    } catch (Exception ex) {
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
}
```

---

# 🔥 External API Calls Inside Transaction

---

## Problem

```java
@Transactional
public void placeOrder() {

    orderRepository.save(order);

    paymentGateway.call();

    inventoryRepository.updateStock();
}
```

---

## Why Risky

```text
Transaction remains open while external API call is running
Database connection is held for longer
Locks may be held longer
System becomes slower under load
```

---

## Better Approach

```text
Keep DB transaction short
Call external systems outside transaction when possible
Use events/outbox pattern for reliable async communication
```

---

# 🔥 Practical Example

---

## Entity

```java
@Entity
public class Account {

    @Id
    private Long id;

    private String accountNumber;

    private BigDecimal balance;
}
```

---

## Repository

```java
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
}
```

---

## Service

```java
@Service
public class TransferService {

    private final AccountRepository accountRepository;

    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void transfer(String from, String to, BigDecimal amount) {

        Account sender = accountRepository.findByAccountNumber(from)
                .orElseThrow();

        Account receiver = accountRepository.findByAccountNumber(to)
                .orElseThrow();

        sender.setBalance(sender.getBalance().subtract(amount));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        receiver.setBalance(receiver.getBalance().add(amount));
    }
}
```

---

## What Happens

```text
Transaction starts
Sender balance updated
If exception occurs → rollback
Receiver balance updated
Method success → commit
```

---

# 🔥 Interview Questions

---

## What is a transaction

```text
A transaction is a group of operations executed as a single unit.
```

---

## What is @Transactional

```text
It tells Spring to run a method inside a transaction.
```

---

## How does @Transactional work internally

```text
Spring creates an AOP proxy. The proxy starts transaction before method execution and commits or rolls back after execution.
```

---

## What is Transaction Manager

```text
It is responsible for starting, committing, and rolling back transactions.
```

---

## What is default propagation

```text
REQUIRED.
```

---

## Difference between REQUIRED and REQUIRES_NEW

```text
REQUIRED joins existing transaction.
REQUIRES_NEW suspends existing transaction and creates a new one.
```

---

## What is isolation level

```text
It controls visibility of changes between concurrent transactions.
```

---

## What is dirty read

```text
Reading uncommitted data from another transaction.
```

---

## What is non-repeatable read

```text
Reading same row twice and getting different values.
```

---

## What is phantom read

```text
Running same query twice and getting different number of rows.
```

---

## Why @Transactional does not work with self invocation

```text
Because internal method call bypasses Spring proxy.
```

---

# 🔥 Common Mistakes

---

```text
Using @Transactional on private methods
Calling transactional method from same class
Catching exception and not rethrowing
Using long external API calls inside transaction
Using wrong propagation
Using SERIALIZABLE everywhere
Assuming readOnly always prevents writes
Forgetting rollbackFor for checked exceptions
```

---

# 🔥 Memory Trick

---

```text
@Transactional = Proxy + TransactionManager + Commit/Rollback
```

---

```text
Propagation = What to do if transaction already exists
```

---

```text
Isolation = What one transaction can see from another transaction
```

---

# 🚀 Final Summary

---

* Transaction means all-or-nothing execution.
* Transactions follow ACID properties.
* `@Transactional` uses Spring AOP proxy internally.
* TransactionInterceptor starts, commits, or rolls back transaction.
* PlatformTransactionManager performs actual transaction operations.
* Declarative transaction uses annotation.
* Programmatic transaction gives manual control.
* Propagation controls transaction joining/creation behavior.
* Isolation controls visibility between concurrent transactions.
* Timeout limits transaction duration.
* readOnly optimizes read operations.
* Self-invocation bypasses proxy.
* Runtime exceptions rollback by default.
* Checked exceptions need rollbackFor.

---

```text
Transactional = Data Consistency + AOP Proxy + Transaction Manager
```

---
