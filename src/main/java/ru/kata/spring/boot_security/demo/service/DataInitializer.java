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
        // Проверяем, есть ли уже роли в базе данных
        if (roleService.findAll().isEmpty()) {
            // Создаем роли, если их нет
            Role adminRole = new Role("ROLE_ADMIN");
            Role userRole = new Role("ROLE_USER");
            roleService.save(adminRole);
            roleService.save(userRole);
        }

        // Проверяем, есть ли уже пользователи в базе данных
        if (usersService.index().isEmpty()) {
            // Создаем начального пользователя (администратора)
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin")); // Шифруем пароль
            admin.setRoles(Collections.singleton(roleService.findByName("ROLE_ADMIN"))); // Преобразуем List в Set
            usersService.save(admin);

            // Создаем обычного пользователя (опционально)
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user")); // Шифруем пароль
            user.setRoles(Collections.singleton(roleService.findByName("ROLE_USER"))); // Назначаем роль пользователя
            usersService.save(user);
        }
    }
}