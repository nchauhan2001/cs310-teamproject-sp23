package edu.jsu.mcis.cs310.tas_sp23;
/*
 * Punch class represents an instance of a punch by an employee using their badge.
 * Each punch is identified by a terminal ID, badge, and an event type (CLOCK_IN, CLOCK_OUT, or TIME_OUT).
 * The originalTimestamp is the time when the punch was recorded.
 * The id is the unique identifier for each punch.
 * The adjustedTimestamp is the time after any punch adjustment has been made.
 * The adjustmentType is the type of punch adjustment that was made.
 */

import static edu.jsu.mcis.cs310.tas_sp23.EventType.CLOCK_IN;
import static edu.jsu.mcis.cs310.tas_sp23.EventType.CLOCK_OUT;
import static edu.jsu.mcis.cs310.tas_sp23.EventType.TIME_OUT;
import java.time.LocalDateTime;

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

    public EventType getEventType() {
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

    // prints the original punch details to console
    public void printOriginal() {
        String punchTypeString;
        switch (eventType) {
            case CLOCK_IN:
                punchTypeString = "CLOCK IN";
                break;
            case CLOCK_OUT:
                punchTypeString = "CLOCK OUT";
                break;
            case TIME_OUT:
                punchTypeString = "TIME OUT";
                break;
            default:
                punchTypeString = "UNKNOWN";
                break;
        }
        System.out.println("#" + badge.getId() + " " + punchTypeString + ": " + originalTimestamp.toString());
    }

    // overrides toString() method to print original punch details
    @Override
    public String toString() {
        printOriginal();
        return "";
    }
}