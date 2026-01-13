# Problem 9: Thinking Process

## 1️⃣ Don't Panic! Let's Understand

### What is the INPUT?

- `List<Student>` with marks

### What is the OUTPUT?

- `Map<Boolean, List<Student>>`
- `true` key → passed students
- `false` key → failed students

### What is the CORE action?

```
BEFORE: [Ravi-75, Priya-35, Arjun-42, Sneha-28, ...]
              ↓ PARTITION by pass/fail
AFTER:  {
          true  → [Ravi-75, Arjun-42, ...],   // PASSED
          false → [Priya-35, Sneha-28, ...]   // FAILED
        }
```

**Binary split! Only TWO groups!** ✅❌

---

## 2️⃣ Why Not groupingBy()?

We COULD use groupingBy:

```java
.collect(groupingBy(s -> s.getMarks() >= 40))
// Result: Map<Boolean, List<Student>>
```

**But wait!** What if NO student fails?

```java
// With groupingBy - missing keys possible!
{true → [all students]}  // false key MISSING!

// With partitioningBy - ALWAYS both keys!
{true → [all students], false → []}  // false key present (empty list)
```

**partitioningBy GUARANTEES both keys!**

---

## 3️⃣ Brainstorming Approaches

### Approach A: Two separate filters

```java
List<Student> passed = students.stream()
    .filter(s -> s.getMarks() >= 40)
    .collect(toList());
    
List<Student> failed = students.stream()
    .filter(s -> s.getMarks() < 40)
    .collect(toList());
```

**Verdict:** ❌ Two passes! Inefficient.

---

### Approach B: groupingBy with Boolean

```java
Map<Boolean, List<Student>> result = students.stream()
    .collect(groupingBy(s -> s.getMarks() >= 40));
```

**Verdict:** ⚠️ Works, but missing keys if all pass/fail.

---

### Approach C: partitioningBy() 🎯

```java
Map<Boolean, List<Student>> result = students.stream()
    .collect(partitioningBy(s -> s.getMarks() >= 40));
```

**ONE LINE! Both keys guaranteed!**

**Verdict:** ✅ Perfect for binary splits!

---

## 4️⃣ Trade-offs

| Approach | Passes | Both Keys? | When to Use |
|----------|--------|------------|-------------|
| A: Two filters | 2 | N/A | Never for partition |
| B: groupingBy | 1 | NO ❌ | When missing key OK |
| C: partitioningBy | 1 | YES ✅ | Binary splits ⭐ |

---

## 5️⃣ My Decision: Approach C

**Why?**

1. ✅ Single pass through data
2. ✅ Both true/false keys ALWAYS present
3. ✅ Clearer intent: "partition" vs "group"
4. ✅ Downstream collectors work same as groupingBy

---

## 6️⃣ Concepts to Master

1. **partitioningBy()** - Binary splitting
2. **Difference from groupingBy()** - Key guarantees
3. **Downstream collectors** - Same as groupingBy!

👉 **See 3_Solution.md for deep dive!**
