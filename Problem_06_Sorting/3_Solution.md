# Problem 6: Solution Deep Dive

## 📚 Concepts We're Using (From Thinking.md Decision)

Based on our analysis, we chose:

```java
Sort by price descending → Collect to Map (keep first) → Get values
```

Let's master each concept:

---

## 1️⃣ Comparator - The Sorting Brain

### What is Comparator?

A function that tells Java **how to compare two objects**.

```java
Comparator<Product> comparator = (p1, p2) -> {
    // Return negative: p1 comes BEFORE p2
    // Return positive: p1 comes AFTER p2
    // Return zero: p1 equals p2
    return p1.getPrice() - p2.getPrice();  // Ascending
};
```

### Modern Way - Comparator.comparing()

```java
// Ascending by price
Comparator.comparing(Product::getPrice)

// Descending by price
Comparator.comparing(Product::getPrice).reversed()

// Multiple criteria: first by dept, then by salary
Comparator.comparing(Employee::getDept)
          .thenComparing(Employee::getSalary)
```

### Comparator Cheat Sheet

| Need | Code |
|------|------|
| Ascending | `Comparator.comparing(T::getField)` |
| Descending | `Comparator.comparing(T::getField).reversed()` |
| Null-safe | `Comparator.nullsFirst(comparing(...))` |
| Multiple fields | `.thenComparing(T::getField2)` |
| Natural order | `Comparator.naturalOrder()` |
| Reverse natural | `Comparator.reverseOrder()` |

---

## 2️⃣ sorted() - Stream Sorting

### Basic Usage

```java
// Natural order (for Comparable types)
numbers.stream().sorted()

// Custom comparator
products.stream().sorted(Comparator.comparing(Product::getPrice))

// Descending
products.stream().sorted(Comparator.comparing(Product::getPrice).reversed())
```

### Key Points About sorted()

| Property | Value |
|----------|-------|
| Type | Intermediate operation |
| Stateful? | YES (needs to see all elements) |
| Lazy? | Yes, but buffers all elements |
| Returns | Stream |

### ⚠️ Performance Warning

`sorted()` is **stateful** - it collects all elements before sorting.

- For infinite streams: ❌ Will hang!
- For huge streams: 💾 High memory usage

---

## 3️⃣ toMap() with Merge Function - Handling Duplicates

### Basic toMap (crashes on duplicates!)

```java
.collect(Collectors.toMap(
    Product::getName,       // Key mapper
    Function.identity()     // Value mapper
))
// ⚠️ IllegalStateException if duplicate keys!
```

### toMap with Merge Function (safe!)

```java
.collect(Collectors.toMap(
    Product::getName,                    // Key mapper
    Function.identity(),                 // Value mapper
    (existing, replacement) -> existing  // Merge function
))
```

### Merge Function Explained

When duplicate key found:

```
existing    = already in map (first one we saw)
replacement = new one trying to enter

Return value = which one stays in map
```

**Common merge strategies:**

```java
// Keep first
(first, second) -> first

// Keep last
(first, second) -> second

// Keep higher value
(first, second) -> first.getPrice() > second.getPrice() ? first : second

// Combine (for numbers)
(first, second) -> first + second
```

---

## 4️⃣ LinkedHashMap - Preserving Order

### HashMap vs LinkedHashMap

| HashMap | LinkedHashMap |
|---------|---------------|
| No order guarantee | Maintains insertion order |
| Faster | Slightly slower |
| Less memory | More memory |

### Why We Need It

```java
// Without LinkedHashMap
products.stream()
    .sorted(byPrice)  // Sorted!
    .collect(toMap(...))  // HashMap - order LOST!

// With LinkedHashMap
products.stream()
    .sorted(byPrice)  // Sorted!
    .collect(toMap(
        ...,
        ...,
        ...,
        LinkedHashMap::new  // Order PRESERVED!
    ))
```

---

## 5️⃣ The Complete Solution

```java
public List<Product> sortAndRemoveDuplicates(List<Product> products) {
    return products.stream()
        // Step 1: Sort by price descending
        .sorted(Comparator.comparing(Product::getPrice).reversed())
        
        // Step 2: Collect to LinkedHashMap (preserve order, keep first of duplicates)
        .collect(Collectors.toMap(
            Product::getName,              // Key: product name
            Function.identity(),           // Value: the product itself
            (first, second) -> first,      // Merge: keep first (highest price)
            LinkedHashMap::new             // Use LinkedHashMap to preserve order
        ))
        
        // Step 3: Get values and convert to List
        .values()
        .stream()
        .collect(Collectors.toList());
}
```

### Step-by-Step Visualization

```
INPUT: [Phone-50k, Laptop-75k, Phone-60k, Mouse-500, Laptop-80k, Keyboard-2k]
           
After sorted() descending:
        [Laptop-80k, Laptop-75k, Phone-60k, Phone-50k, Keyboard-2k, Mouse-500]
           
After toMap() with merge (keep first):
        Map: {Laptop → 80k, Phone → 60k, Keyboard → 2k, Mouse → 500}
        (Laptop-75k merged away, Phone-50k merged away)
           
After values().stream().toList():
        [Laptop-80k, Phone-60k, Keyboard-2k, Mouse-500]
```

---

## 📚 Interview Q&A

### Q1: Why not use distinct()?

**A:** `distinct()` uses `equals()` method. Two Products with same name but different prices are not equal by default. Would need to override `equals()` in Product class, which changes the class contract and may cause issues elsewhere.

### Q2: What if I use HashMap instead of LinkedHashMap?

**A:** HashMap doesn't maintain order. After sorting, the order would be lost. LinkedHashMap preserves insertion order.

### Q3: Time complexity?

**A:**

- sorted(): O(n log n)
- toMap(): O(n)
- Overall: O(n log n)

### Q4: Can we do this without two stream() calls?

**A:** The `.values().stream()` is needed because Map's values() returns Collection, not Stream. This is minimal overhead.

### Q5: What if Product can be null?

**A:** Add null check:

```java
.filter(Objects::nonNull)  // Filter out nulls first
.sorted(...)
```

---

## 🎯 Key Takeaways

1. `Comparator.comparing()` - modern way to create comparators
2. `.reversed()` - flip sort order
3. `sorted()` is stateful intermediate operation
4. `toMap()` merge function handles duplicate keys
5. `LinkedHashMap` preserves insertion order
6. `Function.identity()` = `x -> x`
7. Order of operations matters: sort BEFORE dedup!
