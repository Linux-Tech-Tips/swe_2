/**
 * @author Ethan
 */
package org.unixusergroup.tutorial_time.application.module.dto;


/**
 * CreateModuleUseCase  Response record
 * @param ok A variable to ensure whether the module related use case was successful or not.
 * @param responseText The associated error text with the response
 */
public record ModuleResponse(boolean ok, String responseText) {
    public ModuleResponse() {
        this(true, "");
    }

    public ModuleResponse(String responseText) {
        this(false, responseText);
    }
}
