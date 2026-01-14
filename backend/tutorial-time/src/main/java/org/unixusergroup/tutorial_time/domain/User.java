/**
 * @author Ethan, Martin, Cedar
 */
package org.unixusergroup.tutorial_time.domain;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.hibernate.type.descriptor.jdbc.SqlTypedJdbcType;
import org.unixusergroup.tutorial_time.domain.timetable.Timetable;
import org.unixusergroup.tutorial_time.domain.timetable.Availability;
import java.util.regex.Pattern;

/** A class representing the basis for a student and lecturer */
@MappedSuperclass
public abstract class User {
    
    /** Enumeration defining which type a concrete user is */
    public enum UserType {STUDENT, LECTURER}

    /** The name of the user */
    protected String name;
    /** The university email of the user */
    protected String universityEmail;
    private final String universityEmailRegex = ".*@universityofgalway\\.ie";
    private final String oldUniversityEmailRegex = ".*@nuigalway\\.ie";
    /** The university (student or staff) ID of the user */
    @Id
    protected long universityId;
    /** The user's personal timetable */
    @JdbcTypeCode(SqlTypes.JSON)
    protected Timetable timetable;

    /** Empty default constructor for database storage */
    protected User() {};
    /** Creates a user with the given identity and an empty timetable */
    public User(String name, String universityEmail, long universityId) {
        this(name, universityEmail, universityId, new Timetable());
    }
    /** Creates a user with the given identity and the given timetable */
    public User(String name, String universityEmail, long universityId, Timetable timetable) throws IllegalArgumentException {
        if(name.isEmpty()) {
            throw new IllegalArgumentException("A name must be inputted");
        }
        if(!(Pattern.matches(universityEmailRegex, universityEmail.toLowerCase())) && !(Pattern.matches(oldUniversityEmailRegex, universityEmail.toLowerCase()))) {
            throw new IllegalArgumentException("You must input a university of galway email address");
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
            return ((User)obj).getUniversityId() ==  this.getUniversityId();
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
