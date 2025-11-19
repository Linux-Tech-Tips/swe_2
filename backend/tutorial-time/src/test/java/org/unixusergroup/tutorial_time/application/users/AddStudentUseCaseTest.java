/**
 * @author Ethan
 */
package org.unixusergroup.tutorial_time.application.users;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.unixusergroup.tutorial_time.application.users.dto.AddStudentRequest;
import org.unixusergroup.tutorial_time.application.users.dto.AddStudentResponse;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryStudentRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AddStudentUseCaseTest {
    StudentRepository studentRepository = new InMemoryStudentRepository();

    @Test
    void validStudentAddition() {
        Student expectedStudent = new Student("Megan Test", "m.test@nuigalway.ie", 1000);
        AddStudentRequest addStudentRequest = new AddStudentRequest("Megan Test", "m.test@nuigalway.ie", 1000);
        AddStudentUseCase addStudentUseCase = new AddStudentUseCase(this.studentRepository);
        AddStudentResponse response = addStudentUseCase.execute(addStudentRequest);

        assertTrue(response.ok());
        assertEquals(expectedStudent, this.studentRepository.getStudentById(1000));
    }

    @Test
    void addingStudentWithNoName() {
        AddStudentRequest addStudentRequest = new AddStudentRequest("", "m.test@nuigalway.ie", 1000);
        AddStudentUseCase addStudentUseCase = new AddStudentUseCase(this.studentRepository);
        AddStudentResponse response = addStudentUseCase.execute(addStudentRequest);

        assertFalse(response.ok());
    }

    @Test
    void addingStudentWithNoEmail() {
        AddStudentRequest addStudentRequest = new AddStudentRequest("Megan Test", "", 1000);
        AddStudentUseCase addStudentUseCase = new AddStudentUseCase(this.studentRepository);
        AddStudentResponse response = addStudentUseCase.execute(addStudentRequest);

        assertFalse(response.ok());
    }
}
