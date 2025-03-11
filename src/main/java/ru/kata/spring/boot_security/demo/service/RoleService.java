package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.Optional;
import java.util.Set;

public interface RoleService {
    Set<Role> getRoles();

    Optional<Role> findByName(String roleUser);

    void save(Role roles);
}
