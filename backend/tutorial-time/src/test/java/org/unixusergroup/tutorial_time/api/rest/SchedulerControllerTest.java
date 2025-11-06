/**
 * @author Cedar
 * tests for the scheduler controller
 */

package org.unixusergroup.tutorial_time.api.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.unixusergroup.tutorial_time.application.scheduler.dto.ScheduleResponse;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.timetable.Availability;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryModuleRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryStudentRepository;

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

        Lecturer testLecturer = new Lecturer("Professor Test", "test@universityofgalway.ie", 00000);
        Module testModule = new Module("testModule", testLecturer.getUniversityId());
        Student student1 = new Student("Student1", "s1@university.ie", 1111, new Timetable(2, 2, Availability.UNAVAILABLE));
        student1.setAvailable(0,0);
        memoryStudentRepository.saveStudent(student1);
        testModule.addStudent(student1.getUniversityId());
        Student student2 = new Student("Student2", "s2@university.ie", 2222, new Timetable(2, 2, Availability.UNAVAILABLE));
        student2.setAvailable(0,0);
        memoryStudentRepository.saveStudent(student2);
        testModule.addStudent(student2.getUniversityId());
        Student student3 = new Student("Student3", "s3@university.ie", 3333, new Timetable(2, 2, Availability.UNAVAILABLE));
        student3.setAvailable(1,1);
        memoryStudentRepository.saveStudent(student3);
        testModule.addStudent(student3.getUniversityId());
        Student student4 = new Student("Student4", "s4@university.ie", 4444, new Timetable(2, 2, Availability.UNAVAILABLE));
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
        ArrayList<Integer> correctTimes = new ArrayList<>();
        correctTimes.add(2);
        correctTimes.add(3);
        correctTimes.add(0);
        ScheduleResponse correctResponse = new ScheduleResponse(correctTimes);
        assertEquals(correctResponse, schedulerController.schedule("testModule", 0,0,blockedTimes).getBody());
    }

    /**
     * test that it appropriately handles exceptions
     */
    @Test
    public void testSchedulerControllerException() {
        assertEquals(HttpStatusCode.valueOf(500), schedulerController.schedule("testModule", 1,1,blockedTimes).getStatusCode());
    }
}
