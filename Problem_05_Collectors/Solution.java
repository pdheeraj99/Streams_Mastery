package streams.mastery.problem05;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Problem 5: Collect Orders in Different Ways
 * See: concept.md for detailed explanation
 */
public class Solution {

    public static void main(String[] args) {
        List<Order> orders = Arrays.asList(
                new Order("ORD-001", "Laptop", 75000),
                new Order("ORD-002", "Mouse", 500),
                new Order("ORD-003", "Laptop", 75000), // Duplicate product
                new Order("ORD-004", "Keyboard", 2000));

        System.out.println("=== Problem 5: Collect Orders in Different Ways ===\n");
        System.out.println("Orders:");
        orders.forEach(System.out::println);

        // Collect to List
        System.out.println("\n--- 1. Collect to List ---");
        collectToList(orders);

        // Collect to Set (unique)
        System.out.println("\n--- 2. Collect to Set (Unique) ---");
        collectToSet(orders);

        // Collect to Map
        System.out.println("\n--- 3. Collect to Map ---");
        collectToMap(orders);

        // Joining strings
        System.out.println("\n--- 4. Joining Strings ---");
        joiningExamples(orders);

        // More examples
        System.out.println("\n--- 5. More Collector Examples ---");
        moreExamples(orders);
    }

    /**
     * toList() - Ordered collection with duplicates
     */
    public static void collectToList(List<Order> orders) {
        // Collect order IDs into a List
        List<String> orderIds = orders.stream()
                .map(Order::getId)
                .collect(Collectors.toList());
        System.out.println("Order IDs: " + orderIds);

        // Product names (includes duplicates)
        List<String> products = orders.stream()
                .map(Order::getProduct)
                .collect(Collectors.toList());
        System.out.println("Products (with duplicates): " + products);
    }

    /**
     * toSet() - Unique elements only
     * See concept.md: Q1 about toList vs toSet
     */
    public static void collectToSet(List<Order> orders) {
        // Unique product names
        Set<String> uniqueProducts = orders.stream()
                .map(Order::getProduct)
                .collect(Collectors.toSet());
        System.out.println("Unique products: " + uniqueProducts);

        // Using TreeSet for sorted unique products
        // See concept.md: Q2 about toCollection
        Set<String> sortedProducts = orders.stream()
                .map(Order::getProduct)
                .collect(Collectors.toCollection(TreeSet::new));
        System.out.println("Sorted unique products: " + sortedProducts);
    }

    /**
     * toMap() - Create lookup map
     * See concept.md: toMap and duplicate key handling
     */
    public static void collectToMap(List<Order> orders) {
        // Map: orderId → Order object
        // See concept.md: Q6 about Function.identity()
        Map<String, Order> orderMap = orders.stream()
                .collect(Collectors.toMap(
                        Order::getId,
                        Function.identity()));
        System.out.println("Order map: " + orderMap);
        System.out.println("Lookup ORD-002: " + orderMap.get("ORD-002"));

        // Map: orderId → price
        Map<String, Double> priceMap = orders.stream()
                .collect(Collectors.toMap(
                        Order::getId,
                        Order::getPrice));
        System.out.println("Price map: " + priceMap);

        // GOTCHA: Duplicate keys!
        // Map: product → price (Laptop appears twice!)
        // See concept.md: "toMap() Gotcha"
        System.out.println("\nHandling duplicate keys:");
        Map<String, Double> productPriceMap = orders.stream()
                .collect(Collectors.toMap(
                        Order::getProduct,
                        Order::getPrice,
                        (existing, replacement) -> existing // Keep first
                ));
        System.out.println("Product → Price (kept first): " + productPriceMap);

        // Alternative: sum prices for duplicate products
        Map<String, Double> summedPrices = orders.stream()
                .collect(Collectors.toMap(
                        Order::getProduct,
                        Order::getPrice,
                        Double::sum // Sum duplicates
                ));
        System.out.println("Product → Price (summed): " + summedPrices);
    }

    /**
     * joining() - Concatenate strings
     * See concept.md: "joining()"
     */
    public static void joiningExamples(List<Order> orders) {
        // Simple join (no delimiter)
        String simple = orders.stream()
                .map(Order::getProduct)
                .collect(Collectors.joining());
        System.out.println("Simple join: " + simple);

        // With delimiter
        String withDelimiter = orders.stream()
                .map(Order::getProduct)
                .collect(Collectors.joining(", "));
        System.out.println("With delimiter: " + withDelimiter);

        // With delimiter, prefix, suffix
        String formatted = orders.stream()
                .map(Order::getProduct)
                .collect(Collectors.joining(", ", "[", "]"));
        System.out.println("Formatted: " + formatted);

        // Join non-strings: first map to String!
        // See concept.md: Q5
        String prices = orders.stream()
                .map(o -> String.valueOf(o.getPrice()))
                .collect(Collectors.joining(" | "));
        System.out.println("Prices joined: " + prices);
    }

    public static void moreExamples(List<Order> orders) {
        // Collect to specific collection type
        System.out.println("\n5a. Collect to LinkedList:");
        LinkedList<String> linkedList = orders.stream()
                .map(Order::getId)
                .collect(Collectors.toCollection(LinkedList::new));
        System.out.println("LinkedList: " + linkedList);

        // Count with collector
        System.out.println("\n5b. Counting collector:");
        long count = orders.stream()
                .collect(Collectors.counting());
        System.out.println("Count: " + count);

        // Summing with collector
        System.out.println("\n5c. Summing collector:");
        double totalPrice = orders.stream()
                .collect(Collectors.summingDouble(Order::getPrice));
        System.out.println("Total price: " + totalPrice);

        // Averaging
        System.out.println("\n5d. Averaging collector:");
        double avgPrice = orders.stream()
                .collect(Collectors.averagingDouble(Order::getPrice));
        System.out.println("Average price: " + avgPrice);

        // Collect to LinkedHashMap (maintains insertion order)
        System.out.println("\n5e. LinkedHashMap (ordered):");
        Map<String, Order> orderedMap = orders.stream()
                .collect(Collectors.toMap(
                        Order::getId,
                        Function.identity(),
                        (o1, o2) -> o1,
                        LinkedHashMap::new));
        System.out.println("Ordered map keys: " + orderedMap.keySet());
    }
}

/**
 * Custom Order class
 */
class Order {
    private String id;
    private String product;
    private double price;

    public Order(String id, String product, double price) {
        this.id = id;
        this.product = product;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getProduct() {
        return product;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %.0f", id, product, price);
    }
}
