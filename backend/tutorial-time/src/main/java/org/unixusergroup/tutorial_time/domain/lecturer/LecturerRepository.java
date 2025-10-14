package org.unixusergroup.tutorial_time.domain.lecturer;

import org.springframework.data.repository.CrudRepository;

public interface LecturerRepository extends CrudRepository<Lecturer, Integer> {
    Lecturer findByStaffID(int staffId);
}
