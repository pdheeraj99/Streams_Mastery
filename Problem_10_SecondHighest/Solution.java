package streams.mastery.problem10;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Problem 10: Second Highest Salary by Department
 * 
 * CLASSIC INTERVIEW QUESTION! 🔥
 * 
 * See: 1_Problem.md → The raw question
 * See: 2_Thinking.md → Different approaches
 * See: 3_Solution.md → Step-by-step execution
 */
public class Solution {

    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
                new Employee("E001", "Ravi", 75000, "IT"),
                new Employee("E002", "Priya", 65000, "HR"),
                new Employee("E003", "Arjun", 85000, "IT"),
                new Employee("E004", "Sneha", 72000, "Finance"),
                new Employee("E005", "Kiran", 55000, "IT"),
                new Employee("E006", "Meera", 58000, "HR"),
                new Employee("E007", "Raj", 92000, "Finance"),
                new Employee("E008", "Amit", 80000, "IT"));

        System.out.println("=== Problem 10: Second Highest Salary by Department ===\n");
        System.out.println("Employees:");
        employees.forEach(e -> System.out.println("  " + e));

        // Approach A: Clear steps
        System.out.println("\n--- Approach A: Clear Steps ---");
        Map<String, Optional<Employee>> result1 = approachA(employees);
        printResult(result1);

        // Approach B: Single pipeline
        System.out.println("\n--- Approach B: Single Pipeline ---");
        Map<String, Optional<Employee>> result2 = approachB(employees);
        printResult(result2);

        // Edge cases
        System.out.println("\n--- Edge Cases ---");
        testEdgeCases();

        // Bonus: Nth highest
        System.out.println("\n--- Bonus: Nth Highest ---");
        nthHighestExample(employees);
    }

    /**
     * Approach A: Clear, step-by-step
     * See 3_Solution.md: "Approach A: Clear Steps"
     */
    public static Map<String, Optional<Employee>> approachA(List<Employee> employees) {
        // Step 1: Group by department
        Map<String, List<Employee>> byDept = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));

        // Step 2: For each dept, find second highest
        return byDept.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .sorted(Comparator.comparing(Employee::getSalary).reversed()) // Desc
                                .skip(1) // Skip highest
                                .findFirst() // Get second
                ));
    }

    /**
     * Approach B: Single pipeline with collectingAndThen
     * See 3_Solution.md: "Approach B: Single Pipeline"
     */
    public static Map<String, Optional<Employee>> approachB(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(Employee::getSalary).reversed())
                                        .skip(1)
                                        .findFirst())));
    }

    /**
     * Edge cases testing
     */
    public static void testEdgeCases() {

        // Edge Case 1: Only 1 employee in department
        System.out.println("\n1. Single employee department:");
        List<Employee> singleEmp = Arrays.asList(
                new Employee("E1", "Solo", 50000, "Alone"));
        Map<String, Optional<Employee>> result1 = approachA(singleEmp);
        System.out.println("   Alone dept 2nd highest: " +
                result1.get("Alone").map(Employee::getName).orElse("NONE (as expected!)"));

        // Edge Case 2: Same salary (ties)
        System.out.println("\n2. Tied salaries:");
        List<Employee> tied = Arrays.asList(
                new Employee("E1", "Alice", 50000, "Tie"),
                new Employee("E2", "Bob", 50000, "Tie"),
                new Employee("E3", "Carol", 50000, "Tie"));
        Map<String, Optional<Employee>> result2 = approachA(tied);
        System.out.println("   All same salary, 2nd: " +
                result2.get("Tie").map(Employee::getName).orElse("NONE"));

        // Edge Case 3: Get DISTINCT second highest salary
        System.out.println("\n3. Second highest DISTINCT salary:");
        List<Employee> withTies = Arrays.asList(
                new Employee("E1", "Top1", 100000, "Test"),
                new Employee("E2", "Top2", 100000, "Test"), // Tie for first
                new Employee("E3", "Second", 80000, "Test"),
                new Employee("E4", "Third", 60000, "Test"));
        OptionalDouble secondDistinct = withTies.stream()
                .mapToDouble(Employee::getSalary)
                .distinct()
                .boxed()
                .sorted(Comparator.reverseOrder())
                .skip(1)
                .mapToDouble(d -> d)
                .findFirst();
        System.out.println("   Second distinct salary: " +
                secondDistinct.orElse(0.0));
    }

    /**
     * Bonus: Generalized Nth highest
     * See 3_Solution.md: "Edge Case 3: Get Nth highest"
     */
    public static void nthHighestExample(List<Employee> employees) {

        // IT department only
        List<Employee> itEmployees = employees.stream()
                .filter(e -> e.getDepartment().equals("IT"))
                .collect(Collectors.toList());

        System.out.println("IT Department employees:");
        itEmployees.stream()
                .sorted(Comparator.comparing(Employee::getSalary).reversed())
                .forEach(e -> System.out.println("  " + e.getName() + " - " + e.getSalary()));

        // Find 1st, 2nd, 3rd highest
        for (int n = 1; n <= 4; n++) {
            Optional<Employee> nth = getNthHighest(itEmployees, n);
            System.out.println(n + " highest: " +
                    nth.map(e -> e.getName() + " (" + e.getSalary() + ")").orElse("NONE"));
        }
    }

    /**
     * Generic method: Get Nth highest salary employee
     */
    public static Optional<Employee> getNthHighest(List<Employee> employees, int n) {
        return employees.stream()
                .sorted(Comparator.comparing(Employee::getSalary).reversed())
                .skip(n - 1) // Skip first (n-1) to get nth
                .findFirst();
    }

    private static void printResult(Map<String, Optional<Employee>> result) {
        result.forEach((dept, emp) -> {
            String empInfo = emp.map(e -> e.getName() + " (" + e.getSalary() + ")")
                    .orElse("NONE");
            System.out.println("   " + dept + ": " + empInfo);
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
