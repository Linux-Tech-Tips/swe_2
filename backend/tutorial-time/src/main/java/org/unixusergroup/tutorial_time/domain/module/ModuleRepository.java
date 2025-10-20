/** 
 * @author Ethan, Martin
 */
package org.unixusergroup.tutorial_time.domain.module;

/** Interface allowing Module instances to be saved and loaded to and from a database */
public interface ModuleRepository {
    /** Saves a Module instance into the database */
    public void saveModule(Module module);
    /** Loads a Module instance from the database */
    public Module getModuleByCode(String moduleCode);
}
