package org.unixusergroup.tutorial_time;


import org.junit.jupiter.api.Test;
import org.unixusergroup.tutorial_time.application.useCases.ScheduleUseCase;
import org.unixusergroup.tutorial_time.domain.scheduler.Constraints;
import org.unixusergroup.tutorial_time.domain.scheduler.ScheduleCalculator;

import java.util.ArrayList;

public class schedulerTest {
    @Test
    public void schedulerTest() {
        ArrayList<int[]> timetables = new ArrayList<>();
        int[] tempTimeTable1 = {1,0,0,0,0,
                                0,0,0,0,0,
                                0,0,0,0,0,
                                0,0,0,0,0,
                                0,0,0,0,0,
                                0,0,0,0,0,
                                0,0,0,0,0,
                                0,0,0,0,0};
        int[] tempTimeTable2 = {1,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0};
        int[] tempTimeTable3 = {0,0,0,1,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0};
        int[] tempTimeTable4 = {0,0,1,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0,
                0,0,0,0,0};
        timetables.add(tempTimeTable1);
        timetables.add(tempTimeTable2);
        timetables.add(tempTimeTable3);
        timetables.add(tempTimeTable4);
        ScheduleCalculator scheduleCalculator = new ScheduleCalculator(4, timetables);
        Constraints constraints = new Constraints(0, 0, null);
        ScheduleUseCase scheduler = new ScheduleUseCase(constraints, scheduleCalculator);

        System.out.println("The optimal times are: " + scheduler.schedule());
    }
}
