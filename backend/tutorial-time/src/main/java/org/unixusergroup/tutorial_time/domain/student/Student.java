/** 
 * @author Ethan, Martin
 */
package org.unixusergroup.tutorial_time.domain.student;

import org.unixusergroup.tutorial_time.domain.User;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;

/** Class representing a Student entity, inheriting from User */
public class Student extends User {

    public Student(String name, String email, long studentId) {
        super(name, email, studentId);
    }

    public Student(String name, String email, long studentId, Timetable timetable) {
        super(name, email, studentId, timetable);
    }
}
