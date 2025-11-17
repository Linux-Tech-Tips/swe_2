/**
 * @author Cedar
 */

package org.unixusergroup.tutorial_time.application.exceptions;

/**
 * exception class to represent an illegal constraint
 * this includes both it being the wrong format (e.g. a negative integer)
 * and if the given constraint is not possible
 */
public class IllegalConstraintsException extends IllegalArgumentException {
    public IllegalConstraintsException(String exception) {
        super(exception);
    }
}
