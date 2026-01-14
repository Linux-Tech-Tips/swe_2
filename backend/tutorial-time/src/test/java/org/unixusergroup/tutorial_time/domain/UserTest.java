/**
 * @author cedar
 */

package org.unixusergroup.tutorial_time.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.timetable.Availability;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserTest {
    String name = "Johnny Test Jr";
    String email = "j.test1@universityofgalway.ie";
    long id = 10000L;
    Timetable timetable = new Timetable();
    Student student = new Student(name, email, id, timetable);

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
     * test capitalising the uni email in various ways
     */
    @Test
    public void testUppercaseEmail() {
        assertDoesNotThrow(() -> new Student(name, "j.test1@UniversityOfGalway.ie", id, timetable));
        assertDoesNotThrow(() -> new Lecturer(name, "j.test1@UniversityOfGalway.ie", id, timetable));
        assertDoesNotThrow(() -> new Student(name, "j.test1@UNIVERSITYOFGALWAY.IE", id, timetable));
        assertDoesNotThrow(() -> new Student(name, "j.test1@universityOfGalway.ie", id, timetable));
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
        timetable.setAvailability(0,0, Availability.AVAILABLE);
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
