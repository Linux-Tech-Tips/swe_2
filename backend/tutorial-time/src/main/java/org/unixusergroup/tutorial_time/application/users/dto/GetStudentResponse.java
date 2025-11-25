/**
 * @author Ethan
 */
package org.unixusergroup.tutorial_time.application.users.dto;

import org.unixusergroup.tutorial_time.domain.student.Student;

public record GetStudentResponse(boolean ok, String responseText, Student student) {
    public GetStudentResponse(Student student) {
        this(true, "", student);
    }

    public GetStudentResponse(String responseText) {
        this(false, responseText, null);
    }
}
