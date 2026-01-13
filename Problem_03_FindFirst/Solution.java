package streams.mastery.problem03;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Problem 3: Find First IT Employee with Salary > 50000
 * See: concept.md for detailed explanation
 */
public class Solution {

    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
                new Employee("E001", "Ravi", 45000, "IT"),
                new Employee("E002", "Priya", 65000, "HR"),
                new Employee("E003", "Arjun", 55000, "IT"),
                new Employee("E004", "Sneha", 72000, "Finance"),
                new Employee("E005", "Kiran", 48000, "IT"));

        System.out.println("=== Problem 3: Find First IT Employee (Salary > 50k) ===\n");
        System.out.println("Employees:");
        employees.forEach(System.out::println);

        // Without Streams
        System.out.println("\n--- Without Streams ---");
        Employee result1 = findWithoutStreams(employees);
        printResult(result1);

        // With Streams
        System.out.println("\n--- With Streams ---");
        Optional<Employee> result2 = findWithStreams(employees);
        printOptionalResult(result2);

        // Edge cases
        System.out.println("\n--- Edge Cases ---");
        testEdgeCases(employees);
    }

    /**
     * Traditional approach
     * 
     * Problems:
     * 1. Need to manage loop + break manually
     * 2. Returns null when not found (dangerous!)
     * 3. Caller might forget null check → NullPointerException
     */
    public static Employee findWithoutStreams(List<Employee> employees) {
        for (Employee emp : employees) {
            if (emp.getDepartment().equals("IT") && emp.getSalary() > 50000) {
                return emp; // Found! Exit early
            }
        }
        return null; // Not found - DANGER!
    }

    /**
     * Stream approach using filter() + findFirst()
     * 
     * See concept.md: "Combining filter() + findFirst()"
     * 
     * Benefits:
     * 1. Clean, readable pipeline
     * 2. Returns Optional - forces safe handling
     * 3. Short-circuits - stops at first match
     */
    public static Optional<Employee> findWithStreams(List<Employee> employees) {
        return employees.stream()
                .filter(emp -> emp.getDepartment().equals("IT")) // First: IT dept
                .filter(emp -> emp.getSalary() > 50000) // Then: high salary
                .findFirst(); // Terminal: get first
    }

    public static void testEdgeCases(List<Employee> employees) {

        // Case 1: What if no match? Using orElse()
        // See concept.md: "Optional Handling Methods"
        System.out.println("\n1. No match - using orElse():");
        Employee defaultEmp = new Employee("DEFAULT", "Unknown", 0, "NA");
        Employee result = employees.stream()
                .filter(e -> e.getDepartment().equals("Sales")) // No Sales dept!
                .findFirst()
                .orElse(defaultEmp);
        System.out.println("   Result: " + result);

        // Case 2: Throw exception if not found
        // See concept.md: Q4 about throwing exceptions
        System.out.println("\n2. Not found - throw exception:");
        try {
            Employee emp = employees.stream()
                    .filter(e -> e.getId().equals("E999"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Employee E999 not found!"));
        } catch (RuntimeException e) {
            System.out.println("   Caught: " + e.getMessage());
        }

        // Case 3: Execute only if present
        System.out.println("\n3. ifPresent() - execute only if found:");
        employees.stream()
                .filter(e -> e.getDepartment().equals("IT"))
                .filter(e -> e.getSalary() > 50000)
                .findFirst()
                .ifPresent(emp -> System.out.println("   Found: " + emp.getName()));

        // Case 4: findAny() vs findFirst()
        // See concept.md: Q1 about the difference
        System.out.println("\n4. findAny() - when order doesn't matter:");
        Optional<Employee> any = employees.stream()
                .filter(e -> e.getDepartment().equals("IT"))
                .findAny();
        any.ifPresent(emp -> System.out.println("   Any IT employee: " + emp.getName()));

        // Case 5: Check existence without getting the element
        System.out.println("\n5. anyMatch() - just check existence:");
        boolean hasHighPaidIT = employees.stream()
                .anyMatch(e -> e.getDepartment().equals("IT")
                        && e.getSalary() > 50000);
        System.out.println("   Has high-paid IT employee? " + hasHighPaidIT);
    }

    // Helper methods
    private static void printResult(Employee emp) {
        if (emp != null) {
            System.out.println("Found: " + emp);
        } else {
            System.out.println("Not found (null returned - dangerous!)");
        }
    }

    private static void printOptionalResult(Optional<Employee> opt) {
        // Clean way to handle Optional
        opt.ifPresentOrElse(
                emp -> System.out.println("Found: " + emp),
                () -> System.out.println("Not found (safely handled!)"));
    }
}

/**
 * Custom Employee class - typical interview setup
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

    // Getters
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
