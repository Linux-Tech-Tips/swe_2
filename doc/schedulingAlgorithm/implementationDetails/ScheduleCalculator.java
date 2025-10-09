//this will contain a description of the implementation of the algorithm via comments and stubs


import java.util.ArrayList;
import java.util.HashSet;

public class ScheduleCalculator {
    HashSet<Integer> U; //universal set U which contains the numbers of all students
    TimeSlot[] timeSlots; //array of 40 timeslots
    ArrayList<int[]> timetables; //collection of all students' timetables (each is an array of length 40)
    //this may be better as a hashmap not an array!!
    int moduleSize; //total number of students in the module
    int[] globalTimetable; //all students' timetables added together as array of length 40
    int[][] adjacencyMatrix;

    public ScheduleCalculator(int moduleSize, ArrayList<int[]> timetables) {
        //initialise moduleSize and timetables as given
        //fill U with all natural numbers from 0 to moduleSize-1
        //call createTimeSlots() to initialise time slots
        //call createGlobalTimeTable() to initialise the global timetable
    }

    public void createTimeSlots() {
        /*
        initialises up to 40 empty HashSets (careful of the size limit!! automatically sets to 16 I think --> set the limit to be moduleSize)
        loops through each student timetable --> for each time they're available, add that student to that time set
        the student numbers are equivalent to their position in the schedules array
        the time numbers are equivalent to their position in the time set array
         */
    }

    public void createGlobalTimetable() {
        /*
        initialise empty globalTime
        loop through student timetable collection adding each one
         */
    }

    /**
     * delete a time
     * @param id the identifier of the time to be deleted
     */
    public void deleteTime(int id) {
        /*
        find the respective entry t in the array of time HashSets (HENCE WHY HASHMAP MAY BE BETTER)
        t.deleteTime();
        reduce the degree of adjacent nodes by the weight of the edges between them and the deleted time t
        if relevant set the all entries in the ith column and row of the adjacency matrix to n (number of students)
        THIS MIGHT NOT MATTER AT ALL ACTUALLY ^^^
        */
    }

    /**
     * method to add all time slots together into temp variable T except for given t
     * @param t --> the timeslot to be added
     * @return --> the union
     */
    public HashSet<Integer> unionOfTimes(TimeSlot t) {
        /*
        create empty hashset union
        iterate over array of timeslots:
            union.addAll(i);
            UNLESS i==t --> skip over it
         */
        return null;
    }

    /**
     * constraint 1 (optionally run): delete certain times
     * @param times --> all time ids for the times to be blocked out
     */
    public void blockOutTimes(int[] times) {
        //for all t in times --> deleteTime(t)
    }

    /**
     * constraint 2 (optionally run): maximum number of students in a class
     * @param limit --> the maximum number of students
     */
    public void classSizeLimit(int limit) {
        /*
        sort the array of timeslots
        accessing from the back of the array repeat these steps until the accessed timeslot t has size < limit:
          obtain temp variable T of union of all other sets via unionOfTimes(t)
          iterate over entries a1, a2, ... an in the t hashset, if T.contains(ai) --> remove ai from t hashset and reduce the size
          stop loop once the size of t is equal to the limit
         */
    }

    /**
     * check base case 1: there is one or more times when everyone is available
     * COULD HAVE BETTER NAME
     * @return array of ids of times when everyone is available
     */
    public int[] baseCaseOne() {
        /*
        create empty array
        (stream filter?? only saying this as adrian is currently talking abt streams in programming)
        option 1: look through the global timetable, pick out entries equal to the number of students
             --> add each index to the array
        option 2: loop through all time sets, add the indices of each time set whose size is equal to the number of students
        return array
         */
        return null;
    }

    /**
     * create adjacency matrix (i.e. graph)
     * each edge of weight n between two timeslots t1 and t2 represents the number of students busy at both times
     */
    public void createAdjacencyMatrix() {
        /*
        set adjacencyMatrix to a 40x40 matrix with all entries = 0
        create complements t' of each t by creating t' = U.removeAll(t)
        ^^^ CREATE A SEPARATE METHOD FOR MAKING AN ARRAY OF ALL COMPLEMENTS??
        nested for loops, looping through each node for each node (don't compare nodes to themselves tho)
        check which t' have the same students --> one edge for each identical student --> increment the respective entry in the matrix
        */
    }

    /**
     * check base case 2: two (n) or more times when everyone is available at n-1 of the times
     * COULD HAVE A BETTER NAME
     * @return an array representing those times
     */
    public int[] baseCaseTwo() {
        /*
        createAdjacencyMatrix();
        make an empty array
        runs through the adjacency matrix, adds all nodes with an intersection of 0 to the array
        e.g. if adjacencyMatrix [a][b] = 0 --> add a, b
        (0 means no ppl busy at both times)
        return array
         */
        return null;
    }

    /**
     * actual scheduling algorithm
     * @return an array representing the times needed to cover all students
     */
    public int[] algorithm() {
        /*
        make an empty array
        for(n=0 up to n=39):
          sort the time slot array
          t = smallest timeslot
          add all time slots together into temp variable T except for t (unionOfTimes(t))
          check T.containsAll(t) --> if true delete t
          if false add t to the array
        return array
         */
        return null;
    }

}

