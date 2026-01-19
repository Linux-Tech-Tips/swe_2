/**
 * @author Cedar
 */
package org.unixusergroup.tutorial_time.application.module;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.unixusergroup.tutorial_time.application.module.dto.CreateModuleRequest;
import org.unixusergroup.tutorial_time.application.module.dto.DeleteModuleRequest;
import org.unixusergroup.tutorial_time.application.users.AddLecturerUseCase;
import org.unixusergroup.tutorial_time.application.users.dto.AddLecturerRequest;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryLecturerRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryModuleRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryStudentRepository;


@SpringBootTest
public class DeleteModuleUseCaseTest {
    ModuleRepository moduleRepository = new InMemoryModuleRepository();
    StudentRepository studentRepository = new InMemoryStudentRepository();
    LecturerRepository lecturerRepository = new InMemoryLecturerRepository();

    /** test removing a module */
    @Test
    public void validModuleRemovalTest() {
        CreateModuleRequest createModuleRequest = new CreateModuleRequest("MA180", new ArrayList<>(), new ArrayList<>(), 2000L);
        CreateModuleUseCase createModuleUseCase = new CreateModuleUseCase(this.moduleRepository, this.studentRepository, this.lecturerRepository);
        AddLecturerRequest addLecturerRequest = new AddLecturerRequest("Jonny Test", "j@nuigalway.ie", 2000);
        AddLecturerUseCase addLecturerUseCase = new AddLecturerUseCase(this.lecturerRepository);
        addLecturerUseCase.execute(addLecturerRequest);

        createModuleUseCase.execute(createModuleRequest);

        DeleteModuleRequest deleteModuleRequest = new DeleteModuleRequest("MA180");
        DeleteModuleUseCase deleteModuleUseCase = new DeleteModuleUseCase(this.moduleRepository);
        deleteModuleUseCase.execute(deleteModuleRequest);

        Assertions.assertNull(moduleRepository.getModuleByCode("MA180"));
    }

    /** test removing a module that doesn't exist */
    @Test
    public void removeFakeModuleTest() {
        DeleteModuleRequest deleteModuleRequest = new DeleteModuleRequest("MA180");
        DeleteModuleUseCase deleteModuleUseCase = new DeleteModuleUseCase(this.moduleRepository);

        assertThrows(IllegalArgumentException.class, () -> deleteModuleUseCase.execute(deleteModuleRequest));
    }
}
