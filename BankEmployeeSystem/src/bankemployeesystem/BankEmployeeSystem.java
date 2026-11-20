package bankemployeesystem;

import bankemployeesystem.model.Employee;
import bankemployeesystem.model.EmployeeFileReader;
import java.io.IOException;
import java.util.List;

/**
 * Entry point of the application.
 * TEMPORARY: only tests that the file is read correctly.
 * It will be replaced by the controller and the menu in the next step.
 */
public class BankEmployeeSystem {

    public static void main(String[] args) {
        EmployeeFileReader reader = new EmployeeFileReader();
        try {
            List<Employee> employees = reader.readEmployees("Applicants_Form.txt");
            System.out.println("File read successfully: " + employees.size() + " employees loaded.");
            System.out.println("Skipped lines: " + reader.getSkippedLines());
            for (Employee employee : employees) {
                System.out.println(employee);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}