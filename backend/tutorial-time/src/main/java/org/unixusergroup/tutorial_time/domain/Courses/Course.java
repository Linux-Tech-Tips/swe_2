package org.unixusergroup.tutorial_time.domain.Courses;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.student.Student;

import java.util.List;

public class Course {
    private String courseCode;
    private List<Student> enlistedStudents;
    private Lecturer lecturer;

    public Course(String courseCode, List<Student> enlistedStudents, Lecturer lecturer) {
        this.courseCode = courseCode;
        this.enlistedStudents = enlistedStudents;
        this.lecturer = lecturer;
    }

    public Course of(String courseCode, List<Student> enlistedStudents, Lecturer lecturer) {
        return new Course(courseCode, enlistedStudents, lecturer);
    }
    public String getCode() {
        return courseCode;
    }

    public List<Student> getEnlistedStudents() {
        return enlistedStudents;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setCode(String code) {
        this.courseCode = courseCode;
    }

    public void setEnlistedStudents(List<Student> enlistedStudents) {
        this.enlistedStudents = enlistedStudents;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }
}
