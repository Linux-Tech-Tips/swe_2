/** 
 * @author Martin
 */
package org.unixusergroup.tutorial_time.infrastructure;

import java.util.List;
import java.util.stream.Collectors;

import org.unixusergroup.tutorial_time.domain.student.Student;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;

public class InMemoryStudentRepository extends InMemoryRepository<Long, Student> implements StudentRepository {

    public InMemoryStudentRepository() {
        super();
    }

    public void saveStudent(Student student) {
        this.insert(Long.valueOf(student.getUniversityId()), student);
    }

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
