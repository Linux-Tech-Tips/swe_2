package com.example.tutorial_time.infrastructure.rest;

import com.example.tutorial_time.domain.Tutorial.Timeslot;
import com.example.tutorial_time.domain.student.Student;
import com.example.tutorial_time.domain.student.StudentRepository;
import org.springframework.web.bind.annotation.*;


/**
 * Solely has functionality for adding and removing times from student's timetable.
 */
@RestController
@RequestMapping(path="/api/student")
public class StudentController {
    StudentRepository studentRepository;

    StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    @PostMapping(path="/add")
    public @ResponseBody String addBusyTimeToStudent(@RequestParam int studentId, @RequestParam String day, @RequestParam int hour) {
        Timeslot time = new Timeslot(day, hour);
        Student s = studentRepository.findByStudentId(studentId);
        s.addBusyTime(time);
        studentRepository.save(s);

        return "Time entered";
    }

    @PostMapping(path="/remove")
    public @ResponseBody String removeBusyTimeFromStudent(@RequestParam int studentId, @RequestParam String day, @RequestParam int hour) {
        Timeslot time = new Timeslot(day, hour);
        Student s = studentRepository.findByStudentId(studentId);
        s.addFreeTime(time);
        studentRepository.save(s);
        return "Time removed";
    }
}
