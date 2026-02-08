/**
 * @author Cedar
 */

package org.unixusergroup.tutorial_time.application.scheduler;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.unixusergroup.tutorial_time.application.scheduler.dto.ScheduleRequest;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.scheduler.Constraints;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.timetable.Availability;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryModuleRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryStudentRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ScheduleTutorialsUseCaseTest {
    private static final InMemoryModuleRepository moduleRepository = new InMemoryModuleRepository();
    private static final InMemoryStudentRepository studentRepository = new InMemoryStudentRepository();
    ScheduleRequest request;
    ScheduleTutorialsUseCase testUseCase = new ScheduleTutorialsUseCase(moduleRepository, studentRepository);
    int[] blockedTimes = {};
    Module testModule;

    /**
     * method for initialising variables
     */
    @BeforeEach
    public void setVariables() {
        Lecturer testLecturer = new Lecturer("test@universityofgalway.ie", 10000);
        testModule = new Module("testModule", testLecturer.getUniversityId());
        Student student1 = new Student("s1@universityofgalway.ie", 1111, new Timetable(2, 2, Availability.UNAVAILABLE));
        student1.setAvailable(0,0);
        studentRepository.saveStudent(student1);
        testModule.addStudent(student1.getUniversityId());
        Student student2 = new Student("s2@universityofgalway.ie", 2222, new Timetable(2, 2, Availability.UNAVAILABLE));
        student2.setAvailable(0,0);
        studentRepository.saveStudent(student2);
        testModule.addStudent(student2.getUniversityId());
        Student student3 = new Student("s3@universityofgalway.ie", 3333, new Timetable(2, 2, Availability.UNAVAILABLE));
        student3.setAvailable(1,1);
        studentRepository.saveStudent(student3);
        testModule.addStudent(student3.getUniversityId());
        Student student4 = new Student("s4@universityofgalway.ie", 4444, new Timetable(2, 2, Availability.UNAVAILABLE));
        student4.setAvailable(0,1);
        studentRepository.saveStudent(student4);
        testModule.addStudent(student4.getUniversityId());
        moduleRepository.saveModule(testModule);
        request = new ScheduleRequest("testModule", 0, 0, blockedTimes);
    }

    /**
     * test to see if the scheduler schedules the correct times
     */
    @Test
    public void scheduleTest() {
        String correctTimes = "1 students are available at Wednesday, 9:00\n1 students are available at Thursday, 9:00\n2 students are available at Monday, 9:00\n";
        assertEquals(correctTimes, testUseCase.execute(request).getTimes());
    }

    /**
     * test that it runs properly with a student who is always busy
     * it should still schedule times and append the error
     */
    @Test
    public void testBusyStudent() {
        Student badStudent = new Student("s5@universityofgalway.ie", 5555, new Timetable(2, 2, Availability.UNAVAILABLE));
        testModule.addStudent(5555);
        studentRepository.saveStudent(badStudent);
        request = new ScheduleRequest("testModule", 0, 0, blockedTimes);
        String correctTimes = "WARNING: There are (1) students who registered themselves as busy at all times. These students have been removed from consideration.\n1 students are available at Wednesday, 9:00\n1 students are available at Thursday, 9:00\n2 students are available at Monday, 9:00\n";
        assertEquals(correctTimes, testUseCase.execute(request).getTimes());
    }

    /**
     * test that the useCase throws a runtimeException when given one or more bad times to block off
     */
    @Test
    public void impossibleBlockedTimesTest() {
        int[] badTimes = {2};
        request = new ScheduleRequest("testModule", 0, 0, badTimes);
        assertThrows(RuntimeException.class, () -> testUseCase.execute(request));
    }

    /**
     * test that the useCase throws a runtimeException when given a bad student limit
     */
    @Test
    public void impossibleStudentLimitTest() {
        request = new ScheduleRequest("testModule", 1, 0, blockedTimes);
        assertThrows(RuntimeException.class, () -> testUseCase.execute(request));
    }

    /**
     * test that the useCase throws a runtimeException when given a bad TA minimum
     */
    @Test
    public void impossibleTAMinimum() {
        Lecturer testLecturer = new Lecturer("test@universityofgalway.ie", 10000);
        Module testModule = new Module("testModule", testLecturer.getUniversityId());
        Student student1 = new Student("s1@universityofgalway.ie", 1111, new Timetable(2, 2, Availability.UNAVAILABLE));
        student1.setAvailable(0,0);
        studentRepository.saveStudent(student1);
        testModule.addStudent(student1.getUniversityId());
        Student student2 = new Student("s2@universityofgalway.ie", 2222, new Timetable(2, 2, Availability.UNAVAILABLE));
        student2.setAvailable(0,0);
        testModule.addStudent(student2.getUniversityId());
        studentRepository.saveStudent(student2);
        Student student3 = new Student("s3@universityofgalway.ie", 3333, new Timetable(2, 2, Availability.UNAVAILABLE));
        student3.setAvailable(1,1);
        studentRepository.saveStudent(student3);
        testModule.addStudent(student3.getUniversityId());
        Student student4 = new Student("s4@universityofgalway.ie", 4444, new Timetable(2, 2, Availability.UNAVAILABLE));
        student4.setAvailable(0,1);
        studentRepository.saveStudent(student4);
        testModule.addStudent(student4.getUniversityId());
        Student ta1 = new Student("ta1@universityofgalway.ie", 1111, new Timetable(2, 2, Availability.AVAILABLE));
        ta1.setUnavailable(0,0);
        studentRepository.saveStudent(ta1);
        testModule.addTAStudent(ta1.getUniversityId());
        moduleRepository.saveModule(testModule);
        request = new ScheduleRequest("testModule", 1, 2, blockedTimes);
        assertThrows(RuntimeException.class, () -> testUseCase.execute(request));
    }

    /**
     * test that the request throws an exception when module code that doesn't link to any module is given
     */
    @Test
    public void testSchedulerIllegalModuleCode() {
        request = new ScheduleRequest("fakeModule", 1, 2, blockedTimes);
        assertThrows(IllegalArgumentException.class, () -> testUseCase.execute(request));
    }


    @Test
    public void testConstraintsIllegalParameter() {
        request = new ScheduleRequest("testModule", -1, 0, blockedTimes);
        assertThrows(RuntimeException.class, () -> testUseCase.execute(request));
    }
}
