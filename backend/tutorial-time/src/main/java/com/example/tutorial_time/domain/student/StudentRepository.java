package com.example.tutorial_time.domain.student;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Integer> {
    Student findByStudentId(int studentId);
    List<Student> findAllByModuleCode(String courseCode);

}
