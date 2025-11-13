/**
 * @author Ethan
 */

package org.unixusergroup.tutorial_time.api.rest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unixusergroup.tutorial_time.application.module.CreateModuleUseCase;
import org.unixusergroup.tutorial_time.application.module.GetModuleUseCase;
import org.unixusergroup.tutorial_time.application.module.dto.CreateModuleRequest;
import org.unixusergroup.tutorial_time.application.module.dto.CreateModuleResponse;
import org.unixusergroup.tutorial_time.application.module.dto.GetModuleRequest;
import org.unixusergroup.tutorial_time.application.module.dto.GetModuleResponse;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
import org.unixusergroup.tutorial_time.domain.timetable.Availability;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;

import java.util.List;

import static org.unixusergroup.tutorial_time.infrastructure.ProjectConfiguration.FRONTEND_DOMAIN_NAME;

@RestController
@RequestMapping(path="/api/module")
@CrossOrigin(origins=FRONTEND_DOMAIN_NAME)
/** The API controller exposing the Module use cases. */
public class ModuleController {
    private final ModuleRepository moduleRepository;
    private final LecturerRepository lecturerRepository;
    private final StudentRepository studentRepository;

    ModuleController(ModuleRepository moduleRepository, LecturerRepository lecturerRepository, StudentRepository studentRepository) {
        this.moduleRepository = moduleRepository;
        this.lecturerRepository = lecturerRepository;
        this.studentRepository = studentRepository;
    }

    @PostMapping(path="/createModule")
    @CrossOrigin(origins=FRONTEND_DOMAIN_NAME)
    public ResponseEntity<?> createNewModule(@RequestParam long lecturerId, @RequestParam String moduleCode,
                                             @RequestParam List<Long> studentIds, @RequestParam List<Long> taStudentIds) {
        try {
            CreateModuleUseCase createModuleUseCase = new CreateModuleUseCase(this.moduleRepository, this.studentRepository, this.lecturerRepository);
            CreateModuleRequest createModuleRequest = new CreateModuleRequest(moduleCode, studentIds, taStudentIds, lecturerId);
            CreateModuleResponse response = createModuleUseCase.execute(createModuleRequest);

            if(response.ok()) {
                return ResponseEntity.status(HttpStatus.OK).body("Module created successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response.responseText());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Create module API: " + e.getMessage());
        }
    }

    @GetMapping(path="/getModule")
    @CrossOrigin(origins=FRONTEND_DOMAIN_NAME)
    public ResponseEntity<?> getModule(@RequestParam String moduleCode) {
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
            GetModuleUseCase getModuleUseCase = new GetModuleUseCase(this.moduleRepository);
            GetModuleRequest getModuleRequest = new GetModuleRequest(moduleCode);
            GetModuleResponse response = getModuleUseCase.execute(getModuleRequest);

            if(response.ok()) {
                return ResponseEntity.status(HttpStatus.OK).body(response.module());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response.responseText());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Get module API: " + e.getMessage());
        }
    }
}
