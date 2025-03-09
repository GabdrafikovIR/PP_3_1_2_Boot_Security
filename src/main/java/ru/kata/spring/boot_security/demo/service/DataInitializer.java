package ru.kata.spring.boot_security.demo.service;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Collections;
@Component
public class DataInitializer implements ApplicationRunner {

    private final UsersService usersService;
    private final RoleServiceImp roleService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsersService usersService, RoleServiceImp roleServiceImp, PasswordEncoder passwordEncoder) {
        this.usersService = usersService;
        this.roleService = roleServiceImp;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (roleService.findAll().isEmpty()) {
            Role adminRole = new Role("ROLE_ADMIN");
            Role userRole = new Role("ROLE_USER");
            roleService.save(adminRole);
            roleService.save(userRole);
        }

        if (usersService.index().isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin");
            admin.setRoles(Collections.singleton(roleService.findByName("ROLE_ADMIN"))); // Преобразуем List в Set
            usersService.save(admin);

            User user = new User();
            user.setUsername("user");
            user.setPassword("user");
            user.setRoles(Collections.singleton(roleService.findByName("ROLE_USER"))); // Назначаем роль пользователя
            usersService.save(user);
        }
    }
}