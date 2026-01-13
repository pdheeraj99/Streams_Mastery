# Problem 15: Solution Deep Dive

## 📚 Sequential vs Parallel - Visual

```
SEQUENTIAL:
┌───────────────────────────────────────┐
│  Thread-1: [1] → [2] → [3] → [4] → [5]│
│              ↓     ↓     ↓     ↓     ↓ │
│            process each one by one    │
│                      ↓                 │
│                  Result               │
└───────────────────────────────────────┘

PARALLEL:
┌───────────────────────────────────────┐
│  Thread-1: [1] [2]  ─────┐            │
│  Thread-2: [3] [4]  ─────┼─→ Combine  │
│  Thread-3: [5]      ─────┘     ↓      │
│                           Result      │
└───────────────────────────────────────┘
```

---

## 🎯 How to Create Parallel Stream

### Method 1: parallelStream()

```java
list.parallelStream()
    .filter(...)
    .collect(...);
```

### Method 2: stream().parallel()

```java
list.stream()
    .parallel()
    .filter(...)
    .collect(...);
```

### Check if Parallel

```java
stream.isParallel()  // returns boolean
```

### Convert Back to Sequential

```java
stream.parallel()
      .filter(...)
      .sequential()  // Switch to sequential
      .collect(...);
```

---

## 📊 When Parallel Helps - Decision Matrix

| Factor | Sequential ✓ | Parallel ✓ |
|--------|--------------|------------|
| Data Size | < 10,000 | > 100,000 |
| Operation | Simple/IO | CPU-heavy |
| Source | LinkedList | ArrayList/Array |
| Shared State | Any | None |
| Order | Important | Not important |

---

## 🔧 Best Practices

### 1. Use Proper Collectors (Thread-safe)

```java
// ✅ Good - Collectors are thread-safe
.collect(Collectors.toList())
.collect(Collectors.groupingByConcurrent(...))

// ❌ Bad - Manual collection
List<T> list = new ArrayList<>();
.forEach(x -> list.add(x))  // Race condition!
```

### 2. Avoid Stateful Lambdas

```java
// ❌ Bad - Stateful lambda
AtomicInteger counter = new AtomicInteger();
.filter(x -> counter.incrementAndGet() < 10)  // Unpredictable!

// ✅ Good - Stateless
.filter(x -> x > 10)
.limit(10)  // Use limit() instead
```

### 3. Use forEachOrdered for Ordering

```java
// Order NOT guaranteed
.parallel().forEach(System.out::println);

// Order guaranteed (but slower)
.parallel().forEachOrdered(System.out::println);
```

### 4. Consider Custom Thread Pool

```java
// Default uses common ForkJoinPool (shared!)
// For isolation:
ForkJoinPool customPool = new ForkJoinPool(4);
customPool.submit(() ->
    list.parallelStream()
        .filter(...)
        .collect(...)
).get();
```

---

## 📈 Performance Comparison

### Small Data (1,000 elements)

```
Sequential: ~1ms
Parallel:   ~5ms  ← SLOWER! (thread overhead)
```

### Large Data (1,000,000 elements) with CPU work

```
Sequential: ~500ms
Parallel:   ~150ms  ← FASTER! (4 cores utilized)
```

### Large Data with IO work

```
Sequential: ~5000ms
Parallel:   ~5000ms  ← NO BENEFIT! (IO is the bottleneck)
```

---

## 📚 Interview Q&A

### Q1: When should you NOT use parallel streams?

**A:**

- Small datasets (< 10K elements)
- IO-bound operations
- Shared mutable state
- When order matters and using forEach
- Already inside parallel context

### Q2: What's the default thread pool?

**A:** ForkJoinPool.commonPool() with (CPU cores - 1) threads

### Q3: How to change parallel threads count?

**A:**

```java
System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "8");
// OR use custom ForkJoinPool
```

### Q4: Is parallel stream always faster?

**A:** NO! Thread creation/management has overhead. Only faster for:

- Large data
- CPU-intensive operations
- Good splitable sources

### Q5: What's the difference between Collectors.toList() in parallel?

**A:** It works correctly! Collectors handle thread-safety internally.

### Q6: What about reduce() in parallel?

**A:** Works correctly IF:

- Identity is true identity (0 for sum, "" for concat)
- Accumulator is associative

```java
// ✅ Good - associative
.reduce(0, Integer::sum)

// ❌ Bad - not associative
.reduce(0, (a, b) -> a - b)
```

---

## 🎯 Key Takeaways

1. **Parallel != Faster** - Measure first!
2. **Avoid shared mutable state** - Use collectors
3. **Source matters** - ArrayList good, LinkedList bad
4. **CPU-bound benefits** - IO-bound doesn't
5. **Order costs** - forEachOrdered is slower
6. **Default pool is shared** - Consider isolation
7. **Stateless lambdas only** - No side effects!
