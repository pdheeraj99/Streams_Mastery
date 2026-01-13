# Problem 8: Solution Deep Dive

## 📚 Core Concept: Collectors.groupingBy()

### The Categories Analogy 📂

```
Imagine sorting fruits into baskets by color:

🍎🍊🍋🍎🍊🍎🍋  →  🧺 Red: [🍎🍎🍎]
                    🧺 Orange: [🍊🍊]
                    🧺 Yellow: [🍋🍋]

That's groupingBy() - categorize by a classifier!
```

---

## 🔑 Three Versions of groupingBy()

### Version 1: Simple (classifier only)

```java
groupingBy(Function<T, K> classifier)
```

```java
Map<String, List<Employee>> byDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDepartment));
```

**Result:** `Map<K, List<T>>` - Groups elements into Lists

---

### Version 2: With downstream collector

```java
groupingBy(Function<T, K> classifier, Collector<T, ?, D> downstream)
```

```java
// Group and COUNT
Map<String, Long> countByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,      // Classifier
        Collectors.counting()          // What to do with each group
    ));
```

**Result:** Downstream collector determines the value type!

---

### Version 3: With Map factory + downstream

```java
groupingBy(Function classifier, Supplier<M> mapFactory, Collector downstream)
```

```java
// Ordered map (LinkedHashMap) with counting
Map<String, Long> orderedCountByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        LinkedHashMap::new,            // Use LinkedHashMap
        Collectors.counting()
    ));
```

**Result:** Control the Map implementation!

---

## 🎯 Common Downstream Collectors

| Downstream | Result | Use Case |
|------------|--------|----------|
| `toList()` | List<T> | Default grouping |
| `toSet()` | Set<T> | Unique elements per group |
| `counting()` | Long | Count per group |
| `summingInt/Double()` | Sum | Total per group |
| `averagingInt/Double()` | Double | Average per group |
| `maxBy()/minBy()` | Optional<T> | Max/min per group |
| `mapping(fn, downstream)` | Transformed | Transform then collect |
| `collectingAndThen()` | Modified result | Post-process |
| `reducing()` | Custom | Custom reduction |

---

## 📊 Visual: How groupingBy Works

```
Input Stream: [E1-IT, E2-HR, E3-IT, E4-Finance, E5-IT, E6-HR]
                 │
    ┌────────────┼────────────┐
    │      groupBy(dept)      │
    └────────────┼────────────┘
                 │
         ┌───────┴───────┐
         ▼               ▼
     Classify each element by department
         │               │
    ┌────┴────┐    ┌────┴────┐
    │ IT: [E1,E3,E5] │ HR: [E2,E6] │ Finance: [E4] │
    └─────────┘    └─────────┘    └──────────────┘
                 │
                 ▼
    Map<String, List<Employee>>
```

---

## 🎨 Common Patterns

### Pattern 1: Group and get specific field

```java
// Department → List of Names (not full Employee)
Map<String, List<String>> namesByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.mapping(Employee::getName, Collectors.toList())
    ));
```

### Pattern 2: Group and count

```java
Map<String, Long> countByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.counting()
    ));
```

### Pattern 3: Group and sum

```java
Map<String, Double> salaryByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.summingDouble(Employee::getSalary)
    ));
```

### Pattern 4: Group and find max

```java
Map<String, Optional<Employee>> highestPaidByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.maxBy(Comparator.comparing(Employee::getSalary))
    ));
```

### Pattern 5: Nested grouping

```java
// Group by department, then by salary range
Map<String, Map<String, List<Employee>>> nested = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.groupingBy(e -> e.getSalary() > 50000 ? "High" : "Low")
    ));
```

### Pattern 6: Filter before grouping

```java
// Only group employees with salary > 50000
Map<String, List<Employee>> highPaidByDept = employees.stream()
    .filter(e -> e.getSalary() > 50000)
    .collect(Collectors.groupingBy(Employee::getDepartment));
```

---

## 📚 Interview Q&A

### Q1: Difference between groupingBy() and partitioningBy()?

**A:**

```
groupingBy()     → N groups (by any classifier)
                   Map<K, List<T>>
                   
partitioningBy() → EXACTLY 2 groups (true/false)
                   Map<Boolean, List<T>>
```

### Q2: How to handle null group keys?

**A:** groupingBy throws NullPointerException for null keys!

```java
// Safe version
.filter(e -> e.getDepartment() != null)
.collect(groupingBy(Employee::getDepartment))

// Or assign default
.collect(groupingBy(e -> 
    e.getDepartment() == null ? "Unknown" : e.getDepartment()
))
```

### Q3: How to get ordered results?

**A:** Use LinkedHashMap:

```java
.collect(groupingBy(
    Employee::getDepartment,
    LinkedHashMap::new,
    toList()
))
```

### Q4: Can downstream collector be another groupingBy?

**A:** YES! Nested grouping:

```java
// Department → SalaryRange → Employees
.collect(groupingBy(
    Employee::getDepartment,
    groupingBy(e -> e.getSalary() > 50000 ? "High" : "Low")
))
```

### Q5: How to group and join names as String?

**A:**

```java
Map<String, String> namesJoined = employees.stream()
    .collect(groupingBy(
        Employee::getDepartment,
        Collectors.mapping(
            Employee::getName,
            Collectors.joining(", ")
        )
    ));
// Result: {"IT" → "Ravi, Arjun, Kiran", ...}
```

---

## 🎯 Key Takeaways

1. **groupingBy()** = Category-wise grouping
2. **Simple version** returns `Map<K, List<T>>`
3. **Downstream collector** controls what happens in each group
4. **Common downstreams:** counting, summingDouble, mapping, maxBy
5. **Null keys** → Exception! Handle explicitly
6. **Ordered map** → Use LinkedHashMap supplier
7. **Nested grouping** → groupingBy inside groupingBy
