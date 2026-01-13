package streams.mastery.problem04;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;

/**
 * Problem 4: Calculate Total INR Transactions
 * See: concept.md for detailed explanation
 */
public class Solution {

    public static void main(String[] args) {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("T1", 1000.0, "INR"),
                new Transaction("T2", 500.0, "USD"),
                new Transaction("T3", 2500.0, "INR"),
                new Transaction("T4", 800.0, "INR"),
                new Transaction("T5", 1200.0, "EUR"));

        System.out.println("=== Problem 4: Calculate Total INR Transactions ===\n");
        System.out.println("Transactions:");
        transactions.forEach(System.out::println);

        // Without Streams
        System.out.println("\n--- Without Streams ---");
        double result1 = sumWithoutStreams(transactions);
        System.out.println("Total INR: " + result1);

        // With Streams - mapToDouble approach
        System.out.println("\n--- With Streams (mapToDouble) ---");
        double result2 = sumWithMapToDouble(transactions);
        System.out.println("Total INR: " + result2);

        // With Streams - reduce approach
        System.out.println("\n--- With Streams (reduce) ---");
        double result3 = sumWithReduce(transactions);
        System.out.println("Total INR: " + result3);

        // More aggregation examples
        System.out.println("\n--- More Aggregation Examples ---");
        moreExamples(transactions);
    }

    /**
     * Traditional approach
     */
    public static double sumWithoutStreams(List<Transaction> transactions) {
        double total = 0;

        for (Transaction t : transactions) {
            if (t.getCurrency().equals("INR")) {
                total += t.getAmount();
            }
        }

        return total;
    }

    /**
     * Stream approach using mapToDouble() + sum()
     * 
     * See concept.md: "Approach 1: mapToDouble() + sum()"
     * 
     * Pipeline:
     * 1. filter() → Keep only INR
     * 2. mapToDouble() → Extract amount as primitive double (DoubleStream)
     * 3. sum() → Add all values
     */
    public static double sumWithMapToDouble(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getCurrency().equals("INR"))
                .mapToDouble(Transaction::getAmount) // → DoubleStream
                .sum(); // Built-in sum
    }

    /**
     * Stream approach using reduce()
     * 
     * See concept.md: "Approach 2: reduce()"
     * 
     * reduce(identity, accumulator)
     * - identity = starting value (0.0)
     * - accumulator = how to combine two values
     */
    public static double sumWithReduce(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getCurrency().equals("INR"))
                .map(Transaction::getAmount)
                .reduce(0.0, Double::sum); // Same as (a, b) -> a + b
    }

    public static void moreExamples(List<Transaction> transactions) {

        // Example 1: Count INR transactions
        System.out.println("\n1. Count INR transactions:");
        long count = transactions.stream()
                .filter(t -> t.getCurrency().equals("INR"))
                .count();
        System.out.println("   Count: " + count);

        // Example 2: Find maximum amount (any currency)
        // See concept.md: max() returns Optional
        System.out.println("\n2. Maximum transaction amount:");
        Optional<Double> max = transactions.stream()
                .map(Transaction::getAmount)
                .max(Double::compare);
        max.ifPresent(m -> System.out.println("   Max: " + m));

        // Example 3: Find minimum amount
        System.out.println("\n3. Minimum transaction amount:");
        transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .min()
                .ifPresent(m -> System.out.println("   Min: " + m));

        // Example 4: Average amount
        // See concept.md: average() returns OptionalDouble
        System.out.println("\n4. Average transaction amount:");
        transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .average()
                .ifPresent(avg -> System.out.println("   Average: " + avg));

        // Example 5: Get all stats at once!
        // See concept.md: Q5 about summaryStatistics()
        System.out.println("\n5. Summary Statistics:");
        DoubleSummaryStatistics stats = transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .summaryStatistics();
        System.out.println("   Count: " + stats.getCount());
        System.out.println("   Sum: " + stats.getSum());
        System.out.println("   Min: " + stats.getMin());
        System.out.println("   Max: " + stats.getMax());
        System.out.println("   Average: " + stats.getAverage());

        // Example 6: Reduce for product (not just sum!)
        System.out.println("\n6. Reduce for custom operation (product):");
        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);
        int product = nums.stream()
                .reduce(1, (a, b) -> a * b); // 1*2*3*4*5 = 120
        System.out.println("   Product of [1,2,3,4,5]: " + product);

        // Example 7: Empty stream handling
        System.out.println("\n7. Empty stream - sum returns 0:");
        List<Transaction> empty = Arrays.asList();
        double emptySum = empty.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
        System.out.println("   Sum of empty list: " + emptySum); // 0.0, not error!
    }
}

/**
 * Custom Transaction class
 */
class Transaction {
    private String id;
    private double amount;
    private String currency;

    public Transaction(String id, double amount, String currency) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return String.format("%s - %.1f - %s", id, amount, currency);
    }
}
