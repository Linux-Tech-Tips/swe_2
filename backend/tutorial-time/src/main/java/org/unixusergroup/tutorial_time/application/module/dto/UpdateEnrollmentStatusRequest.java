/*
 * @author Lilian
 */

package org.unixusergroup.tutorial_time.application.module.dto;

/**
 * Update a single student's enrollment status (ADD or REMOVE).
 */
public record UpdateEnrollmentStatusRequest(
    String moduleCode,
    long studentId,
    EnrollmentAction action
) {
    public enum EnrollmentAction {
        ADD,
        REMOVE
    }
}
