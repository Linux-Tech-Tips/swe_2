/** 
 * @author Ethan, Martin
 */
package org.unixusergroup.tutorial_time.domain.module;

import jakarta.persistence.Entity;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.student.Student;

import java.util.List;
import java.util.ArrayList;

@Entity
/** Class representing a university Module entity */
public class Module {

    /** The module code uniquely identifying a module */
    private String moduleCode;
    /** Students enrolled in the module */
    private List<Student> students;
    /** Students working as teaching assistants (TA) for this module */
    private List<Student> taStudents;
    /** The lecturer in charge of this module */
    private Lecturer lecturer;

    /** Constructor creating an empty module with a given code and associated lecturer */
    public Module(String moduleCode, Lecturer lecturer) {
        this(moduleCode, new ArrayList<>(), new ArrayList<>(), lecturer);
    }

    /** Constructor creating a module with the given code, lecturer and student lists */
    public Module(String moduleCode, List<Student> students, List<Student> taStudents, Lecturer lecturer) {
        this.moduleCode = moduleCode;
        this.students = students;
        this.taStudents = taStudents;
        this.lecturer = lecturer;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Student> getTAStudents() {
        return taStudents;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    /** Adds a Student instance to the list of enrolled students */
    public void addStudent(Student student) {
        students.add(student);
    }

    /** Adds a Student instance to the list of TA students for the module */
    public void addTAStudent(Student student) {
        taStudents.add(student);
    }
}
