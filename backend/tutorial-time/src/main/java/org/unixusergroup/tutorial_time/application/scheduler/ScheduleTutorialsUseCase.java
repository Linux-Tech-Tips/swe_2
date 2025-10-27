/**
 * class that instantiates a scheduler object and calls the relevant methods to return tutorial times
 * @author Cedar
 */
package org.unixusergroup.tutorial_time.application.scheduler;

import java.util.ArrayList;

import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.dto.ScheduleRequest;
import org.unixusergroup.tutorial_time.application.dto.ScheduleResponse;
import org.unixusergroup.tutorial_time.domain.scheduler.*;

public class ScheduleTutorialsUseCase implements UseCase <ScheduleResponse, ScheduleRequest> {
    /**
     * constructor
     * all data needed is in the request so no data passed into the constructor
     */
    public ScheduleTutorialsUseCase(){}

    /**
     * method that schedules tutorials
     * @return ScheduleResponse object
     */
    public ScheduleResponse execute(ScheduleRequest request) {
        Constraints constraints = request.constraints();
        Scheduler scheduler = request.scheduler();

        //if there are times to be blocked out --> remove those
        if(constraints.blockedTimes()) {
            try {
                scheduler.blockOutTimes(constraints.getBlockedTimes());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            }
        }

        //TODO: call TA constraints here!!

        //if there is a limited number of students per tutorial --> edit the data as needed
        if(constraints.studentLimit()) {
            try {
                scheduler.classSizeLimit(constraints.getStudentLimit());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            }
        }

        //if base case 1 returns any times --> return those
        ArrayList<Integer> times = scheduler.checkBaseCaseOne();
        if(!times.isEmpty()) {
            return new ScheduleResponse(times);
        }

        //if base case 2 returns any times --> return those
        times = scheduler.checkBaseCaseTwo();
        if(!times.isEmpty()) {
            return new ScheduleResponse(times);
        }

        //call the actual scheduling algorithm, return those values
        return new ScheduleResponse(scheduler.schedule());
    }
}
