# Problem 1: Find Even Numbers

## 📋 Problem Statement

Given a list of integers, find all **EVEN numbers** from the list.

```
Input:  [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
Output: [2, 4, 6, 8, 10]
```

---

## 🎭 Telugu Analogy

Imagine you're at a **wedding buffet** with many dishes (our list).  
You only want **vegetarian items** (our filter condition).

| Approach | How it works |
|----------|--------------|
| Traditional | Walk through EACH dish, check if veg, pick it up, put in plate |
| Stream | "Give me all veg dishes" - one clear instruction! |

---

## 🧠 Core Concept: Filtering

When we **filter**, we're asking: *"Should this element stay or go?"*

```
┌─────────────────────────────────────────────────────┐
│  Input Stream:    [1] [2] [3] [4] [5] [6]          │
│                     │   │   │   │   │   │          │
│  Condition: n%2==0  │   │   │   │   │   │          │
│                     ✗   ✓   ✗   ✓   ✗   ✓          │
│                         │       │       │          │
│  Output Stream:        [2]     [4]     [6]         │
└─────────────────────────────────────────────────────┘

✗ = Does NOT satisfy condition (removed)
✓ = Satisfies condition (kept)
```

### Key Points:
- Takes a **Predicate** (condition that returns true/false)
- **Keeps** elements where condition is TRUE
- **Removes** elements where condition is FALSE
- Input count >= Output count (can only reduce or stay same)

---

## 🔑 The Stream Operation: filter()

```java
.filter(Predicate<T>)
```

| Term | Meaning |
|------|---------|
| Predicate | A function that returns boolean (true/false) |
| Intermediate | Returns a Stream, can be chained |
| Lazy | Doesn't execute until terminal operation |
| Non-mutating | Original collection unchanged |

### Lambda Breakdown:
```java
n -> n % 2 == 0
│         │
│         └── Returns: true if even, false if odd
└── Input: each number from stream
```

---

## 📚 Interview Corner

### Q1: What is the difference between filter() and map()?
**A:** 
- `filter()` → SELECTS elements (keeps or removes based on condition)  
  Returns: Same type, possibly fewer elements
- `map()` → TRANSFORMS elements (changes each element)  
  Returns: Can be different type, same number of elements

### Q2: Is filter() a terminal or intermediate operation?
**A:** INTERMEDIATE operation!
- Returns a Stream (not final result)
- Lazy evaluation (doesn't execute until terminal operation)
- Can be chained with other operations

### Q3: What happens if filter condition is always false?
**A:** You get an **EMPTY** stream/collection (not null, not error)
```java
List<Integer> result = numbers.stream()
                              .filter(n -> n > 1000)  // none match
                              .collect(Collectors.toList());
// result = [] (empty list, NOT null)
```

### Q4: Can filter() modify the original list?
**A:** NO! Streams are **NON-MUTATING**.
- Original collection remains unchanged
- filter() creates a NEW stream with filtered elements

### Q5: Single filter with && OR chained filters?
**A:** Both work, trade-offs:

| Approach | Pros | Cons |
|----------|------|------|
| `.filter(n -> n % 2 == 0 && n > 5)` | Slightly more efficient | Can become hard to read |
| `.filter(n -> n % 2 == 0).filter(n -> n > 5)` | More readable | Slightly more overhead |

👉 Prefer **READABILITY** unless performance is critical!

---

## 🎯 Key Takeaways

1. `filter()` takes a Predicate (condition that returns boolean)
2. `filter()` is an INTERMEDIATE operation (returns Stream)
3. `filter()` is LAZY - doesn't execute until terminal operation
4. `filter()` is NON-MUTATING - original collection unchanged
5. Empty result = empty collection (not null)
6. Can chain multiple filters OR use && in single filter
7. Method references can replace lambdas when applicable

---

## 🏃 Try It Yourself

1. Filter strings that start with "A"
2. Filter employees with salary > 50000
3. Filter products that are in stock AND price < 100
