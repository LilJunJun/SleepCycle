package com.nramos.sleepcycle;

import java.util.Date;

public class Cycle
{
    private Date time;
    private String numberOfCycles;
    private int hour;
    private int minute;
    private String cyclesDuration;

    public Cycle(Date time, String numberOfCycles, int hour, int minute)
    {
        this.time = time;
        this.numberOfCycles = numberOfCycles;
        this.hour = hour;
        this.minute = minute;
    }

    public Date getTime() {
        return time;
    }

    public void Date(Date time) {
        this.time = time;
    }

    public String getNumberOfCycles() {
        return numberOfCycles;
    }

    public void setNumberOfCycles(String numberOfCycles) {
        this.numberOfCycles = numberOfCycles;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String toString()
    {
        return "Cycle toString: " + time + " " + numberOfCycles + " " + hour + ":" + minute;
    }
}
