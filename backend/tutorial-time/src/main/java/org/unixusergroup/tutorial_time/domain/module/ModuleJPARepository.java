/**
 * @author Ethan
 */
package org.unixusergroup.tutorial_time.domain.module;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleJPARepository extends JpaRepository<Module, String> {
}
