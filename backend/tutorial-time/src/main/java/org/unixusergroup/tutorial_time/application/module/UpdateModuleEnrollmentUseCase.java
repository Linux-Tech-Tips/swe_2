/** 
 * @author Lilian
 */

package org.unixusergroup.tutorial_time.application.module;

import java.util.List;

import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;

public class UpdateModuleEnrollmentUseCase {
    
    private final ModuleRepository moduleRepository;
    private final StudentRepository studentRepository;

    public UpdateModuleEnrollmentUseCase(ModuleRepository moduleRepository, StudentRepository studentRepository) {
        this.moduleRepository = moduleRepository;
        this.studentRepository = studentRepository;
    }

    public void excecute(String moduleCode, List<Long> newStudentIds) {
        Module module = moduleRepository.getModuleByCode(moduleCode);

        if (module == null) {
            //throw new ModuleNotFoundException("Module not found: " + moduleCode);
        }

        List<Student> newStudents = studentRepository.findStudentsByIds(newStudentIds);

        if (newStudents == null) {
            //throw new StudentsNotFoundException("No students found for: " + moduleCode);
        }

        module.replaceEnrolledStudents(newStudents);

        moduleRepository.saveModule(module);
    }
}
