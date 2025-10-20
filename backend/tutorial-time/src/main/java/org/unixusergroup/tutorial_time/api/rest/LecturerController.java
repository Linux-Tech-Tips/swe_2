/** 
 * @author Ethan, Martin
 */
package org.unixusergroup.tutorial_time.api.rest;

import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;

import org.springframework.web.bind.annotation.*;

/** The API controller exposing Lecturer use cases */
@RestController
@RequestMapping(path="/api/lecturer")
public class LecturerController {
    LecturerRepository lecturerRepository;

    LecturerController(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
    }

    @PostMapping(path="/add")
    public @ResponseBody String addBusyTimeToLecturer(@RequestParam int staffId, @RequestParam String day, @RequestParam int hour) {
        // TODO IMPLEMENT THIS WITH A USE CASE
        return "UNIMPLEMENTED";
        //Timeslot time = new Timeslot(day, hour);
        //Lecturer l  = lecturerRepository.findByStaffID(staffId);
        //l.addBusyTime(time);
        //lecturerRepository.save(l);
        //return "Time entered";
    }

    @PostMapping(path="/remove")
    public @ResponseBody String removeBusyTimeFromLecturer(@RequestParam int staffId, @RequestParam String day, @RequestParam int hour) {
        // TODO IMPLEMENT THIS WITH A USE CASE
        return "UNIMPLEMENTED";
        //Timeslot time = new Timeslot(day, hour);
        //Lecturer l = lecturerRepository.findByStaffID(staffId);
        //l.addFreeTime(time);
        //lecturerRepository.save(l);
        //return "Time removed";
    }
}
