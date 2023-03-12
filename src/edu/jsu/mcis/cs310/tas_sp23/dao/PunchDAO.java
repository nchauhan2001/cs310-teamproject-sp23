package edu.jsu.mcis.cs310.tas_sp23.dao;

import edu.jsu.mcis.cs310.tas_sp23.Badge;
import edu.jsu.mcis.cs310.tas_sp23.Employee;
import edu.jsu.mcis.cs310.tas_sp23.EventType;
import edu.jsu.mcis.cs310.tas_sp23.Punch;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PunchDAO {

    private static final String QUERY_FIND = "SELECT * FROM event where id = ?";
    private static final String QUERY_CREATE = "INSERT INTO event (terminalid, badgeid, timestamp, eventtypeid) VALUES (?, ?, ?, ?)";
    private static final String QUERY_LIST = "SELECT * FROM event WHERE badgeid = ? AND DATE(timestamp) = ? ORDER BY timestamp";

    private final DAOFactory daoFactory;

    PunchDAO(DAOFactory daoFactory) {

        this.daoFactory = daoFactory;

    }

    public Punch find(int id) {
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

        int key = 0;
        ResultSet rs = null;

        EmployeeDAO employeeDAO = daoFactory.getEmployeeDAO();

        Employee employee = employeeDAO.find(punch.getBadge());

        // Authorize the new punch, check to make sure that the terminal ID
        // that the punch originated from matches the ID of the designated clock terminal of the employee's department.
        // If the terminalId of the new punch is zero, this indicates that the new punch is being added manually to the database
        // by the administrative staff and will be allowed through the authorization
        if (punch.getTerminalId() == employee.getDepartment().getTerminalId() || punch.getTerminalId() == 0) {

            PreparedStatement ps = null;

            try {

                Connection conn = daoFactory.getConnection();

                if (conn.isValid(0)) {

                    final String date = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(punch.getOriginalTimestamp());
                    final int eventTypeId = EventType.valueOf(punch.getPunchType().name()).ordinal();

                    ps = conn.prepareStatement(QUERY_CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
                    ps.setInt(1, punch.getTerminalId());
                    ps.setString(2, punch.getBadge().getId());
                    ps.setString(3, date);
                    ps.setInt(4, eventTypeId);

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
        }

        return key;
    }

    public ArrayList<Punch> list(Badge badge, LocalDate date) {
        ArrayList<Punch> punches = new ArrayList<>();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {
                ps = conn.prepareStatement(QUERY_LIST);
                ps.setString(1, badge.getId());
                ps.setDate(2, Date.valueOf(date));

                boolean hasResults = ps.execute();

                if (hasResults) {
                    rs = ps.getResultSet();

                    while (rs.next()) {
                        int id = rs.getInt("id");
                        int terminalId = rs.getInt("terminalid");
                        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
                        Badge punchBadge = badgeDAO.find(rs.getString("badgeid"));
                        LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
                        EventType eventType = EventType.values()[rs.getInt("eventtypeid")];

                        // Declare the Badge variable before passing it to Punch constructor
                        Punch punch = new Punch(id, terminalId, punchBadge, timestamp, eventType);
                        punches.add(punch);
                    }
                }
                
                // Check for the last punch of the day and add an "extra" punch if needed
                Punch lastPunch = punches.get(punches.size() - 1);

                if (lastPunch.getPunchType() == EventType.CLOCK_IN){
                    LocalDate nextFirstPunch = lastPunch.getOriginalTimestamp().toLocalDate().plusDays(1);

                    ps = conn.prepareStatement(QUERY_LIST);

                    ps.setString(1, badge.getId());
                    ps.setDate(2, Date.valueOf(nextFirstPunch));
                    hasResults = ps.execute();
                    
                    if (hasResults){
                        rs = ps.getResultSet();
                        rs.next();

                        EventType nextPunchType = EventType.values()[rs.getInt("eventtypeid")];

                        if (nextPunchType == EventType.CLOCK_OUT || nextPunchType == EventType.TIME_OUT){
                            Punch punch = find(rs.getInt("id"));
                            
                            punches.add(punch);
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
        return punches;
    }

    // List method for punches from a range of dates - W.H.
    public ArrayList<Punch> list(Badge badge, LocalDate begin, LocalDate end) {
        ArrayList<Punch> list = new ArrayList();
        LocalDate date = begin;
        while (date.isBefore(end) || date.equals(end)) {
            ArrayList<Punch> entries = new ArrayList();
            try {
                entries = list(badge, date);
            } catch (IndexOutOfBoundsException e) {
            }

            if (!entries.isEmpty() && !list.isEmpty()) {
                if (list.get(list.size() - 1).toString().equals(entries.get(0).toString())) {
                    list.remove(list.size() - 1);
                }
            }
            list.addAll(entries);
            date = date.plusDays(1);
        }
        return list;
    }
}
