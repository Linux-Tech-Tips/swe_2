/** 
 * @author Ethan, Martin
 */
package org.unixusergroup.tutorial_time.application.module;

import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;
import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.module.dto.CreateModuleRequest;
import org.unixusergroup.tutorial_time.application.module.dto.CreateModuleResponse;

public class CreateModuleUseCase implements UseCase<CreateModuleResponse, CreateModuleRequest> {

    /** The module repository associated with this use case. */
    private final ModuleRepository moduleRepository;
    public CreateModuleUseCase(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    };

    /** Creates a module with the request params and saves it in ModuleRepository
     * while providing an appropriate response.*/
    @Override
    public CreateModuleResponse execute(CreateModuleRequest moduleRequest) {
        try {
            Module newModule = new Module(moduleRequest.moduleCode(), moduleRequest.students(), moduleRequest.taStudents(), moduleRequest.lecturer());
            if (newModule.getModuleCode().isEmpty() || newModule.getLecturer() == null) {
                throw new IllegalArgumentException();
            }

            this.moduleRepository.saveModule(newModule);
            return new CreateModuleResponse();

        } catch (Exception e) {
            return new CreateModuleResponse(e.getMessage());
        }
    }
}
