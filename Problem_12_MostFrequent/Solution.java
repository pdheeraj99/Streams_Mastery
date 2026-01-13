package streams.mastery.problem12;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Problem 12: Find Most Frequent Element
 * 
 * See: 1_Problem.md → The raw question
 * See: 2_Thinking.md → Different approaches
 * See: 3_Solution.md → Deep dive + variations
 */
public class Solution {

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 3, 2, 3, 3, 2, 1, 3);

        System.out.println("=== Problem 12: Most Frequent Element ===\n");
        System.out.println("Input: " + numbers);

        // Count each element first (for visualization)
        Map<Integer, Long> counts = numbers.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println("Counts: " + counts);

        // Main solution
        System.out.println("\n--- Most Frequent ---");
        Optional<Integer> mostFrequent = findMostFrequent(numbers);
        System.out.println("Result: " + mostFrequent.orElse(-1));

        // Variations
        System.out.println("\n--- Variations ---");
        variations();

        // Edge cases
        System.out.println("\n--- Edge Cases ---");
        testEdgeCases();
    }

    /**
     * Main solution: groupingBy + counting + max
     * See 3_Solution.md
     */
    public static Optional<Integer> findMostFrequent(List<Integer> numbers) {
        return numbers.stream()
                // Step 1: Count each element
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()))
                // Step 2: Find entry with max count
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                // Step 3: Extract the key
                .map(Map.Entry::getKey);
    }

    public static void variations() {
        List<Integer> numbers = Arrays.asList(1, 2, 2, 3, 3, 4); // Tie: 2 and 3 both appear twice

        // Variation 1: Return ALL with highest frequency
        System.out.println("\n1. Handle ties (all most frequent):");
        System.out.println("   Input: " + numbers);
        List<Integer> allMostFrequent = findAllMostFrequent(numbers);
        System.out.println("   All with max count: " + allMostFrequent);

        // Variation 2: Kth most frequent
        System.out.println("\n2. Kth most frequent:");
        List<Integer> nums = Arrays.asList(1, 1, 1, 2, 2, 2, 2, 3, 3);
        System.out.println("   Input: " + nums);
        System.out.println("   Counts: {1=3, 2=4, 3=2}");
        System.out.println("   1st most frequent: " + findKthMostFrequent(nums, 1).orElse(-1));
        System.out.println("   2nd most frequent: " + findKthMostFrequent(nums, 2).orElse(-1));
        System.out.println("   3rd most frequent: " + findKthMostFrequent(nums, 3).orElse(-1));

        // Variation 3: Least frequent
        System.out.println("\n3. Least frequent:");
        Optional<Integer> leastFreq = findLeastFrequent(nums);
        System.out.println("   Least frequent: " + leastFreq.orElse(-1));

        // Variation 4: Most frequent word
        System.out.println("\n4. Most frequent word:");
        String sentence = "the quick brown fox jumps over the lazy dog the";
        System.out.println("   Sentence: \"" + sentence + "\"");
        Optional<String> mostFreqWord = mostFrequentWord(sentence);
        System.out.println("   Most frequent: " + mostFreqWord.orElse("none"));

        // Variation 5: With count (Entry, not just key)
        System.out.println("\n5. Most frequent WITH count:");
        Optional<Map.Entry<Integer, Long>> withCount = findMostFrequentWithCount(nums);
        withCount.ifPresent(e -> System.out.println("   Element: " + e.getKey() + ", Count: " + e.getValue()));
    }

    /**
     * Variation 1: Handle ties - return ALL most frequent
     */
    public static List<Integer> findAllMostFrequent(List<Integer> numbers) {
        Map<Integer, Long> counts = numbers.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        long maxCount = counts.values().stream()
                .max(Long::compare)
                .orElse(0L);

        return counts.entrySet().stream()
                .filter(e -> e.getValue() == maxCount)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Variation 2: Kth most frequent
     */
    public static Optional<Integer> findKthMostFrequent(List<Integer> numbers, int k) {
        return numbers.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .skip(k - 1)
                .findFirst()
                .map(Map.Entry::getKey);
    }

    /**
     * Variation 3: Least frequent
     */
    public static Optional<Integer> findLeastFrequent(List<Integer> numbers) {
        return numbers.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .min(Map.Entry.comparingByValue()) // min instead of max!
                .map(Map.Entry::getKey);
    }

    /**
     * Variation 4: Most frequent word in sentence
     */
    public static Optional<String> mostFrequentWord(String sentence) {
        return Arrays.stream(sentence.toLowerCase().split("\\s+"))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    /**
     * Variation 5: Return Entry (element + count)
     */
    public static Optional<Map.Entry<Integer, Long>> findMostFrequentWithCount(List<Integer> numbers) {
        return numbers.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue());
    }

    public static void testEdgeCases() {
        // Empty list
        System.out.println("\n1. Empty list:");
        Optional<Integer> result1 = findMostFrequent(Collections.emptyList());
        System.out.println("   Result: " + result1.orElse(-1) + " (empty optional)");

        // All same
        System.out.println("\n2. All same element:");
        Optional<Integer> result2 = findMostFrequent(Arrays.asList(5, 5, 5));
        System.out.println("   Input: [5,5,5]");
        System.out.println("   Result: " + result2.orElse(-1));

        // All unique
        System.out.println("\n3. All unique (tie at count=1):");
        List<Integer> allUnique = Arrays.asList(1, 2, 3, 4);
        Optional<Integer> result3 = findMostFrequent(allUnique);
        System.out.println("   Input: [1,2,3,4]");
        System.out.println("   Result: " + result3.orElse(-1) + " (any one of them)");

        // Single element
        System.out.println("\n4. Single element:");
        Optional<Integer> result4 = findMostFrequent(Arrays.asList(42));
        System.out.println("   Input: [42]");
        System.out.println("   Result: " + result4.orElse(-1));
    }
}
