/**
 * @author Cedar
 */

package org.unixusergroup.tutorial_time.application.users;

import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.users.dto.FlipEmailPermissionsRequest;
import org.unixusergroup.tutorial_time.application.users.dto.FlipEmailPermissionsResponse;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;

public class FlipStudentEmailPermissionsUseCase implements UseCase <FlipEmailPermissionsResponse, FlipEmailPermissionsRequest> {
    private final StudentRepository studentRepository;

    public FlipStudentEmailPermissionsUseCase(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public FlipEmailPermissionsResponse execute(FlipEmailPermissionsRequest request) {
        Student student = studentRepository.getStudentById(request.userId());
        if(student==null) {
            System.out.println("the guy isn't real!!");
            throw new IllegalArgumentException("The student with id " + request.userId() + " could not be found");
        }
        student.switchEmailPermissions();
        System.out.println(student.getEmailPermissions());
        System.out.println(student.getUniversityId());
        return new FlipEmailPermissionsResponse();
    }
}
