package bankemployeesystem.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Sorts employees alphabetically by full name using a recursive Merge Sort.
 * The comparison ignores upper/lower case, so "anna" and "Anna" are treated the same.
 */
public class EmployeeSorter {

    /**
     * Returns a new list sorted A-Z by full name.
     * The original list is not changed.
     */
    public List<Employee> mergeSort(List<Employee> list) {
        // Base case: a list with 0 or 1 element is already sorted
        if (list.size() <= 1) {
            return new ArrayList<>(list);
        }

        // Split the list into two halves
        int middle = list.size() / 2;
        List<Employee> left = mergeSort(list.subList(0, middle));          // recursive call
        List<Employee> right = mergeSort(list.subList(middle, list.size())); // recursive call

        // Combine the two sorted halves
        return merge(left, right);
    }

    /** Merges two sorted lists into one sorted list. */
    private List<Employee> merge(List<Employee> left, List<Employee> right) {
        List<Employee> result = new ArrayList<>(left.size() + right.size());
        int i = 0;
        int j = 0;

        while (i < left.size() && j < right.size()) {
            // "<= 0" keeps equal names in their original order (stable sort)
            if (compareNames(left.get(i), right.get(j)) <= 0) {
                result.add(left.get(i++));
            } else {
                result.add(right.get(j++));
            }
        }

        // Copy whatever is left in either half
        while (i < left.size()) {
            result.add(left.get(i++));
        }
        while (j < right.size()) {
            result.add(right.get(j++));
        }
        return result;
    }

    /** Compares two employees by full name, ignoring case. */
    private int compareNames(Employee a, Employee b) {
        return a.getFullName().compareToIgnoreCase(b.getFullName());
    }
}