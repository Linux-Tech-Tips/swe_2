/**
 * @author Ethan
 */
package org.unixusergroup.tutorial_time.domain.student;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentJPARepository extends JpaRepository<Student, Long> {
}
