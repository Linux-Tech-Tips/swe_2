/** 
 * @author Martin
 */
package org.unixusergroup.tutorial_time.domain.timetable;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class TimetableTest {

    /** Test case for the constructors */
    @Test
    void constructorTest() {
        /* Default constructor case */
        Timetable t1 = new Timetable();
        List<Availability> a1 = t1.getMatrix();
        assertEquals(t1.getAvailability(0, 0), a1.get(0));
        assertEquals(a1.size(), Timetable.DEFAULT_COLUMNS * Timetable.DEFAULT_ROWS);
        assertEquals(t1.getAvailability(0, 0), Timetable.DEFAULT_AVAILABILITY);

        /* Explicit values constructor case */
        Timetable t2 = new Timetable(5, 8, Availability.UNAVAILABLE);
        List<Availability> a2 = t2.getMatrix();
        assertEquals(a2.size(), 5 * 8);
        assertEquals(t2.getAvailability(0, 0), a2.get(0));
        assertEquals(t2.getAvailability(0, 0), Availability.UNAVAILABLE);

        /* Constructor error checking */
        assertThrows(IllegalArgumentException.class, () -> new Timetable(-5, 1, Timetable.DEFAULT_AVAILABILITY));
        assertThrows(IllegalArgumentException.class, () -> new Timetable(4, -1, Timetable.DEFAULT_AVAILABILITY));
        assertThrows(IllegalArgumentException.class, () -> new Timetable(-3, -2, Timetable.DEFAULT_AVAILABILITY));
        assertThrows(IllegalArgumentException.class, () -> new Timetable(2, 0, Timetable.DEFAULT_AVAILABILITY));
        assertThrows(IllegalArgumentException.class, () -> new Timetable(0, 1, Timetable.DEFAULT_AVAILABILITY));
        assertThrows(IllegalArgumentException.class, () -> new Timetable(0, 0, Timetable.DEFAULT_AVAILABILITY));
    }

    /** Test case for getArray and getMatrix */
    @Test
    void getArrayMatrixTest() {
        /* Creating and populating an array */
        Timetable t = new Timetable(5, 8, Availability.AVAILABLE);
        t.setAvailability(0, 0, Availability.UNAVAILABLE);
        t.setAvailability(1, 0, Availability.UNAVAILABLE);

        /* Testing the getMatrix function */
        List<Availability> matrix = t.getMatrix();
        assertEquals(matrix.size(), 5 * 8);
        assertEquals(matrix.get(0), Availability.UNAVAILABLE);
        assertEquals(matrix.get(1), Availability.UNAVAILABLE);
        for(int i = 2; i < matrix.size(); ++i) {
            assertEquals(matrix.get(i), Availability.AVAILABLE);
        }

        /* Testing the getArray function */
        int[] array = t.getArray();
        assertEquals(array.length, 5 * 8);
        assertEquals(array[0], 0);
        assertEquals(array[1], 0);
        for(int i = 2; i < array.length; ++i) {
            assertEquals(array[2], 1);
        }
    }

    /** Test case for getFlatIndex */
    @Test 
    void getFlatIndexTest() {
        Timetable t = new Timetable(5, 8, Timetable.DEFAULT_AVAILABILITY);

        assertEquals(t.getFlatIndex(0, 0), 0);
        assertEquals(t.getFlatIndex(1, 0), 1);
        assertEquals(t.getFlatIndex(0, 1), 5);
        assertEquals(t.getFlatIndex(4, 0), 4);
        assertEquals(t.getFlatIndex(0, 7), 35);
        assertEquals(t.getFlatIndex(4, 7), 39);
        assertThrows(IllegalArgumentException.class, () -> t.getFlatIndex(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> t.getFlatIndex(0, -1));
        assertThrows(IllegalArgumentException.class, () -> t.getFlatIndex(5, 0));
        assertThrows(IllegalArgumentException.class, () -> t.getFlatIndex(0, 8));
        assertThrows(IllegalArgumentException.class, () -> t.getFlatIndex(5, 8));
    }
}
