package org.unixusergroup.tutorial_time.application.exceptions;

public class IllegalStudentRemovalException extends Exception {
    public IllegalStudentRemovalException(String errorText){
        super(errorText);
    }
}
