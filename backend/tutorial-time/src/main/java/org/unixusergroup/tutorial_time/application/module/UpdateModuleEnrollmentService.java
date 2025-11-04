package org.unixusergroup.tutorial_time.application.module;

import java.util.List;
import java.util.stream.Collectors;

import org.unixusergroup.tutorial_time.application.common.exceptions.ModuleNotFoundException; 
import org.unixusergroup.tutorial_time.application.common.exceptions.StudentNotFoundException;
import org.unixusergroup.tutorial_time.application.module.dto.ModuleResponse;
import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;

public class UpdateModuleEnrollmentService implements UpdateModuleEnrollmentUseCase {

    private final ModuleRepository moduleRepository;
    private final StudentRepository studentRepository;

    public UpdateModuleEnrollmentService(ModuleRepository moduleRepository, StudentRepository studentRepository) {
        this.moduleRepository = moduleRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public ModuleResponse execute(String moduleCode, List<Long> newStudentIds) {
        
        Module module = moduleRepository.getModuleByCode(moduleCode);
        if (module == null) {
            throw new ModuleNotFoundException("Module not found with code: " + moduleCode);
        }

        List<Student> newStudents = studentRepository.findStudentsByIds(newStudentIds);
        if (newStudents == null) {
            throw new StudentNotFoundException("Students not found");
        }

        module.replaceEnrolledStudents(newStudents);
        moduleRepository.saveModule(module);
        return mapToResponse(module);
    }

    private ModuleResponse mapToResponse(Module module) {
        List<Long> enrolledIds = module.getStudents().stream()
            .map(Student::getUniversityId)
            .collect(Collectors.toList());
            
        return new ModuleResponse(
            module.getModuleCode(), 
            enrolledIds 
        );
    }
}
