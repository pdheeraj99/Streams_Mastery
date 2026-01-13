# Problem 16: Thinking Process

## 1️⃣ How Does collect() Work Internally?

```
Stream: [A] → [B] → [C]
              │
    ┌─────────┴─────────┐
    │  COLLECTOR does:  │
    │                   │
    │  1. supplier()    │ → Create empty container
    │  2. accumulator() │ → Add each element
    │  3. combiner()    │ → Merge containers (parallel)
    │  4. finisher()    │ → Transform to final result
    └───────────────────┘
              │
              ▼
         Final Result
```

---

## 2️⃣ The Collector Interface

```java
public interface Collector<T, A, R> {
    Supplier<A> supplier();           // Create accumulator
    BiConsumer<A, T> accumulator();   // Add element to accumulator
    BinaryOperator<A> combiner();     // Merge two accumulators
    Function<A, R> finisher();        // Transform to result
    Set<Characteristics> characteristics();
}
```

### Type Parameters

- `T` = Input element type
- `A` = Accumulator type (mutable container)
- `R` = Result type

---

## 3️⃣ Two Ways to Create Custom Collectors

### Way 1: Collector.of() (Preferred!)

```java
Collector.of(
    supplier,      // () -> new Container()
    accumulator,   // (container, element) -> add to container
    combiner,      // (container1, container2) -> merge
    finisher,      // container -> final result
    characteristics...
);
```

### Way 2: Implement Collector Interface

```java
public class MyCollector implements Collector<T, A, R> {
    // Implement all methods
}
```

**Collector.of() is cleaner for most cases!**

---

## 4️⃣ Characteristics (Optional but Important)

| Characteristic | Meaning |
|----------------|---------|
| `CONCURRENT` | Accumulator supports concurrent access |
| `UNORDERED` | Order doesn't matter |
| `IDENTITY_FINISH` | Finisher is identity function (A == R) |

---

## 5️⃣ Let's Build: Custom String Joiner

**Goal:** Join strings with delimiter, prefix, suffix

```java
// Input
["Ram", "Sita", "Hanuman"]

// Output with prefix "[", suffix "]", delimiter ", "
"[Ram, Sita, Hanuman]"
```

**Components:**

- Supplier: `() -> new StringJoiner(", ", "[", "]")`
- Accumulator: `(joiner, str) -> joiner.add(str)`
- Combiner: `(j1, j2) -> j1.merge(j2)`
- Finisher: `StringJoiner::toString`

👉 **See 3_Solution.md for complete implementations!**
