package bankemployeesystem.controller;

import bankemployeesystem.model.Employee;
import bankemployeesystem.model.EmployeeFileReader;
import bankemployeesystem.model.MenuOption;
import bankemployeesystem.view.ConsoleView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CONTROLLER: receives the user's choices from the View,
 * calls the Model to do the work and sends the results back to the View.
 */
public class AppController {

    private final ConsoleView view;
    private final EmployeeFileReader fileReader;
    private List<Employee> employees = new ArrayList<>();

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
                    view.showMessage("Sort will be implemented next.");
                    break;
                case SEARCH:
                    view.showMessage("Search will be implemented next.");
                    break;
                case ADD_RECORDS:
                    view.showMessage("Add records will be implemented next.");
                    break;
                case BINARY_TREE:
                    view.showMessage("Binary tree will be implemented next.");
                    break;
                case EXIT:
                    view.showMessage("Goodbye!");
                    break;
            }
        } while (choice != MenuOption.EXIT);
    }
}