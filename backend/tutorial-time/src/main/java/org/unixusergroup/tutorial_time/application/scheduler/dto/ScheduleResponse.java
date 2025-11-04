/**
 * dto for the response of a schedule request
 * @author Cedar
 */

package org.unixusergroup.tutorial_time.application.scheduler.dto;

import java.util.ArrayList;

public record ScheduleResponse(ArrayList<Integer> times) {
    public ArrayList<Integer> getTimes() {
        return times;
    }
}
