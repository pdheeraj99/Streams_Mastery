# Problem 11: Solution Deep Dive

## 📚 Two Key Techniques

### Technique 1: Count and Filter

```
Step 1: Count each element
        [1,2,3,2,4,3,5,1,6] → {1:2, 2:2, 3:2, 4:1, 5:1, 6:1}
        
Step 2: Filter count > 1
        {1:2, 2:2, 3:2} → [1, 2, 3]
```

### Technique 2: Set.add() Trick

```
Set.add(element) returns:
  → true  if NEW (not seen before)
  → false if DUPLICATE (already in set)

Filter for FALSE = Keep only duplicates!
```

---

## 🎯 Solution A: groupingBy + counting

```java
public List<Integer> findDuplicates(List<Integer> numbers) {
    return numbers.stream()
        // Step 1: Group by element itself, count occurrences
        .collect(Collectors.groupingBy(
            Function.identity(),    // Element as key
            Collectors.counting()   // Count as value
        ))
        // Step 2: Filter entries where count > 1
        .entrySet().stream()
        .filter(entry -> entry.getValue() > 1)
        // Step 3: Extract just the keys (elements)
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());
}
```

### Visual Execution

```
Input: [1, 2, 3, 2, 4, 3, 5, 1, 6]
              ↓
groupingBy + counting → Map<Integer, Long>
              ↓
{1=2, 2=2, 3=2, 4=1, 5=1, 6=1}
              ↓
filter(count > 1)
              ↓
{1=2, 2=2, 3=2}
              ↓
map(getKey)
              ↓
[1, 2, 3]
```

---

## 🎯 Solution B: Set.add() Trick (Clever!)

```java
public List<Integer> findDuplicatesClever(List<Integer> numbers) {
    Set<Integer> seen = new HashSet<>();
    
    return numbers.stream()
        .filter(n -> !seen.add(n))  // add() returns false if already exists!
        .distinct()                  // In case same duplicate appears multiple times
        .collect(Collectors.toList());
}
```

### Why This Works

```java
Set<Integer> seen = new HashSet<>();

seen.add(1);  // true  → 1 is NEW, added to set    → filter OUT
seen.add(2);  // true  → 2 is NEW, added to set    → filter OUT
seen.add(3);  // true  → 3 is NEW, added to set    → filter OUT
seen.add(2);  // FALSE → 2 EXISTS, not added       → KEEP! (duplicate)
seen.add(4);  // true  → 4 is NEW, added to set    → filter OUT
seen.add(3);  // FALSE → 3 EXISTS, not added       → KEEP! (duplicate)
seen.add(1);  // FALSE → 1 EXISTS, not added       → KEEP! (duplicate)
```

### ⚠️ Caveat

- Uses external state (`seen` set)
- Not purely functional
- But single pass = O(n) vs O(2n)

---

## 🎨 Variations

### Variation 1: Duplicates with Count

```java
Map<Integer, Long> duplicatesWithCount = numbers.stream()
    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
    .entrySet().stream()
    .filter(e -> e.getValue() > 1)
    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

// Result: {1=2, 2=2, 3=2}
```

### Variation 2: Find First Duplicate

```java
Optional<Integer> firstDuplicate = numbers.stream()
    .filter(n -> !seen.add(n))
    .findFirst();
```

### Variation 3: Remove Duplicates (Keep Unique Only)

```java
// Keep only elements that appear ONCE
List<Integer> uniqueOnly = numbers.stream()
    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
    .entrySet().stream()
    .filter(e -> e.getValue() == 1)  // Count = 1 only
    .map(Map.Entry::getKey)
    .collect(Collectors.toList());
```

### Variation 4: With Custom Objects

```java
// Find duplicate employees by ID
List<Employee> duplicateEmployees = employees.stream()
    .collect(Collectors.groupingBy(Employee::getId, Collectors.counting()))
    .entrySet().stream()
    .filter(e -> e.getValue() > 1)
    .map(Map.Entry::getKey)
    .flatMap(id -> employees.stream().filter(e -> e.getId().equals(id)))
    .distinct()
    .collect(Collectors.toList());
```

---

## 📚 Interview Q&A

### Q1: Why `Function.identity()` instead of `n -> n`?

**A:** Same result, but `identity()` is:

- More readable (expresses intent)
- Sometimes more efficient (reuses same instance)
- Shows you know the API

### Q2: Why `distinct()` in the Set trick approach?

**A:** If an element appears 3+ times:

```
[1, 1, 1] with Set trick:
  seen.add(1) → true  → OUT
  seen.add(1) → false → KEEP
  seen.add(1) → false → KEEP  ← Without distinct, result is [1, 1]!
```

### Q3: Time/Space complexity?

**A:**

- Approach A: O(n) time, O(n) space (for the map)
- Approach B: O(n) time, O(n) space (for the set)
- Both are linear!

### Q4: How to preserve order of first occurrence?

**A:** Use `LinkedHashMap`:

```java
.collect(Collectors.groupingBy(
    Function.identity(),
    LinkedHashMap::new,
    Collectors.counting()
))
```

---

## 🎯 Key Takeaways

1. **groupingBy + counting** = Count occurrences of anything
2. **Function.identity()** = Use element itself as key
3. **Set.add() returns boolean** = Clever trick for duplicates
4. **entrySet().stream()** = Convert Map to stream
5. **Two-step pattern**: Count → Filter is very common!
