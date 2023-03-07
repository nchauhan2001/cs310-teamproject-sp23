package edu.jsu.mcis.cs310.tas_sp23.dao;

import java.time.*;
import java.util.*;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import com.github.cliftonlabs.json_simple.*;
import edu.jsu.mcis.cs310.tas_sp23.EventType;
import edu.jsu.mcis.cs310.tas_sp23.Punch;
import edu.jsu.mcis.cs310.tas_sp23.Shift;

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
    
    public static int calculateTotalMinutes(ArrayList<Punch> dailypunchlist, Shift shift) {
        int total = 0;
        
        Boolean workedBeforeLunch = false;
        Boolean workedAfterLunch = false;
        Boolean punchedOutForLunch = false;
        Boolean punchedInForLunch = false;

        for(int i = 0; i < dailypunchlist.size(); i++) {
            Punch punch = dailypunchlist.get(i);
            
            // There was not an initial a clock-in. Return the total
            if(i == 0 && !punch.getPunchType().equals(EventType.CLOCK_IN)) {
                return total;
            }
            
            // There was never a final clock-out. Return the total
            if(i == dailypunchlist.size()-1 && !punch.getPunchType().equals(EventType.CLOCK_OUT)) {
                return total;
            }
            
            // Check if the employee clocked out for lunch
            if(!punchedOutForLunch && punch.getPunchType().equals(EventType.CLOCK_OUT) && punch.getAdjustedTimestamp().toLocalTime().equals(shift.getLunchStart())) {
                punchedOutForLunch = true;
            }
            
            // Check if the employee clocked in for lunch
            if(!punchedInForLunch && punch.getPunchType().equals(EventType.CLOCK_IN) && punch.getAdjustedTimestamp().toLocalTime().equals(shift.getLunchStop())) {
                punchedInForLunch = true;
            }

            // Check if the employee worked before lunch
            if(!workedBeforeLunch && shift.getLunchStart().isAfter(punch.getAdjustedTimestamp().toLocalTime())) {
                workedBeforeLunch = true;
            }
            
            // Check if the employee worked after lunch
            if(!workedAfterLunch && shift.getLunchStop().isBefore(punch.getAdjustedTimestamp().toLocalTime())) {
                workedAfterLunch = true;
            }
            
            // Check if the punch is a clock in, get the next punch and if it's a clock out, find the difference in minutes and add it to the total of minutes worked
            if(punch.getPunchType().equals(EventType.CLOCK_IN)) {
                Punch nextPunch = dailypunchlist.get(i+1);
                
                if(nextPunch.getPunchType().equals(EventType.CLOCK_OUT)) {
                    
                    Duration diff = Duration.between(punch.getAdjustedTimestamp(), nextPunch.getAdjustedTimestamp());
                    
                    total += diff.toMinutes();

                }
            }
        }

        // Employee didn't clock in/out for lunch and also DID work before and after lunch
        if(workedBeforeLunch && workedAfterLunch && !(punchedOutForLunch && punchedInForLunch)) {
            total -= shift.getLunchDuration().toMinutes();  
        }
        
        return total;
    }
    
}