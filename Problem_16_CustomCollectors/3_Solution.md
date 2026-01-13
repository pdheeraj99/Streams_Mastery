# Problem 16: Solution Deep Dive

## 📚 Collector.of() - The Factory Method

```java
Collector.of(
    supplier,       // Create empty accumulator
    accumulator,    // Add element to accumulator
    combiner,       // Merge two accumulators
    finisher,       // Transform to final result
    characteristics // Optional hints
)
```

---

## 🎯 Custom Collector 1: String Joiner

```java
public static Collector<String, StringJoiner, String> 
    toJoinedString(String delimiter, String prefix, String suffix) {
    
    return Collector.of(
        // Supplier: Create empty StringJoiner
        () -> new StringJoiner(delimiter, prefix, suffix),
        
        // Accumulator: Add each string
        StringJoiner::add,
        
        // Combiner: Merge two joiners (for parallel)
        StringJoiner::merge,
        
        // Finisher: Convert to String
        StringJoiner::toString
    );
}

// Usage
String result = names.stream()
    .collect(toJoinedString(", ", "[", "]"));
// Result: "[Ram, Sita, Hanuman]"
```

---

## 🎯 Custom Collector 2: Statistics Collector

```java
public static Collector<Integer, int[], Map<String, Object>>
    toStatistics() {
    
    return Collector.of(
        // Supplier: [count, sum, min, max]
        () -> new int[]{0, 0, Integer.MAX_VALUE, Integer.MIN_VALUE},
        
        // Accumulator
        (stats, num) -> {
            stats[0]++;                              // count
            stats[1] += num;                         // sum
            stats[2] = Math.min(stats[2], num);      // min
            stats[3] = Math.max(stats[3], num);      // max
        },
        
        // Combiner (for parallel)
        (s1, s2) -> new int[]{
            s1[0] + s2[0],
            s1[1] + s2[1],
            Math.min(s1[2], s2[2]),
            Math.max(s1[3], s2[3])
        },
        
        // Finisher: Convert to Map
        stats -> Map.of(
            "count", stats[0],
            "sum", stats[1],
            "min", stats[0] > 0 ? stats[2] : null,
            "max", stats[0] > 0 ? stats[3] : null,
            "avg", stats[0] > 0 ? (double) stats[1] / stats[0] : 0.0
        )
    );
}
```

---

## 🎯 Custom Collector 3: Top N Collector

```java
public static <T> Collector<T, PriorityQueue<T>, List<T>>
    toTopN(int n, Comparator<T> comparator) {
    
    return Collector.of(
        // Supplier: Min-heap of size n
        () -> new PriorityQueue<>(comparator),
        
        // Accumulator: Keep only top n
        (heap, element) -> {
            heap.offer(element);
            if (heap.size() > n) {
                heap.poll();  // Remove smallest
            }
        },
        
        // Combiner: Merge two heaps
        (h1, h2) -> {
            h1.addAll(h2);
            while (h1.size() > n) h1.poll();
            return h1;
        },
        
        // Finisher: Convert to sorted list
        heap -> {
            List<T> result = new ArrayList<>(heap);
            result.sort(comparator.reversed());
            return result;
        }
    );
}

// Usage
List<Integer> top3 = numbers.stream()
    .collect(toTopN(3, Comparator.naturalOrder()));
```

---

## 🎯 Custom Collector 4: Immutable List Collector

```java
public static <T> Collector<T, List<T>, List<T>>
    toImmutableList() {
    
    return Collector.of(
        ArrayList::new,
        List::add,
        (l1, l2) -> { l1.addAll(l2); return l1; },
        Collections::unmodifiableList  // Make immutable!
    );
}
```

---

## 🎯 Custom Collector 5: Grouping with Limit

```java
// Group by category, but keep only first N per group
public static <T, K> Collector<T, ?, Map<K, List<T>>>
    groupingByWithLimit(Function<T, K> classifier, int limit) {
    
    return Collector.of(
        HashMap::new,
        (map, element) -> {
            K key = classifier.apply(element);
            map.computeIfAbsent(key, k -> new ArrayList<>());
            if (map.get(key).size() < limit) {
                map.get(key).add(element);
            }
        },
        (m1, m2) -> {
            m2.forEach((k, v) -> 
                m1.merge(k, v, (l1, l2) -> {
                    l1.addAll(l2.subList(0, Math.min(l2.size(), limit - l1.size())));
                    return l1;
                })
            );
            return m1;
        }
    );
}
```

---

## 📚 Interview Q&A

### Q1: Why do we need combiner()?

**A:** For parallel streams! When data is split across threads, each thread has its own accumulator. Combiner merges them.

### Q2: When is finisher() called?

**A:** After all elements are accumulated (and combined in parallel case). Transforms accumulator to final result.

### Q3: What are Characteristics for?

**A:** Optimization hints:

- `IDENTITY_FINISH`: Skip finisher if A == R
- `UNORDERED`: Order doesn't matter (parallel optimization)
- `CONCURRENT`: Single accumulator for all threads

### Q4: When to use custom collector vs built-in?

**A:** Use custom when:

- Need multiple aggregations in one pass
- Complex accumulation logic
- Reusable across codebase

### Q5: Memory considerations?

**A:** Accumulator lives throughout collection. For huge streams, consider streaming writes instead.

---

## 🎯 Key Takeaways

1. **Collector.of()** = Quick way to create custom collectors
2. **4 components:** supplier, accumulator, combiner, finisher
3. **Combiner for parallel:** Must correctly merge two accumulators
4. **Finisher transforms:** Accumulator type → Result type
5. **Characteristics:** Optional performance hints
