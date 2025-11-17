/**
 * @author Cedar
 */
package org.unixusergroup.tutorial_time.application.exceptions;

/**
 * custom exception class for when a user requests or creates a timetable with the incorrect user type for their ID
 */
public class IncorrectUserTypeException extends RuntimeException{
    public IncorrectUserTypeException(String e) {
        super(e);
    }
}
