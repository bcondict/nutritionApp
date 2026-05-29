package edu.ucompensar.codigo.service;

import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.DAO.UserMedicalConditionDAO;
import edu.ucompensar.codigo.DAO.UserProfileDAO;
import edu.ucompensar.codigo.entity.User;
import edu.ucompensar.codigo.entity.UserMedicalCondition;
import edu.ucompensar.codigo.entity.UserProfile;

public class UserCharacterizationService {
    private UserProfileDAO userProfileDAO;
    private UserMedicalConditionDAO userMedicalConditionDAO;

    public UserCharacterizationService () {
        this.userProfileDAO = new UserProfileDAO();
        this.userMedicalConditionDAO = new UserMedicalConditionDAO();
    }

    public UserProfile getUserProfile(UUID userId) {
        UserProfile profile = userProfileDAO.findLatestByUserId(userId);
        return profile;
    }
    public List<UserProfile> getUserProfiles(UUID userId) {
        return userProfileDAO.findByUserId(userId);
    }

    public List<UserMedicalCondition> getUserMedicalConditions(UUID userId) {
        return userMedicalConditionDAO.findByUserId(userId);
    }

    public boolean isUserProfileComplete(User user) {
        // Check if the user's profile is complete
        if (user == null) {
            return false;
        }

        List<UserProfile> profiles = userProfileDAO.findByUserId(user.getId());
        if (profiles == null || profiles.isEmpty()) {
            return false;
        }

        List<UserMedicalCondition> medicalConditions = userMedicalConditionDAO.findByUserId(user.getId());
        if (medicalConditions == null || medicalConditions.isEmpty()) {
            return false;
        }

        return false;
    }
}
