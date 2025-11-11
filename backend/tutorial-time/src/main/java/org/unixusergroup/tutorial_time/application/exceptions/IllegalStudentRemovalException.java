package org.unixusergroup.tutorial_time.application.exceptions;

/**
 * custom exception class for handling an illegal removal of a student:
 * 1. from a timeslot
 * 2. from a module
 * is triggered if the student doesn't exist or isn't in the given structure
 */
public class IllegalStudentRemovalException extends Exception {
    public IllegalStudentRemovalException(String errorText){
        super(errorText);
    }
}
