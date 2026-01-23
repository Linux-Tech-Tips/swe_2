/** 
 * @author Martin, Ethan, Cedar
 */

package org.unixusergroup.tutorial_time.api.rest;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;
import org.unixusergroup.tutorial_time.domain.timetable.Availability;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryLecturerRepository;

import static org.unixusergroup.tutorial_time.api.rest.MockAuthUtil.createMockJwtToken;

@SpringBootTest
class LecturerControllerTest {

    /** Test for the createLecturerTimetable REST API function */
    @Test
    void createLecturerTimetableTest() {
        /* Setting up in-memory repository with demo lecturer */
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        lecturerRepository.saveLecturer(new Lecturer("jane.doe@universityofgalway.ie", 654321));

        /* Creating timetable */
        LecturerController controller = new LecturerController(lecturerRepository);
        int[] availability = new int[] { 1, 1, 0, 1, 1,
                                         0, 1, 1, 0, 1,
                                         0, 0, 1, 0, 1,
                                         0, 1, 0, 0, 0,
                                         1, 1, 0, 1, 0,
                                         1, 1, 1, 1, 0,
                                         1, 1, 1, 1, 1,
                                         1, 1, 1, 1, 1 };
        ResponseEntity<?> response = controller.createLecturerTimetable(createMockJwtToken("654321", "Lecturer"), 654321, availability);

        /* Verifying that status is OK and that the saved array is the same as the given array */
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(availability, lecturerRepository.getLecturerById(654321).getTimetable().getArray());
    }

    /** Test that unauthorized access to createLecturerTimetable is not allowed */
    @Test
    void createLecturerTimetableAccessTest() {
        /* Set up repository and controller */        
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        LecturerController controller = new LecturerController(lecturerRepository);

        /* Test unauthorized with no ID */
        ResponseEntity<?> response1 = controller.createLecturerTimetable(createMockJwtToken("", "Lecturer"), 654321, new int[] {});
        assertEquals(HttpStatus.FORBIDDEN, response1.getStatusCode());

        /* Test unauthorized with incorrect ID */
        ResponseEntity<?> response2 = controller.createLecturerTimetable(createMockJwtToken("654", "Lecturer"), 654321, new int[] {});
        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
    }

    /** Test for createLecturerTimetable REST API catching an exception */
    @Test
    void failedCreateLecturerTimetableTest() {
        /* Setting up in-memory repository with demo lecturer */
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();

        /* Creating timetable */
        LecturerController controller = new LecturerController(lecturerRepository);
        int[] availability = new int[] { 1, 1, 0, 1, 1,
                0, 1, 1, 0, 1,
                0, 0, 1, 0, 1,
                0, 1, 0, 0, 0,
                1, 1, 0, 1, 0,
                1, 1, 1, 1, 0,
                1, 1, 1, 1, 1,
                1, 1, 1, 1, 1 };
        ResponseEntity<?> response = controller.createLecturerTimetable(createMockJwtToken("0", "Lecturer"), 0L, availability);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /** Test for the getLecturerTimetable REST API function */
    @Test
    void getLecturerTimetableTest() {
        /* Setting up in-memory repository with demo lecturer */
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
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
        lecturerRepository.saveLecturer(new Lecturer("jane.doe@universityofgalway.ie", 654321, timetable));

        /* Getting timetable */
        LecturerController controller = new LecturerController(lecturerRepository);
        ResponseEntity<?> response = controller.getLecturerTimetable(createMockJwtToken("654321", "Lecturer"), 654321);

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

    /** Test that unauthorized access to getLecturerTimetable is not allowed */
    @Test
    void getLecturerTimetableAccessTest() {
        /* Set up repository and controller */        
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        LecturerController controller = new LecturerController(lecturerRepository);

        /* Test unauthorized with no ID */
        ResponseEntity<?> response1 = controller.getLecturerTimetable(createMockJwtToken("", "Lecturer"), 654321);
        assertEquals(HttpStatus.FORBIDDEN, response1.getStatusCode());

        /* Test unauthorized with incorrect ID */
        ResponseEntity<?> response2 = controller.getLecturerTimetable(createMockJwtToken("321", "Lecturer"), 654321);
        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
    }

    /** Test for getLecturerTimetable REST API catching an exception */
    @Test
    void failedGetLecturerTimetableTest() {
        /* Setting up in-memory repository */
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();

        LecturerController controller = new LecturerController(lecturerRepository);
        ResponseEntity<?> response = controller.getLecturerTimetable(createMockJwtToken("0", "Lecturer"), 0L);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /** Test for addLecturer REST API function */
    @Test
    void addLecturerToRepository() {
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        LecturerController controller = new LecturerController(lecturerRepository);
        Lecturer expectedLecturer = new Lecturer("j.test@nuigalway.ie", 1002);
        ResponseEntity<?> response = controller.addLecturer(createMockJwtToken("1002", "Lecturer"), "j.test@nuigalway.ie", 1002);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Lecturer retrievedLecturer = lecturerRepository.getLecturerById(1002);
        assertEquals(expectedLecturer, retrievedLecturer);
    }

    /** Test that unauthorized access to addLecturer is not allowed */
    @Test
    void addLecturerToRepositoryAccessTest() {
        /* Set up repository and controller */        
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        LecturerController controller = new LecturerController(lecturerRepository);

        /* Test unauthorized with no ID */
        ResponseEntity<?> response1 = controller.addLecturer(createMockJwtToken("", "Lecturer"), "j.test@nuigalway.ie", 1002);
        assertEquals(HttpStatus.FORBIDDEN, response1.getStatusCode());

        /* Test unauthorized with incorrect ID */
        ResponseEntity<?> response2 = controller.addLecturer(createMockJwtToken("1001", "Lecturer"), "j.test@nuigalway.ie", 1002);
        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
    }

    /** Test for addLecturer REST API catching an exception */
    @Test
    void failedAddLecturerToRepository() {
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        LecturerController controller = new LecturerController(lecturerRepository);
        ResponseEntity<?> response = controller.addLecturer(createMockJwtToken("0", "Lecturer"), "j.test@nuigalway.ie", 0L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void retrieveLecturerFromRepository() {
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        LecturerController controller = new LecturerController(lecturerRepository);
        Lecturer expectedLecturer = new Lecturer("johnny.test@nuigalway.ie", 1002);
        lecturerRepository.saveLecturer(expectedLecturer);

        ResponseEntity<?> response = controller.getLecturer(createMockJwtToken("1002", "Lecturer"), 1002);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(expectedLecturer, response.getBody());
    }

    @Test
    void retrieveLecturerFromRepositoryAccessTest() {
        /* Set up repository and controller */        
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        LecturerController controller = new LecturerController(lecturerRepository);

        /* Test unauthorized with no ID */
        ResponseEntity<?> response1 = controller.getLecturer(createMockJwtToken("", "Lecturer"), 1002);
        assertEquals(HttpStatus.FORBIDDEN, response1.getStatusCode());

        /* Test unauthorized with incorrect ID */
        ResponseEntity<?> response2 = controller.getLecturer(createMockJwtToken("1001", "Lecturer"), 1002);
        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
    }

    @Test
    void failedRetrieveLecturerFromRepository() {
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        LecturerController controller = new LecturerController(lecturerRepository);
        ResponseEntity<?> response = controller.getLecturer(createMockJwtToken("1002", "Lecturer"), 1002);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void flipLecturerEmailPermissionsTest() {
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        LecturerController controller = new LecturerController(lecturerRepository);
        controller.addLecturer(createMockJwtToken("1002", "Lecturer"), "L.test@nuigalway.ie", 1002);
        ResponseEntity<?> response = controller.flipEmailPermissions(createMockJwtToken("1002", "Lecturer"), 1002);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void failedFlipLecturerEmailPermissionsTest() {
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        LecturerController controller = new LecturerController(lecturerRepository);

        /* test that a user with the wrong ID can't edit the permissions */
        ResponseEntity<?> response = controller.flipEmailPermissions(createMockJwtToken("100", "Lecturer"), 1002);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        /* test that permissions of a user that doesn't exist can't be edited */
        response = controller.flipEmailPermissions(createMockJwtToken("1002", "Lecturer"), 1002);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}

