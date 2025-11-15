/**
 * @author Cedar
 * test class for Student, this includes all tests for the abstract User class
 */

package org.unixusergroup.tutorial_time.domain.student;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.unixusergroup.tutorial_time.domain.timetable.Availability;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StudentTest {
    String name = "Johnny Test Jr";
    String email = "j.test1@universityofgalway.ie";
    long id = 10000L;
    Timetable timetable = new Timetable();
    Student student = new Student(name, email, id, timetable);

    /**
     * test the constructor with no parameters
     */
    @Test
    public void testEmptyConstructor() {
        Student student = new Student();
        assertNull(student.getName());
        assertEquals(0, student.getUniversityId());
        assertNull(student.getUniversityEmail());
        assertNull(student.getTimetable());
    }

    /**
     * test the constructor without a given timetable
     */
    @Test
    public void testConstructorNoTimetable() {
        Student student = new Student(name, email, id);
        assertEquals(name, student.getName());
        assertEquals(email, student.getUniversityEmail());
        assertEquals(id, student.getUniversityId());
        assertEquals(timetable, student.getTimetable());
    }

    /**
     * test the constructor with all parameters
     */
    @Test
    public void testFullConstructor() {
        timetable.setAvailability(0,0, Availability.AVAILABLE);
        Student student = new Student(name, "j.test1@nuigalway.ie", id, timetable);
        assertEquals(name, student.getName());
        assertEquals("j.test1@nuigalway.ie", student.getUniversityEmail());
        assertEquals(id, student.getUniversityId());
        assertEquals(timetable, student.getTimetable());
    }

    /**
     * test inputting an empty name string
     */
    @Test
    public void testInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> new Student("", email, id, timetable));
    }

    /**
     * test putting in a non-university email address
     */
    @Test
    public void testInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> new Student(name, "johnTest@gmail.com", id, timetable));
    }

    /**
     * test putting in a non-positive ID
     */
    @Test
    public void testInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> new Student(name, email, -11L, timetable));
    }

    /**
     * test the toString method
     */
    @Test
    public void testToString() {
        assertEquals("class org.unixusergroup.tutorial_time.domain.student.Student[name='Johnny Test Jr', email='j.test1@universityofgalway.ie', id='10000']", student.toString());
    }

    /**
     * test that two students of the same ID are equal
     */
    @Test
    public void testEquals() {
        Student student2 = new Student("Bobby Test", "b.test2@nuigalway.ie",  10000L);
        assertEquals(student, student2);
    }

    /**
     * test that two students of different IDs are not equal
     */
    @Test
    public void testNotEquals() {
        Student student2 = new Student(name, email,  20000L);
        assertNotEquals(student, student2);
    }

    /**
     * test setting a given date/time to be available
     */
    @Test
    public void testSetAvailable() {
        student.setAvailable(0,0);
        timetable.setAvailability(0,0,Availability.AVAILABLE);
        assertEquals(student.getTimetable(), timetable);
    }

    /**
     * test setting an invalid date/time to be available
     */
    @Test
    public void testInvalidSetAvailable() {
        assertThrows(IllegalArgumentException.class, () -> student.setAvailable(-1, 10));
    }

    /**
     * test setting a given date/time to be unavailable
     */
    @Test
    public void testSetUnavailable() {
        student.setUnavailable(0,0);
        timetable.setAvailability(0,0,Availability.UNAVAILABLE);
        assertEquals(student.getTimetable(), timetable);
    }

    /**
     * test setting an invalid date/time to be unavailable
     */
    @Test
    public void testInvalidSetUnavailable() {
        assertThrows(IllegalArgumentException.class, () -> student.setAvailable(1, 10));
    }
}
