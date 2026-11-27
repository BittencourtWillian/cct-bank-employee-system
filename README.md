# Bank Employee Management System

Java console application developed for the CCT College Dublin Algorithms & Constructs assessment.

**Author:** Willian Cesar Lino Bittencourt Id: 2024419
**Organisation chosen:** Bank

---

## 1. The Problem

A bank receives employee data from form submissions in a text file (`Applicants_Form.txt`) where the names are in random order. Staff need a simple tool to:

- **Sort** the employee list alphabetically and view the first 20 names;
- **Search** for an employee by name and see their **Manager Type** and **Department**;
- **Add** new employees with validated data (only existing Manager Types and Departments);
- **Visualise** the bank hierarchy as a **binary tree**, with its height and total number of employees.

---

## 2. How to Run

1. Open the project `BankEmployeeSystem` in NetBeans (Java 11 or later).
2. Run the project (**F6**).
3. When asked, type the file name: `Applicants_Form.txt`
4. Choose an option from the menu:

```
Please enter the filename to read: Applicants_Form.txt
File read successfully (46 employees loaded)

Do You wish to SORT or SEARCH:
1. SORT
2. SEARCH
3. ADD RECORDS
4. Create a binary tree
5. EXIT
```

---

## 3. Project Structure

```
src/bankemployeesystem/
├── BankEmployeeSystem.java        -> entry point (creates View, Model helper and Controller)
├── controller/
│   └── AppController.java         -> CONTROLLER: menu logic, connects View and Model
├── model/
│   ├── Employee.java              -> employee data + name validation rules
│   ├── ManagerType.java           -> enum of valid Manager Types
│   ├── Department.java            -> enum of existing Departments
│   ├── MenuOption.java            -> enum of menu options
│   ├── EmployeeFileReader.java    -> reads and validates the text file
│   ├── EmployeeSorter.java        -> recursive Merge Sort
│   ├── EmployeeSearcher.java      -> recursive Binary Search
│   ├── TreeNode.java              -> node of the binary tree
│   └── EmployeeBinaryTree.java    -> level-order insertion, traversal, height, count
└── view/
    └── ConsoleView.java           -> VIEW: all printing and keyboard input
```

---

## 4. MVC (Model-View-Controller) Design

The application is divided into three layers, each one in its own package, so that every class has a single responsibility.

| Layer          | Package / Classes                                                                                             | Responsibility                                                                                                                                          |
| -------------- | ------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Model**      | `model` – `Employee`, enums, `EmployeeFileReader`, `EmployeeSorter`, `EmployeeSearcher`, `EmployeeBinaryTree` | Holds the data and the business rules: reading the file, sorting, searching, validation rules and the tree. It never prints or reads from the keyboard. |
| **View**       | `view` – `ConsoleView`                                                                                        | Everything the user sees or types: menu, tables, messages, questions. It contains no business logic.                                                    |
| **Controller** | `controller` – `AppController`                                                                                | Receives the user's choice from the View, calls the right Model class and sends the result back to the View.                                            |

### Example: searching for an employee

1. **View** – `ConsoleView.showMenu()` prints the menu and `readMenuOption()` converts the user's input `2` into `MenuOption.SEARCH`.
2. **Controller** – `AppController` receives `SEARCH` and calls `handleSearch()`. It asks the View for the name (`view.askText(...)`).
3. **Model** – the Controller asks `EmployeeSorter.mergeSort()` to make sure the list is sorted, then calls `EmployeeSearcher.binarySearch()` to find the employee.
4. **View** – the Controller passes the result to `view.showEmployeeDetails(...)`, or to `view.showMessage(...)` if the name was not found.

```
User types "2"  ->  ConsoleView  ->  AppController.handleSearch()
                                          |
                                          v
                          EmployeeSorter + EmployeeSearcher (Model)
                                          |
                                          v
                    ConsoleView.showEmployeeDetails()  ->  User sees the result
```

### Why MVC?

- **Separation of concerns:** the sorting and searching code does not depend on the console. The same Model could be reused with a graphical interface by replacing only the View.
- **Easier to test and maintain:** a change in how results are displayed only affects `ConsoleView`; a change in an algorithm only affects its Model class.
- **Clear responsibilities:** for example, the rule "a name must have letters only and a first and last name" lives in the Model (`Employee.isValidFullName`), the question is asked by the View, and the Controller decides to ask again when the input is invalid.

---

## 5. Enums for the Menu

All menu options are defined in the `MenuOption` enum (number + label):

```java
SORT(1, "SORT"), SEARCH(2, "SEARCH"), ADD_RECORDS(3, "ADD RECORDS"),
BINARY_TREE(4, "Create a binary tree"), EXIT(5, "EXIT");
```

- The menu is printed by **iterating through `MenuOption.values()`**, so no menu text is hard-coded in the View.
- The Controller uses a `switch` on the enum constants (`case SORT:`), not on "magic numbers" such as `case 1:`.
- Invalid input (letters or numbers outside the list) returns `null` and the menu is shown again.
- Adding a new menu option only requires adding a new constant to the enum.

`ManagerType` and `Department` are also enums. This guarantees that only valid values can exist in the program and is the base of the input validation.

---

## 6. Sorting – Recursive Merge Sort

**Why Merge Sort was chosen:**

- **Guaranteed O(n log n)** in the best, average and worst case. Quick Sort is also recursive and usually fast, but it can degrade to **O(n²)** when the data is already sorted or nearly sorted, which is common here because new records are added to a list that is already sorted.
- **Stable:** employees with equal names keep their original order. Quick Sort is not stable.
- **Recursive by design**, which meets the assessment requirement naturally. Bubble, Selection and Insertion Sort are iterative and **O(n²)**, so they scale poorly as the list grows.
- **Predictable performance as data grows**, since users can keep adding records.
- **Trade-off:** it uses O(n) extra memory, which is acceptable for an employee list of this size.

The comparison ignores upper/lower case, so names are ordered correctly regardless of how they were typed. After sorting, the first 20 employees are displayed.

---

## 7. Searching – Recursive Binary Search

**Why Binary Search was chosen:**

- **O(log n)** instead of **O(n)** for Linear Search. With 46 employees it needs at most about 6 comparisons; with 10,000 employees about 14, instead of up to 10,000.
- **It takes advantage of the sorted list** produced by Merge Sort. Linear Search would ignore that order.
- **Naturally recursive:** each call works on half of the remaining range, so the recursion depth stays small (log n). A recursive Linear Search would need one call per element and could overflow the call stack on large lists.
- A **HashMap** would give O(1) lookups, but it is not a recursive algorithm and would require a second structure to keep in sync every time a record is added.
- **Trade-off:** the list must be sorted, so the Controller sorts it before searching (important after new records are added).

The search ignores upper/lower case and extra spaces, and shows a clear message when the name is not found.

---

## 8. Adding New Records (Validation)

When adding an employee, every value is validated before it is stored:

| Field        | Validation                                                                                                                        |
| ------------ | --------------------------------------------------------------------------------------------------------------------------------- |
| Name         | Letters only (hyphen/apostrophe allowed), first and last name required, automatically formatted (`maria  SILVA` -> `Maria Silva`) |
| Duplicate    | The name must not already exist (checked with the Binary Search)                                                                  |
| Manager Type | Must be one of the `ManagerType` enum values (by number or name)                                                                  |
| Department   | Must be one of the existing `Department` enum values (by number or name)                                                          |

Each field is asked again until a valid value is entered, and pressing Enter on the name cancels the operation. After each new record the list is re-sorted, and all records added in the session are displayed in a table.

---

## 9. Employee Hierarchy – Binary Tree

- Each `TreeNode` stores an employee (Name, Manager Type, Department) and has at most two children.
- Records are inserted using **level-order (breadth-first) insertion** with a **queue**: left child first, then right, then the next node in the level.
- Employees are inserted **by rank** (Head Manager -> Senior Manager -> Manager -> Assistant Manager -> Team Lead -> Employee) and alphabetically within each rank, so senior staff appear at the top of the tree.
- The hierarchy is displayed with a **level-order traversal**, followed by the **tree height** and **total node count**, both calculated recursively.

With the sample file (46 employees) the result is:

```
Level 0 (1 employees):  Benjamin Hill [Head Manager | Customer Service]
Level 1 (2 employees):  Jessica Lee, John Smith [Senior Manager]
Level 2 (4 employees):  Denis Wilson, James Anderson, John Anderson, John Brown [Manager]
...
Tree height : 6
Total nodes : 46
```

Level-order insertion keeps the tree **complete and balanced** (minimum height). A Binary Search Tree built from an already sorted list would become unbalanced and behave like a linked list.

---

## 10. Data Decisions

- The sample file has no "Manager Type" column, so the Manager Type is derived from the **Job title** column. Management titles (Head Manager, Senior Manager, Manager, Assistant Manager, Team Lead) map to their type; other titles (e.g. Clerk, Bookkeeper) are classified as **Employee**.
- Blank or invalid lines in the file are skipped instead of stopping the program.

---

## 11. Future Improvements

- Save new records back to the text file so they are kept after the program closes.
- Search by partial name or first name only, listing all matches.
- Make tree insertion faster for large datasets by keeping the queue of free positions between insertions instead of starting from the root each time.
- Replace the console View with a graphical interface (JavaFX), reusing the same Model and Controller.

---

## References

Cormen, T.H., Leiserson, C.E., Rivest, R.L. and Stein, C. (2022) _Introduction to Algorithms_. 4th edn. Cambridge, MA: MIT Press.

Oracle (2024) _The Java Tutorials – Enum Types_. Available at: https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html (Accessed: 27 November 2026).

Oracle (2024) _Java Platform SE – Interface Queue_. Available at: https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Queue.html (Accessed: 27 November 2026).
