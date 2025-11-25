/**
 * @author Ethan.
 */
package org.unixusergroup.tutorial_time.application.module;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.unixusergroup.tutorial_time.application.module.dto.GetModuleRequest;
import org.unixusergroup.tutorial_time.application.module.dto.GetModuleResponse;
import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.timetable.Availability;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryModuleRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GetModuleUseCaseTest {
    private final InMemoryModuleRepository testModuleRepository = new InMemoryModuleRepository();
    private GetModuleUseCase getModuleUseCaseTest;
    private Module expectedModule;
    private final Lecturer lecturer = new Lecturer("Johnny Test", "j.test1@nuigalway.ie", 20000, new Timetable(2, 2, Availability.UNAVAILABLE));

    @BeforeEach
    void setup() {
        expectedModule = new Module("CT2020", 20000);
        testModuleRepository.saveModule(expectedModule);
        getModuleUseCaseTest = new GetModuleUseCase(testModuleRepository);
    }

    @Test
    void validModuleRetrieval() {
        GetModuleRequest request = new GetModuleRequest("CT2020", lecturer.getUniversityId());
        GetModuleResponse response = getModuleUseCaseTest.execute(request);
        assertEquals(expectedModule, response.module());
    }

    @Test
    void testRetrievingNonPresentModule() {
        GetModuleRequest request = new GetModuleRequest("CT402", lecturer.getUniversityId());
        assertThrows(RuntimeException.class, () -> getModuleUseCaseTest.execute(request));
    }

    @Test
    void testRetrievingModuleWithNullModuleCode() {
        GetModuleRequest request = new GetModuleRequest("", lecturer.getUniversityId());
        assertThrows(RuntimeException.class, () -> getModuleUseCaseTest.execute(request));
    }

    @Test
    void testRetrievingModuleWithIncorrectLecturerId() {
        GetModuleRequest request = new GetModuleRequest("CT402", 1111L);
        assertThrows(RuntimeException.class, () -> getModuleUseCaseTest.execute(request));
    }
}
