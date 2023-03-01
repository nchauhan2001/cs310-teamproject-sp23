package edu.jsu.mcis.cs310.tas_sp23;

import java.util.*;
import java.time.*;

public class Shift {

    private String desc;
    private LocalTime startTime, stopTime, lunchStart, lunchStop;
    // These are the parameters for a given emplyee's shift. 
    // N.B. These are to be measured in minutes!!! - William H.
    private Duration lunchduration, shiftduration;

    public Shift(HashMap map) {
        this.startTime = LocalTime.parse((String) map.get("startTime"));
        this.stopTime = LocalTime.parse((String) map.get("stopTime"));
        this.lunchStart = LocalTime.parse((String) map.get("lunchStart"));
        this.lunchStop = LocalTime.parse((String) map.get("lunchStop"));
        this.lunchduration = Duration.between(lunchStart, lunchStop);
        this.shiftduration = Duration.between(startTime, stopTime);
    }
    // WEBHOOK TEST

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
