package streams.mastery.problem09;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Problem 9: Partition Students Pass/Fail
 * 
 * See: 1_Problem.md → The raw question
 * See: 2_Thinking.md → Why partitioningBy vs groupingBy
 * See: 3_Solution.md → Deep dive into partitioningBy
 */
public class Solution {

    public static void main(String[] args) {
        List<Student> students = Arrays.asList(
                new Student("S001", "Ravi", 75),
                new Student("S002", "Priya", 35),
                new Student("S003", "Arjun", 42),
                new Student("S004", "Sneha", 28),
                new Student("S005", "Kiran", 88),
                new Student("S006", "Meera", 39),
                new Student("S007", "Raj", 55));

        System.out.println("=== Problem 9: Partition Students Pass/Fail ===\n");
        System.out.println("Students (Pass mark = 40):");
        students.forEach(s -> System.out.println("  " + s));

        // Without Streams
        System.out.println("\n--- Without Streams ---");
        Map<Boolean, List<Student>> result1 = withoutStreams(students);
        printResult(result1);

        // With Streams
        System.out.println("\n--- With Streams (partitioningBy) ---");
        Map<Boolean, List<Student>> result2 = withStreams(students);
        printResult(result2);

        // Follow-up examples
        System.out.println("\n--- Follow-up Questions ---");
        followUpExamples(students);

        // Edge case: all pass
        System.out.println("\n--- Edge Case: All Pass ---");
        testAllPass();
    }

    /**
     * Traditional approach
     */
    public static Map<Boolean, List<Student>> withoutStreams(List<Student> students) {
        Map<Boolean, List<Student>> result = new HashMap<>();
        result.put(true, new ArrayList<>()); // Initialize both!
        result.put(false, new ArrayList<>());

        for (Student s : students) {
            if (s.getMarks() >= 40) {
                result.get(true).add(s);
            } else {
                result.get(false).add(s);
            }
        }

        return result;
    }

    /**
     * Stream approach using partitioningBy()
     * 
     * See 3_Solution.md: "Version 1: Simple"
     * 
     * Key advantage: Both true/false keys ALWAYS present!
     */
    public static Map<Boolean, List<Student>> withStreams(List<Student> students) {
        return students.stream()
                .collect(Collectors.partitioningBy(s -> s.getMarks() >= 40));
    }

    public static void followUpExamples(List<Student> students) {

        // 1. Count pass/fail
        System.out.println("\n1. Count Pass/Fail:");
        Map<Boolean, Long> counts = students.stream()
                .collect(Collectors.partitioningBy(
                        s -> s.getMarks() >= 40,
                        Collectors.counting()));
        System.out.println("   Pass: " + counts.get(true));
        System.out.println("   Fail: " + counts.get(false));

        // 2. Average marks
        System.out.println("\n2. Average Marks:");
        Map<Boolean, Double> avgMarks = students.stream()
                .collect(Collectors.partitioningBy(
                        s -> s.getMarks() >= 40,
                        Collectors.averagingDouble(Student::getMarks)));
        System.out.println("   Pass avg: " + String.format("%.2f", avgMarks.get(true)));
        System.out.println("   Fail avg: " + String.format("%.2f", avgMarks.get(false)));

        // 3. Names only
        System.out.println("\n3. Names by Pass/Fail:");
        Map<Boolean, List<String>> names = students.stream()
                .collect(Collectors.partitioningBy(
                        s -> s.getMarks() >= 40,
                        Collectors.mapping(Student::getName, Collectors.toList())));
        System.out.println("   Pass: " + names.get(true));
        System.out.println("   Fail: " + names.get(false));

        // 4. Joined names
        System.out.println("\n4. Names Joined:");
        Map<Boolean, String> namesJoined = students.stream()
                .collect(Collectors.partitioningBy(
                        s -> s.getMarks() >= 40,
                        Collectors.mapping(
                                Student::getName,
                                Collectors.joining(", "))));
        System.out.println("   Pass: " + namesJoined.get(true));
        System.out.println("   Fail: " + namesJoined.get(false));

        // 5. Max marks in each group
        System.out.println("\n5. Highest Marks in Each Group:");
        Map<Boolean, Optional<Student>> toppers = students.stream()
                .collect(Collectors.partitioningBy(
                        s -> s.getMarks() >= 40,
                        Collectors.maxBy(Comparator.comparing(Student::getMarks))));
        System.out.println("   Pass topper: " + toppers.get(true).map(Student::getName).orElse("None"));
        System.out.println("   Fail highest: " + toppers.get(false).map(Student::getName).orElse("None"));
    }

    /**
     * Edge case: All students pass
     * See 3_Solution.md: "Why Both Keys Matter"
     */
    public static void testAllPass() {
        List<Student> allPass = Arrays.asList(
                new Student("A", "Top1", 90),
                new Student("B", "Top2", 85));

        // partitioningBy - false key EXISTS (empty list)
        Map<Boolean, List<Student>> result = allPass.stream()
                .collect(Collectors.partitioningBy(s -> s.getMarks() >= 40));

        System.out.println("All students pass scenario:");
        System.out.println("   true key exists: " + result.containsKey(true));
        System.out.println("   false key exists: " + result.containsKey(false)); // TRUE!
        System.out.println("   Pass count: " + result.get(true).size());
        System.out.println("   Fail count: " + result.get(false).size()); // 0, not NPE!

        // Compare with groupingBy
        System.out.println("\n   (Compare) groupingBy would have:");
        Map<Boolean, List<Student>> groupResult = allPass.stream()
                .collect(Collectors.groupingBy(s -> s.getMarks() >= 40));
        System.out.println("   false key exists in groupingBy: " + groupResult.containsKey(false)); // FALSE!
    }

    private static void printResult(Map<Boolean, List<Student>> result) {
        System.out.print("   PASS ✅: [");
        System.out.print(result.get(true).stream()
                .map(s -> s.getName() + "-" + s.getMarks())
                .collect(Collectors.joining(", ")));
        System.out.println("]");

        System.out.print("   FAIL ❌: [");
        System.out.print(result.get(false).stream()
                .map(s -> s.getName() + "-" + s.getMarks())
                .collect(Collectors.joining(", ")));
        System.out.println("]");
    }
}

/**
 * Student class
 */
class Student {
    private String id;
    private String name;
    private int marks;

    public Student(String id, String name, int marks) {
        this.id = id;
        this.name = name;
        this.marks = marks;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMarks() {
        return marks;
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %d marks", id, name, marks);
    }
}
