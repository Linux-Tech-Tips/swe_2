/**
 * @author Cedar
 */
package org.unixusergroup.tutorial_time.application.module.dto;

/**
 * @param moduleCode the module code
 * @param id the given student's ID
 * @param requestType either removal or addition and student or ta
 */
public record EditEnrollmentRequest(String moduleCode, long id, EditRequestType requestType) {
}
