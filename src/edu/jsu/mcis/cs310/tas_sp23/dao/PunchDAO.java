package edu.jsu.mcis.cs310.tas_sp23.dao;

import edu.jsu.mcis.cs310.tas_sp23.Badge;
import edu.jsu.mcis.cs310.tas_sp23.EventType;
import edu.jsu.mcis.cs310.tas_sp23.Punch;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Objects;

public class PunchDAO {
    
    // also selecting eventtype to use the descriptions when determining which type of event
    // since there's two "id" columns in this query, event.id points to the id from event
    // and eventtype.id points to the id from eventtype
    private static final String QUERY_FIND = "SELECT * FROM event, eventtype where event.id = ?";
    
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
                        
                        id = rs.getInt("event.id");
                        Integer termid = rs.getInt("terminalid");
                        
                        String badgeid = rs.getString("badgeid");
                        Badge badge = new Badge(badgeid, null);
                        
                        LocalDateTime origTimestamp = rs.getTimestamp("timestamp").toLocalDateTime();
                        Integer eventtype = rs.getInt("eventtypeid");
                        Integer eventtypeid = rs.getInt("eventtype.id");
                        
                        EventType punchtype;

                        if (eventtype == eventtypeid){
                            String eventDescription = rs.getString("description");
                            
                            switch (eventDescription) {
                                case "Clock-In Punch":
                                    punchtype = EventType.CLOCK_IN;
                                    break;
                                case "Clock-Out Punch":
                                    punchtype = EventType.CLOCK_OUT;
                                    break;
                                default:
                                    punchtype = EventType.TIME_OUT;
                                    break;
                            }
                            punch = new Punch(id, termid, badge, origTimestamp, punchtype);
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

        return punch;
    }

}
