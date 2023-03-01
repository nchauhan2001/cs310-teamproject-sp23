package edu.jsu.mcis.cs310.tas_sp23;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Employee {
    
    private int id;
    private String firstName, middleName, lastName;
    private LocalDateTime active;
    private Badge badge;
    private Department department;
    private Shift shift;
    private EmployeeType employeeType;
    
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
    
    public int getId() {
        return id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getMiddleName() {
        return middleName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public LocalDateTime getActive() {
        return active;
    }
    
    public Badge getBadge() {
        return badge;
    }
    
    public Department getDepartment() {
        return department;
    }
    
    public Shift getShift() {
        return shift;
    }
    
    public EmployeeType getEmployeeType() {
        return employeeType;
    }
    
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
