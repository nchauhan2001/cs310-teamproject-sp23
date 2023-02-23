package edu.jsu.mcis.cs310.tas_sp23;

public class Department {

    private final String id, description, terminalId;
    
    public Department(String id, String desc, String termid) {
        this.id = id;
        this.description = desc;
        this.terminalId = termid;
    }
    
    public String getId() {
        return id;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getTerminalId() {
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
