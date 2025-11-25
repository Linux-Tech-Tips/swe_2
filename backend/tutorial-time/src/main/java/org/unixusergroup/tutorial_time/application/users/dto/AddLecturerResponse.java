package org.unixusergroup.tutorial_time.application.users.dto;
/**
 * Response object returned after attempting to add a new lecturer.
 * <p>
 * This response indicates whether the operation succeeded and may
 * include an optional message describing the outcome or any error
 * that occurred.
 *
 * @param ok            {@code true} if the lecturer was added successfully,
 *                      {@code false} otherwise
 * @param responseText  an optional message describing the result or error
 *
 * @author Ethan
 */
public record AddLecturerResponse(boolean ok, String responseText) {
    public AddLecturerResponse() {
        this(true, "");
    }

    public AddLecturerResponse(String responseText) {
        this(false, responseText);
    }
}
