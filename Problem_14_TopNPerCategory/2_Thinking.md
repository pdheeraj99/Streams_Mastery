# Problem 14: Thinking Process

## 1️⃣ Break It Down

### What is INPUT?

- `List<Product>` with category and price

### What is OUTPUT?

- `Map<String, List<Product>>`
- Key = Category
- Value = Top 3 products by price (sorted descending)

### Core Action

```
1. GROUP by category
2. Within each group:
   - SORT by price descending
   - LIMIT to 3
```

---

## 2️⃣ Brainstorming Approaches

### Approach A: groupingBy → process values

```java
products.stream()
    .collect(groupingBy(Product::getCategory))
    .entrySet().stream()
    .collect(toMap(
        Map.Entry::getKey,
        e -> e.getValue().stream()
              .sorted(byPriceDesc)
              .limit(3)
              .collect(toList())
    ));
```

**Verdict:** ✅ Clear! Two-stage process.

---

### Approach B: groupingBy with collectingAndThen

```java
products.stream()
    .collect(groupingBy(
        Product::getCategory,
        collectingAndThen(
            toList(),
            list -> list.stream()
                        .sorted(byPriceDesc)
                        .limit(3)
                        .collect(toList())
        )
    ));
```

**Verdict:** ✅ Single collect! More compact.

---

### Approach C: Using maxBy with reduction (Limited)

```java
// Can only get 1 max, not top 3
// Not suitable for this problem
```

**Verdict:** ❌ maxBy gives only 1 element.

---

## 3️⃣ Trade-offs

| Approach | Readability | Compactness |
|----------|-------------|-------------|
| A: Two stages | High ⭐ | Medium |
| B: collectingAndThen | Medium | High ⭐ |

---

## 4️⃣ My Decision: Approach B

**Why?**

1. ✅ Single collect() call
2. ✅ All logic in one pipeline
3. ✅ collectingAndThen is powerful!

---

## 5️⃣ Key Concepts

1. **groupingBy with downstream** - Group then transform
2. **collectingAndThen** - Post-process after collecting
3. **sorted + limit** - Top N pattern
4. **Comparator.reversed()** - Descending order

👉 **See 3_Solution.md for details!**
