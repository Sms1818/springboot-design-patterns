# ***Spring Boot Security – Part 1 (Architecture & Setup)***

---

# 🔥 Why Spring Security is Needed?

---

Modern applications are vulnerable to attacks like:

```text
CSRF  → Cross-Site Request Forgery

CORS  → Cross-Origin Resource Sharing

SQL Injection

XSS   → Cross-Site Scripting
```

---

# 🔥 Goal of Spring Security

---

Spring Security protects:

```text
Users
Sessions
APIs
Database
Resources
```

using:

```text
Authentication
Authorization
Security Filters
Session Management
CSRF Protection
```

---

# 🔥 Two Core Concepts

---

# 🔐 Authentication

---

## 📌 Definition

Authentication verifies:

```text
WHO YOU ARE
```

---

## 🧠 Example

```text
Username + Password verification
```

---

# 🔐 Authorization

---

## 📌 Definition

Authorization checks:

```text
WHAT YOU ARE ALLOWED TO DO
```

---

## 🧠 Example

```text
ADMIN → can delete users

NORMAL USER → cannot delete users
```

---

# 🔥 Authentication vs Authorization

---

| Authentication | Authorization |
|---|---|
| Verify identity | Verify permissions |
| Who are you? | What can you access? |
| Login process | Access control |
| Happens first | Happens after authentication |

---

# 🔥 Normal Spring Boot Flow

---

```text
Client Request
        ↓
Tomcat (Servlet Container)
        ↓
Filter Chain
        ↓
Dispatcher Servlet
        ↓
Interceptors
        ↓
Controller
        ↓
Response
```

---

# 🔥 Spring Security Architecture

---

Spring Security sits inside:

```text
FILTER CHAIN
```

before request reaches:

```text
DispatcherServlet / Controller
```

---

# 🔥 Spring Security Flow

---

```text
Client Request
        ↓
Tomcat
        ↓
Filter Chain
        ↓
Security Filter Chain
        ↓
Authentication
        ↓
Authorization
        ↓
Dispatcher Servlet
        ↓
Controller
        ↓
Response
```

---

# 🔥 Why Spring Security Uses Filters?

---

Because filters execute:

```text
BEFORE DispatcherServlet
```

which allows Spring Security to:

- Block requests early
- Authenticate requests
- Validate tokens
- Validate sessions
- Prevent unauthorized access

before controller execution.

---

# 🔥 Internal Security Architecture

---

```text
Request
   ↓

Security Filter Chain
   ↓

Authentication Filter
   ↓

AuthenticationManager
   ↓

AuthenticationProvider
   ↓

UserDetailsService
   ↓

Database / Memory / LDAP
   ↓

Authentication Success
   ↓

Authorization Check
   ↓

Controller Access
```

---

# 🔥 Important Components

---

| Component | Responsibility |
|---|---|
| SecurityFilterChain | Main security filter pipeline |
| AuthenticationFilter | Extract credentials |
| AuthenticationManager | Coordinates authentication |
| AuthenticationProvider | Performs authentication |
| UserDetailsService | Loads user details |
| PasswordEncoder | Verifies passwords |
| SecurityContext | Stores authenticated user |

---

# 🔥 SecurityFilterChain

---

# 📌 Definition

Main entry point of Spring Security.

---

## 🧠 Responsibilities

- Authentication
- Authorization
- CSRF protection
- Session management
- Security filters
- CORS handling

---

# 🔥 AuthenticationManager

---

# 📌 Purpose

Responsible for:

```text
Authenticating user
```

---

# 🔥 AuthenticationProvider

---

# 📌 Purpose

Contains actual authentication logic.

---

## 🧠 Examples

```text
Validate password
Validate JWT
Validate OAuth token
```

---

# 🔥 UserDetailsService

---

# 📌 Purpose

Loads user from:

```text
Database
Memory
LDAP
External service
```

---

# 🔥 SecurityContext

---

# 📌 Purpose

Stores currently authenticated user information.

---

## 🧠 Internally Uses

```text
ThreadLocal
```

because every request runs in separate thread.

---

# 🔥 Spring Security Dependency

---

# 💻 pom.xml

```xml
<dependency>

    <groupId>
        org.springframework.boot
    </groupId>

    <!--
        Provides:
        - Authentication
        - Authorization
        - Security Filters
        - CSRF Protection
        - Session Security
    -->

    <artifactId>
        spring-boot-starter-security
    </artifactId>

</dependency>
```

---

# 🔥 What Happens After Adding Dependency?

---

Spring Boot automatically:

- Secures all endpoints
- Enables login page
- Enables CSRF protection
- Creates default user
- Generates default password

---

# 🔥 Default Generated Password

---

At application startup:

```text
Using generated security password:
xxxxxxxx
```

appears in console.

---

# 🔥 Session Persistence Dependency

---

# 📌 Why Needed?

To persist sessions inside:

```text
Relational Database
```

instead of memory.

---

# 💻 Dependency

```xml
<dependency>

    <groupId>
        org.springframework.session
    </groupId>

    <artifactId>
        spring-session-jdbc
    </artifactId>

</dependency>
```

---

# 🔥 Why Persist Sessions?

---

Without persistence:

```text
Application restart
→ sessions lost
```

---

# 🔥 Advantages of Session Persistence

---

- Session survives restart
- Multi-instance support
- Distributed systems support
- Shared session management

---

# 🔥 Authentication Mechanisms Supported

---

```text
1. Form Login        (Stateful)

2. Basic Authentication (Stateless)

3. JWT Authentication   (Stateless)

4. OAuth2

    a. Authorization Code
    b. Client Credentials
    c. Password Grant

5. API Key Authentication
```

---

# 🔥 Stateful vs Stateless Authentication

---

| Stateful | Stateless |
|---|---|
| Session based | Token based |
| Stores session on server | No session storage |
| Cookie based | Header based |
| Less scalable | Highly scalable |

---

# 🔥 High-Level Authentication Flow

---

```text
Client Request
        ↓
Security Filter Chain
        ↓
Authentication Filter
        ↓
AuthenticationManager
        ↓
AuthenticationProvider
        ↓
UserDetailsService
        ↓
User Validated
        ↓
SecurityContext Updated
        ↓
Authorization Check
        ↓
Controller Access
        ↓
Response
```

---

# 🎯 Interview Questions

---

# 🔹 Why Spring Security uses Filters instead of Interceptors?

Because filters execute before DispatcherServlet.

---

# 🔹 Difference:
Authentication vs Authorization?

| Authentication | Authorization |
|---|---|
| Verify identity | Verify permissions |

---

# 🔹 What is SecurityFilterChain?

Main filter pipeline of Spring Security.

---

# 🔹 What is AuthenticationManager?

Coordinates authentication process.

---

# 🔹 What is AuthenticationProvider?

Contains actual authentication logic.

---

# 🔹 What is UserDetailsService?

Loads user information.

---

# 🔹 What is SecurityContext?

Stores authenticated user details.

---

# 🔹 What happens after adding spring-boot-starter-security?

All endpoints become secured automatically.

---

# 🔹 Difference:
Stateful vs Stateless?

| Stateful | Stateless |
|---|---|
| Session based | Token based |
| Server stores session | No session storage |

---

# 🚀 Final Summary

---

```text
Spring Security:
- Protects application resources
- Works using Security Filter Chain
- Handles Authentication & Authorization

Core Components:
- SecurityFilterChain
- AuthenticationManager
- AuthenticationProvider
- UserDetailsService
- SecurityContext

Spring Security uses:
- Filters
NOT Interceptors

Authentication Types:
- Form Login
- Basic Auth
- JWT
- OAuth2
- API Key
```

