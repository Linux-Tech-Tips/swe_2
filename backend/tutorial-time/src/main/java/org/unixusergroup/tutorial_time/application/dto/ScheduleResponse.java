/**
 * dto for the response of a schedule request
 * @author Cedar
 */

package org.unixusergroup.tutorial_time.application.dto;

import java.util.ArrayList;

public record ScheduleResponse(ArrayList<Integer> times) {}
