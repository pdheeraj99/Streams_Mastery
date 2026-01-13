# Problem 8: Group Employees by Department

## 📋 The Interview Question

> "You have a list of employees. Group them by department.  
> Return a **Map** where key is department name and value is list of employees in that department."

---

## 📥 Input

```java
List<Employee> employees = Arrays.asList(
    new Employee("E001", "Ravi", 45000, "IT"),
    new Employee("E002", "Priya", 65000, "HR"),
    new Employee("E003", "Arjun", 55000, "IT"),
    new Employee("E004", "Sneha", 72000, "Finance"),
    new Employee("E005", "Kiran", 48000, "IT"),
    new Employee("E006", "Meera", 58000, "HR"),
    new Employee("E007", "Raj", 82000, "Finance")
);
```

---

## 📤 Expected Output

```java
Map<String, List<Employee>> result = {
    "IT"      → [Ravi, Arjun, Kiran],
    "HR"      → [Priya, Meera],
    "Finance" → [Sneha, Raj]
}
```

---

## 🎯 Follow-up Questions (Interviewer might ask)

1. "Group by department, but only get employee NAMES (not full objects)"
2. "Group by department and get COUNT of employees in each"
3. "Group by department and get TOTAL SALARY per department"
4. "Group by department, then by salary range (nested grouping)"
5. "Group by department, but only include employees with salary > 50000"

---

## ⚠️ Edge Cases

1. What if department is null?
2. What if no employees in a department?
3. What about maintaining order of departments?
