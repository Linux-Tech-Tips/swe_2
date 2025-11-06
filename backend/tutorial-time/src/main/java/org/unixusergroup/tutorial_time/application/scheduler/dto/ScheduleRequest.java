/**
 * dto for requesting tutorials to be scheduled
 * @author Cedar
 */

package org.unixusergroup.tutorial_time.application.scheduler.dto;

import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;
import org.unixusergroup.tutorial_time.domain.scheduler.Constraints;
import org.unixusergroup.tutorial_time.domain.scheduler.Scheduler;
import org.unixusergroup.tutorial_time.domain.student.Student;

import java.util.ArrayList;
import java.util.List;

public record ScheduleRequest(String moduleCode, int studentLimit, int minTAs, int[] blockedTimes, ModuleRepository moduleRepository, StudentRepository studentRepository) {

    /**
     * method to create the constraints based on the parameters passed in
     * @return Constraints object
     */
    public Constraints constraints() throws RuntimeException{
        try {
            return new Constraints(studentLimit, minTAs, blockedTimes);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method to create the scheduler
     * it finds the timetables of the given module code and passes that into the scheduler constructor
     * if a minTA constraint is given, it compiles the TA timetables and calls the constructor with them
     * @return Scheduler object
     */
    public Scheduler scheduler() {
        ArrayList<int[]> timetables = new ArrayList<>();

        try {
            Module module = moduleRepository.getModuleByCode(moduleCode);
            System.out.println(module);
            List<Long> students = module.getStudents();
            for(Long student : students) {
                timetables.add(studentRepository.getStudentById(student).getTimetable().getArray());
            }
            if(minTAs>0) {
                ArrayList<int[]> taTimetables = new ArrayList<>();
                List<Long> TAs = module.getTAStudents();
                for(Long student : TAs) {
                    timetables.add(studentRepository.getStudentById(student).getTimetable().getArray());
                }
                return new Scheduler(timetables, taTimetables);
            }
            return new Scheduler(timetables);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
}
