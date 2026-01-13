package streams.mastery.problem02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Problem 2: Transform Employee Names to Uppercase
 * See: concept.md for detailed explanation
 */
public class Solution {

    public static void main(String[] args) {
        List<String> names = Arrays.asList("ram", "sita", "lakshman", "hanuman", "ravana");

        System.out.println("=== Problem 2: Transform Names to Uppercase ===\n");
        System.out.println("Input: " + names);

        // Without Streams
        System.out.println("\n--- Without Streams ---");
        List<String> result1 = transformWithoutStreams(names);
        System.out.println("Output: " + result1);

        // With Streams
        System.out.println("\n--- With Streams ---");
        List<String> result2 = transformWithStreams(names);
        System.out.println("Output: " + result2);

        // More examples
        System.out.println("\n--- More Examples ---");
        moreExamples();
    }

    /**
     * Traditional approach
     * 
     * Notice: Managing loop + result list manually
     * The actual business logic (toUpperCase) is just ONE line!
     */
    public static List<String> transformWithoutStreams(List<String> names) {
        List<String> uppercaseNames = new ArrayList<>();

        for (String name : names) {
            String transformed = name.toUpperCase();
            uppercaseNames.add(transformed);
        }

        return uppercaseNames;
    }

    /**
     * Stream approach using map()
     * 
     * See concept.md: "The Stream Operation: map()"
     * 
     * Pipeline:
     * 1. stream() → Create stream
     * 2. map() → Apply Function to each element
     * 3. collect() → Gather results
     */
    public static List<String> transformWithStreams(List<String> names) {
        return names.stream()
                .map(name -> name.toUpperCase()) // Function: transform each
                .collect(Collectors.toList());
    }

    public static void moreExamples() {

        // Example 1: Double each number
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> doubled = numbers.stream()
                .map(n -> n * 2)
                .collect(Collectors.toList());
        System.out.println("Doubled: " + doubled);

        // Example 2: Type transformation (String → Integer)
        // See concept.md: Q2 about type transformation
        List<String> names = Arrays.asList("Ram", "Sita", "Lakshman");
        List<Integer> lengths = names.stream()
                .map(name -> name.length())
                .collect(Collectors.toList());
        System.out.println("Name lengths: " + lengths);

        // Example 3: Add prefix
        List<String> withPrefix = names.stream()
                .map(name -> "Sri " + name)
                .collect(Collectors.toList());
        System.out.println("With prefix: " + withPrefix);

        // Example 4: Method reference (cleaner)
        // See concept.md: Q3 about method references
        // Lambda: s -> s.toUpperCase()
        // Method reference: String::toUpperCase
        List<String> lowerNames = Arrays.asList("hello", "world");
        List<String> upperNames = lowerNames.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        System.out.println("Method reference: " + upperNames);

        // Example 5: Chain filter + map
        // See concept.md: Q4 about order (filter FIRST for performance)
        List<String> allNames = Arrays.asList("ram", "sita", "lakshman", "hanuman");
        List<String> result = allNames.stream()
                .filter(name -> name.length() > 4) // Filter first!
                .map(String::toUpperCase) // Then transform
                .collect(Collectors.toList());
        System.out.println("Long names uppercase: " + result);
    }
}
