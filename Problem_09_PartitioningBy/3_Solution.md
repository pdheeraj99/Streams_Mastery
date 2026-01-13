# Problem 9: Solution Deep Dive

## 📚 Core Concept: partitioningBy()

### The Pass/Fail Analogy 📝

```
Teacher checking exam papers:

📋📋📋📋📋📋📋  →  ✅ PASS pile: [paper1, paper3, paper5]
                    ❌ FAIL pile: [paper2, paper4, paper6, paper7]

ALWAYS two piles - even if one is empty!
```

---

## 🔑 partitioningBy vs groupingBy

### The Key Difference

| Feature | groupingBy() | partitioningBy() |
|---------|--------------|------------------|
| Groups | N groups (any key) | Exactly 2 (true/false) |
| Missing keys | Possible! | NEVER (both always exist) |
| Key type | Any type K | Boolean only |
| Use case | Categories | Binary yes/no splits |

### Why Both Keys Matter

```java
List<Student> allPass = Arrays.asList(
    new Student("A", 90),
    new Student("B", 80)
);

// groupingBy - false key MISSING!
allPass.stream().collect(groupingBy(s -> s.getMarks() >= 40));
// Result: {true=[A, B]}  ← No false key!

// partitioningBy - both keys GUARANTEED!
allPass.stream().collect(partitioningBy(s -> s.getMarks() >= 40));
// Result: {true=[A, B], false=[]}  ← Empty list, but key exists!
```

**Why this matters:**

```java
result.get(false)  // groupingBy → null (NPE danger!)
result.get(false)  // partitioningBy → [] (safe!)
```

---

## 🎯 Two Versions of partitioningBy()

### Version 1: Simple

```java
partitioningBy(Predicate<T> predicate)
```

```java
Map<Boolean, List<Student>> result = students.stream()
    .collect(partitioningBy(s -> s.getMarks() >= 40));
```

Result: `Map<Boolean, List<T>>`

---

### Version 2: With Downstream

```java
partitioningBy(Predicate<T> predicate, Collector<T,?,D> downstream)
```

```java
// Partition and COUNT
Map<Boolean, Long> counts = students.stream()
    .collect(partitioningBy(
        s -> s.getMarks() >= 40,
        counting()
    ));
// {true=4, false=3}
```

---

## 🎨 Common Patterns

### Pattern 1: Count pass/fail

```java
Map<Boolean, Long> counts = students.stream()
    .collect(partitioningBy(
        s -> s.getMarks() >= 40,
        Collectors.counting()
    ));
```

### Pattern 2: Average marks in each group

```java
Map<Boolean, Double> avgMarks = students.stream()
    .collect(partitioningBy(
        s -> s.getMarks() >= 40,
        Collectors.averagingDouble(Student::getMarks)
    ));
```

### Pattern 3: Get names only

```java
Map<Boolean, List<String>> names = students.stream()
    .collect(partitioningBy(
        s -> s.getMarks() >= 40,
        Collectors.mapping(Student::getName, Collectors.toList())
    ));
```

### Pattern 4: Join names as string

```java
Map<Boolean, String> namesJoined = students.stream()
    .collect(partitioningBy(
        s -> s.getMarks() >= 40,
        Collectors.mapping(
            Student::getName,
            Collectors.joining(", ")
        )
    ));
// {true="Ravi, Arjun", false="Priya, Sneha"}
```

---

## 📊 Visual: How partitioningBy Works

```
Input: [S1-75, S2-35, S3-42, S4-28, S5-88]
                 │
    ┌────────────┼────────────┐
    │ predicate: marks >= 40  │
    └────────────┼────────────┘
                 │
         ┌───────┴───────┐
         ▼               ▼
      true?           false?
         │               │
    ┌────┴────┐    ┌────┴────┐
    │ S1, S3, S5 │    │ S2, S4 │
    └─────────┘    └─────────┘
               │
               ▼
    Map<Boolean, List<Student>>
    {true=[S1,S3,S5], false=[S2,S4]}
```

---

## 📚 Interview Q&A

### Q1: When to use partitioningBy vs groupingBy?

**A:**

```
partitioningBy → Binary split (yes/no, pass/fail, adult/minor)
                 Need both keys guaranteed
                 
groupingBy     → Multiple categories (dept, country, status)
                 OK if some categories empty/missing
```

### Q2: Can partitioningBy have null predicate result?

**A:** NO! Predicate MUST return boolean. No nulls possible.

### Q3: How are both keys guaranteed?

**A:** Internally, partitioningBy creates BOTH entries before processing:

```java
// Simplified internal logic
Map<Boolean, List<T>> result = new HashMap<>();
result.put(true, new ArrayList<>());   // Created upfront!
result.put(false, new ArrayList<>());  // Created upfront!
// Then populate...
```

### Q4: Performance difference?

**A:** Essentially same. Both are O(n) single pass.

### Q5: Common interview scenarios?

**A:**

- Pass/Fail students
- Adult/Minor customers (age >= 18)
- In-stock/Out-of-stock products
- Active/Inactive users
- Above/Below threshold values

---

## 🎯 Key Takeaways

1. **partitioningBy** = Binary yes/no split
2. **ALWAYS two keys** (true and false) - never null!
3. Takes **Predicate** (returns boolean)
4. Same downstream collectors as groupingBy
5. Use when you need BOTH groups guaranteed
6. Perfect for: pass/fail, adult/minor, active/inactive
