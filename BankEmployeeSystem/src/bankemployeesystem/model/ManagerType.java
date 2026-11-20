package bankemployeesystem.model;

/**
 * Manager types used in the bank.
 * Each constant stores a readable label that matches the "Job title" column of the data file.
 */
public enum ManagerType {
    HEAD_MANAGER("Head Manager"),
    SENIOR_MANAGER("Senior Manager"),
    MANAGER("Manager"),
    ASSISTANT_MANAGER("Assistant Manager"),
    TEAM_LEAD("Team Lead"),
    EMPLOYEE("Employee"); // staff with no management role

    private final String label;

    ManagerType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Converts a job title from the file into a ManagerType.
     * Titles that are not management roles (e.g. "Clerk") become EMPLOYEE.
     */
    public static ManagerType fromJobTitle(String jobTitle) {
        for (ManagerType type : values()) {
            if (type.label.equalsIgnoreCase(jobTitle.trim())) {
                return type;
            }
        }
        return EMPLOYEE;
    }
}