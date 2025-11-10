package org.unixusergroup.tutorial_time.application.exceptions;

public class IllegalModuleSizeException extends IllegalArgumentException {
    public IllegalModuleSizeException(String errorText){
        super(errorText);
    }
}
