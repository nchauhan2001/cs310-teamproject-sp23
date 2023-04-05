package edu.jsu.mcis.cs310.tas_sp23;

import java.util.*;
import java.time.*;

public class Shift {
    private int id;
    private String desc;
    private DailySchedule defaultschedule;
    
    private HashMap<Integer, DailySchedule> hm = new HashMap<>();

    public Shift(HashMap<String, String> map) {
        
        this.id = Integer.parseInt(map.get("id"));
        this.desc = map.get("description");

        this.defaultschedule = new DailySchedule(map);
        
        int mon = DayOfWeek.MONDAY.getValue();
        int fri = DayOfWeek.FRIDAY.getValue();
        
        for(int i = mon; i <= fri; i++) {
            hm.put(i, this.defaultschedule);
        }

    }

    public int getID(){
        return id;
    }
    
    public String getDescription(){
        return desc;
    }

    public LocalTime getStartTime() {
        return defaultschedule.getStartTime();
    }

    public LocalTime getStopTime() {
        return defaultschedule.getStopTime();
    }
    
    public int getGracePeriod(){
        return defaultschedule.getGracePeriod();
    }
    
    public int getDockPenalty(){
        return defaultschedule.getDockPenalty();
    }
    
    public int getRoundInterval(){
        return defaultschedule.getRoundInterval();
    }

    public LocalTime getLunchStart() {
        return defaultschedule.getLunchStart();
    }

    public LocalTime getLunchStop() {
        return defaultschedule.getLunchStop();
    }
    
    public int getLunchThreshold(){
        return defaultschedule.getLunchThreshold();
    }

    public Duration getLunchDuration() {
        return defaultschedule.getLunchDuration();
    }

    public Duration getShiftDuration() {
        return defaultschedule.getShiftDuration();
    }
    
    public DailySchedule getDefaultschedule(){
        return this.defaultschedule;
    }
    
    public DailySchedule getDailyschedule(DayOfWeek dow) {
        
        if(hm.containsKey(dow.getValue())) { // The hashmap of DailySchedule does include this day of week
            return hm.get(dow.getValue());
        } else { // It does not include the day of week
            return defaultschedule;
        }
    }
    
    public DailySchedule setDailyschedule(DayOfWeek dow, DailySchedule ds) {
        return hm.put(dow.getValue(), ds);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(desc).append(": ");
        s.append(getStartTime()).append(" - ");
        s.append(getStopTime()).append(" (");
        s.append(getShiftDuration().toMinutes()).append(" minutes); Lunch: ");
        s.append(getLunchStart()).append(" - ");
        s.append(getLunchStop()).append(" (");
        s.append(getLunchDuration().toMinutes()).append(" minutes)");
        return s.toString();
    }
}
