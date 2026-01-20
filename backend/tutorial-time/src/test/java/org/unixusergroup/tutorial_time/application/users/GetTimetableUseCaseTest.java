/** 
 * @author Martin
 */
package org.unixusergroup.tutorial_time.application.users;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import org.unixusergroup.tutorial_time.domain.User;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryStudentRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryLecturerRepository;
import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.users.GetTimetableUseCase;
import org.unixusergroup.tutorial_time.application.users.dto.*;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;
import org.unixusergroup.tutorial_time.domain.timetable.Availability;

@SpringBootTest
class GetTimetableUseCaseTest {

    /** Utility function to create a Timetable from an availability int array */
    private Timetable getTimetableFromArray(int[] array, int cols, int rows, Availability defaultAvailable) {
        Timetable result = new Timetable(cols, rows, defaultAvailable);
        for(int col = 0; col < cols; ++col) {
            for(int row = 0; row < rows; ++row) {
                Availability availability = array[result.getFlatIndex(col, row)] == 0 ? Availability.UNAVAILABLE : Availability.AVAILABLE;
                result.setAvailability(col, row, availability);
            }
        }
        return result;
    }

    /** Test case to get a student timetable */
    @Test
    public void getStudentTimetableTest() {
        /* Create student */
        int[] availability = new int[] { 0, 1, 0, 0, 1, 
                                         0, 0, 1, 0, 0, 
                                         0, 0, 0, 0, 0, 
                                         1, 0, 1, 1, 0, 
                                         0, 0, 0, 0, 1, 
                                         0, 1, 1, 0, 1, 
                                         1, 1, 1, 0, 1, 
                                         1, 1, 1, 1, 1 };
        Student student = new Student("j.doe1@universityofgalway.ie", 123456, getTimetableFromArray(availability, 5, 8, Availability.UNAVAILABLE));

        /* Create demo repositories */
        StudentRepository studentRepository = new InMemoryStudentRepository();
        studentRepository.saveStudent(student);

        /* Create and execute the use case */
        GetTimetableRequest request = new GetTimetableRequest(123456, User.UserType.STUDENT);
        UseCase<GetTimetableResponse, GetTimetableRequest> useCase = new GetTimetableUseCase(studentRepository);
        GetTimetableResponse response = useCase.execute(request);

        /* Check that use case executed correctly */
        assertTrue(Arrays.equals(availability, response.availability()));
    }

    /** Test case to get a lecturer timetable */
    @Test
    public void getLecturerTimetableTest() {
        /* Create lecturer */
        int[] availability = new int[] { 1, 1, 1, 1, 1, 
                                         1, 0, 1, 0, 0, 
                                         0, 0, 0, 0, 0, 
                                         1, 0, 1, 1, 0, 
                                         0, 0, 0, 1, 1, 
                                         0, 1, 1, 0, 0, 
                                         1, 0, 1, 0, 1, 
                                         1, 0, 0, 1, 1 };
        Lecturer lecturer = new Lecturer("john.doe@universityofgalway.ie", 654321, getTimetableFromArray(availability, 5, 8, Availability.UNAVAILABLE));

        /* Create demo repositories */
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        lecturerRepository.saveLecturer(lecturer);

        /* Create and execute the use case */
        GetTimetableRequest request = new GetTimetableRequest(654321, User.UserType.LECTURER);
        UseCase<GetTimetableResponse, GetTimetableRequest> useCase = new GetTimetableUseCase(lecturerRepository);
        GetTimetableResponse response = useCase.execute(request);

        /* Check that use case executed correctly */
        assertTrue(Arrays.equals(availability, response.availability()));
    }

}

