# ***Async Annotation in Spring Boot***

---

# 🔥 What is Async Processing

---

## 📌 Definition

Async processing means executing a task in a separate thread without blocking the main request thread.

---

## 🧠 Core Idea

```text
Main thread receives request
        ↓
Main thread delegates long-running task
        ↓
Another thread executes that task
        ↓
Main thread continues without waiting
```

---

## 🧠 Simple Meaning

```text
Async = Do work in background
```

---

# 🔥 Why Do We Need Async

---

## 📌 Problem

Some operations take time:

```text
Sending email
Sending notification
Generating report
Uploading file to cloud
Processing large file
Calling slow external API
```

---

## ❌ Without Async

```java
public String registerUser() {

    saveUser();

    sendWelcomeEmail();

    return "User registered";
}
```

---

## 🚨 Problem

```text
User has to wait until email sending is complete
```

If email takes 5 seconds:

```text
API response also takes 5 seconds
```

---

## ✅ With Async

```java
public String registerUser() {

    saveUser();

    emailService.sendWelcomeEmailAsync();

    return "User registered";
}
```

Now:

```text
Main request returns quickly
Email runs in background
```

---

# 🔥 What is Thread Pool

---

## 📌 Definition

A thread pool is a collection of reusable worker threads that are available to execute submitted tasks.

---

## 🧠 Simple Meaning

```text
Thread Pool = Group of reusable threads
```

---

## Flow

```text
Application submits tasks
        ↓
Tasks wait in queue
        ↓
Available thread picks task
        ↓
Task executes
        ↓
Thread returns to pool
```

---

## Why Thread Pool Is Needed

Creating a new thread every time is expensive.

Thread creation needs:

```text
Memory
CPU scheduling
Lifecycle management
Context switching
```

So instead of creating new threads again and again:

```text
Create limited threads once
Reuse them for multiple tasks
```

---

# 🔥 ThreadPoolExecutor in Java

---

## Example

```java
int minPoolSize = 2;
int maxPoolSize = 4;
int queueSize = 3;

ThreadPoolExecutor executor = new ThreadPoolExecutor(
        minPoolSize,
        maxPoolSize,
        1,
        TimeUnit.HOURS,
        new ArrayBlockingQueue<>(queueSize)
);
```

---

## Meaning

```text
minPoolSize = minimum number of threads
maxPoolSize = maximum number of threads
queueSize = number of tasks that can wait
```

---

# 🔥 How Thread Pool Works

---

## Example

```text
Core pool size = 2
Max pool size = 4
Queue size = 3
```

---

## Flow

```text
Task 1 → Thread 1
Task 2 → Thread 2

Task 3 → Queue
Task 4 → Queue
Task 5 → Queue

Task 6 → New Thread 3
Task 7 → New Thread 4

Task 8 → Rejected
```

---

## Important Rule

```text
First core threads are used
Then queue is filled
Then extra threads are created up to max pool size
After max threads and full queue, task is rejected
```

---

# 🔥 What is Async Annotation

---

## 📌 Definition

`@Async` is a Spring annotation used to run a method asynchronously in a separate thread.

---

## 🧠 Core Idea

```text
Do not block the caller thread
Run method in background thread
```

---

## 🧠 Simple Meaning

```text
@Async = Run this method in another thread
```

---

# 🔥 Basic Async Example

---

## Main Class

```java
@SpringBootApplication
@EnableAsync
public class AsyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsyncApplication.class, args);
    }
}
```

---

## Service

```java
@Service
public class UserService {

    @Async
    public void asyncMethodTest() {
        System.out.println("Inside asyncMethodTest: "
                + Thread.currentThread().getName());
    }
}
```

---

## Controller

```java
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String getUser() {

        System.out.println("Inside getUserMethod: "
                + Thread.currentThread().getName());

        userService.asyncMethodTest();

        return "User fetched";
    }
}
```

---

## Output

```text
Inside getUserMethod: http-nio-8080-exec-1
Inside asyncMethodTest: task-1
```

---

## 🧠 Meaning

```text
Controller runs on request thread
Async method runs on different thread
```

---

# 🔥 Why EnableAsync Is Required

---

## 📌 Definition

`@EnableAsync` tells Spring to enable async method execution.

---

## Without EnableAsync

```text
@Async annotation will not work
Method will run normally in same thread
```

---

## With EnableAsync

```text
Spring creates proxy
Method call is intercepted
Task is submitted to executor
Method runs in another thread
```

---

# 🔥 How Async Works Internally

---

## Important

`@Async` works using Spring AOP proxy.

---

## Internal Flow

```text
Application starts
        ↓
Spring scans @Async methods
        ↓
Spring creates proxy for async bean
        ↓
Controller calls async method
        ↓
Proxy intercepts method call
        ↓
Proxy submits task to Executor
        ↓
Executor runs task on separate thread
```

---

## Runtime Flow

```text
Controller Thread
        ↓
Calls async method
        ↓
Async Proxy
        ↓
Executor
        ↓
Worker Thread executes method
```

---

## Mental Model

You write:

```java
userService.asyncMethodTest();
```

But internally:

```text
Controller → Proxy → Executor → Worker Thread → Actual Method
```

---

# 🔥 What Executor Does Async Use

---

## Common Misunderstanding

Many people say:

```text
Spring Boot always uses SimpleAsyncTaskExecutor by default
```

This is not fully correct.

---

## Correct Understanding

Spring first tries to find a default executor bean.

If an executor bean is available:

```text
Spring uses that executor
```

If no executor is found:

```text
Spring falls back to SimpleAsyncTaskExecutor
```

---

## Internal Idea

```text
Find default executor
        ↓
If found → use it
        ↓
If not found → use SimpleAsyncTaskExecutor
```

---

# 🔥 SimpleAsyncTaskExecutor

---

## 📌 Definition

`SimpleAsyncTaskExecutor` is an executor that creates a new thread for each task.

---

## 🧠 Simple Meaning

```text
Every async call = new thread
```

---

## Output Example

```text
Inside asyncMethodTest: SimpleAsyncTaskExecutor-1
Inside asyncMethodTest: SimpleAsyncTaskExecutor-2
Inside asyncMethodTest: SimpleAsyncTaskExecutor-3
```

---

## Why It Is Not Recommended

---

## Thread Exhaustion

```text
Every async request creates a new thread
Too many requests can create too many threads
Server may crash
```

---

## Thread Creation Overhead

```text
Threads are not reused
Creating and destroying threads repeatedly is expensive
```

---

## High Memory Usage

```text
Each thread needs memory
Too many threads increase memory pressure
```

---

# 🔥 ThreadPoolTaskExecutor

---

## 📌 Definition

`ThreadPoolTaskExecutor` is Spring's wrapper around Java thread pool executor.

---

## 🧠 Simple Meaning

```text
Spring-friendly thread pool
```

---

## Why It Is Better

```text
Threads are reused
Pool size can be controlled
Queue size can be controlled
Thread names can be customized
Rejection behavior can be handled
```

---

# 🔥 Custom ThreadPoolTaskExecutor

---

## Configuration

```java
@Configuration
public class AsyncConfig {

    @Bean(name = "myThreadPoolExecutor")
    public Executor taskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(3);
        executor.setThreadNamePrefix("MyThread-");

        executor.initialize();

        return executor;
    }
}
```

---

## Usage

```java
@Async("myThreadPoolExecutor")
public void asyncMethodTest() {
    System.out.println("Inside asyncMethodTest: "
            + Thread.currentThread().getName());
}
```

---

## Output

```text
Inside asyncMethodTest: MyThread-1
Inside asyncMethodTest: MyThread-2
```

---

# 🔥 What Happens Under Load

---

Assume:

```text
corePoolSize = 2
maxPoolSize = 4
queueCapacity = 3
```

If many requests come:

```text
Task 1 → MyThread-1
Task 2 → MyThread-2

Task 3 → Queue
Task 4 → Queue
Task 5 → Queue

Task 6 → MyThread-3
Task 7 → MyThread-4

Task 8 → Rejected
```

---

## RejectedExecutionException

If all threads are busy and queue is full:

```text
New async task is rejected
```

This can result in:

```text
java.util.concurrent.RejectedExecutionException
```

---

# 🔥 Why Executor Name Is Important

---

If multiple executors exist, Spring may not know which executor to use.

So this is better:

```java
@Async("myThreadPoolExecutor")
```

---

# 🔥 Custom Java ThreadPoolExecutor

---

## Example

```java
@Configuration
public class AsyncConfig {

    @Bean(name = "javaThreadPoolExecutor")
    public Executor javaThreadPoolExecutor() {

        int minPoolSize = 2;
        int maxPoolSize = 4;
        int queueSize = 3;

        return new ThreadPoolExecutor(
                minPoolSize,
                maxPoolSize,
                1,
                TimeUnit.HOURS,
                new ArrayBlockingQueue<>(queueSize),
                new CustomThreadFactory()
        );
    }
}
```

---

## Custom ThreadFactory

```java
public class CustomThreadFactory implements ThreadFactory {

    private final AtomicInteger counter = new AtomicInteger(1);

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName("MyThread-" + counter.getAndIncrement());
        return thread;
    }
}
```

---

# 🔥 AsyncConfigurer

---

## 📌 Definition

`AsyncConfigurer` allows us to define the default executor for all `@Async` methods.

---

## Why Use It

Instead of writing:

```java
@Async("myThreadPoolExecutor")
```

everywhere, we can configure one default executor.

---

## Example

```java
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(3);
        executor.setThreadNamePrefix("MyThread-");

        executor.initialize();

        return executor;
    }
}
```

---

## Usage

```java
@Async
public void asyncMethodTest() {
    System.out.println("Inside asyncMethodTest: "
            + Thread.currentThread().getName());
}
```

Even without executor name:

```text
My custom executor is used
```

---

# 🔥 Important Rules of Async

---

# 🔥 Rule 1 Method Must Be Called Through Spring Bean

---

## ❌ Wrong

```java
@Service
public class UserService {

    public void mainMethod() {
        asyncMethod();
    }

    @Async
    public void asyncMethod() {
        System.out.println("Async");
    }
}
```

---

## Why It Fails

```text
This is self-invocation
Proxy is bypassed
Async will not work
```

---

## ✅ Correct

```java
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/call")
    public String call() {
        userService.asyncMethod();
        return "done";
    }
}
```

---

# 🔥 Rule 2 Async Works Best on Public Methods

---

Spring proxy can intercept external public method calls reliably.

Avoid using it on private methods.

---

# 🔥 Rule 3 Return Types

---

Async methods can return:

```text
void
Future
CompletableFuture
```

---

## Void Example

```java
@Async
public void sendEmail() {
    System.out.println("Email sent");
}
```

---

## CompletableFuture Example

```java
@Async
public CompletableFuture<String> fetchReport() {

    return CompletableFuture.completedFuture("Report ready");
}
```

---

# 🔥 Rule 4 Exceptions in Void Async Methods

---

If an async method returns `void`, exceptions do not come back to caller thread.

---

## Example

```java
@Async
public void sendEmail() {
    throw new RuntimeException("Email failed");
}
```

The controller will not catch this exception because it is running in another thread.

---

## Solution

Use `AsyncUncaughtExceptionHandler`.

```java
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> {
            System.out.println("Async exception in method: "
                    + method.getName()
                    + " message: "
                    + ex.getMessage());
        };
    }
}
```

---

# 🔥 Rule 5 Exceptions with CompletableFuture

---

If async method returns `CompletableFuture`, handle exception like this:

```java
asyncService.fetchReport()
        .exceptionally(ex -> {
            System.out.println("Exception: " + ex.getMessage());
            return "Fallback response";
        });
```

---

# 🔥 Real Example in File Processing App

---

## Use Case

After file upload succeeds:

```text
Send notification asynchronously
```

File upload response should not wait for notification sending.

---

## Notification Service

```java
@Service
public class NotificationService {

    @Async("myThreadPoolExecutor")
    public void sendUploadNotification(String uploadedBy, String fileName) {

        System.out.println("Sending notification to: " + uploadedBy
                + " for file: " + fileName
                + " | Thread: "
                + Thread.currentThread().getName());
    }
}
```

---

## FileUploadService

```java
@Transactional
public FileUploadResponse uploadAndProcessFile(
        MultipartFile file,
        String fileType,
        String uploadedBy
) {
    FileMetadata metadata = new FileMetadata(
            file.getOriginalFilename(),
            fileType.toUpperCase(),
            uploadedBy,
            "PROCESSING"
    );

    repository.save(metadata);

    FileProcessor processor = factory.getFileProcessor(fileType);
    processor.process(file);

    metadata.setStatus("SUCCESS");

    notificationService.sendUploadNotification(
            uploadedBy,
            file.getOriginalFilename()
    );

    return new FileUploadResponse(
            "File uploaded and processed successfully",
            fileType.toUpperCase(),
            "SUCCESS"
    );
}
```

---

## Result

```text
API returns quickly
Notification runs in background thread
```

---

# 🔥 Async and Transactional

---

## Important Concept

Async method runs in a different thread.

Transaction context is thread-bound.

---

## Meaning

```text
Transaction from caller thread does not automatically continue into async thread
```

---

## Example

```java
@Transactional
public void uploadFile() {
    repository.save(metadata);
    asyncService.sendNotification();
}
```

The transaction belongs to `uploadFile` thread.

The async method runs in another thread.

---

## Important Warning

Do not assume async method participates in caller transaction.

If async method needs DB transaction, add `@Transactional` on async method separately.

---

# 🔥 When to Use Async

---

Use when:

```text
Task is independent
Caller does not need immediate result
Task is slow
Background processing is acceptable
```

---

## Good Use Cases

```text
Email sending
Notification
Audit log
Report generation
File processing after upload
External API call that need not block response
```

---

# 🔥 When Not to Use Async

---

Avoid when:

```text
Caller needs result immediately
Task must be part of same transaction
Ordering is critical
Errors must be returned directly to API client
```

---

# 🔥 Common Mistakes

---

```text
Forgetting @EnableAsync
Calling async method from same class
Using SimpleAsyncTaskExecutor in production
Not configuring thread pool
Ignoring exceptions from void async methods
Assuming caller transaction continues into async method
Creating too many threads
Using @Async on private methods
```

---

# 🔥 Interview Questions

---

## What is Async

```text
Async is used to run a method asynchronously in a separate thread.
```

---

## Why use Async

```text
To avoid blocking the main request thread for long-running independent tasks.
```

---

## How does Async work internally

```text
Spring creates a proxy. The proxy intercepts the method call and submits the task to an Executor.
```

---

## What is ThreadPoolTaskExecutor

```text
It is Spring's wrapper around Java thread pool executor used to manage async threads.
```

---

## Why not use SimpleAsyncTaskExecutor

```text
It creates a new thread for every task and can cause thread exhaustion and high memory usage.
```

---

## What happens if queue is full

```text
If queue is full and max threads are busy, new tasks are rejected.
```

---

## Why does self-invocation fail

```text
Because the method call does not go through Spring proxy.
```

---

## Does Async share caller transaction

```text
No. Async runs in a different thread, and transaction context is thread-bound.
```

---

# 🔥 Memory Trick

---

```text
Async = Proxy + Executor + Background Thread
```

---

```text
Thread Pool = Reuse threads instead of creating unlimited threads
```

---

```text
Executor decides where async task runs
```

---

# 🚀 Final Summary

---

* Async runs methods in separate threads.
* EnableAsync is required.
* Async works using Spring proxy.
* Proxy submits task to Executor.
* Thread pool prevents unlimited thread creation.
* SimpleAsyncTaskExecutor creates new threads and is not recommended for production.
* ThreadPoolTaskExecutor is preferred.
* Custom executor should be configured for production.
* Use executor name when multiple executors exist.
* Use AsyncConfigurer to define default executor.
* Self-invocation does not work.
* Async and transaction contexts are different because they run in different threads.

---

```text
Async = Non-blocking execution using proxy and executor
```

---
