package org.unixusergroup.tutorial_time.infrastructure;

import java.util.List;
import java.util.stream.Collectors;

import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
/**
 * In-memory implementation of the {@link StudentRepository}.
 * <p>
 * This repository stores {@link Student} entities in memory using the
 * generic storage mechanisms provided by {@link InMemoryRepository}. 
 * It is intended for development or testing environments where persistent
 * storage is unnecessary.
 *
 * @author Martin
 */
public class InMemoryStudentRepository extends InMemoryRepository<Long, Student> implements StudentRepository {

    public InMemoryStudentRepository() {
        super();
    }
    /**
     * Saves a student in the in-memory store using their university ID
     * as the key.
     *
     * @param student the student to save
     */
    public void saveStudent(Student student) {
        this.insert(Long.valueOf(student.getUniversityId()), student);
    }
    /**
     * Retrieves a student by their unique university-assigned ID.
     *
     * @param studentId the ID of the student to retrieve
     * @return the corresponding student, or {@code null} if no such student exists
     */
    public Student getStudentById(long studentId) {
        return this.retrieve(Long.valueOf(studentId));
    }

    @Override
    public List<Student> findStudentsByIds(List<Long> studentIds) {
        return this.data.values().stream()
            .filter(student -> studentIds.contains(student.getUniversityId()))
            .collect(Collectors.toList());
    }
}
