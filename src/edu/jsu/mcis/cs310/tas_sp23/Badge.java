package edu.jsu.mcis.cs310.tas_sp23;

import java.util.zip.CRC32;

/**
 *<p>  class that represents single employee badges. Part of the Time and Attendance Policy </p>
 * @author Team C
 */
public class Badge {

    private final String id, description;

    /**
     *
     * @param id represents the employee's badge identification
     * @param description represents the employee's badge description
     */
    public Badge(String id, String description) {
        this.id = id;
        this.description = description;
    }
    
    /**
     *
     * @param description represents the employee's badge description
     */
    public Badge(String description) {
        this.description = description;
        byte[] b = this.description.getBytes();

        CRC32 c = new CRC32();
        
        for (int b1: b){
            c.update(b1);  
        }
        
        String genId = Long.toHexString(c.getValue());
        int diff = 8 - genId.length();

        while (diff > 0){
            String formatId = String.format("0%1s", genId);
            genId = formatId;
            diff--;

        }
        this.id = genId.toUpperCase();
    }

    /**
     *
     * @return
     * <p> GetId getter method with type String that returns the id </p>
     */ 
    public String getId() {
        return id;
    }

    /**
     *
     * @return
     * <p> getDescription getter method with type String that returns the description </p>
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return
     * <p> toString Method outputs the method its state into a string </p>
     */
    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();

        s.append('#').append(id).append(' ');
        s.append('(').append(description).append(')');

        return s.toString();

    }

}
