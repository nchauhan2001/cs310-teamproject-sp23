package edu.jsu.mcis.cs310.tas_sp23.dao;

import java.sql.*;
import java.util.*;
import edu.jsu.mcis.cs310.tas_sp23.Badge;
import edu.jsu.mcis.cs310.tas_sp23.Shift;

public class ShiftDAO {

    private static final String QUERY_FIND = "select * from shift join dailyschedule on shift.dailyscheduleid = dailyschedule.id where dailyschedule.id = ?";
    private static final String QUERY_BADGE = "SELECT * FROM employee WHERE badgeid = ?";
    private final DAOFactory daoFactory;

    ShiftDAO(DAOFactory daoFactory) {

        this.daoFactory = daoFactory;

    }

    public Shift find(int id) {
        Shift shift = null;

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
                        
                        HashMap<String, String> map = new HashMap<>();
                        
                        id = rs.getInt("shift.id");
                        String shiftid = String.valueOf(id);
                        map.put("id", shiftid);
                        
                        String description = rs.getString("shift.description");
                        map.put("description", description);
                        
                        String startTime = rs.getString("shiftstart");
                        map.put("startTime", startTime);
                        
                        String stopTime = rs.getString("shiftstop");
                        map.put("stopTime", stopTime);
                        
                        String roundInterval = rs.getString("roundInterval");
                        map.put("roundInterval", roundInterval);
                        
                        String gracePeriod = rs.getString("graceperiod");
                        map.put("gracePeriod", gracePeriod);
                        
                        String dockPenalty = rs.getString("dockpenalty");
                        map.put("dockPenalty", dockPenalty);
                        
                        String lunchStop = rs.getString("lunchstop");
                        map.put("lunchStop", lunchStop);
                        
                        String lunchStart = rs.getString("lunchstart");
                        map.put("lunchStart", lunchStart);
                        
                        String lunchThreshold = rs.getString("lunchthreshold");
                        map.put("lunchThreshold", lunchThreshold);
                        
                        shift = new Shift(map);
                      
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

        return shift;
    }
    
    public Shift find(Badge badge){
        Shift shift = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {
                
                ps = conn.prepareStatement(QUERY_BADGE);
                ps.setString(1, badge.getId());

                boolean hasresults = ps.execute();
                
                if (hasresults) {

                    rs = ps.getResultSet();

                    while (rs.next()) {
                        
                        int id = rs.getInt("shiftid");

                        ShiftDAO shiftDAO = daoFactory.getShiftDAO();
                        shift = shiftDAO.find(id);
                        
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
        
        return shift;
    }
    
}
