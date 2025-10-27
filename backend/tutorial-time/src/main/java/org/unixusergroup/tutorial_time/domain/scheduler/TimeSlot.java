/**
 * class to represent a time for the scheduler
 * @author Cedar
 */
package org.unixusergroup.tutorial_time.domain.scheduler;
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
    public TimeSlot(int id, int moduleSize) throws IllegalArgumentException{
        if(moduleSize<1) {
            throw new IllegalArgumentException("The module size must be positive");
        }
        this.id = id;
        size = 0;
        degree = 0;
        students = new HashSet<>(moduleSize);
        capacity = moduleSize;
    }

    public int getCapacity() {
        return capacity;
    }

    /**
     * add a given student, increase the size
     * throw exceptions when the set is full or the student already exists in the set
     * @param student the studentID to be added
     */
    public void addStudent(int student){
        if(size==capacity) {
            throw new IllegalArgumentException("The student set is at capacity");
        }

        if (students.contains(student)) {
            throw new IllegalArgumentException("That student is already included");
        }
        students.add(student);
        size++;
    }

    /**
     * remove a given student, decrease the size
     * throws exception when the student doesn't exist or the set is empty
     * @param student the studentID to be removed
     */
    public void removeStudent(int student) {
        if(size==0) {
            throw new IllegalArgumentException("The student set is empty");
        }

        if (!students.contains(student)) {
            throw new IllegalArgumentException("That student is not in the set");
        }
        students.remove(student);
        size--;
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
        //TODO throw exception if it would become negative
        degree-=d;
    }

    public void increaseDegree(int d) {
        degree+=d;
    }

    public int getDegree() {
        return degree;
    }

    /**
     * method for comparing timesets, is compared such that when a list is sorted it is sorted ascending for no. students available at each, when equal size sorted descending by degree
     * @param o object to be compared to
     * @return negative number if this is smaller, positive number if o is smaller
     */
    @Override
    public int compareTo(TimeSlot o) {
        //TODO: TESTS
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
        TimeSlot otherTimeset = (TimeSlot) o;
        return this.id == otherTimeset.getId();
    }
}
