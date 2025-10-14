package org.unixusergroup.tutorial_time.domain.student;

import org.unixusergroup.tutorial_time.domain.Courses.Course;
import org.unixusergroup.tutorial_time.domain.Tutorial.TimeTable;
import org.unixusergroup.tutorial_time.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Student extends User {

    @Id
    private int studentID;

    public Student() {};

    public Student (String name, int studentID) {
        this.name = name;
        this.studentID = studentID;
        this.courses = new ArrayList<String>();
        this.timeTable = new TimeTable();
    }

    public void setTimeTable(TimeTable timeTable) {
        this.timeTable = timeTable;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setCourses(List<String> courses) {
        this.courses = courses;
    }

    @Override
    public List<String> getCourses() {
        return super.getCourses();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public int getStudentID() {
        return studentID;
    }
}
