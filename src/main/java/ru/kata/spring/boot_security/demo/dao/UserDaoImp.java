package ru.kata.spring.boot_security.demo.dao;


import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UsersDao {

   @PersistenceContext
   private EntityManager entityManager;

   @Override
   public void save(User user) {
      entityManager.persist(user);
   }

   @Override
   public List<User> index() {
      TypedQuery<User> users = entityManager.createQuery("from User", User.class);
      return users.getResultList();
   }

   @Override
   public void update(Long id, User user) {
      entityManager.merge(user);
   }

   @Override
   public void delete(Long id) {
      User user = entityManager.find(User.class, id);
      if (user != null) {
         entityManager.remove(user);
      }
   }

   @Override
   public User show(Long id) {
      return entityManager.find(User.class, id);
   }

   @Override
   public User findByUsername(String username) {
      TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
      query.setParameter("username", username);
      return query.getResultStream().findFirst().orElse(null);
   }
}
