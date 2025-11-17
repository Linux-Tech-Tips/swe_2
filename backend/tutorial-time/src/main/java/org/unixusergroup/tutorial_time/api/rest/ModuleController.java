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
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;

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

    @GetMapping(path="/getModule")
    public ResponseEntity<?> getModule(@RequestParam String moduleCode) {
        try {
            GetModuleUseCase getModuleUseCase = new GetModuleUseCase(this.moduleRepository);
            GetModuleRequest getModuleRequest = new GetModuleRequest(moduleCode);
            GetModuleResponse response = getModuleUseCase.execute(getModuleRequest);
            return ResponseEntity.status(HttpStatus.OK).body(response.module());
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Get module API: " + e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Get module API: " + e.getMessage());
        }
    }
}
