package edu.ucompensar.codigo.ui;

import javax.swing.JFrame;

import edu.ucompensar.codigo.service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class RegisterView extends JFrame {

    private JTextField nameField;
    private JTextField lastnameField;
    private JTextField emailField;
    private JFormattedTextField dateString;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    private JButton registerButton;
    private JButton loginButton;

    public RegisterView() {

        setTitle("Nutrition App - Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // MAIN PANEL
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // TITLE
        JLabel title = new JLabel("Create Account", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        mainPanel.add(title, BorderLayout.NORTH);

        // FORM PANEL
        JPanel formPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);

        // Lastname
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Apellido:"), gbc);

        gbc.gridx = 1;
        lastnameField = new JTextField(20);
        formPanel.add(lastnameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        formPanel.add(emailField, gbc);

        // Birthday
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Fecha de nacimiento:"), gbc);

        gbc.gridx = 1;
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        dateString = new JFormattedTextField(format);
        dateString.setColumns(20);
        formPanel.add(dateString, gbc);

        // PASSWORD
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Contraseña:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);

        // Confirm password
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Confirme contraseña:"), gbc);

        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(20);
        formPanel.add(confirmPasswordField, gbc);

        // BUTTON PANEL
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        formPanel.add(buttonPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);

        pack();

        initEvents();
    }

    private void initEvents() {

        registerButton.addActionListener(e -> register());

        loginButton.addActionListener(e -> {
            dispose();

            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }

    private void register() {
        UUID id = UUID.randomUUID();

        String name = nameField.getText();
        String lastname = lastnameField.getText();
        String email = emailField.getText();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse(dateString.getText(), formatter);

        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match");
            return;
        }

        AuthService authService = new AuthService();

        boolean success = authService.register(id,  name, lastname, email, date, password);
        if (success) {
            JOptionPane.showMessageDialog(this, "User registered successfully");

            dispose();

            LoginView loginView = new LoginView();
            loginView.setVisible(true);
            return;
        }

    }

    // Getter and Setters
    public String getNameValue() {
        return nameField.getText();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public char[] getPassword() {
        return passwordField.getPassword();
    }

    public JButton getRegisterButton() {
        return registerButton;
    }

    public JButton getLoginButton() {
        return loginButton;
    }
    public JFormattedTextField getDateString() {
        return dateString;
    }
    public void setDateString(JFormattedTextField dateString) {
        this.dateString = dateString;
    }
}