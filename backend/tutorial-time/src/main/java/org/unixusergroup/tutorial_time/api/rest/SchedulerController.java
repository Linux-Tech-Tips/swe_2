/**
 * @author Cedar
 * rest controller for the scheduler
 */

package org.unixusergroup.tutorial_time.api.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.unixusergroup.tutorial_time.application.scheduler.ScheduleTutorialsUseCase;
import org.springframework.web.bind.annotation.*;
import org.unixusergroup.tutorial_time.application.scheduler.dto.ScheduleRequest;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;

import static org.unixusergroup.tutorial_time.infrastructure.ProjectConfiguration.FRONTEND_DOMAIN_NAME;

@RestController
@RequestMapping(path="/api/scheduler")
@CrossOrigin(origins=FRONTEND_DOMAIN_NAME)
public class SchedulerController {

    private final ScheduleTutorialsUseCase scheduleTutorialsUseCase;

    /**
     * constructor, create a new scheduleTutorialsUseCase instance
     */
    public SchedulerController(ModuleRepository moduleRepository, StudentRepository studentRepository) {
        this.scheduleTutorialsUseCase = new ScheduleTutorialsUseCase(moduleRepository, studentRepository);
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
    @PostMapping(path="/schedule")
    public ResponseEntity<?> schedule(@RequestParam String moduleCode, @RequestParam int studentLimit, @RequestParam int minTAs, @RequestParam int[] blockedTimes) {
        try {
            ScheduleRequest request = new ScheduleRequest(moduleCode, studentLimit, minTAs, blockedTimes);
            return ResponseEntity.status(HttpStatus.OK).body(scheduleTutorialsUseCase.execute(request));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}