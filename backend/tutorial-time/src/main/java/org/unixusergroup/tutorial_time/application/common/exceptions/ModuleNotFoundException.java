/*
 * @author Lilian
 */

package org.unixusergroup.tutorial_time.application.common.exceptions;

public class ModuleNotFoundException extends RuntimeException {
    public ModuleNotFoundException(String message) {
        super(message);
    }
}
