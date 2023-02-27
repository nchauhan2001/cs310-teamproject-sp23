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
    
    
    
    public Employee(int id, String firstName, String middleName, String lastName, LocalDateTime actice, Badge badge, Department department, Shift shift, EmployeeType employeeType) {
        
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
    
    @Override
    public String toString() {
        
        // Formats the date in the correct format
        // MM: month-of-year / dd: day-of-month / yyyy: year-of-era
        // Docs: https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
        final String date = DateTimeFormatter.ofPattern("MM/dd/yyyy").format(active).toUpperCase();

        StringBuilder s = new StringBuilder();

        s.append("ID #").append(id).append(": ");
        s.append(lastName).append(", ").append(firstName).append(" ").append(middleName);
        s.append(" (#").append(badge.getId()).append("), ");
        s.append("Type: ").append(employeeType.toString()).append(", ");
        s.append("Department: ").append(department.getDescription()).append(", ");
        s.append("Active: ").append(date);
        
        return s.toString();
        
    }
}
