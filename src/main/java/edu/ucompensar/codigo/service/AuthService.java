package edu.ucompensar.codigo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;

import edu.ucompensar.codigo.DAO.UserDAO;
import edu.ucompensar.codigo.entity.User;

public class AuthService {
    private UserDAO dao;

    public AuthService() {
        this.dao = new UserDAO();
    }

    public boolean login(String email, String password) {
        if (email == null || email.isBlank()) {
            return false;
        }

        if (password == null || password.isBlank()) {
            return false;
        }

        User user = dao.findByEmail(email);
        if (user == null) {
            return false;
        }

        boolean passwordCorrect = BCrypt.checkpw(
                password,
                user.getPasswordHash()
        );

        if (passwordCorrect) {
            SessionManager.login(user);
            return true;
        }

        return false;
    }

    public void logout() {
        SessionManager.logout();
    }

    public boolean register(UUID id, String name, String lastname, String email, LocalDate birthday, String password) {
        if (id == null) {
            return false;
        }
        if (name == null || name.isBlank()) {
            return false;
        }
        if (lastname == null || lastname.isBlank()) {
            return false;
        }
        if (email == null || email.isBlank()) {
            return false;
        }
        if (birthday == null) {
            return false;
        }
        if (password == null || password.isBlank()) {
            return false;
        }

        LocalDateTime dateNow = LocalDateTime.now();

        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

        User user = new User(id, name, lastname, email, passwordHash, birthday, dateNow, dateNow);

        dao.save(user);

        return true;
    }
}
