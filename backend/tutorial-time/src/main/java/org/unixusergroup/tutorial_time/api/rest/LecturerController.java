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

/** 
 * The API controller exposing Lecturer Use Cases.
 *
 * The API calls for Lecturer Use Cases are mapped to '/api/lecturer/...'
 */
@RestController
@CrossOrigin(origins=ProjectConfiguration.FRONTEND_DOMAIN_NAME)
@RequestMapping(path="/api/lecturer")
public class LecturerController {

    private final LecturerRepository lecturerRepository;

    /** Constructor, requires a LecturerRepository instance to save and load Lecturers */
    public LecturerController(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
    }

    /** 
     * Create Lecturer API mapping (/api/lecturer/createlecturer).
     *
     * This API call creates a new lecturer with the given parameters and saves them into the database.
     * @param name The name of the new lecturer
     * @param email The email of the new lecturer (must be a universityofgalway.ie or nuigalway.ie address)
     * @param staffId The University Staff ID of the new lecturer
     * @return HTTP status of 200 when created successfully,
     *         HTTP status of 500 and the error message when any other error occurs
     */
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

    /** 
     * Get Lecturer API mapping (/api/lecturer/getlecturer).
     *
     * This API call retrieves details about a lecturer based on a given ID from the database, if the lecturer exists.
     * @param staffId The University Staff ID of the lecturer to retrieve
     * @return HTTP status 200 and lecturer data in string JSON format if lecturer exists, 
     *         HTTP status of 500 and the error message when any other error occurs
     */
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

    /** 
     * Create Lecturer Timetable API mapping (/api/lecturer/createtimetable).
     *
     * This API call saves time availability data to a specified lecturer, if the lecturer exists.
     * @param staffId The University Staff ID of the lecturer to modify
     * @param availability Array of 40 integer values indicating availability, 
     *                     a flattened 2D matrix of 8 rows and 5 columns constructed by adding all the rows from the top to bottom.
     *                     The number 0 indicates 'Unavailable', the number 1 indicates 'Available'
     * @return HTTP status 200 if data written ok,
     *         HTTP status of 400 and an error message when an error occurs due to incorrect user input,
     *         HTTP status of 500 and the error message when any other error occurs
     */
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

    /**
     * Get Lecturer Timetable API mapping (/api/lecturer/gettimetable).
     *
     * This API call retrieves time availability data associated with a specified lecturer, if said lecturer exists.
     * @param staffId The University Staff ID of the lecturer to get the availability of
     * @return HTTP status 200 and the availability data in URLSearchParams compatible format,
     *         HTTP status of 400 and an error message when an error occurs due to incorrect user input,
     *         HTTP status of 500 and the error message when any other error occurs
     */
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
