/**
 *
 * @author Ethan
 */
package org.unixusergroup.tutorial_time.application.users;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.unixusergroup.tutorial_time.application.users.dto.AddLecturerRequest;
import org.unixusergroup.tutorial_time.application.users.dto.AddLecturerResponse;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryLecturerRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AddLecturerUseCaseTest {
    LecturerRepository lecturerRepository = new InMemoryLecturerRepository();

    @Test
    void validLecturerAddition() {
        Lecturer expectedLecturer = new Lecturer("Jonny Test", "j@test.com", 2000);
        AddLecturerRequest addLecturerRequest = new AddLecturerRequest("Jonny Test", "j@test.com", 2000);
        AddLecturerUseCase addLecturerUseCase = new AddLecturerUseCase(this.lecturerRepository);
        AddLecturerResponse response = addLecturerUseCase.execute(addLecturerRequest);

        assertTrue(response.ok());
        assertEquals(expectedLecturer, this.lecturerRepository.getLecturerById(2000));
    }

    @Test
    void addingLecturerWithNoName() {
        AddLecturerRequest addLecturerRequest = new AddLecturerRequest("", "j@test.com", 2000);
        AddLecturerUseCase addLecturerUseCase = new AddLecturerUseCase(this.lecturerRepository);
        AddLecturerResponse response = addLecturerUseCase.execute(addLecturerRequest);

        assertFalse(response.ok());
    }

    @Test
    void addingLecturersWithNoEmail() {
        AddLecturerRequest addLecturerRequest = new AddLecturerRequest("Jonny Test", "", 2000);
        AddLecturerUseCase addLecturerUseCase = new AddLecturerUseCase(this.lecturerRepository);
        AddLecturerResponse response = addLecturerUseCase.execute(addLecturerRequest);

        assertFalse(response.ok());
    }
}
