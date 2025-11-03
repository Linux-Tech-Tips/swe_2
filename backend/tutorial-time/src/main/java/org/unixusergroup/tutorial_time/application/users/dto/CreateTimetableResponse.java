/** 
 * @author Martin
 */
package org.unixusergroup.tutorial_time.application.users.dto;

/** CreateTimetableUseCase Response record
 * @param ok whether the use case executed ok
 * @param responseText text associated with the response, especially if not ok
 */
public record CreateTimetableResponse(boolean ok, String responseText) {}
