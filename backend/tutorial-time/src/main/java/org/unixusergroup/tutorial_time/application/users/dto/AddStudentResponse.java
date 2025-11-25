/**
 * @author Ethan
 */
package org.unixusergroup.tutorial_time.application.users.dto;
/**
 * Response object returned after attempting to add a new student.
 * <p>
 * The response indicates whether the operation was successful and may
 * include a descriptive message if an error occurred.
 *
 * @param ok            {@code true} if the student was added successfully,
 *                      {@code false} otherwise
 * @param responseText  a message describing the result or the error encountered
 *
 * author Ethan
 */
public record AddStudentResponse(boolean ok, String responseText) {
    public AddStudentResponse() {
        this(true, "");
    }
    public AddStudentResponse(String responseText) {
        this(false, responseText);
    }
}
