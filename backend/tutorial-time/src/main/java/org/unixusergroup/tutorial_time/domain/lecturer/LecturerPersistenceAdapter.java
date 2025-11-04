/**
 * @author Ethan
 */
package org.unixusergroup.tutorial_time.domain.lecturer;

/** The lecturer repository as a class that'll interface with the persistence mariadb database.
 *  Has an internal jpa repository within it that acts as the connection point between the class and the db.
 */
public class LecturerPersistenceAdapter implements LecturerRepository {
    LecturerJPARepository lecturerJPARepository;

    public LecturerPersistenceAdapter(LecturerJPARepository lecturerJPARepository) {
        this.lecturerJPARepository = lecturerJPARepository;
    }

    @Override
    public void saveLecturer(Lecturer lecturer) {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
        lecturerJPARepository.save(lecturer);
=======

>>>>>>> 701837a (dded JPA repositories that'll connect to the database, with adapters to bridge between the backend and the db.)
=======
        lecturerJPARepository.save(lecturer);
>>>>>>> 5d3e196 (Added a test to ensure database connectivity and modified the entities to ensure that they are represented correctly)
=======

>>>>>>> fad0fd0 (dded JPA repositories that'll connect to the database, with adapters to bridge between the backend and the db.)
    }

    @Override
    public Lecturer getLecturerById(long staffId) {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
        return lecturerJPARepository.findById(staffId).get();
=======
        return null;
>>>>>>> 701837a (dded JPA repositories that'll connect to the database, with adapters to bridge between the backend and the db.)
=======
        return lecturerJPARepository.findById(staffId).get();
>>>>>>> 5d3e196 (Added a test to ensure database connectivity and modified the entities to ensure that they are represented correctly)
=======
        return null;
>>>>>>> fad0fd0 (dded JPA repositories that'll connect to the database, with adapters to bridge between the backend and the db.)
    }
}
