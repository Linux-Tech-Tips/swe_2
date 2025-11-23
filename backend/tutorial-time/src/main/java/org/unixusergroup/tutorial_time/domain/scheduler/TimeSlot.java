/**
 * class to represent a time for the scheduler
 * @author Cedar
 */
package org.unixusergroup.tutorial_time.domain.scheduler;
import org.unixusergroup.tutorial_time.application.exceptions.IllegalModuleSizeException;
import org.unixusergroup.tutorial_time.application.exceptions.IllegalStudentAdditionException;
import org.unixusergroup.tutorial_time.application.exceptions.IllegalStudentRemovalException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class TimeSlot implements Comparable<TimeSlot> {
    private final HashSet<Integer> students; //hashset of all students available at that time
    private final int id; //unique identifier
    private int size; //number of students available (i.e. students.size())
    private int degree;
    private final int capacity;

    /**
     * constructor
     * @param id ID
     * @param moduleSize number of students in the module
     */
    public TimeSlot(int id, int moduleSize) throws IllegalModuleSizeException {
        if(moduleSize<1) {
            throw new IllegalModuleSizeException("The module size must be positive");
        }
        this.id = id;
        size = 0;
        degree = 0;
        students = new HashSet<>(moduleSize);
        capacity = moduleSize;
    }

    /**
     * add a given student, increase the size
     * throw exceptions when the set is full or the student already exists in the set
     * @param student the studentID to be added
     */
    public void addStudent(int student) throws IllegalStudentAdditionException {
        if(size==capacity) {
            throw new IllegalStudentAdditionException("The student set is at capacity");
        }

        if (students.contains(student)) {
            throw new IllegalStudentAdditionException("That student is already included");
        }
        students.add(student);
        size++;
    }

    /**
     * remove a given student, decrease the size
     * throws exception when the student doesn't exist or the set is empty
     * @param student the studentID to be removed
     */
    public void removeStudent(int student) throws IllegalStudentRemovalException {
        if(size==0) {
            throw new IllegalStudentRemovalException("The student set is empty");
        }

        if (!students.contains(student)) {
            throw new IllegalStudentRemovalException("That student is not in the set");
        }
        students.remove(student);
        size--;
    }

    public void removeStudents(ArrayList<Integer> students) {
        for(int i: students) {
            this.removeStudent(i);
        }
    }

    public int getSize() {
        return size;
    }

    public int getId() {
        return id;
    }

    public HashSet<Integer> getStudents() {
        return students;
    }

    public void decreaseDegree(int d) {
        if(degree-d>=0) {
            degree-=d;
            return;
        }
        throw new IllegalArgumentException("The degree cannot be negative");
    }

    public void increaseDegree(int d) {
        degree+=d;
    }

    public int getDegree() {
        return degree;
    }

    /**
     * method for comparing timeslots, is compared such that when a list is sorted it is sorted ascending for no. students available at each, when equal size sorted descending by degree
     * @param o object to be compared to
     * @return negative number if this is smaller, positive number if o is smaller
     */
    @Override
    public int compareTo(TimeSlot o) {
        if(size!=o.getSize()) {
            return size-o.getSize();
        }
        return o.getDegree()-degree;
    }

    /**
     * method for equality based on ID
     * @param o   the reference object with which to compare.
     * @return true if equal, false if not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimeSlot otherTimeslot = (TimeSlot) o;
        return this.id == otherTimeslot.getId();
    }
}
