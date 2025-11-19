/**
 * @author Ethan
 */
package org.unixusergroup.tutorial_time.application.users.dto;

import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;

/**
 *
 * @param ok A signal to see
 * @param responseText
 * @param lecturer
 */
public record GetLecturerResponse(boolean ok, String responseText, Lecturer lecturer) {

    /** Success constructor */
    public GetLecturerResponse(Lecturer lecturer) {
        this(true, "", lecturer);
    }

    /** Error constructor with error text. */
    public GetLecturerResponse(String responseText) {
        this(false, responseText, null);
    }
}
