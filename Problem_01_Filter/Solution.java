package streams.mastery.problem01;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Problem 1: Find Even Numbers from a List.
 * See: concept.md for detailed explanation
 */
public class Solution {

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        System.out.println("=== Problem 1: Find Even Numbers ===\n");
        System.out.println("Input: " + numbers);

        // Without Streams
        System.out.println("\n--- Without Streams ---");
        List<Integer> result1 = findEvenWithoutStreams(numbers);
        System.out.println("Output: " + result1);

        // With Streams
        System.out.println("\n--- With Streams ---");
        List<Integer> result2 = findEvenWithStreams(numbers);
        System.out.println("Output: " + result2);

        // More examples
        System.out.println("\n--- More Examples ---");
        moreExamples();
    }

    /**
     * Traditional approach using for-loop
     * 
     * Downsides:
     * - Need to manage result list manually
     * - More lines of code
     * - Intent not immediately clear
     */
    public static List<Integer> findEvenWithoutStreams(List<Integer> numbers) {
        List<Integer> evenNumbers = new ArrayList<>();

        for (Integer number : numbers) {
            if (number % 2 == 0) {
                evenNumbers.add(number);
            }
        }

        return evenNumbers;
    }

    /**
     * Stream approach using filter()
     * 
     * See concept.md: "The Stream Operation: filter()"
     * 
     * Pipeline:
     * 1. stream() → Create stream from list
     * 2. filter() → Keep only elements matching condition (Predicate)
     * 3. collect() → Gather results into List
     */
    public static List<Integer> findEvenWithStreams(List<Integer> numbers) {
        return numbers.stream() // Create stream
                .filter(n -> n % 2 == 0) // Predicate: keep if even
                .collect(Collectors.toList()); // Terminal: collect to List
    }

    public static void moreExamples() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 25);

        // Example 1: Numbers greater than 5
        List<Integer> greaterThan5 = numbers.stream()
                .filter(n -> n > 5)
                .collect(Collectors.toList());
        System.out.println("Numbers > 5: " + greaterThan5);

        // Example 2: Divisible by 3
        List<Integer> divisibleBy3 = numbers.stream()
                .filter(n -> n % 3 == 0)
                .collect(Collectors.toList());
        System.out.println("Divisible by 3: " + divisibleBy3);

        // Example 3: Multiple conditions in single filter
        // See concept.md: Q5 about && vs chained filters
        List<Integer> evenAndGreater = numbers.stream()
                .filter(n -> n % 2 == 0 && n > 5)
                .collect(Collectors.toList());
        System.out.println("Even AND > 5: " + evenAndGreater);

        // Example 4: Chained filters (same result, different style)
        List<Integer> chainedFilters = numbers.stream()
                .filter(n -> n % 2 == 0)
                .filter(n -> n > 5)
                .collect(Collectors.toList());
        System.out.println("Chained filters: " + chainedFilters);

        // Example 5: Using method reference
        // When your condition is a single method call, use method reference
        List<Integer> withMethodRef = numbers.stream()
                .filter(Solution::isEven)
                .collect(Collectors.toList());
        System.out.println("Method reference: " + withMethodRef);
    }

    // Helper for method reference example
    public static boolean isEven(int n) {
        return n % 2 == 0;
    }
}
