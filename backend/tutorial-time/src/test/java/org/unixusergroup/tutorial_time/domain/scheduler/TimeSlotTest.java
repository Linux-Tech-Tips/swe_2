/**
 * @author Cedar
 * test class for TimeSlot
 */

package org.unixusergroup.tutorial_time.domain.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import static org.junit.jupiter.api.Assertions.*;
import org.unixusergroup.tutorial_time.application.exceptions.IllegalModuleSizeException;
import org.unixusergroup.tutorial_time.application.exceptions.IllegalStudentAdditionException;
import org.unixusergroup.tutorial_time.application.exceptions.IllegalStudentRemovalException;

@SpringBootTest
public class TimeSlotTest {
    private final TimeSlot testTimeSlot = new TimeSlot(0, 4);
    private final TimeSlot otherTimeSlot = new TimeSlot(1, 4);
    private final TimeSlot identicalTimeSlot = new TimeSlot(0, 4);
    private final HashSet<Integer> testStudents = new HashSet<>();

    /**
     * test for the constructor
     */
    @Test
    public void testConstructor() {
        TimeSlot timeSlot = new TimeSlot(1,2);
        assertEquals(1, timeSlot.getId());
        assertEquals(0, timeSlot.getSize());
        assertEquals(0, timeSlot.getDegree());
        assertEquals(new HashSet<>(2), timeSlot.getStudents());
    }

    /**
     * test that the constructor throws an exception for an illegal module size
     */
    @Test
    public void testConstructorInvalidModuleSize() {
        assertThrows(IllegalModuleSizeException.class, () -> new TimeSlot(1,-3));
    }

    /**
     * test the equals method for two equal timeSlots
     */
    @Test
    public void testSuccessfulEquals() {
        assertEquals(testTimeSlot, identicalTimeSlot);
    }

    /**
     * test the equals method for the same object
     */

    @Test
    public void testEqualsSameObject() {
        assertEquals(testTimeSlot, testTimeSlot);
    }

    /**
     * test equals with different class
     */

    @Test
    public void testEqualsDifferentClasses() {
        assertNotEquals(new ArrayList<String>(), testTimeSlot);
    }
    /**
     * test the equals method for two different timeSlots
     */
    @Test
    public void testUnsuccessfulEquals() {
        assertNotEquals(testTimeSlot, otherTimeSlot);
    }

    /**
     * test the comparable method for TimeSlots of different sizes
     * we want the timeslot with the smaller size to be first in the ordering
     */
    @Test
    public void testComparableDifferentSizes() {
        testTimeSlot.addStudent(1);
        TimeSlot[] testArray = {testTimeSlot, otherTimeSlot};
        TimeSlot[] correctOrder = {otherTimeSlot, testTimeSlot};
        Arrays.sort(testArray);
        assertArrayEquals(testArray, correctOrder);
    }

    /**
     * test the comparable method for TimeSlots of equal sizes
     * we want the timeslot with the larger degree to be first in the ordering
     */
    @Test
    public void testComparableSameSizes() {
        testTimeSlot.increaseDegree(2);
        TimeSlot[] testArray = {otherTimeSlot, testTimeSlot};
        TimeSlot[] correctOrder = {testTimeSlot, otherTimeSlot};
        Arrays.sort(testArray);
        assertArrayEquals(testArray, correctOrder);
    }


    /**
     * test adding a student to a timeset
     */
    @Test
    public void testAddStudent() {
        testStudents.add(1);
        testTimeSlot.addStudent(1);
        assertEquals(1, testTimeSlot.getSize());
        assertEquals(testStudents, testTimeSlot.getStudents());
    }

    /**
     * test adding a student to a full timeslot --> should throw an exception,
     */
    @Test
    public void testAddStudentWhenFull() {
        TimeSlot testTimeSlot = new TimeSlot(0, 1);
        testTimeSlot.addStudent(0);
        int prevSize = testTimeSlot.getSize();

        //now add a second student --> module is full!! should throw an exception
        assertThrows(IllegalStudentAdditionException.class, () -> testTimeSlot.addStudent(4));
        assertEquals(prevSize, testTimeSlot.getSize());
    }

    /**
     * test adding a student who already exists --> should throw an exception
     */
    @Test
    public void testAddStudentWhoAlreadyExists() {
        TimeSlot testTimeSlot = new TimeSlot(0, 1);
        testTimeSlot.addStudent(0);
        int prevSize = testTimeSlot.getSize();

        assertThrows(IllegalStudentAdditionException.class, () -> testTimeSlot.addStudent(0));
        assertEquals(prevSize, testTimeSlot.getSize());
    }

    /**
     * test removing a student
     */
    @Test
    public void testRemoveStudent() {
        testTimeSlot.addStudent(1);
        testTimeSlot.removeStudent(1);
        assertEquals(0, testTimeSlot.getSize());
        assertEquals(testStudents, testTimeSlot.getStudents());
    }

    /**
     * test removing a student when there are no students
     */
    @Test
    public void testRemoveStudentWhenEmpty() {
        int prevSize = testTimeSlot.getSize();
        assertThrows(IllegalStudentRemovalException.class, () -> testTimeSlot.removeStudent(1));
        assertEquals(prevSize, testTimeSlot.getSize());
    }

    /**
     * test removing a student who doesn't exist
     */
    @Test
    public void testRemoveStudentWhoDoesNotExist() {
        testTimeSlot.addStudent(0);
        int prevSize = testTimeSlot.getSize();
        assertThrows(IllegalStudentRemovalException.class, () -> testTimeSlot.removeStudent(1));
        assertEquals(prevSize, testTimeSlot.getSize());
    }

}
