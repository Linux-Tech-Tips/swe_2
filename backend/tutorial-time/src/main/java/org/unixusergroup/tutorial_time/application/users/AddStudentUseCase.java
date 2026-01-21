/**
 * @author Ethan
 */
package org.unixusergroup.tutorial_time.application.users;

import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.users.dto.AddStudentRequest;
import org.unixusergroup.tutorial_time.application.users.dto.AddStudentResponse;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;

public class AddStudentUseCase implements UseCase<AddStudentResponse, AddStudentRequest> {
    private final StudentRepository studentRepository;

    public AddStudentUseCase(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public AddStudentResponse execute(AddStudentRequest addStudentRequest) {
        try {
            if (addStudentRequest.email().isEmpty()) {
                throw new IllegalArgumentException("The provided email is empty.");
            }
            Student student = new Student(addStudentRequest.email(), addStudentRequest.studentId());
            this.studentRepository.saveStudent(student);
            return new AddStudentResponse();

        } catch (Exception e) {
            return new AddStudentResponse(e.getMessage());
        }
    }
}
