package edu.jsu.mcis.cs310.tas_sp23.dao;

import edu.jsu.mcis.cs310.tas_sp23.Absenteeism;
import edu.jsu.mcis.cs310.tas_sp23.Employee;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;

public class AbsenteeismDAO {

    private final DAOFactory daoFactory;
    
    private static final String QUERY_FIND = "SELECT * FROM absenteeism WHERE employeeid = ? AND payperiod = ?";
    private static final String QUERY_UPDATE = "UPDATE absenteeism SET percentage = ? WHERE employeeid = ? AND payperiod = ?";
    private static final String QUERY_CREATE = "INSERT INTO absenteeism (employeeid, payperiod, percentage) VALUES (?, ?, ?)";

    AbsenteeismDAO( DAOFactory daoFactory) {

        this.daoFactory = daoFactory;

    }
    
    public Absenteeism find(Employee employee, LocalDate localDate) {
        Absenteeism absenteeism = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {
                                   
                ps = conn.prepareStatement(QUERY_FIND);
                ps.setInt(1, employee.getId());
                LocalDate payPeriod = localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
                ps.setDate(2, Date.valueOf(payPeriod));

                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();

                    while (rs.next()) {

                        BigDecimal percentage = rs.getBigDecimal("percentage");
                        percentage = percentage.setScale(2, RoundingMode.HALF_UP);
                        absenteeism = new Absenteeism(employee, payPeriod, percentage);

                    }

                }

            }

        } catch (SQLException e) {

            throw new DAOException(e.getMessage());

        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }

        }

        return absenteeism;

    }
    
    public int create(Absenteeism absenteeism) {
        
        int key = 0;
            
            PreparedStatement ps = null;
            ResultSet rs = null;
                    
            try {
                
            Connection conn = daoFactory.getConnection();

                if (conn.isValid(0)) {
                    
                    // Find if record already exists
                    if(find(absenteeism.getEmployee(), absenteeism.getPayPeriod()) != null) {
            
                        ps = conn.prepareStatement(QUERY_UPDATE, PreparedStatement.RETURN_GENERATED_KEYS);
                        ps.setBigDecimal(1, absenteeism.getPercentage());
                        ps.setInt(2, absenteeism.getEmployee().getId());
                        ps.setDate(3, Date.valueOf(absenteeism.getPayPeriod()));
                        
                    } else { // If record does not exists already
                        LocalDate payPeriod = absenteeism.getPayPeriod().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
                        
                        ps = conn.prepareStatement(QUERY_CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
                        ps.setInt(1, absenteeism.getEmployee().getId());
                        ps.setDate(2, Date.valueOf(payPeriod));
                        ps.setBigDecimal(3, absenteeism.getPercentage());
                    }
                    
                    int result = ps.executeUpdate();
                    if (result > 0) {

                        rs = ps.getGeneratedKeys();
                        if (rs.next()) {
                            key = rs.getInt(1);
                        }

                    }
                }

            } catch (SQLException e) {

                throw new DAOException(e.getMessage());

            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        throw new DAOException(e.getMessage());
                    }
                }
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException e) {
                        throw new DAOException(e.getMessage());
                    }
                }
            }         
        
        return key;
        
    }
    
}
