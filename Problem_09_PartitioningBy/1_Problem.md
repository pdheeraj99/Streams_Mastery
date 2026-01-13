# Problem 9: Partition Students Pass/Fail

## 📋 The Interview Question

> "You have a list of students with their marks.  
> Partition them into **PASS** (marks ≥ 40) and **FAIL** (marks < 40).  
> Return a Map with Boolean keys."

---

## 📥 Input

```java
List<Student> students = Arrays.asList(
    new Student("S001", "Ravi", 75),
    new Student("S002", "Priya", 35),
    new Student("S003", "Arjun", 42),
    new Student("S004", "Sneha", 28),
    new Student("S005", "Kiran", 88),
    new Student("S006", "Meera", 39),
    new Student("S007", "Raj", 55)
);
```

---

## 📤 Expected Output

```java
Map<Boolean, List<Student>> result = {
    true  → [Ravi-75, Arjun-42, Kiran-88, Raj-55],   // PASS
    false → [Priya-35, Sneha-28, Meera-39]           // FAIL
}
```

---

## 🎯 Follow-up Questions

1. "Get count of pass vs fail students"
2. "Get average marks of pass vs fail"
3. "Get names of pass vs fail students"
4. "What's the difference between partitioningBy and groupingBy?"

---

## ⚠️ Edge Cases

1. All students pass?
2. All students fail?
3. Marks exactly 40 (boundary)?
