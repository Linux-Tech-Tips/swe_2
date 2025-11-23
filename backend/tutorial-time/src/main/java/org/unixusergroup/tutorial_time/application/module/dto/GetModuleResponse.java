/**
 * @author Ethan
 */
package org.unixusergroup.tutorial_time.application.module.dto;
import org.unixusergroup.tutorial_time.domain.module.Module;

/**
 * GetModule use case Response record
 * @param module The retrieved module.
 */
public record GetModuleResponse(Module module) {
    /** Success constructor for the response of the CreateModuleUseCase */
    public GetModuleResponse(Module module) {
        this.module = module;
    }
}
