/*
 * @author Lilian
 */

package org.unixusergroup.tutorial_time.application.module.dto;

import java.util.List;

public record ModuleResponse(String moduleCode,List<Long> enrolledStudentIds) {
    
}
    
