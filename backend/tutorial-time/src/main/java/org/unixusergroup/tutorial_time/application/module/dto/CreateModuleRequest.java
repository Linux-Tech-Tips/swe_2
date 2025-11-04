/**
 * @author Ethan
 */
package org.unixusergroup.tutorial_time.application.module.dto;

import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;
import org.unixusergroup.tutorial_time.domain.student.Student;

import java.util.List;


/** CreateModuleUseCase  Request record
 * A record to represent module related requests within the project.
 *
 * @param moduleCode The module code associated with the module
 * @param students A list of students that are enrolled in this module
 * @param taStudents A list of TA students that associated with this module
 * @param lecturer The lecturer teaching this module.
 */
public record CreateModuleRequest(String moduleCode, List<Student> students, List<Student> taStudents, Lecturer lecturer) {
}
