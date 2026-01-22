/**
 * @author Cedar
 */

package org.unixusergroup.tutorial_time.application.users;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.unixusergroup.tutorial_time.application.module.CreateModuleUseCase;
import org.unixusergroup.tutorial_time.application.module.dto.CreateModuleRequest;
import org.unixusergroup.tutorial_time.application.users.dto.AddLecturerRequest;
import org.unixusergroup.tutorial_time.application.users.dto.DeleteLecturerRequest;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryLecturerRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryModuleRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryStudentRepository;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class DeleteLecturerUseCaseTest {
    LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
    ModuleRepository moduleRepository = new InMemoryModuleRepository();
    StudentRepository studentRepository = new InMemoryStudentRepository();

    /** test removing a lecturer */
    @Test
    public void validLecturerRemovalTest() {
        AddLecturerRequest addLecturerRequest = new AddLecturerRequest("j.test@nuigalway.ie", 2000);
        AddLecturerUseCase addLecturerUseCase = new AddLecturerUseCase(this.lecturerRepository);
        addLecturerUseCase.execute(addLecturerRequest);

        CreateModuleRequest createModuleRequest = new CreateModuleRequest("MA180", new ArrayList<>(), new ArrayList<>(), 2000L);
        CreateModuleUseCase createModuleUseCase = new CreateModuleUseCase(this.moduleRepository, this.studentRepository, this.lecturerRepository);
        createModuleUseCase.execute(createModuleRequest);

        DeleteLecturerRequest deleteLecturerRequest = new DeleteLecturerRequest(2000L);
        DeleteLecturerUseCase deleteLecturerUseCase = new DeleteLecturerUseCase(this.lecturerRepository, this.moduleRepository);
        deleteLecturerUseCase.execute(deleteLecturerRequest);

        Assertions.assertNull(lecturerRepository.getLecturerById(2000L));
        Assertions.assertNull(moduleRepository.getModuleByCode("MA180"));
    }

    /** test removing a lecturer that doesn't exist */
    @Test
    public void removeFakeLecturerTest() {
        DeleteLecturerRequest deleteLecturerRequest = new DeleteLecturerRequest(2000L);
        DeleteLecturerUseCase deleteLecturerUseCase = new DeleteLecturerUseCase(this.lecturerRepository, this.moduleRepository);

        assertThrows(IllegalArgumentException.class, () -> deleteLecturerUseCase.execute(deleteLecturerRequest));
    }
}
