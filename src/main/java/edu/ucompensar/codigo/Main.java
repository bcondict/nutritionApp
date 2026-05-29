package edu.ucompensar.codigo;

import javax.swing.SwingUtilities;

import edu.ucompensar.codigo.service.SessionManager;
import edu.ucompensar.codigo.ui.LoginView;
import edu.ucompensar.codigo.ui.MainWindow;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            if (SessionManager.isLoggedIn()) {
                MainWindow window = new MainWindow();
                window.setVisible(true);
            }
            if (!SessionManager.isLoggedIn()) {
                LoginView loginView = new LoginView();
                loginView.setVisible(true);
            }
        });
    }
}