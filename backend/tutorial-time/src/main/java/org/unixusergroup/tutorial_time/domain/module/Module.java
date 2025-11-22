/** 
 * @author Ethan, Martin, Lilian
 */
package org.unixusergroup.tutorial_time.domain.module;


import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.student.Student;

import java.util.List;
import java.util.ArrayList;

@Entity
/** Class representing a university Module entity */
public class Module {

    /** The module code uniquely identifying a module */
    @Id
    private String moduleCode;
    /** ids of students enrolled in the module */
    private List<Long> students;
    /** ids of students working as teaching assistants (TA) for this module */
    private List<Long> taStudents;
    /** The lecturer in charge of this module */
    private long lecturer;

    public Module() {};
    /** Constructor creating an empty module with a given code and associated lecturer */
    public Module(String moduleCode, long lecturer) {
        this(moduleCode, new ArrayList<>(), new ArrayList<>(), lecturer);
    }

    /** Constructor creating a module with the given code, lecturer and student lists */
    public Module(String moduleCode, List<Long> students, List<Long> taStudents, long lecturer) throws IllegalArgumentException {
        if(moduleCode.isEmpty()) {
            throw new IllegalArgumentException("The module code cannot be empty");
        }
        if(lecturer<1) {
            throw new IllegalArgumentException("The lecturer ID must be a positive integer");
        }
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
    public void addStudent(long student) throws IllegalArgumentException {
        if(student < 1) {
            throw new IllegalArgumentException("The student ID must be positive");
        }
        
        if (!students.contains(student)) {
            students.add(student);
        }
    }

    /**
     * removes a given Student ID from the list of enrolled students
     */

    public void removeStudent(long student) throws IllegalArgumentException {
        if(student < 1) {
            throw new IllegalArgumentException("The student ID must be positive");
        }
        if(!students.remove(student)) {
            throw new IllegalArgumentException("The given student is not registered for this module");
        }
    }


    /** Adds a Student id to the list of TA students for the module */
    public void addTAStudent(long student) throws IllegalArgumentException {
        if(student < 1) {
            throw new IllegalArgumentException("The student ID must be positive");
        }
        taStudents.add(student);
    }

    /**
     * removes a given Student ID from the list of TAs
     */
    
    public void removeTAStudent(long student) throws IllegalArgumentException {
        if(student < 1) {
            throw new IllegalArgumentException("The student ID must be positive");
        }
        if(!taStudents.remove(student)) {
            throw new IllegalArgumentException("The given student is not a TA for this module");
        }
    }
}
