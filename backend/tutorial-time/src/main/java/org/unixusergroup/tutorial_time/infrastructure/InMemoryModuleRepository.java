package org.unixusergroup.tutorial_time.infrastructure;

import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;
/**
 * In-memory implementation of the {@link ModuleRepository}.
 * <p>
 * This repository stores {@link Module} instances in memory using the
 * underlying functionality provided by {@link InMemoryRepository}. It is
 * mainly intended for testing or development environments where persistence
 * is not required.
 *
 * @author Martin
 */
public class InMemoryModuleRepository extends InMemoryRepository<String, Module> implements ModuleRepository {
    /**
     * Creates a new {@code InMemoryModuleRepository} with an empty
     * in-memory data storage.
     */
    public InMemoryModuleRepository() {
        super();
    }
    /**
     * Saves a module using its module code as the storage key.
     *
     * @param module the module to save
     */
    public void saveModule(Module module) {
        this.insert(module.getModuleCode(), module);
    }
    /**
     * Retrieves a module based on its module code.
     *
     * @param moduleCode the module code identifying the module
     * @return the corresponding module, or {@code null} if none is found
     */
    public Module getModuleByCode(String moduleCode) {
        return this.retrieve(moduleCode);
    }
}
