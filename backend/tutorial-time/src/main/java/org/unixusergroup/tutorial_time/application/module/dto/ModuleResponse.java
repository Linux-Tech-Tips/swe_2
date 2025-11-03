package org.unixusergroup.tutorial_time.application.module.dto;
import org.unixusergroup.tutorial_time.domain.module.Module;

/**
 * This is a DTO response that will return the course code from the response.
 *
 * @param module
 */
public record ModuleResponse(Module module) {
    public Module getModule() {
        return this.module;
    }
}
