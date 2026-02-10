package org.unixusergroup.tutorial_time.application.module;

import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.module.dto.DeleteModuleRequest;
import org.unixusergroup.tutorial_time.application.module.dto.DeleteModuleResponse;
import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;

public class DeleteModuleUseCase implements UseCase<DeleteModuleResponse, DeleteModuleRequest> {

    private final ModuleRepository moduleRepository;

    public DeleteModuleUseCase(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    public DeleteModuleResponse execute(DeleteModuleRequest request) {
        Module module = moduleRepository.getModuleByCode(request.moduleCode());
        if(module == null) {
            throw new IllegalArgumentException("Given code does not correspond to a module");
        }
        moduleRepository.deleteModule(module);
        return new DeleteModuleResponse();
    }
}
