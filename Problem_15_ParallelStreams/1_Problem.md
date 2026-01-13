# Problem 15: Parallel Streams - When and How?

## 📋 The Interview Question

> "Process a large list of transactions.  
> Calculate total amount, average, and count.  
> When should you use **parallel streams** vs sequential?  
> Demonstrate the difference."

---

## 📥 Input

```java
// Generate 1 million transactions
List<Transaction> transactions = generateTransactions(1_000_000);
```

---

## 🎯 Questions to Answer

1. **When** to use parallel streams?
2. **When NOT** to use parallel streams?
3. **How** to convert sequential to parallel?
4. **What are the pitfalls**?

---

## 📤 Expected Understanding

```java
// Sequential
transactions.stream()
    .filter(...)
    .map(...)
    .collect(...);

// Parallel
transactions.parallelStream()  // OR .stream().parallel()
    .filter(...)
    .map(...)
    .collect(...);
```

---

## ⚠️ Interview Traps

1. "Parallel is always faster" - FALSE!
2. "Parallel maintains order" - DEPENDS!
3. "Parallel is thread-safe" - NOT ALWAYS!
4. "Use parallel for small data" - BAD IDEA!
