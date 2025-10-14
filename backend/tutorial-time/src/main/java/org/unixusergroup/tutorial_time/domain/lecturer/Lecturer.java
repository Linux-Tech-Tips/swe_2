package org.unixusergroup.tutorial_time.domain.lecturer;

import org.unixusergroup.tutorial_time.domain.Courses.Course;
import org.unixusergroup.tutorial_time.domain.Tutorial.TimeTable;
import org.unixusergroup.tutorial_time.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Lecturer extends User {
    @Id
    public int staffID;

    public Lecturer() {};
    public Lecturer (String name, int staffID) {
        this.name = name;
        this.staffID = staffID;
        this.courses = new ArrayList<Course>();
        this.timeTable = (TimeTable) TimeTable.generateTimeTable();
    }


    public void setCourses(List<String> courses) {
        this.courses = courses;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setTimeTable(TimeTable timeTable) {
        this.timeTable = timeTable;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public List<Course> getCourses() {
        return super.getCourses();
    }

    @Override
    public TimeTable getTimeTable() {
        return super.getTimeTable();
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public int getStaffID() {
        return staffID;
    }

}
