/**
 * @author Cedar
 */

package org.unixusergroup.tutorial_time.domain.scheduler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.unixusergroup.tutorial_time.application.exceptions.IllegalConstraintsException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ConstraintsTest {
    private int[] blockedTimes = {};

    /**
     * testing the constructor
     */
    @Test
    public void testConstructor() {
        Constraints constraints = new Constraints(1,1,blockedTimes);
        assertEquals(1,constraints.getStudentLimit());
        Assertions.assertEquals(blockedTimes, constraints.getBlockedTimes());
        assertEquals(1, constraints.getMinTAs());
        assertTrue(constraints.studentLimit());
        assertFalse(constraints.blockedTimes());
        assertTrue(constraints.minTAs());
    }

    /**
     * test that only positive student limits are allowed
     */
    @Test
    public void testIllegalStudentLimit() {
        assertThrows(IllegalConstraintsException.class, () -> new Constraints(-1,0,blockedTimes));
    }

    /**
     * test that only positive TA minimums are allowed
     */
    @Test
    public void testIllegalMinTAs() {
        assertThrows(IllegalConstraintsException.class, () -> new Constraints(20,-1,blockedTimes));
    }
}
