/**
 * @author Martin
 */
package org.unixusergroup.tutorial_time.api.rest;

import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;
import org.unixusergroup.tutorial_time.domain.timetable.Availability;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.util.Arrays;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SchedulerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModuleRepository mockModuleRepository;

    @Autowired
    private StudentRepository mockStudentRepository;

    @Autowired
    private LecturerRepository mockLecturerRepository;

    @BeforeEach
    public void configure() {
        Lecturer lecturer = new Lecturer("Professor Lecturer", "professor.lecturer@universityofgalway.ie", 123456);
        mockLecturerRepository.saveLecturer(lecturer);
        /* Demo students with varying availabilities */
        Student student1 = new Student("John Doe", "j.doe1@universityofgalway.ie", 111111,
                new Timetable(Timetable.DEFAULT_COLUMNS, Timetable.DEFAULT_ROWS, Availability.UNAVAILABLE));
        student1.setAvailable(3, 0);
        student1.setAvailable(3, 1);
        Student student2 = new Student("Jane Doe", "j.doe2@universityofgalway.ie", 222222,
                new Timetable(Timetable.DEFAULT_COLUMNS, Timetable.DEFAULT_ROWS, Availability.UNAVAILABLE));
        student2.setAvailable(3, 0);
        student2.setAvailable(3, 1);
        student2.setAvailable(3, 2);
        Student student3 = new Student("Jon Doe", "j.doe3@universityofgalway.ie", 333333,
                new Timetable(Timetable.DEFAULT_COLUMNS, Timetable.DEFAULT_ROWS, Availability.UNAVAILABLE));
        student3.setAvailable(2, 1);
        student3.setAvailable(3, 1);
        Student student4 = new Student("Jo Doe", "j.doe4@universityofgalway.ie", 444444,
                new Timetable(Timetable.DEFAULT_COLUMNS, Timetable.DEFAULT_ROWS, Availability.UNAVAILABLE));
        student4.setAvailable(2, 1);
        student4.setAvailable(3, 1);

        mockStudentRepository.saveStudent(student1);
        mockStudentRepository.saveStudent(student2);
        mockStudentRepository.saveStudent(student3);
        mockStudentRepository.saveStudent(student4);

        /* Demo TA students */
        Student taStudent1 = new Student("Test Student", "t.student1@universityofgalway.ie", 888888,
                new Timetable(Timetable.DEFAULT_COLUMNS, Timetable.DEFAULT_ROWS, Availability.UNAVAILABLE));
        taStudent1.setAvailable(3, 0);
        taStudent1.setAvailable(2, 1);
        Student taStudent2 = new Student("Theodore Student", "t.student2@universityofgalway.ie", 999999,
                new Timetable(Timetable.DEFAULT_COLUMNS, Timetable.DEFAULT_ROWS, Availability.UNAVAILABLE));
        taStudent2.setAvailable(3, 0);
        taStudent2.setAvailable(2, 1);
        taStudent2.setAvailable(3, 1);

        mockStudentRepository.saveStudent(taStudent1);
        mockStudentRepository.saveStudent(taStudent2);

        /* Module with students and TAs */
        Module module = new Module("TS222", Arrays.asList(new Long[] {111111L, 222222L, 333333L, 444444L}),
                Arrays.asList(new Long[] {888888L, 999999L}), 123456);
        mockModuleRepository.saveModule(module);


        // WHAT TO HAVE:
        // 1:
        // N N N Y N
        // N N N Y N
        // N N N N N
        // ...
        //
        // 2:
        // N N N Y N
        // N N N Y N
        // N N N Y N
        // N N N N N
        // ...
        //
        // 3:
        // N N N N N
        // N N Y Y N
        // N N N N N
        // ...
        //
        // 4:
        // N N N N N
        // N N Y Y N
        // N N N N N
        // ...
        //
        // This leads to 3,1 being the optimal time for everyone
        //               3,0/3,1 and 2,1 being the optimal times when student limit is 2
        // TA:
        // 1:
        // N N N Y N
        // N N Y N N
        // N N N N N
        // ...
        //
        // 2:
        // N N N Y N
        // N N Y Y N
        // N N N N N
        // ...
        //
        // This leads to 3,1 and 2,1 being the optimal times when minimum TAs is 2,
        // or when the time 3,1 is blocked off
        // testing our constraints
    }

    @Test
    public void scheduleBaseIntegrationTest() {
        try {
            /* Check that scheduling a tutorial with no constraints runs correctly */
            mockMvc.perform(post("/api/scheduler/schedule")
                            .param("moduleCode", "TS222")
                            .param("studentLimit", "0")
                            .param("minTAs", "0")
                            .param("blockedTimes", "")
                            .with(jwt()))
                    .andExpect(status()
                            .isOk())
                    .andExpect(content()
                            .json("{\"times\":" +
                                    "\"All students are available at each of the following times:\n4 students are available at Thursday, 10:00\n\"}"));

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void scheduleStudentLimitIntegrationTest() {
        try {
            /* Check that scheduling a tutorial with a student limit of 2 runs correctly */
            mockMvc.perform(post("/api/scheduler/schedule")
                            .param("moduleCode", "TS222")
                            .param("studentLimit", "2")
                            .param("minTAs", "0")
                            .param("blockedTimes", "")
                            .with(jwt()))
                    .andExpect(status()
                            .isOk())
                    .andExpect(content()
                            .json("{\"times\":" +
                                    " \"The following two times cover all students:" + // have two sets of times here, as both are possible
                                    "\n2 students are available at Thursday, 9:00" +   // but for now this returns 500 with a NullPointerException
                                    "\n2 students are available at Wednesday, 10:00\nThe following two times cover all students:\n" +
                                    "2 students are available at Thursday, 9:00\n" +
                                    "2 students are available at Thursday, 10:00\n\"}"));

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void scheduleMinimumTAsIntegrationTest() {
        try {
            /* Check that scheduling a tutorial with a minimum of 2 TAs runs correctly */
            mockMvc.perform(post("/api/scheduler/schedule")
                            .param("moduleCode", "TS222")
                            .param("studentLimit", "0")
                            .param("minTAs", "2")
                            .param("blockedTimes", "")
                            .with(jwt()))
                    .andExpect(status()
                            .isOk())
                    .andExpect(content()
                            .json("{\"times\":" +
                                    " \"The following two times cover all students:" +
                                    "\n2 students are available at Thursday, 9:00" +
                                    "\n2 students are available at Wednesday, 10:00\n\"}"));

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void scheduleBlockedOffTimesIntegrationTest() {
        try {
            /* Check that scheduling a tutorial with times blocked off runs correctly */
            mockMvc.perform(post("/api/scheduler/schedule")
                            .param("moduleCode", "TS222")
                            .param("studentLimit", "0")
                            .param("minTAs", "0")
                            .param("blockedTimes", "8")
                            .with(jwt()))
                    .andExpect(status()
                            .isOk())
                    .andExpect(content()
                            .json("{\"times\":" +
                                    "\"The following two times cover all students:" +
                                    "\n2 students are available at Thursday, 9:00" +
                                    "\n2 students are available at Wednesday, 10:00\n\"}"));

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void scheduleInvalidIntegrationTest() {
        try {
            /* Check that scheduling tutorials for an invalid module code fails */
            mockMvc.perform(post("/api/scheduler/schedule")
                            .param("moduleCode", "TS111")
                            .param("studentLimit", "0")
                            .param("minTAs", "0")
                            .param("blockedTimes", "")
                            .with(jwt()))
                    .andExpect(status().is(400));

            /* Check that scheduling tutorials for a module with a non-existent student fails */
            Module demoModule = new Module("TS999", Arrays.asList(new Long[] {000000L}),
                    Arrays.asList(new Long[] {888888L}), 123456);
            mockModuleRepository.saveModule(demoModule);
            mockMvc.perform(post("/api/scheduler/schedule")
                            .param("moduleCode", "TS999")
                            .param("studentLimit", "0")
                            .param("minTAs", "0")
                            .param("blockedTimes", "")
                            .with(jwt()))
                    .andExpect(status().is(500));

            /* Check that scheduling tutorials with a negative student limit fails */
            mockMvc.perform(post("/api/scheduler/schedule")
                            .param("moduleCode", "TS222")
                            .param("studentLimit", "-2")
                            .param("minTAs", "0")
                            .param("blockedTimes", "")
                            .with(jwt()))
                    .andExpect(status().is(400));

            /* Check that scheduling tutorials with a negative TA minimum fails */
            mockMvc.perform(post("/api/scheduler/schedule")
                            .param("moduleCode", "TS222")
                            .param("studentLimit", "0")
                            .param("minTAs", "-1")
                            .param("blockedTimes", "")
                            .with(jwt()))
                    .andExpect(status().is(400));

            /* Check that scheduling tutorials with a mimimum TA higher than the total number of TAs fails */
            mockMvc.perform(post("/api/scheduler/schedule")
                            .param("moduleCode", "TS222")
                            .param("studentLimit", "0")
                            .param("minTAs", "3")
                            .param("blockedTimes", "")
                            .with(jwt()))
                    .andExpect(status().is(400));

            /* Check that providing incorrect params to the API call fails */
            mockMvc.perform(post("/api/scheduler/schedule")
                            .param("moduleCode", "TS222")
                            .param("minTAs", "0")
                            .param("blockedTimes", "")
                            .with(jwt()))
                    .andExpect(status().is(400));

        } catch(Exception e) {
            System.out.println(e);
        }
    }

}
