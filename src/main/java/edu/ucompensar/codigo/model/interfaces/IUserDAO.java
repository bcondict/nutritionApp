package edu.ucompensar.codigo.model.interfaces;

import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.entity.User;

public interface IUserDAO {
    void save(User user);
    User findById(UUID id);
    User findByEmail(String email);
    List<User> findAll();
    void update(User user);
    void delete(UUID id);

}
