package org.unixusergroup.tutorial_time.infrastructure;

import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
/**
 * In-memory implementation of the {@link LecturerRepository}.
 * <p>
 * This repository is primarily used for testing or development scenarios
 * where a persistent data store is not required. It stores lecturers
 * in memory using the functionality provided by {@link InMemoryRepository}.
 *
 * @author Martin
 */
public class InMemoryLecturerRepository extends InMemoryRepository<Long, Lecturer> implements LecturerRepository {
    /**
     * Creates a new {@code InMemoryLecturerRepository} with an empty
     * in-memory data store.
     */
    public InMemoryLecturerRepository() {
        super();
    }
/**
     * Saves a lecturer in the in-memory store using their university ID
     * as the key.
     *
     * @param lecturer the lecturer to store
     */
    public void saveLecturer(Lecturer lecturer) {
        this.insert(Long.valueOf(lecturer.getUniversityId()), lecturer);
    }
    /**
     * Retrieves a lecturer by their staff ID.
     *
     * @param staffId the university ID of the lecturer
     * @return the lecturer associated with the given ID, or {@code null} if none exists
     */
    public Lecturer getLecturerById(long staffId) {
        return this.retrieve(Long.valueOf(staffId));
    }
}
