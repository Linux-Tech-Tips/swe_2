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

/** 
 * The API controller exposing Scheduler Use Cases.
 *
 * The API calls for Scheduler Use Cases are mapped to '/api/scheduler/...'
 */
@RestController
@RequestMapping(path="/api/scheduler")
@CrossOrigin(origins=FRONTEND_DOMAIN_NAME)
public class SchedulerController {

    private final ScheduleTutorialsUseCase scheduleTutorialsUseCase;

    /** Constructor, requires a ModuleRepository and StudentRepository instance to get relevant data from the database */
    public SchedulerController(ModuleRepository moduleRepository, StudentRepository studentRepository) {
        this.scheduleTutorialsUseCase = new ScheduleTutorialsUseCase(moduleRepository, studentRepository);
    }

    /**
     * Schedule Tutorials API mapping (/api/scheduler/schedule).
     *
     * The post API for scheduling tutorials, finds optimal tutorial times based on the provided module data and constraints.
     * The constraints need to be populated for the API call, but can be set to defaults on the frontend side.
     * @param moduleCode the Module Code of the module to be scheduled
     * @param studentLimit the maximum number of students per tutorial, 0 means no limit
     * @param minTAs the minimum number of TAs per tutorial, 0 means no minimum
     * @param blockedTimes a list of times that should not be considered, specified as indices in the 40-element Availability array format
     * @return HTTP status 200 and a string in the format of "DAY,TIME\n(DAY2,TIME2\n...)" representing the ideal time(s),
     *         HTTP status of 400 and an error message when an error occurs due to incorrect user input,
     *         HTTP status of 500 and the error message when any other error occurs
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
