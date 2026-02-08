/**
 * class that instantiates a scheduler object and calls the relevant methods to return tutorial times
 * @author Cedar
 */
package org.unixusergroup.tutorial_time.application.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.unixusergroup.tutorial_time.application.common.UseCase;
import org.unixusergroup.tutorial_time.application.scheduler.dto.ScheduleRequest;
import org.unixusergroup.tutorial_time.application.scheduler.dto.ScheduleResponse;
import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;
import org.unixusergroup.tutorial_time.domain.scheduler.*;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
import org.unixusergroup.tutorial_time.domain.student.Student;

public class ScheduleTutorialsUseCase implements UseCase <ScheduleResponse, ScheduleRequest> {
    ModuleRepository moduleRepository;
    StudentRepository studentRepository;

    /**
     * constructor
     */
    public ScheduleTutorialsUseCase(ModuleRepository moduleRepository, StudentRepository studentRepository){
        this.moduleRepository = moduleRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * method that schedules tutorials
     * first it initialises the constraints and scheduler objects
     * then it calls the constraints on the scheduler (if necessary)
     * then all scheduling methods
     * @return ScheduleResponse object
     */
    public ScheduleResponse execute(ScheduleRequest request) {
        Constraints constraints =  new Constraints(request.studentLimit(), request.minTAs(), request.blockedTimes());

        /* Loading student timetables */
        ArrayList<int[]> timetables = new ArrayList<>();
        int missingStudents = 0;
        int missingTAs = 0;
        int busyStudents = 0;
        int busyTAs = 0;

        Module module = moduleRepository.getModuleByCode(request.moduleCode());
        if(module==null) {
            throw new IllegalArgumentException("The given module could not be found");
        }

        List<Long> students = module.getStudents();
        for(Long student : students) {
            Student loadedStudent = studentRepository.getStudentById(student);
            if(loadedStudent == null) {
                ++missingStudents;
            }
            else if(loadedStudent.busyAlways()) {
                ++busyStudents;
            }
            else {
                timetables.add(loadedStudent.getTimetable().getArray());
            }
        }

        /* Creating scheduler instance and setting params */
        Scheduler scheduler = new Scheduler(timetables);
        if(request.minTAs()>0) {
            ArrayList<int[]> taTimetables = new ArrayList<>();
            List<Long> TAs = module.getTAStudents();
            for(Long student : TAs) {
                Student loadedTA = studentRepository.getStudentById(student);
                if(loadedTA == null) {
                    ++missingTAs;
                }
                else if(loadedTA.busyAlways()) {
                    ++busyTAs;
                }
                else {
                    taTimetables.add(loadedTA.getTimetable().getArray());
                }
            }
            scheduler = new Scheduler(timetables, taTimetables);
        }

        /* Creating additional response string if missing students found */
        String warning = "";
        if(missingStudents > 0) {
            warning += "WARNING: There are (" +
                       missingStudents +
                       ") students not registered in the app (as they didn't provide a timetable, the scheduling may be affected).\n";
        }
        if(missingTAs > 0) {
            warning += "WARNING: There are (" +
                       missingTAs +
                       ") TA students not registered in the app (scheduling may be affected).\n";
        }
        if(busyStudents > 0) {
            warning += "WARNING: There are (" + busyStudents + ") students who registered themselves as busy at all times. These students have been removed from consideration.\n";
        }
        if(busyTAs > 0) {
            warning += "WARNING: There are (" + busyTAs + ") TA students who registered themselves as busy at all times. These TAs have been removed from consideration.\n";
        }

        //if there is a minimum number of TAs to be at each tutorial --> remove times as needed
        if(constraints.minTAs()) {
            scheduler.minimumNumberOfTAs(request.minTAs());
        }
        //if there are times to be blocked out --> remove those
        if(constraints.blockedTimes()) {
            scheduler.blockOutTimes(constraints.getBlockedTimes());
        }

        //if there is a limited number of students per tutorial --> edit the data as needed
        if(constraints.studentLimit()) {
            scheduler.classSizeLimit(constraints.getStudentLimit());
        }

        //if base case 1 returns any times --> return those
        String times = scheduler.checkBaseCaseOne();
        if(!times.isEmpty()) {
            return new ScheduleResponse(warning + times);
        }

        //if base case 2 returns any times --> return those
        times = scheduler.checkBaseCaseTwo();
        if(!times.isEmpty()) {
            return new ScheduleResponse(warning + times);
        }

        //call the actual scheduling algorithm, return those values
        return new ScheduleResponse(warning + scheduler.schedule());
    }
}
