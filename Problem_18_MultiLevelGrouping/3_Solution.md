# Problem 18: Solution Deep Dive

## 📚 Two-Level Grouping

```java
// Department → Location → List<Employee>
Map<String, Map<String, List<Employee>>> twoLevel = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.groupingBy(Employee::getLocation)
    ));
```

**Result:**

```
{
  "IT" → {
    "Bangalore" → [Ram, Arjun, Raj],
    "Mumbai" → [Kiran]
  },
  "HR" → {...}
}
```

---

## 📚 Three-Level Grouping

```java
// Department → Location → Level → List<Employee>
Map<String, Map<String, Map<String, List<Employee>>>> threeLevel = 
    employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.groupingBy(
            Employee::getLocation,
            Collectors.groupingBy(Employee::getLevel)
        )
    ));
```

**Result:**

```
{
  "IT" → {
    "Bangalore" → {
      "Senior" → [Ram],
      "Junior" → [Arjun, Raj]
    },
    "Mumbai" → {
      "Senior" → [Kiran]
    }
  }
}
```

---

## 📚 Multi-Level with Counting

```java
// Count at each combination
Map<String, Map<String, Long>> countByDeptAndLocation = 
    employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.groupingBy(
            Employee::getLocation,
            Collectors.counting()  // Count instead of list!
        )
    ));

// Result: {"IT" → {"Bangalore" → 3, "Mumbai" → 1}, ...}
```

---

## 📚 Composite Key Approach

### Using String Concatenation

```java
Map<String, List<Employee>> byComposite = employees.stream()
    .collect(Collectors.groupingBy(
        e -> e.getDepartment() + "|" + e.getLocation()
    ));
// {"IT|Bangalore" → [...], "IT|Mumbai" → [...]}
```

### Using Record (Java 14+)

```java
record DeptLocation(String dept, String location) {}

Map<DeptLocation, List<Employee>> byRecord = employees.stream()
    .collect(Collectors.groupingBy(
        e -> new DeptLocation(e.getDepartment(), e.getLocation())
    ));
// {DeptLocation[IT, Bangalore] → [...]}
```

### Using List as Key

```java
Map<List<String>, List<Employee>> byListKey = employees.stream()
    .collect(Collectors.groupingBy(
        e -> Arrays.asList(e.getDepartment(), e.getLocation())
    ));
// {[IT, Bangalore] → [...]}
```

---

## 📚 Multi-Level with Aggregation

### Sum Salaries at Each Level

```java
Map<String, Map<String, Double>> salaryByDeptLocation = 
    employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.groupingBy(
            Employee::getLocation,
            Collectors.summingDouble(Employee::getSalary)
        )
    ));
// {"IT" → {"Bangalore" → 150000, "Mumbai" → 65000}}
```

### Names at Each Level

```java
Map<String, Map<String, List<String>>> namesByDeptLocation = 
    employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.groupingBy(
            Employee::getLocation,
            Collectors.mapping(Employee::getName, Collectors.toList())
        )
    ));
// {"IT" → {"Bangalore" → ["Ram", "Arjun", "Raj"]}}
```

---

## 📊 Navigating Multi-Level Map

```java
// Get employees in IT, Bangalore, Senior
threeLevel.get("IT")
          .get("Bangalore")
          .get("Senior")
          .forEach(System.out::println);

// Safe navigation with getOrDefault
threeLevel.getOrDefault("IT", Map.of())
          .getOrDefault("Delhi", Map.of())
          .getOrDefault("Senior", List.of());
```

---

## 📚 Interview Q&A

### Q1: How deep can nesting go?

**A:** Technically unlimited, but 3+ levels become hard to read. Consider:

- Composite key for flat structure
- Custom class to hold grouped data

### Q2: Type declaration is long. How to handle?

**A:**

```java
// Use var (Java 10+)
var result = employees.stream()
    .collect(groupingBy(...));

// Or type alias with method
private Map<String, Map<String, List<Employee>>> groupResult() {...}
```

### Q3: Performance of nested groupingBy?

**A:** O(n) - still single pass! Each element is placed in the right bucket at all levels.

### Q4: What if some combinations have no employees?

**A:** Those keys won't exist in the map. Use `getOrDefault()` for safe access.

---

## 🎯 Key Takeaways

1. **Nested groupingBy** = Multi-level hierarchy
2. **Each level's downstream** can be another groupingBy
3. **Innermost downstream** determines final collection (list, count, sum)
4. **Composite key** alternative when hierarchy not needed
5. **var** helps with verbose type declarations
6. **Safe navigation** with getOrDefault()
