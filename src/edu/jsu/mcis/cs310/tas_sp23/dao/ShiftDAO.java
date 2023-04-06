package edu.jsu.mcis.cs310.tas_sp23.dao;

import java.sql.*;
import java.util.*;
import edu.jsu.mcis.cs310.tas_sp23.Badge;
import edu.jsu.mcis.cs310.tas_sp23.DailySchedule;
import edu.jsu.mcis.cs310.tas_sp23.Shift;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class ShiftDAO {

    private static final String QUERY_FIND = "select * from shift join dailyschedule on shift.dailyscheduleid = dailyschedule.id where dailyschedule.id = ?";
    private static final String QUERY_BADGE = "SELECT * FROM employee WHERE badgeid = ?";
    private static final String QUERY_BADGE_V2 = "select * from scheduleoverride join dailyschedule on scheduleoverride.dailyscheduleid = dailyschedule.id";
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
                        
                        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++){
                            String key = rs.getMetaData().getColumnName(i);
                            String value = rs.getString(i);
                            map.put(key, value);
                        }
                        
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
    
    public Shift find(Badge badge, LocalDate localDate){
        Shift shift = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {
                
                ps = conn.prepareStatement(QUERY_BADGE_V2);
                
                boolean hasresults = ps.execute();
                
                if (hasresults) {

                    rs = ps.getResultSet();
                    
                    ShiftDAO shiftdao = daoFactory.getShiftDAO();
                    shift = shiftdao.find(badge);

                    while (rs.next()) {
                        
                        LocalDate date = rs.getTimestamp("start").toLocalDateTime().toLocalDate();
                        int dayNum = rs.getInt("day");
                        DayOfWeek day = localDate.getDayOfWeek().plus(dayNum);
                        String end = rs.getString("end");
                        DailySchedule daily;
                        boolean recurring;
                        
                        if (end == null && rs.getString("badgeid").equals(badge.getId())){
                            recurring = true;
                        }
                        else{
                            recurring = false;
                        }
                        
                        if (recurring){
                            if (localDate.equals(date) || localDate.isAfter(date)){
                                
                                int count = rs.findColumn("dailyschedule.id");
                                HashMap<String, String> map = new HashMap<>();

                                for (int i = count; i <= rs.getMetaData().getColumnCount(); i++){
                                    String key = rs.getMetaData().getColumnName(i);
                                    String value = rs.getString(i);
                                    map.put(key, value);
                                }

                                map.put("description", shift.getDescription());

                                daily = new DailySchedule(map);
                                shift.setDailyschedule(day, daily);
                            }
                        }
                        else{
                            if (localDate.equals(date)){
                            
                                int count = rs.findColumn("dailyschedule.id");
                                HashMap<String, String> map = new HashMap<>();

                                for (int i = count; i <= rs.getMetaData().getColumnCount(); i++){
                                    String key = rs.getMetaData().getColumnName(i);
                                    String value = rs.getString(i);
                                    map.put(key, value);
                                }

                                map.put("description", shift.getDescription());

                                daily = new DailySchedule(map);

                                if (rs.getString("badgeid") == null){
                                    shift.setDailyschedule(day, daily);

                                }
                                else if (badge.getId().equals(rs.getString("badgeid"))){

                                    shift.setDailyschedule(day, daily);

                                }
                            }
                        
                        }
                        
                        
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
