/**
 * @author Ethan
 */
package org.unixusergroup.tutorial_time.application.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.unixusergroup.tutorial_time.application.users.dto.GetStudentRequest;
import org.unixusergroup.tutorial_time.application.users.dto.GetStudentResponse;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryStudentRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GetStudentUseCaseTest {
    StudentRepository studentRepository = new InMemoryStudentRepository();
    Student student;

    @BeforeEach
    void setUp() {
        student = new Student("Susan Test", "s.test@nuigalway.ie", 20004);
        studentRepository.saveStudent(student);
    }

    @Test
    void validStudentRetrieval() {
        GetStudentRequest getStudentRequest = new GetStudentRequest(20004);
        GetStudentUseCase getStudentUseCase = new GetStudentUseCase(this.studentRepository);
        GetStudentResponse response = getStudentUseCase.execute(getStudentRequest);

        assertTrue(response.ok());
        assertEquals(student, response.student());
    }

    @Test
    void retrievingNonPresentStudent() {
        GetStudentRequest getStudentRequest = new GetStudentRequest(1020);
        GetStudentUseCase getStudentUseCase = new GetStudentUseCase(this.studentRepository);
        GetStudentResponse response = getStudentUseCase.execute(getStudentRequest);

        assertFalse(response.ok());
        assertNull(response.student());
    }
}
