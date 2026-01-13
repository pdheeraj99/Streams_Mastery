# Problem 12: Solution Deep Dive

## 📚 Pattern: Count → Find Max

```
STEP 1: Count each element
        [1,3,2,3,3,2,1,3] → {1=2, 2=2, 3=4}

STEP 2: Find entry with maximum value
        max by value → Entry(3, 4)

STEP 3: Extract the key
        3
```

---

## 🎯 Solution: max() with comparingByValue

```java
public Optional<Integer> findMostFrequent(List<Integer> numbers) {
    return numbers.stream()
        // Step 1: Count each element
        .collect(Collectors.groupingBy(
            Function.identity(),
            Collectors.counting()
        ))
        // Step 2: Find entry with max count
        .entrySet().stream()
        .max(Map.Entry.comparingByValue())
        // Step 3: Extract the key (element)
        .map(Map.Entry::getKey);
}
```

---

## 🔑 Key Method: Map.Entry.comparingByValue()

```java
// Compare map entries by their VALUES
Map.Entry.comparingByValue()

// This is equivalent to:
Comparator.comparing(Map.Entry::getValue)

// Also available:
Map.Entry.comparingByKey()  // Compare by keys
```

---

## 🎨 Variations

### Variation 1: Handle Ties (Return ALL most frequent)

```java
public List<Integer> findAllMostFrequent(List<Integer> numbers) {
    Map<Integer, Long> counts = numbers.stream()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    
    // Find the max count
    long maxCount = counts.values().stream()
        .max(Long::compare)
        .orElse(0L);
    
    // Return all with max count
    return counts.entrySet().stream()
        .filter(e -> e.getValue() == maxCount)
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());
}
```

### Variation 2: Kth Most Frequent

```java
public Optional<Integer> findKthMostFrequent(List<Integer> numbers, int k) {
    return numbers.stream()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
        .entrySet().stream()
        .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
        .skip(k - 1)
        .findFirst()
        .map(Map.Entry::getKey);
}
```

### Variation 3: Least Frequent (min instead of max)

```java
public Optional<Integer> findLeastFrequent(List<Integer> numbers) {
    return numbers.stream()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
        .entrySet().stream()
        .min(Map.Entry.comparingByValue())  // min instead of max!
        .map(Map.Entry::getKey);
}
```

### Variation 4: Most Frequent Word

```java
public Optional<String> mostFrequentWord(String sentence) {
    return Arrays.stream(sentence.toLowerCase().split("\\s+"))
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
        .entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey);
}
```

---

## 📊 Visual Flow

```
Input: [1, 3, 2, 3, 3, 2, 1, 3]
              │
    groupingBy(identity(), counting())
              │
              ▼
    {1=2, 2=2, 3=4}
              │
    entrySet().stream()
              │
              ▼
    [Entry(1,2), Entry(2,2), Entry(3,4)]
              │
    max(comparingByValue())
              │
              ▼
    Optional[Entry(3, 4)]
              │
    map(getKey)
              │
              ▼
    Optional[3] ✅
```

---

## 📚 Interview Q&A

### Q1: What if list is empty?

**A:** Returns `Optional.empty()` - safe!

### Q2: Time complexity?

**A:** O(n) - single pass to count, single pass to find max

### Q3: What if there's a tie?

**A:** `max()` returns one of them (undefined which).
For deterministic tie-breaking, add secondary sort:

```java
.max(Map.Entry.<Integer, Long>comparingByValue()
              .thenComparing(Map.Entry::getKey))  // Smallest key wins tie
```

### Q4: How to get both element AND its count?

**A:** Don't unwrap the entry:

```java
Optional<Map.Entry<Integer, Long>> result = ...
           .max(Map.Entry.comparingByValue());
// result.get() gives Entry(3, 4)
```

---

## 🎯 Key Takeaways

1. **groupingBy + counting** - Reusable pattern for frequency!
2. **max()** - Terminal op returning Optional
3. **Map.Entry.comparingByValue()** - Compare entries by value
4. **Two-step pattern**: Count → Find extreme (max/min)
5. Same pattern works for: most frequent, least frequent, Kth frequent
