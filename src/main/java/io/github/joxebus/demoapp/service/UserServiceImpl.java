package io.github.joxebus.demoapp.service;

import static java.lang.String.format;

import java.util.Optional;

import javax.xml.bind.ValidationException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.joxebus.demoapp.model.Role;
import io.github.joxebus.demoapp.model.User;
import io.github.joxebus.demoapp.repository.RoleRepository;
import io.github.joxebus.demoapp.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public User create(User requestUser, String roleName) throws ValidationException {
        Role userRole = roleRepository.findByName(roleName);

        if(userRole == null) {
            throw new ValidationException("Invalid RoleEnum value: "+roleName);
        }

        if (userRepository.findByUsername(requestUser.getUsername()).isPresent()) {
            throw new ValidationException("Username exists!");
        }

        if (!requestUser.getPassword().equals(requestUser.getPasswordConfirm())) {
            throw new ValidationException("Passwords don't match!");
        }

        requestUser.setRole(userRole);
        requestUser.setPassword(bCryptPasswordEncoder.encode(requestUser.getPassword()));

        requestUser = userRepository.save(requestUser);

        return requestUser;
    }

    @Transactional
    public User update(User requestUser) {
        User user = userRepository.findById(requestUser.getId()).orElseGet(null);
        user = userRepository.save(user);
        return user;
    }

    @Transactional
    public void delete(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            userRepository.delete(user.get());
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * This method comes from UserDetailsService interface
     * and is used by Spring Security to get and set the principal user
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(format("User with username - %s, not found", username))
                );
        return user;
    }

}
