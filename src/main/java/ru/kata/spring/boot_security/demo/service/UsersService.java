package ru.kata.spring.boot_security.demo.service;



import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;

public interface UsersService extends UserDetailsService {

    List<User> index();

    User show(Long id);

    boolean save(User user);

    void update(Long id, User user);

    void delete(Long id);

    User loadUserByUsername(String username);
}
