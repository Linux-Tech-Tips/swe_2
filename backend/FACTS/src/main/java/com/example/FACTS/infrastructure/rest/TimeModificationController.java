package com.example.FACTS.infrastructure.rest;
/**
 *
 */

import com.example.FACTS.domain.Tutorial.Timeslot;
import com.example.FACTS.application.TimeModificationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/timetable")
public class TimeModificationController {

    private TimeModificationService timeModificationService;

    TimeModificationController(TimeModificationService timeModificationService) {
        this.timeModificationService = timeModificationService;
    }

    /**
     *
     * @return
     */
    @PostMapping(path="/add")
    public @ResponseBody String addBusyTime(@RequestParam String day, @RequestParam int hour) {
        Timeslot time = new Timeslot(day, hour);
        return "Time entered";
    }

    /**
     *
     * @return
     */
}
