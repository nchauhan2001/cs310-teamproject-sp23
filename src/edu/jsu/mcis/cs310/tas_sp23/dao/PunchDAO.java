package edu.jsu.mcis.cs310.tas_sp23.dao;

import edu.jsu.mcis.cs310.tas_sp23.Badge;
import edu.jsu.mcis.cs310.tas_sp23.Employee;
import edu.jsu.mcis.cs310.tas_sp23.EventType;
import edu.jsu.mcis.cs310.tas_sp23.Punch;
import java.sql.*;
import java.time.LocalDateTime;

public class PunchDAO {
    
    private static final String QUERY_FIND = "SELECT * FROM event where id = ?";
    
    private final DAOFactory daoFactory;

    PunchDAO(DAOFactory daoFactory) {

        this.daoFactory = daoFactory;

    }
    
    public Punch find(int id){
        Punch punch = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                ps = conn.prepareStatement(QUERY_FIND);
                ps.setInt(1, id);

                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();

                    while (rs.next()) {
                        
                        id = rs.getInt("id");
                        Integer termid = rs.getInt("terminalid");
                        
                        String badgeid = rs.getString("badgeid");
                        
                        BadgeDAO badgedao = daoFactory.getBadgeDAO();
                        Badge badge = badgedao.find(badgeid);
                        
                        LocalDateTime origTimestamp = rs.getTimestamp("timestamp").toLocalDateTime();
                        Integer eventtypeid = rs.getInt("eventtypeid");
                        
                        EventType punchtype = EventType.values()[eventtypeid];

                        punch = new Punch(id, termid, badge, origTimestamp, punchtype);
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

        return punch;
    }
    
    public int create(Punch punch) {
        // WIP
        
        EmployeeDAO employeeDAO = daoFactory.getEmployeeDAO();

        Employee employee = employeeDAO.find(punch.getBadge());
        
        // Authorize the new punch, check to make sure that the terminal ID
        // that the punch originated from matches the ID of the designated clock terminal of the employee's department.
        // If it does not match, return 0
        if(punch.getTerminalId() != employee.getDepartment().getTerminalId()) {
            return 0;
        }
        
        
        return 0;
    }

}
