# Problem 16: Build Custom Collectors

## 📋 The Interview Question

> "The built-in collectors don't always fit your needs.  
> Create a **custom collector** that collects elements into a comma-separated string with a prefix and suffix."

**Example:**

```java
List<String> names = Arrays.asList("Ram", "Sita", "Hanuman");

// Should produce: "[Ram, Sita, Hanuman]"
String result = names.stream().collect(customJoiningCollector());
```

---

## 🎯 Why Custom Collectors?

1. Built-in collectors don't cover every use case
2. Reusable across your codebase
3. Shows **deep** understanding of Streams API
4. Common interview question for senior roles!

---

## 📥 What We'll Build

1. **Basic custom collector** - Join with delimiter
2. **Statistics collector** - Calculate multiple stats in one pass
3. **Top N collector** - Keep only top N elements
4. **Conditional collector** - Collect based on condition

---

## ⚠️ Prerequisites

Understanding of:

- `Supplier` - Creates accumulator
- `BiConsumer` - Adds element to accumulator
- `BinaryOperator` - Combines two accumulators (for parallel)
- `Function` - Transforms final result
