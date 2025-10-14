/**
 *
 */
package com.example.tutorial_time.domain.Tutorial;

public class Timeslot {

    private Availability availability;
    private String day;
    private int hour;

    protected Timeslot () {}

    public Timeslot(String day, int hour) {
        this.day = day;
        this.hour = hour;
        this.availability = Availability.AVAILABLE;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Timeslot) {
            return ((Timeslot) obj).day.equals(this.day) && ((Timeslot)obj).hour == this.hour;
        }
        else {
            return false;
        }
    }

    public int getHour() {
        return hour;
    }

    public String getDay() {
        return day;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }
}
