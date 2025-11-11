/** 
 * @author Martin
 */
package org.unixusergroup.tutorial_time.application.users;

import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.users.dto.*;
import org.unixusergroup.tutorial_time.domain.User;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;

/** Use case to get the saved timetable of a user with a given ID */
public class GetTimetableUseCase implements UseCase<GetTimetableResponse, GetTimetableRequest> {

    /** Student repository instance associated with the use case, for obtaining student timetables */
    private StudentRepository studentRepository;
    /** Lecturer repository instance associated with the use case, for obtaining lecturer timetables */
    private LecturerRepository lecturerRepository;

    public GetTimetableUseCase(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        this.lecturerRepository = null;
    }

    public GetTimetableUseCase(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
        this.studentRepository = null;
    }

    /** Gets a Timetable based on the request params, returns status and an availability array if successful */
    public GetTimetableResponse execute(GetTimetableRequest request) {
        /* Get appropriate user based on request type */
        User user = null;
        if(request.userType() == User.UserType.STUDENT && studentRepository != null) {
            user = (User)(studentRepository.getStudentById(request.userId()));
        } else if(request.userType() == User.UserType.LECTURER && lecturerRepository != null) {
            user = (User)(lecturerRepository.getLecturerById(request.userId()));
        } else {
            return new GetTimetableResponse("Error: Incorrect user type in GetTimetableUseCase");
        }
 
        /* Check that obtained user is valid */
        if(user == null) {
            return new GetTimetableResponse("Error: User not found in GetTimetableUseCase");
        }

        /* Get timetable of given user loaded from database */
        int[] availability = user.getTimetable().getArray();

        /* Check that availability valid and return */
        if(availability.length != 40) {
            return new GetTimetableResponse("Error: Malformed availability array in database in GetTimetableUseCase");
        }
        return new GetTimetableResponse(availability);
    }
}
