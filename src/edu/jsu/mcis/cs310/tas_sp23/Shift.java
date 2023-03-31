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
    
    public DailySchedule getDefaultschedule() {
        return defaultschedule;
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
