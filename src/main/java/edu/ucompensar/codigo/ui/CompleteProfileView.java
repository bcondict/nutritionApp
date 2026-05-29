package edu.ucompensar.codigo.ui;

import edu.ucompensar.codigo.entity.UserProfile;
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
    private JComboBox<ActivityLevel> activityCombo;
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
        // ActivityLevel[] activities = {ActivityLevel.SEDENTARY, ActivityLevel.LIGHT, ActivityLevel.MODERATE, ActivityLevel.ACTIVE, ActivityLevel.VERY_ACTIVE};
        ActivityLevel[] activities = ActivityLevel.values();
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
            // Validar campos
            if (weightField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese su peso");
                return;
            }
            
            if (heightField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese su altura");
                return;
            }
            
            Sex sex = Sex.valueOf(sexCombo.getSelectedItem().toString());
            BigDecimal weight = new BigDecimal(weightField.getText());
            Integer height = Integer.parseInt(heightField.getText());
            ActivityLevel activity = ActivityLevel.valueOf(activityCombo.getSelectedItem().toString());
            
            // Validar valores
            if (weight.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "El peso debe ser mayor a 0");
                return;
            }
            
            if (height <= 0) {
                JOptionPane.showMessageDialog(this, "La altura debe ser mayor a 0");
                return;
            }
            
            if (weight.compareTo(new BigDecimal("300")) > 0) {
                JOptionPane.showMessageDialog(this, "El peso parece demasiado alto. Verifique el valor.");
                return;
            }
            
            if (height > 300) {
                JOptionPane.showMessageDialog(this, "La altura parece demasiado alta. Verifique el valor.");
                return;
            }
            
            // Buscar si ya existe un perfil para este usuario
            UserProfile existingProfile = profileService.getLatestProfile(userId);
            
            LocalDateTime now = LocalDateTime.now();
            
            if (existingProfile == null) {
                // Crear nuevo perfil
                UserProfile newProfile = new UserProfile(UUID.randomUUID(), userId, weight, height, sex, activity, now, now, now);
                profileService.save(newProfile);
            } else {
                // Actualizar perfil existente
                existingProfile.setWeightKg(weight);
                existingProfile.setHeightCm(height);
                existingProfile.setSex(sex);
                existingProfile.setActivityLevel(activity);
                existingProfile.setMeasuredAt(now);
                existingProfile.setUpdatedAt(now);
                
                profileService.update(existingProfile);
            }
            
            JOptionPane.showMessageDialog(this, "Perfil guardado exitosamente");
            openMainView();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos para peso y altura");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void saveProfile2() {
        try {
            Sex sex = Sex.valueOf((String) sexCombo.getSelectedItem());
            BigDecimal weight = new BigDecimal(weightField.getText());
            Integer height = Integer.parseInt(heightField.getText());
            ActivityLevel activity = (ActivityLevel) activityCombo.getSelectedItem();
            
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