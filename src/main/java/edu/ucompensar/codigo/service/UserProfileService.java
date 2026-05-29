package edu.ucompensar.codigo.service;

import edu.ucompensar.codigo.DAO.UserProfileDAO;
import edu.ucompensar.codigo.entity.UserProfile;
import edu.ucompensar.codigo.model.enums.ActivityLevel;
import edu.ucompensar.codigo.model.enums.Sex;
import edu.ucompensar.codigo.model.interfaces.IUserProfileDAO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class UserProfileService {
    private final IUserProfileDAO userProfileDAO;

    public UserProfileService() {
        this.userProfileDAO = new UserProfileDAO();
    }

    public void save(UserProfile profile) {
        userProfileDAO.save(profile);
    }

    public List<UserProfile> findByUserId(UUID userId) {
        return userProfileDAO.findByUserId(userId);
    }

    public UserProfile getLatestProfile(UUID userId) {
        return userProfileDAO.findLatestByUserId(userId);
    }

    public void update(UserProfile profile) {
        userProfileDAO.update(profile);
    }

    public boolean hasCompleteProfile(UUID userId) {
        UserProfile profile = getLatestProfile(userId);
        return profile != null && 
               profile.getWeightKg() != null && 
               profile.getHeightCm() != null &&
               profile.getSex() != null &&
               profile.getActivityLevel() != null;
    }

    public void saveOrUpdate(UUID userId, BigDecimal weightKg, Integer heightCm, Sex sex, ActivityLevel activityLevel, LocalDateTime measuredAt) {
        UserProfile existing = getLatestProfile(userId);
        
        if (existing == null) {
            UserProfile newProfile = new UserProfile();
            newProfile.setUserId(userId);
            newProfile.setWeightKg(weightKg);
            newProfile.setHeightCm(heightCm);
            newProfile.setSex(sex);
            newProfile.setActivityLevel(activityLevel);
            newProfile.setMeasuredAt(measuredAt);
            save(newProfile);
        } else {
            existing.setWeightKg(weightKg);
            existing.setHeightCm(heightCm);
            existing.setSex(sex);
            existing.setActivityLevel(activityLevel);
            existing.setMeasuredAt(measuredAt);
            update(existing);
        }
    }
}