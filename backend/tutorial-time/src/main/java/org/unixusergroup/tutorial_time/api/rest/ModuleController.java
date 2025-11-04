/*
 * @author Lilian
 */

package org.unixusergroup.tutorial_time.api.rest;

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
import org.unixusergroup.tutorial_time.application.module.UpdateEnrollmentStatusUseCase;
import org.unixusergroup.tutorial_time.application.module.dto.ModuleResponse;
import org.unixusergroup.tutorial_time.application.module.dto.UpdateEnrollmentStatusRequest;

/**
 * REST Controller responsible for handling web requests regarding Module management.
 */
@RestController
@RequestMapping("/api/modules")
public class ModuleController {

    public enum EnrollmentAction {
        ADD,
        REMOVE
    }

    private final UpdateEnrollmentStatusUseCase updateEnrollmentStatusUseCase;

        public ModuleController (UpdateEnrollmentStatusUseCase updateEnrollmentStatusUseCase) {
            this.updateEnrollmentStatusUseCase = updateEnrollmentStatusUseCase;
        }

        public record EnrollmentUpdateRequest(
            long studentId,
            EnrollmentAction action
        ) {}

        /**
         * Handles PATCH requests to add or remove a single student from a specific module.
         */
        @PatchMapping("/{moduleCode}/enrollment")
        public ResponseEntity<ModuleResponse> updateEnrollment (
            @PathVariable String moduleCode,
            @RequestBody EnrollmentUpdateRequest apiRequest
        ) {
            UpdateEnrollmentStatusRequest useCaseRequest = new UpdateEnrollmentStatusRequest(
                moduleCode,
                apiRequest.studentId(),
                apiRequest.action()
            );

            ModuleResponse updatedModule = updateEnrollmentStatusUseCase.execute(useCaseRequest);
            return ResponseEntity.ok(updatedModule);
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
