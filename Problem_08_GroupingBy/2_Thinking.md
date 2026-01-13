# Problem 8: Thinking Process

## 1️⃣ Don't Panic! Let's Understand

### What is the INPUT?

- `List<Employee>` - flat list of employees
- Each employee has a department field

### What is the OUTPUT?

- `Map<String, List<Employee>>`
- Key = Department name
- Value = All employees in that department

### What is the CORE action?

```
BEFORE: [E1-IT, E2-HR, E3-IT, E4-Finance, E5-IT, E6-HR, E7-Finance]
              ↓ GROUP BY DEPARTMENT
AFTER:  {
          IT      → [E1, E3, E5],
          HR      → [E2, E6],
          Finance → [E4, E7]
        }
```

**This is GROUPING!** 📂 Category wise sorting!

---

## 2️⃣ Brainstorming Approaches

### Approach A: Traditional - Manual Map building

```java
Map<String, List<Employee>> result = new HashMap<>();
for (Employee emp : employees) {
    String dept = emp.getDepartment();
    if (!result.containsKey(dept)) {
        result.put(dept, new ArrayList<>());
    }
    result.get(dept).add(emp);
}
```

**Verdict:** ✅ Works, but verbose. 7+ lines!

---

### Approach B: Using computeIfAbsent (Better traditional)

```java
Map<String, List<Employee>> result = new HashMap<>();
for (Employee emp : employees) {
    result.computeIfAbsent(emp.getDepartment(), k -> new ArrayList<>())
          .add(emp);
}
```

**Verdict:** ✅ Cleaner! Still imperative style.

---

### Approach C: Stream with Collectors.groupingBy() 🎯

```java
Map<String, List<Employee>> result = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDepartment));
```

**ONE LINE!** 🤯

**Verdict:** ✅ Perfect! Declarative, readable, concise.

---

### Approach D: groupingBy with downstream collector

```java
// Group and get names only
Map<String, List<String>> namesByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.mapping(Employee::getName, Collectors.toList())
    ));

// Group and count
Map<String, Long> countByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.counting()
    ));
```

**Verdict:** ✅ Powerful for follow-up questions!

---

## 3️⃣ Trade-offs Comparison

| Approach | Lines | Flexibility | When to Use |
|----------|-------|-------------|-------------|
| A: Manual loop | 7+ | Low | Legacy, no streams |
| B: computeIfAbsent | 4 | Medium | Non-stream preference |
| C: Simple groupingBy | 1-2 | Medium | Basic grouping ⭐ |
| D: groupingBy + downstream | 3-4 | HIGH | Complex requirements |

---

## 4️⃣ My Decision: Approach C (with D for follow-ups)

**Why?**

1. ✅ ONE line for basic grouping
2. ✅ Downstream collectors for complex needs
3. ✅ Very readable: "group by department"
4. ✅ Handles empty groups naturally

---

## 5️⃣ Concepts I Need to Master

1. **Collectors.groupingBy()** - Basic and overloaded versions
2. **Downstream collectors** - What happens AFTER grouping
3. **Common downstream collectors** - counting, mapping, summingDouble
4. **Nested grouping** - groupingBy inside groupingBy

👉 **See 3_Solution.md for deep dive!**
