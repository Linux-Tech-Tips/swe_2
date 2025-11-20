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
                throw new IllegalArgumentException("No module code provided");
            }
            Module module = this.moduleRepository.getModuleByCode(getModuleRequest.moduleCode());
            if (module==null) {
                throw new IllegalArgumentException("Module not found");
            }
            if(module.getLecturer()!=getModuleRequest.lecturerID()) {
                throw new IllegalArgumentException("You are not the lecturer registered for the module " + module.getModuleCode());
            }
            return new GetModuleResponse(module);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
