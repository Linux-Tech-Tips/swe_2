/**
 * @author Ethan
 */
package org.unixusergroup.tutorial_time.application.module.dto;


/**
 * CreateModuleUseCase  Response record
 * @param ok A variable to ensure whether the module related use case was successful or not.
 * @param responseText The associated error text with the response
 */
public record CreateModuleResponse(boolean ok, String responseText) {
    /** Success constructor for the response of the CreateModuleUseCase */
    public CreateModuleResponse() {
        this(true, "");
    }

    /** Error constructor for the response of the CreateModuleUseCase.
     * which contains an error text associated with it.
     */
    public CreateModuleResponse(String responseText) {
        this(false, responseText);
    }
}
