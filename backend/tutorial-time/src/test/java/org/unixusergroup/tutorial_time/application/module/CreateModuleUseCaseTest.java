package org.unixusergroup.tutorial_time.application.module;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.unixusergroup.tutorial_time.application.module.dto.ModuleRequest;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.module.Module;

import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.timetable.Availability;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryModuleRepository;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CreateModuleUseCaseTest {
    private final CreateModuleUseCase testCreateModuleUseCase = new CreateModuleUseCase();
    private ModuleRequest moduleRequest;
    private Lecturer lecturer;
    private final InMemoryModuleRepository testModuleRepository = new InMemoryModuleRepository();

    @BeforeEach
    void setup() {
        ArrayList<Student> students = new ArrayList<>();
        lecturer = new Lecturer("Johnny Test", "j.test1@nuigalway.ie", 20000, new Timetable(2, 2, Availability.UNAVAILABLE));
        moduleRequest = new ModuleRequest("CT2021", new ArrayList<>(), new ArrayList<>(), lecturer, testModuleRepository);

    }

    @Test
    void testModuleCreationWithNullLecturer() {
        moduleRequest = new ModuleRequest("CT2021", new ArrayList<>(), new ArrayList<>(), null, testModuleRepository);
        assertThrows(RuntimeException.class, () -> testCreateModuleUseCase.execute(moduleRequest));
    }

    /**
     * This is running incorrectly at the moment of me writing this. 3:47am
     */
    @Test
    void testModuleCreationWithNullModuleCode() {
        moduleRequest = new ModuleRequest("CT2021", new ArrayList<>(), new ArrayList<>(), lecturer, testModuleRepository);
        assertThrows(RuntimeException.class, () -> testCreateModuleUseCase.execute(moduleRequest));
    }

    @Test
    void testValidModuleCreation() {
        Module module = new Module("CT2021", lecturer);
        assertEquals(module, testCreateModuleUseCase.execute(moduleRequest).getModule());
    }
}
