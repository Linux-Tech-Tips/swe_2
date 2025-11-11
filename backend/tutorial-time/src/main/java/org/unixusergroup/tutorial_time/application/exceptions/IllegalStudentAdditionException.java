/**
 * @author Cedar
 */

package org.unixusergroup.tutorial_time.application.exceptions;

/**
 * custom exception class for handling an illegal addition of a student:
 * 1. to a timeslot
 * 2. to a module
 * is triggered if the student doesn't exist, already is in the given structure, or the given structure is full
 */
public class IllegalStudentAdditionException extends Exception{
    public IllegalStudentAdditionException(String errorText){
        super(errorText);
    }
}
