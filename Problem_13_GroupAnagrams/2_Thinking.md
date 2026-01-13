# Problem 13: Thinking Process

## 1️⃣ The Key Insight! 💡

**How to identify anagrams?**

```
"eat" → sort letters → "aet"
"tea" → sort letters → "aet"
"ate" → sort letters → "aet"

Same sorted form = Same anagram group!
```

**This is a GROUPING problem where the KEY is the sorted word!**

---

## 2️⃣ Brainstorming Approaches

### Approach A: Sort letters as key (Most Common)

```java
words.stream()
    .collect(groupingBy(word -> sortLetters(word)));

// sortLetters("eat") = "aet"
// sortLetters("tea") = "aet"
// Both map to same key!
```

**Verdict:** ✅ Clean! O(k log k) for sorting each word of length k.

---

### Approach B: Character frequency as key

```java
// Instead of sorting, count each letter
"eat" → {a:1, e:1, t:1} → "a1e1t1"
"tea" → {a:1, e:1, t:1} → "a1e1t1"
```

**Verdict:** ✅ O(k) per word. Better for very long words!

---

### Approach C: Prime number product (Clever!)

```java
// Assign prime to each letter: a=2, b=3, c=5, ...
"eat" = 5 * 2 * 53 = 530  (e=5, a=2, t=53)
"tea" = 53 * 5 * 2 = 530

// Unique product for each anagram group!
```

**Verdict:** ⚠️ Clever but overflow risk for long words.

---

## 3️⃣ Trade-offs

| Approach | Time (per word) | Space | Best For |
|----------|-----------------|-------|----------|
| A: Sort | O(k log k) | O(k) | Short words ⭐ |
| B: Frequency | O(k) | O(26) | Long words |
| C: Prime product | O(k) | O(1) | Theory only |

---

## 4️⃣ My Decision: Approach A + mention B

**Interview strategy:**

1. Start with Approach A (sorting) - easy to explain
2. Mention Approach B as optimization for long words
3. Shows you can think about trade-offs!

---

## 5️⃣ Key Concepts

1. **groupingBy with key mapper** - Custom grouping key
2. **String manipulation** - Sorting characters
3. **Trade-off analysis** - Time vs simplicity

👉 **See 3_Solution.md for implementation!**
