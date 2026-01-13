package streams.mastery.problem16;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Problem 16: Build Custom Collectors
 * 
 * ADVANCED INTERVIEW TOPIC! 🔥
 * 
 * See: 1_Problem.md → The raw question
 * See: 2_Thinking.md → Collector internals
 * See: 3_Solution.md → Multiple custom collectors
 */
public class Solution {

    public static void main(String[] args) {
        System.out.println("=== Problem 16: Custom Collectors ===\n");

        // Test data
        List<String> names = Arrays.asList("Ram", "Sita", "Hanuman", "Lakshman");
        List<Integer> numbers = Arrays.asList(5, 2, 8, 1, 9, 3, 7, 4, 6);

        // 1. Custom String Joiner
        System.out.println("--- 1. Custom String Joiner ---");
        String joined = names.stream()
                .collect(toJoinedString(", ", "[", "]"));
        System.out.println("Result: " + joined);

        // 2. Custom Statistics
        System.out.println("\n--- 2. Custom Statistics Collector ---");
        System.out.println("Numbers: " + numbers);
        Map<String, Object> stats = numbers.stream()
                .collect(toStatistics());
        stats.forEach((k, v) -> System.out.println("   " + k + ": " + v));

        // 3. Top N Collector
        System.out.println("\n--- 3. Top N Collector ---");
        List<Integer> top3 = numbers.stream()
                .collect(toTopN(3, Comparator.<Integer>naturalOrder()));
        System.out.println("Top 3: " + top3);

        List<Integer> bottom3 = numbers.stream()
                .collect(toTopN(3, Comparator.<Integer>reverseOrder()));
        System.out.println("Bottom 3: " + bottom3);

        // 4. Immutable List
        System.out.println("\n--- 4. Immutable List Collector ---");
        List<String> immutable = names.stream()
                .collect(toImmutableList());
        System.out.println("Immutable: " + immutable);
        try {
            immutable.add("Test");
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify! (UnsupportedOperationException)");
        }

        // 5. Product collector (multiply all)
        System.out.println("\n--- 5. Product Collector ---");
        List<Integer> smallNums = Arrays.asList(2, 3, 4, 5);
        long product = smallNums.stream()
                .collect(toProduct());
        System.out.println("Product of " + smallNums + " = " + product);

        // 6. Counting by condition
        System.out.println("\n--- 6. Conditional Counter ---");
        Map<String, Long> countByCondition = numbers.stream()
                .collect(toCountByCondition(n -> n > 5 ? "Above 5" : "5 or below"));
        countByCondition.forEach((k, v) -> System.out.println("   " + k + ": " + v));

        // 7. Parallel test
        System.out.println("\n--- 7. Parallel Stream with Custom Collector ---");
        String parallelJoined = names.parallelStream()
                .collect(toJoinedString(" | ", "<<", ">>"));
        System.out.println("Parallel joined: " + parallelJoined);
    }

    /**
     * Custom Collector 1: Join strings with delimiter, prefix, suffix
     */
    public static Collector<String, StringJoiner, String> toJoinedString(String delimiter, String prefix,
            String suffix) {

        return Collector.of(
                // Supplier: Create empty StringJoiner
                () -> new StringJoiner(delimiter, prefix, suffix),

                // Accumulator: Add each string
                StringJoiner::add,

                // Combiner: Merge two joiners (for parallel)
                StringJoiner::merge,

                // Finisher: Convert to String
                StringJoiner::toString);
    }

    /**
     * Custom Collector 2: Calculate statistics in one pass
     */
    public static Collector<Integer, int[], Map<String, Object>> toStatistics() {
        return Collector.of(
                // Supplier: [count, sum, min, max]
                () -> new int[] { 0, 0, Integer.MAX_VALUE, Integer.MIN_VALUE },

                // Accumulator
                (stats, num) -> {
                    stats[0]++; // count
                    stats[1] += num; // sum
                    stats[2] = Math.min(stats[2], num); // min
                    stats[3] = Math.max(stats[3], num); // max
                },

                // Combiner (for parallel)
                (s1, s2) -> new int[] {
                        s1[0] + s2[0],
                        s1[1] + s2[1],
                        Math.min(s1[2], s2[2]),
                        Math.max(s1[3], s2[3])
                },

                // Finisher: Convert to Map
                stats -> {
                    Map<String, Object> result = new LinkedHashMap<>();
                    result.put("count", stats[0]);
                    result.put("sum", stats[1]);
                    result.put("min", stats[0] > 0 ? stats[2] : null);
                    result.put("max", stats[0] > 0 ? stats[3] : null);
                    result.put("avg", stats[0] > 0 ? (double) stats[1] / stats[0] : 0.0);
                    return result;
                });
    }

    /**
     * Custom Collector 3: Keep only top N elements
     */
    public static <T> Collector<T, PriorityQueue<T>, List<T>> toTopN(int n, Comparator<T> comparator) {

        return Collector.of(
                // Supplier: Min-heap
                () -> new PriorityQueue<>(comparator),

                // Accumulator: Keep only top n
                (heap, element) -> {
                    heap.offer(element);
                    if (heap.size() > n) {
                        heap.poll(); // Remove smallest/largest
                    }
                },

                // Combiner: Merge two heaps
                (h1, h2) -> {
                    h1.addAll(h2);
                    while (h1.size() > n)
                        h1.poll();
                    return h1;
                },

                // Finisher: Convert to sorted list
                heap -> {
                    List<T> result = new ArrayList<>(heap);
                    result.sort(comparator.reversed());
                    return result;
                });
    }

    /**
     * Custom Collector 4: Create immutable list
     */
    public static <T> Collector<T, List<T>, List<T>> toImmutableList() {
        return Collector.of(
                ArrayList::new,
                List::add,
                (l1, l2) -> {
                    l1.addAll(l2);
                    return l1;
                },
                Collections::unmodifiableList);
    }

    /**
     * Custom Collector 5: Multiply all elements
     */
    public static Collector<Integer, long[], Long> toProduct() {
        return Collector.of(
                () -> new long[] { 1 }, // Start with 1
                (acc, n) -> acc[0] *= n, // Multiply
                (a1, a2) -> {
                    a1[0] *= a2[0];
                    return a1;
                }, // Combine
                acc -> acc[0] // Extract result
        );
    }

    /**
     * Custom Collector 6: Count by condition
     */
    public static <T> Collector<T, Map<String, Long>, Map<String, Long>> toCountByCondition(
            Function<T, String> classifier) {

        return Collector.of(
                HashMap::new,
                (map, element) -> {
                    String key = classifier.apply(element);
                    map.merge(key, 1L, Long::sum);
                },
                (m1, m2) -> {
                    m2.forEach((k, v) -> m1.merge(k, v, Long::sum));
                    return m1;
                },
                Function.identity(),
                Collector.Characteristics.IDENTITY_FINISH);
    }
}
