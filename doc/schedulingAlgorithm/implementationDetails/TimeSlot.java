import java.util.HashSet;

/*
 * represent each time slot as an object?? attributes being the hashset of all students available then, the number of students available then, its index number, and its degree
 *       make the timeslot class extend comparable such that it would be ascending for no. students available at each, when equal by descending by degree
 *       methods for adding/removing items (remember to edit the size and degree accordingly), getters for all fields
 */

public class TimeSlot implements Comparable<TimeSlot> {
    HashSet<Integer> students; //hashset of all students available at that time
    int id; //unique identifier
    //potentially needs to  be bigger than int (depending on module size)??
    int size; //number of students available (i.e. students.size())
    int degree;

    public TimeSlot(int id) {
        //initialise all values to be empty
        //set id to id
    }

    public void addStudent(int student){
        //add student to the hashset, increment the size
    }

    public void removeStudent(int student) {
        //remove student from the hashset, decrement the size, if relevant decrement the degree
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

    @Override
    public int compareTo(TimeSlot o) {
        //compare such that it would be sorted ascending for no. students available at each
        // when equal size sorted descending by degree
        return 0;
    }
}
