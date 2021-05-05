package io.github.joxebus.demoapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "Hello world from Spring Boot";
    }
}
