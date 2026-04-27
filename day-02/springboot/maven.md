# ***Maven***

---

# 🔥 **What is Maven?**

---

## 📌 Definition

Maven is a **project management and build automation tool** used in Java.

---

## 🧠 **Core Idea**

```text
You define WHAT you need → Maven handles HOW to build it
```

---

## 🎯 Maven Helps With

```text
Build generation
Dependency management
Project structure
Packaging (JAR/WAR)
Testing
Deployment
```

---

# 🔥 **Why Maven is Needed**

---

## ❌ Without Maven

```text
Download JARs manually
Handle versions manually
Manage classpath manually
Compile manually
Messy project structure
```

---

## ✅ With Maven

```text
Automatic dependency download
Standard project structure
One command build
Easy packaging and deployment
```

---

# 🔥 **pom.xml**

---

## 📌 What is pom.xml?

```text
Project Object Model
```

---

## 🧠 Meaning

```text
Heart of Maven project
Contains all configurations
```

---

## 🔹 What it Contains

```text
Project details
Dependencies
Plugins
Build configuration
```

---

## 🔹 Example

```xml
<project>
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>demo</artifactId>
    <version>1.0.0</version>

    <dependencies>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

    </dependencies>

</project>
```

---

## 🧠 Key Terms

```text
groupId    → company/project group
artifactId → project name
version    → project version
```

---

# 🔥 **Project Structure**

---

```text
project-root/
│
├── pom.xml
│
└── src/
    ├── main/
    │   ├── java/
    │   └── resources/
    │
    └── test/
        └── java/
```

---

## 🧠 Meaning

```text
main → application code
test → test cases
resources → config files (application.properties)
```

---

# 🔥 **Maven Lifecycle**

---

## 📌 Lifecycle Phases

```text
validate → compile → test → package → verify → install → deploy
```

---

## 🧠 Rule

```text
If you run a phase → all previous phases also run
```

---

## Example

```bash
mvn package
```

Runs:

```text
validate → compile → test → package
```

---

# 🔥 **Lifecycle Phases Explained**

---

## 🔹 1. validate

```text
Checks project structure
```

---

## 🔹 2. compile

```bash
mvn compile
```

```text
Compiles Java code
Output → target/classes
```

---

## 🔹 3. test

```bash
mvn test
```

```text
Runs unit tests
Uses JUnit
```

---

## 🔹 4. package

```bash
mvn package
```

```text
Creates .jar or .war file
```

---

## 🔹 5. verify

```text
Runs additional checks
(code quality, etc.)
```

---

## 🔹 6. install

```bash
mvn install
```

```text
Stores artifact in local repository
(~/.m2 folder)
```

---

## 🔹 7. deploy

```bash
mvn deploy
```

```text
Uploads artifact to remote repository
```

---

# 🔥 **Target Folder (Build Output)**

---

After build:

```text
target/
├── classes/
├── test-classes/
├── *.jar
```

---

## 🧠 Meaning

```text
Compiled code and packaged files go here
```

---

# 🔥 **Dependencies**

---

## 📌 What are Dependencies?

```text
External libraries used in project
```

---

## Example

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

---

## 🧠 What Happens?

```text
Maven downloads dependency automatically
Stores in ~/.m2 repository
Adds to project classpath
```

---

# 🔥 **Local Repository**

---

## 📌 Location

```text
~/.m2/repository
```

---

## 🧠 Meaning

```text
All downloaded dependencies stored here
```

---

# 🔥 **Plugins**

---

## 📌 What are Plugins?

```text
Tools that extend Maven functionality
```

---

## Example

```xml
<build>
    <plugins>

        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
        </plugin>

    </plugins>
</build>
```

---

## 🧠 Meaning

```text
Plugins help in compile, test, package, etc.
```

---

# 🔥 **Important Commands**

---

## Clean

```bash
mvn clean
```

```text
Deletes target folder
```

---

## Compile

```bash
mvn compile
```

---

## Test

```bash
mvn test
```

---

## Package

```bash
mvn package
```

---

## Install

```bash
mvn install
```

---

## Deploy

```bash
mvn deploy
```

---

# 🔥 **Maven vs Ant**

---

| Feature | Ant | Maven |
|--------|-----|------|
| Approach | Procedural | Declarative |
| Config | Manual steps | Convention-based |
| Dependencies | Manual | Automatic |
| Structure | Flexible | Standard |

---

## 🧠 Simple Meaning

```text
Ant → You tell HOW to build
Maven → You tell WHAT to build
```

---

# 🔥 **Spring Boot + Maven**

---

## 📌 Important

Spring Boot uses Maven for dependency management.

---

## Example

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
</parent>
```

---

## 🧠 Meaning

```text
Spring Boot manages versions automatically
```

---

# 🔥 **Real Project Flow**

---

## Step 1

```bash
mvn clean
```

---

## Step 2

```bash
mvn package
```

---

## Step 3

```bash
java -jar target/app.jar
```

---

# 🔥 **Common Mistakes**

---

## ❌ Adding wrong dependency version

---

## ❌ Forgetting mvn clean

---

## ❌ Editing target folder manually

---

## ❌ Not understanding lifecycle

---

# 🔥 **Interview Questions**

---

## 🔹 What is Maven?

```text
Build and dependency management tool
```

---

## 🔹 What is pom.xml?

```text
Configuration file of Maven
```

---

## 🔹 What is lifecycle?

```text
Sequence of build phases
```

---

## 🔹 Where dependencies stored?

```text
~/.m2 repository
```

---

## 🔹 compile vs package?

```text
compile → compiles code  
package → creates jar/war  
```

---

# 🚀 **Final Summary**

---

* Maven manages project build lifecycle  
* Uses pom.xml  
* Handles dependencies automatically  
* Standard project structure  
* One command build system  

---

```text
Maven = Build + Dependency + Lifecycle Manager
```

---

👉 This is **must-know for Spring Boot + interviews**