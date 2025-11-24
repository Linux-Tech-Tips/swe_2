package org.unixusergroup.tutorial_time.domain.module;

import org.springframework.data.jpa.repository.JpaRepository;
/**
 * JPA repository interface for managing {@link Module} entities.
 * <p>
 * This interface provides CRUD operations and query capabilities for
 * module persistence through Spring Data JPA. By extending
 * {@link JpaRepository}, it inherits a variety of standard operations such as:
 * <ul>
 *     <li>{@code save()}</li>
 *     <li>{@code findById()}</li>
 *     <li>{@code findAll()}</li>
 *     <li>{@code deleteById()}</li>
 * </ul>
 * <p>
 * The module code is used as the primary key.
 *
 * @author Ethan
 */
public interface ModuleJPARepository extends JpaRepository<Module, String> {
}
