/** 
 * @author Ethan, Martin
 */
package org.unixusergroup.tutorial_time.api.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unixusergroup.tutorial_time.domain.User;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.users.CreateTimetableUseCase;
import org.unixusergroup.tutorial_time.application.users.GetTimetableUseCase;
import org.unixusergroup.tutorial_time.application.users.dto.*;
import org.unixusergroup.tutorial_time.infrastructure.ProjectConfiguration;

/** The API controller exposing Student Use Cases */
@RestController
@CrossOrigin(origins=ProjectConfiguration.FRONTEND_DOMAIN_NAME)
@RequestMapping(path="/api/student")
public class StudentController {
    StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @PostMapping(path="/addstudent")
    public ResponseEntity<?> addStudent(@RequestParam int studentId, @RequestParam String name) {
        // TODO IMPLEMENT THIS WITH A USE CASE
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("NOT IMPLEMENTED");
    }

    @PostMapping(path="/createtimetable")
    public ResponseEntity<?> createStudentTimetable(@RequestParam long studentId, @RequestParam int[] availability) {
        try {
            UseCase<CreateTimetableResponse, CreateTimetableRequest> createTimetable = new CreateTimetableUseCase(studentRepository);
            CreateTimetableRequest request = new CreateTimetableRequest(availability, studentId, User.UserType.STUDENT);
            createTimetable.execute(request);
            return ResponseEntity.status(HttpStatus.OK).body("Timetable Created Successfully");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Student Create Timetable API: " + e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Student createtimetable API: " + e.getMessage());
        }
    }

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
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Student Get Timetable API: " + e.getMessage());
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Student gettimetable API: " + e.getMessage());
        }
    }
}
