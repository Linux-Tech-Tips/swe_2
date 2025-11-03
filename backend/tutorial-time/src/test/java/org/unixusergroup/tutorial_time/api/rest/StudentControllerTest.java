/** 
 * @author Martin
 */
package org.unixusergroup.tutorial_time.application.users;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.unixusergroup.tutorial_time.api.rest.StudentController;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;
import org.unixusergroup.tutorial_time.domain.timetable.Availability;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryStudentRepository;

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
        ResponseEntity<?> response = controller.createStudentTimetable(123456, availability);

        /* Verifying that status is OK and that the saved array is the same as the given array */
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertTrue(Arrays.equals(availability, studentRepository.getStudentById(123456).getTimetable().getArray()));
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
        ResponseEntity<?> response = controller.getStudentTimetable(123456);

        /* Verifying that status is OK and that the returned array is the same as the saved array */
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        StringBuilder expectedResult = new StringBuilder();
        for(int i = 0; i < availability.length; ++i) {
            expectedResult.append("availability=").append(availability[i]);
            if(i < availability.length - 1) {
                expectedResult.append("&");
            }
        }
        assertEquals(expectedResult.toString(), response.getBody());
    }
}

