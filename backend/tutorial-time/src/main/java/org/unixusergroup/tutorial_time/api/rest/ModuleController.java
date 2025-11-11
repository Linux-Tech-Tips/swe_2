/**
 * @author Ethan
 */

package org.unixusergroup.tutorial_time.api.rest;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

import java.util.List;

@RestController
@RequestMapping(path="/api/module")

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

    @PostMapping(path="/getModule")
    public ResponseEntity<?> getModule(@RequestParam String moduleCode) {
        try {
            Module module = moduleRepository.getModuleByCode(moduleCode);
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
