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

import org.unixusergroup.tutorial_time.api.rest.LecturerController;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;
import org.unixusergroup.tutorial_time.domain.timetable.Availability;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryLecturerRepository;

@SpringBootTest
class LecturerControllerTest {

    /** Test for the createLecturerTimetable REST API function */
    @Test 
    void createLecturerTimetableTest() {
        /* Setting up in-memory repository with demo lecturer */
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        lecturerRepository.saveLecturer(new Lecturer("Jane Doe", "jane.doe@universityofgalway.ie", 654321));

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
        ResponseEntity<?> response = controller.createLecturerTimetable(654321, availability);

        /* Verifying that status is OK and that the saved array is the same as the given array */
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(availability, lecturerRepository.getLecturerById(654321).getTimetable().getArray());
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
        ResponseEntity<?> response = controller.createLecturerTimetable(0L, availability);
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
        lecturerRepository.saveLecturer(new Lecturer("Jane Doe", "jane.doe@universityofgalway.ie", 654321, timetable));

        /* Getting timetable */
        LecturerController controller = new LecturerController(lecturerRepository);
        ResponseEntity<?> response = controller.getLecturerTimetable(654321);

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

    /** Test for getLecturerTimetable REST API catching an exception */
    @Test
    void failedGetLecturerTimetableTest() {
        /* Setting up in-memory repository */
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();

        LecturerController controller = new LecturerController(lecturerRepository);
        ResponseEntity<?> response = controller.getLecturerTimetable(0L);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /** Test for getLecturerTimetable REST API catching an exception */
    @Test
    void addLecturerToRepository() {
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        LecturerController controller = new LecturerController(lecturerRepository);
        Lecturer expectedLecturer = new Lecturer("Jonny test", "j.test@nuigalway.ie", 1002);
        ResponseEntity<?> response = controller.addLecturer("Jonny test", "j.test@nuigalway.ie", 1002);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Lecturer retrievedLecturer = lecturerRepository.getLecturerById(1002);
        assertEquals(expectedLecturer, retrievedLecturer);
    }

    @Test
    void failedAddLecturerToRepository() {
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        LecturerController controller = new LecturerController(lecturerRepository);
        ResponseEntity<?> response = controller.addLecturer("Johnny Test", "j.test@nuigalway.ie", 0L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void retrieveLecturerFromRepository() {
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        LecturerController controller = new LecturerController(lecturerRepository);
        Lecturer expectedLecturer = new Lecturer("Jonny test", "johnny.test@nuigalway.ie", 1002);
        lecturerRepository.saveLecturer(expectedLecturer);

        ResponseEntity<?> response = controller.getLecturer(1002);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(expectedLecturer, response.getBody());
    }

    @Test
    void failedRetrieveLecturerFromRepository() {
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        LecturerController controller = new LecturerController(lecturerRepository);
        ResponseEntity<?> response = controller.getLecturer(1002);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}

