package edu.jsu.mcis.cs310.tas_sp23;
/*
 * Punch class represents an instance of a punch by an employee using their badge.
 * Each punch is identified by a terminal ID, badge, and an event type (CLOCK_IN, CLOCK_OUT, or TIME_OUT).
 * The originalTimestamp is the time when the punch was recorded.
 * The id is the unique identifier for each punch.
 * The adjustedTimestamp is the time after any punch adjustment has been made.
 * The adjustmentType is the type of punch adjustment that was made.
 */

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class Punch {

    // instance fields
    private final int terminalId;
    private final Badge badge;
    private final EventType eventType;
    private final LocalDateTime originalTimestamp;
    private final String id;
    private LocalDateTime adjustedTimestamp;
    private PunchAdjustmentType adjustmentType;

    // constructor
    public Punch(int terminalId, Badge badge, EventType eventType) {
        this.terminalId = terminalId;
        this.badge = badge;
        this.eventType = eventType;
        this.originalTimestamp = LocalDateTime.now();
        this.id = null;
        this.adjustedTimestamp = null;
        this.adjustmentType = null;
    }
    
    // Second constructor which takes an int id
    public Punch(int id, int terminalId, Badge badge, LocalDateTime origTimestamp, EventType eventType) {
        this.terminalId = terminalId;
        this.badge = badge;
        this.eventType = eventType;
        this.originalTimestamp = origTimestamp;
        this.id = String.valueOf(id);
        this.adjustedTimestamp = null;
        this.adjustmentType = null;
    }

    // getters for all instance fields
    public int getTerminalId() {
        return terminalId;
    }

    public Badge getBadge() {
        return badge;
    }

    public EventType getPunchType() {
        return eventType;
    }

    public LocalDateTime getOriginalTimestamp() {
        return originalTimestamp;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getAdjustedTimestamp() {
        return adjustedTimestamp;
    }

    // setter for adjustedTimestamp
    public void setAdjustedTimestamp(LocalDateTime adjustedTimestamp) {
        this.adjustedTimestamp = adjustedTimestamp;
    }

    public PunchAdjustmentType getAdjustmentType() {
        return adjustmentType;
    }

    // setter for adjustmentType
    public void setAdjustmentType(PunchAdjustmentType adjustmentType) {
        this.adjustmentType = adjustmentType;
    }
    
        
    public void adjust(Shift s){
        String dayOfWeek = originalTimestamp.getDayOfWeek().toString();
        
        boolean isWeekend = false;
        int startSec = s.getStartTime().toSecondOfDay();
        int LStartSec = s.getLunchStart().toSecondOfDay();
        int LStopSec = s.getLunchStop().toSecondOfDay();
        int stopSec = s.getStopTime().toSecondOfDay();
        int dockSec = s.getDockPenalty() * 60;
        int graceSec = s.getGracePeriod() * 60;
        int intervalSec = s.getRoundInterval() * 60;
        int timeSec = originalTimestamp.toLocalTime().toSecondOfDay();
        
        int timeDifference;
        int adjTimeSec = 0;

        if (dayOfWeek.equals("SATURDAY") || dayOfWeek.equals("SUNDAY")){
            isWeekend = true;
        }
        /* CLOCK IN */
        if (eventType.equals(EventType.CLOCK_IN)){
            
            if (isWeekend){
                int intervalTimeDifference = timeSec % intervalSec;
                
                if (intervalTimeDifference > intervalSec/2){
                    timeDifference = intervalSec - intervalTimeDifference;
                    adjTimeSec = timeSec + timeDifference;
                }
                else{
                    adjTimeSec = timeSec - intervalTimeDifference;
                }
                adjustmentType = PunchAdjustmentType.INTERVAL_ROUND;
            }
            else{
                timeDifference = timeSec % intervalSec;
                
                if (startSec > timeSec){
                    timeDifference = intervalSec - timeDifference;
                    adjTimeSec = timeSec + timeDifference;
                    adjustmentType = PunchAdjustmentType.SHIFT_START;
                }
                else if (startSec < timeSec && timeSec < LStartSec) {
                    if (timeDifference > graceSec){
                        adjTimeSec = timeSec + (dockSec - timeDifference);
                        adjustmentType = PunchAdjustmentType.SHIFT_DOCK;
                    }
                    else{
                        adjTimeSec = timeSec - timeDifference;
                        adjustmentType = PunchAdjustmentType.SHIFT_START;
                    }
                }
                else{
                    if (timeSec < LStopSec){
                        adjTimeSec = timeSec + (intervalSec - timeDifference);
                    }
                    else{
                        adjTimeSec = timeSec - timeDifference;
                    }
                    
                    adjustmentType = PunchAdjustmentType.LUNCH_STOP;
                }
                
            }
        }
        /*CLOCK OUT*/
        if (eventType.equals(EventType.CLOCK_OUT)){
            
            if (isWeekend){ 
                int intervalTimeDifference = timeSec % intervalSec;
                
                if (intervalTimeDifference > intervalSec/2){
                    timeDifference = intervalSec - intervalTimeDifference;
                    adjTimeSec = timeSec + timeDifference;
                }
                else{
                    adjTimeSec = timeSec - intervalTimeDifference;
                }
                adjustmentType = PunchAdjustmentType.INTERVAL_ROUND;
            }
            else{
                timeDifference = timeSec % intervalSec;
                
                if (stopSec > timeSec && timeSec > LStopSec){
                    timeDifference = stopSec - timeSec;
                    
                    if (timeDifference > graceSec && timeDifference <= dockSec){
                        if (timeDifference < dockSec){
                            adjTimeSec = timeSec - dockSec - timeDifference;
                        }
                        else{
                            adjTimeSec = timeSec;
                        }
                        adjustmentType = PunchAdjustmentType.SHIFT_DOCK;
                    }
                    else if (timeDifference > graceSec && timeDifference > dockSec){
                        adjTimeSec = timeSec - (intervalSec - timeDifference);
                        adjustmentType = PunchAdjustmentType.INTERVAL_ROUND;
                    }
                    else{
                        adjTimeSec = timeSec + timeDifference;
                        adjustmentType = PunchAdjustmentType.SHIFT_STOP;
                    }
                }
                else if (stopSec < timeSec) {
                    adjTimeSec = timeSec - timeDifference;
                    if (timeDifference < 60){
                        adjustmentType = PunchAdjustmentType.NONE;
                    }
                    else{
                        adjustmentType = PunchAdjustmentType.SHIFT_STOP;
                    }
                }
                else{
                    if (timeSec < LStartSec){
                        adjTimeSec = timeSec + timeDifference;
                    }
                    else{
                        adjTimeSec = timeSec - timeDifference;
                    }
                    adjustmentType = PunchAdjustmentType.LUNCH_START;
                }
            }
        }
        adjustedTimestamp = LocalTime.ofSecondOfDay(adjTimeSec).atDate(originalTimestamp.toLocalDate());
    }

    // prints the original punch details to console
    public String printOriginal() {

        // Formats the date in the correct format
        // E: day-of-week MM: month-of-year / dd: day-of-month / yyyy: year-of-era
        // HH: hour-of-day (0-23) | mm: minute-of-hour | ss: second-of-minute
        // Docs: https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
        final String date = DateTimeFormatter.ofPattern("E MM/dd/yyyy HH:mm:ss").format(originalTimestamp).toUpperCase();
        
        StringBuilder s = new StringBuilder();
        
        s.append('#').append(badge.getId()).append(' ');
        s.append(eventType.toString()).append(": ");
        s.append(date);
        
        return s.toString(); 
        
    }
    
    public String printAdjusted(){
        final String date = DateTimeFormatter.ofPattern("E MM/dd/yyyy").format(originalTimestamp).toUpperCase();
        String adjustedTime = DateTimeFormatter.ofPattern("HH:mm:ss").format(adjustedTimestamp);
        StringBuilder s = new StringBuilder();
        
        s.append('#').append(badge.getId()).append(' ');
        s.append(eventType.toString()).append(": ");
        s.append(date).append(" ");
        s.append(adjustedTime).append(" (");
        s.append(adjustmentType).append(")");
        
        return s.toString();
    }

    // overrides toString() method to print original punch details
    @Override
    public String toString() {
        return printOriginal();
    }
}