package com.example.FACTS.infrastructure;

import java.util.List;

import com.example.FACTS.domain.Courses.Course;
import com.example.FACTS.domain.lecturer.Lecturer;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<CourseRepository, Long> {
    List<Course> findAllByLecturer(Lecturer lecturer);
    Course findByCode(String courseCode);

}
