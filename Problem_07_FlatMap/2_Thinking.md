# Problem 7: Thinking Process

## 1️⃣ Don't Panic! Let's Understand

### What is the INPUT?

- `List<Customer>` where each Customer has `List<Order>`
- **Nested structure:** List inside List!

### What is the OUTPUT?

- `List<String>` - flat list of order IDs
- **Single level:** No nesting!

### What is the CORE action?

```
BEFORE:  [ [ORD-1, ORD-2], [ORD-3, ORD-4, ORD-5], [ORD-6] ]
AFTER:   [ ORD-1, ORD-2, ORD-3, ORD-4, ORD-5, ORD-6 ]
```

**This is FLATTENING!** 📦 → 📄📄📄

---

## 2️⃣ The Problem with map()

Let's try with normal `map()`:

```java
customers.stream()
         .map(Customer::getOrders)  // Each customer → List<Order>
         .collect(toList());

// Result: List<List<Order>> 😱
// Still nested!
```

**Why?**

```
Customer 1 ──map()──→ [Order1, Order2]     ┐
Customer 2 ──map()──→ [Order3, Order4, Order5]  ├── List of Lists!
Customer 3 ──map()──→ [Order6]             ┘
```

**map() gives 1→1: One customer → One list**  
**We need 1→Many: One customer → Multiple orders (flattened)**

---

## 3️⃣ Brainstorming Approaches

### Approach A: Nested for loops (Traditional)

```java
List<String> orderIds = new ArrayList<>();
for (Customer c : customers) {
    for (Order o : c.getOrders()) {
        orderIds.add(o.getId());
    }
}
```

**Verdict:** ✅ Works, but verbose. Not functional style.

---

### Approach B: map() + flatMap() combo (Wrong thinking)

```java
customers.stream()
         .map(Customer::getOrders)     // Stream<List<Order>>
         .flatMap(List::stream)         // Stream<Order>
         .map(Order::getId)             // Stream<String>
         .collect(toList());
```

**Verdict:** ✅ Works! But can we do it in one step?

---

### Approach C: Direct flatMap() 🎯

```java
customers.stream()
         .flatMap(c -> c.getOrders().stream())  // Customer → Stream<Order>
         .map(Order::getId)
         .collect(toList());
```

**How it works:**

```
Customer 1 ──flatMap──→ Order1, Order2 ─────────────┐
Customer 2 ──flatMap──→ Order3, Order4, Order5 ─────┼──→ Single Stream!
Customer 3 ──flatMap──→ Order6 ─────────────────────┘
```

**Verdict:** ✅ Clean! One flatMap does the flattening!

---

### Approach D: flatMap with method reference

```java
customers.stream()
         .map(Customer::getOrders)
         .flatMap(Collection::stream)
         .map(Order::getId)
         .collect(toList());
```

**Verdict:** ✅ Also works! Slightly more steps but readable.

---

## 4️⃣ Trade-offs Comparison

| Approach | Lines | Readability | When to Use |
|----------|-------|-------------|-------------|
| A: Nested loops | 5+ | Medium | Legacy code |
| B: map + flatMap | 4 | Good | When mapping first |
| C: Direct flatMap | 3 | Best ⭐ | Usually preferred |
| D: Method refs | 4 | Good | When method refs apply |

---

## 5️⃣ My Decision: Approach C

**Why?**

1. ✅ Single flatMap handles the nesting
2. ✅ Clean lambda: `c -> c.getOrders().stream()`
3. ✅ Easy to read: "For each customer, stream their orders"
4. ✅ Handles empty order lists automatically!

---

## 6️⃣ Concepts I Need to Master

1. **flatMap()** - How it flattens nested streams
2. **map vs flatMap** - When to use which
3. **Handling empty/null nested collections**

👉 **See 3_Solution.md for deep dive!**
