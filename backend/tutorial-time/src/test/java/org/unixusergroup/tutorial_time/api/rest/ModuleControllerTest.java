/**
 * @author Ethan, Lilian
 */
package org.unixusergroup.tutorial_time.api.rest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryLecturerRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryModuleRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryStudentRepository;

@SpringBootTest
public class ModuleControllerTest {

    /** Test for createNewModule rest API function. */
    @Test
    void createModuleTest() {
        ModuleRepository moduleRepository = new InMemoryModuleRepository();
        StudentRepository studentRepository = new InMemoryStudentRepository();

        /** Creating a lecturer and adding it to the test lecturer repo. */
        Lecturer lecturer = new Lecturer("Johnny test", "j.test@nuigalway.ie", 10002);
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
        Lecturer lecturer = new Lecturer("Johnny test", "j.test@nuigalway.ie", 10002);
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

    @Test
    void removeTATest() {
        /** setting up data */
        InMemoryModuleRepository moduleRepository = new InMemoryModuleRepository();
        StudentRepository studentRepository = new InMemoryStudentRepository();
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        ArrayList<Long> students = new ArrayList<>();
        ArrayList<Long> tas = new ArrayList<>();
        students.add(1L);
        tas.add(3L);
        Module module = new Module("testModule", students, tas, 1000L);
        moduleRepository.saveModule(module);

        /** create and call the modulecontroller */
        ModuleController moduleController = new ModuleController(moduleRepository, lecturerRepository, studentRepository);
        ResponseEntity<?> response = moduleController.removeTA(module.getModuleCode(), 3L);

        /** verify output */
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new ArrayList<>(), module.getTAStudents());
    }

    @Test
    void addTATest() {
        /** setting up data */
        InMemoryModuleRepository moduleRepository = new InMemoryModuleRepository();
        StudentRepository studentRepository = new InMemoryStudentRepository();
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        ArrayList<Long> students = new ArrayList<>();
        ArrayList<Long> tas = new ArrayList<>();
        students.add(1L);
        tas.add(3L);
        Module module = new Module("testModule", students, tas, 1000L);
        moduleRepository.saveModule(module);

        /** create and call the modulecontroller */
        ModuleController moduleController = new ModuleController(moduleRepository, lecturerRepository, studentRepository);
        ResponseEntity<?> response = moduleController.addTA(module.getModuleCode(), 4L);
        tas.add(4L);

        /** verify output */
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tas, module.getTAStudents());
    }

    @Test
    void addStudentTest() {
        /** setting up data */
        InMemoryModuleRepository moduleRepository = new InMemoryModuleRepository();
        StudentRepository studentRepository = new InMemoryStudentRepository();
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        ArrayList<Long> students = new ArrayList<>();
        ArrayList<Long> tas = new ArrayList<>();
        students.add(1L);
        tas.add(3L);
        Module module = new Module("testModule", students, tas, 1000L);
        moduleRepository.saveModule(module);

        /** create and call the modulecontroller */
        ModuleController moduleController = new ModuleController(moduleRepository, lecturerRepository, studentRepository);
        ResponseEntity<?> response = moduleController.addStudent(module.getModuleCode(), 5L);
        students.add(5L);

        /** verify output */
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(students, module.getStudents());
    }

    @Test
    void removeStudentTest() {
        /** setting up data */
        InMemoryModuleRepository moduleRepository = new InMemoryModuleRepository();
        StudentRepository studentRepository = new InMemoryStudentRepository();
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        ArrayList<Long> students = new ArrayList<>();
        ArrayList<Long> tas = new ArrayList<>();
        students.add(1L);
        students.add(2L);
        tas.add(3L);
        Module module = new Module("testModule", students, tas, 1000L);
        moduleRepository.saveModule(module);

        /** create and call the modulecontroller */
        ModuleController moduleController = new ModuleController(moduleRepository, lecturerRepository, studentRepository);
        ResponseEntity<?> response = moduleController.removeStudent(module.getModuleCode(), 2L);
        students.remove(2L);

        /** verify output */
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(students, module.getStudents());
    }
}
