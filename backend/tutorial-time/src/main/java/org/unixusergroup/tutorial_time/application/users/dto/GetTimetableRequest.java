/** 
 * @author Martin
 */
package org.unixusergroup.tutorial_time.application.users.dto;

import org.unixusergroup.tutorial_time.domain.User;

/** GetTimetableUseCase Request record 
 * @param userId the university ID of the user who's timetable to get
 * @param userType the type of the user, whether lecturer or student
 */
public record GetTimetableRequest(long userId, User.UserType userType) {}
