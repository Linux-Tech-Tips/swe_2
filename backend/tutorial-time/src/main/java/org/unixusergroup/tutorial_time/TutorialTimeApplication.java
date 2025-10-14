package org.unixusergroup.tutorial_time;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@SpringBootApplication
@RequestMapping(path="/")
@Controller
public class TutorialTimeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TutorialTimeApplication.class, args);
	}

    @GetMapping(path="/hello")
    public @ResponseBody String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Welcome to facts. New %s\n", name);
    }
}
