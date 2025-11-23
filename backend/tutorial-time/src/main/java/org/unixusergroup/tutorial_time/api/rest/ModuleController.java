/**
 * @author Ethan, Cedar, Lilian
 */
package org.unixusergroup.tutorial_time.api.rest;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.unixusergroup.tutorial_time.application.common.exceptions.ModuleNotFoundException;
import org.unixusergroup.tutorial_time.application.common.exceptions.StudentNotFoundException;
import org.unixusergroup.tutorial_time.application.module.CreateModuleUseCase;
import org.unixusergroup.tutorial_time.application.module.EditEnrollmentUseCase;
import org.unixusergroup.tutorial_time.application.module.GetModuleUseCase;
import org.unixusergroup.tutorial_time.application.module.dto.CreateModuleRequest;
import org.unixusergroup.tutorial_time.application.module.dto.EditEnrollmentRequest;
import org.unixusergroup.tutorial_time.application.module.dto.EditRequestType;
import org.unixusergroup.tutorial_time.application.module.dto.GetModuleRequest;
import org.unixusergroup.tutorial_time.application.module.dto.GetModuleResponse;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
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
     * @return HTTP status of 200 on successful creation,
     *         HTTP status of 400 and an error message when an error occurs due to incorrect user input,
     *         HTTP status of 500 and the error message when any other error occurs
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
     * @return HTTP status of 200 and Module data in string JSON format on success,
     *         HTTP status of 400 and an error message when an error occurs due to incorrect user input,
     *         HTTP status of 500 and the error message when any other error occurs
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

    /**
     * Add Student API mapping (/api/module/addstudent).
     *
     * This API call adds a student of a given ID to the list of enrolled students for a given module.
     * @param moduleCode the Module Code of the module which is affected by this change
     * @param id the University Student ID of the student to be added
     * @return HTTP status of 200 if update successful,
     * HTTP status of 400 and an error message when an error occurs due to incorrect user input,
     * HTTP status of 500 and the error message when any other error occurs
     */
    @PostMapping(path="/addstudent")
    public ResponseEntity<?> addStudent(@RequestParam String moduleCode, @RequestParam long id) {
        try {
            EditEnrollmentUseCase editEnrollmentUseCase = new EditEnrollmentUseCase(this.moduleRepository);
            EditEnrollmentRequest editEnrollmentRequest = new EditEnrollmentRequest(moduleCode, id, EditRequestType.STUDENT_ADDITION);
            editEnrollmentUseCase.execute(editEnrollmentRequest);
            return ResponseEntity.status(HttpStatus.OK).body("Module updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Remove Student API mapping (/api/module/removestudent).
     *
     * This API call removes a student of a given ID from the list of enrolled students for a given module.
     * @param moduleCode the Module Code of the module which is affected by this change
     * @param id the University Student ID of the student to be removed
     * @return HTTP status of 200 if update successful,
     * HTTP status of 400 and an error message when an error occurs due to incorrect user input,
     * HTTP status of 500 and the error message when any other error occurs
     */
    @PostMapping(path="/removestudent")
    public ResponseEntity<?> removeStudent(@RequestParam String moduleCode, @RequestParam long id) {
        try {
            EditEnrollmentUseCase editEnrollmentUseCase = new EditEnrollmentUseCase(this.moduleRepository);
            EditEnrollmentRequest editEnrollmentRequest = new EditEnrollmentRequest(moduleCode, id, EditRequestType.STUDENT_REMOVAL);
            editEnrollmentUseCase.execute(editEnrollmentRequest);
            return ResponseEntity.status(HttpStatus.OK).body("Module updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Remove TA API mapping (/api/module/removeta).
     *
     * This API call removes a student of a given ID from the list of teaching assistants for a given module.
     * @param moduleCode the Module Code of the module which is affected by this change
     * @param id the University Student ID of the student to be removed as a TA
     * @return HTTP status of 200 if update successful,
     *         HTTP status of 400 and an error message when an error occurs due to incorrect user input,
     *         HTTP status of 500 and the error message when any other error occurs
     */
    @PostMapping(path="/removeta")
    public ResponseEntity<?> removeTA(@RequestParam String moduleCode, @RequestParam long id) {
        try {
            EditEnrollmentUseCase editEnrollmentUseCase = new EditEnrollmentUseCase(this.moduleRepository);
            EditEnrollmentRequest editEnrollmentRequest = new EditEnrollmentRequest(moduleCode, id, EditRequestType.TA_REMOVAL);
            editEnrollmentUseCase.execute(editEnrollmentRequest);
            return ResponseEntity.status(HttpStatus.OK).body("Module updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Add TA API mapping (/api/module/addta).
     *
     * This API call adds a student of a given ID to the list of teaching assistants for a given module.
     * @param moduleCode the Module Code of the module which is affected by this change
     * @param id the University Student ID of the student to be added as a TA
     * @return HTTP status of 200 if update successful,
     *         HTTP status of 400 and an error message when an error occurs due to incorrect user input,
     *         HTTP status of 500 and the error message when any other error occurs
     */
    @PostMapping(path="/addta")
    public ResponseEntity<?> addTA(@RequestParam String moduleCode, @RequestParam long id) {
        try {
            EditEnrollmentUseCase editEnrollmentUseCase = new EditEnrollmentUseCase(this.moduleRepository);
            EditEnrollmentRequest editEnrollmentRequest = new EditEnrollmentRequest(moduleCode, id, EditRequestType.TA_ADDITION);
            editEnrollmentUseCase.execute(editEnrollmentRequest);
            return ResponseEntity.status(HttpStatus.OK).body("Module updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    /* Handler for a 404 (Not Found)*/
    @ExceptionHandler(ModuleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleModuleNotFound(ModuleNotFoundException ex) {
        return ex.getMessage();
    }

    /* Handler for a 400 (Bad Request) - for invalid input data*/
    @ExceptionHandler(StudentNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleStudentNotFound(StudentNotFoundException ex) {
        return ex.getMessage();
    }
}
