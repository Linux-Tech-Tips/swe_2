/** 
 * @author Martin
 */
package org.unixusergroup.tutorial_time.application.users.dto;

/** CreateTimetableUseCase Response record
 * @param ok whether the use case executed ok
 * @param responseText text associated with the response, especially if not ok
 */
public record CreateTimetableResponse(boolean ok, String responseText) {
    
    /** Success constructor, sets ok to true and responseText to an empty string */
    public CreateTimetableResponse() {
        this(true, "");
    }

    /** Error constructor, sets ok to false and responseText to the given message */
    public CreateTimetableResponse(String responseText) {
        this(false, responseText);
    }
}
