/**
 * @author Cedar
 */

package org.unixusergroup.tutorial_time.application.users;

import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.users.dto.DeleteStudentRequest;
import org.unixusergroup.tutorial_time.application.users.dto.DeleteStudentResponse;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;

public class DeleteStudentUseCase implements UseCase<DeleteStudentResponse, DeleteStudentRequest> {

    private final StudentRepository studentRepository;

    public DeleteStudentUseCase(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public DeleteStudentResponse execute(DeleteStudentRequest request) {
        Student student = studentRepository.getStudentById(request.studentId());
        if(student == null) {
            throw new IllegalArgumentException("Given id does not correspond to a student");
        }
        studentRepository.deleteStudent(student);
        return new DeleteStudentResponse();
    }
}

