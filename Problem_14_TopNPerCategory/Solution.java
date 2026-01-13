package streams.mastery.problem14;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Problem 14: Top 3 Most Expensive Products Per Category
 * 
 * REAL-WORLD INTERVIEW QUESTION! 🔥
 * 
 * See: 1_Problem.md → The raw question
 * See: 2_Thinking.md → Different approaches
 * See: 3_Solution.md → Deep dive + variations
 */
public class Solution {

    public static void main(String[] args) {
        List<Product> products = Arrays.asList(
                new Product("iPhone", "Electronics", 999),
                new Product("Samsung TV", "Electronics", 1299),
                new Product("MacBook", "Electronics", 1999),
                new Product("Dell Laptop", "Electronics", 899),
                new Product("Sony Headphones", "Electronics", 299),

                new Product("Sofa", "Furniture", 599),
                new Product("Dining Table", "Furniture", 799),
                new Product("Office Chair", "Furniture", 349),
                new Product("Bookshelf", "Furniture", 199),

                new Product("Nike Shoes", "Fashion", 150),
                new Product("Levi's Jeans", "Fashion", 80),
                new Product("Ray-Ban", "Fashion", 200));

        Solution solution = new Solution();

        System.out.println("=== Problem 14: Top 3 Per Category ===\n");
        System.out.println("Products:");
        products.forEach(p -> System.out.println("  " + p));

        // Main solution
        System.out.println("\n--- Top 3 Per Category ---");
        Map<String, List<Product>> top3 = solution.topNPerCategory(products, 3);
        top3.forEach((cat, prods) -> {
            System.out.println("\n" + cat + ":");
            prods.forEach(p -> System.out.println("   " + p.getName() + " - $" + p.getPrice()));
        });

        // Variations
        System.out.println("\n--- Variations ---");
        solution.variations(products);
    }

    /**
     * Main solution: Top N per category using collectingAndThen
     * See 3_Solution.md
     */
    public Map<String, List<Product>> topNPerCategory(List<Product> products, int n) {
        return products.stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(Product::getPrice).reversed())
                                        .limit(n)
                                        .collect(Collectors.toList()))));
    }

    public void variations(List<Product> products) {

        // Variation 1: Bottom 3 (Cheapest) per category
        System.out.println("\n1. Bottom 3 (Cheapest) per Category:");
        Map<String, List<Product>> bottom3 = products.stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(Product::getPrice)) // Ascending!
                                        .limit(3)
                                        .collect(Collectors.toList()))));
        bottom3.forEach((cat, prods) -> System.out.println("   " + cat + ": " + prods.stream()
                .map(p -> p.getName()).collect(Collectors.joining(", "))));

        // Variation 2: Top 3 Overall (not per category)
        System.out.println("\n2. Top 3 Overall:");
        List<Product> top3Overall = products.stream()
                .sorted(Comparator.comparing(Product::getPrice).reversed())
                .limit(3)
                .collect(Collectors.toList());
        top3Overall.forEach(p -> System.out.println("   " + p));

        // Variation 3: Sum of Top 3 per category
        System.out.println("\n3. Sum of Top 3 per Category:");
        Map<String, Double> sumTop3 = products.stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(Product::getPrice).reversed())
                                        .limit(3)
                                        .mapToDouble(Product::getPrice)
                                        .sum())));
        sumTop3.forEach((cat, sum) -> System.out.println("   " + cat + ": $" + sum));

        // Variation 4: Average of Top 3 per category
        System.out.println("\n4. Average of Top 3 per Category:");
        Map<String, Double> avgTop3 = products.stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(Product::getPrice).reversed())
                                        .limit(3)
                                        .mapToDouble(Product::getPrice)
                                        .average()
                                        .orElse(0.0))));
        avgTop3.forEach((cat, avg) -> System.out.println("   " + cat + ": $" + String.format("%.2f", avg)));

        // Variation 5: Category with less than N products
        System.out.println("\n5. Top 5 Fashion (only 3 products - graceful handling):");
        Map<String, List<Product>> top5 = topNPerCategory(products, 5);
        List<Product> fashionTop5 = top5.get("Fashion");
        System.out.println("   Fashion has " + fashionTop5.size() + " products (limit was 5)");

        // Variation 6: Product names only (not full objects)
        System.out.println("\n6. Top 3 Product Names per Category:");
        Map<String, List<String>> top3Names = products.stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(Product::getPrice).reversed())
                                        .limit(3)
                                        .map(Product::getName)
                                        .collect(Collectors.toList()))));
        top3Names.forEach((cat, names) -> System.out.println("   " + cat + ": " + names));
    }
}

/**
 * Product class
 */
class Product {
    private String name;
    private String category;
    private double price;

    public Product(String name, String category, double price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - $%.0f", name, category, price);
    }
}
