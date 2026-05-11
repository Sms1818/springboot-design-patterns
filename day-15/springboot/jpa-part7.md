# ***JPA in Spring Boot – Part 7 (OneToMany, ManyToOne & ManyToMany)***

---

# 🔥 OneToMany – Unidirectional

---

## 📌 Concept

```
One Parent → Multiple Children
Reference exists only from Parent → Child
```

---

## 🧠 Example

```
One User → Multiple Orders
```

---

# 🔥 Default Behavior

---

## 📌 Important

Since:

```
1 Parent → Many Children
```

We cannot store multiple child IDs in one parent row.

👉 So JPA creates:

```
NEW MAPPING TABLE
```

---

# 🔥 Entity Example

---

## 💻 Parent Entity

```java
@Entity
@Table(name = "user_details")
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderDetails> orderDetails = new ArrayList<>();
}
```

---

## 💻 Child Entity

```java
@Entity
@Table(name = "order_details")
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
}
```

---

# 🔥 Internal DB Structure

---

## 📌 JPA Creates Extra Table

```
user_details
order_details
user_details_order_details
```

---

## 🧠 Why?

Because:

```
One parent cannot store multiple FK values
```

---

# 🔥 Avoid Extra Mapping Table

---

## 📌 Solution → @JoinColumn

---

## 💻 Example

```java
@OneToMany(cascade = CascadeType.ALL)
@JoinColumn(
    name = "user_id_fk",
    referencedColumnName = "userId"
)
private List<OrderDetails> orderDetails;
```

---

# 🔥 Result

---

## 📌 FK stored in Child Table

```
order_details.user_id_fk
```

---

## 🧠 No Extra Mapping Table Created

---

# 🔥 Full Example

---

## 💻 Parent Entity

```java
@Entity
@Table(name = "user_details")
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;
    private String phone;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(
        name = "user_id_fk",
        referencedColumnName = "userId"
    )
    private List<OrderDetails> orderDetails = new ArrayList<>();
}
```

---

## 💻 Child Entity

```java
@Entity
@Table(name = "order_details")
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
}
```

---

# 🔥 Fetch Type

---

## 📌 Default Fetch Type

```java
@OneToMany
```

➡️ Default:

```
LAZY Loading
```

---

# 🔥 LAZY Loading

---

## 📌 Meaning

```
Parent loaded first
Children loaded ONLY when accessed
```

---

## 🧠 Flow

```
SELECT * FROM user_details
        ↓
Access orders
        ↓
SELECT * FROM order_details
```

---

# 🔥 DTO Triggering Lazy Query

---

## 💻 Example

```java
public class UserDetailsDTO {

    private Long id;
    private String name;
    private String phone;
    private List<OrderDetails> orders;

    public UserDetailsDTO(UserDetails userDetails) {

        this.id = userDetails.getUserId();
        this.name = userDetails.getName();
        this.phone = userDetails.getPhone();

        this.orders = userDetails.getOrderDetails();
    }
}
```

---

## 🧠 Important

```java
userDetails.getOrderDetails()
```

👉 Triggers lazy query

---

# 🔥 EAGER Fetch

---

## 📌 Meaning

```
Parent + Child loaded together
```

---

## 💻 Example

```java
@OneToMany(
    cascade = CascadeType.ALL,
    fetch = FetchType.EAGER
)
```

---

# 🔥 SQL Behavior

---

## 🧠 Hibernate Performs

```
LEFT JOIN
```

---

## 📌 Flow

```
SELECT users
LEFT JOIN orders
```

---

# 🔥 Cascade Types

---

# 🔥 CascadeType.PERSIST

---

## 📌 Behavior

```
Saving parent → saves children
```

---

# 🔥 CascadeType.MERGE

---

## 📌 Behavior

```
Updating parent → updates children
```

---

# 🔥 CascadeType.REMOVE

---

## 📌 Behavior

```
Deleting parent → deletes children
```

---

# 🔥 CascadeType.ALL

---

## 📌 Includes

```
PERSIST
MERGE
REMOVE
REFRESH
DETACH
```

---

# 🔥 Orphan Removal (VERY IMPORTANT)

---

## 📌 Definition

Automatically deletes child if removed from parent collection.

---

# 🔥 Example

---

## 💻 Controller

```java
@GetMapping("/user/{id}")
public UserDetails testOrphan(@PathVariable Long id) {

    UserDetails output = userDetailsService.findByID(id);

    output.getOrderDetails().remove(0);

    userDetailsService.saveUser(output);

    return output;
}
```

---

# 🔥 orphanRemoval = false

---

## 💻 Mapping

```java
@OneToMany(
    cascade = CascadeType.ALL,
    orphanRemoval = false
)
```

---

## 🧠 Result

```
Child removed from collection
BUT row still exists in DB
```

---

# 🔥 orphanRemoval = true

---

## 💻 Mapping

```java
@OneToMany(
    cascade = CascadeType.ALL,
    orphanRemoval = true
)
```

---

## 🧠 Result

```
Child removed from collection
        ↓
DELETE query executed
```

---

# 🔥 OneToMany – Bidirectional

---

## 📌 Concept

```
Parent references Child
Child references Parent
```

---

# 🔥 Two Sides

---

| Side | Purpose |
|---|---|
| Owning Side | Holds FK |
| Inverse Side | mappedBy side |

---

# 🔥 Inverse Side

---

## 💻 Parent Entity

```java
@Entity
@Table(name = "user_details")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "userId"
)
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;
    private String phone;

    @OneToMany(
        mappedBy = "userDetails",
        cascade = CascadeType.ALL
    )
    private List<OrderDetails> orderDetails =
            new ArrayList<>();

    public void setOrderDetails(
            List<OrderDetails> orderDetails) {

        this.orderDetails = orderDetails;

        for (OrderDetails order : orderDetails) {
            order.setUserDetails(this);
        }
    }
}
```

---

# 🔥 Owning Side

---

## 💻 Child Entity

```java
@Entity
@Table(name = "order_details")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id"
)
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    @ManyToOne
    @JoinColumn(
        name = "user_id_owning_fk",
        referencedColumnName = "userID"
    )
    private UserDetails userDetails;
}
```

---

# 🔥 Important Rule

---

## 📌 mappedBy Side

```
Does NOT create FK
```

---

## 📌 Owning Side

```
Creates & manages FK
```

---

# 🔥 ManyToOne – Unidirectional

---

## 📌 Concept

```
Many Children → One Parent
```

---

## 🧠 Example

```
Many Orders → One User
```

---

## 📌 Parent has NO reference to child

---

# 🔥 Example

---

## 💻 Parent Entity

```java
@Entity
@Table(name = "user_details")
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;
    private String phone;
}
```

---

## 💻 Child Entity

```java
@Entity
@Table(name = "order_details")
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
        name = "user_id_owning_fk",
        referencedColumnName = "userID"
    )
    private UserDetails userDetails;
}
```

---

# 🔥 ManyToMany – Unidirectional

---

## 📌 Concept

```
Many Orders ↔ Many Products
```

---

## 🧠 Since many-to-many:

```
Join table is mandatory
```

---

# 🔥 Example

---

## 💻 Order Entity

```java
@Entity
@Table(name = "order_details")
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderNo;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "order_product",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns =
            @JoinColumn(name = "product_id")
    )
    private List<ProductDetails> productDetails =
            new ArrayList<>();
}
```

---

## 💻 Product Entity

```java
@Entity
@Table(name = "product_details")
public class ProductDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String name;
    private double price;
}
```

---

# 🔥 Join Table Structure

---

## 📌 JPA Creates

```
order_product
```

---

## 📌 Columns

```
order_id
product_id
```

---

# 🔥 Real Save Flow

---

## 📌 Step 1

Create products first

---

## 📌 Step 2

Attach managed products to order

---

## 💻 Example

```java
@PostMapping(path = "/order")
public OrderDetails insertOrder(
        @RequestBody OrderDetails orderDetail) {

    List<ProductDetails> managedProducts =
        orderDetail.getProductDetails()
        .stream()
        .map(product ->
            productService.findByID(
                product.getProductId()
            )
        )
        .collect(Collectors.toList());

    orderDetail.setProductDetails(managedProducts);

    return orderService.saveOrder(orderDetail);
}
```

---

# 🔥 Why Fetch Managed Products?

---

## 🧠 Reason

```
Avoid detached entity issues
```

---

# 🔥 ManyToMany – Bidirectional

---

## 📌 Concept

```
Both entities reference each other
```

---

# 🔥 Owning Side

---

## 💻 Order Entity

```java
@ManyToMany(cascade = CascadeType.ALL)
@JoinTable(
    name = "order_product",
    joinColumns = @JoinColumn(name = "order_id"),
    inverseJoinColumns =
        @JoinColumn(name = "product_id")
)
private List<ProductDetails> productDetails;
```

---

# 🔥 Inverse Side

---

## 💻 Product Entity

```java
@ManyToMany(mappedBy = "productDetails")
@JsonIgnore
private List<OrderDetails> orders =
        new ArrayList<>();
```

---

# 🔥 Why @JsonIgnore?

---

## 🧠 Prevents

```
Infinite recursion during serialization
```

---

# 🔥 Full Association Flow

---

```text
OneToMany:
Parent → Multiple Children

ManyToOne:
Multiple Children → One Parent

ManyToMany:
Multiple ↔ Multiple
(join table required)
```

---

# 🎯 Interview Questions

---

## 🔹 Default fetch type for OneToMany?

```text
LAZY
```

---

## 🔹 Why does JPA create join table?

Because one parent cannot store multiple FK values.

---

## 🔹 How to avoid extra mapping table?

```java
@JoinColumn
```

---

## 🔹 What is orphanRemoval?

Deletes child when removed from parent collection.

---

## 🔹 Difference:
mappedBy vs JoinColumn?

| mappedBy | JoinColumn |
|---|---|
| Inverse side | Owning side |
| No FK | Creates FK |

---

## 🔹 Why use managed entities in ManyToMany?

Avoid detached entity exception.

---

## 🔹 Why @JsonIgnore used?

Prevent infinite recursion.

---

# 🚀 Final Summary

---

```text
OneToMany:
- Default LAZY
- Creates join table by default
- @JoinColumn avoids extra table

Bidirectional:
- mappedBy = inverse side
- JoinColumn = owning side

OrphanRemoval:
- true → delete child
- false → child remains

ManyToMany:
- Join table mandatory
- Both entities can own relation
```