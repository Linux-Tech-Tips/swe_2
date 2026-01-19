/**
 * @author Cedar
 */

package org.unixusergroup.tutorial_time.application.users;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.unixusergroup.tutorial_time.application.users.dto.AddStudentRequest;
import org.unixusergroup.tutorial_time.application.users.dto.DeleteStudentRequest;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryStudentRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class DeleteStudentUseCaseTest {
    StudentRepository studentRepository = new InMemoryStudentRepository();

    /** test removing a student */
    @Test
    public void validStudentRemovalTest() {
        AddStudentRequest addStudentRequest = new AddStudentRequest("Jonny Test", "j@nuigalway.ie", 2000);
        AddStudentUseCase addStudentUseCase = new AddStudentUseCase(this.studentRepository);
        addStudentUseCase.execute(addStudentRequest);

        DeleteStudentRequest deleteStudentRequest = new DeleteStudentRequest(2000L);
        DeleteStudentUseCase deleteStudentUseCase = new DeleteStudentUseCase(this.studentRepository);
        deleteStudentUseCase.execute(deleteStudentRequest);

        Assertions.assertNull(studentRepository.getStudentById(2000L));
    }

    /** test removing a student that doesn't exist */
    @Test
    public void removeFakeStudentTest() {
        DeleteStudentRequest deleteStudentRequest = new DeleteStudentRequest(2000L);
        DeleteStudentUseCase deleteStudentUseCase = new DeleteStudentUseCase(this.studentRepository);

        assertThrows(IllegalArgumentException.class, () -> deleteStudentUseCase.execute(deleteStudentRequest));
    }
}
