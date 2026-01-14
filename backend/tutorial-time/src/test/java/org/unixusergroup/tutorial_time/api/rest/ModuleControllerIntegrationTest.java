/** 
 * @author Martin
 */
package org.unixusergroup.tutorial_time.api.rest;

import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
import static org.unixusergroup.tutorial_time.api.rest.MockAuthUtil.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

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
import java.util.List;
import org.json.JSONArray;
import java.util.Collection;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ModuleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModuleRepository mockModuleRepository;
    
    @Autowired
    private LecturerRepository mockLecturerRepository;

    @Autowired
    private StudentRepository mockStudentRepository;

    @BeforeEach
    private void configure() {
        Lecturer lecturer = new Lecturer("Test Lecturer", "test.lecturer@universityofgalway.ie", 123456);
        mockLecturerRepository.saveLecturer(lecturer);
        Student student1 = new Student("John Doe", "j.doe1@universityofgalway.ie", 111111);
        Student student2 = new Student("Jane Doe", "j.doe2@universityofgalway.ie", 222222);
        Student student3 = new Student("Test A", "t.a1@universityofgalway.ie", 333333);
        Student student4 = new Student("Test B", "t.b1@universityofgalway.ie", 444444);
        mockStudentRepository.saveStudent(student1);
        mockStudentRepository.saveStudent(student2);
        mockStudentRepository.saveStudent(student3);
        mockStudentRepository.saveStudent(student4);
        Module module = new Module("TS101", 123456);
        module.addStudent(111111);
        module.addStudent(222222);
        module.addTAStudent(333333);
        module.addTAStudent(444444);
        mockModuleRepository.saveModule(module);
        Module module2 = new Module("TS201", 123456);
        mockModuleRepository.saveModule(module);
    }

    @Test
    public void createModuleValidIntegrationTest() {
        try {
            /* Checking that creating a valid module works */
            mockMvc.perform(post("/api/module/createmodule")
                            .param("lecturerId", "123456")
                            .param("moduleCode", "TS102")
                            .param("studentIds", "111111,222222")
                            .param("taStudentIds", "333333,444444")
                            .with(authentication(createMockJwtToken("123456", "Lecturer")))
                            .with(csrf()))
                   .andExpect(status().is(200));

            Module createdModule = mockModuleRepository.getModuleByCode("TS102");
            assertNotNull(createdModule);
            assertEquals(createdModule.getModuleCode(), "TS102");
            assertEquals(createdModule.getLecturer(), 123456);
            assertIterableEquals((Collection)createdModule.getStudents(), (Collection)Arrays.asList(new Long[] {111111L, 222222L}));
            assertIterableEquals((Collection)createdModule.getTAStudents(), (Collection)Arrays.asList(new Long[] {333333L, 444444L}));
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void createModuleInvalidIntegrationTest() {
        try {
            /* Checking that an empty module code returns an error */
            mockMvc.perform(post("/api/module/createmodule")
                            .param("lecturerId", "123456")
                            .param("moduleCode", "")
                            .param("studentIds", "111111,222222")
                            .param("taStudentIds", "333333,444444")
                            .with(authentication(createMockJwtToken("123456", "Lecturer")))
                            .with(csrf()))
                   .andExpect(status().is(400));

            /* Checking that creating a module with a non-existent lecturer returns an error */
            mockMvc.perform(post("/api/module/createmodule")
                            .param("lecturerId", "754321")
                            .param("moduleCode", "TS103")
                            .param("studentIds", "111111,222222")
                            .param("taStudentIds", "333333,444444")
                            .with(authentication(createMockJwtToken("754321", "Lecturer")))
                            .with(csrf()))
                   .andExpect(status().is(400));

            /* Checking that trying to create a module without proper parameters fails */
            mockMvc.perform(post("/api/module/createmodule")
                            .param("lecturerId", "123456")
                            .param("moduleCode", "TS103")
                            .param("taStudentIds", "333333,444444")
                            .with(authentication(createMockJwtToken("123456", "Lecturer")))
                            .with(csrf()))
                   .andExpect(status().is(400));

            /* Verifying that no such module was created */
            assertNull(mockModuleRepository.getModuleByCode("TS103"));

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void getModuleValidIntegrationTest() {
        try {
            /* Testing that getmodule returns the appropriate module */
            mockMvc.perform(get("/api/module/getmodule")
                            .param("moduleCode", "TS101")
                            .param("lecturerId", "123456")
                            .with(authentication(createMockJwtToken("123456", "Lecturer"))))
                   .andExpect(status()
                              .isOk())
                   .andExpect(content()
                              .json("{\"moduleCode\": \"TS101\"," +
                                    " \"lecturer\": 123456," + 
                                    " \"students\": [111111, 222222]," + 
                                    " \"tastudents\": [333333, 444444]}"));

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void getModuleInvalidIntegrationTest() {
        try {
            /* Checking that getting a module with an empty ID fails */
            mockMvc.perform(get("/api/module/getmodule")
                            .param("moduleCode", "")
                            .param("lecturerId", "123456")
                            .with(authentication(createMockJwtToken("123456", "Lecturer"))))
                   .andExpect(status().is(400));

            /* Checking that getting a non-existent module fails */
            mockMvc.perform(get("/api/module/getmodule")
                            .param("moduleCode", "TS111")
                            .param("lecturerId", "123456")
                            .with(authentication(createMockJwtToken("123456", "Lecturer"))))
                   .andExpect(status().is(400));

            /* Checking that getting a module without providing the appropriate lecturer ID fails */
            mockMvc.perform(get("/api/module/getmodule")
                            .param("moduleCode", "TS101")
                            .param("lecturerId", "754321")
                            .with(authentication(createMockJwtToken("754321", "Lecturer"))))
                   .andExpect(status().is(400));

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void getModuleAccessIntegrationTest() {
        try {
            /* Check that a Lecturer-authenticated user can access the module API */
            mockMvc.perform(get("/api/module/getmodule")
                            .param("moduleCode", "TS101")
                            .param("lecturerId", "123456")
                            .with(authentication(createMockJwtToken("123456", "Lecturer"))))
                   .andExpect(status().isOk());
            /* Check that a Student-authenticated user can't access the module API */
            mockMvc.perform(get("/api/module/getmodule")
                            .param("moduleCode", "TS101")
                            .param("lecturerId", "123456")
                            .with(authentication(createMockJwtToken("123456", "Student"))))
                   .andExpect(status().is(403));
            /* Check that a JWT authenticated user without a role can't access the module API */
            mockMvc.perform(get("/api/module/getmodule")
                            .param("moduleCode", "TS101")
                            .param("lecturerId", "123456")
                            .with(jwt()))
                   .andExpect(status().is(403));
            /* Check that a user without authentication can't access the module API */
            mockMvc.perform(get("/api/module/getmodule")
                            .param("moduleCode", "TS101")
                            .param("lecturerId", "123456"))
                   .andExpect(status().is(401));

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void getModulesValidIntegrationTest() {
        try {
            /* Getting expected modules */
            List<String> expectedCodes = mockModuleRepository.getModuleCodesByLecturer(123456);
            JSONArray expectedJson = new JSONArray(expectedCodes);
            /* Testing that getmodule returns the appropriate module */
            mockMvc.perform(get("/api/module/getmodules")
                            .param("lecturerId", "123456")
                            .with(authentication(createMockJwtToken("123456", "Lecturer"))))
                   .andExpect(status()
                              .isOk())
                   .andExpect(content()
                              .json(expectedJson.toString()));

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void getModulesInvalidIntegrationTest() {
        try {
            /* Checking that getting a module without providing a valid lecturer ID fails */
            mockMvc.perform(get("/api/module/getmodules")
                            .param("lecturerId", "754321")
                            .with(authentication(createMockJwtToken("754321", "Lecturer"))))
                   .andExpect(status().is(400));

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void getModulesAccessIntegrationTest() {
        try {
            /* Check that a Lecturer-authenticated user can access the module API */
            mockMvc.perform(get("/api/module/getmodules")
                            .param("lecturerId", "123456")
                            .with(authentication(createMockJwtToken("123456", "Lecturer"))))
                   .andExpect(status().isOk());
            /* Check that a Student-authenticated user can't access the module API */
            mockMvc.perform(get("/api/module/getmodules")
                            .param("lecturerId", "123456")
                            .with(authentication(createMockJwtToken("123456", "Student"))))
                   .andExpect(status().is(403));
            /* Check that a JWT authenticated user without a role can't access the module API */
            mockMvc.perform(get("/api/module/getmodules")
                            .param("lecturerId", "123456")
                            .with(jwt()))
                   .andExpect(status().is(403));
            /* Check that a user without authentication can't access the module API */
            mockMvc.perform(get("/api/module/getmodules")
                            .param("lecturerId", "123456"))
                   .andExpect(status().is(401));

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void addTAValidIntegrationTest() {
        try {
            /* Set up demo module */
            Module demoModule = new Module("TS104", 123456);
            mockModuleRepository.saveModule(demoModule);
            Student demoStudent = new Student("John John", "j.john1@universityofgalway.ie", 555555);
            mockStudentRepository.saveStudent(demoStudent);
            /* Checking that adding a valid TA to a module works */
            mockMvc.perform(post("/api/module/addta")
                            .param("moduleCode", "TS104")
                            .param("id", "555555")
                            .with(authentication(createMockJwtToken("123456", "Lecturer")))
                            .with(csrf()))
                   .andExpect(status()
                              .isOk());

            Module modifiedModule = mockModuleRepository.getModuleByCode("TS104");
            assertIterableEquals((Collection)modifiedModule.getTAStudents(), (Collection)Arrays.asList(new Long[] {555555L}));

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void addTAInvalidIntegrationTest() {
        try {
            /* Get the module before test cases run */
            Module moduleBeforeTest = mockModuleRepository.getModuleByCode("TS101");

            /* Checking that adding a TA to a non-existent module doesn't work */
            mockMvc.perform(post("/api/module/addta")
                            .param("moduleCode", "TS111")
                            .param("id", "333333")
                            .with(authentication(createMockJwtToken("123456", "Lecturer")))
                            .with(csrf()))
                   .andExpect(status().is(400));

            /* Checking that adding a TA with a negative ID doesn't work */
            mockMvc.perform(post("/api/module/addta")
                            .param("moduleCode", "TS101")
                            .param("id", "-33")
                            .with(authentication(createMockJwtToken("123456", "Lecturer")))
                            .with(csrf()))
                   .andExpect(status().is(500));

            /* Checking that adding a TA without proper arguments fails */
            mockMvc.perform(post("/api/module/addta")
                            .param("moduleCode", "TS101")
                            .with(authentication(createMockJwtToken("123456", "Lecturer")))
                            .with(csrf()))
                   .andExpect(status().is(400));

            /* Check that module was not changed */
            Module moduleAfterTest = mockModuleRepository.getModuleByCode("TS101");
            assertEquals(moduleBeforeTest.getModuleCode(), moduleAfterTest.getModuleCode());
            assertEquals(moduleBeforeTest.getLecturer(), moduleAfterTest.getLecturer());
            assertIterableEquals((Collection)moduleBeforeTest.getStudents(), (Collection)moduleAfterTest.getStudents());
            assertIterableEquals((Collection)moduleBeforeTest.getTAStudents(), (Collection)moduleAfterTest.getTAStudents());

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void removeTAValidIntegrationTest() {
        try {
            /* Set up demo module */
            Module demoModule = new Module("TS104", 123456);
            Student demoStudent = new Student("John John", "j.john1@universityofgalway.ie", 555555);
            mockStudentRepository.saveStudent(demoStudent);
            demoModule.addTAStudent(555555);
            mockModuleRepository.saveModule(demoModule);
            /* Checking that removing a valid TA from a module works */
            mockMvc.perform(post("/api/module/removeta")
                            .param("moduleCode", "TS104")
                            .param("id", "555555")
                            .with(authentication(createMockJwtToken("123456", "Lecturer")))
                            .with(csrf()))
                   .andExpect(status()
                              .isOk());
            /* Check that TA removed */
            Module moduleAfterTest = mockModuleRepository.getModuleByCode("TS104");
            assertTrue(moduleAfterTest.getTAStudents().isEmpty());

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void removeTAInvalidIntegrationTesat() {
        try {
            /* Get the module before test cases run */
            Module moduleBeforeTest = mockModuleRepository.getModuleByCode("TS101");

            /* Checking that removing a TA from a non-existent module doesn't work */
            mockMvc.perform(post("/api/module/removeta")
                            .param("moduleCode", "TS111")
                            .param("id", "333333")
                            .with(authentication(createMockJwtToken("123456", "Lecturer")))
                            .with(csrf()))
                   .andExpect(status().is(400));

            /* Checking that removing a TA with a negative ID doesn't work */
            mockMvc.perform(post("/api/module/removeta")
                            .param("moduleCode", "TS101")
                            .param("id", "-33")
                            .with(authentication(createMockJwtToken("123456", "Lecturer")))
                            .with(csrf()))
                   .andExpect(status().is(500));

            /* Checking that removing a student that's not a TA for the module doesn't work */
            mockMvc.perform(post("/api/module/removeta")
                            .param("moduleCode", "TS101")
                            .param("id", "111111")
                            .with(authentication(createMockJwtToken("123456", "Lecturer")))
                            .with(csrf()))
                   .andExpect(status().is(500));

            /* Checking that removing a TA without proper arguments fails */
            mockMvc.perform(post("/api/module/removeta")
                            .param("moduleCode", "TS101")
                            .with(authentication(createMockJwtToken("123456", "Lecturer")))
                            .with(csrf()))
                   .andExpect(status().is(400));

            /* Check that module was not changed */
            Module moduleAfterTest = mockModuleRepository.getModuleByCode("TS101");
            assertEquals(moduleBeforeTest.getModuleCode(), moduleAfterTest.getModuleCode());
            assertEquals(moduleBeforeTest.getLecturer(), moduleAfterTest.getLecturer());
            assertIterableEquals((Collection)moduleBeforeTest.getStudents(), (Collection)moduleAfterTest.getStudents());
            assertIterableEquals((Collection)moduleBeforeTest.getTAStudents(), (Collection)moduleAfterTest.getTAStudents());

        } catch(Exception e) {
            System.out.println(e);
        }
    }

}
