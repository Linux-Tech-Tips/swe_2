/** 
 * @author Martin, Ethan
 */
package org.unixusergroup.tutorial_time.api.rest;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;
import org.unixusergroup.tutorial_time.domain.timetable.Availability;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryStudentRepository;
import static org.unixusergroup.tutorial_time.api.rest.MockAuthUtil.createMockJwtToken;

@SpringBootTest
class StudentControllerTest {

    /** Test for the createStudentTimetable REST API function */
    @Test 
    void createStudentTimetableTest() {
        /* Setting up in-memory repository with demo student */
        StudentRepository studentRepository = new InMemoryStudentRepository();
        studentRepository.saveStudent(new Student("John Doe", "j.doe1@universityofgalway.ie", 123456));

        /* Creating timetable */
        StudentController controller = new StudentController(studentRepository);
        int[] availability = new int[] { 1, 1, 0, 1, 1,
                                         0, 1, 1, 0, 1,
                                         0, 0, 1, 0, 1,
                                         0, 1, 0, 0, 0,
                                         1, 1, 0, 1, 0,
                                         1, 1, 1, 1, 0,
                                         1, 1, 1, 1, 1,
                                         1, 1, 1, 1, 1 };
        ResponseEntity<?> response = controller.createStudentTimetable(createMockJwtToken("123456", "Student"), 123456, availability);

        /* Verifying that status is OK and that the saved array is the same as the given array */
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Arrays.equals(availability, studentRepository.getStudentById(123456).getTimetable().getArray()));
    }

    @Test
    void createStudentTimetableAccessTest() {
        /* Set up repository and controller */        
        StudentRepository studentRepository = new InMemoryStudentRepository();
        StudentController controller = new StudentController(studentRepository);

        /* Test unauthorized with no ID */
        ResponseEntity<?> response1 = controller.createStudentTimetable(createMockJwtToken("", "Student"), 123456, new int[] {});
        assertEquals(HttpStatus.FORBIDDEN, response1.getStatusCode());

        /* Test unauthorized with incorrect ID */
        ResponseEntity<?> response2 = controller.createStudentTimetable(createMockJwtToken("123", "Student"), 123456, new int[] {});
        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
    }

    @Test
    void failedCreateStudentTimetableTest() {
        /* Setting up in-memory repository with demo student */
        StudentRepository studentRepository = new InMemoryStudentRepository();

        /* Creating timetable */
        StudentController controller = new StudentController(studentRepository);
        int[] availability = new int[] { 1, 1, 0, 1, 1,
                0, 1, 1, 0, 1,
                0, 0, 1, 0, 1,
                0, 1, 0, 0, 0,
                1, 1, 0, 1, 0,
                1, 1, 1, 1, 0,
                1, 1, 1, 1, 1,
                1, 1, 1, 1, 1 };
        ResponseEntity<?> response = controller.createStudentTimetable(createMockJwtToken("0", "Student"), 0L, availability);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /** Test for the getStudentTimetable REST API function */
    @Test
    void getStudentTimetableTest() {
        /* Setting up in-memory repository with demo student */
        StudentRepository studentRepository = new InMemoryStudentRepository();
        Timetable timetable = new Timetable();
        int[] availability = new int[] { 1, 1, 0, 1, 1,
                                         0, 1, 1, 0, 1,
                                         0, 0, 1, 0, 1,
                                         0, 1, 0, 0, 0,
                                         1, 1, 0, 1, 0,
                                         1, 1, 1, 1, 0,
                                         1, 1, 1, 1, 1,
                                         1, 1, 1, 1, 1 };
        for(int col = 0; col < timetable.getColumns(); ++col) {
            for(int row = 0; row < timetable.getRows(); ++row) {
                timetable.setAvailability(col, row, availability[timetable.getFlatIndex(col, row)] == 0 ? Availability.UNAVAILABLE : Availability.AVAILABLE);
            }
        }
        studentRepository.saveStudent(new Student("John Doe", "j.doe1@universityofgalway.ie", 123456, timetable));

        /* Getting timetable */
        StudentController controller = new StudentController(studentRepository);
        ResponseEntity<?> response = controller.getStudentTimetable(createMockJwtToken("123456", "Student"), 123456);

        /* Verifying that status is OK and that the returned array is the same as the saved array */
        assertEquals(HttpStatus.OK, response.getStatusCode());
        StringBuilder expectedResult = new StringBuilder();
        for(int i = 0; i < availability.length; ++i) {
            expectedResult.append("availability=").append(availability[i]);
            if(i < availability.length - 1) {
                expectedResult.append("&");
            }
        }
        assertEquals(expectedResult.toString(), response.getBody());
    }

    @Test
    void getStudentTimetableAccessTest() {
        /* Set up repository and controller */        
        StudentRepository studentRepository = new InMemoryStudentRepository();
        StudentController controller = new StudentController(studentRepository);

        /* Test unauthorized with no ID */
        ResponseEntity<?> response1 = controller.getStudentTimetable(createMockJwtToken("", "Student"), 123456);
        assertEquals(HttpStatus.FORBIDDEN, response1.getStatusCode());

        /* Test unauthorized with incorrect ID */
        ResponseEntity<?> response2 = controller.getStudentTimetable(createMockJwtToken("123", "Student"), 123456);
        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
    }

    @Test
    void failedGetStudentTimetableTest() {
        /* Setting up in-memory repository */
        StudentRepository studentRepository = new InMemoryStudentRepository();

        StudentController controller = new StudentController(studentRepository);
        ResponseEntity<?> response = controller.getStudentTimetable(createMockJwtToken("0", "Student"), 0L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void addStudentToRepository() {
        StudentRepository studentRepository = new InMemoryStudentRepository();
        StudentController controller = new StudentController(studentRepository);

        Student expectedStudent = new Student("Lance test", "L.test@nuigalway.ie", 20003);
        studentRepository.saveStudent(expectedStudent);


        ResponseEntity<?> response = controller.addStudent(createMockJwtToken("20003", "Student"), "Lance test", "L.test@nuigalway.ie", 20003);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Student retrievedStudent = studentRepository.getStudentById(20003);
        assertEquals(expectedStudent, retrievedStudent);
    }

    @Test
    void addStudentToRepositoryAccessTest() {
        /* Set up repository and controller */        
        StudentRepository studentRepository = new InMemoryStudentRepository();
        StudentController controller = new StudentController(studentRepository);

        /* Test unauthorized with no ID */
        ResponseEntity<?> response1 = controller.addStudent(createMockJwtToken("", "Student"), "Lance test", "L.test@nuigalway.ie", 20003);
        assertEquals(HttpStatus.FORBIDDEN, response1.getStatusCode());

        /* Test unauthorized with incorrect ID */
        ResponseEntity<?> response2 = controller.addStudent(createMockJwtToken("5768", "Student"), "Lance test", "L.test@nuigalway.ie", 20003);
        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
    }

    @Test
    void failedAddStudentToRepository() {
        StudentRepository studentRepository = new InMemoryStudentRepository();
        StudentController controller = new StudentController(studentRepository);
        ResponseEntity<?> response = controller.addStudent(createMockJwtToken("0", "Student"), "Lance test", "L.test@nuigalway.ie", 0);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void retrieveStudentFromRepository() {
        StudentRepository studentRepository = new InMemoryStudentRepository();
        StudentController controller = new StudentController(studentRepository);

        Student expectedStudent = new Student("Lance test", "L.test@nuigalway.ie", 20003);
        studentRepository.saveStudent(expectedStudent);

        ResponseEntity<?> response = controller.getStudent(createMockJwtToken("20003", "Student"), 20003);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(expectedStudent, response.getBody());
    }

    @Test
    void retrieveStudentFromRepositoryAccessTest() {
        /* Set up repository and controller */        
        StudentRepository studentRepository = new InMemoryStudentRepository();
        StudentController controller = new StudentController(studentRepository);

        /* Test unauthorized with no ID */
        ResponseEntity<?> response1 = controller.getStudent(createMockJwtToken("", "Student"), 20003);
        assertEquals(HttpStatus.FORBIDDEN, response1.getStatusCode());

        /* Test unauthorized with incorrect ID */
        ResponseEntity<?> response2 = controller.getStudent(createMockJwtToken("203", "Student"), 20003);
        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
    }

    @Test
    void failedRetrieveStudentFromRepository() {
        StudentRepository studentRepository = new InMemoryStudentRepository();
        StudentController controller = new StudentController(studentRepository);
        ResponseEntity<?> response = controller.getStudent(createMockJwtToken("1002", "Student"), 1002);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}

