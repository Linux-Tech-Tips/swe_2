/** 
 * @author Martin
 */
package org.unixusergroup.tutorial_time.application.users.dto;

/** GetTimetableUseCase Response record 
 * @param ok whether the use case executed ok
 * @param availability the requested timetable availability information of the user, null if not ok
 * @param responseText additional text details of the response, if not ok
 */
public record GetTimetableResponse(boolean ok, int[] availability, String responseText) {

    /** Error constructor, given a responseText, set ok to false and availability to null */
    public GetTimetableResponse(String responseText) {
        this(false, null, responseText);
    }

    /** Success constructor, given an availability array, set ok to true and responseText to an empty string */
    public GetTimetableResponse(int[] availability) {
        this(true, availability, "");
    }
}
