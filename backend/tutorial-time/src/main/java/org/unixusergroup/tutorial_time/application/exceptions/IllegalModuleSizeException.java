/**
 * @author Cedar
 */
package org.unixusergroup.tutorial_time.application.exceptions;

/**
 * custom exception class to handle an illegal module size input when creating a timeslot
 */
public class IllegalModuleSizeException extends IllegalArgumentException {
    public IllegalModuleSizeException(String errorText){
        super(errorText);
    }
}
