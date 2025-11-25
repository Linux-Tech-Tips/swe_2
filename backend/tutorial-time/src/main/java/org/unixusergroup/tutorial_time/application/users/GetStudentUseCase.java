package org.unixusergroup.tutorial_time.application.users;

import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.users.dto.GetStudentRequest;
import org.unixusergroup.tutorial_time.application.users.dto.GetStudentResponse;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
/**
 * Use case for retrieving a {@link Student} by their student ID.
 * <p>
 * This use case queries the {@link StudentRepository} to determine whether
 * a student exists for the provided ID. If the student is not found or an
 * unexpected error occurs, the response will contain an appropriate error
 * message instead of student data.
 */
public class GetStudentUseCase implements UseCase<GetStudentResponse, GetStudentRequest> {
    private final StudentRepository studentRepository;
    /**
     * Creates a new {@code GetStudentUseCase}.
     *
     * @param studentRepository the repository used to retrieve student information
     */
    public GetStudentUseCase(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    /**
     * Executes the use case to retrieve a student by ID.
     * <p>
     * If a student exists for the provided ID, a successful response is returned.
     * Otherwise, an error message is returned in the response.
     *
     * @param getStudentRequest the request containing the student ID
     * @return a {@link GetStudentResponse} containing the student data or an error message
     */
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
