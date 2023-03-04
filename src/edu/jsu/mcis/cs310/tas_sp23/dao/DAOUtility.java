package edu.jsu.mcis.cs310.tas_sp23.dao;

import java.time.*;
import java.util.*;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import com.github.cliftonlabs.json_simple.*;
import edu.jsu.mcis.cs310.tas_sp23.Punch;

/**
 * 
 * Utility class for DAOs.  This is a final, non-constructable class containing
 * common DAO logic and other repeated and/or standardized code, refactored into
 * individual static methods.
 * 
 */
public final class DAOUtility {
    
    public static String getPunchListAsJSON(ArrayList<Punch> punchList) {
 
        String dateFormat = "E MM/dd/yyyy HH:mm:ss";
        
        /* Create ArrayList Object */
        ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();

        /* Create HashMap Object (one for every Punch!) */
        for (int i = 0; i < punchList.size(); i++){
            HashMap<String, String>  punchData = new HashMap<>();
            Punch punch = punchList.get(i);

            /* Add Punch Data to HashMap */
            punchData.put("id", String.valueOf(punch.getId()));
            punchData.put("badgeid", punch.getBadge().getId());
            punchData.put("terminalid", String.valueOf(punch.getTerminalId()));
            punchData.put("punchtype", punch.getPunchType().toString());
            punchData.put("adjustmenttype", punch.getAdjustmentType().toString());
            
            String date = DateTimeFormatter.ofPattern(dateFormat).format(punch.getOriginalTimestamp()).toUpperCase();
            punchData.put("originaltimestamp", date);
            
            String adjustedTime = DateTimeFormatter.ofPattern(dateFormat).format(punch.getAdjustedTimestamp()).toUpperCase();
            punchData.put("adjustedtimestamp", adjustedTime);

            /* Append HashMap to ArrayList */
            jsonData.add(punchData);
        }
        return Jsoner.serialize(jsonData);
    }
}