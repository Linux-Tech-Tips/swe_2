/**
 * @author Cedar
 */

package org.unixusergroup.tutorial_time.application.users;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.unixusergroup.tutorial_time.application.users.dto.AddLecturerRequest;
import org.unixusergroup.tutorial_time.application.users.dto.FlipEmailPermissionsRequest;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryLecturerRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FlipLecturerEmailPermissionsUseCaseTest {
    LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
    
    @Test
    public void validFlipLecturerEmailPermissionsTest() {
        AddLecturerRequest addLecturerRequest = new AddLecturerRequest("j@nuigalway.ie", 2000);
        AddLecturerUseCase addLecturerUseCase = new AddLecturerUseCase(this.lecturerRepository);
        addLecturerUseCase.execute(addLecturerRequest);

        FlipEmailPermissionsRequest flipEmailPermissionsRequest = new FlipEmailPermissionsRequest(2000);
        FlipLecturerEmailPermissionsUseCase flipLecturerEmailPermissionsUseCase = new FlipLecturerEmailPermissionsUseCase(lecturerRepository);
        flipLecturerEmailPermissionsUseCase.execute(flipEmailPermissionsRequest);

        Assertions.assertFalse(lecturerRepository.getLecturerById(2000).getEmailPermissions());
    }

    @Test
    public void invalidFlipLecturerEmailPermissionsTest() {
        FlipEmailPermissionsRequest flipEmailPermissionsRequest = new FlipEmailPermissionsRequest(2000);
        FlipLecturerEmailPermissionsUseCase flipLecturerEmailPermissionsUseCase = new FlipLecturerEmailPermissionsUseCase(lecturerRepository);
        assertThrows(IllegalArgumentException.class, () -> flipLecturerEmailPermissionsUseCase.execute(flipEmailPermissionsRequest));
    }
}
