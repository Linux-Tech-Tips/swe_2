/**
 * @author Ethan, Martin
 */
package org.unixusergroup.tutorial_time.domain;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;
import org.unixusergroup.tutorial_time.domain.timetable.Availability;

@MappedSuperclass
/** A class representing the basis for a student and lecturer */
public abstract class User {
    
    /** Enumeration defining which type a concrete user is */
    public enum UserType {STUDENT, LECTURER}

    /** The name of the user */
    protected String name;
    /** The university email of the user */
    protected String universityEmail;
    @Id
    /** The university (student or staff) ID of the user */
    protected long universityId;
    /** The user's personal timetable */
    protected Timetable timetable;

    /** Creates a user with the given identity and an empty timetable */
    public User(String name, String universityEmail, long universityId) {
        this(name, universityEmail, universityId, new Timetable());
    }
    /** Creates a user with the given identity and the given timetable */
    public User(String name, String universityEmail, long universityId, Timetable timetable) {
        this.name = name;
        this.universityEmail = universityEmail;
        this.universityId = universityId;
        this.timetable = timetable;
    }

    @Override
    public String toString() {
        return String.format("%s[name='%s', email='%s', id='%d']", this.getClass(), this.name, this.universityEmail, this.universityId);
    }

    public String getName() {
        return name;
    }

    public String getUniversityEmail() {
        return universityEmail;
    }

    public long getUniversityId() {
        return universityId;
    }

    public Timetable getTimetable() {
        return timetable;
    }

    public void setAvailable(int day, int time) {
        timetable.setAvailability(day, time, Availability.AVAILABLE);
    }

    public void setUnavailable(int day, int time) {
        timetable.setAvailability(day, time, Availability.UNAVAILABLE);
    }
}
