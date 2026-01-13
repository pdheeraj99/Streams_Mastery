# Problem 17: Downstream Collectors Deep Dive

## 📋 The Interview Question

> "You have a list of employees. Group them by department and:  
>
> 1. Get only their **names** (not full Employee objects)  
> 2. Get only employees with **salary > 50000**  
> 3. Get a **comma-separated string** of names  
> 4. Get the **count** per department"

---

## 🎯 What are Downstream Collectors?

When you use `groupingBy()`, the default downstream collector is `toList()`.

But you can **change what happens to each group** using downstream collectors!

```java
groupingBy(classifier)                    // Default: toList()
groupingBy(classifier, downstream)        // Custom downstream!
```

---

## 📚 Downstream Collectors We'll Learn

| Collector | Purpose |
|-----------|---------|
| `mapping()` | Transform elements before collecting |
| `filtering()` | Filter elements before collecting |
| `flatMapping()` | Flatten + collect |
| `collectingAndThen()` | Post-process the result |
| `counting()` | Count elements |
| `summingInt/Double()` | Sum values |
| `maxBy()/minBy()` | Find max/min |
| `reducing()` | Custom reduction |

---

## 📥 Input

```java
List<Employee> employees = Arrays.asList(
    new Employee("Ram", 45000, "IT"),
    new Employee("Sita", 65000, "HR"),
    new Employee("Arjun", 55000, "IT"),
    new Employee("Priya", 72000, "Finance"),
    new Employee("Kiran", 48000, "IT"),
    new Employee("Meera", 58000, "HR")
);
```
