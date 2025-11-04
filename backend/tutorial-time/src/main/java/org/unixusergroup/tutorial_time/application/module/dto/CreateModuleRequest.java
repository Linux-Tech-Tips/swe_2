/**
 * @author Ethan
 */
package org.unixusergroup.tutorial_time.application.module.dto;

import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;
import org.unixusergroup.tutorial_time.domain.student.Student;

import java.util.List;


/** CreateModuleUseCase  Request record
 *
 * @param moduleCode The module code associated with the module
 * @param studentIds A list of student ids that are enrolled in this module
 * @param taStudentIds A list of TA student ids that associated with this module
 * @param lecturerId The staffId of the lecturer teaching this module.
 */
public record CreateModuleRequest(String moduleCode, List<Long> studentIds, List<Long> taStudentIds, long lecturerId) {
}
