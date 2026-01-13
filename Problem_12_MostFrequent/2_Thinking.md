# Problem 12: Thinking Process

## 1️⃣ Understand the Problem

### What is INPUT?

- `List<T>` with elements

### What is OUTPUT?

- Single element that appears most often
- If tie, any one of them

### Core Action

```
[1, 3, 2, 3, 3, 2, 1, 3]
     ↓ Count each
{1→2, 2→2, 3→4}
     ↓ Find max count
3 (count = 4)
```

**Count → Find Max!**

---

## 2️⃣ Brainstorming Approaches

### Approach A: groupingBy + max on entry set

```java
numbers.stream()
    .collect(groupingBy(identity(), counting()))
    .entrySet().stream()
    .max(Map.Entry.comparingByValue())
    .map(Map.Entry::getKey)
    .orElse(null);
```

**Verdict:** ✅ Clear! Two passes.

---

### Approach B: reduce to find max entry

```java
numbers.stream()
    .collect(groupingBy(identity(), counting()))
    .entrySet().stream()
    .reduce((e1, e2) -> e1.getValue() > e2.getValue() ? e1 : e2)
    .map(Map.Entry::getKey);
```

**Verdict:** ✅ Works! Less readable.

---

### Approach C: Collections.max with custom comparator

```java
Map<Integer, Long> counts = numbers.stream()
    .collect(groupingBy(identity(), counting()));
    
Collections.max(counts.entrySet(), Map.Entry.comparingByValue())
           .getKey();
```

**Verdict:** ✅ Classic approach!

---

## 3️⃣ Trade-offs

| Approach | Readability | Stream-only? |
|----------|-------------|--------------|
| A: max() on entries | High ⭐ | Yes |
| B: reduce() | Medium | Yes |
| C: Collections.max | High | No (hybrid) |

---

## 4️⃣ My Decision: Approach A

**Why?**

1. ✅ Pure stream approach
2. ✅ Uses `max()` - clear intent
3. ✅ `Map.Entry.comparingByValue()` - elegant!
4. ✅ Returns Optional - safe!

---

## 5️⃣ Key Concepts

1. **groupingBy + counting** - Count occurrences
2. **max()** - Find maximum in stream
3. **Map.Entry.comparingByValue()** - Compare by map values
4. **reduce()** - Alternative for finding max

👉 **See 3_Solution.md for details!**
