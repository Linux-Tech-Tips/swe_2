/*
 * @author Lilian
 */

package org.unixusergroup.tutorial_time.api.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.unixusergroup.tutorial_time.application.common.exceptions.ModuleNotFoundException;
import org.unixusergroup.tutorial_time.application.common.exceptions.StudentNotFoundException;
import org.unixusergroup.tutorial_time.application.module.UpdateModuleEnrollmentUseCase;
import org.unixusergroup.tutorial_time.application.module.dto.ModuleResponse;

/**
 * REST Controller responsible for handling web requests regarding Module management.
 */
@RestController
@RequestMapping("/api/modules")
public class ModuleController {

    /* Constructs ModuleController & automatically injects Use Case implementation.*/
    private final UpdateModuleEnrollmentUseCase updateModuleEnrollmentUseCase;

    public ModuleController (UpdateModuleEnrollmentUseCase updateModuleEnrollmentUseCase) {
        this.updateModuleEnrollmentUseCase = updateModuleEnrollmentUseCase;
    }

    /*dTO for request body regarding updating student enrollment */
    public record EnrollmentUpdateRequest(List<Long> studentIds) {}

    /*Deals with PATCH requests for updating enrollment of students in specific module
     * Replaces existing student list with new list provided
     */
    @PatchMapping("/{moduleCode}/enrollment")
    public ResponseEntity<ModuleResponse> updateEnrollment (
        @PathVariable String moduleCode,
        @RequestBody EnrollmentUpdateRequest request
    ) {
        ModuleResponse updateModule = updateModuleEnrollmentUseCase.execute(moduleCode, request.studentIds());
        return ResponseEntity.ok(updateModule);
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
