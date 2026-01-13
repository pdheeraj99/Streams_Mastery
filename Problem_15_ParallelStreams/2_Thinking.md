# Problem 15: Thinking Process

## 1️⃣ Understanding Parallel Streams

### What is Parallel Stream?

```
Sequential: [1] → [2] → [3] → [4] → [5]  (one at a time)

Parallel:   [1,2] → Thread 1
            [3,4] → Thread 2    → Combine results
            [5]   → Thread 3
```

Uses **ForkJoinPool** internally to split work across CPU cores.

---

## 2️⃣ When to Use Parallel? ✅

### Use Parallel When

1. **Large data** (thousands+ elements)
2. **CPU-intensive operations** (heavy computations)
3. **Independent operations** (no shared state)
4. **Splitable source** (ArrayList, arrays - good; LinkedList - bad)

### DON'T Use Parallel When: ❌

1. **Small data** (overhead > benefit)
2. **IO-bound operations** (network, file)
3. **Order matters** (and you use unordered ops)
4. **Shared mutable state** (race conditions!)
5. **Already in parallel context** (nested parallelism = bad)

---

## 3️⃣ The Golden Rule

```
┌─────────────────────────────────────────────────────────┐
│  Parallel Stream = LAST RESORT optimization!            │
│                                                         │
│  1. First write correct sequential code                 │
│  2. Measure performance                                 │
│  3. If too slow AND data is large, try parallel         │
│  4. Measure again to verify improvement                 │
└─────────────────────────────────────────────────────────┘
```

---

## 4️⃣ Common Pitfalls

### Pitfall 1: Shared Mutable State (DANGEROUS!)

```java
// ❌ WRONG - Race condition!
List<Integer> results = new ArrayList<>();
numbers.parallelStream()
    .filter(n -> n > 0)
    .forEach(n -> results.add(n));  // NOT thread-safe!

// ✅ CORRECT - Use collect
List<Integer> results = numbers.parallelStream()
    .filter(n -> n > 0)
    .collect(Collectors.toList());  // Thread-safe!
```

### Pitfall 2: Ordering Issues

```java
// Sequential: maintains encounter order
// Parallel with forEachOrdered: maintains order (slower)
// Parallel with forEach: NO order guarantee!
```

### Pitfall 3: Source Type Matters

```java
ArrayList    → Good for parallel (random access, easy to split)
LinkedList   → Bad for parallel (sequential access)
TreeSet      → Medium (balanced, OK to split)
```

---

## 5️⃣ Key Concepts to Master

1. **parallelStream()** vs **stream().parallel()**
2. **ForkJoinPool** - default thread pool
3. **Spliterator** - how to split data
4. **Stateless operations** - safe for parallel
5. **Order preservation** - forEachOrdered

👉 **See 3_Solution.md for benchmarks and best practices!**
