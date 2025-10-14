package com.example.tutorial_time.domain.student;

import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Integer> {
    Student findByStudentID(int studentId);

}
