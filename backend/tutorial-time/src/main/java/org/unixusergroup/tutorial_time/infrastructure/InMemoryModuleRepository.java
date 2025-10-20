/** 
 * @author Martin
 */
package org.unixusergroup.tutorial_time.infrastructure;

import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;

public class InMemoryModuleRepository extends InMemoryRepository<String, Module> implements ModuleRepository {

    public InMemoryModuleRepository() {
        super();
    }

    public void saveModule(Module module) {
        this.insert(module.getModuleCode(), module);
    }

    public Module getModuleByCode(String moduleCode) {
        return this.retrieve(moduleCode);
    }
}
