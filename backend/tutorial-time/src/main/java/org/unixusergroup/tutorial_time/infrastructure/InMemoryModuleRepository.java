/** 
 * @author Martin, Cedar
 */
package org.unixusergroup.tutorial_time.infrastructure;

import org.unixusergroup.tutorial_time.domain.module.Module;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

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

    public List<String> getModuleCodesByLecturer(long lecturerId) {
        Collection<Module> modules = this.retrieveAll();
        List<String> moduleIds = new ArrayList<>();
        for(Module m : modules) {
            if(m.getLecturer() == lecturerId) {
                moduleIds.add(m.getModuleCode());
            }
        }
        return moduleIds;
    }

    public void deleteModule(Module module) {
        this.remove(module.getModuleCode());
    }
}
