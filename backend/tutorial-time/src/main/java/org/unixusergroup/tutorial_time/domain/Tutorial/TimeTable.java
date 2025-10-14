package org.unixusergroup.tutorial_time.domain.Tutorial;
/**
 * Required methods.
 * Filtering by
 */

import java.util.ArrayList;
import java.util.List;

public class TimeTable {
    private List<Timeslot> timeSlots;

    public TimeTable() {
        this.timeSlots = TimeTable.generateTimeTable();
    }
    public TimeTable(List<Timeslot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public static List<Timeslot> generateTimeTable() {
        List<Timeslot> times = new ArrayList<Timeslot>();
        List<String> days = new ArrayList<String>();

        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");

        for(String day: days) {
            for(int i = 9; i < 18; i++) {
                times.add(new Timeslot(day, i));
            }
        }
        return times;
    }

    public void setUnavailableTimeSlot(Timeslot busyTime) {
        int index = this.timeSlots.indexOf(busyTime);
        busyTime.setAvailability(Availability.UNAVAILABLE);
        this.timeSlots.set(index, busyTime);
    }

    public void setUnavailableTimeSlots(List<Timeslot> differentTimes) {
        this.timeSlots = this.timeSlots.stream().filter(differentTimes::contains).map((timeslot) -> {
            timeslot.setAvailability(Availability.UNAVAILABLE);
            return timeslot;
        }).toList();
    }

    public void setAvailableTimeSlot(Timeslot busyTime) {
        int index = this.timeSlots.indexOf(busyTime);
        busyTime.setAvailability(Availability.AVAILABLE);
        this.timeSlots.set(index, busyTime);
    }

    public List<Timeslot> getAvailableTimeSlots() {
        return timeSlots.stream().filter((t) -> {
            return t.getAvailability() == Availability.AVAILABLE;
        }).toList();
    }

    public List<Timeslot> getUnavailableTimeSlots() {
        return timeSlots.stream().filter((t) -> {
            return t.getAvailability() == Availability.UNAVAILABLE;
        }).toList();
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("STUB");
        // TODO STUB - WIP IMPLEMENT LATER
        return result.toString();
    }
}
