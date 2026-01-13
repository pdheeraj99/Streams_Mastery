# Problem 17: Solution Deep Dive

## 📚 All Downstream Collectors

### 1. mapping() - Transform Elements

```java
Collectors.mapping(mapper, downstream)
```

**Purpose:** Apply transformation to each element, then collect.

```java
// Get names grouped by department
Map<String, List<String>> namesByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.mapping(Employee::getName, Collectors.toList())
    ));
// {IT=[Ram, Arjun, Kiran], HR=[Sita, Meera], Finance=[Priya]}
```

---

### 2. filtering() - Filter Within Groups (Java 9+)

```java
Collectors.filtering(predicate, downstream)
```

**Purpose:** Filter elements before collecting in each group.

```java
// Only high-salary employees per department
Map<String, List<Employee>> highPaidByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.filtering(e -> e.getSalary() > 50000, Collectors.toList())
    ));
```

**Important:** Different from `.filter()` before `groupingBy()`:

```java
// filter() before groupingBy: empty departments REMOVED
// filtering() as downstream: empty departments KEPT (empty list)
```

---

### 3. flatMapping() - Flatten + Collect (Java 9+)

```java
Collectors.flatMapping(mapper, downstream)
```

**Purpose:** For nested collections, flatten while collecting.

```java
// If each Employee has List<String> skills
Map<String, Set<String>> skillsByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.flatMapping(
            e -> e.getSkills().stream(),
            Collectors.toSet()
        )
    ));
// {IT=[Java, Python, SQL], HR=[Excel, Communication]}
```

---

### 4. collectingAndThen() - Post-Process Result

```java
Collectors.collectingAndThen(downstream, finisher)
```

**Purpose:** Transform the final collected result.

```java
// Group and make immutable
Map<String, List<Employee>> immutableByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.collectingAndThen(
            Collectors.toList(),
            Collections::unmodifiableList
        )
    ));

// Get exactly first employee per department
Map<String, Employee> firstByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.collectingAndThen(
            Collectors.toList(),
            list -> list.get(0)
        )
    ));
```

---

### 5. Chaining: filtering + mapping

```java
// High-salary employees' names per department
Map<String, List<String>> highPaidNames = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.filtering(
            e -> e.getSalary() > 50000,
            Collectors.mapping(Employee::getName, Collectors.toList())
        )
    ));
// {IT=[Arjun], HR=[Sita, Meera], Finance=[Priya]}
```

---

### 6. counting() - Count Per Group

```java
Map<String, Long> countByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.counting()
    ));
// {IT=3, HR=2, Finance=1}
```

---

### 7. summingDouble() - Sum Per Group

```java
Map<String, Double> salaryByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.summingDouble(Employee::getSalary)
    ));
// {IT=148000, HR=123000, Finance=72000}
```

---

### 8. maxBy()/minBy() - Extreme Per Group

```java
Map<String, Optional<Employee>> highestPaid = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.maxBy(Comparator.comparing(Employee::getSalary))
    ));
// {IT=Optional[Arjun], HR=Optional[Priya], ...}
```

---

### 9. joining() - Concatenate Strings

```java
Map<String, String> namesJoined = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.mapping(
            Employee::getName,
            Collectors.joining(", ")
        )
    ));
// {IT="Ram, Arjun, Kiran", HR="Sita, Meera", Finance="Priya"}
```

---

### 10. reducing() - Custom Aggregation

```java
// Concatenate names with custom logic
Map<String, String> reduced = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.reducing(
            "",
            Employee::getName,
            (s1, s2) -> s1.isEmpty() ? s2 : s1 + " & " + s2
        )
    ));
// {IT="Ram & Arjun & Kiran", ...}
```

---

## 📚 Interview Q&A

### Q1: filtering() vs filter() before groupingBy?

**A:**

```java
// filter() REMOVES empty groups
employees.stream()
    .filter(e -> e.getSalary() > 100000)
    .collect(groupingBy(Employee::getDepartment))
// Departments with no high-salary employees are MISSING

// filtering() KEEPS empty groups
employees.stream()
    .collect(groupingBy(
        Employee::getDepartment,
        filtering(e -> e.getSalary() > 100000, toList())
    ))
// All departments present, some with empty lists
```

### Q2: Can you nest multiple downstream collectors?

**A:** Yes! Chain them:

```java
filtering(..., mapping(..., toList()))
collectingAndThen(maxBy(...), Optional::get)
```

### Q3: When to use reducing() vs summingDouble()?

**A:**

- `summingDouble()` - Simple numeric sum
- `reducing()` - Custom aggregation logic

---

## 🎯 Key Takeaways

1. **mapping()** = Transform, then collect
2. **filtering()** = Filter within groups (keeps empty groups!)
3. **flatMapping()** = Flatten nested, then collect
4. **collectingAndThen()** = Post-process final result
5. **Chain them** for powerful combinations!
6. **downstream** = What happens TO each group
