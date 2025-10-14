/**
 * A class representing the basis for a student and a lecturer.
 */
package org.unixusergroup.tutorial_time.domain;


import org.unixusergroup.tutorial_time.domain.Courses.Course;
import org.unixusergroup.tutorial_time.domain.Tutorial.TimeTable;
import java.util.List;

import org.unixusergroup.tutorial_time.domain.Tutorial.Timeslot;


public class User {

//    @Id
  //  @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    protected String name;
    protected long universityId;
    protected List<Course> courses;
    protected TimeTable timeTable;


    protected User() {}

    public User(String name, long universityId) {
        this.name = name;
        this.universityId = universityId;
    }
    public User(String name, List<Course> courses) {
        this.name = name;
        this.courses = courses;
        this.timeTable = new TimeTable();
    }

    public User(String name, List<Course> courses, TimeTable timetable) {
        this.name = name;
        this.courses = courses;
        this.timeTable = timetable;
    }

    @Override
    public String toString() {
        return String.format("%s[name='%s', courseCode='%s']", this.getClass(), this.name, this.courses);
    }

    public String getName() {
        return this.name;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public void addBusyTime(Timeslot time) {
        this.timeTable.setUnavailableTimeSlot(time);
    }
    public void addFreeTime(Timeslot time) {
        this.timeTable.setAvailableTimeSlot(time);
    }
    public void addCourse(Course course) {
        this.courses.add(course);
    }

}
