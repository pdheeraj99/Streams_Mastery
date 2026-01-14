package streams.mastery.problem18;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Problem 18: Multi-level Grouping
 * 
 * COMPLEX BUT POWERFUL! 🔥
 * 
 * See: 1_Problem.md   → The problem
 * See: 2_Thinking.md  → Approaches compared
 * See: 3_Solution.md  → All patterns
 */
public class Solution {

    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee("Ram", "IT", "Bangalore", "Senior", 75000),
            new Employee("Sita", "HR", "Mumbai", "Junior", 45000),
            new Employee("Arjun", "IT", "Bangalore", "Junior", 55000),
            new Employee("Priya", "Finance", "Delhi", "Senior", 72000),
            new Employee("Kiran", "IT", "Mumbai", "Senior", 65000),
            new Employee("Meera", "HR", "Bangalore", "Senior", 58000),
            new Employee("Raj", "IT", "Bangalore", "Junior", 48000),
            new Employee("Sneha", "Finance", "Mumbai", "Junior", 52000)
        );
        
        System.out.println("=== Problem 18: Multi-level Grouping ===\n");
        System.out.println("Employees:");
        employees.forEach(e -> System.out.println("  " + e));
        
        // 1. Two-level grouping
        System.out.println("\n--- 1. Two-Level: Dept → Location ---");
        var twoLevel = employees.stream()
            .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.groupingBy(Employee::getLocation)
            ));
        printTwoLevel(twoLevel);
        
        // 2. Three-level grouping
        System.out.println("\n--- 2. Three-Level: Dept → Location → Level ---");
        var threeLevel = employees.stream()
            .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.groupingBy(
                    Employee::getLocation,
                    Collectors.groupingBy(Employee::getLevel)
                )
            ));
        printThreeLevel(threeLevel);
        
        // 3. Two-level with counting
        System.out.println("\n--- 3. Counting: Dept → Location → Count ---");
        var countByDeptLocation = employees.stream()
            .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.groupingBy(
                    Employee::getLocation,
                    Collectors.counting()
                )
            ));
        countByDeptLocation.forEach((dept, locations) -> {
            System.out.println("  " + dept + ":");
            locations.forEach((loc, count) -> 
                System.out.println("    " + loc + ": " + count));
        });
        
        // 4. Two-level with sum
        System.out.println("\n--- 4. Sum Salary: Dept → Location → Total ---");
        var salaryByDeptLocation = employees.stream()
            .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.groupingBy(
                    Employee::getLocation,
                    Collectors.summingDouble(Employee::getSalary)
                )
            ));
        salaryByDeptLocation.forEach((dept, locations) -> {
            System.out.println("  " + dept + ":");
            locations.forEach((loc, salary) -> 
                System.out.println("    " + loc + ": $" + salary));
        });
        
        // 5. Two-level with names only
        System.out.println("\n--- 5. Names Only: Dept → Location → Names ---");
        var namesByDeptLocation = employees.stream()
            .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.groupingBy(
                    Employee::getLocation,
                    Collectors.mapping(Employee::getName, Collectors.toList())
                )
            ));
        namesByDeptLocation.forEach((dept, locations) -> {
            System.out.println("  " + dept + ":");
            locations.forEach((loc, names) -> 
                System.out.println("    " + loc + ": " + names));
        });
        
        // 6. Composite key (flat)
        System.out.println("\n--- 6. Composite Key: 'Dept|Location' ---");
        var byComposite = employees.stream()
            .collect(Collectors.groupingBy(
                e -> e.getDepartment() + "|" + e.getLocation(),
                Collectors.mapping(Employee::getName, Collectors.toList())
            ));
        byComposite.forEach((key, names) -> 
            System.out.println("  " + key + ": " + names));
        
        // 7. List as composite key
        System.out.println("\n--- 7. List as Composite Key ---");
        var byListKey = employees.stream()
            .collect(Collectors.groupingBy(
                e -> Arrays.asList(e.getDepartment(), e.getLocation()),
                Collectors.counting()
            ));
        byListKey.forEach((key, count) -> 
            System.out.println("  " + key + ": " + count));
        
        // 8. Safe navigation
        System.out.println("\n--- 8. Safe Navigation Demo ---");
        // Try to get IT, Delhi, Senior (doesn't exist!)
        List<Employee> result = threeLevel
            .getOrDefault("IT", Map.of())
            .getOrDefault("Delhi", Map.of())
            .getOrDefault("Senior", List.of());
        System.out.println("  IT → Delhi → Senior: " + result.size() + " employees (safe!)");
        
        // Get existing: IT, Bangalore, Junior
        List<Employee> existing = threeLevel.get("IT").get("Bangalore").get("Junior");
        System.out.println("  IT → Bangalore → Junior: " + 
            existing.stream().map(Employee::getName).collect(Collectors.joining(", ")));
    }
    
    private static void printTwoLevel(Map<String, Map<String, List<Employee>>> map) {
        map.forEach((dept, locations) -> {
            System.out.println("  " + dept + ":");
            locations.forEach((loc, emps) -> {
                String names = emps.stream()
                    .map(Employee::getName)
                    .collect(Collectors.joining(", "));
                System.out.println("    " + loc + ": [" + names + "]");
            });
        });
    }
    
    private static void printThreeLevel(
            Map<String, Map<String, Map<String, List<Employee>>>> map) {
        map.forEach((dept, locations) -> {
            System.out.println("  " + dept + ":");
            locations.forEach((loc, levels) -> {
                System.out.println("    " + loc + ":");
                levels.forEach((level, emps) -> {
                    String names = emps.stream()
                        .map(Employee::getName)
                        .collect(Collectors.joining(", "));
                    System.out.println("      " + level + ": [" + names + "]");
                });
            });
        });
    }
}

class Employee {
    private String name;
    private String department;
    private String location;
    private String level;
    private double salary;
    
    public Employee(String name, String department, String location, 
                   String level, double salary) {
        this.name = name;
        this.department = department;
        this.location = location;
        this.level = level;
        this.salary = salary;
    }
    
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public String getLocation() { return location; }
    public String getLevel() { return level; }
    public double getSalary() { return salary; }
    
    @Override
    public String toString() {
        return String.format("%s - %s - %s - %s", name, department, location, level);
    }
}
