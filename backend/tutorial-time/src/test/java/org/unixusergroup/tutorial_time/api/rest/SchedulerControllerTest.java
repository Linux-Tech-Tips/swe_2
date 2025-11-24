/**
 * @author Cedar
 * tests for the scheduler controller
 */

package org.unixusergroup.tutorial_time.api.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.unixusergroup.tutorial_time.application.scheduler.dto.ScheduleResponse;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.timetable.Availability;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryModuleRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryStudentRepository;
import static org.unixusergroup.tutorial_time.api.rest.MockAuthUtil.createMockJwtToken;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SchedulerControllerTest {
    int[] blockedTimes = {};
    SchedulerController schedulerController;

    /**
     * method for setting up the variables (scheduleController, moduleRepository, module)
     */
    @BeforeEach
    public void setVariables() {
        InMemoryModuleRepository memoryModuleRepository = new InMemoryModuleRepository();
        InMemoryStudentRepository memoryStudentRepository = new InMemoryStudentRepository();

        Lecturer testLecturer = new Lecturer("Professor Test", "test@universityofgalway.ie", 10000);
        Module testModule = new Module("testModule", testLecturer.getUniversityId());
        Student student1 = new Student("Student1", "s1@universityofgalway.ie", 1111, new Timetable(2, 2, Availability.UNAVAILABLE));
        student1.setAvailable(0,0);
        memoryStudentRepository.saveStudent(student1);
        testModule.addStudent(student1.getUniversityId());
        Student student2 = new Student("Student2", "s2@universityofgalway.ie", 2222, new Timetable(2, 2, Availability.UNAVAILABLE));
        student2.setAvailable(0,0);
        memoryStudentRepository.saveStudent(student2);
        testModule.addStudent(student2.getUniversityId());
        Student student3 = new Student("Student3", "s3@universityofgalway.ie", 3333, new Timetable(2, 2, Availability.UNAVAILABLE));
        student3.setAvailable(1,1);
        memoryStudentRepository.saveStudent(student3);
        testModule.addStudent(student3.getUniversityId());
        Student student4 = new Student("Student4", "s4@universityofgalway.ie", 4444, new Timetable(2, 2, Availability.UNAVAILABLE));
        student4.setAvailable(0,1);
        memoryStudentRepository.saveStudent(student4);
        testModule.addStudent(student4.getUniversityId());
        memoryModuleRepository.saveModule(testModule);

        schedulerController = new SchedulerController(memoryModuleRepository, memoryStudentRepository);
    }

    /**
     * test creating a schedulerController instance and calling its schedule method
     */
    @Test
    public void testSchedulerController() {
        String correctTimes = "1 students are available at Wednesday, 9:00\n1 students are available at Thursday, 9:00\n2 students are available at Monday, 9:00\n";
        ScheduleResponse correctResponse = new ScheduleResponse(correctTimes);
        assertEquals(correctResponse, schedulerController.schedule(createMockJwtToken("10000", "Lecturer"), "testModule", 0,0,blockedTimes).getBody());
    }

    @Test
    public void testSchedulerControllerAccess() {
        /* Test unauthorized with no ID */
        assertEquals(HttpStatusCode.valueOf(403), schedulerController.schedule(createMockJwtToken("", "Student"), "testModule", 1,1,blockedTimes).getStatusCode());
        /* Test unauthorized with invalid ID */
        assertEquals(HttpStatusCode.valueOf(403), schedulerController.schedule(createMockJwtToken("20000", "Student"), "testModule", 1,1,blockedTimes).getStatusCode());
    }

    /**
     * test that it appropriately handles exceptions
     */
    @Test
    public void testSchedulerControllerException() {
        assertEquals(HttpStatusCode.valueOf(400), schedulerController.schedule(createMockJwtToken("10000", "Lecturer"), "testModule", 1,1,blockedTimes).getStatusCode());
    }
}
