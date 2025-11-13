/** 
 * @author Martin
 */
package org.unixusergroup.tutorial_time.infrastructure;

import org.unixusergroup.tutorial_time.domain.student.StudentRepository;
import org.unixusergroup.tutorial_time.domain.lecturer.LecturerRepository;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfiguration {

    /** The backend domain name of the project, for CORS reasons */
    public static final String FRONTEND_DOMAIN_NAME = "http://localhost:5173/"; /* Change this to 'http://localhost:5173/' for testing */

    @Bean
    public StudentRepository studentRepository() {
        return new InMemoryStudentRepository();
    }

    @Bean
    public LecturerRepository lecturerRepository() {
        return new InMemoryLecturerRepository();
    }

    @Bean
    public ModuleRepository moduleRepository() {
        return new InMemoryModuleRepository();
    }

}
