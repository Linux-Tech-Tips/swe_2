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
import org.unixusergroup.tutorial_time.application.module.dto.CreateModuleRequest;
import org.unixusergroup.tutorial_time.application.module.dto.CreateModuleResponse;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
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
    public ResponseEntity<?> createNewModule(@RequestParam long staffId, @RequestParam String moduleCode,
                                             @RequestParam List<Student> students, @RequestParam List<Student> taStudents) {
        try {
            Lecturer lecturer = lecturerRepository.getLecturerById(staffId);
            CreateModuleUseCase createModuleUseCase = new CreateModuleUseCase(this.moduleRepository);
            CreateModuleRequest createModuleRequest = new CreateModuleRequest(moduleCode, students, taStudents, lecturer);
            CreateModuleResponse response = createModuleUseCase.execute(createModuleRequest);

            if(response.ok()) {
                return ResponseEntity.status(HttpStatus.OK).body("Module created successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response.responseText());
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Create module API: " + e.getMessage());
        }
    }


}
