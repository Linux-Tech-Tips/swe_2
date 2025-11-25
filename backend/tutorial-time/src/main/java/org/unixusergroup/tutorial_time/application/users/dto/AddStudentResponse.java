/**
 * @author Ethan
 */
package org.unixusergroup.tutorial_time.application.users.dto;

public record AddStudentResponse(boolean ok, String responseText) {
    public AddStudentResponse() {
        this(true, "");
    }
    public AddStudentResponse(String responseText) {
        this(false, responseText);
    }
}
