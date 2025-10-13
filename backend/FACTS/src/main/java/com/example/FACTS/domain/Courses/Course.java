package com.example.FACTS.domain.Courses;
import com.example.FACTS.domain.lecturer.Lecturer;
import com.example.FACTS.domain.student.Student;
import com.example.FACTS.domain.Tutorial.Timeslot;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.List;

@Entity
public class Course {
    @Id
    private String courseCode;
    private List<Student> enlistedStudents;
    private List<Lecturer> activeLecturers;
    private List<Timeslot> taughtTimes;


    public String getCode() {
        return courseCode;
    }

    public List<Student> getEnlistedStudents() {
        return enlistedStudents;
    }

    public List<Lecturer> getActiveLecturers() {
        return activeLecturers;
    }

    public List<Timeslot> getTaughtTimes() {
        return taughtTimes;
    }

    public void setCode(String code) {
        this.courseCode = courseCode;
    }

    public void setEnlistedStudents(List<Student> enlistedStudents) {
        this.enlistedStudents = enlistedStudents;
    }

    public void setActiveLecturers(List<Lecturer> activeLecturers) {
        this.activeLecturers = activeLecturers;
    }

    public void setTaughtTimes(List<Timeslot> taughtTimes) {
        this.taughtTimes = taughtTimes;
    }
}
