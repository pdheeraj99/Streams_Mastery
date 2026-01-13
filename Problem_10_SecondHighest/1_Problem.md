# Problem 10: Second Highest Salary by Department

## 📋 The Interview Question (CLASSIC! 🔥)

> "Find the **second highest salary** in each department.  
> If a department has less than 2 employees, return null/Optional.empty for that department."

---

## 📥 Input

```java
List<Employee> employees = Arrays.asList(
    new Employee("E001", "Ravi", 75000, "IT"),
    new Employee("E002", "Priya", 65000, "HR"),
    new Employee("E003", "Arjun", 85000, "IT"),
    new Employee("E004", "Sneha", 72000, "Finance"),
    new Employee("E005", "Kiran", 55000, "IT"),
    new Employee("E006", "Meera", 58000, "HR"),
    new Employee("E007", "Raj", 92000, "Finance"),
    new Employee("E008", "Amit", 80000, "IT")
);
```

**Structure:**

```
IT:      Arjun-85k, Amit-80k, Ravi-75k, Kiran-55k  → 2nd: Amit (80k)
HR:      Priya-65k, Meera-58k                       → 2nd: Meera (58k)
Finance: Raj-92k, Sneha-72k                         → 2nd: Sneha (72k)
```

---

## 📤 Expected Output

```java
Map<String, Optional<Employee>> result = {
    "IT"      → Optional[Amit - 80000],
    "HR"      → Optional[Meera - 58000],
    "Finance" → Optional[Sneha - 72000]
}
```

---

## 🎯 Follow-up Questions

1. "What if there's only 1 employee in a department?"
2. "What if two employees have same salary? (duplicates)"
3. "Find Nth highest salary" (generalized version)

---

## ⚠️ Edge Cases

1. Department with only 1 employee
2. Two employees with same highest salary
3. All employees with same salary
4. Empty department
