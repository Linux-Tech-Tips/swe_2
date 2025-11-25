package org.unixusergroup.tutorial_time.domain.student;

import org.springframework.data.jpa.repository.JpaRepository;
/**
 * JPA repository interface for managing {@link Student} entities.
 * <p>
 * This repository provides standard CRUD operations and query capabilities
 * for student persistence through Spring Data JPA. By extending
 * {@link JpaRepository}, it inherits commonly used operations such as:
 * <ul>
 *     <li>{@code save()}</li>
 *     <li>{@code findById()}</li>
 *     <li>{@code findAll()}</li>
 *     <li>{@code deleteById()}</li>
 * </ul>
 * <p>
 * Students are identified by their unique student ID (a {@code long} value).
 *
 * @author Ethan
 */
public interface StudentJPARepository extends JpaRepository<Student, Long> {
}
