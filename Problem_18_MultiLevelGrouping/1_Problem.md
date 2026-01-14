# Problem 18: Multi-level Grouping

## 📋 The Interview Question

> "You have a list of employees with department, location, and experience level.  
> Group them by **department**, then by **location**, then by **experience level**."

---

## 📥 Input

```java
List<Employee> employees = Arrays.asList(
    new Employee("Ram", "IT", "Bangalore", "Senior"),
    new Employee("Sita", "HR", "Mumbai", "Junior"),
    new Employee("Arjun", "IT", "Bangalore", "Junior"),
    new Employee("Priya", "Finance", "Delhi", "Senior"),
    new Employee("Kiran", "IT", "Mumbai", "Senior"),
    new Employee("Meera", "HR", "Bangalore", "Senior"),
    new Employee("Raj", "IT", "Bangalore", "Junior"),
    new Employee("Sneha", "Finance", "Mumbai", "Junior")
);
```

---

## 📤 Expected Output

```java
{
    "IT" → {
        "Bangalore" → {
            "Senior" → [Ram],
            "Junior" → [Arjun, Raj]
        },
        "Mumbai" → {
            "Senior" → [Kiran]
        }
    },
    "HR" → {
        "Mumbai" → {"Junior" → [Sita]},
        "Bangalore" → {"Senior" → [Meera]}
    },
    "Finance" → {...}
}
```

**Result Type:** `Map<String, Map<String, Map<String, List<Employee>>>>`

---

## 🎯 Variations

1. "Group by 2 levels only (dept → location)"
2. "Group by composite key (dept-location combined)"
3. "Count at each level instead of list"
4. "First group, then aggregate (sum salaries)"
