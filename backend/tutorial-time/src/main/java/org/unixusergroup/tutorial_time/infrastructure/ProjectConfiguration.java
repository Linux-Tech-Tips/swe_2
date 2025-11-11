/** 
 * @author Martin
 */
package org.unixusergroup.tutorial_time.infrastructure;

import org.unixusergroup.tutorial_time.domain.lecturer.LecturerJPARepository;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerPersistenceAdapter;
import org.unixusergroup.tutorial_time.domain.module.ModuleJPARepository;
import org.unixusergroup.tutorial_time.domain.module.ModulePersistenceAdapter;
import org.unixusergroup.tutorial_time.domain.student.StudentJPARepository;
import org.unixusergroup.tutorial_time.domain.student.StudentPersistenceAdapter;
import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfiguration {

    /** The backend domain name of the project, for CORS reasons */
    public static final String FRONTEND_DOMAIN_NAME = "https://frontend.unixusergroup.org/"; /* Change this to 'http://localhost:5173/' for testing */

    @Bean
    public StudentRepository studentRepository(StudentJPARepository studentJPARepository) {
        return new StudentPersistenceAdapter(studentJPARepository);
    }

    @Bean
    public LecturerRepository lecturerRepository(LecturerJPARepository lecturerJPARepository) {
        return new LecturerPersistenceAdapter(lecturerJPARepository);
    }

    @Bean
    public ModuleRepository moduleRepository(ModuleJPARepository moduleJPARepository) {
        return new ModulePersistenceAdapter(moduleJPARepository);
    }

}
