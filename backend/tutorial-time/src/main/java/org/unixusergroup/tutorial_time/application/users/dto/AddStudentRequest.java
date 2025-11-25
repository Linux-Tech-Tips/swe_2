/**
 * @author Ethan
 */

package org.unixusergroup.tutorial_time.application.users.dto;
/**
 * Request object for adding a new student to the system.
 * <p>
 * This record provides the basic identifying information required
 * to create and register a student.
 *
 * @param name       the student's full name
 * @param email      the student's email address
 * @param studentId  the unique student identifier assigned to the student
 *
 * @author Ethan
 */
public record AddStudentRequest(String name, String email, long studentId) {
}
