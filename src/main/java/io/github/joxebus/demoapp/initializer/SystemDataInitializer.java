package io.github.joxebus.demoapp.initializer;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import io.github.joxebus.demoapp.model.Role;
import io.github.joxebus.demoapp.model.User;
import io.github.joxebus.demoapp.repository.RoleRepository;
import io.github.joxebus.demoapp.repository.UserRepository;

@Component
public class SystemDataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public SystemDataInitializer(UserRepository userRepository,
                                 RoleRepository roleRepository,
                                 BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        if(userRepository.count() == 0) {
            // Is the first time the application has been executed
            // 1 - Create ROLES
            Role adminRole = new Role();
            adminRole.setName(Role.ADMIN);

            Role userRole = new Role();
            userRole.setName(Role.USER);

            roleRepository.save(adminRole);
            roleRepository.save(userRole);

            // 2 - Create users

            User adminUser = new User();
            adminUser.setRole(adminRole);
            adminUser.setEnabled(true);
            adminUser.setUsername("admin@email.com");
            adminUser.setPassword(bCryptPasswordEncoder.encode("admin123"));

            User userUser = new User();
            userUser.setRole(userRole);
            userUser.setEnabled(true);
            userUser.setUsername("user@email.com");
            userUser.setPassword(bCryptPasswordEncoder.encode("user123"));

            userRepository.save(adminUser);
            userRepository.save(userUser);

        }
    }
}
