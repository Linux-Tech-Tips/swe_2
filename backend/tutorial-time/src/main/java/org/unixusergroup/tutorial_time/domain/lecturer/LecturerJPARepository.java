
package org.unixusergroup.tutorial_time.domain.lecturer;

import org.springframework.data.jpa.repository.JpaRepository;
/**
 * JPA repository interface for managing {@link Lecturer} entities.
 * <p>
 * This interface provides CRUD operations and query support through
 * Spring Data JPA for lecturer persistence. By extending
 * {@link JpaRepository}, it inherits methods such as:
 * <ul>
 *     <li>{@code save()}</li>
 *     <li>{@code findById()}</li>
 *     <li>{@code findAll()}</li>
 *     <li>{@code deleteById()}</li>
 * </ul>
 *
 * @author Ethan
 */
public interface LecturerJPARepository extends JpaRepository<Lecturer, Long> {
}
