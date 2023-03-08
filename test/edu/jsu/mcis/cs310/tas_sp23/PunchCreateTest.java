package edu.jsu.mcis.cs310.tas_sp23;

import edu.jsu.mcis.cs310.tas_sp23.dao.BadgeDAO;
import edu.jsu.mcis.cs310.tas_sp23.dao.DAOFactory;
import edu.jsu.mcis.cs310.tas_sp23.dao.PunchDAO;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.*;
import static org.junit.Assert.*;

public class PunchCreateTest {

    private DAOFactory daoFactory;

    @Before
    public void setup() {

        daoFactory = new DAOFactory("tas.jdbc");

    }

    @Test
    public void testCreatePunch1() {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        PunchDAO punchDAO = daoFactory.getPunchDAO();
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();

        /* Create New Punch Object */
        
        Punch p1 = new Punch(103, badgeDAO.find("021890C0"), EventType.CLOCK_IN);

        /* Create Timestamp Objects */
        
        LocalDateTime ots, rts;

        /* Get Punch Properties */
        
        String badgeid = p1.getBadge().getId();
        ots = p1.getOriginalTimestamp();
        int terminalid = p1.getTerminalId();
        EventType punchtype = p1.getPunchType();

        /* Insert Punch Into Database */
        
        int punchid = punchDAO.create(p1);

        /* Retrieve New Punch */
        
        Punch p2 = punchDAO.find(punchid);

        /* Compare Punches */
        
        assertEquals(badgeid, p2.getBadge().getId());

        rts = p2.getOriginalTimestamp();

        assertEquals(terminalid, p2.getTerminalId());
        assertEquals(punchtype, p2.getPunchType());
        assertEquals(ots.format(dtf), rts.format(dtf));

    }
    
    @Test
    public void testCreatePunch2(){
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        PunchDAO punchDAO = daoFactory.getPunchDAO();
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        
        Punch p3 = new Punch(107, badgeDAO.find("2A5620A0"), EventType.CLOCK_IN);
        
        /* Create Timestamp Objects */
        
        LocalDateTime ots, rts;

        /* Get Punch Properties */
        
        String badgeid = p3.getBadge().getId();
        ots = p3.getOriginalTimestamp();
        int terminalid = p3.getTerminalId();
        EventType punchtype = p3.getPunchType();

        /* Insert Punch Into Database */
        
        int punchid = punchDAO.create(p3);

        /* Retrieve New Punch */
        
        Punch p4 = punchDAO.find(punchid);

        /* Compare Punches */
        
        assertEquals(badgeid, p4.getBadge().getId());

        rts = p4.getOriginalTimestamp();

        assertEquals(terminalid, p4.getTerminalId());
        assertEquals(punchtype, p4.getPunchType());
        assertEquals(ots.format(dtf), rts.format(dtf));
    }
    
    @Test
    public void testCreatePunch3(){
        
    }
}
