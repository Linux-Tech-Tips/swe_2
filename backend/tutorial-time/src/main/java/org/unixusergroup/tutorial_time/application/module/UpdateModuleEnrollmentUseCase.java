/** 
 * @author Lilian
 */

package org.unixusergroup.tutorial_time.application.module;

import java.util.List;

import org.unixusergroup.tutorial_time.application.module.dto.ModuleResponse;

public interface UpdateModuleEnrollmentUseCase {

    ModuleResponse execute(String moduleCode, List<Long> newStudentIds);
}