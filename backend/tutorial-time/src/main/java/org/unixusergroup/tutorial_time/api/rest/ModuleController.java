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
import org.unixusergroup.tutorial_time.application.module.dto.GetModuleRequest;
import org.unixusergroup.tutorial_time.application.module.dto.GetModuleResponse;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;
import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
import org.unixusergroup.tutorial_time.domain.timetable.Availability;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;

import java.util.List;

import static org.unixusergroup.tutorial_time.infrastructure.ProjectConfiguration.FRONTEND_DOMAIN_NAME;

/** 
 * The API controller exposing Module Use Cases.
 *
 * The API calls for Module Use Cases are mapped to '/api/module/...'
 */
@RestController
@RequestMapping(path="/api/module")
@CrossOrigin(origins=FRONTEND_DOMAIN_NAME)
public class ModuleController {

    private final ModuleRepository moduleRepository;
    private final LecturerRepository lecturerRepository;
    private final StudentRepository studentRepository;

    /** Constructor, requires a ModuleRepository, LecturerRepository and StudentRepository instance to communicate with the database */
    ModuleController(ModuleRepository moduleRepository, LecturerRepository lecturerRepository, StudentRepository studentRepository) {
        this.moduleRepository = moduleRepository;
        this.lecturerRepository = lecturerRepository;
        this.studentRepository = studentRepository;
    }

    /** 
     * Create Module API mapping (/api/module/createmodule).
     *
     * This API call creates a new module with the given parameters and associates it with the lecturer of the given ID.
     * @param lecturerId the University Staff ID of the lecturer with whom the module is to be associated
     * @param moduleCode the Module Code of the newly created module
     * @param studentIds a list of University Student ID numbers of students to be enrolled in the module
     * @param taStudentIds a list of University Student ID numbers of students to serve as Teaching Assistants for the module
     * @return HTTP status of 200 on successful creation, 500 and the error message if any error occurs
     */
    @PostMapping(path="/createmodule")
    public ResponseEntity<?> createNewModule(@RequestParam long lecturerId, @RequestParam String moduleCode,
                                             @RequestParam List<Long> studentIds, @RequestParam List<Long> taStudentIds) {
        try {
            CreateModuleUseCase createModuleUseCase = new CreateModuleUseCase(this.moduleRepository, this.studentRepository, this.lecturerRepository);
            CreateModuleRequest createModuleRequest = new CreateModuleRequest(moduleCode, studentIds, taStudentIds, lecturerId);
            createModuleUseCase.execute(createModuleRequest);
            return ResponseEntity.status(HttpStatus.OK).body("Module created successfully.");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Create module API: " + e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Create module API: " + e.getMessage());
        }
    }

    /** 
     * Get Module API mapping (/api/module/getmodule).
     *
     * This API call retrieves the data associated with a specific module, given the module exists and the provided lecturer ID is authorized to access it.
     * @param moduleCode the University Module Code of the module to retrieve
     * @param lecturerId the University Staff ID of the lecturer with whom the module is associated
     * @return HTTP status of 200 and Module data in string JSON format on success, 500 and the error message on failure
     */
    @GetMapping(path="/getmodule")
    public ResponseEntity<?> getModule(@RequestParam String moduleCode, @RequestParam long lecturerId) {
        try {
            GetModuleUseCase getModuleUseCase = new GetModuleUseCase(this.moduleRepository);
            GetModuleRequest getModuleRequest = new GetModuleRequest(moduleCode, lecturerId);
            GetModuleResponse response = getModuleUseCase.execute(getModuleRequest);
            return ResponseEntity.status(HttpStatus.OK).body(response.module());
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
