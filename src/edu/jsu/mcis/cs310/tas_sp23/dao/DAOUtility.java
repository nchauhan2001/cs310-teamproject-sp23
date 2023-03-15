package edu.jsu.mcis.cs310.tas_sp23.dao;

import java.time.*;
import java.util.*;
import java.time.format.DateTimeFormatter;
import com.github.cliftonlabs.json_simple.*;
import edu.jsu.mcis.cs310.tas_sp23.EventType;
import edu.jsu.mcis.cs310.tas_sp23.Punch;
import edu.jsu.mcis.cs310.tas_sp23.Shift;
import java.math.BigDecimal;
import java.math.RoundingMode;

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
            punchData.put("badgeid", punch.getBadge().getId());
            punchData.put("terminalid", String.valueOf(punch.getTerminalId()));
            punchData.put("id", String.valueOf(punch.getId()));
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
    
    public static int calculateTotalMinutes(ArrayList<Punch> punchlist, Shift shift) {
        
        int total = 0; // Total for all the punches
       
        int dailyTotal = 0; // Total minutes for each day
        boolean punchedOutForLunch = false;
        boolean punchedInForLunch = false;
        
        LocalDate currentDate = punchlist.get(0).getAdjustedTimestamp().toLocalDate(); // The current date of the punches we are working with
        
        for(int i = 0; i < punchlist.size(); i++) {

            if(punchlist.size() - 1 > i) { // True if there is a punch after this one
                    
                Punch punch = punchlist.get(i);
                Punch nextPunch = punchlist.get(i+1);
                
                // True if the current Punch and the next punch are pairs of CLOCK_IN and CLOCK_OUT
                if(punch.getPunchType().equals(EventType.CLOCK_IN) && nextPunch.getPunchType().equals(EventType.CLOCK_OUT)) {

                    // If nextPunch isn't on the same date as currentDate, change the currentDate
                    if(!currentDate.equals(nextPunch.getAdjustedTimestamp().toLocalDate())) {
                        currentDate = punchlist.get(i).getAdjustedTimestamp().toLocalDate();   
                    }
                        
                    // Check if the employee clocked out for lunch
                    if(!punchedOutForLunch && punch.getPunchType().equals(EventType.CLOCK_OUT) && punch.getAdjustedTimestamp().toLocalTime().equals(shift.getLunchStart())) {
                        punchedOutForLunch = true;
                    }

                    // Check if the employee clocked in for lunch
                    if(!punchedInForLunch && punch.getPunchType().equals(EventType.CLOCK_IN) && punch.getAdjustedTimestamp().toLocalTime().equals(shift.getLunchStop())) {
                        punchedInForLunch = true;
                    }

                    // Find the difference in minutes and add it to the total of minutes worked
                    Duration diff = Duration.between(punch.getAdjustedTimestamp(), nextPunch.getAdjustedTimestamp());
                    dailyTotal += diff.toMinutes();

                } else { // They are not pairs of CLOCK_IN and CLOCK_OUT
                    
                    if(!nextPunch.getPunchType().equals(EventType.TIME_OUT)) { // The next punch is NOT a TIME_OUT
                    
                        total += dailyTotal;
                        
                    }
                    
                    dailyTotal = 0;
                    punchedOutForLunch = false;
                    punchedInForLunch = false;
                    
                }
                
                // Employee didn't clock in/out for lunch and also met the lunch threshold
                if(dailyTotal > shift.getLunchThreshold() && !(punchedOutForLunch && punchedInForLunch)) {
                    
                    dailyTotal -= shift.getLunchDuration().toMinutes();
                    
                }
                
            } else { // There was no next punch. Add the dailyTotal to the total
                
                total += dailyTotal;
                
            }
           
        }
       
        return total;
        
    }
    
    public static String getPunchListPlusTotalsAsJSON(ArrayList<Punch> punchlist, Shift shift) {
        
        JsonObject result = new JsonObject(); 

        ArrayList<Punch> sortedArray = new ArrayList<>(punchlist);
        
        // Sorting the punchlist into the order that they were punched
        for(int i = 0; i < sortedArray.size() - 1; i++) {
            for(int j = 1; j < sortedArray.size() - i - 1; j++) {
                
                Punch p = sortedArray.get(j);
                Punch nextP = sortedArray.get(j + 1);
                
                if(p.getAdjustedTimestamp().isAfter(nextP.getAdjustedTimestamp())) {
                    Punch temp = sortedArray.get(j);
                    sortedArray.set(j, sortedArray.get(j + 1));
                    sortedArray.set(j + 1, temp);
                }
                
            }
        }
             
        result.put("punchlist", Jsoner.deserialize(getPunchListAsJSON(sortedArray), new JsonArray()));
        result.put("totalminutes", calculateTotalMinutes(sortedArray, shift));
        result.put("absenteeism", calculateAbsenteeism(sortedArray, shift).toString() + "%");
        
        return Jsoner.serialize(result);
        
    }
    
    public static BigDecimal calculateAbsenteeism(ArrayList<Punch> punchlist, Shift s){
        
        double scheduledMinutes = 2400;
        BigDecimal percentage;
        
        int totalMinutes = calculateTotalMinutes(punchlist, s);
        double percent = (scheduledMinutes - totalMinutes) / scheduledMinutes * 100;
        
        percentage = new BigDecimal(percent);
        
        percentage = percentage.setScale(2, RoundingMode.HALF_UP);
        
        return percentage;
    }
}