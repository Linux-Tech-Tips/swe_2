package org.unixusergroup.tutorial_time.application.users.dto;

import org.unixusergroup.tutorial_time.domain.student.Student;
/**
 * Response object returned after attempting to retrieve a student.
 * <p>
 * This response indicates whether the lookup was successful and contains
 * either the retrieved {@link Student} or an explanatory error message.
 *
 * @param ok            {@code true} if the student was successfully retrieved,
 *                      {@code false} otherwise
 * @param responseText  a description of the result or the error encountered
 * @param student       the retrieved student, or {@code null} if the lookup failed
 *
 * author Ethan
 */
public record GetStudentResponse(boolean ok, String responseText, Student student) {
    public GetStudentResponse(Student student) {
        this(true, "", student);
    }

    public GetStudentResponse(String responseText) {
        this(false, responseText, null);
    }
}
