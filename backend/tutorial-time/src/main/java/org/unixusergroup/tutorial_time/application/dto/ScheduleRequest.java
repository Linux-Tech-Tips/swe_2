/**
 * dto for requesting tutorials to be scheduled
 * @author Cedar
 */

package org.unixusergroup.tutorial_time.application.dto;

import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;
import org.unixusergroup.tutorial_time.domain.scheduler.Constraints;
import org.unixusergroup.tutorial_time.domain.scheduler.Scheduler;
import org.unixusergroup.tutorial_time.domain.student.Student;

import java.util.ArrayList;
import java.util.List;

//TODO is this where this stuff belongs???
// TODO: figure out constraints
public record ScheduleRequest(String moduleCode, Constraints constraints) {

    private static ModuleRepository moduleRepository;

    public Constraints constraints() {
        return constraints;
    }

    public Scheduler scheduler() {
        ArrayList<Timetable> timetables = new ArrayList<>();
        ArrayList<int[]> integerTimetables = new ArrayList<>();

        Module module = moduleRepository.getModuleByCode(moduleCode);
        List<Student> students = module.getStudents();
        for(Student student : students) {
            //TODO: convert the student timetables to int[] format and add them to the list
            timetables.add(student.getTimetable());
        }
        //TODO: create the scheduler
        return null;
    }
}
