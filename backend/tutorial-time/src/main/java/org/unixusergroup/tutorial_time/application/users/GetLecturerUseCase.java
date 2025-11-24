package org.unixusergroup.tutorial_time.application.users;

import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.users.dto.GetLecturerRequest;
import org.unixusergroup.tutorial_time.application.users.dto.GetLecturerResponse;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;

/**
 * Use case for retrieving a {@link Lecturer} based on their staff ID.
 * <p>
 * This use case looks up the lecturer in the {@link LecturerRepository}
 * and verifies that the ID corresponds to an existing lecturer.
 * If the lecturer does not exist or another error occurs, an error
 * message is returned in the response object.
 */
public class GetLecturerUseCase implements UseCase<GetLecturerResponse, GetLecturerRequest> {
    private final LecturerRepository lecturerRepository;
    /**
     * Creates a new {@code GetLecturerUseCase}.
     *
     * @param lecturerRepository the repository used to look up lecturer information
     */
    public GetLecturerUseCase(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
    }
    /**
     * Executes the use case to retrieve lecturer information.
     * <p>
     * The lecturer is searched using the staff ID provided in the request.
     * If no lecturer exists with the given ID, the response will contain
     * an appropriate error message.
     *
     * @param getLecturerRequest the request containing the lecturer's staff ID
     * @return a {@link GetLecturerResponse} containing the lecturer data or an error message
     */
    @Override
    public GetLecturerResponse execute(GetLecturerRequest getLecturerRequest) {
        try {
            Lecturer lecturer = this.lecturerRepository.getLecturerById(getLecturerRequest.staffId());
            if (lecturer == null) {
                throw new IllegalArgumentException("The provided staff ID is not registered.");
            }
            return new GetLecturerResponse(lecturer);

        } catch (Exception e) {
            return new GetLecturerResponse(e.getMessage());
        }
    }
}
