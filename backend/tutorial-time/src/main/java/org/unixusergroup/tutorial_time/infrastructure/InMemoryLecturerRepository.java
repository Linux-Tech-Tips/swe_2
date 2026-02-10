/** 
 * @author Martin, Cedar
 */
package org.unixusergroup.tutorial_time.infrastructure;

import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;

public class InMemoryLecturerRepository extends InMemoryRepository<Long, Lecturer> implements LecturerRepository {

    public InMemoryLecturerRepository() {
        super();
    }

    public void saveLecturer(Lecturer lecturer) {
        this.insert(Long.valueOf(lecturer.getUniversityId()), lecturer);
    }

    public Lecturer getLecturerById(long staffId) {
        return this.retrieve(Long.valueOf(staffId));
    }

    public void deleteLecturer(Lecturer lecturer) {
        this.remove(lecturer.getUniversityId());
    }
}
