package bankemployeesystem.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads employee records from a comma-separated text file.
 * Expected columns: First name, Last name, Gender, Email, Salary,
 * Department, Position, Job title, Company.
 */
public class EmployeeFileReader {

    // Column positions in each line of the file
    private static final int FIRST_NAME = 0;
    private static final int LAST_NAME = 1;
    private static final int DEPARTMENT = 5;
    private static final int JOB_TITLE = 7;

    private int skippedLines; // invalid lines ignored during the last read

    /**
     * Reads the file and returns a list of employees.
     * Empty or invalid lines are skipped so one bad line does not stop the program.
     */
    public List<Employee> readEmployees(String fileName) throws IOException {
        List<Employee> employees = new ArrayList<>();
        skippedLines = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.readLine(); // skip the header line

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // ignore blank lines
                }

                // -1 keeps empty columns (e.g. an empty Position) so the indexes stay correct
                String[] parts = line.split(",", -1);
                if (parts.length <= JOB_TITLE) {
                    skippedLines++;
                    continue;
                }

                Department department = Department.fromLabel(parts[DEPARTMENT]);
                if (department == null) {
                    skippedLines++; // unknown department, keep data valid
                    continue;
                }

                String jobTitle = parts[JOB_TITLE].trim();
                employees.add(new Employee(
                        parts[FIRST_NAME].trim(),
                        parts[LAST_NAME].trim(),
                        jobTitle,
                        ManagerType.fromJobTitle(jobTitle),
                        department));
            }
        }
        return employees;
    }

    public int getSkippedLines() {
        return skippedLines;
    }
}