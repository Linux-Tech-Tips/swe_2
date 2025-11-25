/**
 * Request object for adding a new lecturer to the system.
 * <p>
 * This record contains the basic identifying information required
 * to create and register a lecturer.
 *
 * @param name     the lecturer's full name
 * @param email    the lecturer's email address
 * @param staffId  the unique staff ID assigned to the lecturer
 *
 * @author Ethan
 */
package org.unixusergroup.tutorial_time.application.users.dto;

public record AddLecturerRequest(String name, String email, long staffId) {
}
