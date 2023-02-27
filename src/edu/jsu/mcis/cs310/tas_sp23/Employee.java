package edu.jsu.mcis.cs310.tas_sp23;

import java.time.LocalDateTime;

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
}
