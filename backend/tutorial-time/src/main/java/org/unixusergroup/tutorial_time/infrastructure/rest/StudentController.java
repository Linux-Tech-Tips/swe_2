package org.unixusergroup.tutorial_time.infrastructure.rest;

import org.unixusergroup.tutorial_time.domain.Tutorial.Timeslot;
import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
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

    @PostMapping(path="/addstudent")
    public @ResponseBody String addStudent(@RequestParam int studentId, @RequestParam String name) {
        Student s = new Student(name, studentId);
        studentRepository.save(s);
        return "Student made";
    }

    @GetMapping(path="/list")
    public @ResponseBody Iterable<Student> listStudents() {
        return studentRepository.findAll();
    }
    @PostMapping(path="/addtime")
    public @ResponseBody String addBusyTimeToStudent(@RequestParam int studentId, @RequestParam String day, @RequestParam int hour) {
        Timeslot time = new Timeslot(day, hour);
        Student s = studentRepository.findByStudentID(studentId);
        s.addBusyTime(time);
        studentRepository.save(s);

        return "Time entered";
    }

    @PostMapping(path="/removetime")
    public @ResponseBody String removeBusyTimeFromStudent(@RequestParam int studentId, @RequestParam String day, @RequestParam int hour) {
        Timeslot time = new Timeslot(day, hour);
        Student s = studentRepository.findByStudentID(studentId);
        s.addFreeTime(time);
        studentRepository.save(s);
        return "Time removed";
    }
}
