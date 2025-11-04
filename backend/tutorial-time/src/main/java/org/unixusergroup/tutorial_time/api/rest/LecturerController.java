/** 
 * @author Ethan, Martin
 */
package org.unixusergroup.tutorial_time.api.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.unixusergroup.tutorial_time.domain.User;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
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

    @PostMapping(path="/createtimetable")
    public ResponseEntity<?> createLecturerTimetable(@RequestParam long staffId, @RequestParam int[] availability) {
        try {
            UseCase<CreateTimetableResponse, CreateTimetableRequest> createTimetable = new CreateTimetableUseCase(lecturerRepository);
            CreateTimetableRequest request = new CreateTimetableRequest(availability, staffId, User.UserType.LECTURER);
            CreateTimetableResponse response = createTimetable.execute(request);

            if(response.ok()) {
                return ResponseEntity.status(HttpStatus.OK).body("Timetable Created Successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response.responseText());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lecturer createtimetable API: " + e.getMessage());
        }
    }

    @PostMapping(path="/gettimetable")
    public ResponseEntity<?> getLecturerTimetable(@RequestParam long staffId) {
        try {
            UseCase<GetTimetableResponse, GetTimetableRequest> getTimetable = new GetTimetableUseCase(lecturerRepository);
            GetTimetableRequest request = new GetTimetableRequest(staffId, User.UserType.LECTURER);
            GetTimetableResponse response = getTimetable.execute(request);

            if(response.ok()) {
                StringBuilder result = new StringBuilder();
                for(int i = 0; i < response.availability().length; ++i) {
                    result.append("availability=").append(response.availability()[i]);
                    if(i < response.availability().length - 1) {
                        result.append("&");
                    }
                }
                return ResponseEntity.status(HttpStatus.OK).body(result.toString());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response.responseText());
            }
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lecturer gettimetable API: " + e.getMessage());
        }
    }

 }
