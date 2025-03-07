package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UsersDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;


import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class UsersServiceImp implements UsersService {
   private final UsersDao usersDao;
   private final RoleServiceImp roleServiceImp;
   private final PasswordEncoder passwordEncoder;

   @Autowired
   public UsersServiceImp(UsersDao usersDao, RoleServiceImp roleServiceImp, PasswordEncoder passwordEncoder) {
      this.usersDao = usersDao;
      this.roleServiceImp = roleServiceImp;
      this.passwordEncoder = passwordEncoder;
   }

   @Override
   @Transactional
   public boolean save(User user) {
      User savedUser = usersDao.findByUsername(user.getUsername());
      List<Role> roles= roleServiceImp.findAll();

      if (savedUser != null) {
         return false;
      }

      if (roles.isEmpty()) {
         Role roles1 = new Role("ROLE_ADMIN");
         Role roles2 = new Role("ROLE_USER");
         user.setRoles(new HashSet<>(Set.of(roles1, roles2)));
         roleServiceImp.save(roles1);
         roleServiceImp.save(roles2);
      } else if (!(user.getUser() && user.getAdmin())) {
         user.setRoles(new HashSet<>(Set.of(roles.get(1))));
      } else {
         checkSetRoles(user, roles);
      }
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      usersDao.save(user);
      return true;
   }

   public void checkSetRoles(User user, List<Role> roles) {
      if (user.getRoles() == null) {
         user.setRoles(new HashSet<>());
      } else {
         user.getRoles().clear();
      }

      if (user.getAdmin()){
         user.getRoles().add(roles.get(0));
      }
      if (user.getUser()) {
         user.getRoles().add(roles.get(1));
      }
   }

   @Override
   @Transactional(readOnly = true)
   public List<User> index() {
      return usersDao.index();
   }

   @Override
   @Transactional(readOnly = true)
   public User show(Long id) {
      return usersDao.show(id);
   }

   @Override
   @Transactional
   public void update(Long id, User updatedUser) {
      updatedUser.setId(id); // Устанавливаем ID
      usersDao.update(id, updatedUser);
   }

   @Override
   @Transactional
   public void delete(Long id) {
      usersDao.delete(id);
   }

   @Override
   @Transactional(readOnly = true)
   public User loadUserByUsername(String username) throws UsernameNotFoundException {
      User user = usersDao.findByUsername(username);
      System.err.println("Ползователь с именем " + user.getUsername() + " и с паролем " + user.getPassword() + " найден");
      if (user == null) {
         System.err.println("Пользователь не найден");
         throw new UsernameNotFoundException("Пользователь не найден: " + username);
      }
      return user;
   }
}
