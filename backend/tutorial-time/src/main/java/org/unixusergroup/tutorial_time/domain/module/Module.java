/** 
 * @author Ethan, Martin
 */
package org.unixusergroup.tutorial_time.domain.module;

import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.student.Student;

import java.util.List;
import java.util.ArrayList;

/** Class representing a university Module entity */
public class Module {

    /** The module code uniquely identifying a module */
    private String moduleCode;
    /** ids of students enrolled in the module */
    private List<Long> students;
    /** ids of students working as teaching assistants (TA) for this module */
    private List<Long> taStudents;
    /** The lecturer in charge of this module */
    private long lecturer;

    /** Constructor creating an empty module with a given code and associated lecturer */
    public Module(String moduleCode, long lecturer) {
        this(moduleCode, new ArrayList<>(), new ArrayList<>(), lecturer);
    }

    /** Constructor creating a module with the given code, lecturer and student lists */
    public Module(String moduleCode, List<Long> students, List<Long> taStudents, long lecturer) {
        this.moduleCode = moduleCode;
        this.students = students;
        this.taStudents = taStudents;
        this.lecturer = lecturer;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Module) {
            return ((Module)(obj)).getModuleCode().equals(this.getModuleCode());
        }
        else {
            return false;
        }
    }
    public String getModuleCode() {
        return moduleCode;
    }

    public List<Long> getStudents() {
        return students;
    }

    public List<Long> getTAStudents() {
        return taStudents;
    }

    public long getLecturer() {
        return lecturer;
    }

    /** Adds a Student id to the list of enrolled students */
    public void addStudent(long student) {
        students.add(student);
    }

    /** Adds a Student id to the list of TA students for the module */
    public void addTAStudent(long student) {
        taStudents.add(student);
    }
}
