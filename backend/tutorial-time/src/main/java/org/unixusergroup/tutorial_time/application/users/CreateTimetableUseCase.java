/** 
 * @author Martin
 */
package org.unixusergroup.tutorial_time.application.users;

import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.users.dto.*;

import org.unixusergroup.tutorial_time.domain.User;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;

/** Use case to create a Timetable instance from a given int[] and save it under a specific user */
public class CreateTimetableUseCase implements UseCase<CreateTimetableResponse, CreateTimetableRequest> {

    /** Student repository instance associated with the use case, for creating student timetables */
    private StudentRepository studentRepository;
    /** Lecturer repository instance associated with the use case, for creating lecturer timetables */
    private LecturerRepository lecturerRepository;

    public CreateTimetableUseCase(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        this.lecturerRepository = null;
    }

    public CreateTimetableUseCase(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
        this.studentRepository = null;
    }

    /** Creates a timetable from the request params, saves it into the appropriate repository and returns status */
    public CreateTimetableResponse execute(CreateTimetableRequest request) {
        /* Check that the availability field exists and is appropriate */
        if(request.availability().length != 40) {
            return new CreateTimetableResponse("Error: Incorrect number of timeslots in CreateTimetableUseCase");
        }

        /* Obtain appropriate user instance based on request type */
        User user = null;
        if(request.userType() == User.UserType.STUDENT && studentRepository != null) {
            user = (User)(studentRepository.getStudentById(request.userId()));
        } else if(request.userType() == User.UserType.LECTURER && lecturerRepository != null) {
            user = (User)(lecturerRepository.getLecturerById(request.userId()));
        } else {
            return new CreateTimetableResponse("Error: Incorrect user type in CreateTimetableUseCase");
        }

        /* Check that obtained user is valid */
        if(user == null) {
            return new CreateTimetableResponse("Error: User not found in CreateTimetableUseCase");
        }

        /* Populate the timetable instance with the given availability data */
        try {
            Timetable timetable = user.getTimetable();
            for(int col = 0; col < timetable.getColumns(); ++col) {
                for(int row = 0; row < timetable.getRows(); ++row) {
                    int flatIdx = timetable.getFlatIndex(col, row);
                    int value = request.availability()[flatIdx];
                    if(value == 0)
                        user.setUnavailable(col, row);
                    else if(value == 1)
                        user.setAvailable(col, row);
                    else
                        return new CreateTimetableResponse("Error: Incorrect value " + value + " for user availability in CreateTimetableUseCase");
                }
            }
        } catch(Exception e) {
            return new CreateTimetableResponse(e.getMessage());
        }

        /* Save the modified user instance in the appropriate repository */
        if(request.userType() == User.UserType.STUDENT) {
            studentRepository.saveStudent((Student)(user));
        } else if(request.userType() == User.UserType.LECTURER) {
            lecturerRepository.saveLecturer((Lecturer)(user));
        } else {
            return new CreateTimetableResponse("Error: Incorrect user type in CreateTimetableUseCase");
        }
        return new CreateTimetableResponse();
    }
}
