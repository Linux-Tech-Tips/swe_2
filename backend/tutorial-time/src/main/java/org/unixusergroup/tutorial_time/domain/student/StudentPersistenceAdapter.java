/**
 * @author Ethan, Lilian
 */
package org.unixusergroup.tutorial_time.domain.student;

import java.util.List;

/** The student repository as a class that'll interface with the persistence mariadb database.
 *  Has an internal jpa repository within it that acts as the connection point between the class and the db.
 */

public class StudentPersistenceAdapter implements StudentRepository {
    StudentJPARepository studentJPARepository;

    public StudentPersistenceAdapter(StudentJPARepository studentJPARepository) {
        this.studentJPARepository = studentJPARepository;
    }

    @Override
    public void saveStudent(Student student) {
        studentJPARepository.save(student);
    }

    @Override
    public Student getStudentById(long studentId) {
        return studentJPARepository.findById(studentId).orElse(null);
    }

    @Override
    public List<Student> findStudentsByIds(List<Long> studentIds) {
        return studentJPARepository.findAllById(studentIds);
    }
}
