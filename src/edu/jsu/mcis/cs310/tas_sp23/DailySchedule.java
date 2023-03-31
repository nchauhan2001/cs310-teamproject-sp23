package edu.jsu.mcis.cs310.tas_sp23;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;

public class DailySchedule {
    
    private LocalTime startTime, stopTime, lunchStart, lunchStop;
    private int lunchThreshold, roundInterval, gracePeriod, dockPenalty;
    private Duration lunchduration, shiftduration;

    public DailySchedule(HashMap<String, String> map) {
        
        String desc = map.get("description");
       
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
    
    public LocalTime getStartTime() {
        return startTime;
    }
    
    public LocalTime getStopTime() {
        return stopTime;
    }
    
    public LocalTime getLunchStart() {
        return lunchStart;
    }
    
    public LocalTime getLunchStop() {
        return lunchStop;
    }
    
    public int getRoundInterval() {
        return roundInterval;
    }
    
    public int getGracePeriod() {
        return gracePeriod;
    }
    
    public int getDockPenalty() {
        return dockPenalty;
    }
    
    public int getLunchThreshold() {
        return lunchThreshold;
    }
    
    public Duration getLunchDuration() {
        return lunchduration;
    }

    public Duration getShiftDuration() {
        return shiftduration;
    }
    
}
