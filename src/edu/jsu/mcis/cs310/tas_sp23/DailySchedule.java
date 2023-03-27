package edu.jsu.mcis.cs310.tas_sp23;

import java.time.LocalTime;

public class DailySchedule {
    
    private LocalTime startTime, stopTime, lunchStart, lunchStop;
    private int lunchThreshold, roundInterval, gracePeriod, dockPenalty;;

    public DailySchedule(LocalTime startTime, LocalTime stopTime, LocalTime lunchStart, LocalTime lunchStop, int roundInterval, int gracePeriod, int dockPenalty, int lunchThreshold) {
        
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.lunchStart = lunchStart;
        this.lunchStop = lunchStop;
        this.roundInterval = roundInterval;
        this.gracePeriod = gracePeriod;
        this.dockPenalty = dockPenalty;
        this.lunchThreshold = lunchThreshold;
        
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
    
}
