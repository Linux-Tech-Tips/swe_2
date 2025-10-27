/**
 * class representing the scheduler, has all necessary methods to create a scheduler instance and schedule tutorials
 * @author Cedar
 */

package org.unixusergroup.tutorial_time.domain.scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import jakarta.persistence.Entity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Id;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

//TODO: does he NEED to be an entity?? isn't going to be stored in database or accessed, will just be recreated for new requests
@Entity
public class Scheduler {
    private final HashSet<Integer> U; //universal set U which contains the numbers of all students
    @ElementCollection
    @JdbcTypeCode(SqlTypes.JSON)
    private ArrayList<TimeSlot> timeSlots; //array of 40 timeslots
    private final ArrayList<int[]> timetables; //collection of all students' timetables (each is an array of length 40)
    private final int moduleSize; //total number of students in the module
    private ArrayList<int[]> adjacencyMatrix;
    private boolean adjacencyMatrixSet;
    private int numberOfTimeSlots;

    @Id
    private int id;

    /**
     * Constructor
     * @param timetables the timetables of each student
     */
    public Scheduler(ArrayList<int[]> timetables) {
        if(timetables.isEmpty()) {
            throw new IllegalArgumentException("At least one timetable must be entered");
        }
        this.moduleSize = timetables.size();
        this.timetables = timetables;
        U = new HashSet<>(moduleSize);
        for(int i=0; i<moduleSize; i++) {
            U.add(i);
        }
        adjacencyMatrixSet = false;
        this.numberOfTimeSlots = timetables.getFirst().length;
        try {
            createTimeSlots();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    //TODO check if there's students who are never available

    public int getModuleSize() {
        return moduleSize;
    }

    public HashSet<Integer> getUniverse() {
        return U;
    }

    public ArrayList<int[]> getTimetables() {
        return timetables;
    }

    public boolean getAdjacencyMatrixSet() {
        return adjacencyMatrixSet;
    }

    public int getNumberOfTimeSlots() {
        return numberOfTimeSlots;
    }

    /**
     * method to initialise all the timeslots, setting each to include all students that are available at that time
     */
    public void createTimeSlots() throws IllegalArgumentException{
        timeSlots = new ArrayList<>();
        for(int i = 0; i< numberOfTimeSlots; i++) {
            try {
                timeSlots.add(new TimeSlot(i, moduleSize));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            }
        }

        // loops through each student timetable --> for each time they're available, add that student to that time set
        for(int j=0; j<moduleSize; j++) {
            for(int i = 0; i< numberOfTimeSlots; i++) {
                if(timetables.get(j)[i]==1) {
                    timeSlots.get(i).addStudent(j);
                }
            }
        }
        /*
        initialises numberOftimeslots empty HashSets
        loops through each student timetable --> for each time they're available, add that student to that time set
        the student numbers are equivalent to their position in the schedules array
        the time numbers are equivalent to their position in the time set array
         */
    }

    /**
     * delete a time
     * @param id the identifier of the time to be deleted
     */
    public void deleteTime(int id) throws IllegalArgumentException {
        //find the timeslot with the given id
        for(int i = 0; i< numberOfTimeSlots; i++) {
            if (timeSlots.get(i).getId() == id) {
                //check if there's any students that are only available at this time
                if(!unionOfTimes(timeSlots.get(i)).containsAll(timeSlots.get(i).getStudents())) {
                    //TODO: make this exception better (i.e. say which time/students it is
                    // also should it throw a warning instead of an exception? maybe lecturers want to force it??
                    throw new IllegalArgumentException("There are students who are only available at this time");
                }
                System.out.println("We have removed time " + id);
                timeSlots.remove(i);
                numberOfTimeSlots--;
                //check if the adjacency matrix exists yet (this method will be called before it's created if times are blocked off)
                if (adjacencyMatrixSet) {
                    //decrease the degrees of adjacent nodes
                    for (int j = 0; j < numberOfTimeSlots; j++) {
                        timeSlots.get(j).decreaseDegree(adjacencyMatrix.get(i)[j]);
                    }
                }
                return;
            }
        }
        throw new IllegalArgumentException("The given time does not exist");
    }

    /**
     * for testing purposes
     */
    public ArrayList<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    /**
     * method to add all time slots together into temp variable T except for given t
     * @param t --> the timeslot to be added
     * @return --> the union
     */
    public HashSet<Integer> unionOfTimes(TimeSlot t) {
        HashSet<Integer> unionOfOthers = new HashSet<>(moduleSize);
        for(TimeSlot i : timeSlots) {
            if(i.equals(t)) {
                continue;
            }
            unionOfOthers.addAll(i.getStudents());
        }
        return unionOfOthers;
    }

    /**
     * constraint 1 (optionally run): delete certain times
     * @param times --> all time ids for the times to be blocked out
     */
    public void blockOutTimes(ArrayList<Integer> times) {
        for(int t : times) {
            deleteTime(t);
        }
    }

    /**
     * constraint 2 (optionally run): maximum number of students in a class
     * @param limit --> the maximum number of students
     */
    public void classSizeLimit(int limit) throws IllegalArgumentException {
        Collections.sort(timeSlots);
        //access from the end of the array to access the largest timeslots
        for(int i = numberOfTimeSlots -1; i>=0; i--) {
            //if the timeslot is within the size limit --> end loop
            if(timeSlots.get(i).getSize()<=limit) {
                break;
            }
            //set of all elements represented in other timeslots
            HashSet<Integer> unionOfOthers = unionOfTimes(timeSlots.get(i));
            for(int a : timeSlots.get(i).getStudents()) {
                //remove elements that are represented in other timeslots
                if(unionOfOthers.contains(a)) {
                    timeSlots.get(i).removeStudent(a);
                }
                //once it fits into the limit --> stop
                if(timeSlots.get(i).getSize()==limit) {
                    break;
                }
            }
            if(timeSlots.get(i).getSize()>limit) {
                //TODO should it throw a warning instead of an exception? maybe lecturers want to force it??
                throw new IllegalArgumentException("This class size limit is not possible");
            }

        }
        /*
        sort the array of timeslots
        accessing from the back of the array repeat these steps until the accessed timeslot t has size < limit:
          obtain temp variable T of union of all other sets via unionOfTimes(t)
          iterate over entries a1, a2, ... an in the t hashset, if T.contains(ai) --> remove ai from t hashset and reduce the size
          stop loop once the size of t is equal to the limit
         */
    }

    //TODO: ADD TA CONSTRAINTS

    /**
     * check base case 1: there is one or more times when everyone is available
     * COULD HAVE BETTER NAME
     * @return arraylist of ids of times when everyone is available
     */
    public ArrayList<Integer> checkBaseCaseOne() {
        //loop through all time sets, add the indices of each time set whose size is equal to the number of students to the returned array

        ArrayList<Integer> times = new ArrayList<>();
        for(int i = 0; i< numberOfTimeSlots; i++) {
            if(timeSlots.get(i).getSize()==moduleSize) {
                times.add(timeSlots.get(i).getId());
            }
        }
        return times;
    }

    /**
     * for testing
     */

    public ArrayList<int[]> getAdjacencyMatrix() {
        return adjacencyMatrix;
    }
    /**
     * create adjacency matrix (i.e. graph)
     * each edge of weight n between two timeslots t1 and t2 represents the number of students busy at both times
     * in parallel initialise the degrees of the nodes
     */
    public void createAdjacencyMatrix() {
        adjacencyMatrix = new ArrayList<>();
        for(int i = 0; i< numberOfTimeSlots; i++) {
            adjacencyMatrix.add(new int[numberOfTimeSlots]);
        }

        for(int i = 0; i< numberOfTimeSlots; i++) {
            for(int j = 0; j< numberOfTimeSlots; j++) {
                //don't compare nodes to themselves, just put in -1
                if(j==i) {
                    adjacencyMatrix.get(j)[i] = -1;
                    continue;
                }
                //for all students --> if both j and i don't contain that student --> increase their matrix entry, increase their degrees
                for(int a : U) {
                    if(!timeSlots.get(j).getStudents().contains(a) && !timeSlots.get(i).getStudents().contains(a)) {
                        adjacencyMatrix.get(j)[i]++;
                        timeSlots.get(j).increaseDegree(1);
                        timeSlots.get(i).increaseDegree(1);
                    }
                }
            }
        }
        adjacencyMatrixSet = true;
    }

    /**
     * check base case 2: two (n) or more times when everyone is available at n-1 of the times
     * COULD HAVE A BETTER NAME
     * @return an array representing those times
     */
    public ArrayList<Integer> checkBaseCaseTwo() {
        createAdjacencyMatrix();
        ArrayList<Integer> times = new ArrayList<>();

        for(int i = 0; i< numberOfTimeSlots; i++) {
            for (int j = 0; j < numberOfTimeSlots; j++) {
                if(adjacencyMatrix.get(i)[j]==0) {
                    if(!times.contains(i)) {
                        times.add(i);
                    }
                    if(!times.contains(j)) {
                        times.add(j);
                    }
                }
            }
        }
        /*
        runs through the adjacency matrix, adds all nodes with an intersection of 0 to the times array
        e.g. if adjacencyMatrix [a][b] = 0 --> add a, b
        (0 means no ppl busy at both times)
         */
        return times;
    }

    /**
     * actual scheduling algorithm
     * @return an arraylist including all the times needed to cover all students
     */
    public ArrayList<Integer> schedule() {
        ArrayList<Integer> times = new ArrayList<>();
        HashSet<Integer> unionOfOthers;
        for(int i = 0; i< numberOfTimeSlots; i++) {
            Collections.sort(timeSlots);
            TimeSlot temp = timeSlots.get(i);
            unionOfOthers = unionOfTimes(temp);

            //if not all students in a given timeslot are represented in other timeslots --> add it to the returned times
            if(!(unionOfOthers.containsAll(temp.getStudents()))) {
                times.add(temp.getId());
            }
        }
        return times;
    }

}