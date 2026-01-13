# Problem 10: Thinking Process

## 1️⃣ Don't Panic! Break It Down

### What is the INPUT?

- `List<Employee>` with department and salary

### What is the OUTPUT?

- `Map<String, Optional<Employee>>`
- Key = Department
- Value = Second highest paid employee (or empty if < 2 employees)

### What is the CORE action?

```
1. GROUP by department
2. Within each group, SORT by salary descending
3. SKIP first one (highest)
4. GET first of remaining (second highest)
```

**Multiple operations combined!** 🔗

---

## 2️⃣ Brainstorming Approaches

### Approach A: Group → Sort → Skip → FindFirst

```java
employees.stream()
    .collect(groupingBy(Employee::getDepartment))  // Group
    .entrySet().stream()
    .collect(toMap(
        Map.Entry::getKey,
        e -> e.getValue().stream()
              .sorted(comparing(Employee::getSalary).reversed())  // Sort desc
              .skip(1)                                             // Skip highest
              .findFirst()                                         // Get second
    ));
```

**Verdict:** ✅ Works! Clear logic.

---

### Approach B: groupingBy with collectingAndThen

```java
employees.stream()
    .collect(groupingBy(
        Employee::getDepartment,
        collectingAndThen(
            toList(),
            list -> list.stream()
                        .sorted(comparing(Employee::getSalary).reversed())
                        .skip(1)
                        .findFirst()
        )
    ));
```

**Verdict:** ✅ More compact! Single collect.

---

### Approach C: Custom Collector (Advanced)

```java
// Create a collector that keeps top 2 elements
// Complex but reusable for "Nth highest"
```

**Verdict:** ⚠️ Overkill for this problem.

---

### Approach D: Using reduce with pair tracking

```java
// Track highest and second highest in one pass
// More efficient but complex
```

**Verdict:** ⚠️ Complex, hard to read.

---

## 3️⃣ Trade-offs

| Approach | Readability | Efficiency | Reusability |
|----------|-------------|------------|-------------|
| A: Separate operations | High ⭐ | Medium | Medium |
| B: collectingAndThen | Medium | Medium | High ⭐ |
| C: Custom Collector | Low | High | High |
| D: Reduce with pairs | Low | High ⭐ | Low |

---

## 4️⃣ My Decision: Approach A (with B as alternative)

**Why Approach A?**

1. ✅ Each step is clear and understandable
2. ✅ Easy to debug - can see intermediate results
3. ✅ Easy to modify (3rd highest? Just change skip(2))
4. ✅ Interview-friendly - easy to explain

**When to use B?**

- When you want single pipeline
- When you're comfortable with nested lambdas

---

## 5️⃣ Key Operations We're Combining

1. **groupingBy()** - Group employees by department
2. **sorted()** - Sort by salary descending
3. **skip(n)** - Skip first n elements
4. **findFirst()** - Get first of remaining
5. **Optional** - Handle "not found" case

👉 **See 3_Solution.md for the execution flow!**
