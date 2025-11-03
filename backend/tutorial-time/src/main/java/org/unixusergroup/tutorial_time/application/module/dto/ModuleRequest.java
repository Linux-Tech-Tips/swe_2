package org.unixusergroup.tutorial_time.application.module.dto;

import org.unixusergroup.tutorial_time.domain.lecturer.Lecturer;
import org.unixusergroup.tutorial_time.domain.module.ModuleRepository;
import org.unixusergroup.tutorial_time.domain.student.Student;

import java.util.List;

/**
 * This is a class to represent the module request data from the frontend.
 *
 * Needs to have data about the code, lecturer, students, ta's
 *
 * @param moduleRepository
 */
public record ModuleRequest(String moduleCode, List<Student> students, List<Student> taStudents, Lecturer lecturer, ModuleRepository moduleRepository) {
}
