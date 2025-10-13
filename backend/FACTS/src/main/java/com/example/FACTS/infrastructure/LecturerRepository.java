package com.example.FACTS.infrastructure;

import com.example.FACTS.domain.lecturer.Lecturer;
import org.springframework.data.repository.CrudRepository;

public interface LecturerRepository extends CrudRepository<Lecturer, Long> {

}
