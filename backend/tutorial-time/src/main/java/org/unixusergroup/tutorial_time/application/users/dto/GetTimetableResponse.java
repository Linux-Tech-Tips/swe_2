/** 
 * @author Martin
 */
package org.unixusergroup.tutorial_time.application.users.dto;

/** GetTimetableUseCase Response record
 * @param availability the requested timetable availability information of the user, null if not ok
 */
public record GetTimetableResponse(int[] availability) {
    /** Success constructor, given an availability array, set ok to true and responseText to an empty string */
    public GetTimetableResponse(int[] availability) {
        this.availability = availability;
    }
}
