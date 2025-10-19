package org.unixusergroup.tutorial_time.infrastructure.rest;

import org.unixusergroup.tutorial_time.domain.scheduler.*;
import org.unixusergroup.tutorial_time.application.useCases.ScheduleUseCase;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/scheduler")
public class ScheduleController {
    //separate APIs in this file for constraints

    //call SchedulerUseCase for the schedule API, parameter constraints + module reference/timetables
    }