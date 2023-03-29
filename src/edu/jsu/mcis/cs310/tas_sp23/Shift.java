package edu.jsu.mcis.cs310.tas_sp23;

import java.util.*;
import java.time.*;

public class Shift {
    private int id, gracePeriod, dockPenalty;
    private String desc;
    private LocalTime startTime, stopTime, lunchStart, lunchStop;
    private int lunchThreshold, roundInterval;
    // These are the parameters for a given emplyee's shift. 
    // N.B. These are to be measured in minutes!!! - William H.
    private Duration lunchduration, shiftduration;
    private DailySchedule defaultschedule;

    public Shift(HashMap<String, String> map) {
        
        this.id = Integer.parseInt(map.get("id"));
        this.desc = map.get("description");
        
        this.defaultschedule = new DailySchedule(map);

    }

    public int getID(){
        return id;
    }
    
    public String getDescription(){
        return desc;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getStopTime() {
        return stopTime;
    }
    
    public int getGracePeriod(){
        return gracePeriod;
    }
    
    public int getDockPenalty(){
        return dockPenalty;
    }
    
    public int getRoundInterval(){
        return roundInterval;
    }

    public LocalTime getLunchStart() {
        return lunchStart;
    }

    public LocalTime getLunchStop() {
        return lunchStop;
    }
    
    public int getLunchThreshold(){
        return lunchThreshold;
    }

    public Duration getLunchDuration() {
        return lunchduration;
    }

    public Duration getShiftDuration() {
        return shiftduration;
    }
    
    public DailySchedule getDefaultschedule() {
        return defaultschedule;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(desc).append(": ");
        s.append(defaultschedule.getStartTime()).append(" - ");
        return s.toString();
    }
}
