/** 
 * @author Ethan, Martin
 */
package org.unixusergroup.tutorial_time.domain.student;

/** Interface allowing Student instances to be saved and loaded to and from a database */
public interface StudentRepository {
    /** Saves a Student instance into the database */
    public void saveStudent(Student student);
    /** Loads a Student instance from the database */
    public Student getStudentById(long studentId);
}
