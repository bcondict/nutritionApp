package edu.ucompensar.codigo.model.interfaces;

import edu.ucompensar.codigo.entity.User;

public interface ISessionManager {
    void login(User user);
    void logout();
    boolean isLoggedIn();
    User getCurrentUser();
}
