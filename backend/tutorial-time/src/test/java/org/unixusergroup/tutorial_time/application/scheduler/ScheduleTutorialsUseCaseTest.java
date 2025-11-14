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
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.timetable.Availability;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryModuleRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryStudentRepository;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ScheduleTutorialsUseCaseTest {
    private static final InMemoryModuleRepository moduleRepository = new InMemoryModuleRepository();
    private static final InMemoryStudentRepository studentRepository = new InMemoryStudentRepository();
    ScheduleRequest request;
    ScheduleTutorialsUseCase testUseCase = new ScheduleTutorialsUseCase();
    int[] blockedTimes = {};

    /**
     * method for initialising variables
     */
    @BeforeEach
    public void setVariables() {
        Lecturer testLecturer = new Lecturer("Professor Test", "test@universityofgalway.ie", 10000);
        Module testModule = new Module("testModule", testLecturer.getUniversityId());
        Student student1 = new Student("Student1", "s1@university.ie", 1111, new Timetable(2, 2, Availability.UNAVAILABLE));
        student1.setAvailable(0,0);
        studentRepository.saveStudent(student1);
        testModule.addStudent(student1.getUniversityId());
        Student student2 = new Student("Student2", "s2@university.ie", 2222, new Timetable(2, 2, Availability.UNAVAILABLE));
        student2.setAvailable(0,0);
        studentRepository.saveStudent(student2);
        testModule.addStudent(student2.getUniversityId());
        Student student3 = new Student("Student3", "s3@university.ie", 3333, new Timetable(2, 2, Availability.UNAVAILABLE));
        student3.setAvailable(1,1);
        studentRepository.saveStudent(student3);
        testModule.addStudent(student3.getUniversityId());
        Student student4 = new Student("Student4", "s4@university.ie", 4444, new Timetable(2, 2, Availability.UNAVAILABLE));
        student4.setAvailable(0,1);
        studentRepository.saveStudent(student4);
        testModule.addStudent(student4.getUniversityId());
        moduleRepository.saveModule(testModule);
        request = new ScheduleRequest("testModule", 0, 0, blockedTimes, moduleRepository, studentRepository);
    }

    /**
     * test to see if the scheduler schedules the correct times
     */
    @Test
    public void scheduleTest() {
        String correctTimes = "Wednesday, 9:00\nThursday, 9:00\nMonday, 9:00\n";
        assertEquals(correctTimes, testUseCase.execute(request).getTimes());
    }

    /**
     * test that the useCase throws a runtimeException when given one or more bad times to block off
     */
    @Test
    public void impossibleBlockedTimesTest() {
        int[] badTimes = {2};
        request = new ScheduleRequest("testModule", 0, 0, badTimes, moduleRepository, studentRepository);
        assertThrows(RuntimeException.class, () -> testUseCase.execute(request));
    }

    /**
     * test that the useCase throws a runtimeException when given a bad student limit
     */
    @Test
    public void impossibleStudentLimitTest() {
        request = new ScheduleRequest("testModule", 1, 0, blockedTimes, moduleRepository, studentRepository);
        assertThrows(RuntimeException.class, () -> testUseCase.execute(request));
    }

    /**
     * test that the useCase throws a runtimeException when given a bad TA minimum
     */
    @Test
    public void impossibleTAMinimum() {
        Lecturer testLecturer = new Lecturer("Professor Test", "test@universityofgalway.ie", 10000);
        Module testModule = new Module("testModule", testLecturer.getUniversityId());
        Student student1 = new Student("Student1", "s1@university.ie", 1111, new Timetable(2, 2, Availability.UNAVAILABLE));
        student1.setAvailable(0,0);
        studentRepository.saveStudent(student1);
        testModule.addStudent(student1.getUniversityId());
        Student student2 = new Student("Student2", "s2@university.ie", 2222, new Timetable(2, 2, Availability.UNAVAILABLE));
        student2.setAvailable(0,0);
        testModule.addStudent(student2.getUniversityId());
        studentRepository.saveStudent(student2);
        Student student3 = new Student("Student3", "s3@university.ie", 3333, new Timetable(2, 2, Availability.UNAVAILABLE));
        student3.setAvailable(1,1);
        studentRepository.saveStudent(student3);
        testModule.addStudent(student3.getUniversityId());
        Student student4 = new Student("Student4", "s4@university.ie", 4444, new Timetable(2, 2, Availability.UNAVAILABLE));
        student4.setAvailable(0,1);
        studentRepository.saveStudent(student4);
        testModule.addStudent(student4.getUniversityId());
        Student ta1 = new Student("TA1", "ta1@university.ie", 1111, new Timetable(2, 2, Availability.AVAILABLE));
        ta1.setUnavailable(0,0);
        studentRepository.saveStudent(ta1);
        testModule.addTAStudent(ta1.getUniversityId());
        moduleRepository.saveModule(testModule);
        request = new ScheduleRequest("testModule", 1, 2, blockedTimes, moduleRepository, studentRepository);
        assertThrows(RuntimeException.class, () -> testUseCase.execute(request));
    }
}
