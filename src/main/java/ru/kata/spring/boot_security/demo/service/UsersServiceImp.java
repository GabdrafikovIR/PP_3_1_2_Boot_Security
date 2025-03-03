package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UsersDao;
import ru.kata.spring.boot_security.demo.model.User;


import java.util.List;


@Service
public class UsersServiceImp implements UsersService {
   private final UsersDao usersDao;

   @Autowired
   public UsersServiceImp(UsersDao usersDao) {
      this.usersDao = usersDao;
   }

   @Override
   @Transactional
   public void save(User user) {
      usersDao.save(user);
   }

   @Override
   @Transactional(readOnly = true)
   public List<User> index() {
      return usersDao.index();
   }

   @Override
   @Transactional(readOnly = true)
   public User show(int id) {
      return usersDao.show(id);
   }

   @Override
   @Transactional
   public void update(int id, User updatedUser) {
      usersDao.update(id, updatedUser);
   }

   @Override
   @Transactional
   public void delete(int id) {
      usersDao.delete(id);
   }
}
