/** 
 * @author Ethan, Martin, Lilian, Cedar
 */
package org.unixusergroup.tutorial_time.domain.student;


/** Interface allowing Student instances to be saved and loaded to and from a database */
public interface StudentRepository {
    /** Saves a Student instance into the database */
    public void saveStudent(Student student);

    /** Loads a Student instance from the database */
    public Student getStudentById(long studentId);

    /** Deletes a given Student instance from the database */
    public void deleteStudent(Student student);
}
