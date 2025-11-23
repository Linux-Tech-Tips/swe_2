/** 
 * @author Ethan, Martin, Lilian
 */
package org.unixusergroup.tutorial_time.domain.student;

import java.util.List;

/** Interface allowing Student instances to be saved and loaded to and from a database */
public interface StudentRepository {
    /** Saves a Student instance into the database */
    public void saveStudent(Student student);
    /** Loads a Student instance from the database */
    public Student getStudentById(long studentId);
    /** Loads list of Student instances from database with IDs */
    public List<Student> findStudentsByIds(List<Long> studentIds);
}
