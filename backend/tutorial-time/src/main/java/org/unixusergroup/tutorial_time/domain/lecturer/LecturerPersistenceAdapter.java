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
        lecturerJPARepository.save(lecturer);
    }

    @Override
    public Lecturer getLecturerById(long staffId) {
        return lecturerJPARepository.findById(staffId).get();
    }
}
