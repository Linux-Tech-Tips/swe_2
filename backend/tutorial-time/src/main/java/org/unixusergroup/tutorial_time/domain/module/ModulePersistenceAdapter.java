/**
 * @author Ethan, Martin, Cedar
 */
package org.unixusergroup.tutorial_time.domain.module;

import java.util.List;
import java.util.ArrayList;

/** The module repository as a class that'll interface with the persistence mariadb database.
 *  Has an internal jpa repository within it that acts as the connection point between the class and the db.
 */
public class ModulePersistenceAdapter implements ModuleRepository {
    ModuleJPARepository moduleJPARepository;

    public ModulePersistenceAdapter(ModuleJPARepository moduleJPARepository) {
        this.moduleJPARepository = moduleJPARepository;
    }

    @Override
    public void saveModule(Module module) {
        moduleJPARepository.save(module);
    }

    @Override
    public Module getModuleByCode(String moduleCode) {
        return moduleJPARepository.findById(moduleCode).orElse(null);
    }

    @Override
    public List<String> getModuleCodesByLecturer(long lecturerId) {
        List<Module> modules = moduleJPARepository.findAll();
        List<String> moduleCodes = new ArrayList<>();

        for(Module m : modules) {
            if(m.getLecturer() == lecturerId) {
                moduleCodes.add(m.getModuleCode());
            }
        }
        return moduleCodes;
    }

    @Override
    public void deleteModule(Module module) {
        moduleJPARepository.delete(module);
    }
}
