# Problem 10: Solution Deep Dive

## 📚 This Problem Tests EVERYTHING

```
┌─────────────────────────────────────────────────┐
│  SKILLS BEING TESTED:                           │
│                                                 │
│  ✅ groupingBy()     - Grouping                 │
│  ✅ sorted()         - Sorting with Comparator  │
│  ✅ skip()           - Skipping elements        │
│  ✅ findFirst()      - Finding element          │
│  ✅ Optional         - Handling absence         │
│  ✅ Chaining         - Combining operations     │
│  ✅ Nested Streams   - Stream inside collector  │
└─────────────────────────────────────────────────┘
```

---

## 🔑 Step-by-Step Execution

### Step 1: Group by Department

```java
.collect(groupingBy(Employee::getDepartment))

Result: Map<String, List<Employee>>
{
  "IT"      → [Ravi-75k, Arjun-85k, Kiran-55k, Amit-80k],
  "HR"      → [Priya-65k, Meera-58k],
  "Finance" → [Sneha-72k, Raj-92k]
}
```

### Step 2: For Each Department, Process the List

```java
.entrySet().stream()  // Stream over map entries
```

### Step 3: Sort by Salary (Descending)

```java
.sorted(Comparator.comparing(Employee::getSalary).reversed())

IT after sort: [Arjun-85k, Amit-80k, Ravi-75k, Kiran-55k]
HR after sort: [Priya-65k, Meera-58k]
```

### Step 4: Skip First (Highest)

```java
.skip(1)

IT after skip: [Amit-80k, Ravi-75k, Kiran-55k]  ← Arjun skipped!
HR after skip: [Meera-58k]                       ← Priya skipped!
```

### Step 5: Get First of Remaining

```java
.findFirst()

IT: Optional[Amit-80k]   ← Second highest!
HR: Optional[Meera-58k]  ← Second highest!
```

---

## 🎨 The Complete Solution

### Approach A: Clear Steps

```java
public Map<String, Optional<Employee>> secondHighestByDept(List<Employee> employees) {
    // Step 1: Group by department
    Map<String, List<Employee>> byDept = employees.stream()
        .collect(Collectors.groupingBy(Employee::getDepartment));
    
    // Step 2: For each department, find second highest
    return byDept.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> entry.getValue().stream()
                          .sorted(Comparator.comparing(Employee::getSalary).reversed())
                          .skip(1)
                          .findFirst()
        ));
}
```

### Approach B: Single Pipeline

```java
public Map<String, Optional<Employee>> secondHighestByDept(List<Employee> employees) {
    return employees.stream()
        .collect(Collectors.groupingBy(
            Employee::getDepartment,
            Collectors.collectingAndThen(
                Collectors.toList(),
                list -> list.stream()
                            .sorted(Comparator.comparing(Employee::getSalary).reversed())
                            .skip(1)
                            .findFirst()
            )
        ));
}
```

---

## 🔧 Handling Edge Cases

### Edge Case 1: Department with only 1 employee

```java
.skip(1).findFirst()  // Returns Optional.empty() ✅
```

### Edge Case 2: Employees with same salary

```java
// Two employees: Ravi-80k, Amit-80k (tied for highest)
// After sort: [Ravi-80k, Amit-80k]  (order might vary)
// After skip(1): [Amit-80k]
// Result: One of them becomes "second highest"

// To handle ties properly:
.sorted(Comparator.comparing(Employee::getSalary).reversed()
                  .thenComparing(Employee::getName))  // Secondary sort
```

### Edge Case 3: Get "Nth" highest (Generalized)

```java
public Optional<Employee> getNthHighest(List<Employee> emps, int n) {
    return emps.stream()
               .sorted(Comparator.comparing(Employee::getSalary).reversed())
               .skip(n - 1)  // Skip first (n-1) to get nth
               .findFirst();
}

// 1st highest: skip(0)
// 2nd highest: skip(1)
// 3rd highest: skip(2)
```

---

## 📊 Visual Flow

```
INPUT: [E1-IT-75k, E2-HR-65k, E3-IT-85k, E4-Fin-72k, ...]
                            │
            ┌───────────────┼───────────────┐
            │         groupingBy(dept)       │
            └───────────────┼───────────────┘
                            │
         ┌──────────────────┼──────────────────┐
         ▼                  ▼                  ▼
    IT: [E1,E3,E5,E8]  HR: [E2,E6]    Fin: [E4,E7]
         │                  │                  │
    sorted(desc)       sorted(desc)       sorted(desc)
         │                  │                  │
    [85k,80k,75k,55k]  [65k,58k]         [92k,72k]
         │                  │                  │
      skip(1)            skip(1)           skip(1)
         │                  │                  │
    [80k,75k,55k]      [58k]             [72k]
         │                  │                  │
    findFirst()       findFirst()       findFirst()
         │                  │                  │
    Amit-80k         Meera-58k         Sneha-72k
```

---

## 📚 Interview Q&A

### Q1: Why use skip(1) instead of removing first element?

**A:**

- `skip(1)` is stateless and functional
- Removing element mutates the list
- skip integrates naturally in stream pipeline

### Q2: What happens if skip exceeds list size?

**A:** Returns empty stream (not error)

```java
[A, B].skip(5).findFirst()  // Optional.empty()
```

### Q3: Can you do this without sorting the entire list?

**A:** Yes! Use a priority queue or partial sort for O(n) instead of O(n log n):

```java
// But for interview, sorted approach is cleaner to explain
```

### Q4: How to get distinct salaries (no ties)?

**A:**

```java
.map(Employee::getSalary)
.distinct()
.sorted(Comparator.reverseOrder())
.skip(1)
.findFirst()  // Second highest DISTINCT salary
```

### Q5: Time complexity?

**A:**

- groupingBy: O(n)
- sorted: O(k log k) per department (k = dept size)
- Overall: O(n log k) where k = max dept size

---

## 🎯 Key Takeaways

1. **Combine operations** - This problem uses 5+ stream operations
2. **skip(n)** - Perfect for "Nth" element problems
3. **collectingAndThen** - Transform result after collecting
4. **Optional** - Natural return for "might not exist" scenarios
5. **Think step by step** - Break complex problems into stages
6. **Handle edge cases** - Single element, ties, empty lists
