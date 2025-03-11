package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class UserServiceImp implements UserService {
   private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;

   @Autowired
   public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder) {
      this.userRepository = userRepository;
      this.passwordEncoder = passwordEncoder;
   }


   @Override
   @Transactional(readOnly = true)
   public List<User> findAll() {
      return userRepository.findAll();
   }

   @Override
   @Transactional(readOnly = true)
   public User findById(Long id) {
      return userRepository.findById(id).orElse(null);
   }


   @Override
   @Transactional(readOnly = true)
   public Optional<User> findByUsername(String username) {
      return Optional.ofNullable(userRepository.findByUsername(username));
   }

   @Override
   @Transactional
   public void saveUser(User user) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      userRepository.save(user);
   }

   @Override
   @Transactional
   public void updateUser(User user) {
      User existingUser = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
      existingUser.setUsername(user.getUsername());
      existingUser.setName(user.getName());
      if (user.getPassword() != null && !user.getPassword().isEmpty()) {
         existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
      }
      existingUser.setRoles(user.getRoles());
      userRepository.save(existingUser);
   }

   @Override
   @Transactional
   public void deleteById(Long id) {
      if (userRepository.findById(id).isPresent()) {
         userRepository.deleteById(id);
      } else {
         userRepository.findAll();
      }
   }
}

