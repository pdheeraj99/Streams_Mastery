# Problem 18: Thinking Process

## 1️⃣ Understanding Multi-level Grouping

```
SINGLE LEVEL:
Employees → groupBy(dept) → {IT: [...], HR: [...]}

TWO LEVELS:
Employees → groupBy(dept) 
         → groupBy(location) within each dept
         → {IT: {Bangalore: [...], Mumbai: [...]}}

THREE LEVELS:
Employees → groupBy(dept)
         → groupBy(location)
         → groupBy(level)
         → {IT: {Bangalore: {Senior: [...], Junior: [...]}}}
```

---

## 2️⃣ The Pattern: Nested groupingBy

```java
groupingBy(
    level1Classifier,
    groupingBy(
        level2Classifier,
        groupingBy(
            level3Classifier,
            downstream
        )
    )
)
```

**Each level's downstream is another groupingBy!**

---

## 3️⃣ Brainstorming Approaches

### Approach A: Nested groupingBy (Recommended)

```java
employees.stream()
    .collect(groupingBy(
        Employee::getDepartment,
        groupingBy(
            Employee::getLocation,
            groupingBy(Employee::getLevel)
        )
    ));
```

**Verdict:** ✅ Clean, readable, type-safe!

---

### Approach B: Composite Key (Flatten hierarchy)

```java
employees.stream()
    .collect(groupingBy(
        e -> e.getDepartment() + "-" + e.getLocation()
    ));
// Result: {"IT-Bangalore": [...], "IT-Mumbai": [...]}
```

**Verdict:** ⚠️ Loses hierarchy, but simpler when only 1 Map needed.

---

### Approach C: Record as Key (Java 14+)

```java
record DeptLocation(String dept, String location) {}

employees.stream()
    .collect(groupingBy(
        e -> new DeptLocation(e.getDepartment(), e.getLocation())
    ));
```

**Verdict:** ✅ Type-safe composite key!

---

## 4️⃣ Trade-offs

| Approach | Levels | Type Safety | Readability |
|----------|--------|-------------|-------------|
| A: Nested groupingBy | Many | High ⭐ | Medium |
| B: String composite | 1 | Low | High |
| C: Record composite | 1 | High ⭐ | High ⭐ |

---

## 5️⃣ When to Use What

| Use Case | Best Approach |
|----------|---------------|
| True hierarchy (drill-down) | Nested groupingBy |
| Flat grouping by combo | Composite key |
| Need to iterate over groups | Nested (easier navigation) |
| Simple count by combo | Composite key |

👉 **See 3_Solution.md for all implementations!**
</Parameter>
<parameter name="Complexity">3
