package edu.ucompensar.codigo.model.interfaces;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.entity.UserDietaryPreference;

public interface IUserDietaryPreferenceDAO {
    
    // CRUD básico
    void save(UserDietaryPreference userDietaryPreference);
    UserDietaryPreference findById(UUID id);
    List<UserDietaryPreference> findAll();
    void update(UserDietaryPreference userDietaryPreference);
    void delete(UUID id);
    
    // Métodos de búsqueda por usuario
    List<UserDietaryPreference> findByUserId(UUID userId);
    List<UserDietaryPreference> findByUserIdWithDetails(UUID userId);
    List<UUID> findDietaryPreferenceIdsByUserId(UUID userId);
    
    // Métodos de búsqueda por preferencia dietética
    List<UserDietaryPreference> findByDietaryPreferenceId(UUID dietaryPreferenceId);
    List<UUID> findUserIdsByDietaryPreferenceId(UUID dietaryPreferenceId);
    
    // Métodos de búsqueda combinada
    UserDietaryPreference findByUserIdAndDietaryPreferenceId(UUID userId, UUID dietaryPreferenceId);
    boolean existsByUserIdAndDietaryPreferenceId(UUID userId, UUID dietaryPreferenceId);
    
    // Métodos de utilidad
    int countByUserId(UUID userId);
    int countByDietaryPreferenceId(UUID dietaryPreferenceId);
    List<UUID> findUsersWithMultiplePreferences();
    List<UUID> findUsersWithPreferenceCount(int minCount);
    
    // Métodos de eliminación
    void deleteByUserId(UUID userId);
    void deleteByDietaryPreferenceId(UUID dietaryPreferenceId);
    void deleteByUserIdAndDietaryPreferenceId(UUID userId, UUID dietaryPreferenceId);
    
    // Métodos de validación
    void saveWithValidation(UserDietaryPreference userDietaryPreference);
    void updateWithValidation(UserDietaryPreference userDietaryPreference);
    
    // Métodos de bulk operations
    void saveAll(List<UserDietaryPreference> userPreferences);
    void deleteAllByUserId(UUID userId);
    
    // Métodos de reportes
    List<UserDietaryPreference> findAllOrderByCreatedAtDesc();
    List<UserDietaryPreference> findUserPreferencesCreatedAfter(LocalDateTime date);
    List<UserDietaryPreference> findUserPreferencesCreatedBefore(LocalDateTime date);
    List<UserDietaryPreference> findUserPreferencesByUserIdOrderByCreatedAt(UUID userId);
}