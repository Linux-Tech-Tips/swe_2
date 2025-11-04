/**
 * @author Ethan
 */
package org.unixusergroup.tutorial_time.application.module.dto;
import org.unixusergroup.tutorial_time.domain.module.Module;

/**
 * GetModule use case Response record
 * @param ok A variable to ensure whether the module related use case was successful or not.
 * @param responseText The associated error text with the response
 * @param module The retrieved module.
 */
public record GetModuleResponse(boolean ok, Module module, String responseText) {
    /** Success constructor for the response of the CreateModuleUseCase */
    public GetModuleResponse(Module module) {
        this(true, module, "");
    }

    /** Error constructor for the response of the CreateModuleUseCase.
     * which contains an error text associated with it.
     */
    public GetModuleResponse(String responseText) {
        this(false, null, responseText);
    }
}
