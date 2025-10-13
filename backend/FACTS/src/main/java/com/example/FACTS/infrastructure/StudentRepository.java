package com.example.FACTS.infrastructure;

import java.util.List;

import com.example.FACTS.domain.student.Student;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Long> {
    Student findById(int studentId);
    List<Student> findAllByModuleCode(String courseCode);

}
