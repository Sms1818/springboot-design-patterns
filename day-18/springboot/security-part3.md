# ***Spring Boot Security – Part 3 (Form Authentication & Session Management)***

---

# 🔥 What is Form Based Authentication?

---

Form Authentication is the default authentication mechanism provided by:

```text
Spring Security
```

---

# 🧠 Core Idea

---

```text
User enters username/password
        ↓
Spring validates credentials
        ↓
Session created
        ↓
Browser stores JSESSIONID
        ↓
Subsequent requests authenticated
using session
```

---

# 🔥 Form Authentication is Stateful

---

# 📌 Meaning

Server maintains:

```text
Authentication State
```

using:

```text
HTTP Session
```

---

# 🔥 Stateful Authentication Flow

---

```text
User Login
        ↓
Authentication Success
        ↓
HTTP Session Created
        ↓
JSESSIONID Generated
        ↓
Browser stores cookie
        ↓
Future requests use session
```

---

# 🔥 Important Components

---

| Component | Responsibility |
|---|---|
| UsernamePasswordAuthenticationFilter | Extract username/password |
| AuthenticationManager | Coordinate authentication |
| AuthenticationProvider | Validate credentials |
| SecurityContext | Store authenticated user |
| HttpSession | Persist authentication |

---

# 🔥 Default Login Page

---

After enabling Spring Security:

```text
/login
```

page is automatically created.

---

# 🔥 Default Logout URL

---

Spring Security also provides:

```text
/logout
```

endpoint automatically.

---

# 🔥 Default Spring Security Configuration

---

# 💻 Internal Configuration

```java
@Bean
@Order(SecurityProperties.BASIC_AUTH_ORDER)

SecurityFilterChain
defaultSecurityFilterChain(HttpSecurity http)
        throws Exception {

    http.authorizeHttpRequests(

            requests ->

                    requests.anyRequest()
                            .authenticated()
    );

    http.formLogin(withDefaults());

    http.httpBasic(withDefaults());

    return http.build();
}
```

---

# 🧠 Meaning

---

Spring Boot automatically enables:

- Form Login
- HTTP Basic Authentication
- Authentication for all APIs

---

# 🔥 Custom SecurityFilterChain

---

# 💻 SecurityConfig.java

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain
    securityFilterChain(HttpSecurity http)

            throws Exception {

        http

            .authorizeHttpRequests(auth -> auth

                .anyRequest()
                .authenticated()
            )

            .formLogin(
                    Customizer.withDefaults()
            );

        return http.build();
    }
}
```

---

# 🔥 Form Login Flow Internally

---

```text
Client sends credentials
        ↓
UsernamePasswordAuthenticationFilter
        ↓
AuthenticationManager
        ↓
AuthenticationProvider
        ↓
UserDetailsService
        ↓
Password verification
        ↓
Authentication object created
        ↓
Stored in SecurityContext
        ↓
Session created
        ↓
JSESSIONID returned
```

---

# 🔥 UsernamePasswordAuthenticationFilter

---

# 📌 Responsibility

Extracts:

```text
username
password
```

from login request.

---

# 🔥 AuthenticationManager

---

# 📌 Responsibility

Coordinates authentication process.

---

# 🔥 AuthenticationProvider

---

# 📌 Responsibility

Performs actual credential validation.

---

# 🔥 SecurityContext

---

# 📌 Purpose

Stores authenticated user details.

---

# 🧠 Internally Uses

```text
ThreadLocal
```

---

# 🔥 SecurityContext Flow

---

```text
Authentication Success
        ↓
Authentication object created
        ↓
Stored in SecurityContext
        ↓
Accessible during request lifecycle
```

---

# 🔥 Session Creation

---

# 📌 After Successful Login

Spring creates:

```text
HttpSession
```

---

# 🧠 Session Contains

---

```text
SecurityContext
Authenticated User
Roles
Authorities
```

---

# 🔥 JSESSIONID

---

# 📌 Definition

Unique identifier representing:

```text
HTTP Session
```

---

# 🧠 Flow

---

```text
Session Created
        ↓
JSESSIONID generated
        ↓
Returned in Set-Cookie header
        ↓
Browser stores cookie
```

---

# 🔥 Subsequent Request Flow

---

```text
Browser sends JSESSIONID
        ↓
Spring finds session
        ↓
SecurityContext restored
        ↓
User authenticated automatically
```

---

# 🔥 Session Timeout

---

# 💻 application.properties

```properties
server.servlet.session.timeout=1m
```

---

# 🧠 Meaning

---

```text
Session expires after 1 minute
of inactivity
```

---

# 🔥 Important

---

If user continuously sends requests:

```text
Session expiry keeps extending
```

---

# 🔥 Session Persistence

---

# 📌 Problem

Without persistence:

```text
Application restart
→ all sessions lost
```

---

# 🔥 Store Sessions in Database

---

# 💻 pom.xml

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

# 💻 application.properties

```properties
spring.session.store-type=jdbc

spring.session.jdbc.initialize-schema=always
```

---

# 🧠 What Happens?

---

Spring Boot automatically creates:

```text
SPRING_SESSION
```

table.

---

# 🔥 Why Store Sessions in DB?

---

Useful for:

```text
Distributed systems
Multiple server instances
Load balancing
```

---

# 🔥 Authorization

---

# 📌 Definition

Checks:

```text
What authenticated user
is allowed to access
```

---

# 🔥 Role-Based Authorization

---

# 💻 SecurityConfig.java

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain
    securityFilterChain(HttpSecurity http)

            throws Exception {

        http

            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/users")
                .hasRole("USER")

                .anyRequest()
                .authenticated()
            )

            .formLogin(
                    Customizer.withDefaults()
            );

        return http.build();
    }
}
```

---

# 🔥 Authorization Flow

---

```text
User Authenticated
        ↓
AuthorizationFilter Executes
        ↓
Required role checked
        ↓
SecurityContext checked
        ↓
Role matched?
        ↓
YES → Access Granted
NO  → 403 Forbidden
```

---

# 🔥 Important

---

When using:

```java
hasRole("USER")
```

Spring internally converts it into:

```text
ROLE_USER
```

---

# 🔥 Multiple Roles

---

# 💻 Example

```java
.requestMatchers("/users")

.hasAnyRole("USER", "ADMIN")
```

---

# 🔥 Multiple Roles in Properties

---

# 💻 application.properties

```properties
spring.security.user.roles=
USER,ADMIN
```

---

# 🔥 permitAll()

---

# 📌 Purpose

Makes API public.

---

# 💻 Example

```java
.requestMatchers("/register")
.permitAll()
```

---

# 🧠 Meaning

---

```text
No authentication required
```

---

# 🔥 authenticated()

---

# 📌 Purpose

Requires authentication.

---

# 💻 Example

```java
.anyRequest()
.authenticated()
```

---

# 🧠 Meaning

---

```text
User must login first
```

---

# 🔥 Restrict Sessions Per User

---

# 📌 Problem

Same user can login from:

```text
Multiple browsers
Multiple devices
```

---

# 🔥 Solution

---

# 💻 SecurityConfig.java

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain
    securityFilterChain(HttpSecurity http)

            throws Exception {

        http

            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/users")
                .hasRole("USER")

                .anyRequest()
                .authenticated()
            )

            .sessionManagement(session ->

                    session

                        .maximumSessions(1)

                        .maxSessionsPreventsLogin(true)
            )

            .formLogin(
                    Customizer.withDefaults()
            );

        return http.build();
    }
}
```

---

# 🧠 Meaning

---

```text
Only 1 active session allowed
per user
```

---

# 🔥 Session Creation Policies

---

# 💻 Example

```java
.sessionManagement(session ->

        session.sessionCreationPolicy(
                SessionCreationPolicy.IF_REQUIRED
        )
)
```

---

# 🔥 Different Session Policies

---

| Policy | Meaning |
|---|---|
| ALWAYS | Always create session |
| NEVER | Never create session but use existing |
| STATELESS | No session creation |
| IF_REQUIRED | Create only when needed |

---

# 🔥 IF_REQUIRED

---

# 📌 Default Policy

---

## 🧠 Behavior

```text
Session created only when required
```

---

# 🔥 STATELESS

---

# 📌 Used For

```text
JWT Authentication
REST APIs
```

---

## 🧠 Behavior

```text
No session created
```

---

# 🔥 Disadvantages of Form Authentication

---

# ❌ Vulnerable to CSRF

---

Because browser automatically sends:

```text
JSESSIONID
```

---

# ❌ Session Hijacking Risk

---

Attackers may steal:

```text
JSESSIONID
```

---

# ❌ Scalability Issues

---

Managing sessions becomes difficult in:

```text
Distributed systems
```

---

# ❌ Server Memory/DB Overhead

---

Sessions require:

- Memory
- DB storage
- Cache management

---

# ⚠️ Important

---

For Form Login:

```text
CSRF should NOT be disabled
```

---

# ❌ Wrong

```java
.csrf(csrf -> csrf.disable())
```

---

# 🎯 Interview Questions

---

# 🔹 What is Form Authentication?

Stateful authentication using sessions.

---

# 🔹 What is JSESSIONID?

Unique identifier representing HTTP session.

---

# 🔹 Why Form Authentication is Stateful?

Because server stores session state.

---

# 🔹 What is UsernamePasswordAuthenticationFilter?

Extracts username & password from request.

---

# 🔹 What happens after successful login?

- Session created
- SecurityContext stored
- JSESSIONID returned

---

# 🔹 Difference:
permitAll() vs authenticated()

| permitAll() | authenticated() |
|---|---|
| Public API | Requires login |

---

# 🔹 What does hasRole("USER") check internally?

```text
ROLE_USER
```

---

# 🔹 Why Form Authentication is less scalable?

Because server must manage sessions.

---

# 🔹 Why session persistence needed?

To survive restart and support distributed systems.

---

# 🔹 Difference:
STATELESS vs IF_REQUIRED?

| STATELESS | IF_REQUIRED |
|---|---|
| No session | Create when needed |
| Used in JWT | Default for form login |

---

# 🚀 Final Summary

---

```text
Form Authentication:
- Stateful authentication
- Uses sessions
- Uses JSESSIONID cookie

Flow:
Login
→ Session Created
→ SecurityContext Stored
→ Browser stores JSESSIONID

Authorization:
- permitAll()
- authenticated()
- hasRole()

Session Management:
- Timeout configurable
- DB persistence supported
- Concurrent sessions controllable

Session Policies:
- ALWAYS
- NEVER
- IF_REQUIRED
- STATELESS
```