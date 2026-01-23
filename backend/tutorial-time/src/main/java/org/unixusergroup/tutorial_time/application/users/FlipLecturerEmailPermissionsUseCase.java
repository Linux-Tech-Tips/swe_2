/**
 * @author Cedar
 */

package org.unixusergroup.tutorial_time.application.users;

import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.users.dto.FlipEmailPermissionsRequest;
import org.unixusergroup.tutorial_time.application.users.dto.FlipEmailPermissionsResponse;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;

public class FlipLecturerEmailPermissionsUseCase implements UseCase <FlipEmailPermissionsResponse, FlipEmailPermissionsRequest> {
    private final LecturerRepository lecturerRepository;

    public FlipLecturerEmailPermissionsUseCase(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
    }

    public FlipEmailPermissionsResponse execute(FlipEmailPermissionsRequest request) {
        Lecturer lecturer = lecturerRepository.getLecturerById(request.userId());
        if(lecturer==null) {
            throw new IllegalArgumentException("The lecturer with id " + request.userId() + " could not be found");
        }
        lecturer.switchEmailPermissions();
        return new FlipEmailPermissionsResponse();
    }
}
