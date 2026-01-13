# Problem 4: Calculate Total INR Transactions

## 📋 Problem Statement

You have a list of financial transactions. Calculate the **total amount** of all transactions in **INR currency**.

```
Input:  List of Transaction objects
        T1 - 1000.0 - INR
        T2 - 500.0  - USD    ← Skip (not INR)
        T3 - 2500.0 - INR
        T4 - 800.0  - INR

Output: Total INR amount = 4300.0
```

---

## 🎭 Telugu Analogy

Imagine you're a **shopkeeper** counting today's sales:

- You have sales in **Cash** and **UPI**
- Boss asks: "Total **UPI** sales enti?"

You need to:

1. **Filter** only UPI transactions
2. **Add up** all the amounts

Same here: Filter INR → Sum amounts

---

## 🧠 Core Concept: Aggregation

So far we've learned:

- `filter()` → Select elements
- `map()` → Transform elements
- `findFirst()` → Get one element

Now: **Combine multiple elements into ONE result**

### Aggregation Operations

| Operation | Returns | What it does |
|-----------|---------|--------------|
| `count()` | long | Count elements |
| `sum()` | int/long/double | Add all values |
| `min()` | Optional | Smallest value |
| `max()` | Optional | Largest value |
| `average()` | OptionalDouble | Mean value |
| `reduce()` | T or Optional | Custom aggregation |

---

## 🔑 Two Approaches to Sum

### Approach 1: mapToDouble() + sum()

```java
double total = transactions.stream()
    .filter(t -> t.getCurrency().equals("INR"))
    .mapToDouble(Transaction::getAmount)  // Stream<Transaction> → DoubleStream
    .sum();                                // Built-in sum
```

**Why mapToDouble?**

- Regular `map()` returns `Stream<Double>` (objects)
- `mapToDouble()` returns `DoubleStream` (primitives)
- `DoubleStream` has built-in `sum()`, `average()`, `max()`, etc.

### Approach 2: reduce()

```java
double total = transactions.stream()
    .filter(t -> t.getCurrency().equals("INR"))
    .map(Transaction::getAmount)
    .reduce(0.0, (a, b) -> a + b);  // Custom accumulation
```

**reduce(identity, accumulator)**

- `identity` = Starting value (0.0 for sum)
- `accumulator` = How to combine two values

```
reduce(0.0, (a, b) -> a + b)

Step 1: 0.0 + 1000.0 = 1000.0
Step 2: 1000.0 + 2500.0 = 3500.0
Step 3: 3500.0 + 800.0 = 4300.0
```

---

## 🔗 Primitive Streams

| Stream | Methods | Use case |
|--------|---------|----------|
| `IntStream` | `sum()`, `average()`, `max()`, `min()` | int values |
| `LongStream` | `sum()`, `average()`, `max()`, `min()` | long values |
| `DoubleStream` | `sum()`, `average()`, `max()`, `min()` | double values |

### Converting to Primitive Stream

```java
// From Stream<T> to primitive stream
.mapToInt(T::getIntField)        // → IntStream
.mapToLong(T::getLongField)      // → LongStream
.mapToDouble(T::getDoubleField)  // → DoubleStream
```

### Back to Object Stream

```java
IntStream.of(1, 2, 3)
         .boxed()  // IntStream → Stream<Integer>
```

---

## 📚 Interview Corner

### Q1: What's the difference between map() and mapToDouble()?

**A:**

```java
// map() → Returns Stream<Double> (boxed objects)
.map(t -> t.getAmount())     // Stream<Double>

// mapToDouble() → Returns DoubleStream (primitives)
.mapToDouble(t -> t.getAmount())  // DoubleStream
```

`DoubleStream` is more efficient and has specialized methods like `sum()`.

### Q2: Explain reduce() with an example

**A:**

```java
// reduce(identity, accumulator)
List<Integer> nums = [1, 2, 3, 4, 5];

int sum = nums.stream()
              .reduce(0, (a, b) -> a + b);  // Sum = 15

int product = nums.stream()
                  .reduce(1, (a, b) -> a * b);  // Product = 120
```

### Q3: What if the stream is empty? What does sum() return?

**A:**

- `sum()` returns **0** (not null, not error)
- `average()` returns **OptionalDouble.empty()**
- `reduce(identity, acc)` returns **identity**

```java
List<Integer> empty = new ArrayList<>();
int sum = empty.stream().mapToInt(x -> x).sum();  // Returns 0
```

### Q4: reduce() with no identity value?

**A:**

```java
// With identity: always returns a value
int sum = nums.reduce(0, Integer::sum);  // Returns int

// Without identity: returns Optional (stream might be empty!)
Optional<Integer> sum = nums.reduce(Integer::sum);  // Returns Optional
```

### Q5: How to get multiple stats at once?

**A:** Use `summaryStatistics()`!

```java
DoubleSummaryStatistics stats = transactions.stream()
    .mapToDouble(Transaction::getAmount)
    .summaryStatistics();

stats.getSum();      // Total
stats.getAverage();  // Mean
stats.getMax();      // Maximum
stats.getMin();      // Minimum
stats.getCount();    // Count
```

---

## 🎯 Key Takeaways

1. Use `mapToDouble/Int/Long` for numeric operations (more efficient)
2. `sum()` returns 0 for empty streams (safe!)
3. `reduce()` is the most flexible - can do any aggregation
4. `reduce(identity, accumulator)` - identity is starting value
5. Use `summaryStatistics()` to get multiple stats at once
6. `average()` returns `OptionalDouble` (might be empty)
7. All aggregation operations are **TERMINAL**

---

## 🏃 Try It Yourself

1. Find average salary of all employees
2. Find maximum transaction amount
3. Count number of orders with status "DELIVERED"
4. Calculate total price of items in shopping cart
