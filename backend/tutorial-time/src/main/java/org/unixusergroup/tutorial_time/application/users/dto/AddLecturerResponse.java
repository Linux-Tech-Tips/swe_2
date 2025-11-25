/**
 *
 * @author Ethan
 */
package org.unixusergroup.tutorial_time.application.users.dto;

public record AddLecturerResponse(boolean ok, String responseText) {
    public AddLecturerResponse() {
        this(true, "");
    }

    public AddLecturerResponse(String responseText) {
        this(false, responseText);
    }
}
