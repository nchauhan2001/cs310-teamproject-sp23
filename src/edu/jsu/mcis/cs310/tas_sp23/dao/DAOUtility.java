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
    
    public static int calculateTotalMinutes(ArrayList<Punch> punchlist, Shift shift) {

        ArrayList<ArrayList<Punch>> dailypunchlist = new ArrayList<>();
  
        ArrayList<Punch> arr = new ArrayList<>();
        
        LocalDate currentDate = punchlist.get(0).getAdjustedTimestamp().toLocalDate();

        for(int i = 0; i < punchlist.size(); i++) {

            if(currentDate.isEqual(punchlist.get(i).getAdjustedTimestamp().toLocalDate())) { // Date is within the current date
                
                arr.add(punchlist.get(i));
                
            } else { // Date is NOT within the current date
                
                currentDate = punchlist.get(i).getAdjustedTimestamp().toLocalDate();          
                dailypunchlist.add(arr);
                arr = new ArrayList<>();
                
                arr.add(punchlist.get(i));
                
            }
        }
        
        if(arr.size() > 0) {
            dailypunchlist.add(arr);
        }

                
        int total = 0;

        for(int i = 0; i < dailypunchlist.size(); i++) {

            Boolean punchedOutForLunch = false;
            Boolean punchedInForLunch = false;
            
            int dailyTotal = 0;
            
            for(int j = 0; j < dailypunchlist.get(i).size(); j++) {
                
                Punch punch = dailypunchlist.get(i).get(j);

                // There was not an initial a clock-in. Return the total
                if(j == 0 && !punch.getPunchType().equals(EventType.CLOCK_IN)) {
                    break;
                }

                // There was never a final clock-out. Return the total
                if(j == dailypunchlist.get(i).size()-1 && !punch.getPunchType().equals(EventType.CLOCK_OUT)) {
                    break;
                }

                // Check if the employee clocked out for lunch
                if(!punchedOutForLunch && punch.getPunchType().equals(EventType.CLOCK_OUT) && punch.getAdjustedTimestamp().toLocalTime().equals(shift.getLunchStart())) {
                    punchedOutForLunch = true;
                }

                // Check if the employee clocked in for lunch
                if(!punchedInForLunch && punch.getPunchType().equals(EventType.CLOCK_IN) && punch.getAdjustedTimestamp().toLocalTime().equals(shift.getLunchStop())) {
                    punchedInForLunch = true;
                }

                // Check if the punch is a clock in, get the next punch and if it's a clock out, find the difference in minutes and add it to the total of minutes worked
                if(punch.getPunchType().equals(EventType.CLOCK_IN)) {
                    Punch nextPunch = dailypunchlist.get(i).get(j+1);

                    if(nextPunch.getPunchType().equals(EventType.CLOCK_OUT)) {

                        Duration diff = Duration.between(punch.getAdjustedTimestamp(), nextPunch.getAdjustedTimestamp());

                        dailyTotal += diff.toMinutes();

                    }
                }
            
            }
            
            // Employee didn't clock in/out for lunch and also met the lunch threshold
            if(dailyTotal > shift.getLunchThreshold() && !(punchedOutForLunch && punchedInForLunch)) {
                dailyTotal -= shift.getLunchDuration().toMinutes();  
            }
            
            total += dailyTotal;

        }

        return total;
        
    }
    
    public static String getPunchListPlusTotalsAsJSON(ArrayList<Punch> punchlist, Shift shift) {
        final String dateFormat = "E MM/dd/yyyy HH:mm:ss";
        
        JsonObject result = new JsonObject(); 
        JsonArray jsonData = new JsonArray();

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
        
        for(int i = 0; i < sortedArray.size(); i++) {
            
            JsonObject data = new JsonObject();
            Punch punch = sortedArray.get(i);

            String date = DateTimeFormatter.ofPattern(dateFormat).format(punch.getOriginalTimestamp()).toUpperCase();
            data.put("originaltimestamp", date);
            
            data.put("badgeid", punch.getBadge().getId());

            String adjustedTime = DateTimeFormatter.ofPattern(dateFormat).format(punch.getAdjustedTimestamp()).toUpperCase();
            data.put("adjustedtimestamp", adjustedTime);
            
            data.put("adjustmenttype", punch.getAdjustmentType().toString());
            data.put("terminalid", String.valueOf(punch.getTerminalId()));
            data.put("id", String.valueOf(punch.getId()));
            
            data.put("punchtype", punch.getPunchType().toString());

            jsonData.add(data);
            
        }
        
        result.put("punchlist", jsonData);
        
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