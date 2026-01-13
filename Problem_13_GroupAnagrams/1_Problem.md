# Problem 13: Group Anagrams

## 📋 The Interview Question (VERY COMMON! 🔥)

> "Given a list of words, group them by anagrams.  
> Anagrams are words with same letters in different order."

---

## 📥 Input

```java
List<String> words = Arrays.asList(
    "eat", "tea", "tan", "ate", "nat", "bat", "tab", "ant"
);
```

---

## 📤 Expected Output

```java
[
    ["eat", "tea", "ate"],     // e, a, t
    ["tan", "nat", "ant"],     // t, a, n
    ["bat", "tab"]             // b, a, t
]
```

---

## 🧠 What Makes Words Anagrams?

```
"eat" and "tea":
  e-a-t  →  sort  →  a-e-t
  t-e-a  →  sort  →  a-e-t
  
  Same sorted form = ANAGRAMS!
```

---

## 🎯 Variations

1. "Check if two words are anagrams" (simpler)
2. "Find all anagram pairs"
3. "Find largest anagram group"

---

## ⚠️ Edge Cases

1. Empty strings
2. Single character words
3. No anagrams exist
4. Case sensitivity ("Eat" vs "eat")?
