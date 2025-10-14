package com.example.tutorial_time.domain.lecturer;

import com.example.tutorial_time.domain.Courses.Course;
import org.springframework.data.repository.CrudRepository;

public interface LecturerRepository extends CrudRepository<Lecturer, Integer> {
    Lecturer findByStaffId(int staffId);
    Lecturer findByCourse(Course course);
}
