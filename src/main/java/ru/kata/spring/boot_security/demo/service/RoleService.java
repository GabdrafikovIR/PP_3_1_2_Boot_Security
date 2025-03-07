package ru.kata.spring.boot_security.demo.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

public interface RoleService {
    Role findByName(String roleUser);

    List<Role> findAll();

    void save(Role role);
}
