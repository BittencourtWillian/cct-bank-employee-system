package bankemployeesystem.model;

/**
 * Options of the main menu.
 * Each constant stores the number the user types and the text shown on screen,
 * so the menu is built from the enum instead of hard-coded numbers and strings.
 */
public enum MenuOption {
    SORT(1, "SORT"),
    SEARCH(2, "SEARCH"),
    ADD_RECORDS(3, "ADD RECORDS"),
    BINARY_TREE(4, "Create a binary tree"),
    EXIT(5, "EXIT");

    private final int number;
    private final String label;

    MenuOption(int number, String label) {
        this.number = number;
        this.label = label;
    }

    public int getNumber() {
        return number;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Returns the option that matches the number typed by the user,
     * or null when the number is not a valid option.
     */
    public static MenuOption fromNumber(int number) {
        for (MenuOption option : values()) {
            if (option.number == number) {
                return option;
            }
        }
        return null;
    }
}