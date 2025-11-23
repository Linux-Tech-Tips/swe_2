/** 
 * @author Ethan, Martin
 */
package org.unixusergroup.tutorial_time.domain.lecturer;

import jakarta.persistence.Entity;
import org.unixusergroup.tutorial_time.domain.User;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;

@Entity
/** Class representing a Lecturer entity, inheriting from User */
public class Lecturer extends User {

    public Lecturer() {};
    public Lecturer(String name, String email, long staffId) {
        super(name, email, staffId);
    }

    public Lecturer(String name, String email, long staffId, Timetable timetable) {
        super(name, email, staffId, timetable);
    }
}
