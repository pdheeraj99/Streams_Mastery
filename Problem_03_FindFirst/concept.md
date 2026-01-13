# Problem 3: Find First IT Employee with High Salary

## 📋 Problem Statement

You are building an HR system. Given a list of employees, find the **first employee** from **IT department** whose **salary is greater than 50000**.

```
Input: List of Employee objects
       E001 - Ravi   - 45000 - IT
       E002 - Priya  - 65000 - HR
       E003 - Arjun  - 55000 - IT      ← Expected result
       E004 - Sneha  - 72000 - Finance
       E005 - Kiran  - 48000 - IT

Output: Employee: Arjun (E003), Salary: 55000

Edge Case: What if NO employee matches? → Handle gracefully!
```

---

## 🎭 Telugu Analogy

You're looking for a **specific person** in a crowd:

- Must be wearing **red shirt** (IT department)
- Must be **taller than 6 feet** (salary > 50000)
- You need **FIRST person** matching, not all of them!

| Scenario | Traditional | Stream |
|----------|-------------|--------|
| Find first match | Loop + break | One clean operation |
| Not found | Return null? -1? | Return Optional (safe!) |

---

## 🧠 Core Concept: Finding Elements

We've learned:

- `filter()` → Get ALL matching elements
- But what if we want just ONE?

### Finding Operations Comparison

| Operation | Returns | Use When |
|-----------|---------|----------|
| `filter().collect()` | List<T> | Need ALL matches |
| `findFirst()` | Optional<T> | Need FIRST match |
| `findAny()` | Optional<T> | Need ANY match (parallel) |
| `anyMatch()` | boolean | Just check if exists |
| `noneMatch()` | boolean | Check none match |
| `allMatch()` | boolean | Check all match |

---

## 🔑 The Stream Operation: findFirst()

```java
.findFirst() → Optional<T>
```

### Why Optional?

Traditional approach returns `null` when not found:

```java
Employee result = null;  // DANGER: NullPointerException waiting to happen!
```

Stream returns `Optional`:

```java
Optional<Employee> result;  // SAFE: Forces you to handle "not found"
```

### Optional Handling Methods

```java
Optional<Employee> result = ...

// Method 1: Check then get
if (result.isPresent()) {
    Employee emp = result.get();
}

// Method 2: Provide default
Employee emp = result.orElse(defaultEmployee);

// Method 3: Throw if empty
Employee emp = result.orElseThrow(() -> new NotFoundException());

// Method 4: Execute if present
result.ifPresent(emp -> System.out.println(emp));
```

---

## 🔗 Combining filter() + findFirst()

The power is in **chaining**:

```
employees.stream()
     │
     ▼
.filter(e -> e.getDept().equals("IT"))     ← First filter: IT only
     │
     ▼
.filter(e -> e.getSalary() > 50000)        ← Second filter: high salary
     │
     ▼
.findFirst()                                ← Get first match
     │
     ▼
Optional<Employee>                          ← Safe result
```

### Why Two Filters vs One?

```java
// Option 1: Single filter with &&
.filter(e -> e.getDept().equals("IT") && e.getSalary() > 50000)

// Option 2: Chained filters
.filter(e -> e.getDept().equals("IT"))
.filter(e -> e.getSalary() > 50000)
```

Both work! Option 2 is more **readable** for complex conditions.

---

## 📚 Interview Corner

### Q1: Difference between findFirst() and findAny()?

**A:**

| | findFirst() | findAny() |
|-|-------------|-----------|
| Guarantee | Returns FIRST element in encounter order | Returns ANY element (no order guarantee) |
| Sequential Stream | Same behavior | Same behavior |
| Parallel Stream | Still returns first (slower) | Returns any (faster!) |
| Use case | Order matters | Order doesn't matter, need speed |

### Q2: Why does findFirst() return Optional, not the element?

**A:**

- Stream might be **empty** after filtering
- Optional **forces** you to handle the "not found" case
- Prevents `NullPointerException`
- It's a **better API design**

### Q3: What's the difference between orElse() and orElseGet()?

**A:**

```java
// orElse(): Default is ALWAYS evaluated
.orElse(createDefaultEmployee())  // Method called even if present!

// orElseGet(): Default evaluated ONLY if empty (lazy)
.orElseGet(() -> createDefaultEmployee())  // Method called only when needed
```

👉 Use `orElseGet()` for expensive operations!

### Q4: How to throw custom exception if not found?

**A:**

```java
Employee emp = employees.stream()
    .filter(e -> e.getId().equals("E999"))
    .findFirst()
    .orElseThrow(() -> new EmployeeNotFoundException("E999"));
```

### Q5: Is findFirst() terminal or intermediate?

**A:** **TERMINAL** operation!

- Returns Optional (not Stream)
- Triggers the entire pipeline to execute
- Short-circuits: stops processing once match found!

---

## 🎯 Key Takeaways

1. `findFirst()` returns `Optional<T>`, not `T`
2. Optional forces safe handling of "not found"
3. `findFirst()` is TERMINAL (ends the stream)
4. It's SHORT-CIRCUITING (stops at first match)
5. Combine with `filter()` to find specific elements
6. Use `orElse()`, `orElseGet()`, `orElseThrow()` to unwrap
7. For parallel streams where order doesn't matter, use `findAny()`

---

## 🏃 Try It Yourself

1. Find first product with price > 1000
2. Find first student who scored above 90
3. Find first order that is "PENDING" status
