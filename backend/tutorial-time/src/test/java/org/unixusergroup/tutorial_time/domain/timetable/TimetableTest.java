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

    @Test
    void defaultConstructorTest() {
        Timetable t = new Timetable();
        List<Availability> a = t.getMatrix();
        assertEquals(a.size(), Timetable.DEFAULT_COLUMNS * Timetable.DEFAULT_ROWS);
        assertEquals(t.getAvailability(0, 0), Timetable.DEFAULT_AVAILABILITY);
    }

}
