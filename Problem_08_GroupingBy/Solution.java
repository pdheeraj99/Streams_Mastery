package streams.mastery.problem08;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Problem 8: Group Employees by Department
 * 
 * See: 1_Problem.md → The raw question
 * See: 2_Thinking.md → Why we chose groupingBy
 * See: 3_Solution.md → Deep dive into groupingBy patterns
 */
public class Solution {

    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
                new Employee("E001", "Ravi", 45000, "IT"),
                new Employee("E002", "Priya", 65000, "HR"),
                new Employee("E003", "Arjun", 55000, "IT"),
                new Employee("E004", "Sneha", 72000, "Finance"),
                new Employee("E005", "Kiran", 48000, "IT"),
                new Employee("E006", "Meera", 58000, "HR"),
                new Employee("E007", "Raj", 82000, "Finance"));

        System.out.println("=== Problem 8: Group Employees by Department ===\n");
        System.out.println("Employees:");
        employees.forEach(e -> System.out.println("  " + e));

        // Without Streams
        System.out.println("\n--- Without Streams ---");
        Map<String, List<Employee>> result1 = withoutStreams(employees);
        printMap("By Department", result1);

        // With Streams - simple groupingBy
        System.out.println("\n--- With Streams (groupingBy) ---");
        Map<String, List<Employee>> result2 = withStreams(employees);
        printMap("By Department", result2);

        // Follow-up examples
        System.out.println("\n--- Follow-up Questions ---");
        followUpExamples(employees);
    }

    /**
     * Traditional approach
     */
    public static Map<String, List<Employee>> withoutStreams(List<Employee> employees) {
        Map<String, List<Employee>> result = new HashMap<>();

        for (Employee emp : employees) {
            String dept = emp.getDepartment();
            // computeIfAbsent: create list if not exists
            result.computeIfAbsent(dept, k -> new ArrayList<>()).add(emp);
        }

        return result;
    }

    /**
     * Stream approach using groupingBy()
     * 
     * See 3_Solution.md: "Version 1: Simple"
     * 
     * ONE LINE magic! 🎯
     */
    public static Map<String, List<Employee>> withStreams(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));
    }

    /**
     * Follow-up interview questions
     * See 3_Solution.md: "Common Patterns"
     */
    public static void followUpExamples(List<Employee> employees) {

        // 1. Group and get only NAMES
        // See 3_Solution.md: Pattern 1
        System.out.println("\n1. Names by Department:");
        Map<String, List<String>> namesByDept = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.mapping(Employee::getName, Collectors.toList())));
        namesByDept.forEach((k, v) -> System.out.println("   " + k + ": " + v));

        // 2. Count per department
        // See 3_Solution.md: Pattern 2
        System.out.println("\n2. Count by Department:");
        Map<String, Long> countByDept = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.counting()));
        countByDept.forEach((k, v) -> System.out.println("   " + k + ": " + v));

        // 3. Total salary per department
        // See 3_Solution.md: Pattern 3
        System.out.println("\n3. Total Salary by Department:");
        Map<String, Double> salaryByDept = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.summingDouble(Employee::getSalary)));
        salaryByDept.forEach((k, v) -> System.out.println("   " + k + ": " + v));

        // 4. Average salary per department
        System.out.println("\n4. Average Salary by Department:");
        Map<String, Double> avgByDept = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.averagingDouble(Employee::getSalary)));
        avgByDept.forEach((k, v) -> System.out.println("   " + k + ": " + String.format("%.2f", v)));

        // 5. Highest paid per department
        // See 3_Solution.md: Pattern 4
        System.out.println("\n5. Highest Paid by Department:");
        Map<String, Optional<Employee>> highestByDept = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.maxBy(Comparator.comparing(Employee::getSalary))));
        highestByDept.forEach((k, v) -> System.out.println("   " + k + ": " + v.map(Employee::getName).orElse("None")));

        // 6. Nested grouping: Department → Salary Range
        // See 3_Solution.md: Pattern 5
        System.out.println("\n6. Nested Grouping (Dept → Salary Range):");
        Map<String, Map<String, List<String>>> nested = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.groupingBy(
                                e -> e.getSalary() >= 60000 ? "High (≥60k)" : "Normal (<60k)",
                                Collectors.mapping(Employee::getName, Collectors.toList()))));
        nested.forEach((dept, ranges) -> {
            System.out.println("   " + dept + ":");
            ranges.forEach((range, names) -> System.out.println("      " + range + ": " + names));
        });

        // 7. Join names as comma-separated string
        // See 3_Solution.md: Q5
        System.out.println("\n7. Names Joined by Department:");
        Map<String, String> namesJoined = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.mapping(
                                Employee::getName,
                                Collectors.joining(", "))));
        namesJoined.forEach((k, v) -> System.out.println("   " + k + ": " + v));

        // 8. Only high-salary employees grouped
        // See 3_Solution.md: Pattern 6
        System.out.println("\n8. High Salary (>50k) by Department:");
        Map<String, List<String>> highPaidByDept = employees.stream()
                .filter(e -> e.getSalary() > 50000) // Filter BEFORE grouping
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.mapping(Employee::getName, Collectors.toList())));
        highPaidByDept.forEach((k, v) -> System.out.println("   " + k + ": " + v));
    }

    private static void printMap(String title, Map<String, List<Employee>> map) {
        System.out.println(title + ":");
        map.forEach((k, v) -> {
            System.out.print("   " + k + ": [");
            System.out.print(v.stream().map(Employee::getName).collect(Collectors.joining(", ")));
            System.out.println("]");
        });
    }
}

/**
 * Employee class
 */
class Employee {
    private String id;
    private String name;
    private double salary;
    private String department;

    public Employee(String id, String name, double salary, String department) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.department = department;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %.0f - %s", id, name, salary, department);
    }
}
