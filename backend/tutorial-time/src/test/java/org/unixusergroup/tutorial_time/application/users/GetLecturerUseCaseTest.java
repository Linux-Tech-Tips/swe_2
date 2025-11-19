/**
 * @author Ethan
 */
package org.unixusergroup.tutorial_time.application.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.unixusergroup.tutorial_time.application.users.dto.GetLecturerRequest;
import org.unixusergroup.tutorial_time.application.users.dto.GetLecturerResponse;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryLecturerRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GetLecturerUseCaseTest {
    LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
    Lecturer lecturer;
    @BeforeEach
    void setUp() {
       lecturer = new Lecturer("Johnny Test", "j.test1@nuigalway.ie", 20000);
       lecturerRepository.saveLecturer(lecturer);
    }

    @Test
    void validLecturerRetrieval() {
        GetLecturerRequest getLecturerRequest = new GetLecturerRequest(20000);
        GetLecturerUseCase getLecturerUseCase = new GetLecturerUseCase(this.lecturerRepository);
        GetLecturerResponse response = getLecturerUseCase.execute(getLecturerRequest);

        assertTrue(response.ok());
        assertEquals(lecturer, response.lecturer());
    }

    @Test
    void retrievingNonPresentLecturer() {
        GetLecturerRequest getLecturerRequest = new GetLecturerRequest(1200);
        GetLecturerUseCase getLecturerUseCase = new GetLecturerUseCase(this.lecturerRepository);
        GetLecturerResponse response = getLecturerUseCase.execute(getLecturerRequest);

        assertFalse(response.ok());
        assertNull(response.lecturer());
    }
}
