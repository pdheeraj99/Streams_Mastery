package streams.mastery.problem11;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Problem 11: Find Duplicates in List
 * 
 * See: 1_Problem.md → The raw question
 * See: 2_Thinking.md → Different approaches compared
 * See: 3_Solution.md → Deep dive + variations
 */
public class Solution {

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 2, 4, 3, 5, 1, 6);

        System.out.println("=== Problem 11: Find Duplicates ===\n");
        System.out.println("Input: " + numbers);

        // Approach A: groupingBy + counting
        System.out.println("\n--- Approach A: groupingBy + counting ---");
        List<Integer> result1 = findDuplicatesA(numbers);
        System.out.println("Duplicates: " + result1);

        // Approach B: Set.add trick
        System.out.println("\n--- Approach B: Set.add() trick ---");
        List<Integer> result2 = findDuplicatesB(numbers);
        System.out.println("Duplicates: " + result2);

        // Variations
        System.out.println("\n--- Variations ---");
        variations(numbers);

        // Edge cases
        System.out.println("\n--- Edge Cases ---");
        testEdgeCases();
    }

    /**
     * Approach A: Count and filter
     * See 3_Solution.md: "Solution A: groupingBy + counting"
     */
    public static List<Integer> findDuplicatesA(List<Integer> numbers) {
        return numbers.stream()
                // Step 1: Count each element
                .collect(Collectors.groupingBy(
                        Function.identity(), // Element itself as key
                        Collectors.counting() // Count as value
                ))
                // Step 2: Filter count > 1
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                // Step 3: Get just the elements
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Approach B: Clever Set.add trick
     * See 3_Solution.md: "Solution B: Set.add() Trick"
     */
    public static List<Integer> findDuplicatesB(List<Integer> numbers) {
        Set<Integer> seen = new HashSet<>();

        return numbers.stream()
                .filter(n -> !seen.add(n)) // add() returns false if already exists!
                .distinct() // Avoid same duplicate appearing multiple times
                .collect(Collectors.toList());
    }

    /**
     * Different variations of the problem
     */
    public static void variations(List<Integer> numbers) {

        // Variation 1: Duplicates with their count
        System.out.println("\n1. Duplicates with count:");
        Map<Integer, Long> duplicatesWithCount = numbers.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println("   " + duplicatesWithCount);

        // Variation 2: First duplicate
        System.out.println("\n2. First duplicate:");
        Set<Integer> seen = new HashSet<>();
        Optional<Integer> firstDuplicate = numbers.stream()
                .filter(n -> !seen.add(n))
                .findFirst();
        System.out.println("   " + firstDuplicate.orElse(-1));

        // Variation 3: Unique only (appearing exactly once)
        System.out.println("\n3. Unique only (count = 1):");
        List<Integer> uniqueOnly = numbers.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() == 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        System.out.println("   " + uniqueOnly);

        // Variation 4: Preserve order (LinkedHashMap)
        System.out.println("\n4. Duplicates with preserved order:");
        List<Integer> orderedDupes = numbers.stream()
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        LinkedHashMap::new, // Preserves insertion order!
                        Collectors.counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        System.out.println("   " + orderedDupes);

        // Variation 5: With Strings
        System.out.println("\n5. String duplicates:");
        List<String> words = Arrays.asList("apple", "banana", "apple", "cherry", "banana", "date");
        List<String> dupWords = findDuplicatesGeneric(words);
        System.out.println("   Input: " + words);
        System.out.println("   Duplicates: " + dupWords);
    }

    /**
     * Generic version for any type
     */
    public static <T> List<T> findDuplicatesGeneric(List<T> items) {
        return items.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static void testEdgeCases() {

        // Empty list
        System.out.println("\n1. Empty list:");
        List<Integer> result1 = findDuplicatesA(Collections.emptyList());
        System.out.println("   Result: " + result1);

        // No duplicates
        System.out.println("\n2. No duplicates:");
        List<Integer> result2 = findDuplicatesA(Arrays.asList(1, 2, 3, 4, 5));
        System.out.println("   Input: [1,2,3,4,5]");
        System.out.println("   Result: " + result2);

        // All same
        System.out.println("\n3. All same element:");
        List<Integer> result3 = findDuplicatesA(Arrays.asList(5, 5, 5, 5));
        System.out.println("   Input: [5,5,5,5]");
        System.out.println("   Result: " + result3);

        // Element appearing 3+ times
        System.out.println("\n4. Element appearing 3+ times:");
        List<Integer> result4 = findDuplicatesA(Arrays.asList(1, 1, 1, 2, 2));
        System.out.println("   Input: [1,1,1,2,2]");
        System.out.println("   Result: " + result4);
    }
}
