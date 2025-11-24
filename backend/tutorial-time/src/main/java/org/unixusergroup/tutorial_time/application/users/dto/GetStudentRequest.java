package org.unixusergroup.tutorial_time.application.users.dto;
/**
 * Request object for retrieving a student by their unique ID.
 * <p>
 * This record contains only the identifier needed to look up a student
 * within the system.
 *
 * @param studentId the unique identifier of the student to retrieve
 *
 * author Ethan
 */
public record GetStudentRequest(long studentId) {
}
