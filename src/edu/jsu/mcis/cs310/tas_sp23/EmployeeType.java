package edu.jsu.mcis.cs310.tas_sp23;

/**
 *
 * @author Team C
 * <p> Determines if an employee is part time, full time, etc. </p>
 */
public enum EmployeeType {

    PART_TIME("Temporary / Part-Time"),
    FULL_TIME("Full-Time");
    private final String description;

    private EmployeeType(String d) {
        description = d;
    }

    /**
     *
     * @return
     * <p> toString Method outputs the method its state into a string </p>
     */
    @Override
    public String toString() {
        return description;
    }
    
}
