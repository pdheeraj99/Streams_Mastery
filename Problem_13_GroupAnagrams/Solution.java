package streams.mastery.problem13;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Problem 13: Group Anagrams
 * 
 * VERY COMMON INTERVIEW QUESTION! 🔥
 * 
 * See: 1_Problem.md → The raw question
 * See: 2_Thinking.md → Different approaches
 * See: 3_Solution.md → Deep dive + optimizations
 */
public class Solution {

    public static void main(String[] args) {
        List<String> words = Arrays.asList(
                "eat", "tea", "tan", "ate", "nat", "bat", "tab", "ant");

        System.out.println("=== Problem 13: Group Anagrams ===\n");
        System.out.println("Input: " + words);

        Solution solution = new Solution();

        // Approach A: Sort letters
        System.out.println("\n--- Approach A: Sort Letters ---");
        Collection<List<String>> result1 = solution.groupAnagramsSort(words);
        result1.forEach(group -> System.out.println("   " + group));

        // Approach B: Character count
        System.out.println("\n--- Approach B: Character Count ---");
        Collection<List<String>> result2 = solution.groupAnagramsCount(words);
        result2.forEach(group -> System.out.println("   " + group));

        // Variations
        System.out.println("\n--- Variations ---");
        solution.variations(words);

        // Edge cases
        System.out.println("\n--- Edge Cases ---");
        solution.testEdgeCases();
    }

    /**
     * Approach A: Sort letters as grouping key
     * See 3_Solution.md: "Solution A"
     */
    public Collection<List<String>> groupAnagramsSort(List<String> words) {
        return words.stream()
                .collect(Collectors.groupingBy(this::sortLetters))
                .values();
    }

    /**
     * Approach B: Character count as grouping key
     * See 3_Solution.md: "Solution B"
     * More efficient for long words!
     */
    public Collection<List<String>> groupAnagramsCount(List<String> words) {
        return words.stream()
                .collect(Collectors.groupingBy(this::charCountKey))
                .values();
    }

    /**
     * Sort letters to create canonical form
     * "eat" → "aet", "tea" → "aet"
     */
    private String sortLetters(String word) {
        char[] chars = word.toLowerCase().toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    /**
     * Stream-only version of sorting (for demonstration)
     */
    private String sortLettersStream(String word) {
        return word.toLowerCase().chars()
                .sorted()
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.joining());
    }

    /**
     * Character count as key (O(k) vs O(k log k))
     */
    private String charCountKey(String word) {
        int[] count = new int[26];
        for (char c : word.toLowerCase().toCharArray()) {
            count[c - 'a']++;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            if (count[i] > 0) {
                sb.append((char) ('a' + i)).append(count[i]);
            }
        }
        return sb.toString();
    }

    public void variations(List<String> words) {

        // Variation 1: Check if two words are anagrams
        System.out.println("\n1. Are anagrams?");
        System.out.println("   'eat' & 'tea': " + areAnagrams("eat", "tea"));
        System.out.println("   'eat' & 'bat': " + areAnagrams("eat", "bat"));

        // Variation 2: Largest anagram group
        System.out.println("\n2. Largest anagram group:");
        List<String> largest = largestAnagramGroup(words);
        System.out.println("   " + largest);

        // Variation 3: Count anagram groups
        System.out.println("\n3. Number of anagram groups:");
        long groupCount = countAnagramGroups(words);
        System.out.println("   " + groupCount + " groups");

        // Variation 4: Count anagram pairs
        System.out.println("\n4. Anagram pairs count:");
        System.out.println("   Input: " + words);
        long pairCount = countAnagramPairs(words);
        System.out.println("   Pairs: " + pairCount);
        // [eat,tea,ate] = 3 pairs, [tan,nat,ant] = 3 pairs, [bat,tab] = 1 pair
        // Total = 7 pairs

        // Variation 5: Get anagram groups as Map (with key)
        System.out.println("\n5. Anagrams with their sorted key:");
        Map<String, List<String>> withKeys = words.stream()
                .collect(Collectors.groupingBy(this::sortLetters));
        withKeys.forEach((key, group) -> System.out.println("   Key '" + key + "': " + group));
    }

    /**
     * Check if two strings are anagrams
     */
    public boolean areAnagrams(String s1, String s2) {
        return sortLetters(s1).equals(sortLetters(s2));
    }

    /**
     * Find the largest anagram group
     */
    public List<String> largestAnagramGroup(List<String> words) {
        return words.stream()
                .collect(Collectors.groupingBy(this::sortLetters))
                .values().stream()
                .max(Comparator.comparingInt(List::size))
                .orElse(Collections.emptyList());
    }

    /**
     * Count number of anagram groups
     */
    public long countAnagramGroups(List<String> words) {
        return words.stream()
                .collect(Collectors.groupingBy(this::sortLetters))
                .size();
    }

    /**
     * Count total anagram pairs
     * Group of n words has n*(n-1)/2 pairs
     */
    public long countAnagramPairs(List<String> words) {
        return words.stream()
                .collect(Collectors.groupingBy(this::sortLetters, Collectors.counting()))
                .values().stream()
                .mapToLong(count -> count * (count - 1) / 2)
                .sum();
    }

    public void testEdgeCases() {
        // Empty strings
        System.out.println("\n1. Empty strings:");
        List<String> withEmpty = Arrays.asList("", "", "a");
        Collection<List<String>> r1 = groupAnagramsSort(withEmpty);
        r1.forEach(g -> System.out.println("   " + g));

        // Single characters
        System.out.println("\n2. Single characters:");
        List<String> singles = Arrays.asList("a", "b", "a", "c", "b");
        Collection<List<String>> r2 = groupAnagramsSort(singles);
        r2.forEach(g -> System.out.println("   " + g));

        // No anagrams
        System.out.println("\n3. No anagrams:");
        List<String> noAnagrams = Arrays.asList("abc", "def", "ghi");
        Collection<List<String>> r3 = groupAnagramsSort(noAnagrams);
        System.out.println("   Groups: " + r3.size() + " (each word is its own group)");
    }
}
