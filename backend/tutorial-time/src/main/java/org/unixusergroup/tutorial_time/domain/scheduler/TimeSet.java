package org.unixusergroup.tutorial_time.domain.scheduler;
import java.util.HashSet;

public class TimeSet implements Comparable<TimeSet> {
    HashSet<Integer> students; //hashset of all students available at that time
    int id; //unique identifier
    //potentially needs to  be bigger than int (depending on module size)??
    int size; //number of students available (i.e. students.size())
    int degree;

    public TimeSet(int id, int moduleSize) {
        this.id = id;
        size = 0;
        degree = 0;
        students = new HashSet<>(moduleSize);
    }

    /**
     * methhod for getting the complement of a timeset
     * @param universe the universal set (all possible elements)
     * @return
     */
    public TimeSet complement(HashSet<Integer> universe) {
        universe.removeAll(students);
        TimeSet complement = new TimeSet(id, universe.size());
        complement.addStudentGroup(universe);
        return complement;
    }

    public void addStudent(int student){
        students.add(student);
        size++;
        //DEGREE???
    }

    public void removeStudent(int student) {
        students.remove(student);
        size--;
        //DEGREE????
    }

    public void deleteTime() {
        students.clear();
        size = 0;
        degree = 0;
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

    public void addStudentGroup(HashSet<Integer> students) {
        this.students.addAll(students);
        size+=students.size();
    }

    public void decreaseDegree(int d) {
        degree-=d;
    }

    public void increaseDegree(int d) {
        degree+=d;
    }

    public void increaseSize() {
        size++;
    }

    public void decreaseSize() {
        size--;
    }

    public int getDegree() {
        return degree;
    }

    @Override
    public int compareTo(TimeSet o) {
        //TODO: CHECK THIS GUY
        if(size!=o.getSize()) {
            return o.getSize()-size;
        }
        return degree-o.getDegree();
        //compare such that it would be sorted ascending for no. students available at each
        // when equal size sorted descending by degree
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimeSet otherTimeset = (TimeSet) o;
        return this.id == otherTimeset.getId();
    }
}
