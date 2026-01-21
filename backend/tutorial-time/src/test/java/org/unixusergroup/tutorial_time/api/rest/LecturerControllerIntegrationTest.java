/** 
 * @author Martin
 */
package org.unixusergroup.tutorial_time.api.rest;

import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
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
public class LecturerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LecturerRepository mockLecturerRepository;

    @BeforeEach
    private void configure() {
        Lecturer lecturer = new Lecturer("test.lecturer@universityofgalway.ie", 123456);
        mockLecturerRepository.saveLecturer(lecturer);
    }

    @Test
    public void getLecturerAccessIntegrationTest() {
        try {
            /* Check that a Lecturer-authenticated user can access the lecturer API */
            mockMvc.perform(get("/api/lecturer/getlecturer")
                            .param("staffId", "123456")
                            .with(authentication(createMockJwtToken("123456", "Lecturer"))))
                   .andExpect(status().isOk());
            /* Check that a Student-authenticated user can't access the lecturer API */
            mockMvc.perform(get("/api/lecturer/getlecturer")
                            .param("staffId", "123456")
                            .with(authentication(createMockJwtToken("123456", "Student"))))
                   .andExpect(status().is(403));
            /* Check that a JWT-authenticated user without a role can't access the lecturer API */
            mockMvc.perform(get("/api/lecturer/getlecturer")
                            .param("staffId", "123456")
                            .with(jwt()))
                   .andExpect(status().is(403));
            /* Check that a user without JWT authentication can't access the lecturer API */
            mockMvc.perform(get("/api/lecturer/getlecturer")
                            .param("staffId", "123456"))
                   .andExpect(status().is(401));

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void getLecturerValidIntegrationTest() {
        try {
            /* Check that the getlecturer API call works as expected with JWT auth and an MVC */
            mockMvc.perform(get("/api/lecturer/getlecturer")
                            .param("staffId", "123456")
                            .with(authentication(createMockJwtToken("123456", "Lecturer"))))
                   .andExpect(status()
                              .isOk())
                   .andExpect(content()
                              .json("{\"universityEmail\": \"test.lecturer@universityofgalway.ie\"," +
                                    " \"universityId\": 123456}"));
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void getLecturerInvalidIntegrationTest() {
        try {
            /* Testing that getting a lecturer with a non-existent ID doesn't work */
            mockMvc.perform(get("/api/lecturer/getlecturer")
                            .param("staffId", "00000")
                            .with(authentication(createMockJwtToken("00000", "Lecturer"))))
                   .andExpect(status()
                              .is(500));

            /* Testing that getting a lecturer without supplying an ID doesn't work */
            mockMvc.perform(get("/api/lecturer/getlecturer")
                            .with(authentication(createMockJwtToken("", "Lecturer"))))
                   .andExpect(status()
                              .is(400));

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void createLecturerValidIntegrationTest() {
        try {
            /* Testing that creating a valid lecturer works as expected using the API call */
            mockMvc.perform(post("/api/lecturer/createlecturer")
                            .param("email", "john.doe@universityofgalway.ie")
                            .param("staffId", "654321")
                            .with(authentication(createMockJwtToken("654321", "Lecturer")))
                            .with(csrf()))
                   .andExpect(status().is(200));
            /* Verify that the lecturer was added */
            Lecturer savedLecturer = mockLecturerRepository.getLecturerById(654321);
            assertEquals(savedLecturer, new Lecturer("john.doe@universityofgalway.ie", 654321));
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void createLecturerInvalidIntegrationTest() {
        try {
            /* Testing that creating a lecturer with an empty email doesn't work */
            mockMvc.perform(post("/api/lecturer/createlecturer")
                            .param("email", "")
                            .param("staffId", "754321")
                            .with(authentication(createMockJwtToken("754321", "Lecturer")))
                            .with(csrf()))
                   .andExpect(status().is(500));
            /* Testing that creating a lecturer with an invalid email doesn't work */
            mockMvc.perform(post("/api/lecturer/createlecturer")
                            .param("email", "jane.doe@mail.com") // must be an NUI address
                            .param("staffId", "754321")
                            .with(authentication(createMockJwtToken("754321", "Lecturer")))
                            .with(csrf()))
                   .andExpect(status().is(500));
            /* Testing that creating a lecturer without proper params doesn't work */
            mockMvc.perform(post("/api/lecturer/createlecturer")
                            .param("email", "jane.doe@universityofgalway.ie")
                            .with(authentication(createMockJwtToken("", "Lecturer")))
                            .with(csrf()))
                   .andExpect(status().is(400));
            /* Check that no lecturer of the given ID was added */
            assertNull(mockLecturerRepository.getLecturerById(754321));

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void createLecturerTimetableValidIntegrationTest() {
        try {
            /* Testing the Create Lecturer Timetable API with valid data */
            mockMvc.perform(post("/api/lecturer/createtimetable")
                            .param("staffId", "123456")
                            .param("availability", "0,0,0,0,0,1,0,0,0,1,1,1,1,1,1,1,1,0,1,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1")
                            .with(authentication(createMockJwtToken("123456", "Lecturer")))
                            .with(csrf()))
                   .andExpect(status().is(200));
            /* Check that the lecturer timetable was saved successfully */
            Lecturer lecturer = mockLecturerRepository.getLecturerById(123456);
            assertTrue(Arrays.equals(lecturer.getTimetable().getArray(), new int[] {
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
    public void createLecturerTimetableInvalidIntegrationTest() {
        try {
            /* Obtaining lecturer of ID tested here before the test cases run */
            Lecturer lecturerBeforeTest = mockLecturerRepository.getLecturerById(123456);

            /* Testing that creating a timetable with an incorrect array size fails */
            mockMvc.perform(post("/api/lecturer/createtimetable")
                            .param("staffId", "123456")
                            .param("availability", "0,0,0,0,0,1,1,1,1,1,0,1,1,1,0")
                            .with(authentication(createMockJwtToken("123456", "Lecturer")))
                            .with(csrf()))
                   .andExpect(status().is(400));
            /* Testing that creating a timetable for a non-existent lecturer fails */
            mockMvc.perform(post("/api/lecturer/createtimetable")
                            .param("staffId", "754321")
                            .param("availability", "1,1,1,1,1,1,0,0,0,1,1,1,1,1,1,1,1,0,1,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1")
                            .with(authentication(createMockJwtToken("754321", "Lecturer")))
                            .with(csrf()))
                   .andExpect(status().is(400));
            /* Testing that creating a timetable with an invalid value fails */
            mockMvc.perform(post("/api/lecturer/createtimetable")
                            .param("staffId", "123456")
                            .param("availability", "1,2,1,1,1,1,0,0,0,1,1,1,1,1,1,1,1,0,1,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1")
                            .with(authentication(createMockJwtToken("123456", "Lecturer")))
                            .with(csrf()))
                   .andExpect(status().is(500));
            /* Testing that creating a timetable with invalid params fails */
            mockMvc.perform(post("/api/lecturer/createtimetable")
                            .param("staffId", "123456")
                            .with(authentication(createMockJwtToken("123456", "Lecturer")))
                            .with(csrf()))
                   .andExpect(status().is(400));

            /* Obtaining lecturer of ID tested here after the test cases */
            Lecturer lecturerAfterTest = mockLecturerRepository.getLecturerById(123456);
            /* Check that test cases didn't modify the existing lecturer */
            assertEquals(lecturerBeforeTest, lecturerAfterTest);

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void getLecturerTimetableValidIntegrationTest() {
        try {
            /* Getting expected timetable from the repository */
            Timetable validTimetable = mockLecturerRepository.getLecturerById(123456).getTimetable();
            String expectedResult = "";
            for(int i = 0; i < validTimetable.getArray().length; ++i) {
                if(i > 0)
                    expectedResult += "&";
                expectedResult += "availability=" + validTimetable.getArray()[i];
            }
            /* Executing request */
            mockMvc.perform(post("/api/lecturer/gettimetable")
                            .param("staffId", "123456")
                            .with(authentication(createMockJwtToken("123456", "Lecturer")))
                            .with(csrf()))
                   .andExpect(status().is(200))
                   .andExpect(content()
                              .string(expectedResult));

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void getLecturerTimetableInvalidIntegrationTest() {
        try {
            /* Testing that getting a timetable of a non-existent lecturer doesn't work */
            mockMvc.perform(post("/api/lecturer/gettimetable")
                            .param("staffId", "00000")
                            .with(authentication(createMockJwtToken("00000", "Lecturer")))
                            .with(csrf()))
                   .andExpect(status().is(400));
            /* Testing that getting a timetable without specifying lecturer ID doesn't work */
            mockMvc.perform(post("/api/lecturer/gettimetable")
                            .with(authentication(createMockJwtToken("", "Lecturer")))
                            .with(csrf()))
                   .andExpect(status().is(400));
            /* Testing that if malformed lecturer created in database, obtaining their timetable fails */
            Timetable invalidTimetable = new Timetable(5, 5, Availability.AVAILABLE);
            Lecturer invalidLecturer = new Lecturer("john.test@universityofgalway.ie", 11111, invalidTimetable);
            mockLecturerRepository.saveLecturer(invalidLecturer);
            mockMvc.perform(post("/api/lecturer/gettimetable")
                            .param("staffId", "11111")
                            .with(authentication(createMockJwtToken("11111", "Lecturer")))
                            .with(csrf()))
                   .andExpect(status().is(500));

        } catch(Exception e) {
            System.out.println(e);
        }
    }

}
