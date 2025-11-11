package org.unixusergroup.tutorial_time.application.users;

import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.users.dto.GetLecturerRequest;
import org.unixusergroup.tutorial_time.application.users.dto.GetLecturerResponse;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;

public class GetLecturerUseCase implements UseCase<GetLecturerResponse, GetLecturerRequest> {
    private final LecturerRepository lecturerRepository;

    public GetLecturerUseCase(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
    }

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
