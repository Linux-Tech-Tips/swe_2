package org.unixusergroup.tutorial_time.infrastructure.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.unixusergroup.tutorial_time.domain.Courses.Course;
import org.unixusergroup.tutorial_time.domain.Courses.CourseRepository;
import org.unixusergroup.tutorial_time.domain.Tutorial.Timeslot;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
import org.springframework.web.bind.annotation.*;
import org.unixusergroup.tutorial_time.domain.student.Student;

import java.util.List;

@RestController
@RequestMapping(path="/api/lecturer")
public class LecturerController {
    LecturerRepository lecturerRepository;
    CourseRepository courseRepository;
    LecturerController(LecturerRepository lecturerRepository, CourseRepository courseRepository) {
        this.lecturerRepository = lecturerRepository;
        this.courseRepository = courseRepository;
    }

    @PostMapping(path="/add")
    public @ResponseBody String addBusyTimeToLecturer(@RequestParam int staffId, @RequestParam String day, @RequestParam int hour) {
        Timeslot time = new Timeslot(day, hour);
        Lecturer l  = lecturerRepository.findByStaffID(staffId);
        l.addBusyTime(time);
        lecturerRepository.save(l);
        return "Time entered";
    }

    @PostMapping(path="/remove")
    public @ResponseBody String removeBusyTimeFromLecturer(@RequestParam int staffId, @RequestParam String day, @RequestParam int hour) {
        Timeslot time = new Timeslot(day, hour);
        Lecturer l = lecturerRepository.findByStaffID(staffId);
        l.addFreeTime(time);
        lecturerRepository.save(l);
        return "Time removed";
    }
    @PostMapping(path="/createCourse")
    public ResponseEntity<?> createModule(@RequestParam String courseCode, @RequestParam int staffID, @RequestParam List<Student> enrollingStudents) {
        try {
            Lecturer lecturer = lecturerRepository.findByStaffID(staffID);
            Course course = new Course();
            course.setCode(courseCode);
            course.setEnlistedStudents(enrollingStudents);
            course.setLecturer(lecturer);

            lecturer.addCourse(course);
            for (Student s: enrollingStudents) {
                s.addCourse(course);
            }
            return ResponseEntity.status(HttpStatus.OK).body(courseRepository.save(course));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        }
    }
}
