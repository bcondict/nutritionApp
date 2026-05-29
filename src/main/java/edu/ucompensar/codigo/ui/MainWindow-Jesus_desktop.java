package edu.ucompensar.codigo.ui;
import javax.swing.*;

import edu.ucompensar.codigo.entity.User;
import edu.ucompensar.codigo.service.SessionManager;

public class MainWindow extends JFrame {

    public MainWindow() {
        User currentUser = SessionManager.getCurrentUser();

        setTitle("Nutrition app - " + currentUser.getName());

        // Window settings
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initialzeComponents();
    }

    private void initialzeComponents() {
        JLabel title = new JLabel("Nutrition APP");
        add(title);

        if () {

        }

    }

    private void 
}