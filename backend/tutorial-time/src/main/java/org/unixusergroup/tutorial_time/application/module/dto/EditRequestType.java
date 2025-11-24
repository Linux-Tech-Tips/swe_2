package org.unixusergroup.tutorial_time.application.module.dto;

/**
 * Represents the type of enrollment edit request that can be performed
 * on a module. These operations cover both teaching assistant (TA)
 * assignments and student enrollments.
 */
public enum EditRequestType {
    TA_ADDITION,
    TA_REMOVAL,
    STUDENT_ADDITION,
    STUDENT_REMOVAL
}
