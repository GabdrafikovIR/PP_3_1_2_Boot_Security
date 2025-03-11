package ru.kata.spring.boot_security.demo.service;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.Set;


@Component
public class DataInitializer implements ApplicationRunner {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }



    @Override
    public void run(ApplicationArguments args) {
        if (roleService.getRoles().isEmpty()) {
            Role adminRole = new Role();
            adminRole.setRoles("ROLE_ADMIN");
            Role userRole = new Role();
            userRole.setRoles("ROLE_USER");
            roleService.save(adminRole);
            roleService.save(userRole);
        }

        if (userService.findAll().isEmpty()) {
            User admin = new User();
            admin.setName("admin");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(Set.of(
                    roleService.findByName("ROLE_ADMIN")
                            .orElseThrow(() -> new RuntimeException("Роль ROLE_ADMIN не найдена")),
                    roleService.findByName("ROLE_USER")
                            .orElseThrow(() -> new RuntimeException("Роль ROLE_USER не найдена"))
            ));
            userService.saveUser(admin);

            User user = new User();
            user.setName("user");
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user"));
            user.setRoles(Set.of(
                    roleService.findByName("ROLE_USER")
                            .orElseThrow(() -> new RuntimeException(("Роль ROLE_USER не найдена")))
            ));
            userService.saveUser(user);
        }
    }
}