/**
 * @author Ethan, Lilian
 */
package org.unixusergroup.tutorial_time.api.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
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
import static org.unixusergroup.tutorial_time.api.rest.MockAuthUtil.createMockJwtToken;

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
        ResponseEntity<?> response = moduleController.createNewModule(createMockJwtToken("10002", "Lecturer"), 10002, "CT202", new ArrayList<>(), new ArrayList<>());

        /** Verifying that the useCase was successful and checking if the correct module was saved. */
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedModule, moduleRepository.getModuleByCode("CT202"));
    }

    @Test
    void createModuleAccessTest() {
        /* Set up repository and controller */        
        ModuleRepository moduleRepository = new InMemoryModuleRepository();
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        StudentRepository studentRepository = new InMemoryStudentRepository();
        ModuleController moduleController = new ModuleController(moduleRepository, lecturerRepository, studentRepository);

        /* Test unauthorized with no ID */
        ResponseEntity<?> response1 = moduleController.createNewModule(createMockJwtToken("", "Lecturer"), 10002, "CT202", new ArrayList<>(), new ArrayList<>());
        assertEquals(HttpStatus.FORBIDDEN, response1.getStatusCode());

        /* Test unauthorized with incorrect ID */
        ResponseEntity<?> response2 = moduleController.createNewModule(createMockJwtToken("10001", "Lecturer"), 10002, "CT202", new ArrayList<>(), new ArrayList<>());
        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
    }

    /**
     * test for failed create module call
     */
    @Test
    void failedCreateModuleTest() {
        ModuleRepository moduleRepository = new InMemoryModuleRepository();
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        StudentRepository studentRepository = new InMemoryStudentRepository();

        ModuleController moduleController = new ModuleController(moduleRepository, lecturerRepository, studentRepository);
        ResponseEntity<?> response = moduleController.createNewModule(createMockJwtToken("10002", "Lecturer"), 10002, "CT202", new ArrayList<>(), new ArrayList<>());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
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
        ResponseEntity<?> response = moduleController.getModule(createMockJwtToken("10002", "Lecturer"), "CT202", lecturer.getUniversityId());

        /** Verifying that the useCase was successful and checking if the correct module was saved. */
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedModule, response.getBody());
    }

    @Test
    void getModuleAccessTest() {
        /* Set up repository and controller */        
        ModuleRepository moduleRepository = new InMemoryModuleRepository();
        Lecturer lecturer = new Lecturer("Johnny test", "j.test@nuigalway.ie", 10002);
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        StudentRepository studentRepository = new InMemoryStudentRepository();
        ModuleController moduleController = new ModuleController(moduleRepository, lecturerRepository, studentRepository);

        /* Test unauthorized with no ID */
        ResponseEntity<?> response1 = moduleController.getModule(createMockJwtToken("", "Lecturer"), "CT202", lecturer.getUniversityId());
        assertEquals(HttpStatus.FORBIDDEN, response1.getStatusCode());

        /* Test unauthorized with incorrect ID */
        ResponseEntity<?> response2 = moduleController.getModule(createMockJwtToken("10001", "Lecturer"), "CT202", lecturer.getUniversityId());
        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
    }

    @Test
    void failedGetModuleTest() {
        ModuleRepository moduleRepository = new InMemoryModuleRepository();
        StudentRepository studentRepository = new InMemoryStudentRepository();
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();

        ModuleController moduleController = new ModuleController(moduleRepository, lecturerRepository, studentRepository);
        ResponseEntity<?> response = moduleController.getModule(createMockJwtToken("1", "Lecturer"), "CT202", 1L);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /** Test for getModules rest API function. */
    @Test
    void getModulesTest() {
        ModuleRepository moduleRepository = new InMemoryModuleRepository();

        /** Creating a lecturer and adding it to the test lecturer repo. */
        Lecturer lecturer = new Lecturer("Johnny test", "j.test@nuigalway.ie", 10002);
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        lecturerRepository.saveLecturer(lecturer);

        StudentRepository studentRepository = new InMemoryStudentRepository();

        /** Creating modules and saving to the module repo. */
        Module module1 = new Module("CT202", lecturer.getUniversityId());
        Module module2 = new Module("CT203", lecturer.getUniversityId());
        Module module3 = new Module("CT204", lecturer.getUniversityId());
        moduleRepository.saveModule(module1);
        moduleRepository.saveModule(module2);
        moduleRepository.saveModule(module3);
        /* Expected list of modules */
        Set<String> expectedCodes = new HashSet<>();
        expectedCodes.add("CT202");
        expectedCodes.add("CT203");
        expectedCodes.add("CT204");

        /** Calling the rest api function after creating the controller */
        ModuleController moduleController = new ModuleController(moduleRepository, lecturerRepository, studentRepository);
        ResponseEntity<?> response = moduleController.getModules(createMockJwtToken("10002", "Lecturer"), lecturer.getUniversityId());

        /** Verifying that the useCase was successful and checking if the correct module was saved. */
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertIterableEquals((Collection)(expectedCodes), (Collection)(response.getBody()));
    }

    @Test
    void getModulesAccessTest() {
        /* Set up repository and controller */        
        ModuleRepository moduleRepository = new InMemoryModuleRepository();
        Lecturer lecturer = new Lecturer("Johnny test", "j.test@nuigalway.ie", 10002);
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        StudentRepository studentRepository = new InMemoryStudentRepository();
        ModuleController moduleController = new ModuleController(moduleRepository, lecturerRepository, studentRepository);

        /* Test unauthorized with no ID */
        ResponseEntity<?> response1 = moduleController.getModules(createMockJwtToken("", "Lecturer"), lecturer.getUniversityId());
        assertEquals(HttpStatus.FORBIDDEN, response1.getStatusCode());

        /* Test unauthorized with incorrect ID */
        ResponseEntity<?> response2 = moduleController.getModules(createMockJwtToken("10001", "Lecturer"), lecturer.getUniversityId());
        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
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
        ResponseEntity<?> response = moduleController.removeTA(createMockJwtToken("1000", "Lecturer"), module.getModuleCode(), 3L);

        /** verify output */
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new ArrayList<>(), module.getTAStudents());
    }

    @Test
    void removeTAAccessTest() {
        /* Set up repository and controller */        
        ModuleRepository moduleRepository = new InMemoryModuleRepository();
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        StudentRepository studentRepository = new InMemoryStudentRepository();
        Module module = new Module("testModule", new ArrayList<>(), new ArrayList<>(), 1000L);
        moduleRepository.saveModule(module);
        ModuleController moduleController = new ModuleController(moduleRepository, lecturerRepository, studentRepository);

        /* Test unauthorized with no ID */
        ResponseEntity<?> response1 = moduleController.removeTA(createMockJwtToken("", "Lecturer"), module.getModuleCode(), 3L);
        assertEquals(HttpStatus.FORBIDDEN, response1.getStatusCode());

        /* Test unauthorized with incorrect ID */
        ResponseEntity<?> response2 = moduleController.removeTA(createMockJwtToken("2000", "Lecturer"), module.getModuleCode(), 3L);
        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
    }

    @Test
    void failedRemoveTATest() {
        /** setting up data */
        InMemoryModuleRepository moduleRepository = new InMemoryModuleRepository();
        StudentRepository studentRepository = new InMemoryStudentRepository();
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        ModuleController moduleController = new ModuleController(moduleRepository, lecturerRepository, studentRepository);

        ResponseEntity<?> response = moduleController.removeTA(createMockJwtToken("123", "Lecturer"), "fakeModule", 3L);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
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
        ResponseEntity<?> response = moduleController.addTA(createMockJwtToken("1000", "Lecturer"), module.getModuleCode(), 4L);
        tas.add(4L);

        /** verify output */
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tas, module.getTAStudents());
    }

    @Test
    void addTAAccessTest() {
        /* Set up repository and controller */        
        ModuleRepository moduleRepository = new InMemoryModuleRepository();
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        StudentRepository studentRepository = new InMemoryStudentRepository();
        Module module = new Module("testModule", new ArrayList<>(), new ArrayList<>(), 1000L);
        moduleRepository.saveModule(module);
        ModuleController moduleController = new ModuleController(moduleRepository, lecturerRepository, studentRepository);

        /* Test unauthorized with no ID */
        ResponseEntity<?> response1 = moduleController.addTA(createMockJwtToken("", "Lecturer"), module.getModuleCode(), 4L);
        assertEquals(HttpStatus.FORBIDDEN, response1.getStatusCode());

        /* Test unauthorized with incorrect ID */
        ResponseEntity<?> response2 = moduleController.addTA(createMockJwtToken("2000", "Lecturer"), module.getModuleCode(), 4L);
        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
    }

    @Test
    void failedAddTATest() {
        /** setting up data */
        InMemoryModuleRepository moduleRepository = new InMemoryModuleRepository();
        StudentRepository studentRepository = new InMemoryStudentRepository();
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        ModuleController moduleController = new ModuleController(moduleRepository, lecturerRepository, studentRepository);

        ResponseEntity<?> response = moduleController.addTA(createMockJwtToken("123", "Lecturer"), "fakeModule", 3L);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
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
        ResponseEntity<?> response = moduleController.addStudent(createMockJwtToken("1000", "Lecturer"), module.getModuleCode(), 5L);
        students.add(5L);

        /** verify output */
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(students, module.getStudents());
    }

    @Test
    void addStudentAccessTest() {
        /* Set up repository and controller */        
        ModuleRepository moduleRepository = new InMemoryModuleRepository();
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        StudentRepository studentRepository = new InMemoryStudentRepository();
        Module module = new Module("testModule", new ArrayList<>(), new ArrayList<>(), 1000L);
        moduleRepository.saveModule(module);
        ModuleController moduleController = new ModuleController(moduleRepository, lecturerRepository, studentRepository);

        /* Test unauthorized with no ID */
        ResponseEntity<?> response1 = moduleController.addStudent(createMockJwtToken("", "Lecturer"), module.getModuleCode(), 5L);
        assertEquals(HttpStatus.FORBIDDEN, response1.getStatusCode());

        /* Test unauthorized with incorrect ID */
        ResponseEntity<?> response2 = moduleController.addStudent(createMockJwtToken("2000", "Lecturer"), module.getModuleCode(), 5L);
        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
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
        ResponseEntity<?> response = moduleController.removeStudent(createMockJwtToken("1000", "Lecturer"), module.getModuleCode(), 2L);
        students.remove(2L);

        /** verify output */
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(students, module.getStudents());
    }

    @Test
    void removeStudentAccessTest() {
        /* Set up repository and controller */        
        ModuleRepository moduleRepository = new InMemoryModuleRepository();
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        StudentRepository studentRepository = new InMemoryStudentRepository();
        Module module = new Module("testModule", new ArrayList<>(), new ArrayList<>(), 1000L);
        moduleRepository.saveModule(module);
        ModuleController moduleController = new ModuleController(moduleRepository, lecturerRepository, studentRepository);

        /* Test unauthorized with no ID */
        ResponseEntity<?> response1 = moduleController.removeStudent(createMockJwtToken("", "Lecturer"), module.getModuleCode(), 2L);
        assertEquals(HttpStatus.FORBIDDEN, response1.getStatusCode());

        /* Test unauthorized with incorrect ID */
        ResponseEntity<?> response2 = moduleController.removeStudent(createMockJwtToken("2000", "Lecturer"), module.getModuleCode(), 2L);
        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
    }
}
