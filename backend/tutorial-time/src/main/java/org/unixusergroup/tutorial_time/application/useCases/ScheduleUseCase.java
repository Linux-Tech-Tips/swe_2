//highest level of the scheduler organisation, this is what the api will actually call
package org.unixusergroup.tutorial_time.application.useCases;

import java.util.ArrayList;
import java.util.HashSet;
import org.unixusergroup.tutorial_time.domain.scheduler.*;

public class ScheduleUseCase {
    private Constraints constraints; //need to create this class, it will consist of student limit, min TAs,  blocked off times
    private ScheduleCalculator scheduleCalculator; //is he passed in or is he created by the Scheduler?

    public ScheduleUseCase(Constraints constraints, ScheduleCalculator scheduleCalculator) {
        this.constraints = constraints;
        this.scheduleCalculator = scheduleCalculator;
    }

    /**
     * method that schedules tutorials
     * @return a list of good times
     */
    public ArrayList<Integer> schedule() {
        //if there are times to be blocked out --> remove those
        if(constraints.blockedTimes()) {
            scheduleCalculator.blockOutTimes(constraints.getBlockedTimes());
        }

        //TODO: call TA constraints here!!

        if(constraints.studentLimit()) {
            scheduleCalculator.classSizeLimit(constraints.getStudentLimit());
        }

        //if base case 1 returns any times --> return those
        ArrayList<Integer> times = scheduleCalculator.checkBaseCaseOne();
        if(!times.isEmpty()) {
            return times;
        }

        //if base case 2 returns any times --> return those
        times = scheduleCalculator.checkBaseCaseTwo();
        if(!times.isEmpty()) {
            return times;
        }

        //call the actual scheduling algorithm
        return scheduleCalculator.schedule();
    }
}
