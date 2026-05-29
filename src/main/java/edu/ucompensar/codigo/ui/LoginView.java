package edu.ucompensar.codigo.ui;

import javax.swing.*;

import edu.ucompensar.codigo.entity.User;
import edu.ucompensar.codigo.service.AuthService;
import edu.ucompensar.codigo.service.SessionManager;

import java.awt.*;

public class LoginView extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginView() {
        setTitle("Nutrition app - Login");

        // Window settings
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout());

        // panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Nutrition app", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(Box.createVerticalStrut(20));

        // Components
        JPanel formPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // email
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("email: "), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        formPanel.add(emailField, gbc);

        // password
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password: "), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        formPanel.add(passwordField, gbc);

        formPanel.add(Box.createVerticalStrut(20));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        registerButton = new JButton("Register");
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        buttonPanel.add(loginButton, gbc);
        buttonPanel.add(registerButton, gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);

        pack();

        initEvents();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public char[] getPassword() {
        return passwordField.getPassword();
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    private void initEvents() {
        loginButton.addActionListener(e -> {
            login();
        });
        registerButton.addActionListener(e -> {
            changeRegisterView();
        });
    }

    private void login() {
        String username = emailField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);

        AuthService authService = new AuthService();

        boolean success = authService.login(username, password);
        if (success) {
            JOptionPane.showMessageDialog(this, "Login exitoso");
            User user = SessionManager.getCurrentUser();

            System.out.println(user.getName());
            dispose();

            MainWindow mw = new MainWindow();
            mw.setVisible(true);
        }
        else {
            JOptionPane.showMessageDialog(this, "Credenciales invalidas");
        }
    }

    private void changeRegisterView() {
        dispose();
        RegisterView rv = new RegisterView();
        rv.setVisible(true);
    }
}
