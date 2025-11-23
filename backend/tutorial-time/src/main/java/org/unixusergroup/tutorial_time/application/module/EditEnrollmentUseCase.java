/**
 * @author Cedar
 */

package org.unixusergroup.tutorial_time.application.module;

import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.module.dto.EditEnrollmentRequest;
import org.unixusergroup.tutorial_time.application.module.dto.EditEnrollmentResponse;
import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;

public class EditEnrollmentUseCase implements UseCase<EditEnrollmentResponse, EditEnrollmentRequest> {

    private final ModuleRepository moduleRepository;

    /**
     * constructor
     * @param moduleRepository
     */
    public EditEnrollmentUseCase(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    /**
     * execution method
     * checks that the module exists, and then depending on the request type either adds or removes the given id from the correct list
     * @param request associated request
     * @return an empty response item if successful
     */
    public EditEnrollmentResponse execute(EditEnrollmentRequest request) {
        /* Get the module from the repository */
        Module module = moduleRepository.getModuleByCode(request.moduleCode());
        if(module == null) {
            throw new IllegalArgumentException("The given module does not exist");
        }

        /* Perform the required action */
        try {
            switch(request.requestType()) {
                case TA_REMOVAL:
                    module.removeTAStudent(request.id());
                    break;
                case TA_ADDITION:
                    module.addTAStudent(request.id());
                    break;
                case STUDENT_REMOVAL:
                    module.removeStudent(request.id());
                    break;
                case STUDENT_ADDITION:
                    module.addStudent(request.id());
                    break;
           }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

        /* If action performed successfully (no throw) save module back into the repository */
        moduleRepository.saveModule(module);
        return new EditEnrollmentResponse();
    }
}
