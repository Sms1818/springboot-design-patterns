# ***Spring Boot Security – Part 2 (User Creation & UserDetailsService)***

---

# 🔥 User Creation in Spring Security

---

Before learning:

```text
JWT
OAuth2
Authorization
Security Filters
```

first we must understand:

```text
How users are created?
How Spring Security stores users?
How authentication validates users?
```

because:

```text
Authentication can happen only after user creation.
```

---

# 🔥 What Happens After Adding Security Dependency?

---

# 💻 pom.xml

```xml
<dependency>

    <groupId>
        org.springframework.boot
    </groupId>

    <artifactId>
        spring-boot-starter-security
    </artifactId>

</dependency>
```

---

# 🧠 During Application Startup

---

Spring Boot automatically:

- Creates default user
- Generates random password
- Enables authentication
- Secures all APIs

---

# 🔥 Default Credentials

---

## 📌 Username

```text
user
```

---

## 📌 Password

```text
Randomly generated
```

---

# 🧠 Important

Every server restart:

```text
new password generated
```

---

# 🔥 Internal Spring Boot Logic

---

Spring internally uses:

```text
SecurityProperties.java
```

---

# 💻 Internal Code

```java
public static class User {

    private String name = "user";

    private String password =
            UUID.randomUUID().toString();

    private List<String> roles =
            new ArrayList<>();

    private boolean passwordGenerated = true;
}
```

---

# 🔥 Internal Flow

---

```text
Application Starts
        ↓
Security AutoConfiguration Executes
        ↓
Default User Created
        ↓
Random Password Generated
        ↓
All APIs Secured
```

---

# 🔥 How Spring Creates Default User?

---

Spring Boot internally uses:

```text
UserDetailsServiceAutoConfiguration
```

which creates:

```text
InMemoryUserDetailsManager
```

---

# 🔥 Internal Architecture

---

```text
Security Auto Configuration
            ↓
UserDetailsServiceAutoConfiguration
            ↓
InMemoryUserDetailsManager
            ↓
Default User Created
```

---

# 🔥 Ways to Control User Creation

---

```text
1. application.properties

2. InMemoryUserDetailsManager

3. Database Authentication
```

---

# 🔥 1. Using application.properties

---

# ⚠️ Important

Mostly used only for:

```text
Development
Testing
```

NOT production.

---

# 💻 application.properties

```properties
spring.security.user.name=my_username

spring.security.user.password=my_password

spring.security.user.roles=ADMIN
```

---

# 🧠 What Happens Internally?

---

Spring internally:

- Reads properties
- Uses reflection
- Calls setter methods
- Overrides default user

---

# 🔥 Internal Flow

---

```text
application.properties
        ↓
SecurityProperties
        ↓
setUsername()
setPassword()
setRoles()
        ↓
Default user overridden
```

---

# 🔥 2. Using InMemoryUserDetailsManager

---

# 📌 Definition

Spring Security provides:

```text
InMemoryUserDetailsManager
```

to store users in memory.

---

# ⚠️ Important

Mostly used for:

```text
Development
Testing
POC
```

NOT production.

---

# 🔥 InMemory Architecture

---

```text
UserDetailsService
        ↓
UserDetailsManager
        ↓
InMemoryUserDetailsManager
        ↓
Users stored in RAM
```

---

# 💻 SecurityConfig.java

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails user1 = User
                .withUsername("my_username_1")

                .password("{noop}my_password_1")

                .roles("ADMIN")

                .build();

        UserDetails user2 = User
                .withUsername("my_username_2")

                .password("{noop}1234")

                .roles("USER")

                .build();

        return new InMemoryUserDetailsManager(
                user1,
                user2
        );
    }
}
```

---

# 🔥 What is {noop}?

---

Spring Security password format is:

```text
{id}encodedPassword
```

---

# 📌 Examples

```text
{noop}password

{bcrypt}hashed_password

{sha256}hashed_password
```

---

# 🔥 Meaning of Different IDs

---

| ID | Meaning |
|---|---|
| {noop} | No hashing |
| {bcrypt} | BCrypt hashing |
| {sha256} | SHA256 hashing |

---

# 🔥 Authentication Flow with {noop}

---

```text
User enters password
        ↓
Spring fetches stored password
        ↓
Reads {noop}
        ↓
No hashing performed
        ↓
Plain password comparison
```

---

# 🔥 BCrypt Password Example

---

# 💻 SecurityConfig.java

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails user1 = User

                .withUsername("my_username_1")

                .password(
                        "{bcrypt}" +
                        new BCryptPasswordEncoder()
                                .encode("my_password_1")
                )

                .roles("ADMIN")

                .build();

        return new InMemoryUserDetailsManager(
                user1
        );
    }
}
```

---

# 🔥 How BCrypt Authentication Works?

---

# 🧠 Internal Flow

---

```text
Stored Password:
{bcrypt}hashed_password
        ↓
DelegatingPasswordEncoder checks {bcrypt}
        ↓
BCryptPasswordEncoder selected
        ↓
Incoming password hashed
        ↓
Hash comparison happens
```

---

# 🔥 DelegatingPasswordEncoder

---

# 📌 Purpose

Automatically selects correct:

```text
PasswordEncoder
```

based on:

```text
{id}
```

---

# 🔥 Problem with {bcrypt}

---

Sometimes we don't want:

```text
{bcrypt}
```

prefix inside stored password.

---

# 🔥 Solution → Define PasswordEncoder Bean

---

# 💻 SecurityConfig.java

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails user1 = User

                .withUsername("my_username_1")

                .password(
                        new BCryptPasswordEncoder()
                                .encode("my_password_1")
                )

                .roles("ADMIN")

                .build();

        return new InMemoryUserDetailsManager(
                user1
        );
    }
}
```

---

# 🧠 What Happens Now?

---

Spring directly uses:

```text
BCryptPasswordEncoder
```

without checking:

```text
{id}
```

---

# 🔥 Why Password Hashing is Important?

---

Never store:

```text
Plain passwords
```

inside database.

---

# ❌ Wrong

```text
password = "admin123"
```

---

# ✅ Correct

```text
password =
"$2a$10$..."
```

---

# 🔥 3. Storing Users in Database

---

# ✅ Recommended for Production

---

Because:

```text
Users should persist permanently
```

instead of memory.

---

# 🔥 Entity Architecture

---

```text
Database
    ↓
UserAuthEntity
    ↓
Repository
    ↓
UserDetailsService
    ↓
Spring Security
```

---

# 💻 UserAuthEntity.java

```java
@Entity
@Table(name = "user_auth")

public class UserAuthEntity
        implements UserDetails {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(
            unique = true,
            nullable = false
    )
    private String username;

    @Column(nullable = false)
    private String password;

    private String role;

    @Override
    public Collection<? extends GrantedAuthority>
    getAuthorities() {

        return List.of(
                new SimpleGrantedAuthority(role)
        );
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

---

# 🔥 Why Entity Implements UserDetails?

---

Because Spring Security internally expects:

```text
UserDetails object
```

during authentication.

---

# 🧠 Benefit

---

No manual mapping required:

```text
Entity → UserDetails
```

---

# 💻 Repository

```java
@Repository
public interface UserAuthEntityRepository

        extends JpaRepository<
                UserAuthEntity,
                Long
        > {

    Optional<UserAuthEntity>
    findByUsername(String username);
}
```

---

# 💻 UserAuthEntityService.java

```java
@Service
public class UserAuthEntityService
        implements UserDetailsService {

    @Autowired
    private UserAuthEntityRepository
            userAuthEntityRepository;

    public UserDetails save(
            UserAuthEntity userAuth
    ) {

        return userAuthEntityRepository
                .save(userAuth);
    }

    @Override
    public UserAuthEntity
    loadUserByUsername(String username)

            throws UsernameNotFoundException {

        return userAuthEntityRepository

                .findByUsername(username)

                .orElseThrow(() ->

                        new UsernameNotFoundException(
                                "User not found"
                        )
                );
    }
}
```

---

# 🔥 Why Implement UserDetailsService?

---

Because Spring Security does not know:

```text
How to fetch users from DB
```

---

# 🧠 During Authentication

---

Spring internally calls:

```java
loadUserByUsername()
```

---

# 🔥 Registration API

---

# 💻 UserAuthController.java

```java
@RestController
@RequestMapping("/auth")
public class UserAuthController {

    @Autowired
    private UserAuthEntityService
            userAuthEntityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(

            @RequestBody
            UserAuthEntity userAuthDetails
    ) {

        userAuthDetails.setPassword(

                passwordEncoder.encode(
                        userAuthDetails.getPassword()
                )
        );

        userAuthEntityService
                .save(userAuthDetails);

        return ResponseEntity.ok(
                "User registered successfully!"
        );
    }
}
```

---

# 🔥 Important

---

Always hash password before storing.

---

# ❌ Wrong

```text
Store plain password
```

---

# ✅ Correct

```text
Store hashed password
```

---

# 🔥 Problem with Registration API

---

By default:

```text
All endpoints are authenticated
```

---

# ❓ Then how will user access:

```text
/auth/register
```

without authentication?

---

# 🔥 Solution → permitAll()

---

# 💻 SecurityConfig.java

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain
    securityFilterChain(HttpSecurity http)

            throws Exception {

        http

            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/auth/register")
                .permitAll()

                .anyRequest()
                .authenticated()
            )

            .csrf(csrf -> csrf.disable())

            .httpBasic(
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
/auth/register
→ public API

Remaining APIs
→ authenticated
```

---

# 🎯 Interview Questions

---

# 🔹 What happens after adding spring-boot-starter-security?

Spring Boot:

- Secures all APIs
- Creates default user
- Generates random password

---

# 🔹 What is InMemoryUserDetailsManager?

Stores users in RAM.

---

# 🔹 Why UserAuthEntity implements UserDetails?

Because Spring Security internally expects UserDetails object.

---

# 🔹 Why implement UserDetailsService?

To teach Spring Security how to fetch users from DB.

---

# 🔹 What is DelegatingPasswordEncoder?

Chooses PasswordEncoder using:

```text
{id}
```

---

# 🔹 Difference:
{noop} vs {bcrypt}?

| {noop} | {bcrypt} |
|---|---|
| Plain password | Hashed password |
| Unsafe | Secure |

---

# 🔹 Why use BCrypt?

Because passwords should never be stored in plain text.

---

# 🔹 What does permitAll() do?

Allows public access without authentication.

---

# 🔹 Why BCryptPasswordEncoder Bean used?

To avoid manually adding:

```text
{bcrypt}
```

prefix.

---

# 🚀 Final Summary

---

```text
Spring Security:
- Automatically creates default user
- Uses InMemoryUserDetailsManager internally

Ways to create users:
1. application.properties
2. InMemoryUserDetailsManager
3. Database Authentication

Password Security:
- Use BCrypt hashing
- Never store plain password

Core Interfaces:
- UserDetails
- UserDetailsService

permitAll():
- Makes APIs public
```