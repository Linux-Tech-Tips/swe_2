//this will contain a description of the implementation of the algorithm via comments and stubs
package org.unixusergroup.tutorial_time.domain.scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import jakarta.persistence.Entity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.criteria.CriteriaBuilder;

@Entity
public class ScheduleCalculator {
    HashSet<Integer> U; //universal set U which contains the numbers of all students
    @ElementCollection
    //@jdbc //UHHH SMTH
    TimeSet[] timeSlots; //array of 40 timeslots
    ArrayList<int[]> timetables; //collection of all students' timetables (each is an array of length 40)
    int moduleSize; //total number of students in the module
    int[][] adjacencyMatrix;
    boolean adjacencyMatrixSet;

    public ScheduleCalculator(int moduleSize, ArrayList<int[]> timetables) {
        this.moduleSize = moduleSize;
        this.timetables = timetables;
        U = new HashSet<>(moduleSize);
        for(int i=0; i<moduleSize; i++) {
            U.add(i);
        }
        createTimeSets();
        adjacencyMatrixSet = false;
    }

    public void createTimeSets() {
        timeSlots = new TimeSet[40];
        for(int i=0; i<40; i++) {
            timeSlots[i] = new TimeSet(i, moduleSize);
        }

        // loops through each student timetable --> for each time they're available, add that student to that time set
        for(int j=0; j<moduleSize; j++) {
            for(int i=0; i<40; i++) {
                if(timetables.get(j)[i]==1) {
                    timeSlots[i].addStudent(j);
                }
            }
        }
        /*
        initialises up to 40 empty HashSets (careful of the size limit!! automatically sets to 16 I think --> set the limit to be moduleSize)
        loops through each student timetable --> for each time they're available, add that student to that time set
        the student numbers are equivalent to their position in the schedules array
        the time numbers are equivalent to their position in the time set array
         */
    }


    /**
     * delete a time
     * @param id the identifier of the time to be deleted
     */
    public void deleteTime(int id) {
        for(int i=0; i<40; i++) {
            if(timeSlots[i].getId() == i) {
                timeSlots[i].deleteTime();

                //check if the adjacency matrix exists yet (this method will be called beforehand if times are blocked off)
                if(adjacencyMatrixSet) {
                    //decrease the degrees of adjacent nodes
                    for(int j=0; j<40; j++) {
                        timeSlots[j].decreaseDegree(adjacencyMatrix[i][j]);
                    }
                }
                break;
                //IDK IF THIS MATTERS SO I HAVEN'T DONE IT
                // if relevant set the all entries in the ith column and row of the adjacency matrix to n (number of students)
            }
        }
        //IF WE SWITCH TO AN ARRAYLIST OF TIMES, JUST REMOVE IT FROM THE ARRAYLIST
    }

    /**
     * method to add all time slots together into temp variable T except for given t
     * @param t --> the timeslot to be added
     * @return --> the union
     */
    public HashSet<Integer> unionOfTimes(TimeSet t) {
        HashSet<Integer> union = copyU();
        union.removeAll(t.getStudents());
        return union;
    }

    /**
     * constraint 1 (optionally run): delete certain times
     * @param times --> all time ids for the times to be blocked out
     */
    public void blockOutTimes(int[] times) {
        for(int t : times) {
            deleteTime(t);
        }
    }

    /**
     * constraint 2 (optionally run): maximum number of students in a class
     * @param limit --> the maximum number of students
     */
    public void classSizeLimit(int limit) {
        Arrays.sort(timeSlots);
        //access from the end of the array to access the largest timeslots
        for(int i=39; i>=0; i--) {
            //if the timeslot is within the size limit --> end loop
            if(timeSlots[i].getSize()<=limit) {
                break;
            }

            //set of all elements represented in other timeslots
            HashSet<Integer> unionOfOthers = unionOfTimes(timeSlots[i]);
            for(int a : timeSlots[i].getStudents()) {
                //remove elements that are represented in other timeslots
                if(unionOfOthers.contains(a)) {
                    timeSlots[i].removeStudent(a);
                }
                //once it fits into the limit --> stop
                if(timeSlots[i].getSize()==limit) {
                    break;
                }
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
     * @return array of ids of times when everyone is available
     */
    public ArrayList<Integer> checkBaseCaseOne() {
        //loop through all time sets, add the indices of each time set whose size is equal to the number of students to the returned array

        ArrayList<Integer> times = new ArrayList<>();
        for(int i=0; i<40; i++) {
            if(timeSlots[i].getSize()==moduleSize) {
                times.add(timeSlots[i].getId());
            }
        }
        return times;
    }

    /**
     * returns a copy of the universe (needed for complements, as modifying hashsets modifies the original set --> we want to keep the universe unscathed
     * @return copy of the universe
     */
    public HashSet<Integer> copyU() {
        HashSet<Integer> Ucopy = new HashSet<>(moduleSize);
        Ucopy.addAll(U);
        return Ucopy;
    }

    /**
     * create adjacency matrix (i.e. graph)
     * each edge of weight n between two timeslots t1 and t2 represents the number of students busy at both times
     * in parallel initialise the degrees of the nodes
     */
    //TODO not ideal, as the matrix is mirrored on the diagonal --> does everything twice
    public void createAdjacencyMatrix() {
        adjacencyMatrix = new int[40][40];

        //create array of complements
        TimeSet[] complements = new TimeSet[40];
        for(int i=0; i<40; i++) {
            HashSet<Integer> tempU = copyU();
            complements[i] = timeSlots[i].complement(tempU);
        }

        for(int i=0; i<40; i++) {
            for(int j=0; j<40; j++) {
                //don't compare nodes to themselves, just put in -1
                if(j==i) {
                    adjacencyMatrix[j][i] = -1;
                    continue;
                }
                //for each element in node i --> if j contains it, add one to their adjacency matrix field, increase both their degrees
                for(int a : complements[i].getStudents()) {
                    if(complements[j].getStudents().contains(a)) {
                        adjacencyMatrix[j][i]++;
                        complements[j].increaseDegree(1);
                        complements[i].increaseDegree(1);
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

        for(int i=0; i<40; i++) {
            for (int j = 0; j < 40; j++) {
                if(adjacencyMatrix[i][j]==0) {
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
     * @return an array representing the times needed to cover all students
     */
    public ArrayList<Integer> schedule() {
        ArrayList<Integer> times = new ArrayList<>();
        HashSet<Integer> unionOfOthers;
        for(int i=0; i<40; i++) {
            Arrays.sort(timeSlots);
            unionOfOthers = unionOfTimes(timeSlots[i]);

            //if all students in a given timeslot are represented in other timesets --> delete that timeset
            if(unionOfOthers.containsAll(timeSlots[i].getStudents())) {
                deleteTime(timeSlots[i].getId());
            }

            //if not --> add it to the returned times
            else {
                times.add(timeSlots[i].getId());
            }
        }
        return times;
    }

}