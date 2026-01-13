# Problem 2: Transform Employee Names to Uppercase

## 📋 Problem Statement

You have employee names stored in database (all lowercase).  
The UI design team wants all names displayed in **UPPERCASE**.

```
Input:  ["ram", "sita", "lakshman", "hanuman", "ravana"]
Output: ["RAM", "SITA", "LAKSHMAN", "HANUMAN", "RAVANA"]
```

---

## 🎭 Telugu Analogy

Imagine a **photo studio**.

| Step | What happens |
|------|--------------|
| Input | Customers give you photos → [photo1, photo2, photo3] |
| Process | You apply a filter/edit to each |
| Output | [edited1, edited2, edited3] |

**Same number of photos, but EACH ONE is changed!**

---

## 🧠 Core Concept: Transformation

Compare with Problem 1:

| Problem | Action | Input → Output |
|---------|--------|----------------|
| Problem 1 (Even) | SELECTING which to keep | 10 items → 5 items |
| Problem 2 (This) | CHANGING each element | 5 items → 5 items |

```
┌─────────────────────────────────────────────────────────────┐
│  Input Stream:   ["ram"]  ["sita"]  ["lakshman"]           │
│                      │        │          │                  │
│  Transform:          ▼        ▼          ▼                  │
│                  ["RAM"]  ["SITA"]  ["LAKSHMAN"]            │
│  Output Stream:  ["RAM"]  ["SITA"]  ["LAKSHMAN"]            │
└─────────────────────────────────────────────────────────────┘

Key: Same COUNT, different VALUES
```

---

## 🔑 The Stream Operation: map()

```java
.map(Function<T, R>)
```

| Term | Meaning |
|------|---------|
| Function<T, R> | Takes input of type T, returns output of type R |
| T → R | Can transform to DIFFERENT type (String → Integer) |
| Intermediate | Returns a Stream, can be chained |
| Non-mutating | Original collection unchanged |

### Lambda Breakdown

```java
name -> name.toUpperCase()
│              │
│              └── Returns: transformed value (uppercase string)
└── Input: each name from stream
```

### Key Difference: filter vs map

```java
// filter: Predicate (returns boolean)
.filter(n -> n > 5)     // Keep or remove?

// map: Function (returns new value)
.map(n -> n * 2)        // Transform to what?
```

---

## 📚 Interview Corner

### Q1: What's the difference between map() and filter()?

**A:**

```
filter(Predicate) → SELECTS elements (returns boolean)
                    Input: 10 items → Output: 0 to 10 items

map(Function)     → TRANSFORMS elements (returns new value)
                    Input: 10 items → Output: exactly 10 items
```

### Q2: Can map() return a different type than input?

**A:** YES! This is powerful.

```java
List<String> names = ["Ram", "Sita"];
List<Integer> lengths = names.stream()
                             .map(String::length)  // String → Integer
                             .collect(toList());
// lengths = [3, 4]
```

### Q3: What is a method reference?

**A:** Shorthand for a lambda that calls ONE method.

| Lambda | Method Reference |
|--------|-----------------|
| `s -> s.toUpperCase()` | `String::toUpperCase` |
| `s -> s.length()` | `String::length` |
| `n -> Math.abs(n)` | `Math::abs` |

Types:

- Static: `ClassName::staticMethod` → `Math::abs`
- Instance: `object::method` → `System.out::println`
- Constructor: `ClassName::new` → `ArrayList::new`

### Q4: Does the order of map() and filter() matter?

**A:** Result might be same, but **PERFORMANCE** differs!

```java
// BETTER: filter first, then transform
.filter(name -> name.length() > 4)  // Reduces elements FIRST
.map(String::toUpperCase)           // Transforms FEWER elements

// WORSE: transform first, then filter
.map(String::toUpperCase)           // Transforms ALL elements
.filter(name -> name.length() > 4)  // Then filters
```

👉 **Filter FIRST** to reduce elements before expensive operations!

### Q5: Is map() intermediate or terminal?

**A:** INTERMEDIATE

- Returns a Stream (can be chained)
- Lazy (doesn't execute until terminal operation)

---

## 🎯 Key Takeaways

1. `map()` transforms EACH element using a Function
2. Input count = Output count (unlike filter which can reduce)
3. `map()` can change the TYPE of elements (String → Integer)
4. Use method references for cleaner code when possible
5. Order matters: **filter before map** for performance
6. `map()` is INTERMEDIATE (lazy, returns Stream)
7. `map()` is NON-MUTATING (original unchanged)

---

## 🏃 Try It Yourself

1. Transform list of prices by adding 18% GST
2. Extract first character from each name
3. Convert list of Employee objects to list of their salaries
