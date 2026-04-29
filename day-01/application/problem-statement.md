# 🧩 Day 1 — Factory Method Pattern (Mini Application)

## 📌 Problem Statement

Design and implement a **Payment Processing System** using **Spring Boot** that supports multiple payment methods.

The system should process payments dynamically based on the payment type provided by the user.

---

## 🏗️ Functional Requirements

### 1. API Endpoint

Create a REST endpoint:

```http
POST /api/payments/pay
```

---

### 2. Request Format

```json
{
  "paymentType": "UPI",
  "amount": 1500
}
```

---

### 3. Response Format

```json
{
  "message": "Payment of ₹1500 processed using UPI"
}
```

---

### 4. Supported Payment Types

* UPI
* CREDIT_CARD
* PAYPAL

---

### 5. Behavior

* Based on the `paymentType`, the system should:

  * Select the appropriate payment service
  * Process the payment
  * Return a success message

---

### 6. Extensibility Requirement

* The system should allow adding new payment methods (e.g., NET_BANKING) without modifying existing logic significantly.

---
