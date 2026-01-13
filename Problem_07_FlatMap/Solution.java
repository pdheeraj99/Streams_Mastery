package streams.mastery.problem07;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Problem 7: Flatten Nested Orders
 * 
 * See: 1_Problem.md   → The raw question
 * See: 2_Thinking.md  → Why we chose flatMap
 * See: 3_Solution.md  → Deep dive into flatMap concept
 */
public class Solution {

    public static void main(String[] args) {
        List<Customer> customers = Arrays.asList(
            new Customer("C1", "Ravi", Arrays.asList(
                new Order("ORD-001", 1500),
                new Order("ORD-002", 2500)
            )),
            new Customer("C2", "Priya", Arrays.asList(
                new Order("ORD-003", 800),
                new Order("ORD-004", 3200),
                new Order("ORD-005", 1100)
            )),
            new Customer("C3", "Arjun", Arrays.asList(
                new Order("ORD-006", 4500)
            )),
            new Customer("C4", "Empty", Collections.emptyList())  // Edge case!
        );
        
        System.out.println("=== Problem 7: Flatten Nested Orders ===\n");
        System.out.println("Customers with their orders:");
        customers.forEach(c -> System.out.println("  " + c));
        
        // Without Streams
        System.out.println("\n--- Without Streams ---");
        List<String> result1 = withoutStreams(customers);
        System.out.println("Order IDs: " + result1);
        
        // With Streams - flatMap
        System.out.println("\n--- With Streams (flatMap) ---");
        List<String> result2 = withStreams(customers);
        System.out.println("Order IDs: " + result2);
        
        // More examples
        System.out.println("\n--- More flatMap Examples ---");
        moreExamples(customers);
    }

    /**
     * Traditional approach - nested loops
     */
    public static List<String> withoutStreams(List<Customer> customers) {
        List<String> orderIds = new ArrayList<>();
        
        for (Customer customer : customers) {
            for (Order order : customer.getOrders()) {
                orderIds.add(order.getId());
            }
        }
        
        return orderIds;
    }

    /**
     * Stream approach using flatMap()
     * 
     * See 3_Solution.md: "map() vs flatMap()"
     * 
     * Pipeline:
     * 1. stream()   → Stream<Customer>
     * 2. flatMap()  → Customer → Stream<Order> → all flattened to Stream<Order>
     * 3. map()      → Order → String (ID)
     * 4. collect()  → List<String>
     */
    public static List<String> withStreams(List<Customer> customers) {
        return customers.stream()
            // flatMap: Each customer → their orders (flattened)
            // See 3_Solution.md: "Box Analogy"
            .flatMap(customer -> customer.getOrders().stream())
            // Now we have Stream<Order>, extract IDs
            .map(Order::getId)
            .collect(Collectors.toList());
    }

    public static void moreExamples(List<Customer> customers) {
        
        // Example 1: Get ALL orders (not just IDs)
        System.out.println("\n1. All orders (objects):");
        List<Order> allOrders = customers.stream()
                                         .flatMap(c -> c.getOrders().stream())
                                         .collect(Collectors.toList());
        allOrders.forEach(o -> System.out.println("   " + o));
        
        // Example 2: Total amount across ALL orders
        // See 3_Solution.md: Pattern with aggregation
        System.out.println("\n2. Total amount of all orders:");
        double total = customers.stream()
                                .flatMap(c -> c.getOrders().stream())
                                .mapToDouble(Order::getAmount)
                                .sum();
        System.out.println("   Total: " + total);
        
        // Example 3: Filter after flatten - orders > 2000
        System.out.println("\n3. Orders with amount > 2000:");
        customers.stream()
                 .flatMap(c -> c.getOrders().stream())
                 .filter(o -> o.getAmount() > 2000)
                 .forEach(o -> System.out.println("   " + o));
        
        // Example 4: Flatten simple nested list
        System.out.println("\n4. Flatten List<List<Integer>>:");
        List<List<Integer>> nested = Arrays.asList(
            Arrays.asList(1, 2, 3),
            Arrays.asList(4, 5),
            Arrays.asList(6, 7, 8, 9)
        );
        List<Integer> flat = nested.stream()
                                   .flatMap(Collection::stream)
                                   .collect(Collectors.toList());
        System.out.println("   Nested: " + nested);
        System.out.println("   Flat:   " + flat);
        
        // Example 5: Split strings into words
        System.out.println("\n5. Split sentences into words:");
        List<String> sentences = Arrays.asList(
            "Hello World",
            "Java Streams",
            "flatMap is powerful"
        );
        List<String> words = sentences.stream()
                                      .flatMap(s -> Arrays.stream(s.split(" ")))
                                      .collect(Collectors.toList());
        System.out.println("   Sentences: " + sentences);
        System.out.println("   Words: " + words);
        
        // Example 6: Handle potential null orders
        // See 3_Solution.md: "Empty Collections Handling"
        System.out.println("\n6. Safe flatMap (handle null):");
        List<Customer> customersWithNull = Arrays.asList(
            new Customer("C1", "Test", Arrays.asList(new Order("O1", 100))),
            new Customer("C2", "Null Orders", null)  // null orders!
        );
        List<String> safeResult = customersWithNull.stream()
            .flatMap(c -> c.getOrders() == null 
                          ? Stream.empty() 
                          : c.getOrders().stream())
            .map(Order::getId)
            .collect(Collectors.toList());
        System.out.println("   Safe result: " + safeResult);
    }
}

/**
 * Customer with nested Orders
 */
class Customer {
    private String id;
    private String name;
    private List<Order> orders;
    
    public Customer(String id, String name, List<Order> orders) {
        this.id = id;
        this.name = name;
        this.orders = orders != null ? orders : Collections.emptyList();
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
    public List<Order> getOrders() { return orders; }
    
    @Override
    public String toString() {
        return name + " (" + orders.size() + " orders)";
    }
}

/**
 * Order class
 */
class Order {
    private String id;
    private double amount;
    
    public Order(String id, double amount) {
        this.id = id;
        this.amount = amount;
    }
    
    public String getId() { return id; }
    public double getAmount() { return amount; }
    
    @Override
    public String toString() {
        return id + " - " + amount;
    }
}
