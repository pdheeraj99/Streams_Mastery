# Problem 6: Thinking Process

## 1️⃣ Don't Panic! Let's Understand First

### What is the INPUT?

- A `List<Product>` where Product has `name` and `price`
- Can have duplicate names with different prices
- Order is random

### What is the OUTPUT?

- A `List<Product>` that is:
  - Sorted by price (descending)
  - No duplicate names
  - Keeps highest priced version of duplicates

### What is the CORE action?

- **Sorting** (by price, descending)
- **Removing duplicates** (by name, keeping highest price)

### 🔴 KEY INSIGHT

Order of operations matters!

- If I remove duplicates first, which one gets removed?
- If I sort first, then remove, I can control which stays!

---

## 2️⃣ Brainstorming Approaches

### Approach A: Sort First → Then Use distinct()

```java
products.stream()
        .sorted(byPriceDescending)
        .distinct()  // Will this work?
        .collect(toList());
```

**🤔 Wait... `distinct()` uses `equals()`!**

- By default, distinct() checks if objects are equal
- Two Products with same name but different price are NOT equal by default
- Won't work unless we override equals() to compare only by name

**Verdict:** ❌ Not clean. Requires modifying Product class.

---

### Approach B: Sort First → Collect to Map (keep first)

```java
products.stream()
        .sorted(byPriceDescending)           // Highest price first
        .collect(toMap(
            Product::getName,                 // Key: product name
            Function.identity(),              // Value: the product
            (first, second) -> first          // Keep first (highest price)
        ))
        .values();
```

**How it works:**

1. Sort descending → [Phone-60k, Phone-50k, ...]
2. Collect to Map → first Phone (60k) goes in
3. Second Phone (50k) is duplicate key → merge function keeps first
4. Get values → unique products with highest prices!

**Verdict:** ✅ Clean! No class modification needed.

---

### Approach C: Group by Name → Pick Max from Each Group

```java
products.stream()
        .collect(groupingBy(
            Product::getName,
            maxBy(comparing(Product::getPrice))
        ))
        .values()
        .stream()
        .map(Optional::get)
        .sorted(byPriceDescending)
        .collect(toList());
```

**How it works:**

1. Group all products by name
2. From each group, get the max price one
3. Unwrap from Optional
4. Sort the final list

**Verdict:** ⚠️ Works but verbose. Two passes needed.

---

### Approach D: TreeMap with Custom Key

```java
products.stream()
        .collect(toMap(
            Product::getName,
            Function.identity(),
            (p1, p2) -> p1.getPrice() > p2.getPrice() ? p1 : p2,  // Keep higher
            LinkedHashMap::new                                      // Maintain order
        ))
        .values();
```

**How it works:**

1. Collect to Map
2. Merge function explicitly compares prices
3. No pre-sorting needed

**Verdict:** ✅ Works! But still need to sort result.

---

## 3️⃣ Trade-offs Comparison

| Approach | Pros | Cons | Complexity |
|----------|------|------|------------|
| **A: distinct()** | Simple syntax | Requires equals() override | Low but invasive |
| **B: Sort + toMap** | Clean, no class changes | Order lost in HashMap | Medium |
| **C: groupingBy + maxBy** | Clear intent | Verbose, two stream passes | High |
| **D: toMap with merge** | Explicit comparison | Need separate sort | Medium |

---

## 4️⃣ My Decision: Approach B with LinkedHashMap

**Why?**

1. ✅ No modification to Product class
2. ✅ Clean, readable pipeline
3. ✅ Single pass (with sort + collect)
4. ✅ Using LinkedHashMap preserves insertion order after sort

**Final approach:**

```java
products.stream()
        .sorted(Comparator.comparing(Product::getPrice).reversed())
        .collect(Collectors.toMap(
            Product::getName,
            Function.identity(),
            (first, second) -> first,
            LinkedHashMap::new
        ))
        .values()
        .stream()
        .collect(Collectors.toList());
```

---

## 5️⃣ What Concepts Do I Need for This?

To implement this beautifully, I need to understand:

1. **Comparator** - How to create custom sorting logic
2. **sorted()** - How stream sorting works
3. **toMap() with merge function** - Handling duplicates
4. **LinkedHashMap** - Maintaining insertion order

👉 **See `3_Solution.md` for deep dive into these concepts!**
