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
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
import org.unixusergroup.tutorial_time.domain.timetable.Availability;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;

import static org.unixusergroup.tutorial_time.infrastructure.ProjectConfiguration.FRONTEND_DOMAIN_NAME;

@RestController
@RequestMapping(path="/api/scheduler")
@CrossOrigin(origins=FRONTEND_DOMAIN_NAME)
public class SchedulerController {

    private final ScheduleTutorialsUseCase scheduleTutorialsUseCase;
    private final ModuleRepository moduleRepository;
    private final StudentRepository studentRepository;

    /**
     * constructor, create a new scheduleTutorialsUseCase instance
     */
    public SchedulerController(ModuleRepository moduleRepository, StudentRepository studentRepository) {
        this.moduleRepository = moduleRepository;
        this.studentRepository = studentRepository;
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
    @PostMapping(path="/schedule")
    @CrossOrigin(origins=FRONTEND_DOMAIN_NAME)
    public ResponseEntity<?> schedule(@RequestParam String moduleCode, @RequestParam int studentLimit, @RequestParam int minTAs, @RequestParam int[] blockedTimes) {
        Lecturer testLecturer = new Lecturer("Professor Test", "test@universityofgalway.ie", 00000);
        Module testModule = new Module("testModule", testLecturer.getUniversityId());
        Student student1 = new Student("Student1", "s1@university.ie", 1111, new Timetable(2, 2, Availability.UNAVAILABLE));
        student1.setAvailable(0,0);
        studentRepository.saveStudent(student1);
        testModule.addStudent(student1.getUniversityId());
        Student student2 = new Student("Student2", "s2@university.ie", 2222, new Timetable(2, 2, Availability.UNAVAILABLE));
        student2.setAvailable(0,0);
        studentRepository.saveStudent(student2);
        testModule.addStudent(student2.getUniversityId());
        Student student3 = new Student("Student3", "s3@university.ie", 3333, new Timetable(2, 2, Availability.UNAVAILABLE));
        student3.setAvailable(1,1);
        studentRepository.saveStudent(student3);
        testModule.addStudent(student3.getUniversityId());
        Student student4 = new Student("Student4", "s4@university.ie", 4444, new Timetable(2, 2, Availability.UNAVAILABLE));
        student4.setAvailable(0,1);
        studentRepository.saveStudent(student4);
        testModule.addStudent(student4.getUniversityId());
        moduleRepository.saveModule(testModule);
        try {
            ScheduleRequest request = new ScheduleRequest(moduleCode, studentLimit, minTAs, blockedTimes, moduleRepository, studentRepository);
            return ResponseEntity.status(HttpStatus.OK).body(scheduleTutorialsUseCase.execute(request));
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}