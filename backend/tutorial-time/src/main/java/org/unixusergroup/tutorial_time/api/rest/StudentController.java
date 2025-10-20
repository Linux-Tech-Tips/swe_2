/** 
 * @author Ethan, Martin
 */
package org.unixusergroup.tutorial_time.api.rest;

import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;

import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** The API controller exposing Student Use Cases */
@RestController
@RequestMapping(path="/api/student")
public class StudentController {
    StudentRepository studentRepository;

    StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    @PostMapping(path="/addstudent")
    public ResponseEntity<?> addStudent(@RequestParam int studentId, @RequestParam String name) {
        // TODO IMPLEMENT THIS WITH A USE CASE
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("NOT IMPLEMENTED");
        //try {
        //    Student s = new Student();
        //    s.setStudentID(studentId);
        //    s.setName(name);
        //    Student saved = studentRepository.save(s);
        //    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        //} catch (Exception e) {
        //    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        //}
    }

    @GetMapping(path="/list")
    public ResponseEntity<?> listStudents() {
        // TODO IMPLEMENT THIS WITH A USE CASE
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("NOT IMPLEMENTED");
        //try {
        //    return ResponseEntity.status(HttpStatus.FOUND).body(studentRepository.findAll());
        //} catch (Exception e) {
        //    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        //}
    }

    @PostMapping(path="/addtime")
    public ResponseEntity<?> addBusyTimeToStudent(@RequestParam int studentId, @RequestParam String day, @RequestParam int hour) {
        // TODO IMPLEMENT THIS WITH A USE CASE
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("NOT IMPLEMENTED");
        //try {
        //    Timeslot time = new Timeslot(day, hour);
        //    Student s = studentRepository.findByStudentID(studentId);
        //    s.addBusyTime(time);
        //    studentRepository.save(s);
        //
        //    return ResponseEntity.status(HttpStatus.CREATED).body(time);
        //} catch (Exception e) {
        //    return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(e.getMessage());
        //}
    }

    @PostMapping(path="/removetime")
    public ResponseEntity<?> removeBusyTimeFromStudent(@RequestParam int studentId, @RequestParam String day, @RequestParam int hour) {
        // TODO IMPLEMENT THIS WITH A USE CASE
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("NOT IMPLEMENTED");
        //try {
        //    Timeslot time = new Timeslot(day, hour);
        //    Student s = studentRepository.findByStudentID(studentId);
        //    s.addFreeTime(time);
        //    studentRepository.save(s);
        //    return ResponseEntity.status(HttpStatus.ACCEPTED).body(time);
        //} catch (Exception e) {
        //    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        //}
    }
}
