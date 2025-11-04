/*
 * @author Lilian
 */

package org.unixusergroup.tutorial_time.application.module.dto;

import org.unixusergroup.tutorial_time.api.rest.ModuleController.EnrollmentAction;

/**
 * Update a single student's enrollment status (ADD or REMOVE).
 */
public record UpdateEnrollmentStatusRequest(
    String moduleCode,
    long studentId,
    EnrollmentAction action
) {
    // Records automatically generate the constructor and accessors 
}
