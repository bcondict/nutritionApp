package edu.ucompensar.codigo.model.interfaces;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.entity.UserProfile;

public interface IUserProfileDAO {
    
    // CRUD básico
    void save(UserProfile userProfile);
    UserProfile findById(UUID id);
    List<UserProfile> findAll();
    void update(UserProfile userProfile);
    void delete(UUID id);
    
    // Métodos específicos de búsqueda
    UserProfile findLatestByUserId(UUID userId);
    List<UserProfile> findByUserId(UUID userId);
    List<UserProfile> findByUserIdAndDateRange(UUID userId, LocalDateTime startDate, LocalDateTime endDate);
    
    // Métodos de utilidad
    boolean existsById(UUID id);
    int countByUserId(UUID userId);
    void deleteByUserId(UUID userId);
    
    // Métodos para reportes y análisis
    List<UserProfile> findProfilesWithBmiBetween(BigDecimal minBmi, BigDecimal maxBmi);
    List<UserProfile> findProfilesBySex(String sex);
    List<UserProfile> findProfilesByActivityLevel(String activityLevel);
}