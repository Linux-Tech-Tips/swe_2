/**
 * @author
 * test class for the Lecturer class
 */

package org.unixusergroup.tutorial_time.domain.lecturer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.unixusergroup.tutorial_time.domain.timetable.Availability;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class LecturerTest {
    String email = "johnny.test@universityofgalway.ie";
    long id = 10000L;
    Timetable timetable = new Timetable();

    /**
     * test the constructor with no parameters
     */
    @Test
    public void testEmptyConstructor() {
        Lecturer lecturer = new Lecturer();
        assertEquals(0, lecturer.getUniversityId());
        assertNull(lecturer.getUniversityEmail());
        assertNull(lecturer.getTimetable());
    }

    /**
     * test the constructor without a given timetable
     */
    @Test
    public void testConstructorNoTimetable() {
        Lecturer lecturer = new Lecturer(email, id);
        assertEquals(email, lecturer.getUniversityEmail());
        assertEquals(id, lecturer.getUniversityId());
        assertEquals(timetable, lecturer.getTimetable());
    }


    /**
     * test the constructor with all parameters
     */
    @Test
    public void testFullConstructor() {
        timetable.setAvailability(0,0, Availability.AVAILABLE);
        Lecturer lecturer = new Lecturer("johnny.test@nuigalway.ie", id, timetable);
        assertEquals("johnny.test@nuigalway.ie", lecturer.getUniversityEmail());
        assertEquals(id, lecturer.getUniversityId());
        assertEquals(timetable, lecturer.getTimetable());
    }
}
