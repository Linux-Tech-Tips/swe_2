/**
 * @author Ethan
 */
package org.unixusergroup.tutorial_time.application.module;

import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.module.dto.GetModuleRequest;
import org.unixusergroup.tutorial_time.application.module.dto.GetModuleResponse;
import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;

public class GetModuleUseCase implements UseCase<GetModuleResponse, GetModuleRequest> {
    private final ModuleRepository moduleRepository;

    public GetModuleUseCase(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    @Override
    public GetModuleResponse execute(GetModuleRequest getModuleRequest) {
        try {
            if (getModuleRequest.moduleCode().isEmpty()) {
                throw new IllegalArgumentException("No module code provided.");
            }
            if (this.moduleRepository==null) {
                return new GetModuleResponse("Error: Module not found in GetModuleUseCase.");
            }
            Module module = this.moduleRepository.getModuleByCode(getModuleRequest.moduleCode());
            return new GetModuleResponse(module);

        } catch (Exception e) {
            return new GetModuleResponse(e.getMessage());
        }
    }
}
