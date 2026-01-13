package streams.mastery.problem06;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Problem 6: Sort Products and Remove Duplicates
 * 
 * See: 1_Problem.md → The raw question
 * See: 2_Thinking.md → Why we chose this approach
 * See: 3_Solution.md → Deep dive into concepts used
 */
public class Solution {

    public static void main(String[] args) {
        List<Product> products = Arrays.asList(
                new Product("Phone", 50000),
                new Product("Laptop", 75000),
                new Product("Phone", 60000), // Duplicate - higher price
                new Product("Mouse", 500),
                new Product("Laptop", 80000), // Duplicate - higher price
                new Product("Keyboard", 2000));

        System.out.println("=== Problem 6: Sort & Remove Duplicates ===\n");
        System.out.println("Input products:");
        products.forEach(System.out::println);

        // Without Streams
        System.out.println("\n--- Without Streams ---");
        List<Product> result1 = withoutStreams(products);
        System.out.println("Result:");
        result1.forEach(System.out::println);

        // With Streams (our chosen approach)
        System.out.println("\n--- With Streams (Our Approach) ---");
        List<Product> result2 = withStreams(products);
        System.out.println("Result:");
        result2.forEach(System.out::println);

        // Alternative approaches demonstration
        System.out.println("\n--- Alternative Approaches ---");
        alternativeApproaches(products);
    }

    /**
     * Traditional approach without streams
     */
    public static List<Product> withoutStreams(List<Product> products) {
        // Step 1: Sort by price descending
        List<Product> sorted = new ArrayList<>(products);
        sorted.sort((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));

        // Step 2: Keep only first occurrence of each name
        Map<String, Product> seen = new LinkedHashMap<>();
        for (Product p : sorted) {
            if (!seen.containsKey(p.getName())) {
                seen.put(p.getName(), p);
            }
        }

        return new ArrayList<>(seen.values());
    }

    /**
     * Stream approach - Our chosen solution
     * 
     * Pipeline:
     * 1. sorted() → descending by price
     * 2. toMap() → name as key, keep first (highest price)
     * 3. values() → get the products
     * 
     * See 3_Solution.md for detailed explanation of each concept
     */
    public static List<Product> withStreams(List<Product> products) {
        return products.stream()
                // Sort by price descending
                // See 3_Solution.md: "Comparator" section
                .sorted(Comparator.comparing(Product::getPrice).reversed())

                // Collect to LinkedHashMap, keeping first of duplicates
                // See 3_Solution.md: "toMap() with Merge Function" section
                .collect(Collectors.toMap(
                        Product::getName, // Key: product name
                        Function.identity(), // Value: product itself
                        (first, second) -> first, // Merge: keep first (highest)
                        LinkedHashMap::new // Preserve insertion order
                ))

                // Get values and convert to List
                .values()
                .stream()
                .collect(Collectors.toList());
    }

    /**
     * Alternative approaches from 2_Thinking.md
     */
    public static void alternativeApproaches(List<Product> products) {

        // Approach C: groupingBy + maxBy
        // More verbose but clear intent
        System.out.println("\nApproach C (groupingBy + maxBy):");

        List<Product> resultC = products.stream()
                .collect(Collectors.groupingBy(
                        Product::getName,
                        Collectors.maxBy(Comparator.comparing(Product::getPrice))))
                .values()
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(Comparator.comparing(Product::getPrice).reversed())
                .collect(Collectors.toList());

        resultC.forEach(System.out::println);

        // Approach D: toMap with explicit comparison
        // No pre-sorting needed
        System.out.println("\nApproach D (toMap with price comparison):");

        List<Product> resultD = products.stream()
                .collect(Collectors.toMap(
                        Product::getName,
                        Function.identity(),
                        (p1, p2) -> p1.getPrice() > p2.getPrice() ? p1 : p2 // Explicit comparison
                ))
                .values()
                .stream()
                .sorted(Comparator.comparing(Product::getPrice).reversed()) // Sort at end
                .collect(Collectors.toList());

        resultD.forEach(System.out::println);
    }
}

/**
 * Product class - Simple POJO for this problem
 */
class Product {
    private String name;
    private double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%s - %.0f", name, price);
    }
}
