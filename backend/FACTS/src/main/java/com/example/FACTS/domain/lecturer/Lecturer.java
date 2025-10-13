package com.example.FACTS.domain.lecturer;

import com.example.FACTS.domain.Courses.Course;
import com.example.FACTS.domain.Tutorial.TimeTable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.List;

@Entity
public class Lecturer {

    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    private int staffId;

    private String name;
    private String email;
    private TimeTable timeslots;
    private List<Course> courses;

    protected Lecturer() {};

    Lecturer(String name, int staffId, String email, TimeTable timeslots, List<Course> courses) {
        this.name = name;
        this.staffId = staffId;
        this.email = email;
        this.timeslots = timeslots;
        this.courses = courses;
    }

    public String getName() {
        return name;
    }

    public int getStaffId() {
        return staffId;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
