package bankemployeesystem;

import bankemployeesystem.controller.AppController;
import bankemployeesystem.model.EmployeeFileReader;
import bankemployeesystem.view.ConsoleView;

/**
 * Entry point of the application.
 * Creates the View, the Model helper and the Controller, then starts the program.
 */
public class BankEmployeeSystem {

    public static void main(String[] args) {
        ConsoleView view = new ConsoleView();
        EmployeeFileReader fileReader = new EmployeeFileReader();
        AppController controller = new AppController(view, fileReader);
        controller.start();
    }
}