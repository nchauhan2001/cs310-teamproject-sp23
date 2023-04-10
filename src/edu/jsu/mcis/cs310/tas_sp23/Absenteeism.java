package edu.jsu.mcis.cs310.tas_sp23;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *<p> This feature provides an Absenteeism model object class which will be used to represent an employee's absenteeism record for a specific pay period.</p> 
 * @author Team C
 */
public class Absenteeism {
    
    private Employee employee;
    private LocalDate payPeriod;
    private BigDecimal percentage;
    
    /**
     *
     * @param employee the employee's badge number 
     * @param localDate starting pay period
     * @param percentage percentage of time the employee missed
     */
    public Absenteeism(Employee employee, LocalDate localDate, BigDecimal percentage){
        this.employee = employee;
        this.payPeriod = localDate;
        this.percentage = percentage;
    }
    
    /**
     *<p> getter method 'getEmployee' returns employee </p>
     * @return
     */
    public Employee getEmployee(){
        return employee;
    }

    /**
     *<p> getter method 'getPayPeriod' returns PayPeriod </p>
     * @return
     */
    public LocalDate getPayPeriod(){
        return payPeriod;
    }

    /**
     *<p> getter method 'getPercentage' returns percentage </p>
     * @return
     */
    public BigDecimal getPercentage(){
        return percentage;
    }
    
    /**
     *<p> toString method that returns s as a string </p>
     * @return
     */
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        String date = DateTimeFormatter.ofPattern("MM-dd-yyyy").format(payPeriod);
        
        s.append("#").append(employee.getBadge().getId());
        s.append(" (Pay Period Starting ").append(date).append("): ");
        s.append(percentage).append("%");
        
        return s.toString();
    }
}
