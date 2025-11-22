package org.unixusergroup.tutorial_time.application.module;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.unixusergroup.tutorial_time.application.common.exceptions.ModuleNotFoundException;
import org.unixusergroup.tutorial_time.application.common.exceptions.StudentNotFoundException;
import org.unixusergroup.tutorial_time.application.module.dto.ModuleResponse;
import org.unixusergroup.tutorial_time.application.module.dto.UpdateEnrollmentStatusRequest;
import org.unixusergroup.tutorial_time.application.module.dto.UpdateEnrollmentStatusRequest.EnrollmentAction;
import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;

@Service
public class UpdateEnrollmentStatusService implements UpdateEnrollmentStatusUseCase {

    private final ModuleRepository moduleRepository;
    private final StudentRepository studentRepository;

    public UpdateEnrollmentStatusService(ModuleRepository moduleRepository, StudentRepository studentRepository) {
        this.moduleRepository = moduleRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public ModuleResponse execute(UpdateEnrollmentStatusRequest request) {
        
        Module module = moduleRepository.getModuleByCode(request.moduleCode());
        if (module == null) {
            throw new ModuleNotFoundException("Module not found: " + request.moduleCode());
        }

        Student student = studentRepository.getStudentById(request.studentId());
        if (student == null) {
            throw new StudentNotFoundException("Student not found: " + request.studentId());
        }

        if (request.action() == EnrollmentAction.ADD) {
            module.addStudentToModule(student);
        } else if (request.action() == EnrollmentAction.REMOVE) {
            module.removeStudentFromModule(student);
        }

        moduleRepository.saveModule(module);

        List<Long> studentIds = module.getStudents().stream()
                .map(Student::getUniversityId)
                .collect(Collectors.toList());

        return new ModuleResponse(module.getModuleCode(), studentIds);
    }
}