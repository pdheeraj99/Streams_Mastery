# Problem 6: Sort Products and Remove Duplicates

## 📋 The Interview Question (As Interviewer Asks)

> "You have a list of products. Some products have the same name but different prices (like 'Phone' appearing twice with different prices).  
>
> Your task:
>
> 1. Sort products by price in **descending order** (highest first)
> 2. Remove duplicate product names, but **keep the one with highest price**
>
> Return the final list."

---

## 📥 Input

```java
List<Product> products = Arrays.asList(
    new Product("Phone", 50000),
    new Product("Laptop", 75000),
    new Product("Phone", 60000),    // Duplicate name, higher price
    new Product("Mouse", 500),
    new Product("Laptop", 80000),   // Duplicate name, higher price
    new Product("Keyboard", 2000)
);
```

---

## 📤 Expected Output

```java
[
    Laptop - 80000,    // Highest price Laptop kept
    Phone - 60000,     // Highest price Phone kept
    Keyboard - 2000,
    Mouse - 500
]
```

---

## ⚠️ Edge Cases to Consider

1. What if list is empty?
2. What if all products have same name?
3. What if all products have same price?
4. What about null values?
