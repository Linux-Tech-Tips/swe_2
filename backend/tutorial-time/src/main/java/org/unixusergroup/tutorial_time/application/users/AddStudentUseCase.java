/**
 * @author Ethan
 */
package org.unixusergroup.tutorial_time.application.users;

import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.users.dto.AddStudentRequest;
import org.unixusergroup.tutorial_time.application.users.dto.AddStudentResponse;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;

/**
 * Use case for adding a new {@link Student} to the system.
 * <p>
 * This use case validates the student's name and email, constructs a
 * new student entity, and persists it using the provided
 * {@link StudentRepository}. If validation fails or the operation
 * encounters an error, the error message is returned in the response.
 *
 */
public class AddStudentUseCase implements UseCase<AddStudentResponse, AddStudentRequest> {
    private final StudentRepository studentRepository;
    /**
     * Creates a new {@code AddStudentUseCase}.
     *
     * @param studentRepository the repository used to store student information
     */
    public AddStudentUseCase(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    /**
     * Executes the use case to add a new student.
     * <p>
     * Validation ensures:
     * <ul>
     *     <li>the student's name is not empty</li>
     *     <li>the student's email is not empty</li>
     * </ul>
     * If the data is valid, a new {@link Student} is created and saved.
     * <p>
     * If any exception occurs during validation or persistence, the
     * response will contain the associated error message.
     *
     * @param addStudentRequest the request containing student details
     * @return an {@link AddStudentResponse} indicating success or containing an error message
     */
    @Override
    public AddStudentResponse execute(AddStudentRequest addStudentRequest) {
        try {
            if(addStudentRequest.name().isEmpty()) {
                throw new IllegalArgumentException("The provided name for the student is invalid.");
            }
            if (addStudentRequest.email().isEmpty()) {
                throw new IllegalArgumentException("The provided email is empty.");
            }
            Student student = new Student(addStudentRequest.name(), addStudentRequest.email(), addStudentRequest.studentId());
            this.studentRepository.saveStudent(student);
            return new AddStudentResponse();

        } catch (Exception e) {
            return new AddStudentResponse(e.getMessage());
        }
    }
}
