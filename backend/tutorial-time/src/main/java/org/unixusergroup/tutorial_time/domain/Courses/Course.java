package org.unixusergroup.tutorial_time.domain.Courses;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.student.Student;
 
import java.util.List;

public class Course {
    private long id;

    private String courseCode;
    private List<Student> enlistedStudents;
    private Lecturer lecturer;
    private List<Student> activeTAs;

    public Course(String courseCode, List<Student> enlistedStudents, Lecturer lecturer) {
        this.courseCode = courseCode;
        this.enlistedStudents = enlistedStudents;
        this.lecturer = lecturer;
        this.activeTAs = activeTAs;
    }

    public Course of(String courseCode, List<Student> enlistedStudents, Lecturer lecturer) {
        return new Course(courseCode, enlistedStudents, lecturer,  List.of());
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

    public List<Student> getActiveTAs() {
        return activeTAs;
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
    
    public void setActiveTAs(List<Student> activeTAs) {
        this.activeTAs = activeTAs;
    }
}
