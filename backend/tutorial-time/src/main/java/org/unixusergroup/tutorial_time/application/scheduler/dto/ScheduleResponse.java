/**
 * dto for the response of a schedule request
 * @author Cedar
 */

package org.unixusergroup.tutorial_time.application.scheduler.dto;

import java.util.ArrayList;

public record ScheduleResponse(String times) {
    public String getTimes() {
        return times;
    }
}
