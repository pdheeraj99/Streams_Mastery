# Problem 14: Top 3 Most Expensive Products Per Category

## 📋 The Interview Question (Real-World! 🔥)

> "You have a list of products with name, category, and price.  
> Find the **top 3 most expensive products in EACH category**."

---

## 📥 Input

```java
List<Product> products = Arrays.asList(
    new Product("iPhone", "Electronics", 999),
    new Product("Samsung TV", "Electronics", 1299),
    new Product("MacBook", "Electronics", 1999),
    new Product("Dell Laptop", "Electronics", 899),
    new Product("Sony Headphones", "Electronics", 299),
    
    new Product("Sofa", "Furniture", 599),
    new Product("Dining Table", "Furniture", 799),
    new Product("Office Chair", "Furniture", 349),
    new Product("Bookshelf", "Furniture", 199),
    
    new Product("Nike Shoes", "Fashion", 150),
    new Product("Levi's Jeans", "Fashion", 80),
    new Product("Ray-Ban", "Fashion", 200)
);
```

---

## 📤 Expected Output

```java
{
    "Electronics" → [MacBook-1999, Samsung TV-1299, iPhone-999],
    "Furniture"   → [Dining Table-799, Sofa-599, Office Chair-349],
    "Fashion"     → [Ray-Ban-200, Nike Shoes-150, Levi's-80]
}
```

---

## 🎯 Follow-ups

1. "What if category has less than 3 products?"
2. "Get sum of top 3 prices per category"
3. "Get BOTTOM 3 (cheapest) instead"
4. "Get Nth most expensive overall"

---

## ⚠️ Edge Cases

1. Category with less than 3 products
2. Products with same price
3. Empty product list
