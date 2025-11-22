/**
 * @author Cedar
 * test class for Student
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
}
