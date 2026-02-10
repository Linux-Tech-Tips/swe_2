/** 
 * @author Ethan, Martin, Cedar
 */
package org.unixusergroup.tutorial_time.domain.module;

import java.util.List;

/** Interface allowing Module instances to be saved and loaded to and from a database */
public interface ModuleRepository {
    /** Saves a Module instance into the database */
    public void saveModule(Module module);

    /** Loads a Module instance from the database */
    public Module getModuleByCode(String moduleCode);

    /** Finds a list of all module codes in the repository associated with a specific lecturer */
    public List<String> getModuleCodesByLecturer(long lecturerId);

    /** Deletes a given module instance from the database */
    public void deleteModule(Module module);
}
