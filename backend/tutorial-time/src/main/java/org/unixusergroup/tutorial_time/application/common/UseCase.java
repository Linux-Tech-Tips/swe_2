/** 
 * @author Ethan
 */
package org.unixusergroup.tutorial_time.application.common;

public interface UseCase<Output, Input> {
    public Output execute(Input input);
}
