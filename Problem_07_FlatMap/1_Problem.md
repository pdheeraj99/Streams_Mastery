# Problem 7: Flatten Nested Orders

## 📋 The Interview Question

> "You have a list of Customers. Each customer has a list of Orders.  
> Get a **single flat list** of ALL order IDs across all customers."

---

## 📥 Input

```java
List<Customer> customers = Arrays.asList(
    new Customer("C1", "Ravi", Arrays.asList(
        new Order("ORD-001", 1500),
        new Order("ORD-002", 2500)
    )),
    new Customer("C2", "Priya", Arrays.asList(
        new Order("ORD-003", 800),
        new Order("ORD-004", 3200),
        new Order("ORD-005", 1100)
    )),
    new Customer("C3", "Arjun", Arrays.asList(
        new Order("ORD-006", 4500)
    ))
);
```

**Structure:**

```
Customer 1 (Ravi)
├── Order ORD-001
└── Order ORD-002

Customer 2 (Priya)
├── Order ORD-003
├── Order ORD-004
└── Order ORD-005

Customer 3 (Arjun)
└── Order ORD-006
```

---

## 📤 Expected Output

```java
["ORD-001", "ORD-002", "ORD-003", "ORD-004", "ORD-005", "ORD-006"]
```

**Flat list of ALL order IDs from ALL customers!**

---

## 🎯 Follow-up Questions (Interviewer might ask)

1. "Get total amount of ALL orders across all customers"
2. "Get all orders with amount > 2000"
3. "Get customer name and their order IDs as Map"

---

## ⚠️ Edge Cases

1. What if a customer has no orders (empty list)?
2. What if customers list is empty?
3. What if orders list is null?
