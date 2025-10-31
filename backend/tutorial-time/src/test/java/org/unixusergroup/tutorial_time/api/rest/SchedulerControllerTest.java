/**
 * @author Cedar
 * tests for the scheduler controller
 */

package org.unixusergroup.tutorial_time.api.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.unixusergroup.tutorial_time.application.scheduler.dto.ScheduleResponse;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.timetable.Availability;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryModuleRepository;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SchedulerControllerTest {

    SchedulerController schedulerController;

    /**
     * method for setting up the variables (scheduleController, moduleRepository, module)
     */
    @BeforeEach
    public void setVariables() {
        InMemoryModuleRepository memoryModuleRepository = new InMemoryModuleRepository();
        schedulerController = new SchedulerController(memoryModuleRepository);

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
        memoryModuleRepository.saveModule(testModule);
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
        assertEquals(correctResponse, schedulerController.schedule("testModule", 0,0,new ArrayList<>()).getBody());
    }

    /**
     * test that it appropriately handles exceptions
     */
    @Test
    public void testSchedulerControllerException() {
        assertThrows(RuntimeException.class, () -> schedulerController.schedule("testModule", 1,0,new ArrayList<>()));
    }
}
