/**
 * @author Cedar
 */
package org.unixusergroup.tutorial_time.application.exceptions;

/**
 * custom exception class to handle an illegal input of student timetables
 * is triggered when no student or TA timetables are passed into a Scheduler instance
 */
public class IllegalStudentTimetablesException extends IllegalArgumentException {
    public IllegalStudentTimetablesException(String e) {
        super(e);
    }
}
