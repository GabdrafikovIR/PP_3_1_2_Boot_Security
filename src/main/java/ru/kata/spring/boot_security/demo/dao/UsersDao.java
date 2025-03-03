package ru.kata.spring.boot_security.demo.dao;



import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UsersDao {

    void save(User user);

    List<User> index();

    void update(int id, User user);

    void delete(int id);

    User show(int id);

}
