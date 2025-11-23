/*
 * @author Lilian
 */

package org.unixusergroup.tutorial_time.application.common.exceptions;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException (String message) {
        super(message);
    }
}
