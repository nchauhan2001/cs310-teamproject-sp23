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

    public Shift(HashMap<String, String> map) {
        this.id = Integer.parseInt(map.get("id"));
        this.desc = map.get("description");
        this.startTime = LocalTime.parse( map.get("startTime"));
        this.stopTime = LocalTime.parse( map.get("stopTime"));
        this.gracePeriod = Integer.parseInt(map.get("gracePeriod"));
        this.dockPenalty = Integer.parseInt(map.get("dockPenalty"));
        this.roundInterval = Integer.parseInt(map.get("roundInterval"));
        this.lunchStart = LocalTime.parse( map.get("lunchStart"));
        this.lunchStop = LocalTime.parse( map.get("lunchStop"));
        this.lunchThreshold = Integer.parseInt(map.get("lunchThreshold"));
        
        this.lunchduration = Duration.between(lunchStart, lunchStop);
        
        if (desc.equals("Shift 3")){
            this.shiftduration = Duration.between(startTime, stopTime);
            LocalTime timeDifference = (LocalTime)this.shiftduration.addTo(LocalTime.MAX);
            this.shiftduration = Duration.ofHours(timeDifference.getHour()).plus(this.lunchduration);
            System.out.println(this.shiftduration);
        }
        else{
            this.shiftduration = Duration.between(startTime, stopTime);

        }
        
    }
    // WEBHOOK TEST
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

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(desc).append(": ");
        s.append(startTime).append(" - ");
        s.append(stopTime).append(" (");
        s.append(shiftduration.toMinutes()).append(" minutes); Lunch: ");
        s.append(lunchStart).append(" - ");
        s.append(lunchStop).append(" (");
        s.append(lunchduration.toMinutes()).append(" minutes)");

        return s.toString();
    }
}
