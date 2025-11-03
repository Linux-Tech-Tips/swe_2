/** 
 * @author Martin
 */
package org.unixusergroup.tutorial_time.application.users.dto;

/** CreateTimetableUseCase Request record
 * @param availability 40-integer array containing timetable availability information
 * @param userId the university ID of the user the timetable should be added to
 * @param userType the type of the user, whether lecturer or student
 */
public record CreateTimetableRequest(int[] availability, long userId, UserType userType) {

    public enum UserType {STUDENT, LECTURER}

}
