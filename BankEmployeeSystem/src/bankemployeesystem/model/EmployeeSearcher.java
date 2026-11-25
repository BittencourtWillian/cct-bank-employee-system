package bankemployeesystem.model;

import java.util.List;

/**
 * Finds an employee by full name using a recursive Binary Search.
 * The list MUST be sorted A-Z by full name (EmployeeSorter does this).
 */
public class EmployeeSearcher {

    /**
     * Searches the sorted list for the given full name (case-insensitive).
     * Returns the employee, or null when the name is not in the list.
     */
    public Employee binarySearch(List<Employee> sortedList, String fullName) {
        // Remove extra spaces so "  kate   summer " still matches "Kate Summer"
        String target = fullName.trim().replaceAll("\\s+", " ");
        return binarySearch(sortedList, target, 0, sortedList.size() - 1);
    }

    /** Recursive helper that searches only between the positions low and high. */
    private Employee binarySearch(List<Employee> list, String target, int low, int high) {
        // Base case: the search range is empty, so the name is not in the list
        if (low > high) {
            return null;
        }

        int middle = low + (high - low) / 2;
        Employee current = list.get(middle);
        int comparison = target.compareToIgnoreCase(current.getFullName());

        if (comparison == 0) {
            return current;                                      // found it
        } else if (comparison < 0) {
            return binarySearch(list, target, low, middle - 1);  // search the left half
        } else {
            return binarySearch(list, target, middle + 1, high); // search the right half
        }
    }
}