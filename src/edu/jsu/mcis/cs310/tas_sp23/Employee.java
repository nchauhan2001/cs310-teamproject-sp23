package edu.jsu.mcis.cs310.tas_sp23;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Team C
 * <p>  provides an Employee model object class which will be used to represent employees. </p>
 */
public class Employee {
    
    private int id;
    private String firstName, middleName, lastName;
    private LocalDateTime active;
    private Badge badge;
    private Department department;
    private Shift shift;
    private EmployeeType employeeType;
    
    /**
     *
     * @param id employee's identification number of type int
     * @param firstName employee's first name of type String
     * @param middleName employee's middle name of type String 
     * @param lastName employee's last name of type String
     * @param active employee's active status of type LocalDateTime
     * @param badge employee's badge identification of type Badge
     * @param department employee's active department of type Department
     * @param shift employee's shift of type Shift
     * @param employeeType employee's shift type of type EmployeeType
     */
    public Employee(int id, String firstName, String middleName, String lastName, LocalDateTime active, Badge badge, Department department, Shift shift, EmployeeType employeeType) {
        
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.active = active;
        this.badge = badge;
        this.department = department;
        this.shift = shift;
        this.employeeType = employeeType;
        
    }
    
    /**
     *
     * @return
     * <p> getId getter method with type int that returns an id  </p>
     */
    public int getId() {
        return id;
    }
    
    /**
     *
     * @return
     * <p> getFirstName getter method with type String that returns the employee's first name  </p>
     */
    public String getFirstName() {
        return firstName;
    }
    
    /**
     *
     * @return
     * <p> getMiddleName getter method with type String that returns the employee's middle name  </p>
     */
    public String getMiddleName() {
        return middleName;
    }
    
    /**
     *
     * @return
     * <p> getLastName getter method with type String that returns the employee's last name  </p>
     */
    public String getLastName() {
        return lastName;
    }
    
    /**
     *
     * @return
     * <p> getActive getter method with type LocalDateTime that returns active status  </p>
     */
    public LocalDateTime getActive() {
        return active;
    }
    
    /**
     *
     * @return
     * <p> getBadge getter method with type Badge that returns badge </p>
     */
    public Badge getBadge() {
        return badge;
    }
    
    /**
     *
     * @return
     * <p> getDepartment getter method with type Department that returns department </p>
     */
    public Department getDepartment() {
        return department;
    }
    
    /**
     *
     * @return
     * <p> getShift getter method with type Shift that returns shift </p>
     */
    public Shift getShift() {
        return shift;
    }
    
    /**
     *
     * @return
     * <p> getEmployeeType getter method with type Employee type that returns the employee type </p>
     */
    public EmployeeType getEmployeeType() {
        return employeeType;
    }
    
    /**
     *
     * @return
     * <p> toString method </p>
     */
    @Override
    public String toString() {
        
        // Formats the date in the correct format
        // MM: month-of-year / dd: day-of-month / yyyy: year-of-era
        // Docs: https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
        final String date = DateTimeFormatter.ofPattern("MM/dd/yyyy").format(active);

        StringBuilder s = new StringBuilder();

        s.append("ID #").append(id).append(": ");
        s.append(badge.getDescription());
        s.append(" (#").append(badge.getId()).append("), ");
        s.append("Type: ").append(employeeType.toString()).append(", ");
        s.append("Department: ").append(department.getDescription()).append(", ");
        s.append("Active: ").append(date);
        
        return s.toString();
        
    }
}
