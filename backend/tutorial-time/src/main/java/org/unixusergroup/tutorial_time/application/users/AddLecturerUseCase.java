package org.unixusergroup.tutorial_time.application.users;

import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.users.dto.AddLecturerRequest;
import org.unixusergroup.tutorial_time.application.users.dto.AddLecturerResponse;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;

public class AddLecturerUseCase implements UseCase<AddLecturerResponse, AddLecturerRequest> {
    private final LecturerRepository lecturerRepository;

    public AddLecturerUseCase(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
    }

    /**
     * Executes the use case to add a new lecturer.
     * <p>
     * Validation ensures that:
     * <ul>
     *     <li>the lecture's name is not empty</li>
     *     <li>the lecturer's email is not empty</li>
     * </ul>
     * If validation passes, a new {@link Lecturer} is created and saved.
     * <p>
     * If an exception occurs during validation or saving, the response
     * will contain the associated error message.
     *
     * @param addLecturerRequest the request containing lecturer details
     * @return an {@link AddLecturerResponse} indicating success or containing an error message
     */
    @Override
    public AddLecturerResponse execute(AddLecturerRequest addLecturerRequest) {
        try {
            if (addLecturerRequest.name().isEmpty()) {
                throw new IllegalArgumentException("The provided name for the lecturer is invalid.");
            }
            if (addLecturerRequest.email().isEmpty()) {
                throw new IllegalArgumentException("The provided email is empty.");
            }
            Lecturer lecturer = new Lecturer(addLecturerRequest.name(), addLecturerRequest.email(), addLecturerRequest.staffId());
            this.lecturerRepository.saveLecturer(lecturer);
            return new AddLecturerResponse();
        } catch (Exception e) {
            return new AddLecturerResponse(e.getMessage());
        }
    }
}
