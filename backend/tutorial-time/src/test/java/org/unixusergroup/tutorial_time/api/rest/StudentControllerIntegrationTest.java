/** 
 * @author Martin
 */
package org.unixusergroup.tutorial_time.api.rest;

import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;
import org.unixusergroup.tutorial_time.domain.timetable.Availability;
import static org.unixusergroup.tutorial_time.api.rest.MockAuthUtil.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.core.convert.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.util.Arrays;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository mockStudentRepository;

    @BeforeEach
    private void configure() {
        Student student = new Student("Test Student", "t.student1@universityofgalway.ie", 123456);
        mockStudentRepository.saveStudent(student);
    }

    @Test
    public void getStudentAccessIntegrationTest() {
        try {
            /* Check that a Student-authenticated user can access the student API */
            mockMvc.perform(get("/api/student/getstudent")
                            .param("studentId", "123456")
                            .with(authentication(createMockJwtToken("123456", "Student"))))
                   .andExpect(status().isOk());
            /* Check that a Lecturer-authenticated user can't access the student API */
            mockMvc.perform(get("/api/student/getstudent")
                            .param("studentId", "123456")
                            .with(authentication(createMockJwtToken("123456", "Lecturer"))))
                   .andExpect(status().is(403));
            /* Check that a JWT-authenticated user without a role can't access the student API */
            mockMvc.perform(get("/api/student/getstudent")
                            .param("studentId", "123456")
                            .with(jwt()))
                   .andExpect(status().is(403));
            /* Check that a user without JWT authentication can't access the student API */
            mockMvc.perform(get("/api/student/getstudent")
                            .param("studentId", "123456"))
                   .andExpect(status().is(401));

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void getStudentValidIntegrationTest() {
        try {
            /* Check that the getstudent API call works as expected with JWT auth and an MVC */
            mockMvc.perform(get("/api/student/getstudent")
                            .param("studentId", "123456")
                            .with(authentication(createMockJwtToken("123456", "Student"))))
                   .andExpect(status()
                              .isOk())
                   .andExpect(content()
                              .json("{\"name\": \"Test Student\"," +
                                    " \"universityEmail\": \"t.student1@universityofgalway.ie\"," +
                                    " \"universityId\": 123456}"));
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void getStudentInvalidIntegrationTest() {
        try {
            /* Testing that getting a student with a non-existent ID doesn't work */
            mockMvc.perform(get("/api/student/getstudent")
                            .param("studentId", "00000")
                            .with(authentication(createMockJwtToken("00000", "Student"))))
                   .andExpect(status()
                              .is(500));

            /* Testing that getting a student without supplying and ID doesn't work */
            mockMvc.perform(get("/api/student/getstudent")
                            .with(authentication(createMockJwtToken("", "Student"))))
                   .andExpect(status()
                              .is(400));

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void createStudentValidIntegrationTest() {
        try {
            /* Testing that creating a valid student works as expected using the API call */
            mockMvc.perform(post("/api/student/createstudent")
                            .param("name", "John Doe")
                            .param("email", "john.doe@universityofgalway.ie")
                            .param("studentId", "654321")
                            .with(authentication(createMockJwtToken("654321", "Student")))
                            .with(csrf()))
                   .andExpect(status().is(200));
            /* Verify that the student was added */
            Student savedStudent = mockStudentRepository.getStudentById(654321);
            assertEquals(savedStudent, new Student("John Doe", "john.doe@universityofgalway.ie", 654321));
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void createStudentInvalidIntegrationTest() {
        try {
            /* Testing that creating a student with an empty name doesn't work */
            mockMvc.perform(post("/api/student/createstudent")
                            .param("name", "")
                            .param("email", "john.doe@universityofgalway.ie")
                            .param("studentId", "754321")
                            .with(authentication(createMockJwtToken("754321", "Student")))
                            .with(csrf()))
                   .andExpect(status().is(500));
            /* Testing that creating a student with an empty email doesn't work */
            mockMvc.perform(post("/api/student/createstudent")
                            .param("name", "Jane Doe")
                            .param("email", "")
                            .param("studentId", "754321")
                            .with(authentication(createMockJwtToken("754321", "Student")))
                            .with(csrf()))
                   .andExpect(status().is(500));
            /* Testing that creating a student with an invalid email doesn't work */
            mockMvc.perform(post("/api/student/createstudent")
                            .param("name", "Jane Doe")
                            .param("email", "jane.doe@mail.com") // must be an NUI address
                            .param("studentId", "754321")
                            .with(authentication(createMockJwtToken("754321", "Student")))
                            .with(csrf()))
                   .andExpect(status().is(500));
            /* Testing that creating a student without proper params doesn't work */
            mockMvc.perform(post("/api/student/createstudent")
                            .param("name", "Jane Doe")
                            .param("email", "jane.doe@universityofgalway.ie")
                            .with(authentication(createMockJwtToken("", "Student")))
                            .with(csrf()))
                   .andExpect(status().is(400));
            /* Check that no student of the given ID was added */
            assertNull(mockStudentRepository.getStudentById(754321));

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void createStudentTimetableValidIntegrationTest() {
        try {
            /* Testing the Create Student Timetable API with valid data */
            mockMvc.perform(post("/api/student/createtimetable")
                            .param("studentId", "123456")
                            .param("availability", "0,0,0,0,0,1,0,0,0,1,1,1,1,1,1,1,1,0,1,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1")
                            .with(authentication(createMockJwtToken("123456", "Student")))
                            .with(csrf()))
                   .andExpect(status().is(200));
            /* Check that the student timetable was saved successfully */
            Student student = mockStudentRepository.getStudentById(123456);
            assertTrue(Arrays.equals(student.getTimetable().getArray(), new int[] {
                0, 0, 0, 0, 0,
                1, 0, 0, 0, 1,
                1, 1, 1, 1, 1,
                1, 1, 0, 1, 1,
                1, 0, 0, 0, 1,
                1, 1, 1, 1, 1,
                1, 1, 1, 1, 1,
                1, 1, 1, 1, 1
            }));

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void createStudentTimetableInvalidIntegrationTest() {
        try {
            /* Obtaining student of ID tested here before the test cases run */
            Student studentBeforeTest = mockStudentRepository.getStudentById(123456);

            /* Testing that creating a timetable with an incorrect array size fails */
            mockMvc.perform(post("/api/student/createtimetable")
                            .param("studentId", "123456")
                            .param("availability", "0,0,0,0,0,1,1,1,1,1,0,1,1,1,0")
                            .with(authentication(createMockJwtToken("123456", "Student")))
                            .with(csrf()))
                   .andExpect(status().is(500));
            /* Testing that creating a timetable for a non-existent student fails */
            mockMvc.perform(post("/api/student/createtimetable")
                            .param("studentId", "754321")
                            .param("availability", "1,1,1,1,1,1,0,0,0,1,1,1,1,1,1,1,1,0,1,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1")
                            .with(authentication(createMockJwtToken("754321", "Student")))
                            .with(csrf()))
                   .andExpect(status().is(500));
            /* Testing that creating a timetable with an invalid value fails */
            mockMvc.perform(post("/api/student/createtimetable")
                            .param("studentId", "123456")
                            .param("availability", "1,2,1,1,1,1,0,0,0,1,1,1,1,1,1,1,1,0,1,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1")
                            .with(authentication(createMockJwtToken("123456", "Student")))
                            .with(csrf()))
                   .andExpect(status().is(500));
            /* Testing that creating a timetable with invalid params fails */
            mockMvc.perform(post("/api/student/createtimetable")
                            .param("studentId", "123456")
                            .with(authentication(createMockJwtToken("123456", "Student")))
                            .with(csrf()))
                   .andExpect(status().is(400));

            /* Obtaining student of ID tested here after the test cases */
            Student studentAfterTest = mockStudentRepository.getStudentById(123456);
            /* Check that test cases didn't modify the existing student */
            assertEquals(studentBeforeTest, studentAfterTest);

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void getStudentTimetableValidIntegrationTest() {
        try {
            /* Getting expected timetable from the repository */
            Timetable validTimetable = mockStudentRepository.getStudentById(123456).getTimetable();
            String expectedResult = "";
            for(int i = 0; i < validTimetable.getArray().length; ++i) {
                if(i > 0)
                    expectedResult += "&";
                expectedResult += "availability=" + validTimetable.getArray()[i];
            }
            /* Executing request */
            mockMvc.perform(post("/api/student/gettimetable")
                            .param("studentId", "123456")
                            .with(authentication(createMockJwtToken("123456", "Student")))
                            .with(csrf()))
                   .andExpect(status().is(200))
                   .andExpect(content()
                              .string(expectedResult));

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void getStudentTimetableInvalidIntegrationTest() {
        try {
            /* Testing that getting a timetable of a non-existent student doesn't work */
            mockMvc.perform(post("/api/student/gettimetable")
                            .param("studentId", "00000")
                            .with(authentication(createMockJwtToken("00000", "Student")))
                            .with(csrf()))
                   .andExpect(status().is(500));
            /* Testing that getting a timetable without specifying student ID doesn't work */
            mockMvc.perform(post("/api/student/gettimetable")
                            .with(authentication(createMockJwtToken("", "Student")))
                            .with(csrf()))
                   .andExpect(status().is(400));
            /* Testing that if malformed student created in database, obtaining their timetable fails */
            Timetable invalidTimetable = new Timetable(5, 5, Availability.AVAILABLE);
            Student invalidStudent = new Student("John Test", "john.test@universityofgalway.ie", 11111, invalidTimetable);
            mockStudentRepository.saveStudent(invalidStudent);
            mockMvc.perform(post("/api/student/gettimetable")
                            .param("studentId", "11111")
                            .with(authentication(createMockJwtToken("11111", "Student")))
                            .with(csrf()))
                   .andExpect(status().is(500));

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void deleteStudentValidIntegrationTest() {
        try {
            /* create a student */
            mockMvc.perform(post("/api/student/createstudent")
                            .param("name", "John Doe")
                            .param("email", "john.doe@universityofgalway.ie")
                            .param("studentId", "654321")
                            .with(authentication(createMockJwtToken("654321", "Student")))
                            .with(csrf()))
                    .andExpect(status().is(200));

            /* delete that student */
            mockMvc.perform(post("/api/student/deletestudent")
                            .param("studentId", "654321")
                            .with(authentication(createMockJwtToken("654321", "Student")))
                            .with(csrf()))
                    .andExpect(status().is(200));

            /* Verify that the student was deleted */
            assertNull(mockStudentRepository.getStudentById(654321));
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void deleteStudentInvalidIntegrationTest() {
        try {
            /* test that deleting a non-existent student throws an error */
            mockMvc.perform(post("/api/student/deletestudent")
                            .param("studentId", "654321")
                            .with(authentication(createMockJwtToken("654321", "Student")))
                            .with(csrf()))
                    .andExpect(status().is(404));

            /* Testing that deleting a student without proper params doesn't work */
            mockMvc.perform(post("/api/student/deletestudent")
                            .param("studentId", "654321")
                            .with(authentication(createMockJwtToken("", "Student")))
                            .with(csrf()))
                    .andExpect(status().is(403));
        } catch(Exception e) {
            System.out.println(e);
        }
    }

}
