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

    @Override
    public AddLecturerResponse execute(AddLecturerRequest addLecturerRequest) {
        try {
            if (addLecturerRequest.email().isEmpty()) {
                throw new IllegalArgumentException("The provided email is empty.");
            }
            Lecturer lecturer = new Lecturer(addLecturerRequest.email(), addLecturerRequest.staffId());
            this.lecturerRepository.saveLecturer(lecturer);
            return new AddLecturerResponse();
        } catch (Exception e) {
            return new AddLecturerResponse(e.getMessage());
        }
    }
}
