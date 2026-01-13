# Problem 11: Thinking Process

## 1️⃣ Don't Panic! Understand First

### What is the INPUT?

- `List<T>` with potential duplicate elements

### What is the OUTPUT?

- `List<T>` containing only elements that appeared more than once
- Each duplicate should appear only once in result

### What is the CORE action?

```
[1, 2, 3, 2, 4, 3, 5, 1, 6]
     ↓ Find elements with count > 1
[1, 2, 3]  ← These appeared twice
```

**Count occurrences, filter count > 1!**

---

## 2️⃣ Brainstorming Approaches

### Approach A: Count all → Filter > 1

```java
numbers.stream()
    .collect(groupingBy(Function.identity(), counting()))  // Count each
    .entrySet().stream()
    .filter(e -> e.getValue() > 1)                         // Keep count > 1
    .map(Map.Entry::getKey)                                // Get the element
    .collect(toList());
```

**Verdict:** ✅ Clear logic! Two passes.

---

### Approach B: Set trick - add returns false if exists

```java
Set<Integer> seen = new HashSet<>();
numbers.stream()
    .filter(n -> !seen.add(n))  // add returns false if already exists!
    .collect(toSet());          // Use Set to avoid duplicate duplicates
```

**How it works:**

```
seen.add(1) → true  (added)     → filtered out
seen.add(2) → true  (added)     → filtered out
seen.add(3) → true  (added)     → filtered out
seen.add(2) → FALSE (exists!)   → KEPT! ← Duplicate found!
seen.add(3) → FALSE (exists!)   → KEPT! ← Duplicate found!
```

**Verdict:** ✅ Clever! Single pass! But uses external state.

---

### Approach C: Collectors.filtering (Java 9+)

```java
numbers.stream()
    .collect(groupingBy(
        Function.identity(),
        filtering(x -> true, counting())
    ))
    .entrySet().stream()
    .filter(e -> e.getValue() > 1)
    .map(Map.Entry::getKey)
    .collect(toList());
```

**Verdict:** ⚠️ Same as A, more verbose.

---

### Approach D: Traditional with Map

```java
Map<Integer, Integer> countMap = new HashMap<>();
for (int n : numbers) {
    countMap.merge(n, 1, Integer::sum);
}
// Then filter entries with value > 1
```

**Verdict:** ✅ Works, imperative style.

---

## 3️⃣ Trade-offs

| Approach | Passes | State | Readability | Interview? |
|----------|--------|-------|-------------|------------|
| A: groupingBy + filter | 2 | None | High ⭐ | Best |
| B: Set.add trick | 1 | External | Medium | Clever |
| C: Collectors.filtering | 2 | None | Medium | Ok |
| D: Traditional | 2 | Map | High | Fallback |

---

## 4️⃣ My Decision: Approach A (Primary) + B (For bonus points)

**Approach A for interview:**

- Easy to explain step by step
- No external state (pure functional)
- Shows groupingBy + counting knowledge

**Approach B for bonus:**

- Shows clever thinking
- Single pass efficiency
- Interviewer might appreciate the trick

---

## 5️⃣ Key Concepts

1. **groupingBy + counting** - Count occurrences
2. **Function.identity()** - Group by the element itself
3. **Set.add() trick** - Returns false if already exists
4. **entrySet().stream()** - Stream over map entries

👉 **See 3_Solution.md for details!**
