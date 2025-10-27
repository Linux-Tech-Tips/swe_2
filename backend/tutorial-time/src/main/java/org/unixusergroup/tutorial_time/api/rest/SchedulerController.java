package org.unixusergroup.tutorial_time.api.rest;

import org.springframework.http.ResponseEntity;
import org.unixusergroup.tutorial_time.application.dto.ScheduleRequest;
import org.unixusergroup.tutorial_time.application.dto.ScheduleResponse;
import org.unixusergroup.tutorial_time.application.scheduler.ScheduleTutorialsUseCase;
import org.springframework.web.bind.annotation.*;
/*
@RestController
@RequestMapping(path="/api/scheduler")
//TODO: currently commented out due to bean error "Unsatisfied dependency expressed through constructor parameter 0: No qualifying bean of type 'org.unixusergroup.tutorial_time.application.useCases.ScheduleUseCase' available: expected at least 1 bean which qualifies as autowire candidate"
   other error: "Parameter 0 of constructor in org.unixusergroup.tutorial_time.api.rest.SchedulerController required a bean of type 'org.unixusergroup.tutorial_time.application.scheduler.ScheduleTutorialsUseCase' that could not be found."

public class SchedulerController {

    private final ScheduleTutorialsUseCase scheduleTutorialsUseCase;

    public SchedulerController(ScheduleTutorialsUseCase scheduleTutorialsUseCase) {
        this.scheduleTutorialsUseCase = scheduleTutorialsUseCase;
    }
    //separate APIs in this file for constraints
    //TODO: 3 separate APIs but they all edit the same Constraints object which is then passed into the ScheduleRequest object
    // --> does that mean Constraints should be an entity?? bc it is changeable??

    //call SchedulerUseCase for the schedule API, parameter constraints + module reference/timetables
    //TODO: am still confused where it's getting the actual data TT
    // where is the ScheduleRequest being crated? where is the scheduler inside the request being created?
    // where is that scheduler getting the timetables from?? --> input should include the module code
    @PostMapping
    public ResponseEntity<ScheduleResponse> schedule(@RequestBody ScheduleRequest request) {
        try {
            return ResponseEntity.ok(scheduleTutorialsUseCase.execute(request));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

}
*/
