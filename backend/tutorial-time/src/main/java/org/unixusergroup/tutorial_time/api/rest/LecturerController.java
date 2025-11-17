/**
 * @author Ethan, Martin
 */
package org.unixusergroup.tutorial_time.api.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.unixusergroup.tutorial_time.application.users.AddLecturerUseCase;
import org.unixusergroup.tutorial_time.application.users.GetLecturerUseCase;
import org.unixusergroup.tutorial_time.domain.User;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.users.CreateTimetableUseCase;
import org.unixusergroup.tutorial_time.application.users.GetTimetableUseCase;
import org.unixusergroup.tutorial_time.application.users.dto.*;
import org.unixusergroup.tutorial_time.infrastructure.ProjectConfiguration;

/** The API controller exposing Lecturer use cases */
@RestController
@CrossOrigin(origins=ProjectConfiguration.FRONTEND_DOMAIN_NAME)
@RequestMapping(path="/api/lecturer")
public class LecturerController {
    LecturerRepository lecturerRepository;

    public LecturerController(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
    }

    @PostMapping(path="/createlecturer")
    public ResponseEntity<?> addLecturer(@RequestParam String name, @RequestParam String email, @RequestParam long staffId) {
        try {
            AddLecturerUseCase addLecturerUseCase = new AddLecturerUseCase(this.lecturerRepository);
            AddLecturerRequest addLecturerRequest = new AddLecturerRequest(name, email, staffId);
            AddLecturerResponse response = addLecturerUseCase.execute(addLecturerRequest);

            if(response.ok()) {
                return ResponseEntity.status(HttpStatus.OK).body("Lecturer was added successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response.responseText());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lecturer addLecturer API: " + e.getMessage());
        }
    }
    @GetMapping(path="/getlecturer")
    public ResponseEntity<?> getLecturer(@RequestParam long staffId) {
        try {
            GetLecturerRequest getLecturerRequest = new GetLecturerRequest(staffId);
            GetLecturerUseCase getLecturerUseCase = new GetLecturerUseCase(this.lecturerRepository);
            GetLecturerResponse response = getLecturerUseCase.execute(getLecturerRequest);

            if (response.ok()) {
                return ResponseEntity.status(HttpStatus.OK).body(response.lecturer());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response.responseText());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lecturer getLecturer API: " + e.getMessage());
        }
    }

    @PostMapping(path="/createtimetable")
    public ResponseEntity<?> createLecturerTimetable(@RequestParam long staffId, @RequestParam int[] availability) {
        try {
            UseCase<CreateTimetableResponse, CreateTimetableRequest> createTimetable = new CreateTimetableUseCase(lecturerRepository);
            CreateTimetableRequest request = new CreateTimetableRequest(availability, staffId, User.UserType.LECTURER);
            createTimetable.execute(request);
            return ResponseEntity.status(HttpStatus.OK).body("Timetable Created Successfully");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lecturer Create Timetable API: " + e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lecturer createtimetable API: " + e.getMessage());
        }
    }

    @PostMapping(path="/gettimetable")
    public ResponseEntity<?> getLecturerTimetable(@RequestParam long staffId) {
        try {
            UseCase<GetTimetableResponse, GetTimetableRequest> getTimetable = new GetTimetableUseCase(lecturerRepository);
            GetTimetableRequest request = new GetTimetableRequest(staffId, User.UserType.LECTURER);
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lecturer Get Timetable API: " + e.getMessage());
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lecturer gettimetable API: " + e.getMessage());
        }
    }

}