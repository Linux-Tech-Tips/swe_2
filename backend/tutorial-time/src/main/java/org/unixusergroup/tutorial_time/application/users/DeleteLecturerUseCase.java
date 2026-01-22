/**
 * @author Cedar
 */

package org.unixusergroup.tutorial_time.application.users;

import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.module.GetModulesUseCase;
import org.unixusergroup.tutorial_time.application.module.dto.GetModulesRequest;
import org.unixusergroup.tutorial_time.application.users.dto.DeleteLecturerRequest;
import org.unixusergroup.tutorial_time.application.users.dto.DeleteLecturerResponse;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;

import java.util.List;

public class DeleteLecturerUseCase implements UseCase<DeleteLecturerResponse, DeleteLecturerRequest> {
    private final LecturerRepository lecturerRepository;
    private final ModuleRepository moduleRepository;

    public DeleteLecturerUseCase(LecturerRepository lecturerRepository, ModuleRepository moduleRepository) {
        this.lecturerRepository = lecturerRepository;
        this.moduleRepository = moduleRepository;
    }

    @Override
    public DeleteLecturerResponse execute(DeleteLecturerRequest request) {
        Lecturer lecturer = lecturerRepository.getLecturerById(request.staffId());
        if(lecturer == null) {
            throw new IllegalArgumentException("Given id does not correspond to a lecturer");
        }
        /* fetch all modules related to a lecturer and delete them */
        GetModulesUseCase getModulesUseCase = new GetModulesUseCase(moduleRepository, lecturerRepository);
        GetModulesRequest getModulesRequest = new GetModulesRequest(lecturer.getUniversityId());

        List<String> moduleCodes = getModulesUseCase.execute(getModulesRequest).modules();
        for(String code : moduleCodes) {
            Module module = moduleRepository.getModuleByCode(code);
            if(module == null) {
                throw new IllegalArgumentException("Given code does not correspond to a module");
            }
            moduleRepository.deleteModule(module);;
        }
        lecturerRepository.deleteLecturer(lecturer);
        return new DeleteLecturerResponse();
    }
}
