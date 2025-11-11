/**
 * @author Ethan, Martin, Cedar
 */
package org.unixusergroup.tutorial_time.domain;

import org.unixusergroup.tutorial_time.domain.timetable.Timetable;
import org.unixusergroup.tutorial_time.domain.timetable.Availability;

/** A class representing the basis for a student and lecturer */
public abstract class User {
    
    /** Enumeration defining which type a concrete user is */
    public enum UserType {STUDENT, LECTURER}

    /** The name of the user */
    protected String name;
    /** The university email of the user */
    protected String universityEmail;
    /** The university (student or staff) ID of the user */
    protected long universityId;
    /** The user's personal timetable */
    protected Timetable timetable;

    /** Creates a user with the given identity and an empty timetable */
    public User(String name, String universityEmail, long universityId) {
        this(name, universityEmail, universityId, new Timetable());
    }
    /** Creates a user with the given identity and the given timetable */
    public User(String name, String universityEmail, long universityId, Timetable timetable) throws IllegalArgumentException {
        if(name.isEmpty()) {
            throw new IllegalArgumentException("A name must be inputted");
        }
        //TODO: edit this to look specifically for nuig emails
        if(universityEmail.isEmpty()) {
            throw new IllegalArgumentException("An email must be inputted");
        }
        if(universityId<1) {
            throw new IllegalArgumentException("The ID must be a positive integer");
        }
        this.name = name;
        this.universityEmail = universityEmail;
        this.universityId = universityId;
        this.timetable = timetable;
    }

    @Override
    public String toString() {
        return String.format("%s[name='%s', email='%s', id='%d']", this.getClass(), this.name, this.universityEmail, this.universityId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return ((User) obj).getUniversityId() == this.getUniversityId();
        } else {
            return false;
        }
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

    public void setAvailable(int day, int time) throws IllegalArgumentException {
        if(day < 0 || day > 4) {
            throw new IllegalArgumentException("Please enter a day integer between 0 and 4");
        }
        if(time < 0 || time > 8) {
            throw new IllegalArgumentException("Please enter a time integer between 0 and 8");
        }
        timetable.setAvailability(day, time, Availability.AVAILABLE);
    }

    public void setUnavailable(int day, int time) throws IllegalArgumentException {
        if(day < 0 || day > 4) {
            throw new IllegalArgumentException("Please enter a day integer between 0 and 4");
        }
        if(time < 0 || time > 8) {
            throw new IllegalArgumentException("Please enter a time integer between 0 and 8");
        }
        timetable.setAvailability(day, time, Availability.UNAVAILABLE);
    }
}
