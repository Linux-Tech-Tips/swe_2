/**
 * @author Cedar
 * rest controller for the scheduler
 */

package org.unixusergroup.tutorial_time.api.rest;

import org.springframework.http.ResponseEntity;
import org.unixusergroup.tutorial_time.application.scheduler.ScheduleTutorialsUseCase;
import org.springframework.web.bind.annotation.*;
import org.unixusergroup.tutorial_time.application.scheduler.dto.ScheduleRequest;
import org.unixusergroup.tutorial_time.application.scheduler.dto.ScheduleResponse;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;

import java.util.ArrayList;

@RestController
@RequestMapping(path="/api/scheduler")

public class SchedulerController {

    private final ScheduleTutorialsUseCase scheduleTutorialsUseCase;
    private final ModuleRepository moduleRepository;

    /**
     * constructor, create a new scheduleTutorialsUseCase instance
     */
    public SchedulerController(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
        this.scheduleTutorialsUseCase = new ScheduleTutorialsUseCase();
    }

    /**
     * post API for scheduling tutorials
     * even when the lecturer doesn't specify constraints, the frontend sets them to defaults
     * @param moduleCode the code of the module to be scheduled
     * @param studentLimit maximum number of students per tutorial
     * @param minTAs minimum number of TAs per tutorial
     * @param blockedTimes arraylist of times that should not be considered
     * @return ScheduleResponse object containing the best times
     */
    @PostMapping
    public ResponseEntity<ScheduleResponse> schedule(@RequestParam String moduleCode, @RequestParam int studentLimit, @RequestParam int minTAs, @RequestParam ArrayList<Integer> blockedTimes) {
        try {
            ScheduleRequest request = new ScheduleRequest(moduleCode, studentLimit, minTAs, blockedTimes, moduleRepository);
            return ResponseEntity.ok(scheduleTutorialsUseCase.execute(request));
        }
        catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}