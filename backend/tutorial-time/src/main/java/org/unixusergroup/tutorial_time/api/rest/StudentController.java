/** 
 * @author Ethan, Martin
 */
package org.unixusergroup.tutorial_time.api.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.unixusergroup.tutorial_time.application.users.AddStudentUseCase;
import org.unixusergroup.tutorial_time.application.users.GetStudentUseCase;
import org.unixusergroup.tutorial_time.domain.User;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.users.CreateTimetableUseCase;
import org.unixusergroup.tutorial_time.application.users.GetTimetableUseCase;
import org.unixusergroup.tutorial_time.application.users.dto.*;
import org.unixusergroup.tutorial_time.infrastructure.ProjectConfiguration;

/** 
 * The API controller exposing Student Use Cases.
 *
 * The API calls for Student Use Cases are mapped to '/api/student/...'
 */
@RestController
@CrossOrigin(origins=ProjectConfiguration.FRONTEND_DOMAIN_NAME)
@RequestMapping(path="/api/student")
public class StudentController {

    private final StudentRepository studentRepository;

    /** Constructor, requires a StudentRepository instance to save and load Students */
    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /** 
     * Create Student API mapping (/api/student/createstudent).
     *
     * This API call creates a new student with the given parameters and saves them into the database.
     * @param name The name of the new student
     * @param email The email of the new student (must be a universityofgalway.ie or nuigalway.ie address)
     * @param studentId The University Student ID of the new student
     * @return HTTP status of 200 when created successfully,
     *         HTTP status of 500 and the error message when any other error occurs
     */
    @PostMapping(path="/createstudent")
    public ResponseEntity<?> addStudent(@RequestParam String name, @RequestParam String email, @RequestParam long studentId) {
        try {
            AddStudentRequest addStudentRequest = new AddStudentRequest(name, email, studentId);
            AddStudentUseCase addStudentUseCase = new AddStudentUseCase(this.studentRepository);
            AddStudentResponse response = addStudentUseCase.execute(addStudentRequest);

            if (response.ok()) {
                return ResponseEntity.status(HttpStatus.OK).body("Student added successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response.responseText());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Student add student API: " + e.getMessage());
        }
    }

    /** 
     * Get Student API mapping (/api/student/getstudent).
     *
     * This API call retrieves details about a student based on a given student ID from the database, if said student exists.
     * @param studentId The University Student ID of the student to retrieve
     * @return HTTP status of 200 and student data in string JSON format if student exists,
     *         HTTP status of 500 and the error message when any other error occurs
     */
    @GetMapping(path="/getstudent")
    public ResponseEntity<?> getStudent(@RequestParam long studentId) {
        try {
            GetStudentRequest getStudentRequest = new GetStudentRequest(studentId);
            GetStudentUseCase getStudentUseCase = new GetStudentUseCase(this.studentRepository);
            GetStudentResponse response = getStudentUseCase.execute(getStudentRequest);

            if (response.ok()) {
                return ResponseEntity.status(HttpStatus.OK).body(response.student());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response.responseText());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Student get student API: " + e.getMessage());
        }
    }

    /** 
     * Create Student Timetable API mapping (/api/student/createtimetable).
     *
     * This API call saves time availability data to a specified student, if the student exists.
     * @param studentId The University Student ID of the student to modify
     * @param availability Array of 40 integer values indicating availability, 
     *                     a flattened 2D matrix of 8 rows and 5 columns constructed by adding all the rows from top to bottom.
     *                     The number 0 indicates 'Unavailable', the number 1 indicates 'Available'
     * @return HTTP status 200 if data written ok,
     *         HTTP status of 500 and the error message when any other error occurs
     */
    @PostMapping(path="/createtimetable")
    public ResponseEntity<?> createStudentTimetable(@RequestParam long studentId, @RequestParam int[] availability) {
        try {
            UseCase<CreateTimetableResponse, CreateTimetableRequest> createTimetable = new CreateTimetableUseCase(studentRepository);
            CreateTimetableRequest request = new CreateTimetableRequest(availability, studentId, User.UserType.STUDENT);
            createTimetable.execute(request);
            return ResponseEntity.status(HttpStatus.OK).body("Timetable Created Successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Student createtimetable API: " + e.getMessage());
        }
    }

    /**
     * Get Student Timetable API mapping (/api/student/gettimetable).
     *
     * This API call retrieves the time availability data associated with a specified student, if said student exists.
     * @param studentId The University Student ID of the student to get the availability of
     * @return HTTP status 200 and the availability data in a URLSearchParams compatible format,
     *         HTTP status of 500 and the error message when any other error occurs
     */
    @PostMapping(path="/gettimetable")
    public ResponseEntity<?> getStudentTimetable(@RequestParam long studentId) {
        try {
            UseCase<GetTimetableResponse, GetTimetableRequest> getTimetable = new GetTimetableUseCase(studentRepository);
            GetTimetableRequest request = new GetTimetableRequest(studentId, User.UserType.STUDENT);
            GetTimetableResponse response = getTimetable.execute(request);

            StringBuilder result = new StringBuilder();
            for(int i = 0; i < response.availability().length; ++i) {
                result.append("availability=").append(response.availability()[i]);
                if(i < response.availability().length - 1) {
                    result.append("&");
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(result.toString());
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Student gettimetable API: " + e.getMessage());
        }
    }
}
