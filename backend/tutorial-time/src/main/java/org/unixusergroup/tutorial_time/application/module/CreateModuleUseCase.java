/** 
 * @author Ethan, Martin
 */
package org.unixusergroup.tutorial_time.application.module;

import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.module.dto.ModuleRequest;
import org.unixusergroup.tutorial_time.application.module.dto.ModuleResponse;

public class CreateModuleUseCase implements UseCase<ModuleResponse, ModuleRequest> {

    public CreateModuleUseCase() {};

    /**
     * TODO: implement the logic that allows the repository to create a module and saves it into the repository.
     *
     * So atm
     * Need to run checks such that an invalid module, throws an exception.
     * @param moduleRequest
     * @return
     */
    @Override
    public ModuleResponse execute(ModuleRequest moduleRequest) throws RuntimeException {
        try {
            Module newModule = new Module(moduleRequest.moduleCode(), moduleRequest.students(), moduleRequest.taStudents(), moduleRequest.lecturer());
            if (newModule.getModuleCode().isEmpty() || newModule.getLecturer() == null) {
                throw new RuntimeException();
            }

            moduleRequest.moduleRepository().saveModule(newModule);
            return new ModuleResponse(newModule);

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
