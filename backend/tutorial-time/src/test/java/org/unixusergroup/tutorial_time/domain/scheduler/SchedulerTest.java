/**
 * @author Cedar
 * test class for the scheduler
 */

package org.unixusergroup.tutorial_time.domain.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SchedulerTest {
    static int[] testTimeTable1 = {1,0,0,0};
    static int[] testTimeTable2 = {1,0,0,0};
    static int[] testTimeTable3 = {0,0,0,1};
    static int[] testTimeTable4 = {0,0,1,0};
    static int[] taTimeTable1 = {1,0,1,1};
    static int[] taTimeTable2 = {0,1,1,1};
    static ArrayList<int[]> timetables;
    static Scheduler testScheduler;
    static ArrayList<TimeSlot> testTimes;

    /**
     * set up test variables
     */
    @BeforeEach
    public void setVariables() {
        timetables = new ArrayList<>();
        timetables.add(testTimeTable1);
        timetables.add(testTimeTable2);
        timetables.add(testTimeTable3);
        timetables.add(testTimeTable4);
        testScheduler = new Scheduler(timetables);
        testTimes = new ArrayList<>();
        testTimes.add(new TimeSlot(0,4));
        testTimes.get(0).addStudent(0);
        testTimes.get(0).addStudent(1);
        testTimes.add(new TimeSlot(1,4));
        testTimes.add(new TimeSlot(2,4));
        testTimes.get(2).addStudent(3);
        testTimes.add(new TimeSlot(3,4));
        testTimes.get(3).addStudent(2);
    }

    /**
     * test the constructor
     * createTimeSlots is tested elsewhere and thus not tested here
     */
    @Test
    public void testConstructor() {
       HashSet<Integer> universe = new HashSet<>(4);
        for(int i=0; i<4; i++) {
            universe.add(i);
        }

        assertEquals(4, testScheduler.getModuleSize());
        assertEquals(timetables, testScheduler.getStudentTimetables());
        assertEquals(universe, testScheduler.getUniverse());
        assertFalse(testScheduler.getAdjacencyMatrixSet());
        assertEquals(4, testScheduler.getNumberOfTimeSlots());
    }

    /**
     * test that the constructor throws an exception when given an empty list of student timetables
     */
    @Test
    public void testConstructorEmptyTimetables() {
        ArrayList<int[]> emptyTimetables = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> new Scheduler(emptyTimetables));
    }

    /**
     * test the constructor with the additional TA timetables parameter
     */
    @Test
    public void testTAConstructor() {
        ArrayList<int[]> taTimetables = new ArrayList<>();
        taTimetables.add(taTimeTable1);
        taTimetables.add(taTimeTable2);

        testScheduler = new Scheduler(timetables, taTimetables);
        assertEquals(taTimetables, testScheduler.getTATimetables());
    }

    /**
     * test that the constructor throws an exception when given an empty list of TA timetables
     */
    @Test
    public void testConstructorEmptyTATimetables() {
        ArrayList<int[]> emptyTimetables = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> new Scheduler(timetables, emptyTimetables));
    }

    /**
     * test creating the timetables
     */
    @Test
    public void testCreateTimeSlots() {
        assertEquals(testTimes, testScheduler.getTimeSlots());
    }

    /**
     * test deleting a time
     */
    @Test
    public void testDeleteTime() {
        testTimes.remove(1);
        testScheduler.deleteTime(1);
        assertEquals(testTimes, testScheduler.getTimeSlots());
    }

    /**
     * test deleting a time when that's the only time a student is available
     * scheduler should throw an exception when one tries to delete the only time a student is available
     */
    @Test
    public void testDeleteTimeStudentOnlyFreeTime(){
        assertThrows(IllegalArgumentException.class, () -> testScheduler.deleteTime(2));
    }

    /**
     * test deleting a time that doesn't exist
     */
    @Test
    public void testDeleteTimeThatDoesNotExist() {
        assertThrows(IllegalArgumentException.class, () -> testScheduler.deleteTime(5));
    }

    /**
     * test creating a union of all timeslots except a given timeslot t
     */
    @Test
    public void testUnionOfOtherTimes() {
        HashSet<Integer> testUnion = new HashSet<>(4);
        testUnion.addAll(testTimes.get(0).getStudents());
        testUnion.addAll(testTimes.get(1).getStudents());
        testUnion.addAll(testTimes.get(2).getStudents());
        assertEquals(testUnion, testScheduler.unionOfTimes(testTimes.get(3)));
    }

    /**
     * test the constraint of a limit on tutorial size
     */
    @Test
    public void testClassSizeLimit() {
        int[] testTimeTable1 = {1,1,0,0};
        int[] testTimeTable2 = {1,0,0,0};
        int[] testTimeTable3 = {0,0,0,1};
        int[] testTimeTable4 = {0,0,1,0};
        ArrayList<int[]> timetables = new ArrayList<>();
        timetables.add(testTimeTable1);
        timetables.add(testTimeTable2);
        timetables.add(testTimeTable3);
        timetables.add(testTimeTable4);
        ArrayList<TimeSlot> testTimes = new ArrayList<>();
        testTimes.add(new TimeSlot(0,4));
        testTimes.get(0).addStudent(0);
        testTimes.get(0).addStudent(1);
        testTimes.add(new TimeSlot(1,4));
        testTimes.get(1).addStudent(0);
        testTimes.add(new TimeSlot(2,4));
        testTimes.get(2).addStudent(3);
        testTimes.add(new TimeSlot(3,4));
        testTimes.get(3).addStudent(2);
        Collections.sort(testTimes);
        testTimes.get(3).removeStudent(0);

        Scheduler testScheduler = new Scheduler(timetables);
        testScheduler.classSizeLimit(1);

        assertEquals(testTimes.get(0), testScheduler.getTimeSlots().get(0));
        assertEquals(testTimes.get(1), testScheduler.getTimeSlots().get(1));
        assertEquals(testTimes.get(2), testScheduler.getTimeSlots().get(2));
        assertEquals(testTimes.get(3), testScheduler.getTimeSlots().get(3));
    }

    /**
     * test that the scheduler throws an error when given a limit that can't be done while representing all students
     */
    @Test
    public void testImpossibleClassSizeLimit() {
        assertThrows(IllegalArgumentException.class, () -> testScheduler.classSizeLimit(1));
    }

    /**
     * test the block out times constraint
     */
    @Test
    public void testBlockOutTimes(){
        ArrayList<Integer> blockedTimes = new ArrayList<>();
        blockedTimes.add(1);
        testScheduler.blockOutTimes(blockedTimes);
        testTimes.remove(1);
        assertEquals(testTimes, testScheduler.getTimeSlots());
    }

    /**
     * test that giving a TA limit removes all times at which not enough TAs are available
     */
    @Test
    public void testTaLimit() {
        ArrayList<int[]> taTimeTables = new ArrayList<>();
        taTimeTables.add(taTimeTable1);

        Scheduler testScheduler = new Scheduler(timetables, taTimeTables);
        testScheduler.minimumNumberOfTAs(1);
        testTimes.remove(1);

        assertEquals(testTimes, testScheduler.getTimeSlots());
    }

    /**
     * test that the TA method properly catches and handles the possible exception thrown by deleteTime()
     */
    @Test
    public void testImpossibleTaLimit() {
        ArrayList<int[]> taTimeTables = new ArrayList<>();
        taTimeTables.add(taTimeTable2);

        Scheduler testScheduler = new Scheduler(timetables, taTimeTables);

        assertThrows(RuntimeException.class, () -> testScheduler.minimumNumberOfTAs(1));
    }

    /**
     * test that the TA method catches and handles there being less TAs than the limit given
     */
    @Test
    public void testNotEnoughTAs() {
        ArrayList<int[]> taTimeTables = new ArrayList<>();
        taTimeTables.add(taTimeTable2);

        Scheduler testScheduler = new Scheduler(timetables, taTimeTables);

        assertThrows(IllegalArgumentException.class, () -> testScheduler.minimumNumberOfTAs(2));
    }
    /**
     * test base case one when unsuccessful
     */
    @Test
    public void testBaseCaseOneUnsuccessful() {
        assertTrue(testScheduler.checkBaseCaseOne().isEmpty());
    }

    /**
     * test base case one when successful
     */
    @Test
    public void testBaseCaseOneSuccessful() {
        ArrayList<int[]> testTimetables2 = new ArrayList<>();
        testTimetables2.add(testTimeTable1);
        testTimetables2.add(testTimeTable2);
        Scheduler testScheduler2 = new Scheduler(testTimetables2);
        ArrayList<Integer> correctTime = new ArrayList<>();
        correctTime.add(0);
        assertEquals(correctTime, testScheduler2.checkBaseCaseOne());
    }

    /**
     * test that the adjacency matrix is created correctly
     */
    @Test
    public void testCreateAdjacencyMatrix() {
        //make a testAdjacencyMatrix --> check that it's the same as the generated adjacencyMatrix
        ArrayList<int[]> testAdjacencyMatrix = new ArrayList<>();

        testAdjacencyMatrix.add(new int[] {-1,2,1,1});
        testAdjacencyMatrix.add(new int[] {2,-1,3,3});
        testAdjacencyMatrix.add(new int[] {1,3,-1,2});
        testAdjacencyMatrix.add(new int[] {1,3,2,-1});

        testScheduler.createAdjacencyMatrix();
        ArrayList<int[]> schedulerAdjacencyMatrix = testScheduler.getAdjacencyMatrix();

        assertArrayEquals(testAdjacencyMatrix.get(0), schedulerAdjacencyMatrix.get(0));
        assertArrayEquals(testAdjacencyMatrix.get(1), schedulerAdjacencyMatrix.get(1));
        assertArrayEquals(testAdjacencyMatrix.get(2), schedulerAdjacencyMatrix.get(2));
        assertArrayEquals(testAdjacencyMatrix.get(3), schedulerAdjacencyMatrix.get(3));
    }

    /**
     * test base case 2 when unsuccessful
     */
    @Test
    public void testBaseCaseTwoUnsuccessful() {
        assertTrue(testScheduler.checkBaseCaseTwo().isEmpty());
    }

    /**
     * test base case 2 when successful
     */
    @Test
    public void testBaseCaseTwoSuccessful() {
        ArrayList<int[]> testTimetables2 = new ArrayList<>();
        testTimetables2.add(testTimeTable1);
        testTimetables2.add(testTimeTable2);
        testTimetables2.add(testTimeTable3);

        Scheduler testScheduler2 = new Scheduler(testTimetables2);
        ArrayList<Integer> correctTime = new ArrayList<>();
        correctTime.add(0);
        correctTime.add(3);

        assertEquals(correctTime, testScheduler2.checkBaseCaseTwo());
    }

    /**
     * test the scheduling algorithm
     */
    @Test
    public void testSchedule() {
        HashSet<Integer> correctTimes = new HashSet<>();
        correctTimes.add(0);
        correctTimes.add(2);
        correctTimes.add(3);

        //this is to check that they contain the same elements, not necessarily in the same order
        assertEquals(correctTimes, new HashSet<>(testScheduler.schedule()));
    }

}
