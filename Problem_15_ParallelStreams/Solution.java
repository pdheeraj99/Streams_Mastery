package streams.mastery.problem15;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Problem 15: Parallel Streams - When and How?
 * 
 * IMPORTANT INTERVIEW TOPIC! 🔥
 * 
 * See: 1_Problem.md → The raw question
 * See: 2_Thinking.md → When to use parallel
 * See: 3_Solution.md → Best practices + pitfalls
 */
public class Solution {

    public static void main(String[] args) {
        System.out.println("=== Problem 15: Parallel Streams ===\n");

        // Generate test data
        List<Integer> smallData = IntStream.range(0, 1_000)
                .boxed().collect(Collectors.toList());
        List<Integer> largeData = IntStream.range(0, 1_000_000)
                .boxed().collect(Collectors.toList());

        System.out.println("Small data size: " + smallData.size());
        System.out.println("Large data size: " + largeData.size());

        // 1. Basic comparison
        System.out.println("\n--- 1. Sequential vs Parallel (Large Data) ---");
        comparePerformance(largeData);

        // 2. Small data comparison
        System.out.println("\n--- 2. Small Data (Parallel overhead visible) ---");
        comparePerformance(smallData);

        // 3. Pitfall demonstrations
        System.out.println("\n--- 3. Common Pitfalls ---");
        demonstratePitfalls();

        // 4. Order comparison
        System.out.println("\n--- 4. Order Preservation ---");
        demonstrateOrder();

        // 5. Best practices
        System.out.println("\n--- 5. Best Practices ---");
        demonstrateBestPractices();
    }

    /**
     * Compare sequential vs parallel performance
     */
    public static void comparePerformance(List<Integer> data) {

        // Warm up JVM
        for (int i = 0; i < 3; i++) {
            data.stream().mapToLong(n -> (long) n * n).sum();
            data.parallelStream().mapToLong(n -> (long) n * n).sum();
        }

        // Sequential
        long start1 = System.currentTimeMillis();
        long sum1 = data.stream()
                .mapToLong(n -> {
                    // Simulate CPU work
                    return (long) n * n;
                })
                .sum();
        long time1 = System.currentTimeMillis() - start1;

        // Parallel
        long start2 = System.currentTimeMillis();
        long sum2 = data.parallelStream()
                .mapToLong(n -> {
                    // Same CPU work
                    return (long) n * n;
                })
                .sum();
        long time2 = System.currentTimeMillis() - start2;

        System.out.println("   Sequential: " + time1 + "ms (sum=" + sum1 + ")");
        System.out.println("   Parallel:   " + time2 + "ms (sum=" + sum2 + ")");
        System.out.println("   Winner: " + (time1 < time2 ? "Sequential" : "Parallel"));
    }

    /**
     * Demonstrate common pitfalls
     */
    public static void demonstratePitfalls() {
        List<Integer> numbers = IntStream.range(0, 100)
                .boxed().collect(Collectors.toList());

        // Pitfall 1: Shared mutable state (DANGEROUS!)
        System.out.println("\n   Pitfall 1: Shared Mutable State");
        System.out.println("   ❌ WRONG way (race condition):");

        // DON'T DO THIS!
        List<Integer> wrongWay = new ArrayList<>();
        numbers.parallelStream()
                .filter(n -> n % 2 == 0)
                .forEach(n -> wrongWay.add(n)); // NOT thread-safe!
        System.out.println("      Result size (should be 50): " + wrongWay.size() +
                " (may vary due to race condition!)");

        // Correct way
        System.out.println("   ✅ CORRECT way (use collect):");
        List<Integer> correctWay = numbers.parallelStream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
        System.out.println("      Result size: " + correctWay.size());

        // Pitfall 2: Source type matters
        System.out.println("\n   Pitfall 2: Source Type");
        System.out.println("   ArrayList → Good for parallel (random access)");
        System.out.println("   LinkedList → Bad for parallel (sequential access)");
    }

    /**
     * Demonstrate order preservation
     */
    public static void demonstrateOrder() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        System.out.println("   Original: " + numbers);

        // Sequential - always ordered
        System.out.print("   Sequential forEach: ");
        numbers.stream().forEach(n -> System.out.print(n + " "));
        System.out.println();

        // Parallel forEach - order NOT guaranteed
        System.out.print("   Parallel forEach:   ");
        numbers.parallelStream().forEach(n -> System.out.print(n + " "));
        System.out.println(" (order may vary!)");

        // Parallel forEachOrdered - order guaranteed
        System.out.print("   Parallel forEachOrdered: ");
        numbers.parallelStream().forEachOrdered(n -> System.out.print(n + " "));
        System.out.println(" (order preserved)");
    }

    /**
     * Demonstrate best practices
     */
    public static void demonstrateBestPractices() {
        List<Integer> numbers = IntStream.range(0, 1000)
                .boxed().collect(Collectors.toList());

        // Best Practice 1: Check if parallel
        System.out.println("\n   Best Practice 1: Check if parallel");
        var stream = numbers.parallelStream();
        System.out.println("   Is parallel: " + stream.isParallel());

        // Best Practice 2: Use appropriate collectors
        System.out.println("\n   Best Practice 2: Thread-safe collectors");
        Map<Boolean, List<Integer>> partitioned = numbers.parallelStream()
                .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        System.out.println("   Even count: " + partitioned.get(true).size());
        System.out.println("   Odd count: " + partitioned.get(false).size());

        // Best Practice 3: groupingByConcurrent for parallel
        System.out.println("\n   Best Practice 3: groupingByConcurrent");
        Map<Integer, List<Integer>> grouped = numbers.parallelStream()
                .collect(Collectors.groupingByConcurrent(n -> n % 10));
        System.out.println("   Groups by last digit: " + grouped.size());

        // Best Practice 4: Stateless operations
        System.out.println("\n   Best Practice 4: Stateless operations");
        long sum = numbers.parallelStream()
                .filter(n -> n > 500) // Stateless ✅
                .mapToLong(n -> n * 2L) // Stateless ✅
                .sum(); // Thread-safe reduction ✅
        System.out.println("   Sum of doubled values > 500: " + sum);

        // Best Practice 5: Custom ForkJoinPool (advanced)
        System.out.println("\n   Best Practice 5: Custom ForkJoinPool");
        System.out.println("   Default pool size: " + ForkJoinPool.commonPool().getParallelism());
        System.out.println("   (equals CPU cores - 1)");
    }
}
