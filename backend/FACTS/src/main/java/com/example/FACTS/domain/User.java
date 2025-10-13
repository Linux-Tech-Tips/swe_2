/**
 * A class representing the basis for a student and a lecturer.
 */
package com.example.FACTS.domain;


import com.example.FACTS.domain.Tutorial.TimeTable;
import java.util.List;

import com.example.FACTS.domain.Tutorial.Timeslot;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private List<String> courses;
    private TimeTable timeTable;


    protected User() {}

    public User(String name, List<String> courses) {
        this.name = name;
        this.courses = courses;
        this.timeTable = new TimeTable();
    }

    public User(String name, List<String> courses, TimeTable timetable) {
        this.name = name;
        this.courses = courses;
        this.timeTable = timetable;
    }

    @Override
    public String toString() {
        return String.format("%s[name='%s', courseCode='%s']", this.getClass(), this.name, this.courses);
    }

    public String getName() {
        return this.name;
    }

    public List<String> getCourses() {
        return courses;
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addBusyTime(Timeslot time) {
        this.timeTable.setUnavailableTimeSlot(time);
    }
    public void addFreeTime(Timeslot time) {
        this.timeTable.setAvailableTimeSlot(time);
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }

    public void setTimeTable(TimeTable timeTable) {
        this.timeTable = timeTable;
    }
}
