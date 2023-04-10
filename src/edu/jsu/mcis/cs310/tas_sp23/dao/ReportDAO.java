package edu.jsu.mcis.cs310.tas_sp23.dao;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import edu.jsu.mcis.cs310.tas_sp23.Badge;
import edu.jsu.mcis.cs310.tas_sp23.Department;
import edu.jsu.mcis.cs310.tas_sp23.EmployeeType;
import java.sql.*;

public class ReportDAO {
    
    private static final String QUERY_SUMMARY = "SELECT * FROM employee WHERE departmentid = ? ORDER BY lastname";
    private static final String QUERY_SUMMARY2 = "SELECT * FROM employee ORDER BY lastname";
    
    private final DAOFactory daoFactory;

    ReportDAO(DAOFactory daoFactory) {

        this.daoFactory = daoFactory;

    }
    
    public String getBadgeSummary(Object id){
        String s = "";
        
        JsonArray jsonArray = new JsonArray();
        
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                ps = conn.prepareStatement(QUERY_SUMMARY);
                int departmentID = Integer.parseInt(id.toString());
                ps.setInt(1, departmentID);

                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();

                    while (rs.next()) {
                        JsonObject jsonObject = new JsonObject();
                        
                        String badgeid = rs.getString("badgeid");
                        BadgeDAO badgedao = daoFactory.getBadgeDAO();
                        Badge badge = badgedao.find(badgeid);
                        
                        DepartmentDAO departmentdao = daoFactory.getDepartmentDAO();
                        Department department = departmentdao.find(departmentID);
                        
                        jsonObject.put("name", badge.getDescription());
                        jsonObject.put("badgeid", badgeid);
                        jsonObject.put("department", department.getDescription());
                        
                        int employeeTypeid = rs.getInt("employeetypeid");
                        EmployeeType employeeType = EmployeeType.values()[employeeTypeid];
                        
                        String type;
                        if (employeeType.equals(EmployeeType.FULL_TIME)){
                            type = "Full-Time Employee";
                        }
                        else{
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
    
}
