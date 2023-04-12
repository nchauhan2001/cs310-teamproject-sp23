package edu.jsu.mcis.cs310.tas_sp23;

import edu.jsu.mcis.cs310.tas_sp23.dao.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BadgeCreateDeleteTest {

    private DAOFactory daoFactory;

    @Before
    public void setup() {

        daoFactory = new DAOFactory("tas.jdbc");

    }

    @Test
    public void testCreateBadge1() {

        /* Create Badges */

        Badge b1 = new Badge("Bies, Bill X");

        /* Compare Badge to Expected Value */
        
        assertEquals("#052B00DC (Bies, Bill X)", b1.toString());

    }
    
    @Test
    public void testCreateBadge2() {
        
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();

        /* Create New Badge Object */

        Badge b2 = new Badge("Smith, Daniel Q");
        
        /* Insert New Badge (delete first in case badge already exists) */
        
        badgeDAO.delete(b2.getId());
        boolean result = badgeDAO.create(b2);

        /* Compare Badge to Expected Value */
        
        assertEquals("#02AA8E86 (Smith, Daniel Q)", b2.toString());
        
        /* Check Insertion Result */
        
        assertEquals(true, result);

    }
    
    @Test
    public void testDeleteBadge1() {
        
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();

        /* Create New Badge Object */

        Badge b = new Badge("Haney, Debra F");
        
        /* Insert New Badge (delete first in case badge already exists) */
        
        badgeDAO.delete(b.getId());
        badgeDAO.create(b);
        
        /* Delete New Badge */
        
        boolean result = badgeDAO.delete(b.getId());

        /* Compare Badge to Expected Value */
        
        assertEquals("#8EA649AD (Haney, Debra F)", b.toString());
        
        /* Check Deletion Result */
        
        assertEquals(true, result);

    }
    @Test
    public void testCreateBadge3() {
        
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();

        /* Create New Badge Object */

        Badge b3 = new Badge("Deems, James D");
        
        /* Insert New Badge (delete first in case badge already exists) */
        
        badgeDAO.delete(b3.getId());
        boolean result = badgeDAO.create(b3);

        /* Compare Badge to Expected Value */
        
        assertEquals("#CD05B01A (Deems, James D)", b3.toString());
        
        /* Check Insertion Result */
        
        assertEquals(true, result);

    }
    
    @Test
    public void testDeleteBadge2() {
        
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();

        /* Create New Badge Object */

        Badge b4 = new Badge("Sharp, Matt R");
        
        /* Insert New Badge (delete first in case badge already exists) */
        
        badgeDAO.delete(b4.getId());
        badgeDAO.create(b4);
        
        /* Delete New Badge */
        
        boolean result = badgeDAO.delete(b4.getId());

        /* Compare Badge to Expected Value */
        
        assertEquals("#D79B32B4 (Sharp, Matt R)", b4.toString());
        
        /* Check Deletion Result */
        
        assertEquals(true, result);

    }
    
}
