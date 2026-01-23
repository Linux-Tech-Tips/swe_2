/**
 * @author Cedar
 */

package org.unixusergroup.tutorial_time.application.users;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.unixusergroup.tutorial_time.application.users.dto.AddStudentRequest;
import org.unixusergroup.tutorial_time.application.users.dto.FlipEmailPermissionsRequest;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryStudentRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FlipStudentEmailPermissionsUseCaseTest {
    StudentRepository studentRepository = new InMemoryStudentRepository();

    @Test
    public void validFlipStudentEmailPermissionsTest() {
        AddStudentRequest addStudentRequest = new AddStudentRequest("j@nuigalway.ie", 2000);
        AddStudentUseCase addStudentUseCase = new AddStudentUseCase(this.studentRepository);
        addStudentUseCase.execute(addStudentRequest);

        FlipEmailPermissionsRequest flipEmailPermissionsRequest = new FlipEmailPermissionsRequest(2000);
        FlipStudentEmailPermissionsUseCase flipStudentEmailPermissionsUseCase = new FlipStudentEmailPermissionsUseCase(studentRepository);
        flipStudentEmailPermissionsUseCase.execute(flipEmailPermissionsRequest);

        Assertions.assertFalse(studentRepository.getStudentById(2000).getEmailPermissions());
    }

    @Test
    public void invalidFlipStudentEmailPermissionsTest() {
        FlipEmailPermissionsRequest flipEmailPermissionsRequest = new FlipEmailPermissionsRequest(2000);
        FlipStudentEmailPermissionsUseCase flipStudentEmailPermissionsUseCase = new FlipStudentEmailPermissionsUseCase(studentRepository);
        assertThrows(IllegalArgumentException.class, () -> flipStudentEmailPermissionsUseCase.execute(flipEmailPermissionsRequest));
    }
}
