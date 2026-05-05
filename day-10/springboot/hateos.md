# ***HATEOAS in Spring Boot***

---

# 🔥 What is HATEOAS?

---

## 📌 Definition

**HATEOAS (Hypermedia As The Engine Of Application State)** is a REST principle where the **server tells the client what actions can be performed next**. 

---

## 🧠 Core Idea

```
Client does NOT decide next API
Server provides next possible actions via links
```

---

## 🎯 Key Concept

Instead of hardcoding API flow on client:

👉 Server sends **links (actions)** in response  
👉 Client simply follows those links  

---

# 🔥 Without HATEOAS

---

## 📌 Example Response

```json
{
  "userID": "123456",
  "name": "SJ",
  "verifyStatus": "UNVERIFIED"
}
```

---

## 🚨 Problem

Client must decide:

- Which API to call next  
- When to call  
- How to call  

---

## ❌ Client Logic (Tight Coupling)

```
if(status == UNVERIFIED) {
   if(type == SMS && state == NOT_STARTED)
       call /sms-verify-start
   else if(state == STARTED)
       call /sms-verify-finish
}
```

---

# 🔥 With HATEOAS

---

## 📌 Response with Links

```json
{
  "userID": "123456",
  "name": "SJ",
  "verifyStatus": "UNVERIFIED",
  "links": [
    {
      "rel": "verify",
      "href": "/api/sms-verify-finish/123456",
      "type": "POST"
    }
  ]
}
```

---

## ✅ Benefit

Client now:

```
if(status == UNVERIFIED) {
   call verify link from response
}
```

👉 No need to understand internal API flow  

---

# 🔥 Why Use HATEOAS?

---

## 🎯 Main Goals

- Loose Coupling  
- API Discovery  

---

## 🧠 Explanation

- Server provides **next possible actions**
- Client becomes **dumb (less logic)**
- API becomes **self-explanatory** 

---

# 🔥 Real Problem Solved

---

## 📌 Tight Coupling Issue

From diagram (page 3):

Client needs to know:
- verifyStatus  
- verifyType  
- verifyState  

And decide next API manually   

---

## ✅ With HATEOAS

- Server sends correct **verify link**
- Client just executes it  

---

# 🔥 Important Rule

---

🚨 Never add all possible actions blindly

---

## ❌ Problems

- Large response payload  
- Increased latency  
- Complex server logic 

---

## ✅ Best Practice

- Add only **relevant next actions**
- Based on current state  

---

# 🔥 Dependency Required

---

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-hateoas</artifactId>
</dependency>
```

---

# 🔥 Implementation

---

## 💻 Controller Example

```java
@RestController
@RequestMapping("/api/")
public class UserController {

    @Autowired
    User user;

    @PostMapping("/adduser")
    public ResponseEntity<UserResponse> addUser() {

        UserResponse response = user.getUser();

        Link verifyLink = WebMvcLinkBuilder
                .linkTo(UserController.class)
                .slash("sms-verify-finish")
                .slash(response.getUserID())
                .withRel("verify")
                .withType("POST");

        response.addLink(verifyLink);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
```

---

# 🔥 Response Model

---

## 💻 Base Class

```java
public class HateoasLinks {

    private List<Link> links = new ArrayList<>();

    public void addLink(Link link) {
        links.add(link);
    }
}
```

---

## 💻 Response DTO

```java
public class UserResponse extends HateoasLinks {

    private String userID;
    private String name;
    private String verifyStatus;

    // getters and setters
}
```

---

# 🔥 Alternative Link Creation

---

```java
Link verifyLink = Link.of("/api/sms-verify-finish/" + response.getUserID())
        .withRel("verify")
        .withType("POST");
```

---

# 🔥 Flow Comparison

---

## Without HATEOAS

```
Client → decides next API → calls API
```

---

## With HATEOAS

```
Client → reads link → calls API
```

---

# 🔥 Advantages

---

- Loose coupling  
- Self-discoverable APIs  
- Less client logic  
- Better API evolution  

---

# 🔥 Disadvantages

---

- Increased response size  
- Added complexity  
- Performance overhead  
- Requires proper design  

---

# 🔥 When to Use

---

- Complex workflows  
- Multi-step processes (like verification)  
- APIs where flow may change  

---

# 🎯 Interview Questions

---

## 🔹 What is HATEOAS?

HATEOAS is a REST principle where server provides next possible actions via links.

---

## 🔹 Why use HATEOAS?

To achieve:
- Loose coupling  
- API discovery  

---

## 🔹 What problem does it solve?

Removes client-side decision logic for API flow.

---

## 🔹 HATEOAS vs Traditional REST?

| Traditional REST | HATEOAS |
|---|---|
| Client decides flow | Server guides flow |
| Tight coupling | Loose coupling |
| Static APIs | Dynamic navigation |

---

## 🔹 What are drawbacks?

- Larger responses  
- Increased complexity  
- Performance impact  

---

## 🔹 When should you NOT use HATEOAS?

- Simple CRUD APIs  
- When flow is fixed  

---

# 🚀 Final Summary

---

```
HATEOAS = Server-driven navigation
Client follows links, not logic
Loose coupling achieved
```