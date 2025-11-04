package org.unixusergroup.tutorial_time.application.module;

import org.unixusergroup.tutorial_time.application.module.dto.ModuleResponse;
import org.unixusergroup.tutorial_time.application.module.dto.UpdateEnrollmentStatusRequest;


/**
 * Interface for adding or removing a single student from a module.
 */
public interface UpdateEnrollmentStatusUseCase {

    // Ensure the DTOs are correctly referenced here:
    ModuleResponse execute(UpdateEnrollmentStatusRequest request);
}