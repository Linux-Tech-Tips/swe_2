package com.example.FACTS.application;

import com.example.FACTS.domain.Tutorial.Availability;
import com.example.FACTS.domain.Tutorial.Timeslot;
import com.example.FACTS.infrastructure.CourseRepository;
import com.example.FACTS.domain.student.Student;

import com.example.FACTS.infrastructure.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * This will call upon the ScheduleUpdateUseCase by blocking out and entering timeslots.
 *
 */

@Service
public class TimeModificationService {

    @Autowired
    private final CourseRepository courseRepository;
    private StudentRepository studentRepository;

    TimeModificationService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // Block time slot - course
    StudentRepository s_r;
    public @ResponseBody String blockCertainTimeSlot(@RequestParam int studentID, @RequestParam String day, @RequestParam int hour) {
        Timeslot time = new Timeslot(day, hour);
        time.setAvailability(Availability.UNAVAILABLE);

        Student s = studentRepository.findById(studentID);
        s.addBusyTime(time);
        studentRepository.save(s);

        return "Changed";
    }
    // Enter Time slot

    // Enter Time slot - course

    //
}
