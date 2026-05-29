package edu.ucompensar.codigo.ui;

import edu.ucompensar.codigo.model.enums.ActivityLevel;
import edu.ucompensar.codigo.model.enums.Sex;
import edu.ucompensar.codigo.service.AuthService;
import edu.ucompensar.codigo.service.UserProfileService;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class CompleteProfileView extends JFrame {
    private JComboBox<String> sexCombo;
    private JTextField weightField;
    private JTextField heightField;
    private JComboBox<String> activityCombo;
    private JButton saveButton;
    private JButton skipButton;
    
    private final UUID userId;
    private final AuthService authService;
    private final UserProfileService profileService;

    public CompleteProfileView(UUID userId) {
        this.userId = userId;
        this.authService = new AuthService();
        this.profileService = new UserProfileService();
        
        setTitle("Completar Perfil");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel title = new JLabel("Completa tu perfil", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(title, BorderLayout.NORTH);
        
        // Formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Sexo
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Sexo:"), gbc);
        
        gbc.gridx = 1;
        String[] sexes = {"MALE", "FEMALE", "OTHER"};
        sexCombo = new JComboBox<>(sexes);
        formPanel.add(sexCombo, gbc);
        
        // Peso
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Peso (kg):"), gbc);
        
        gbc.gridx = 1;
        weightField = new JTextField(15);
        formPanel.add(weightField, gbc);
        
        // Altura
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Altura (cm):"), gbc);
        
        gbc.gridx = 1;
        heightField = new JTextField(15);
        formPanel.add(heightField, gbc);
        
        // Nivel de actividad
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Nivel de actividad:"), gbc);
        
        gbc.gridx = 1;
        String[] activities = {"SEDENTARY", "LIGHT", "MODERATE", "ACTIVE", "VERY_ACTIVE"};
        activityCombo = new JComboBox<>(activities);
        formPanel.add(activityCombo, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        saveButton = new JButton("Guardar y continuar");
        skipButton = new JButton("Omitir por ahora");
        buttonPanel.add(saveButton);
        buttonPanel.add(skipButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        pack();
        
        initEvents();
    }
    
    private void initEvents() {
        saveButton.addActionListener(e -> saveProfile());
        skipButton.addActionListener(e -> {
            openMainView();
        });
    }
    
    private void saveProfile() {
        try {
            Sex sex = Sex.valueOf((String) sexCombo.getSelectedItem());
            BigDecimal weight = new BigDecimal(weightField.getText());
            Integer height = Integer.parseInt(heightField.getText());
            ActivityLevel activity = ActivityLevel.valueOf((String) activityCombo.getSelectedItem());
            
            if (weight.compareTo(BigDecimal.ZERO) <= 0 || height <= 0) {
                JOptionPane.showMessageDialog(this, "Peso y altura deben ser valores positivos");
                return;
            }
            
            profileService.saveOrUpdate(userId, weight, height, sex, activity, LocalDateTime.now());
            
            JOptionPane.showMessageDialog(this, "Perfil guardado exitosamente");
            openMainView();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }
    
    private void openMainView() {
        dispose();
        MainView mainView = new MainView(userId);
        mainView.setVisible(true);
    }
}