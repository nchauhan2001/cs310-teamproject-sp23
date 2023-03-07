package edu.jsu.mcis.cs310.tas_sp23;
/*
 * Punch class represents an instance of a punch by an employee using their badge.
 * Each punch is identified by a terminal ID, badge, and an event type (CLOCK_IN, CLOCK_OUT, or TIME_OUT).
 * The originalTimestamp is the time when the punch was recorded.
 * The id is the unique identifier for each punch.
 * The adjustedTimestamp is the time after any punch adjustment has been made.
 * The adjustmentType is the type of punch adjustment that was made.
 */

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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
        String dayOfWeek = originalTimestamp.getDayOfWeek().toString();
        
        LocalTime originalLocalTime = originalTimestamp.toLocalTime();
        Duration durationDifference;
        LocalTime intervalTime = LocalTime.of(0, s.getRoundInterval());
        LocalTime dockTime = LocalTime.of(0, s.getDockPenalty());
        int originalTimeSec = originalLocalTime.toSecondOfDay();
        int intervalSec = intervalTime.toSecondOfDay();
        int differenceSec = originalTimeSec % intervalSec;
        int intervalDifferenceSec = intervalSec - differenceSec;
        LocalTime timeDifference = LocalTime.ofSecondOfDay(differenceSec);
        LocalTime intervalTimeDifference = LocalTime.ofSecondOfDay(intervalDifferenceSec);
        
        if (dayOfWeek.equals("SATURDAY") || dayOfWeek.equals("SUNDAY")){
            
            if (timeDifference.compareTo(LocalTime.ofSecondOfDay(intervalSec/2)) > 0){
                durationDifference = Duration.between(timeDifference, intervalTime);
                adjustedTimestamp = (LocalDateTime) durationDifference.addTo(originalTimestamp);
            }
            else{
                durationDifference = Duration.between(intervalTimeDifference, intervalTime);
                adjustedTimestamp = (LocalDateTime) durationDifference.subtractFrom(originalTimestamp);
            }
            adjustmentType = PunchAdjustmentType.INTERVAL_ROUND;
        }
        /* CLOCK IN */
        else if (eventType.equals(EventType.CLOCK_IN)){
            durationDifference = Duration.between(s.getStartTime(), originalLocalTime);
            LocalTime dockDifference = (LocalTime)durationDifference.subtractFrom(dockTime);
            
            if (originalLocalTime.isAfter(s.getStartTime()) && originalLocalTime.isBefore(s.getLunchStart())){
                if (originalLocalTime.getMinute() > s.getGracePeriod()){
                    int dockMinutes = dockDifference.getMinute();
                    int dockSeconds = dockDifference.getSecond();
                    adjustedTimestamp = originalTimestamp.plusMinutes(dockMinutes).plusSeconds(dockSeconds);
                    adjustmentType = PunchAdjustmentType.SHIFT_DOCK;
                }
                else{
                    durationDifference = Duration.between(s.getStartTime(), originalLocalTime);
                    adjustedTimestamp = (LocalDateTime) durationDifference.subtractFrom(originalTimestamp);
                    adjustmentType = PunchAdjustmentType.SHIFT_START;
                }
            }
            else if (originalLocalTime.isBefore(s.getStartTime())){
                durationDifference = Duration.between(originalLocalTime, s.getStartTime());
                adjustedTimestamp = (LocalDateTime) durationDifference.addTo(originalTimestamp);
                adjustmentType = PunchAdjustmentType.SHIFT_START;
            }
            else if (originalLocalTime.isBefore(s.getLunchStop()) && originalLocalTime.isAfter(s.getLunchStart())){
                durationDifference = Duration.between(originalLocalTime, s.getLunchStop());
                adjustedTimestamp = (LocalDateTime) durationDifference.addTo(originalTimestamp);
                adjustmentType = PunchAdjustmentType.LUNCH_STOP;
            }
        }
        /*CLOCK OUT*/
        else if (eventType.equals(EventType.CLOCK_OUT)){
            durationDifference = Duration.between(originalLocalTime, s.getStopTime());
            Duration graceDuration = Duration.of(s.getGracePeriod(), ChronoUnit.MINUTES);
            Duration dockDuration = Duration.of(s.getDockPenalty(), ChronoUnit.MINUTES);
            
            if (originalLocalTime.isAfter(s.getStopTime()) && durationDifference.compareTo(graceDuration) < 0){
                if (differenceSec < 60){
                    adjustedTimestamp = originalTimestamp.withSecond(0).withNano(0);
                    adjustmentType = PunchAdjustmentType.NONE;
                }
                else{
                    adjustedTimestamp = (LocalDateTime) durationDifference.addTo(originalTimestamp);
                    adjustmentType = PunchAdjustmentType.SHIFT_STOP;
                }
            }
            else if (originalLocalTime.isBefore(s.getStopTime()) && originalLocalTime.isAfter(s.getLunchStop())){
                durationDifference = Duration.between(originalLocalTime, s.getStopTime());

                // clock out before grace period
                if (durationDifference.compareTo(graceDuration) > 0 && durationDifference.compareTo(dockDuration) <= 0){
                    if (durationDifference.compareTo(dockDuration) < 0){
                        durationDifference = Duration.between(originalLocalTime, s.getStopTime());
                        LocalTime dockDifference = (LocalTime)durationDifference.subtractFrom(dockTime);
                        int dockMinutes = dockDifference.getMinute();
                        int dockSeconds = dockDifference.getSecond();
                        adjustedTimestamp = originalTimestamp.minusMinutes(dockMinutes).minusSeconds(dockSeconds);
                    }
                    else{
                        adjustedTimestamp = originalTimestamp.withSecond(0).withNano(0);
                    }
                    adjustmentType = PunchAdjustmentType.SHIFT_DOCK;
                }
                else if (durationDifference.compareTo(dockDuration) > 0){
                    durationDifference = Duration.between(timeDifference, intervalTime);
                    adjustedTimestamp = (LocalDateTime) durationDifference.addTo(originalTimestamp);
                    adjustmentType = PunchAdjustmentType.INTERVAL_ROUND;
                }
                else{
                    adjustedTimestamp = (LocalDateTime) durationDifference.addTo(originalTimestamp);
                    adjustmentType = PunchAdjustmentType.SHIFT_STOP;
                }
            }
            else if (originalLocalTime.isBefore(s.getLunchStop()) && originalLocalTime.isAfter(s.getLunchStart())){
                durationDifference = Duration.between(originalLocalTime, s.getLunchStart());
                adjustedTimestamp = (LocalDateTime) durationDifference.addTo(originalTimestamp);
                adjustmentType = PunchAdjustmentType.LUNCH_START;
            }
        }
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