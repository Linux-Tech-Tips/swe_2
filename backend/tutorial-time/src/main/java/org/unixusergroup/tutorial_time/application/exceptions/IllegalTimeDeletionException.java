package org.unixusergroup.tutorial_time.application.exceptions;

public class IllegalTimeDeletionException extends IllegalArgumentException{
    public IllegalTimeDeletionException (String e) {
        super(e);
    }
}
