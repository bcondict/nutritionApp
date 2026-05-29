package edu.ucompensar.codigo.ui;

import java.awt.*;
import javax.swing.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import edu.ucompensar.codigo.entity.User;
import edu.ucompensar.codigo.model.enums.ActivityLevel;
import edu.ucompensar.codigo.model.enums.Sex;
import edu.ucompensar.codigo.service.SessionManager;
import edu.ucompensar.codigo.service.UserCharacterizationService;

public class ProfileCompletionView extends JFrame {
    private User currentUser = SessionManager.getCurrentUser();
    private UserCharacterizationService userCharacterizationService = new UserCharacterizationService();

    private JTextField heightField;
    private JTextField weightField;
    private JComboBox sexComboBox;
    private JComboBox activityLevelComboBox;
    private JFormattedTextField measureDateField;
    private JButton saveButton;

    public ProfileCompletionView() {
        setTitle("Complete Your Profile - Nutrition App");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
    }


    private void initializeComponents() {
        JLabel titleLabel = new JLabel("Completa tu perfil");
        add(titleLabel);

        JLabel userProfileLabel = new JLabel("Información del Perfil de Usuario:");
        add(userProfileLabel);

        JPanel userProfilePanel = new JPanel(new GridLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Height
        gbc.gridx = 0;
        gbc.gridy = 0;
        userProfilePanel.add(new JLabel("Altura:"));

        gbc.gridx = 1;
        heightField = new JTextField(20);
        userProfilePanel.add(heightField, gbc);

        // weight
        gbc.gridx = 0;
        gbc.gridy = 1;
        userProfilePanel.add(new JLabel("Peso:"));

        gbc.gridx = 1;
        weightField = new JTextField(20);
        userProfilePanel.add(weightField, gbc);

        // Sex
        gbc.gridx = 0;
        gbc.gridy = 2;
        userProfilePanel.add(new JLabel("Sexo:"));

        gbc.gridx = 1;
        Sex[] sexOptions = Sex.values();
        sexComboBox = new JComboBox<>(sexOptions);
        userProfilePanel.add(sexComboBox, gbc);

        // Activity level
        gbc.gridx = 0;
        gbc.gridy = 3;
        userProfilePanel.add(new JLabel("Nivel de actividad física:"));

        gbc.gridx = 1;
        ActivityLevel[] activityLevelOptions = ActivityLevel.values();
        activityLevelComboBox = new JComboBox<>(activityLevelOptions);
        userProfilePanel.add(activityLevelComboBox, gbc);

        // Measure date
        gbc.gridx = 0;
        gbc.gridy = 4;
        userProfilePanel.add(new JLabel("Fecha de la medición:"));

        gbc.gridx = 1;
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        measureDateField = new JFormattedTextField(format);
        measureDateField.setColumns(20);

        userProfilePanel.add(measureDateField, gbc);

        add(userProfilePanel);

        // Save button
        JButton saveButton = new JButton("Guardar");
        add(saveButton);

        // User Medical Condition
        
    }
}
