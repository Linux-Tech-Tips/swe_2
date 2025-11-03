/** 
 * @author Martin
 */
package org.unixusergroup.tutorial_time.application.users;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

import org.unixusergroup.tutorial_time.domain.User;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryStudentRepository;
import org.unixusergroup.tutorial_time.infrastructure.InMemoryLecturerRepository;
import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.users.CreateTimetableUseCase;
import org.unixusergroup.tutorial_time.application.users.dto.*;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;
import org.unixusergroup.tutorial_time.domain.timetable.Availability;

@SpringBootTest
class CreateTimetableUseCaseTest {

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

    /** Utility function to run the CreateTimetableUseCase with a given student instance and availability array */
    private CreateTimetableResponse studentTestFixture(Student student, int[] availability, StudentRepository studentRepository) {
        studentRepository.saveStudent(student);
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();

        UseCase<CreateTimetableResponse, CreateTimetableRequest> useCase = new CreateTimetableUseCase(studentRepository, lecturerRepository);
        CreateTimetableRequest request = new CreateTimetableRequest(availability, student.getUniversityId(), User.UserType.STUDENT);
        return useCase.execute(request);
    }

    /** Utility function to run the CreateTimetableUseCase with a given lecturer instance and availability array */
    private CreateTimetableResponse lecturerTestFixture(Lecturer lecturer, int[] availability, LecturerRepository lecturerRepository) {
        lecturerRepository.saveLecturer(lecturer);
        StudentRepository studentRepository = new InMemoryStudentRepository();

        UseCase<CreateTimetableResponse, CreateTimetableRequest> useCase = new CreateTimetableUseCase(studentRepository, lecturerRepository);
        CreateTimetableRequest request = new CreateTimetableRequest(availability, lecturer.getUniversityId(), User.UserType.LECTURER);
        return useCase.execute(request);
    }
 
    /** Test case to create a student timetable for a student initialized with an empty timetable */
    @Test
    public void createStudentTimetableTest() {
        Student student = new Student("John Doe", "j.doe1@universityofgalway.ie", 123456);
        int[] availability = new int[] { 0, 0, 0, 0, 1,
                                         1, 1, 0, 0, 1,
                                         1, 1, 0, 1, 0,
                                         0, 1, 0, 1, 0,
                                         0, 1, 1, 1, 0,
                                         1, 0, 1, 0, 0,
                                         0, 0, 0, 0, 0,
                                         0, 0, 0, 0, 0 };
        StudentRepository studentRepository = new InMemoryStudentRepository();
        CreateTimetableResponse response = studentTestFixture(student, availability, studentRepository);

        assertTrue(response.ok());
        assertEquals(response.responseText(), "");

        Timetable expectedTimetable = getTimetableFromArray(availability, 5, 8, Availability.UNAVAILABLE);
        Timetable producedTimetable = studentRepository.getStudentById(123456).getTimetable();
        assertEquals(expectedTimetable, producedTimetable);
    }

    /** Test case to create a student timetable for a student with an existing timetable */
    @Test
    public void overwriteStudentTimetableTest() {
        int[] oldAvailability = new int[] { 1, 1, 0, 0, 1, 
                                            1, 1, 0, 1, 1, 
                                            1, 1, 1, 1, 1, 
                                            1, 0, 1, 0, 1, 
                                            1, 1, 1, 1, 1, 
                                            0, 1, 0, 1, 0, 
                                            0, 0, 0, 0, 0, 
                                            0, 0, 0, 0, 0 };
        Student student = new Student("John Doe", "j.doe1@universityofgalway.ie", 123456, getTimetableFromArray(oldAvailability, 5, 8, Availability.UNAVAILABLE));
        int[] availability = new int[] { 0, 0, 0, 0, 1,
                                         1, 1, 0, 0, 1,
                                         1, 1, 0, 1, 0,
                                         0, 1, 0, 1, 0,
                                         0, 1, 1, 1, 0,
                                         1, 0, 1, 0, 0,
                                         0, 0, 0, 0, 0,
                                         0, 0, 0, 0, 0 };
        StudentRepository studentRepository = new InMemoryStudentRepository();
        CreateTimetableResponse response = studentTestFixture(student, availability, studentRepository);

        assertTrue(response.ok());
        assertEquals(response.responseText(), "");

        Timetable oldTimetable = getTimetableFromArray(oldAvailability, 5, 8, Availability.UNAVAILABLE);
        Timetable expectedTimetable = getTimetableFromArray(availability, 5, 8, Availability.UNAVAILABLE);
        Timetable producedTimetable = studentRepository.getStudentById(123456).getTimetable();
        assertEquals(expectedTimetable, producedTimetable);
        assertNotEquals(expectedTimetable, oldTimetable);
        assertNotEquals(oldTimetable, producedTimetable);
    }

    /** Test case to create a lecturer timetable for a lecturer initialized with an empty timetable */
    @Test
    public void createLecturerTimetableTest() {
        Lecturer lecturer = new Lecturer("John Doe", "john.doe@universityofgalway.ie", 654321);
        int[] availability = new int[] { 0, 0, 0, 1, 0,
                                         1, 1, 0, 1, 0,
                                         1, 1, 0, 1, 0,
                                         0, 1, 0, 0, 1,
                                         0, 1, 0, 1, 1,
                                         1, 0, 1, 0, 0,
                                         0, 0, 0, 0, 0,
                                         1, 1, 1, 1, 1 };
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        CreateTimetableResponse response = lecturerTestFixture(lecturer, availability, lecturerRepository);

        assertTrue(response.ok());
        assertEquals(response.responseText(), "");

        Timetable expectedTimetable = getTimetableFromArray(availability, 5, 8, Availability.UNAVAILABLE);
        Timetable producedTimetable = lecturerRepository.getLecturerById(654321).getTimetable();
        assertEquals(expectedTimetable, producedTimetable);
    }

    /** Test case to create a lecturer timetable for a lecturer with an existing timetable */
    @Test
    public void overwriteLecturerTimetableTest() {
        int[] oldAvailability = new int[] { 1, 1, 0, 0, 1, 
                                            1, 1, 0, 1, 1, 
                                            1, 1, 1, 1, 1, 
                                            1, 0, 1, 0, 1, 
                                            1, 1, 1, 1, 1, 
                                            0, 1, 0, 1, 0, 
                                            0, 0, 0, 0, 0, 
                                            0, 0, 0, 0, 0 };
        Lecturer lecturer = new Lecturer("John Doe", "john.doe@universityofgalway.ie", 654321);
        int[] availability = new int[] { 0, 0, 0, 0, 1,
                                         1, 1, 0, 0, 1,
                                         1, 1, 0, 1, 0,
                                         0, 1, 0, 1, 0,
                                         0, 1, 1, 1, 0,
                                         1, 0, 1, 0, 0,
                                         0, 0, 0, 0, 0,
                                         0, 0, 0, 0, 0 };
        LecturerRepository lecturerRepository = new InMemoryLecturerRepository();
        CreateTimetableResponse response = lecturerTestFixture(lecturer, availability, lecturerRepository);

        assertTrue(response.ok());
        assertEquals(response.responseText(), "");

        Timetable oldTimetable = getTimetableFromArray(oldAvailability, 5, 8, Availability.UNAVAILABLE);
        Timetable expectedTimetable = getTimetableFromArray(availability, 5, 8, Availability.UNAVAILABLE);
        Timetable producedTimetable = lecturerRepository.getLecturerById(654321).getTimetable();
        assertEquals(expectedTimetable, producedTimetable);
        assertNotEquals(expectedTimetable, oldTimetable);
        assertNotEquals(oldTimetable, producedTimetable);
    }

}

