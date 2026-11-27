package bankemployeesystem.controller;

import bankemployeesystem.model.Department;
import bankemployeesystem.model.Employee;
import bankemployeesystem.model.EmployeeFileReader;
import bankemployeesystem.model.EmployeeSearcher;
import bankemployeesystem.model.EmployeeSorter;
import bankemployeesystem.model.ManagerType;
import bankemployeesystem.model.MenuOption;
import bankemployeesystem.view.ConsoleView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import bankemployeesystem.model.EmployeeBinaryTree;

/**
 * CONTROLLER: receives the user's choices from the View,
 * calls the Model to do the work and sends the results back to the View.
 */
public class AppController {

    private static final int NAMES_TO_SHOW = 20; // assignment: show the first 20 names
    private static final int MIN_TREE_RECORDS = 20; // assignment: at least 20 records in the tree
    private final ConsoleView view;
    private final EmployeeFileReader fileReader;
    private final EmployeeSorter sorter = new EmployeeSorter();
    private final EmployeeSearcher searcher = new EmployeeSearcher();
    private List<Employee> employees = new ArrayList<>();
    private final List<Employee> newRecords = new ArrayList<>(); // records added by the user

    public AppController(ConsoleView view, EmployeeFileReader fileReader) {
        this.view = view;
        this.fileReader = fileReader;
    }

    /** Starts the application: loads the file, then runs the menu loop. */
    public void start() {
        loadFile();
        runMenu();
    }

    /** Keeps asking for a file name until a file is read successfully. */
    private void loadFile() {
        while (true) {
            String fileName = view.askFileName();
            try {
                employees = fileReader.readEmployees(fileName);
                view.showMessage("File read successfully (" + employees.size() + " employees loaded)");
                return;
            } catch (IOException e) {
                view.showMessage("Could not read '" + fileName + "'. Please try again.");
            }
        }
    }

    /** Shows the menu and handles each choice until the user selects EXIT. */
    private void runMenu() {
        MenuOption choice;
        do {
            view.showMenu();
            choice = view.readMenuOption();

            if (choice == null) {
                view.showMessage("Invalid option. Please enter a number from 1 to "
                        + MenuOption.values().length + ".");
                continue;
            }

            view.showMessage(choice.getLabel() + " selected");

            switch (choice) {
                case SORT:
                    handleSort();
                    break;
                case SEARCH:
                    handleSearch();
                    break;
                case ADD_RECORDS:
                    handleAddRecords();
                    break;
                case BINARY_TREE:
                    handleBinaryTree();
                    break;
                case EXIT:
                    view.showMessage("Goodbye!");
                    break;
            }
        } while (choice != MenuOption.EXIT);
    } // <-- end of runMenu()

    /** Sorts the list A-Z with the recursive Merge Sort and shows the first 20 names. */
    private void handleSort() {
        employees = sorter.mergeSort(employees); // keep the sorted list for the search later
        view.showEmployees(employees, NAMES_TO_SHOW);
    }

    /**
     * Asks for a name and finds it with the recursive Binary Search.
     * The list is sorted first because Binary Search only works on sorted data.
     */
    private void handleSearch() {
        employees = sorter.mergeSort(employees);

        String name = view.askText("Enter the full name to search (e.g. Kate Summer): ");
        if (name.isEmpty()) {
            view.showMessage("Name cannot be empty.");
            return;
        }

        Employee found = searcher.binarySearch(employees, name);
        if (found == null) {
            view.showMessage("No employee found with the name '" + name + "'.");
        } else {
            view.showEmployeeDetails(found);
        }
    }

    /**
     * Lets the user add one or more employees.
     * Every value is validated before the record is stored, then the list is
     * re-sorted and all records added in this session are displayed.
     */
    private void handleAddRecords() {
        while (true) {
            Employee newEmployee = askNewEmployee();
            if (newEmployee == null) {
                view.showMessage("Add cancelled.");
                break;
            }

            employees.add(newEmployee);
            newRecords.add(newEmployee);
            employees = sorter.mergeSort(employees); // keep the list sorted for Binary Search
            view.showMessage("Employee added successfully. Total employees: " + employees.size());

            if (!view.askYesNo("Add another employee? (Y/N): ")) {
                break;
            }
        }

        if (!newRecords.isEmpty()) {
            view.showMessage("\nNewly added records:");
            view.showEmployees(newRecords, newRecords.size());
        }
    }

    /**
     * Collects and validates the data for one new employee.
     * Returns null if the user cancels by pressing Enter on the name.
     */
    private Employee askNewEmployee() {
        // 1) Name: must be valid and must not exist already
        String name;
        while (true) {
            name = view.askText("Enter full name (first and last) or press Enter to cancel: ");
            if (name.isEmpty()) {
                return null;
            }
            if (!Employee.isValidFullName(name)) {
                view.showMessage("Invalid name. Use letters only and type first and last name (e.g. Maria Silva).");
                continue;
            }
            name = Employee.formatName(name);
            employees = sorter.mergeSort(employees);
            if (searcher.binarySearch(employees, name) != null) {
                view.showMessage("'" + name + "' already exists. Please enter a different name.");
                continue;
            }
            break;
        }

        // 2) Manager Type: must be one of the enum values
        ManagerType managerType;
        while (true) {
            view.showManagerTypes();
            managerType = ManagerType.fromInput(view.askText("Choose a Manager Type (number or name): "));
            if (managerType != null) {
                break;
            }
            view.showMessage("Invalid Manager Type. Please choose one from the list.");
        }

        // 3) Department: must be an existing department
        Department department;
        while (true) {
            view.showDepartments();
            department = Department.fromInput(view.askText("Choose a Department (number or name): "));
            if (department != null) {
                break;
            }
            view.showMessage("Invalid Department. Please choose one from the list.");
        }

        // First word = first name, the rest = last name (e.g. "Maria da Silva")
        String[] parts = name.split(" ", 2);
        return new Employee(parts[0], parts[1], managerType.getLabel(), managerType, department);
    }
    /**
     * Builds the employee hierarchy as a binary tree and shows it.
     * Employees are inserted by rank (Head Manager first, Employee last), and A-Z
     * inside each rank, so senior staff end up at the top levels of the tree.
     */
    private void handleBinaryTree() {
        if (employees.size() < MIN_TREE_RECORDS) {
            view.showMessage("At least " + MIN_TREE_RECORDS + " employees are needed. Currently: "
                    + employees.size() + ". Please add more records.");
            return;
        }

        employees = sorter.mergeSort(employees); // A-Z inside each rank
        EmployeeBinaryTree tree = new EmployeeBinaryTree();

        // The ManagerType enum is declared from highest to lowest rank
        for (ManagerType rank : ManagerType.values()) {
            for (Employee employee : employees) {
                if (employee.getManagerType() == rank) {
                    tree.insert(employee); // level-order insertion
                }
            }
        }

        view.showTree(tree.levelOrder(), tree.getHeight(), tree.getNodeCount());
    }
} // <-- end of the class