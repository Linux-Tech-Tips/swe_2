/**
 * dto for requesting tutorials to be scheduled
 * @author Cedar
 */

package org.unixusergroup.tutorial_time.application.scheduler.dto;

public record ScheduleRequest(String moduleCode, int studentLimit, int minTAs, int[] blockedTimes) {}
