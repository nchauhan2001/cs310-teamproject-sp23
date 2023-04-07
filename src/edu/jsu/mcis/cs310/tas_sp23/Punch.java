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
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;

public class Punch {

    // instance fields
    private final int terminalId;
    private final Badge badge;
    private final EventType eventType;
    private final LocalDateTime originalTimestamp;
    private int id;
    private LocalDateTime adjustedTimestamp;
    private PunchAdjustmentType adjustmentType;
    final String dateFormat = "E MM/dd/yyyy HH:mm:ss";

    // constructor
    public Punch(int terminalId, Badge badge, EventType eventType) {
        this.terminalId = terminalId;
        this.badge = badge;
        this.eventType = eventType;
        this.originalTimestamp = LocalDateTime.now();
        this.adjustedTimestamp = null;
        this.adjustmentType = null;
    }
    
    // Second constructor which takes an int id
    public Punch(int id, int terminalId, Badge badge, LocalDateTime origTimestamp, EventType eventType) {
        this.terminalId = terminalId;
        this.badge = badge;
        this.eventType = eventType;
        this.originalTimestamp = origTimestamp;
        this.id = id;
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

    public int getId() {
        return id;
    }

    public LocalDateTime getAdjustedTimestamp() {
        return adjustedTimestamp;
    }

    public PunchAdjustmentType getAdjustmentType() {
        return adjustmentType;
    }
        
    public void adjust(Shift s){
        DayOfWeek dayOfWeek = originalTimestamp.getDayOfWeek();
        
        int startSec = s.getDailyschedule(dayOfWeek).getStartTime().toSecondOfDay();
        int LStartSec = s.getDailyschedule(dayOfWeek).getLunchStart().toSecondOfDay();
        int LStopSec = s.getDailyschedule(dayOfWeek).getLunchStop().toSecondOfDay();
        int stopSec = s.getDailyschedule(dayOfWeek).getStopTime().toSecondOfDay();
        int dockSec = s.getDailyschedule(dayOfWeek).getDockPenalty() * 60;
        int graceSec = s.getDailyschedule(dayOfWeek).getGracePeriod() * 60;
        int intervalSec = s.getDailyschedule(dayOfWeek).getRoundInterval() * 60;
        int timeSec = originalTimestamp.toLocalTime().toSecondOfDay();
        
        int intervalTimeDifference = timeSec % intervalSec;
        int timeDifference = intervalSec - intervalTimeDifference;
        int adjTimeSec;
        
        if (intervalTimeDifference >= intervalSec/2){
            adjTimeSec = timeSec + timeDifference;
        }
        else{
            adjTimeSec = timeSec - intervalTimeDifference;
        }
        
        if (dayOfWeek.equals(dayOfWeek.SATURDAY) || dayOfWeek.equals(dayOfWeek.SUNDAY)){
            adjustmentType = PunchAdjustmentType.INTERVAL_ROUND;
        }
        /* CLOCK IN */
        else if (eventType.equals(EventType.CLOCK_IN)){

            if (timeSec < startSec){
                if (startSec-timeSec < intervalSec){
                    // even if time is closer to interval increment before shift start
                    // don't adjust backwards, only forwards for shift start
                    adjTimeSec = timeSec + timeDifference;
                    adjustmentType = PunchAdjustmentType.SHIFT_START;
                }
                else if (startSec - timeSec > intervalSec && intervalTimeDifference > 60){
                    adjustmentType = PunchAdjustmentType.INTERVAL_ROUND;
                }
                else{
                    adjustmentType = PunchAdjustmentType.NONE;
                }
            }
            else if (timeSec < LStartSec) {
                if (intervalTimeDifference > graceSec){
                    adjustmentType = PunchAdjustmentType.SHIFT_DOCK;
                }
                else{
                    adjustmentType = PunchAdjustmentType.SHIFT_START;
                }
            }
            else{
                adjustmentType = PunchAdjustmentType.LUNCH_STOP;
            }
            
        }
        /*CLOCK OUT*/
        else if (eventType.equals(EventType.CLOCK_OUT)){
            
            if (timeSec < stopSec && timeSec > LStopSec){
                if (timeDifference < graceSec){
                    adjustmentType = PunchAdjustmentType.SHIFT_STOP;
                }
                else if (timeDifference > graceSec && (stopSec - timeSec) <= dockSec){
                    adjustmentType = PunchAdjustmentType.SHIFT_DOCK;
                }
                else{
                    adjustmentType = PunchAdjustmentType.INTERVAL_ROUND;
                }
            }
            else if (timeSec > stopSec) {
                if (intervalTimeDifference < 60 && timeSec-stopSec > intervalSec){
                    adjustmentType = PunchAdjustmentType.NONE;
                }
                else if (timeSec-stopSec > intervalSec){
                    adjustmentType = PunchAdjustmentType.INTERVAL_ROUND;
                }
                else{
                    adjTimeSec = timeSec - intervalTimeDifference;
                    adjustmentType = PunchAdjustmentType.SHIFT_STOP;
                }
            }
            else {
                adjustmentType = PunchAdjustmentType.LUNCH_START;
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
        final String date = DateTimeFormatter.ofPattern(dateFormat).format(originalTimestamp).toUpperCase();
        
        StringBuilder s = new StringBuilder();
        
        s.append('#').append(badge.getId()).append(' ');
        s.append(eventType.toString()).append(": ");
        s.append(date);
        
        return s.toString();
    }
    
    // prints adjusted punch information
    public String printAdjusted(){
        final String date = DateTimeFormatter.ofPattern(dateFormat).format(adjustedTimestamp).toUpperCase();
        StringBuilder s = new StringBuilder();
        
        s.append('#').append(badge.getId()).append(' ');
        s.append(eventType.toString()).append(": ");
        s.append(date).append(" (");
        s.append(adjustmentType).append(")");
        
        return s.toString();
    }

    // overrides toString() method to print original punch details
    @Override
    public String toString() {
        return printOriginal();
    }
}