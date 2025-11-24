/**
 * @author Ethan
 */
package org.unixusergroup.tutorial_time.application.module;

import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.module.dto.GetModuleRequest;
import org.unixusergroup.tutorial_time.application.module.dto.GetModuleResponse;
import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;

/**
 * A use case for retrieving a {@link Module} by its module code.
 * <p>
 * This use case validates:
 * <ul>
 *     <li>that a module code is provided</li>
 *     <li>that the module exists</li>
 *     <li>that the requesting lecturer is the lecturer of the module</li>
 * </ul>
 *
 * If any condition fails, an appropriate {@link IllegalArgumentException}
 * is thrown and wrapped in a {@link RuntimeException}.
 *
 */
public class GetModuleUseCase implements UseCase<GetModuleResponse, GetModuleRequest> {
    private final ModuleRepository moduleRepository;

    public GetModuleUseCase(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

     /**
     * Executes the use case to retrieve module data.
     *
     * @param getModuleRequest the request containing the module code and lecturer ID
     * @return a {@link GetModuleResponse} containing the retrieved module
     * @throws RuntimeException if validation fails or the module cannot be retrieved
     */
    @Override
    public GetModuleResponse execute(GetModuleRequest getModuleRequest) {
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
    }
}
