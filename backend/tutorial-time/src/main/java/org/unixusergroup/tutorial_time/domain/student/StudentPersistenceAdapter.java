/**
 * @author Ethan
 */
package org.unixusergroup.tutorial_time.domain.student;

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
<<<<<<< HEAD
<<<<<<< HEAD
        studentJPARepository.save(student);
=======

>>>>>>> 701837a (dded JPA repositories that'll connect to the database, with adapters to bridge between the backend and the db.)
=======
        studentJPARepository.save(student);
>>>>>>> 5d3e196 (Added a test to ensure database connectivity and modified the entities to ensure that they are represented correctly)
    }

    @Override
    public Student getStudentById(long studentId) {
<<<<<<< HEAD
<<<<<<< HEAD
        return studentJPARepository.findById(studentId).get();
=======
        return null;
>>>>>>> 701837a (dded JPA repositories that'll connect to the database, with adapters to bridge between the backend and the db.)
=======
        return studentJPARepository.findById(studentId).get();
>>>>>>> 5d3e196 (Added a test to ensure database connectivity and modified the entities to ensure that they are represented correctly)
    }
}
