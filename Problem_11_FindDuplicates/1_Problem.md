# Problem 11: Find Duplicates in List

## 📋 The Interview Question

> "Given a list, find all elements that appear **more than once**.  
> Return only the duplicate values (not how many times they appear)."

---

## 📥 Input

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 2, 4, 3, 5, 1, 6);
```

---

## 📤 Expected Output

```java
[1, 2, 3]  // These elements appeared more than once
```

---

## 🎯 Variations (Interviewer might ask)

1. "Find duplicates with their count (how many times each appeared)"
2. "Find duplicates in list of Strings/Objects"
3. "Find first duplicate that appears"
4. "Remove duplicates (keep only unique)"

---

## ⚠️ Edge Cases

1. Empty list
2. No duplicates (all unique)
3. All elements are same
4. Preserve order of first occurrence?
