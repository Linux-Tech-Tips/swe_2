package org.unixusergroup.tutorial_time.application.module.dto;

/**
 * Request object for retrieving a module by its code.
 * <p>
 * This request includes both the module code to be looked up and the
 * lecturer ID making the request, allowing the use case to verify that
 * the requester is authorized to access the module.
 *
 * @param moduleCode the code identifying the module
 * @param lecturerID the ID of the lecturer requesting the module
 */
public record GetModuleRequest(String moduleCode, long lecturerID) {
}
