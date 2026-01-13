# Problem 17: Thinking Process

## 1️⃣ The Problem with Default groupingBy

```java
// Default: gives List<Employee>
Map<String, List<Employee>> byDept = employees.stream()
    .collect(groupingBy(Employee::getDepartment));

// But what if you want List<String> (names only)?
// What if you want to filter high-salary only?
// What if you want count instead of list?
```

**Solution: Downstream Collectors!**

---

## 2️⃣ How Downstream Works

```
groupingBy(classifier, downstream)
              │              │
              │              └── What to do WITH each group
              │
              └── How to CREATE groups
```

**Think:** "Group by X, then do Y with each group"

---

## 3️⃣ The Key Downstream Collectors

### mapping() - Transform before collecting

```java
// Group → get names only
groupingBy(
    Employee::getDepartment,
    mapping(Employee::getName, toList())
)
// Result: Map<String, List<String>>
```

### filtering() - Filter within groups

```java
// Group → keep only high salary
groupingBy(
    Employee::getDepartment,
    filtering(e -> e.getSalary() > 50000, toList())
)
// Result: Map<String, List<Employee>> (filtered)
```

### flatMapping() - Flatten nested collections

```java
// If Employee has List<Skill>
groupingBy(
    Employee::getDepartment,
    flatMapping(e -> e.getSkills().stream(), toSet())
)
// Result: Map<String, Set<Skill>>
```

### collectingAndThen() - Post-process result

```java
// Group → get list → make immutable
groupingBy(
    Employee::getDepartment,
    collectingAndThen(toList(), Collections::unmodifiableList)
)
```

---

## 4️⃣ Chaining Downstreams

**Mind-blowing:** You can chain them!

```java
groupingBy(
    Employee::getDepartment,
    filtering(
        e -> e.getSalary() > 50000,
        mapping(Employee::getName, toList())
    )
)
// Filter first, then map!
```

---

## 5️⃣ Visual Flow

```
Input: [Ram-IT, Sita-HR, Arjun-IT, ...]
              │
    groupingBy(dept)
              │
         ┌────┴────┐
         ▼         ▼
      IT group   HR group
      [Ram,      [Sita,
       Arjun,     Meera]
       Kiran]
         │         │
    downstream  downstream
    collector   collector
         │         │
         ▼         ▼
    [Ram,Arjun,  [Sita,Meera]  ← mapping(getName)
     Kiran]
```

👉 **See 3_Solution.md for all combinations!**
