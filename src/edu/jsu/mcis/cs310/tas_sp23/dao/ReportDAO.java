package edu.jsu.mcis.cs310.tas_sp23.dao;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import edu.jsu.mcis.cs310.tas_sp23.Badge;
import edu.jsu.mcis.cs310.tas_sp23.Department;
import edu.jsu.mcis.cs310.tas_sp23.Employee;
import edu.jsu.mcis.cs310.tas_sp23.EmployeeType;
import edu.jsu.mcis.cs310.tas_sp23.EventType;
import edu.jsu.mcis.cs310.tas_sp23.Punch;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

/**
 * <p>
 * CLASS DESCRIPTION </p>
 *
 * @author wahic
 */
public class ReportDAO {

    private static final String QUERY_SUMMARY = "SELECT * FROM employee WHERE departmentid = ? ORDER BY lastname, firstname, middlename";
    private static final String QUERY_SUMMARY2 = "SELECT * FROM employee ORDER BY lastname, firstname, middlename";

    private final DAOFactory daoFactory;

    /**
     * <p>
     * METHOD DESCRIPTION </p>
     *
     * @param daoFactory
     */
    ReportDAO(DAOFactory daoFactory) {

        this.daoFactory = daoFactory;

    }

    public String getBadgeSummary(Object id) {
        String s = "";

        JsonArray jsonArray = new JsonArray();

        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean hasresults;

        try {

            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                if (id == null) {
                    ps = conn.prepareStatement(QUERY_SUMMARY2);
                    hasresults = ps.execute();
                } else {
                    ps = conn.prepareStatement(QUERY_SUMMARY);
                    ps.setInt(1, Integer.parseInt(id.toString()));
                    hasresults = ps.execute();
                }

                if (hasresults) {

                    rs = ps.getResultSet();

                    while (rs.next()) {
                        JsonObject jsonObject = new JsonObject();

                        String badgeid = rs.getString("badgeid");
                        BadgeDAO badgedao = daoFactory.getBadgeDAO();
                        Badge badge = badgedao.find(badgeid);
                        int departmentID;

                        if (id != null) {
                            departmentID = Integer.parseInt(id.toString());
                        } else {
                            departmentID = rs.getInt("departmentid");
                        }

                        DepartmentDAO departmentdao = daoFactory.getDepartmentDAO();

                        Department department = departmentdao.find(departmentID);
                        jsonObject.put("department", department.getDescription());
                        jsonObject.put("name", badge.getDescription());
                        jsonObject.put("badgeid", badgeid);

                        int employeeTypeid = rs.getInt("employeetypeid");
                        EmployeeType employeeType = EmployeeType.values()[employeeTypeid];

                        String type;
                        if (employeeType.equals(EmployeeType.FULL_TIME)) {
                            type = "Full-Time Employee";
                        } else {
                            type = "Temporary Employee";
                        }

                        jsonObject.put("type", type);

                        jsonArray.add(jsonObject);
                    }
                    s = Jsoner.serialize(jsonArray);
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

        return s;
    }

    public String getWhosInWhosOut(LocalDateTime localDateTime, Integer department) {

        EmployeeDAO employeeDAO = daoFactory.getEmployeeDAO();
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        PunchDAO punchDAO = daoFactory.getPunchDAO();

        JsonArray badges = Jsoner.deserialize(getBadgeSummary(department), new JsonArray());

        JsonArray jsonArray = new JsonArray();

        for (int i = 0; i < badges.size(); i++) {

            JsonObject jsonObj = (JsonObject) badges.get(i);

            String badgeid = (String) jsonObj.get("badgeid");
            Badge badge = badgeDAO.find(badgeid);

            Employee employee = employeeDAO.find(badge);

            ArrayList<Punch> punches = punchDAO.list(badge, localDateTime.toLocalDate());
            LinkedHashMap<String, String> jsonData = new LinkedHashMap<>();

            for (int j = 0; j < punches.size(); j++) {

                Punch punch = punches.get(j);

                LocalDateTime originalTimeStamp = punch.getOriginalTimestamp();

                if (originalTimeStamp.isAfter(localDateTime)) {

                    if (j - 1 >= 0) {
                        punch = punches.get(j - 1);

                        String status = punch.getPunchType() == EventType.CLOCK_IN ? "In" : "Out";
                        final String dateFormat = "E MM/dd/yyyy HH:mm:ss";

                        jsonData.put("arrived", DateTimeFormatter.ofPattern(dateFormat).format(punch.getOriginalTimestamp()).toUpperCase());
                        jsonData.put("employeetype", (String) jsonObj.get("type"));
                        jsonData.put("firstname", employee.getFirstName());
                        jsonData.put("badgeid", (String) jsonObj.get("badgeid"));
                        jsonData.put("shift", employee.getShift().getDescription());
                        jsonData.put("lastname", employee.getLastName());
                        jsonData.put("status", status);

                    } else {

                        jsonData.put("employeetype", (String) jsonObj.get("type"));
                        jsonData.put("firstname", employee.getFirstName());
                        jsonData.put("badgeid", (String) jsonObj.get("badgeid"));
                        jsonData.put("shift", employee.getShift().getDescription());
                        jsonData.put("lastname", employee.getLastName());
                        jsonData.put("status", "Out");

                    }

                    break;

                }

            }

            if (punches.size() - 1 <= 0) {

                jsonData.put("employeetype", (String) jsonObj.get("type"));
                jsonData.put("firstname", employee.getFirstName());
                jsonData.put("badgeid", (String) jsonObj.get("badgeid"));
                jsonData.put("shift", employee.getShift().getDescription());
                jsonData.put("lastname", employee.getLastName());
                jsonData.put("status", "Out");

            }

            if (!jsonData.isEmpty()) {
                jsonArray.add(jsonData);
            }

        }

        ArrayList<LinkedHashMap> inEmployees = new ArrayList<>();
        ArrayList<LinkedHashMap> outEmployees = new ArrayList<>();

        for (Object obj : jsonArray) {

            LinkedHashMap linkedHM = (LinkedHashMap) obj;

            if (linkedHM.get("status").equals("In")) {

                inEmployees.add(linkedHM);

            } else {

                outEmployees.add(linkedHM);

            }
        }

        Collections.sort(inEmployees, new Comparator() {

            public int compare(Object o1, Object o2) {

                String e1 = (String) ((LinkedHashMap) o1).get("employeetype");
                String e2 = (String) ((LinkedHashMap) o2).get("employeetype");

                if (e1.equals(e2)) {
                    return 0;
                }
                if (e1.equals("Full-Time Employee")) {
                    return -1;
                }
                return 1;

            }

        });

        Collections.sort(outEmployees, new Comparator() {

            public int compare(Object o1, Object o2) {

                String e1 = (String) ((LinkedHashMap) o1).get("employeetype");
                String e2 = (String) ((LinkedHashMap) o2).get("employeetype");

                if (e1.equals(e2)) {
                    return 0;
                }
                if (e1.equals("Full-Time Employee")) {
                    return -1;
                }
                return 1;

            }
        });

        jsonArray = new JsonArray();

        jsonArray.addAll(inEmployees);
        jsonArray.addAll(outEmployees);

        return Jsoner.serialize(jsonArray);
    }

    public String getHoursSummary(LocalDate date, Integer departmentID, EmployeeType employeeType) {
        String s = "";

        JsonArray jsonArray = new JsonArray();

        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean hasresults;

        try {
            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {
                StringBuilder queryBuilder = new StringBuilder();
                queryBuilder.append("SELECT employee.id, badge.description, badge.id AS badgeid, employee.firstname, employee.middlename, employee.lastname, employee.employeetypeid, employee.shift, timecard.punchtypeid, timecard.originaltimestamp, timecard.adjustedtimestamp, timecard.notes ");
                queryBuilder.append("FROM employee ");
                queryBuilder.append("LEFT JOIN badge ON employee.badgeid = badge.id ");
                queryBuilder.append("LEFT JOIN timecard ON employee.id = timecard.employeeid ");
                queryBuilder.append("WHERE timecard.originaltimestamp >= ? AND timecard.originaltimestamp < ? ");
                if (departmentID != null) {
                    queryBuilder.append("AND employee.departmentid = ? ");
                }
                if (employeeType != null) {
                    queryBuilder.append("AND employee.employeetypeid = ? ");
                }
                queryBuilder.append("ORDER BY employee.lastname, employee.firstname, employee.middlename");

                ps = conn.prepareStatement(queryBuilder.toString());
                LocalDateTime start = date.atTime(0, 0, 0);
                LocalDateTime end = date.plusDays(1).atTime(0, 0, 0);
                ps.setTimestamp(1, Timestamp.valueOf(start));
                ps.setTimestamp(2, Timestamp.valueOf(end));
                int index = 3;
                if (departmentID != null) {
                    ps.setInt(index, departmentID);
                    index++;
                }
                if (employeeType != null) {
                    ps.setInt(index, employeeType.ordinal());
                }

                hasresults = ps.execute();

                if (hasresults) {
                    rs = ps.getResultSet();

                    while (rs.next()) {
                        int employeeID = rs.getInt("id");
                        String badgeDescription = rs.getString("description");
                        String badgeID = rs.getString("badgeid");
                        String firstName = rs.getString("firstname");
                        String middleName = rs.getString("middlename");
                        String lastName = rs.getString("lastname");
                        EmployeeType type = EmployeeType.values()[rs.getInt("employeetypeid")];
                        String shift = rs.getString("shift");
                        int punchTypeID = rs.getInt("punchtypeid");
                        LocalDateTime originalTimeStamp = rs.getTimestamp("originaltimestamp").toLocalDateTime();
                        LocalDateTime adjustedTimeStamp = rs.getTimestamp("adjustedtimestamp") != null ? rs.getTimestamp("adjustedtimestamp").toLocalDateTime() : null;
                        String notes = rs.getString("notes");

                    

}
