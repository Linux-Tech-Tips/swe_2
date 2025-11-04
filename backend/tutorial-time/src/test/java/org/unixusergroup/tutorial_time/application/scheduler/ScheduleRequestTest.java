package org.unixusergroup.tutorial_time.application.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.unixusergroup.tutorial_time.application.scheduler.dto.ScheduleRequest;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.scheduler.Constraints;
import org.unixusergroup.tutorial_time.domain.scheduler.Scheduler;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.timetable.Availability;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryModuleRepository;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ScheduleRequestTest {
    private static int studentLimit;
    private static int minTAs;
    private final int[] blockedTimes = new int[5];
    private final String moduleCode = "";
    private static final InMemoryModuleRepository moduleRepository = new InMemoryModuleRepository();
    private final ScheduleRequest request = new ScheduleRequest(moduleCode, studentLimit, minTAs, blockedTimes, moduleRepository);


    @Test
    public void testConstraints() {
        Constraints testConstraints = new Constraints(studentLimit, minTAs, blockedTimes);
        Constraints requestConstraints = request.constraints();

        assertEquals(testConstraints.getStudentLimit(), requestConstraints.getStudentLimit());
        assertEquals(testConstraints.getBlockedTimes(), requestConstraints.getBlockedTimes());
        assertEquals(testConstraints.getMinTAs(), requestConstraints.getMinTAs());
    }

    @Test
    public void testConstraintsIllegalParameter() {
        assertThrows(RuntimeException.class, () -> new ScheduleRequest(moduleCode, -1, minTAs, blockedTimes, moduleRepository).constraints());
    }

    @Test
    public void testScheduler() {
        Module testModule = getModule();
        moduleRepository.saveModule(testModule);
        ScheduleRequest request = new ScheduleRequest("testModule", 0, 0, blockedTimes, moduleRepository);

        int[] testTimeTable1 = {1,0,0,0};
        int[] testTimeTable2 = {1,0,0,0};
        int[] testTimeTable3 = {0,0,0,1};
        int[] testTimeTable4 = {0,0,1,0};

        Scheduler requestScheduler = request.scheduler();
        assertEquals(4, requestScheduler.getModuleSize());
        assertArrayEquals(testTimeTable1, requestScheduler.getStudentTimetables().get(0));
        assertArrayEquals(testTimeTable2, requestScheduler.getStudentTimetables().get(1));
        assertArrayEquals(testTimeTable3, requestScheduler.getStudentTimetables().get(2));
        assertArrayEquals(testTimeTable4, requestScheduler.getStudentTimetables().get(3));
        assertEquals(4, requestScheduler.getNumberOfTimeSlots());
    }

    /**
     * test that it fetches TA timetables and loads them into the correct place
     */
    @Test
    public void testFetchingTATimetables() {
        Module testModule = getModule();
        Student ta1 = new Student("TA1", "ta1@university.ie", 1111, new Timetable(2, 2, Availability.AVAILABLE));
        ta1.setUnavailable(1,0);
        testModule.addTAStudent(ta1);
        Student ta2 = new Student("TA2", "ta2@university.ie", 2222, new Timetable(2, 2, Availability.AVAILABLE));
        ta2.setUnavailable(1,0);
        testModule.addTAStudent(ta2);
        moduleRepository.saveModule(testModule);
        ScheduleRequest request = new ScheduleRequest("testModule", 0, 1, blockedTimes, moduleRepository);
        Scheduler requestScheduler = request.scheduler();

        int[] taTimeTable1 = {1,0,1,1};
        int[] taTimeTable2 = {1,0,1,1};

        assertArrayEquals(taTimeTable1, requestScheduler.getTATimetables().get(0));
        assertArrayEquals(taTimeTable2, requestScheduler.getTATimetables().get(1));
    }

    /**
     * test that the request throws an exception when module code that doesn't link to any module is given
     */
    @Test
    public void testSchedulerIllegalModuleCode() {
        assertThrows(RuntimeException.class, () -> new ScheduleRequest("fakeModule", 0, 0, blockedTimes, moduleRepository).scheduler());
    }

    /**
     * method for setting up a test module
     * @return the test module
     */
    private static Module getModule() {
        Lecturer testLecturer = new Lecturer("Professor Test", "test@universityofgalway.ie", 00000);
        Module testModule = new Module("testModule", testLecturer);
        Student student1 = new Student("Student1", "s1@university.ie", 1111, new Timetable(2, 2, Availability.UNAVAILABLE));
        student1.setAvailable(0,0);
        testModule.addStudent(student1);
        Student student2 = new Student("Student2", "s2@university.ie", 2222, new Timetable(2, 2, Availability.UNAVAILABLE));
        student2.setAvailable(0,0);
        testModule.addStudent(student2);
        Student student3 = new Student("Student3", "s3@university.ie", 3333, new Timetable(2, 2, Availability.UNAVAILABLE));
        student3.setAvailable(1,1);
        testModule.addStudent(student3);
        Student student4 = new Student("Student4", "s4@university.ie", 4444, new Timetable(2, 2, Availability.UNAVAILABLE));
        student4.setAvailable(0,1);
        testModule.addStudent(student4);
        return testModule;
    }
}
