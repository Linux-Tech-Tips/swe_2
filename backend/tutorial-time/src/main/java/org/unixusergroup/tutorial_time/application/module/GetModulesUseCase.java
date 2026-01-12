/**
 * @author Martin
 */
package org.unixusergroup.tutorial_time.application.module;

import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.module.dto.GetModulesRequest;
import org.unixusergroup.tutorial_time.application.module.dto.GetModulesResponse;
import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;

import java.util.List;

public class GetModulesUseCase implements UseCase<GetModulesResponse, GetModulesRequest> {
    private final ModuleRepository moduleRepository;

    public GetModulesUseCase(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    @Override
    public GetModulesResponse execute(GetModulesRequest getModulesRequest) {
        List<String> moduleCodes = moduleRepository.getModuleCodesByLecturer(getModulesRequest.lecturerId());
        return new GetModulesResponse(moduleCodes);
    }
}

