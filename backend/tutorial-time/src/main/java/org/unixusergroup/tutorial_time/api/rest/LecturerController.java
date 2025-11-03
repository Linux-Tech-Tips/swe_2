/** 
 * @author Ethan, Martin
 */
package org.unixusergroup.tutorial_time.api.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.unixusergroup.tutorial_time.application.module.CreateModuleUseCase;
import org.unixusergroup.tutorial_time.application.module.dto.CreateModuleRequest;
import org.unixusergroup.tutorial_time.application.module.dto.CreateModuleResponse;
import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;

import org.springframework.web.bind.annotation.*;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;
import org.unixusergroup.tutorial_time.domain.student.Student;

import java.util.List;

/** The API controller exposing Lecturer use cases */
@RestController
@RequestMapping(path="/api/lecturer")
public class LecturerController {
    private final LecturerRepository lecturerRepository;
    private final ModuleRepository moduleRepository;

    LecturerController(LecturerRepository lecturerRepository, ModuleRepository moduleRepository) {
        this.lecturerRepository = lecturerRepository;
        this.moduleRepository = moduleRepository;
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


    @PostMapping(path="/createModule")
    public ResponseEntity<?> createNewModule(@RequestParam long staffId, @RequestParam String moduleCode,
                                                          @RequestParam List<Student> students, @RequestParam List<Student> taStudents) {
      try {
          Lecturer lecturer = lecturerRepository.getLecturerById(staffId);
          CreateModuleUseCase createModuleUseCase = new CreateModuleUseCase(this.moduleRepository);
          CreateModuleRequest createModuleRequest = new CreateModuleRequest(moduleCode, students, taStudents, lecturer);
          CreateModuleResponse response = createModuleUseCase.execute(createModuleRequest);
          if(response.ok()) {
              return ResponseEntity.ok("Module created successfully.");
          }
          else {
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response.responseText());
          }
      } catch (IllegalArgumentException e) {
          return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Create module API: " + e.getMessage());
      }
    };
}
