package org.unixusergroup.tutorial_time.application.exceptions;

public class IllegalStudentAdditionException extends Exception{
    public IllegalStudentAdditionException(String errorText){
        super(errorText);
    }
}
