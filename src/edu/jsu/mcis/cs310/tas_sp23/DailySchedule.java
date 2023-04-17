package edu.jsu.mcis.cs310.tas_sp23;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;

/**
 *
 * @author Team C
 * <p> The shift parameters for a single business day: the shift start and stop times, the lunch start and stop times, the interval, the grace period, etc. </p>
 */
public class DailySchedule {
    
    private LocalTime startTime, stopTime, lunchStart, lunchStop;
    private int lunchThreshold, roundInterval, gracePeriod, dockPenalty;
    private Duration lunchduration, shiftduration;

    /**
     *
     * @param map defines constructor DailySchedule and takes in a HashMap with key-value pairs of strings
     * <p> extracts the value associated with the key "description" from the HashMap using the "get" method, </p>
     * <p> and stores it in "desc" </p>
     * <p> then extracts the value associated with all of the keys from the HashMap and passes it to the </p>
     * <p> parse method of the "LocalTime" class to convert the string to a LocalTime object, then is assigned </p>
     * <p> to each property of the object that the constructor is initializing using the "this" keyword. </p>
     */
    public DailySchedule(HashMap<String, String> map) {
        
        String desc = map.get("description");
       
        this.startTime = LocalTime.parse( map.get("shiftstart"));
        this.stopTime = LocalTime.parse( map.get("shiftstop"));
        this.gracePeriod = Integer.parseInt(map.get("graceperiod"));
        this.dockPenalty = Integer.parseInt(map.get("dockpenalty"));
        this.roundInterval = Integer.parseInt(map.get("roundinterval"));
        this.lunchStart = LocalTime.parse( map.get("lunchstart"));
        this.lunchStop = LocalTime.parse( map.get("lunchstop"));
        this.lunchThreshold = Integer.parseInt(map.get("lunchthreshold"));
        
        this.lunchduration = Duration.between(lunchStart, lunchStop);
        
        if (desc.equals("Shift 3")){
            this.shiftduration = Duration.between(startTime, stopTime);
            LocalTime timeDifference = (LocalTime)this.shiftduration.addTo(LocalTime.MAX);
            this.shiftduration = Duration.ofHours(timeDifference.getHour()).plus(this.lunchduration);
        }
        else{
            this.shiftduration = Duration.between(startTime, stopTime);
        }
        
    }
    
    /**
     *
     * @return
     * <p> getStartTime getter method of type LocalTime that returns the initial start time </p>
     */
    public LocalTime getStartTime() {
        return startTime;
    }
    
    /**
     *
     * @return
     * <p> getStopTime getter method of type LocalTime that returns the initial stoppage time  </p>
     */
    public LocalTime getStopTime() {
        return stopTime;
    }
    
    /**
     *
     * @return
     * <p> getLunchStart getter method of type LocalTime that returns lunchStart  </p>
     */
    public LocalTime getLunchStart() {
        return lunchStart;
    }
    
    /**
     *
     * @return
     * <p> getLunchStop getter method of type LocalTime that returns when lunch stops  </p>
     */
    public LocalTime getLunchStop() {
        return lunchStop;
    }
    
    /**
     *
     * @return
     * <p> getRoundInterval getter method of type int that returns the round interval  </p>
     */
    public int getRoundInterval() {
        return roundInterval;
    }
    
    /**
     *
     * @return
     * <p> getGracePeriod getter method of type int that returns the grace period  </p>
     */
    public int getGracePeriod() {
        return gracePeriod;
    }
    
    /**
     *
     * @return
     * <p> getDockPenalty getter method of type int that returns the dock penalty  </p>
     */
    public int getDockPenalty() {
        return dockPenalty;
    }
    
    /**
     *
     * @return
     * <p> getLunchThreshold getter method of type int that returns the lunch threshold  </p> 
     */
    public int getLunchThreshold() {
        return lunchThreshold;
    }
    
    /**
     *
     * @return
     * <p> getLunchDuration getter method of type Duration that returns the lunch duration  </p>
     */
    public Duration getLunchDuration() {
        return lunchduration;
    }

    /**
     *
     * @return
     * <p> getShiftDuration getter method of type Duration that returns the shift duration  </p>
     */
    public Duration getShiftDuration() {
        return shiftduration;
    }
    
    /**
     *
     * @return
     * <p> toString Method outputs the method its state into a string </p>
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(getStartTime()).append(" - ");
        s.append(getStopTime()).append(" (");
        s.append(getShiftDuration().toMinutes()).append(" minutes); Lunch: ");
        s.append(getLunchStart()).append(" - ");
        s.append(getLunchStop()).append(" (");
        s.append(getLunchDuration().toMinutes()).append(" minutes)");
        return s.toString();
    }
}
