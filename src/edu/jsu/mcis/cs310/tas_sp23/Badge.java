package edu.jsu.mcis.cs310.tas_sp23;

import java.util.zip.CRC32;

public class Badge {

    private final String id, description;

    public Badge(String id, String description) {
        this.id = id;
        this.description = description;
    }
    
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

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();

        s.append('#').append(id).append(' ');
        s.append('(').append(description).append(')');

        return s.toString();

    }

}
