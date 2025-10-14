//highest level of the scheduler organisiation, this is what the api will actually call
package org.unixusergroup.tutorial_time.application.useCases;

import java.util.HashSet;

public class Scheduler {
    private SchedulerConstraints constraints; //need to create this class, it will consist of student limit, min TAs,  blocked off times
    private ScheduleCalculator scheduleCalculator; //is he passed in or is he created by the Scheduler?

    public Scheduler(SchedulerConstraints constraints, ScheduleCalculator scheduleCalculator) {
        this.constraints = constraints;
        this.scheduleCalculator = scheduleCalculator;
    }

    /**
     * method that schedules tutorials
     * @return a list of good times
     */
    public int[] schedule() {
        //if there are times to be blocked out --> remove those
        if(constraints.blockTimes()) {
            scheduleCalculator.blockOutTimes(constraints.getBlockedTimes());
        }

        //TODO: call TA constraints here!!

        if(constraints.studentLimit()) {
            scheduleCalculator.classSizeLimit(constraints.getStudentLimit());
        }


        //if base case 1 returns any times --> return those
        int[] times = scheduleCalculator.checkBaseCaseOne();
        if(times!=[]) {
            return times;
        }

        //if base case 2 returns any times --> return those
        times = scheduleCalculator.checkBaseCaseTwo();
        if(times!=[]) {
            return times;
        }

        //call the actual scheduling algorithm
        return scheduleCalculator.schedule();


    }
}
