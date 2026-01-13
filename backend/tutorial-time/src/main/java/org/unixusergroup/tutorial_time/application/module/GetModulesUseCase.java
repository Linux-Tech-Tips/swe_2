/**
 * @author Martin
 */
package org.unixusergroup.tutorial_time.application.module;

import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.module.dto.GetModulesRequest;
import org.unixusergroup.tutorial_time.application.module.dto.GetModulesResponse;
import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;

import java.util.List;

public class GetModulesUseCase implements UseCase<GetModulesResponse, GetModulesRequest> {
    private final ModuleRepository moduleRepository;
    private final LecturerRepository lecturerRepository;

    public GetModulesUseCase(ModuleRepository moduleRepository, LecturerRepository lecturerRepository) {
        this.moduleRepository = moduleRepository;
        this.lecturerRepository = lecturerRepository;
    }

    @Override
    public GetModulesResponse execute(GetModulesRequest getModulesRequest) {
        if(lecturerRepository.getLecturerById(getModulesRequest.lecturerId()) == null) {
            throw new IllegalArgumentException("Lecturer of provided ID doesn't exist");
        }
        List<String> moduleCodes = moduleRepository.getModuleCodesByLecturer(getModulesRequest.lecturerId());
        return new GetModulesResponse(moduleCodes);
    }
}

