package org.unixusergroup.tutorial_time.application.users;

import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.users.dto.GetStudentRequest;
import org.unixusergroup.tutorial_time.application.users.dto.GetStudentResponse;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;

public class GetStudentUseCase implements UseCase<GetStudentResponse, GetStudentRequest> {
    private final StudentRepository studentRepository;

    public GetStudentUseCase(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public GetStudentResponse execute(GetStudentRequest getStudentRequest) {
        try {
            Student student = this.studentRepository.getStudentById(getStudentRequest.studentId());
            if (student == null) {
                throw new IllegalArgumentException("The provided student id is not found");
            }

            return new GetStudentResponse(student);
        } catch (Exception e) {
            return new GetStudentResponse(e.getMessage());
        }
    }
}
