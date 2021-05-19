package io.github.joxebus.demoapp.service;

import java.util.Optional;

import javax.xml.bind.ValidationException;

import org.springframework.security.core.userdetails.UserDetailsService;

import io.github.joxebus.demoapp.model.User;

public interface UserService extends UserDetailsService {

    User create(User requestUser, String roleName) throws ValidationException;
    User update(User requestUser);
    void delete(Long id);
    Optional<User> findByUsername(String username);
}
