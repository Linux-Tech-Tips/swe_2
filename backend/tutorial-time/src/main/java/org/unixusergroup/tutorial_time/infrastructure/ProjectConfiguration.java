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
