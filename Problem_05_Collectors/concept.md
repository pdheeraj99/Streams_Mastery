# Problem 5: Collect Orders in Different Ways

## 📋 Problem Statement

You have a list of orders. Perform these operations:

1. Collect all **unique product names** into a **Set**
2. Collect all **order IDs** into a **List**
3. Create a **Map** of orderId → Order object
4. Join all product names into a **comma-separated String**

```
Input:  List of Order objects
        ORD-001 - Laptop    - 75000
        ORD-002 - Mouse     - 500
        ORD-003 - Laptop    - 75000   ← Duplicate product
        ORD-004 - Keyboard  - 2000

Output: 
        Set: [Laptop, Mouse, Keyboard]           ← No duplicates
        List: [ORD-001, ORD-002, ORD-003, ORD-004]
        Map: {ORD-001=Order(...), ORD-002=Order(...), ...}
        String: "Laptop, Mouse, Laptop, Keyboard"
```

---

## 🎭 Telugu Analogy

You went shopping and got a **big bill** (list of items).

Now you want to:

1. **Unique items** → "What all did I buy?" → Set
2. **All item codes** → For warranty registration → List
3. **Quick lookup** → Find item by code → Map
4. **Send to friend** → "Bought Laptop, Mouse, Keyboard" → String

Different needs, different **collection types**!

---

## 🧠 Core Concept: Collectors

Until now we used:

```java
.collect(Collectors.toList())
```

But **Collectors** has MANY more options!

### Common Collectors

| Collector | Result | Use Case |
|-----------|--------|----------|
| `toList()` | List<T> | Ordered collection with duplicates |
| `toSet()` | Set<T> | Unique elements only |
| `toMap(keyFn, valueFn)` | Map<K,V> | Key-value lookup |
| `joining()` | String | Concatenate strings |
| `groupingBy()` | Map<K, List<V>> | Group by criteria |
| `partitioningBy()` | Map<Boolean, List<T>> | Split into two groups |
| `counting()` | Long | Count elements |
| `summingInt/Double()` | Sum | Sum values |
| `averagingInt/Double()` | Double | Average |

---

## 🔑 Key Collectors Explained

### 1. toList() - You know this

```java
List<String> names = stream.collect(Collectors.toList());
```

### 2. toSet() - Remove duplicates

```java
Set<String> uniqueNames = stream.collect(Collectors.toSet());
```

### 3. toMap() - Create lookup map

```java
// toMap(keyFunction, valueFunction)
Map<String, Order> orderMap = orders.stream()
    .collect(Collectors.toMap(
        Order::getId,      // Key: order ID
        order -> order     // Value: the order itself
    ));

// Or use Function.identity() for "the object itself"
Map<String, Order> orderMap = orders.stream()
    .collect(Collectors.toMap(
        Order::getId,
        Function.identity()
    ));
```

### 4. joining() - Concatenate strings

```java
// Simple join
String result = names.stream()
    .collect(Collectors.joining());  // "LaptopMouseKeyboard"

// With delimiter
String result = names.stream()
    .collect(Collectors.joining(", "));  // "Laptop, Mouse, Keyboard"

// With delimiter, prefix, suffix
String result = names.stream()
    .collect(Collectors.joining(", ", "[", "]"));  // "[Laptop, Mouse, Keyboard]"
```

---

## ⚠️ toMap() Gotcha: Duplicate Keys

**Problem:** What if two orders have same ID?

```java
orders.stream()
    .collect(Collectors.toMap(Order::getId, Order::getProduct));
// BOOM! IllegalStateException: Duplicate key
```

**Solution:** Provide merge function

```java
orders.stream()
    .collect(Collectors.toMap(
        Order::getId,
        Order::getProduct,
        (existing, replacement) -> existing  // Keep first one
    ));
```

---

## 📚 Interview Corner

### Q1: Difference between toList() and toSet()?

**A:**

| toList() | toSet() |
|----------|---------|
| Maintains insertion order | No guaranteed order |
| Allows duplicates | Removes duplicates |
| Returns ArrayList | Returns HashSet |

### Q2: How to collect into a specific List implementation?

**A:** Use `toCollection()`

```java
// ArrayList (default)
.collect(Collectors.toList())

// LinkedList specifically
.collect(Collectors.toCollection(LinkedList::new))

// TreeSet (sorted)
.collect(Collectors.toCollection(TreeSet::new))
```

### Q3: toMap() with duplicate keys - how to handle?

**A:** Provide merge function:

```java
.collect(Collectors.toMap(
    Order::getProduct,           // Key (might have duplicates)
    Order::getPrice,             // Value
    (price1, price2) -> price1   // Merge: keep first
    // Or: (p1, p2) -> p1 + p2   // Merge: sum them
    // Or: Math::max             // Merge: keep max
))
```

### Q4: How to create Map with specific Map implementation?

**A:** Fourth parameter:

```java
.collect(Collectors.toMap(
    Order::getId,
    Function.identity(),
    (o1, o2) -> o1,
    LinkedHashMap::new    // Specific Map type
))
```

### Q5: joining() on non-String elements?

**A:** First map to String!

```java
// Convert to String first
String prices = orders.stream()
    .map(o -> String.valueOf(o.getPrice()))
    .collect(Collectors.joining(", "));
```

### Q6: What does Function.identity() mean?

**A:** Returns the input as-is

```java
Function.identity()  // Same as: x -> x

// These are equivalent:
.toMap(Order::getId, order -> order)
.toMap(Order::getId, Function.identity())
```

---

## 🎯 Key Takeaways

1. `toList()` - ordered, with duplicates
2. `toSet()` - unordered, unique only
3. `toMap(keyFn, valueFn)` - create lookup map
4. `toMap` with duplicate keys → provide merge function!
5. `joining(delimiter)` - concatenate strings
6. `toCollection(Supplier)` - specific collection type
7. `Function.identity()` - returns object itself
8. All collectors are used with `collect()` terminal operation

---

## 🏃 Try It Yourself

1. Collect employee names sorted alphabetically (TreeSet)
2. Create Map of employeeId → salary
3. Join all department names with " | " separator
4. Handle duplicate product names by summing their prices
