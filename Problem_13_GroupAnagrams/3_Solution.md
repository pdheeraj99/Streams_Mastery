# Problem 13: Solution Deep Dive

## 📚 Core Insight

```
Anagram Detection = Same characters, different order
                  = Same SORTED string!

"eat" → sort → "aet"  ┐
"tea" → sort → "aet"  ├── Same key = Same group!
"ate" → sort → "aet"  ┘
```

---

## 🎯 Solution A: Sort Letters as Key

```java
public Collection<List<String>> groupAnagrams(List<String> words) {
    return words.stream()
        .collect(Collectors.groupingBy(this::sortLetters))
        .values();
}

private String sortLetters(String word) {
    char[] chars = word.toLowerCase().toCharArray();
    Arrays.sort(chars);
    return new String(chars);
}
```

### How It Works

```
Input: ["eat", "tea", "tan", "ate", "nat", "bat"]
              │
    groupingBy(sortLetters)
              │
              ▼
{
  "aet" → ["eat", "tea", "ate"],
  "ant" → ["tan", "nat"],
  "abt" → ["bat"]
}
              │
    .values()
              │
              ▼
[["eat","tea","ate"], ["tan","nat"], ["bat"]]
```

---

## 🎯 Solution B: Character Count as Key

```java
public Collection<List<String>> groupAnagramsOptimized(List<String> words) {
    return words.stream()
        .collect(Collectors.groupingBy(this::charCountKey))
        .values();
}

private String charCountKey(String word) {
    int[] count = new int[26];
    for (char c : word.toLowerCase().toCharArray()) {
        count[c - 'a']++;
    }
    // Convert count array to string key
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 26; i++) {
        if (count[i] > 0) {
            sb.append((char)('a' + i)).append(count[i]);
        }
    }
    return sb.toString();
}
```

### How It Works

```
"eat" → count: {a:1, e:1, t:1} → key: "a1e1t1"
"tea" → count: {a:1, e:1, t:1} → key: "a1e1t1"

Same key = Same group!
```

---

## 📊 Stream-Only Sorting Approach

```java
// Sort letters entirely with streams
private String sortLettersStream(String word) {
    return word.chars()                          // IntStream of characters
        .mapToObj(c -> (char) c)                 // Stream<Character>
        .sorted()                                 // Sort characters
        .map(String::valueOf)                    // Character → String
        .collect(Collectors.joining());          // Join to single string
}
```

---

## 🎨 Variations

### Variation 1: Check if Two Strings are Anagrams

```java
public boolean areAnagrams(String s1, String s2) {
    return sortLetters(s1).equals(sortLetters(s2));
}
```

### Variation 2: Find Largest Anagram Group

```java
public List<String> largestAnagramGroup(List<String> words) {
    return words.stream()
        .collect(Collectors.groupingBy(this::sortLetters))
        .values().stream()
        .max(Comparator.comparingInt(List::size))
        .orElse(Collections.emptyList());
}
```

### Variation 3: Count Anagram Groups

```java
public long countAnagramGroups(List<String> words) {
    return words.stream()
        .collect(Collectors.groupingBy(this::sortLetters))
        .size();
}
```

### Variation 4: Find Anagram Pairs Count

```java
// If a group has n words, pairs = n*(n-1)/2
public long countAnagramPairs(List<String> words) {
    return words.stream()
        .collect(Collectors.groupingBy(this::sortLetters, Collectors.counting()))
        .values().stream()
        .mapToLong(count -> count * (count - 1) / 2)
        .sum();
}
```

---

## 📚 Interview Q&A

### Q1: Time Complexity?

**A:**

- Solution A: O(n × k log k) where n = word count, k = max word length
- Solution B: O(n × k) - better for long words

### Q2: Space Complexity?

**A:** O(n × k) for storing the groups

### Q3: What if case-sensitive?

**A:** Remove `.toLowerCase()` from the solution

### Q4: What about unicode/special characters?

**A:** Sort approach still works. Frequency approach needs larger array or HashMap.

### Q5: Which approach for interview?

**A:**

1. Start with Sort (A) - easy to explain
2. Mention Frequency (B) as optimization
3. Show you understand trade-offs

---

## 🎯 Key Takeaways

1. **Anagram key = Sorted string** - Core insight!
2. **groupingBy with custom key** - Very powerful pattern
3. **Two approaches**: Sort (simple) vs Count (optimized)
4. **Works with any comparable elements** - Not just strings!
5. **Follow-up ready**: Largest group, pair count, etc.
