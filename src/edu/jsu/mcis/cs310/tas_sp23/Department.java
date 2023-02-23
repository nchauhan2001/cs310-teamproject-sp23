package edu.jsu.mcis.cs310.tas_sp23;

public class Department {

    private int id, terminalId;
    private final String description;
    
    public Department(int id, String desc, int termid) {
        this.id = id;
        this.description = desc;
        this.terminalId = termid;
    }
    
    public int getId() {
        return id;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getTerminalId() {
        return terminalId;
    }
    
    @Override
    public String toString() {
        
        StringBuilder s = new StringBuilder();
        
        s.append('#').append(id).append(' ');
        s.append('(').append(description).append("), ");
        s.append("Terminal ID: ").append(terminalId);
        
        return s.toString(); 
    }
}
