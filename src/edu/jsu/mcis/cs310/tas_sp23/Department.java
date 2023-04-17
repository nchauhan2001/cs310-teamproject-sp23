package edu.jsu.mcis.cs310.tas_sp23;

/**
 *
 * @author Team C
 * <p> provides a Department model object class which will be used to represent departments. </p>
 */
public class Department {

    private int id, terminalId;
    private final String description;
    
    /**
     *
     * @param id private integer numeric identification
     * @param desc private description
     * @param termid indicates the terminal number that employees assigned to this department are permitted to use as a time clock
     */
    public Department(int id, String desc, int termid) {
        this.id = id;
        this.description = desc;
        this.terminalId = termid;
    }
    
    /**
     *
     * @return
     * <p> getId method with type int that returns the id number </p>
     */
    public int getId() {
        return id;
    }
    
    /**
     *
     * @return
     * <p> getDescription method with type String that returns the description </p>
     */
    public String getDescription() {
        return description;
    }
    
    /**
     *
     * @return
     * <p> getTerminalId method with type int that returns the terminal Id </p>
     */
    public int getTerminalId() {
        return terminalId;
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
        s.append('(').append(description).append("), ");
        s.append("Terminal ID: ").append(terminalId);
        
        return s.toString(); 
    }
}
