package io.github.joxebus.demoapp.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "Unsecured index";
    }

    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    @GetMapping(value = "/admin")
    public String admin() {
        return "This endpoint is only available for ADMIN users";
    }

    @PreAuthorize("hasAuthority('USER_ROLE')")
    @GetMapping(value = "/user")
    public String user() {
        return "This endpoint is only available for USER users";
    }

    @PreAuthorize("hasAnyAuthority('USER_ROLE', 'ADMIN_ROLE')")
    @GetMapping(value = "/adminOrUser")
    public String adminOrUser() {
        return "This endpoint is only available for ADMIN and USER users";
    }
}
