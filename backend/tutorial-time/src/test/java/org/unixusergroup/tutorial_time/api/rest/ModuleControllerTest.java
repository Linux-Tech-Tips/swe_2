/**
 * @author Ethan
 */
package org.unixusergroup.tutorial_time.api.rest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryLecturerRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryModuleRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryStudentRepository;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ModuleControllerTest {

    /** Test for createNewModule rest API function. */
    @Test
    void createModuleTest() {
        ModuleRepository moduleRepository = new InMemoryModuleRepository();
        StudentRepository studentRepository = new InMemoryStudentRepository();

        /** Creating a lecturer and adding it to the test lecturer repo. */
        Lecturer lecturer = new Lecturer("Johnny test", "j.test@universityofgalway.ie", 10002);
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        lecturerRepository.saveLecturer(lecturer);

        /** Calling the rest api function after creating the controller */
        Module expectedModule = new Module("CT202", 10002);
        ModuleController moduleController = new ModuleController(moduleRepository, lecturerRepository, studentRepository);
        ResponseEntity<?> response = moduleController.createNewModule(10002, "CT202", new ArrayList<>(), new ArrayList<>());

        /** Verifying that the useCase was successful and checking if the correct module was saved. */
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedModule, moduleRepository.getModuleByCode("CT202"));
    }


    /** Test for getModule rest API function. */
    @Test
    void getModuleTest() {
        ModuleRepository moduleRepository = new InMemoryModuleRepository();
        StudentRepository studentRepository = new InMemoryStudentRepository();

        /** Creating a lecturer and adding it to the test lecturer repo. */
        Lecturer lecturer = new Lecturer("Johnny test", "j.test@universityofgalway.ie", 10002);
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        lecturerRepository.saveLecturer(lecturer);

        /** Creating a module and save it to the module repo. */
        Module expectedModule = new Module("CT202", lecturer.getUniversityId());
        moduleRepository.saveModule(expectedModule);

        /** Calling the rest api function after creating the controller */
        ModuleController moduleController = new ModuleController(moduleRepository, lecturerRepository, studentRepository);
        ResponseEntity<?> response = moduleController.getModule("CT202", lecturer.getUniversityId());

        /** Verifying that the useCase was successful and checking if the correct module was saved. */
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedModule, response.getBody());
    }
}
