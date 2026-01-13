package streams.mastery.problem17;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Problem 17: Downstream Collectors Deep Dive
 * 
 * VERY IMPORTANT FOR INTERVIEWS! 🔥
 * 
 * See: 1_Problem.md → The problem
 * See: 2_Thinking.md → How downstreams work
 * See: 3_Solution.md → All collectors explained
 */
public class Solution {

    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
                new Employee("Ram", 45000, "IT", Arrays.asList("Java", "SQL")),
                new Employee("Sita", 65000, "HR", Arrays.asList("Excel", "Communication")),
                new Employee("Arjun", 55000, "IT", Arrays.asList("Python", "Java")),
                new Employee("Priya", 72000, "Finance", Arrays.asList("Excel", "Accounting")),
                new Employee("Kiran", 48000, "IT", Arrays.asList("JavaScript", "React")),
                new Employee("Meera", 58000, "HR", Arrays.asList("Recruiting", "Excel")));

        System.out.println("=== Problem 17: Downstream Collectors ===\n");
        System.out.println("Employees:");
        employees.forEach(e -> System.out.println("  " + e));

        // 1. mapping() - Get names by department
        System.out.println("\n--- 1. mapping() - Names by Department ---");
        Map<String, List<String>> namesByDept = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.mapping(Employee::getName, Collectors.toList())));
        namesByDept.forEach((k, v) -> System.out.println("   " + k + ": " + v));

        // 2. filtering() - High salary only (Java 9+)
        System.out.println("\n--- 2. filtering() - High Salary (>50000) by Dept ---");
        Map<String, List<String>> highPaidByDept = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.filtering(
                                e -> e.getSalary() > 50000,
                                Collectors.mapping(Employee::getName, Collectors.toList()))));
        highPaidByDept.forEach((k, v) -> System.out.println("   " + k + ": " + v));

        // 3. flatMapping() - All skills per department
        System.out.println("\n--- 3. flatMapping() - Skills by Department ---");
        Map<String, Set<String>> skillsByDept = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.flatMapping(
                                e -> e.getSkills().stream(),
                                Collectors.toSet())));
        skillsByDept.forEach((k, v) -> System.out.println("   " + k + ": " + v));

        // 4. collectingAndThen() - Make result immutable
        System.out.println("\n--- 4. collectingAndThen() - Immutable Lists ---");
        Map<String, List<Employee>> immutable = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                Collections::unmodifiableList)));
        System.out.println("   Created immutable lists per department");
        try {
            immutable.get("IT").add(new Employee("Test", 0, "IT", List.of()));
        } catch (UnsupportedOperationException e) {
            System.out.println("   Cannot modify: UnsupportedOperationException!");
        }

        // 5. counting() - Count per department
        System.out.println("\n--- 5. counting() - Count by Department ---");
        Map<String, Long> countByDept = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.counting()));
        countByDept.forEach((k, v) -> System.out.println("   " + k + ": " + v));

        // 6. summingDouble() - Total salary per department
        System.out.println("\n--- 6. summingDouble() - Total Salary by Dept ---");
        Map<String, Double> salaryByDept = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.summingDouble(Employee::getSalary)));
        salaryByDept.forEach((k, v) -> System.out.println("   " + k + ": $" + v));

        // 7. averagingDouble() - Average salary per department
        System.out.println("\n--- 7. averagingDouble() - Avg Salary by Dept ---");
        Map<String, Double> avgByDept = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.averagingDouble(Employee::getSalary)));
        avgByDept.forEach((k, v) -> System.out.println("   " + k + ": $" + String.format("%.0f", v)));

        // 8. maxBy() - Highest paid per department
        System.out.println("\n--- 8. maxBy() - Highest Paid by Dept ---");
        Map<String, Optional<Employee>> highestByDept = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.maxBy(Comparator.comparing(Employee::getSalary))));
        highestByDept.forEach((k, v) -> System.out.println("   " + k + ": " + v.map(Employee::getName).orElse("None")));

        // 9. joining() - Names as comma-separated string
        System.out.println("\n--- 9. joining() - Names Joined by Dept ---");
        Map<String, String> namesJoined = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.mapping(
                                Employee::getName,
                                Collectors.joining(", "))));
        namesJoined.forEach((k, v) -> System.out.println("   " + k + ": " + v));

        // 10. reducing() - Custom aggregation
        System.out.println("\n--- 10. reducing() - Custom Name Concat ---");
        Map<String, String> reduced = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.reducing(
                                "",
                                Employee::getName,
                                (s1, s2) -> s1.isEmpty() ? s2 : s1 + " & " + s2)));
        reduced.forEach((k, v) -> System.out.println("   " + k + ": " + v));

        // 11. BONUS: filtering vs filter comparison
        System.out.println("\n--- 11. BONUS: filtering() vs filter() ---");
        demonstrateFilterDifference(employees);
    }

    public static void demonstrateFilterDifference(List<Employee> employees) {
        // filter() BEFORE groupingBy - departments with no match are MISSING
        Map<String, List<Employee>> filterBefore = employees.stream()
                .filter(e -> e.getSalary() > 70000)
                .collect(Collectors.groupingBy(Employee::getDepartment));
        System.out.println("   filter() before: " + filterBefore.keySet() +
                " (missing depts with no high salary!)");

        // filtering() as downstream - ALL departments present, some empty
        Map<String, List<Employee>> filteringDownstream = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.filtering(e -> e.getSalary() > 70000, Collectors.toList())));
        System.out.println("   filtering() as downstream: " + filteringDownstream.keySet() +
                " (all depts present!)");
        System.out.println("   IT list size: " + filteringDownstream.get("IT").size() + " (empty but exists)");
    }
}

class Employee {
    private String name;
    private double salary;
    private String department;
    private List<String> skills;

    public Employee(String name, double salary, String department, List<String> skills) {
        this.name = name;
        this.salary = salary;
        this.department = department;
        this.skills = skills;
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

    public List<String> getSkills() {
        return skills;
    }

    @Override
    public String toString() {
        return String.format("%s - $%.0f - %s", name, salary, department);
    }
}
