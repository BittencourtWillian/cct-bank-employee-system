package bankemployeesystem.model;

/**
 * Departments that exist in the bank.
 * The label matches the "Department" column of the data file.
 */
public enum Department {
    IT_DEVELOPMENT("IT Development"),
    SALES("Sales"),
    HR("HR"),
    FINANCE("Finance"),
    ACCOUNTING("Accounting"),
    MARKETING("Marketing"),
    OPERATIONS("Operations"),
    TECHNICAL_SUPPORT("Technical Support"),
    CUSTOMER_SERVICE("Customer Service");

    private final String label;

    Department(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Finds the Department whose label matches the given text (case-insensitive).
     * Returns null when the department does not exist.
     */
    public static Department fromLabel(String text) {
        for (Department dept : values()) {
            if (dept.label.equalsIgnoreCase(text.trim())) {
                return dept;
            }
        }
        return null;
    }
}