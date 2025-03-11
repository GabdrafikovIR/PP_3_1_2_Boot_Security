package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;
import java.util.Optional;


public interface UserService {

    List<User> findAll();

    User findById(Long Id);

    Optional<User> findByUsername(String username);

    void saveUser(User user);

    void updateUser(User user);

    void deleteById(Long id);
}
