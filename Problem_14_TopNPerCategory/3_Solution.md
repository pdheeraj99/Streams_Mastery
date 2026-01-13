# Problem 14: Solution Deep Dive

## 📚 Pattern: Group → Sort → Limit

```
STEP 1: Group by category
        Electronics → [all electronics]
        Furniture   → [all furniture]
        
STEP 2: For each group:
        Sort by price descending
        Take first 3 (limit)
```

---

## 🎯 Solution: collectingAndThen

```java
public Map<String, List<Product>> topNPerCategory(List<Product> products, int n) {
    return products.stream()
        .collect(Collectors.groupingBy(
            Product::getCategory,
            Collectors.collectingAndThen(
                Collectors.toList(),
                list -> list.stream()
                    .sorted(Comparator.comparing(Product::getPrice).reversed())
                    .limit(n)
                    .collect(Collectors.toList())
            )
        ));
}
```

---

## 🔑 Key: collectingAndThen

```java
collectingAndThen(downstream, finisher)
```

| Part | What it does |
|------|--------------|
| `downstream` | Collect elements first (toList()) |
| `finisher` | Transform the result (sort + limit) |

**Think:** "Collect, THEN transform"

---

## 📊 Visual Flow

```
Input: [iPhone-Elec-999, TV-Elec-1299, MacBook-Elec-1999, ...]
                            │
            groupingBy(category)
                            │
         ┌──────────────────┼──────────────────┐
         ▼                  ▼                  ▼
    Electronics         Furniture           Fashion
    [iPhone-999,        [Sofa-599,          [Nike-150,
     TV-1299,            Table-799,          Jeans-80,
     MacBook-1999,       Chair-349,          RayBan-200]
     Dell-899,           Shelf-199]
     Sony-299]
         │                  │                  │
    collectingAndThen(toList(), sort+limit(3))
         │                  │                  │
         ▼                  ▼                  ▼
    [MacBook-1999,      [Table-799,         [RayBan-200,
     TV-1299,            Sofa-599,           Nike-150,
     iPhone-999]         Chair-349]          Jeans-80]
```

---

## 🎨 Variations

### Variation 1: Bottom N (Cheapest)

```java
// Just remove .reversed()!
.sorted(Comparator.comparing(Product::getPrice))  // Ascending
.limit(n)
```

### Variation 2: Top N Overall (Not per category)

```java
public List<Product> topNOverall(List<Product> products, int n) {
    return products.stream()
        .sorted(Comparator.comparing(Product::getPrice).reversed())
        .limit(n)
        .collect(Collectors.toList());
}
```

### Variation 3: Sum of Top 3 per Category

```java
Map<String, Double> sumTop3ByCategory = products.stream()
    .collect(Collectors.groupingBy(
        Product::getCategory,
        Collectors.collectingAndThen(
            Collectors.toList(),
            list -> list.stream()
                .sorted(Comparator.comparing(Product::getPrice).reversed())
                .limit(3)
                .mapToDouble(Product::getPrice)
                .sum()
        )
    ));
```

### Variation 4: Top N with Ties Handling

```java
// Include all products with same price as Nth
// More complex - requires custom logic
```

---

## 📚 Interview Q&A

### Q1: Why collectingAndThen instead of separate stream?

**A:**

- Keeps all logic in single collect
- More declarative
- Shows advanced Collectors knowledge

### Q2: What if category has < 3 products?

**A:** Works fine! `limit(3)` on 2 items returns 2 items.

### Q3: Can we make N configurable?

**A:** Yes! Pass `n` as parameter:

```java
.limit(n)  // Use the parameter
```

### Q4: How to maintain insertion order of categories?

**A:** Use LinkedHashMap:

```java
Collectors.groupingBy(
    Product::getCategory,
    LinkedHashMap::new,  // Add this!
    collectingAndThen(...)
)
```

---

## 🎯 Key Takeaways

1. **collectingAndThen** = Collect + Transform
2. **sorted + limit** = Top N pattern
3. **Per-group operations** inside groupingBy downstream
4. **Comparator.reversed()** for descending
5. Pattern works for any "Top N per Category" problem
