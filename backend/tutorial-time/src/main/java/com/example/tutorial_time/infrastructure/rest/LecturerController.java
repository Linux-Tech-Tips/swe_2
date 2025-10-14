package com.example.tutorial_time.infrastructure.rest;

import ch.qos.logback.core.rolling.LengthCounter;
import com.example.tutorial_time.domain.Tutorial.Timeslot;
import com.example.tutorial_time.domain.lecturer.Lecturer;
import com.example.tutorial_time.domain.lecturer.LecturerRepository;
import com.example.tutorial_time.domain.student.Student;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/lecturer")
public class LecturerController {
    LecturerRepository lecturerRepository;

    LecturerController(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
    }

    @PostMapping(path="/add")
    public @ResponseBody String addBusyTimeToLecturer(@RequestParam int staffId, @RequestParam String day, @RequestParam int hour) {
        Timeslot time = new Timeslot(day, hour);
        Lecturer l  = lecturerRepository.findByStaffId(staffId);
        l.addBusyTime(time);
        lecturerRepository.save(l);
        return "Time entered";
    }

    @PostMapping(path="/remove")
    public @ResponseBody String removeBusyTimeFromLecturer(@RequestParam int staffId, @RequestParam String day, @RequestParam int hour) {
        Timeslot time = new Timeslot(day, hour);
        Lecturer l = lecturerRepository.findByStaffId(staffId);
        l.addFreeTime(time);
        lecturerRepository.save(l);
        return "Time removed";
    }
}
