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
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        PunchDAO punchDAO = daoFactory.getPunchDAO();
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        
        Punch p5 = new Punch(103, badgeDAO.find("8D9E5710"), EventType.CLOCK_OUT);
        LocalDateTime ots, rts;
        
        String badgeid = p5.getBadge().getId();
        ots = p5.getOriginalTimestamp();
        int terminalid = p5.getTerminalId();
        EventType punchtype = p5.getPunchType();
        
        int punchid = punchDAO.create(p5);
        
        Punch p6 = punchDAO.find(punchid);
        
        assertEquals(badgeid, p6.getBadge().getId());
        
        rts = p6.getOriginalTimestamp();
        
        assertEquals(terminalid, p6.getTerminalId());
        assertEquals(punchtype, p6.getPunchType());
        assertEquals(ots.format(dtf), rts.format(dtf));
        
    }
    
    @Test
    public void testCreatePunch4(){
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        PunchDAO punchDAO = daoFactory.getPunchDAO();
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        
        Punch p7 = new Punch(107, badgeDAO.find("DFE4EB13"), EventType.CLOCK_OUT);
        LocalDateTime ots, rts;
        
        String badgeid = p7.getBadge().getId();
        ots = p7.getOriginalTimestamp();
        int terminalid = p7.getTerminalId();
        EventType punchtype = p7.getPunchType();
        
        int punchid = punchDAO.create(p7);
        
        Punch p8 = punchDAO.find(punchid);
        
        assertEquals(badgeid, p8.getBadge().getId());
        
        rts = p8.getOriginalTimestamp();
        
        assertEquals(terminalid, p8.getTerminalId());
        assertEquals(punchtype, p8.getPunchType());
        assertEquals(ots.format(dtf), rts.format(dtf));
    }
    
    @Test
    public void testCreatePunch5(){
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        PunchDAO punchDAO = daoFactory.getPunchDAO();
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        
        Punch p9 = new Punch(0, badgeDAO.find("4382D92D"), EventType.TIME_OUT);
        LocalDateTime ots, rts;
        
        String badgeid = p9.getBadge().getId();
        ots = p9.getOriginalTimestamp();
        int terminalid = p9.getTerminalId();
        EventType punchtype = p9.getPunchType();
        
        int punchid = punchDAO.create(p9);
        
        Punch p10 = punchDAO.find(punchid);
        
        assertEquals(badgeid, p10.getBadge().getId());
        
        rts = p10.getOriginalTimestamp();
        
        assertEquals(terminalid, p10.getTerminalId());
        assertEquals(punchtype, p10.getPunchType());
        assertEquals(ots.format(dtf), rts.format(dtf));
    }
    
    @Test
    public void testCreatePunch6(){
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        PunchDAO punchDAO = daoFactory.getPunchDAO();
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        
        Punch p11 = new Punch(0, badgeDAO.find("ADD650A8"), EventType.TIME_OUT);
        LocalDateTime ots, rts;
        
        String badgeid = p11.getBadge().getId();
        ots = p11.getOriginalTimestamp();
        int terminalid = p11.getTerminalId();
        EventType punchtype = p11.getPunchType();
        
        int punchid = punchDAO.create(p11);
        
        Punch p12 = punchDAO.find(punchid);
        
        assertEquals(badgeid, p12.getBadge().getId());
        
        rts = p12.getOriginalTimestamp();
        
        assertEquals(terminalid, p12.getTerminalId());
        assertEquals(punchtype, p12.getPunchType());
        assertEquals(ots.format(dtf), rts.format(dtf));
    }
}
