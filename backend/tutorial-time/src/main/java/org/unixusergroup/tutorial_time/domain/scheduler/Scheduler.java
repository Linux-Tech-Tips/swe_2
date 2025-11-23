/**
 * class representing the scheduler, has all necessary methods to create a scheduler instance and schedule tutorials
 * @author Cedar
 */

package org.unixusergroup.tutorial_time.domain.scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import jakarta.persistence.ElementCollection;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.unixusergroup.tutorial_time.application.exceptions.*;

public class Scheduler {
    /**universal set U which contains the numbers of all students*/
    private final HashSet<Integer> U;
    @ElementCollection
    @JdbcTypeCode(SqlTypes.JSON)
    private ArrayList<TimeSlot> timeSlots; //array of 40 timeslots
    private final ArrayList<int[]> studentTimetables; //collection of all students' timetables (each is an array of length 40)
    private ArrayList<int[]> taTimetables = new ArrayList<>(); //collection of TA timetables
    private final int moduleSize; //total number of students in the module
    private ArrayList<int[]> adjacencyMatrix;
    private boolean adjacencyMatrixSet;
    private int numberOfTimeSlots;

    /**
     * Constructor
     * @param studentTimetables the timetables of each student
     */
    public Scheduler(ArrayList<int[]> studentTimetables) {
        if(studentTimetables.isEmpty()) {
            throw new IllegalStudentTimetablesException( "At least one student timetable must be entered");
        }
        this.moduleSize = studentTimetables.size();
        this.studentTimetables = studentTimetables;
        U = new HashSet<>(moduleSize);
        for(int i=0; i<moduleSize; i++) {
            U.add(i);
        }
        adjacencyMatrixSet = false;
        this.numberOfTimeSlots = studentTimetables.getFirst().length;
        try {
            createTimeSlots();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * alternate constructor specifically for the case of minTA constraint --> provide the TA timetables
     * @param studentTimetables student timetables
     * @param taTimetables TA timetables
     */
    public Scheduler(ArrayList<int[]> studentTimetables, ArrayList<int[]> taTimetables) throws IllegalStudentTimetablesException{
        this(studentTimetables);
        if(taTimetables.isEmpty()) {
            throw new IllegalStudentTimetablesException("At least one TA must be registered for the module");
        }
        this.taTimetables = taTimetables;
    }

    //TODO check if there's students who are never available??

    public int getModuleSize() {
        return moduleSize;
    }

    public ArrayList<int[]> getStudentTimetables() {
        return studentTimetables;
    }

    public ArrayList<int[]> getTATimetables() {
        return taTimetables;
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
    public void createTimeSlots() throws RuntimeException {
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
                if(studentTimetables.get(j)[i]==1) {
                    try {
                        timeSlots.get(i).addStudent(j);
                    } catch (IllegalStudentAdditionException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        /*
        initialises numberOfTimeslots empty HashSets
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
                    throw new IllegalTimeDeletionException("There are students who are only available at " + convertToDate(id));
                }
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
        throw new IllegalTimeDeletionException("The given time does not exist");
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
    public void blockOutTimes(int[] times) {
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
            ArrayList<Integer> studentsToBeRemoved = new ArrayList<>();
            for(int a : timeSlots.get(i).getStudents()) {
                //remove elements that are represented in other timeslots
                if(unionOfOthers.contains(a)) {
                    try {
                        studentsToBeRemoved.add(a);
                    } catch (IllegalStudentRemovalException e) {
                        throw new RuntimeException(e);
                    }
                }
                //once it fits into the limit --> stop
                if((timeSlots.get(i).getSize()-studentsToBeRemoved.size())==limit) {
                    break;
                }
            }
            timeSlots.get(i).removeStudents(studentsToBeRemoved);
            if(timeSlots.get(i).getSize()>limit) {
                //TODO should it throw a warning instead of an exception? maybe lecturers want to force it??
                throw new IllegalConstraintsException( "This class size limit is not possible");
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

    /**
     * constraint for minimum number of TAs at each timeslot
     * it collates all TA timetables together and then deletes each time at which not enough TAs are available
     * @param minTAs the minimum number of TAs
     */
    public void minimumNumberOfTAs(int minTAs) throws RuntimeException{
        if(taTimetables.size()<minTAs) {
            throw new IllegalConstraintsException("The minimum number of TAs is above the number of TAs registered for this module");
        }
        int[] compiledTATimetables = new int[numberOfTimeSlots];

        //loop over all TA timetables, collect the number of TAs available at each time
        for(int i=0; i<taTimetables.size(); i++) {
            for(int j=0; j<numberOfTimeSlots; j++) {
                compiledTATimetables[j]+=taTimetables.get(i)[j];
            }
        }

        for(int i=0; i<numberOfTimeSlots; i++) {
            if(compiledTATimetables[i]<minTAs) {
                try {
                    deleteTime(i);
                } catch (IllegalArgumentException e) {
                    throw new IllegalConstraintsException("The TA availability does not line up with student availability for this constraint to be possible");
                }
            }
        }
    }

    /**
     * check base case 1: there is one or more times when everyone is available
     * @return arraylist of ids of times when everyone is available
     */
    public String checkBaseCaseOne() {
        //loop through all time sets, add the indices of each time set whose size is equal to the number of students to the returned array

        String times = "All students are available at each of the following times:\n";
        for(int i = 0; i< numberOfTimeSlots; i++) {
            if(timeSlots.get(i).getSize()==moduleSize) {
                times+=moduleSize + " students are available at " + convertToDate(timeSlots.get(i).getId());
            }
        }
        if(times.equals("All students are available at each of the following times:\n")) {
            times = "";
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
        for(int i = 0; i< timeSlots.size(); i++) {
            adjacencyMatrix.add(new int[timeSlots.size()]);
        }

        for(int i = 0; i< timeSlots.size(); i++) {
            for(int j = 0; j< timeSlots.size(); j++) {
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
     * @return an array representing those times
     */
    public String checkBaseCaseTwo() {
        createAdjacencyMatrix();
        ArrayList<String> times = new ArrayList<>();

        for(int i = 0; i< numberOfTimeSlots; i++) {
            for (int j = 0; j < numberOfTimeSlots; j++) {
                if(adjacencyMatrix.get(i)[j]==0) {
                    times.add("The following two times cover all students:\n");
                    times.add(timeSlots.get(i).getSize() + " students are available at " + convertToDate(timeSlots.get(i).getId()));
                    times.add(timeSlots.get(j).getSize() + " students are available at " + convertToDate(timeSlots.get(j).getId()));
                }
            }
        }

        /*
        runs through the adjacency matrix, adds all nodes with an intersection of 0 to the times array
        e.g. if adjacencyMatrix [a][b] = 0 --> add a, b
        (0 means no ppl busy at both times)
         */
        return String.join("", times.subList(0, times.size()/2));
    }

    /**
     * actual scheduling algorithm
     * @return an arraylist including all the times needed to cover all students
     */
    public String schedule() {
        String times = "";
        HashSet<Integer> unionOfOthers;
        Collections.sort(timeSlots);
        for (TimeSlot temp : timeSlots) {
            unionOfOthers = unionOfTimes(temp);

            //if not all students in a given timeslot are represented in other timeslots --> add it to the returned times
            if (!(unionOfOthers.containsAll(temp.getStudents()))) {
                times += temp.getSize() + " students are available at " + convertToDate(temp.getId());
            }
        }
        return times;
    }

    /**
     * method for converting a time ID to a day/time
     * @return string representation of a time
     * t%5 = index of its day in the day array, t//5 = index of its time in the time array
     */
    public String convertToDate(int id) {
        String date = "";
        switch (id%5) {
            case 0:
                date+="Monday, ";
                break;
            case 1:
                date+="Tuesday, ";
                break;
            case 2:
                date+="Wednesday, ";
                break;
            case 3:
                date+="Thursday, ";
                break;
            case 4:
                date+="Friday, ";
                break;
        }
        int time = id/5;
        switch(time) {
            case 0:
                date+="9:00\n";
                break;
            case 1:
                date+="10:00\n";
                break;
            case 2:
                date+="11:00\n";
                break;
            case 3:
                date+="12:00\n";
                break;
            case 4:
                date+="13:00\n";
                break;
            case 5:
                date+="14:00\n";
                break;
            case 6:
                date+="15:00\n";
                break;
            case 7:
                date+="16:00\n";
                break;
        }
        return date;
    }

}