/**
 * @author Cedar
 */

package org.unixusergroup.tutorial_time.application.users;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.unixusergroup.tutorial_time.application.users.dto.AddLecturerRequest;
import org.unixusergroup.tutorial_time.application.users.dto.DeleteLecturerRequest;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryLecturerRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class DeleteLecturerUseCaseTest {
    LecturerRepository lecturerRepository = new InMemoryLecturerRepository();

    /** test removing a lecturer */
    @Test
    public void validLecturerRemovalTest() {
        AddLecturerRequest addLecturerRequest = new AddLecturerRequest("Jonny Test", "j@nuigalway.ie", 2000);
        AddLecturerUseCase addLecturerUseCase = new AddLecturerUseCase(this.lecturerRepository);
        addLecturerUseCase.execute(addLecturerRequest);

        DeleteLecturerRequest deleteLecturerRequest = new DeleteLecturerRequest(2000L);
        DeleteLecturerUseCase deleteLecturerUseCase = new DeleteLecturerUseCase(this.lecturerRepository);
        deleteLecturerUseCase.execute(deleteLecturerRequest);

        Assertions.assertNull(lecturerRepository.getLecturerById(2000L));
    }

    /** test removing a lecturer that doesn't exist */
    @Test
    public void removeFakeLecturerTest() {
        DeleteLecturerRequest deleteLecturerRequest = new DeleteLecturerRequest(2000L);
        DeleteLecturerUseCase deleteLecturerUseCase = new DeleteLecturerUseCase(this.lecturerRepository);

        assertThrows(IllegalArgumentException.class, () -> deleteLecturerUseCase.execute(deleteLecturerRequest));
    }
}
