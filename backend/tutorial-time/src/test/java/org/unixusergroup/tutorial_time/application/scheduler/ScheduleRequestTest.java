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
    private final ArrayList<Integer> blockedTimes = new ArrayList<>();
    private final String moduleCode = "";
    private static final InMemoryModuleRepository moduleRepository = new InMemoryModuleRepository();
    private final ScheduleRequest request = new ScheduleRequest(moduleCode, studentLimit, minTAs, blockedTimes, moduleRepository);


    @Test
    public void testConstraints() {
        Constraints testConstraints = new Constraints(studentLimit, minTAs, blockedTimes);
        Constraints requestConstraints = request.constraints();

        assertEquals(testConstraints.getStudentLimit(), requestConstraints.getStudentLimit());
        assertEquals(testConstraints.getBlockedTimes(), requestConstraints.getBlockedTimes());
    }

    @Test
    public void testConstraintsIllegalParameter() {
        assertThrows(RuntimeException.class, () -> new ScheduleRequest(moduleCode, -1, minTAs, blockedTimes, moduleRepository).constraints());
    }

    @Test
    public void testScheduler() {
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
        moduleRepository.saveModule(testModule);
        ScheduleRequest request = new ScheduleRequest("testModule", 0, 0, new ArrayList<>(), moduleRepository);

        int[] testTimeTable1 = {1,0,0,0};
        int[] testTimeTable2 = {1,0,0,0};
        int[] testTimeTable3 = {0,0,0,1};
        int[] testTimeTable4 = {0,0,1,0};

        Scheduler requestScheduler = request.scheduler();
        assertEquals(4, requestScheduler.getModuleSize());
        assertArrayEquals(testTimeTable1, requestScheduler.getTimetables().get(0));
        assertArrayEquals(testTimeTable2, requestScheduler.getTimetables().get(1));
        assertArrayEquals(testTimeTable3, requestScheduler.getTimetables().get(2));
        assertArrayEquals(testTimeTable4, requestScheduler.getTimetables().get(3));
        assertEquals(4, requestScheduler.getNumberOfTimeSlots());
    }

    @Test
    public void testSchedulerIllegalModuleCode() {
        assertThrows(RuntimeException.class, () -> new ScheduleRequest("fakeModule", 0, 0, new ArrayList<>(), moduleRepository).scheduler());
    }
}
