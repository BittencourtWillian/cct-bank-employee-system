package bankemployeesystem.model;

/**
 * Represents one bank employee.
 * Only the fields needed by the program are kept: name, job title, manager type and department.
 */
public class Employee {

    private final String firstName;
    private final String lastName;
    private final String jobTitle;
    private final ManagerType managerType;
    private final Department department;

    public Employee(String firstName, String lastName, String jobTitle,
                    ManagerType managerType, Department department) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
        this.managerType = managerType;
        this.department = department;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    /** Full name used for sorting and searching, e.g. "Kate Summer". */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public ManagerType getManagerType() {
        return managerType;
    }

    public Department getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return String.format("%-20s | %-17s | %-17s | %s",
                getFullName(), managerType.getLabel(), department.getLabel(), jobTitle);
    }

    /**
     * Validation rule for a new employee name:
     * first and last name, letters only (hyphen or apostrophe allowed, e.g. "Mary-Jane O'Neil").
     */
    public static boolean isValidFullName(String name) {
        String word = "\\p{L}+(['-]\\p{L}+)*";
        return name.trim().replaceAll("\\s+", " ").matches(word + "( " + word + ")+");
    }

    /** Formats a name: removes extra spaces and capitalises each word ("maria  silva" -> "Maria Silva"). */
    public static String formatName(String name) {
        String[] words = name.trim().toLowerCase().split("\\s+");
        StringBuilder result = new StringBuilder();
        for (String w : words) {
            if (result.length() > 0) {
                result.append(" ");
            }
            result.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1));
        }
        return result.toString();
    }
}