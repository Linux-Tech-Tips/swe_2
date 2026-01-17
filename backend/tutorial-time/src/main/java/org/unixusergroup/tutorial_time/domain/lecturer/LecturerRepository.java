/** 
 * @author Ethan, Martin, Cedar
 */
package org.unixusergroup.tutorial_time.domain.lecturer;

/** Interface allowing Lecturer instances to be saved and loaded to and from a database */
public interface LecturerRepository {
    /** Saves a Lecturer instance to the database */
    public void saveLecturer(Lecturer lecturer);

    /** Loads a Lecturer instance from the database */
    public Lecturer getLecturerById(long staffId);

    /** Deletes a given Lecturer instance from the database */
    public void deleteLecturer(Lecturer lecturer);
}
