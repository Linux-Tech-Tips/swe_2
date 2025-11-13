/**
 * class that instantiates a scheduler object and calls the relevant methods to return tutorial times
 * @author Cedar
 */
package org.unixusergroup.tutorial_time.application.scheduler;

import java.util.ArrayList;

import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.exceptions.IllegalConstraintsException;
import org.unixusergroup.tutorial_time.application.exceptions.IllegalTimeDeletionException;
import org.unixusergroup.tutorial_time.application.scheduler.dto.ScheduleRequest;
import org.unixusergroup.tutorial_time.application.scheduler.dto.ScheduleResponse;
import org.unixusergroup.tutorial_time.domain.scheduler.*;

public class ScheduleTutorialsUseCase implements UseCase <ScheduleResponse, ScheduleRequest> {
    /**
     * constructor
     * all data needed is in the request so no data passed into the constructor
     */
    public ScheduleTutorialsUseCase(){}

    /**
     * method that schedules tutorials
     * first it calls the constraints (if necessary)
     * then all scheduling methods
     * @return ScheduleResponse object
     */
    public ScheduleResponse execute(ScheduleRequest request) throws RuntimeException{
        Constraints constraints = request.constraints();
        Scheduler scheduler = request.scheduler();

        //if there is a minimum number of TAs to be at each tutorial --> remove times as needed
        if(constraints.minTAs()) {
            try{
                scheduler.minimumNumberOfTAs(request.minTAs());
            } catch (IllegalConstraintsException e) {
                throw new RuntimeException(e);
            }
        }

        //if there are times to be blocked out --> remove those
        if(constraints.blockedTimes()) {
            try {
                scheduler.blockOutTimes(constraints.getBlockedTimes());
            } catch (IllegalTimeDeletionException e) {
                throw new RuntimeException(e);
            }
        }

        //if there is a limited number of students per tutorial --> edit the data as needed
        if(constraints.studentLimit()) {
            try {
                scheduler.classSizeLimit(constraints.getStudentLimit());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            }
        }

        //if base case 1 returns any times --> return those
        String times = scheduler.checkBaseCaseOne();
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
