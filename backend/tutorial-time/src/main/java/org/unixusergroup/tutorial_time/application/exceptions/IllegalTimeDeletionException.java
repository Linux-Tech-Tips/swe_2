/**
 * @author Cedar
 */
package org.unixusergroup.tutorial_time.application.exceptions;

/**
 * more fine-grained version of illegalConstraintsException
 * thrown when a constraint leads to the deletion of a time which would lead to a student never being available
 */
public class IllegalTimeDeletionException extends IllegalArgumentException{
    public IllegalTimeDeletionException (String e) {
        super(e);
    }
}
