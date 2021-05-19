package io.github.joxebus.demoapp.controller;

import javax.xml.bind.ValidationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.joxebus.demoapp.model.Role;
import io.github.joxebus.demoapp.model.User;
import io.github.joxebus.demoapp.security.JwtTokenUtil;
import io.github.joxebus.demoapp.service.UserService;

@RestController
@RequestMapping(value = "api/")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping(value = "login",
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<User> login(@RequestBody User requestUser){
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(requestUser.getUsername(), requestUser.getPassword()));

            User user = (User) authenticate.getPrincipal();

            // TODO create some logic or parser to return only some of the fields instead of the whole entity
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, jwtTokenUtil.generateAccessToken(user))
                    .body(user);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    @PostMapping(value = "register",
            consumes = "application/json",
            produces = "application/json")
    public User register(@RequestBody User requestUser) throws ValidationException {
        return userService.create(requestUser, Role.USER);
    }

}
