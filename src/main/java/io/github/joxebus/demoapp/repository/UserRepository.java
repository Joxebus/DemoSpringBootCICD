package io.github.joxebus.demoapp.repository;

import java.util.List;
import java.util.Optional;

import io.github.joxebus.demoapp.model.Role;
import io.github.joxebus.demoapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByUsername(String username);
    List<User> findAllByRole(Role role);
}
