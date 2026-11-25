package bankemployeesystem.view;

import bankemployeesystem.model.MenuOption;
import java.util.Scanner;
import bankemployeesystem.model.Employee;
import java.util.List;
/**
 * VIEW: everything the user sees or types goes through this class.
 * It does not contain business logic; it only prints and reads.
 */
public class ConsoleView {

    private final Scanner scanner = new Scanner(System.in);

    /** Asks the user for the name of the file to read. */
    public String askFileName() {
        System.out.print("Please enter the filename to read: ");
        return scanner.nextLine().trim();
    }

    /** Prints the main menu by looping through the MenuOption enum. */
    public void showMenu() {
        System.out.println();
        System.out.println("Do You wish to SORT or SEARCH:");
        for (MenuOption option : MenuOption.values()) {
            System.out.println(option.getNumber() + ". " + option.getLabel());
        }
        System.out.print("> ");
    }

    /**
     * Reads the user's menu choice and converts it to a MenuOption.
     * Returns null if the input is not a number or not a valid option.
     */
    public MenuOption readMenuOption() {
        String input = scanner.nextLine().trim();
        try {
            return MenuOption.fromNumber(Integer.parseInt(input));
        } catch (NumberFormatException e) {
            return null; // text typed instead of a number
        }
    }

    /** Prints a simple message on its own line. */
    public void showMessage(String message) {
        System.out.println(message);
    }
        /**
     * Prints up to 'limit' employees as a numbered table.
     * Used to show the first 20 names after sorting.
     */
    public void showEmployees(List<Employee> employees, int limit) {
        int count = Math.min(limit, employees.size());
        System.out.println();
        System.out.printf("%-4s %-20s | %-17s | %-17s | %s%n",
                "No.", "Name", "Manager Type", "Department", "Job Title");
        System.out.println("-".repeat(90));
        for (int i = 0; i < count; i++) {
            System.out.printf("%-4d %s%n", i + 1, employees.get(i));
        }
        System.out.println("Showing " + count + " of " + employees.size() + " employees.");
    }
        /** Shows a question and returns what the user typed (without extra spaces). */
    public String askText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /** Shows the details of one employee found by the search. */
    public void showEmployeeDetails(Employee employee) {
        System.out.println();
        System.out.println("Employee found:");
        System.out.println("  Name         : " + employee.getFullName());
        System.out.println("  Manager Type : " + employee.getManagerType().getLabel());
        System.out.println("  Department   : " + employee.getDepartment().getLabel());
        System.out.println("  Job Title    : " + employee.getJobTitle());
    }
}