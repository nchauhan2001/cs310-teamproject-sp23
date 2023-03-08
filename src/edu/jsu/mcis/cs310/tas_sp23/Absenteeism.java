package edu.jsu.mcis.cs310.tas_sp23;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Absenteeism {
    
    private Employee employee;
    private LocalDate localDate;
    private BigDecimal percentage;
    
    public Absenteeism(Employee employee, LocalDate localDate, BigDecimal percentage){
        this.employee = employee;
        this.localDate = localDate;
        this.percentage = percentage;
    }
    
    public Employee getEmployee(){
        return employee;
    }
    public LocalDate getLocalDate(){
        return localDate;
    }
    public BigDecimal getPercentage(){
        return percentage;
    }
    
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        String date = DateTimeFormatter.ofPattern("MM-dd-yyyy").format(localDate);
        
        s.append("#").append(employee.getBadge().getId());
        s.append(" (Pay Period Starting ").append(date).append("): ");
        s.append(percentage).append("%");
        
        return s.toString();
    }
}
