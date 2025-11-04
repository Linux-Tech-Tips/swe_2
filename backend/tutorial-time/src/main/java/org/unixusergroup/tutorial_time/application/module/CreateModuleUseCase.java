/** 
 * @author Ethan, Martin
 */
package org.unixusergroup.tutorial_time.application.module;

import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;
import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.module.dto.CreateModuleRequest;
import org.unixusergroup.tutorial_time.application.module.dto.CreateModuleResponse;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;

import java.util.ArrayList;
import java.util.List;

public class CreateModuleUseCase implements UseCase<CreateModuleResponse, CreateModuleRequest> {

    /** The module repository associated with this use case. */
    private final ModuleRepository moduleRepository;
    private final StudentRepository studentRepository;
    private final LecturerRepository lecturerRepository;

    public CreateModuleUseCase(ModuleRepository moduleRepository, StudentRepository studentRepository, LecturerRepository lecturerRepository) {
        this.moduleRepository = moduleRepository;
        this.studentRepository = studentRepository;
        this.lecturerRepository = lecturerRepository;
    };

    /** Creates a module with the request params and saves it in ModuleRepository
     * while providing an appropriate response.*/
    @Override
    public CreateModuleResponse execute(CreateModuleRequest moduleRequest) {
        try {

            /** This is using the student ids from the module request if it's not empty to retrieve the students from the repository */
            List<Student> enrolledStudents = new ArrayList<Student>();
            if(!moduleRequest.studentIds().isEmpty()) {
                for(long studentId: moduleRequest.studentIds()) {
                    Student student = this.studentRepository.getStudentById(studentId);
                    enrolledStudents.add(student);
                }
            }
            /** This is using the ta student ids from the module request if it's not empty to retrieve the ta's from the repository */
            List<Student> taStudents = new ArrayList<Student>();
            if(!moduleRequest.taStudentIds().isEmpty()) {
                for(long taStudentId: moduleRequest.taStudentIds()) {
                    Student ta = this.studentRepository.getStudentById(taStudentId);
                    taStudents.add(ta);
                }
            }

            /** If lecturer doesn't exist in the database, the use case will throw an error which will lead to the response */
            Lecturer lecturer = lecturerRepository.getLecturerById(moduleRequest.lecturerId());

            /** Verifing that the module code is not empty */
            if (moduleRequest.moduleCode().isEmpty()) {
                throw new IllegalArgumentException("The provided module code is empty.");
            }
            if (lecturer == null) {
                throw new IllegalArgumentException("The provided lecturer id is not present in the repo.");
            }
            Module newModule = new Module(moduleRequest.moduleCode(), enrolledStudents, taStudents, lecturer);

            this.moduleRepository.saveModule(newModule);
            return new CreateModuleResponse();

        } catch (Exception e) {
            return new CreateModuleResponse(e.getMessage());
        }
    }
}
