package edu.ucompensar.codigo.ui;

import java.util.List;
import java.util.UUID;

import javax.swing.*;

import edu.ucompensar.codigo.entity.User;
import edu.ucompensar.codigo.entity.UserMedicalCondition;
import edu.ucompensar.codigo.entity.UserProfile;
import edu.ucompensar.codigo.service.SessionManager;
import edu.ucompensar.codigo.service.UserCharacterizationService;

public class MainWindow extends JFrame {
    private User currentUser = SessionManager.getCurrentUser();

    private UserCharacterizationService userCharacterizationService = new UserCharacterizationService();

    public MainWindow() {

        setTitle("Nutrition app - " + currentUser.getName());

        // Window settings
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initialzeComponents(isUserProfileComplete());
    }

    private void initialzeComponents(boolean isProfileComplete) {
        JLabel title = new JLabel("Nutrition APP");
        add(title);

        if (isProfileComplete) {
            initializeProfleComplete();
        }
        else {
            initializeProfileIncomplete();
        }
    }

    private void initializeProfleComplete() {
        UserProfile userProfile = getUserProfile(currentUser.getId());
        List<UserMedicalCondition> userMedicalConditions = getUserMedicalCondition(currentUser.getId());

        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getName() + "!");
        add(welcomeLabel);

    }

    private void initializeProfileIncomplete() {
        JLabel incompleteProfileLabel = new JLabel("Complete su perfil para poder crear un plan nutricional personalizado");
        add(incompleteProfileLabel);

        JButton completeProfileButton = new JButton("Completar perfil");
        completeProfileButton.addActionListener(e -> {
            // Open profile completion view
            ProfileCompletionView profileCompletionView = new ProfileCompletionView();
            profileCompletionView.setVisible(true);
            this.dispose();
        });
        add(completeProfileButton);
    }

    private boolean isUserProfileComplete() {
        User currentUser = SessionManager.getCurrentUser();
        // Check if the user's profile is complete
        if (currentUser == null) {
            return false;
        }

        boolean hasUserProfileComplete = userCharacterizationService.isUserProfileComplete(currentUser);
        if (!hasUserProfileComplete) {
            return false;
        }

        return true;
    }

    private UserProfile getUserProfile(UUID userId) {
        UserProfile profile = userCharacterizationService.getUserProfile(userId);
        return profile;
    }

    private List<UserMedicalCondition> getUserMedicalCondition(UUID userId) {
        List<UserMedicalCondition> userMedicalConditions = userCharacterizationService.getUserMedicalConditions(userId);
        if (userMedicalConditions == null || userMedicalConditions.isEmpty()) {
            return null;
        }

        return userMedicalConditions;
    }

}