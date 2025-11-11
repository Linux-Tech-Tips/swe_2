/**
 * @author Ethan
 */

package org.unixusergroup.tutorial_time.application.module;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.unixusergroup.tutorial_time.application.module.dto.CreateModuleRequest;
import org.unixusergroup.tutorial_time.application.module.dto.CreateModuleResponse;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.module.Module;

import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.timetable.Availability;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryLecturerRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryModuleRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryStudentRepository;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CreateModuleUseCaseTest {
    private CreateModuleRequest createModuleRequest;
    private Lecturer lecturer;
    private final InMemoryModuleRepository testModuleRepository = new InMemoryModuleRepository();
    private final InMemoryStudentRepository testStudentRepository = new InMemoryStudentRepository();
    private final InMemoryLecturerRepository testLecturerRepository = new InMemoryLecturerRepository();
    private final CreateModuleUseCase testCreateModuleUseCase = new CreateModuleUseCase(testModuleRepository, testStudentRepository, testLecturerRepository);

    @BeforeEach
    void setup() {
        lecturer = new Lecturer("Johnny Test", "j.test1@nuigalway.ie", 20000, new Timetable(2, 2, Availability.UNAVAILABLE));
        createModuleRequest = new CreateModuleRequest("CT2021", new ArrayList<>(), new ArrayList<>(), lecturer.getUniversityId());

    }

    /** This is a just a test case of creating a module with no lecturer */
    @Test
    void testModuleCreationWithInvalidLecturer() {
        createModuleRequest = new CreateModuleRequest("CT2021", new ArrayList<>(), new ArrayList<>(), 0);
        assertThrows(RuntimeException.class, () -> testCreateModuleUseCase.execute(createModuleRequest));
    }

    /** This is testing creating a module with no code */
    @Test
    void testModuleCreationWithNullModuleCode() {
        createModuleRequest = new CreateModuleRequest("", new ArrayList<>(), new ArrayList<>(), 20000);
        assertThrows(RuntimeException.class, () -> testCreateModuleUseCase.execute(createModuleRequest));
    }

    /** This is just a test case of correct module creation functionality. */
    @Test
    void testValidModuleCreation() {
        Module module = new Module("CT2021", 20000);
        testLecturerRepository.saveLecturer(lecturer);
        testCreateModuleUseCase.execute(createModuleRequest);
        Module generatedModule = testModuleRepository.getModuleByCode("CT2021");
        assertEquals(module, generatedModule);
    }

    /** This is testing creating a module with an invalid student ID */
    @Test
    void testModuleCreationWithInvalidStudent() {
        ArrayList<Long> studentIDs = new ArrayList<>();
        studentIDs.add(000L);
        createModuleRequest = new CreateModuleRequest("", studentIDs, new ArrayList<>(), 20000);
        assertThrows(RuntimeException.class, () -> testCreateModuleUseCase.execute(createModuleRequest));
    }

    /** This is testing creating an invalid TA ID */
    @Test
    void testModuleCreationWithInvalidTA() {
        ArrayList<Long> taIDs = new ArrayList<>();
        taIDs.add(000L);
        createModuleRequest = new CreateModuleRequest("", new ArrayList<>(), taIDs, 20000);
        assertThrows(RuntimeException.class, () -> testCreateModuleUseCase.execute(createModuleRequest));
    }
}
