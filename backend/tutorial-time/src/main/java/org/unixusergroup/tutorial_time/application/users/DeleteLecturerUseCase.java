/**
 * @author Cedar
 */

package org.unixusergroup.tutorial_time.application.users;

import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.users.dto.DeleteLecturerRequest;
import org.unixusergroup.tutorial_time.application.users.dto.DeleteLecturerResponse;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;

public class DeleteLecturerUseCase implements UseCase<DeleteLecturerResponse, DeleteLecturerRequest> {

    private final LecturerRepository lecturerRepository;

    public DeleteLecturerUseCase(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
    }

    @Override
    public DeleteLecturerResponse execute(DeleteLecturerRequest request) {
        Lecturer lecturer = lecturerRepository.getLecturerById(request.staffId());
        if(lecturer == null) {
            throw new IllegalArgumentException("Given id does not correspond to a lecturer");
        }
        lecturerRepository.deleteLecturer(lecturer);
        return new DeleteLecturerResponse();
    }
}
