package edu.ucompensar.codigo.model.interfaces;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.entity.UserMedicalCondition;
import edu.ucompensar.codigo.model.enums.MedicalConditionSeverity;

public interface IUserMedicalConditionDAO {
    
    // CRUD básico
    void save(UserMedicalCondition userMedicalCondition);
    UserMedicalCondition findById(UUID id);
    List<UserMedicalCondition> findAll();
    void update(UserMedicalCondition userMedicalCondition);
    void delete(UUID id);
    
    // Métodos específicos de búsqueda por usuario
    List<UserMedicalCondition> findByUserId(UUID userId);
    List<UserMedicalCondition> findByUserIdWithSeverity(UUID userId, MedicalConditionSeverity severity);
    List<UserMedicalCondition> findByUserIdDiagnosedAfter(UUID userId, LocalDateTime date);
    List<UserMedicalCondition> findByUserIdDiagnosedBefore(UUID userId, LocalDateTime date);
    
    // Métodos específicos de búsqueda por condición médica
    List<UserMedicalCondition> findByMedicalConditionId(UUID medicalConditionId);
    List<UserMedicalCondition> findByMedicalConditionIdWithSeverity(UUID medicalConditionId, MedicalConditionSeverity severity);
    
    // Métodos de búsqueda combinada
    List<UserMedicalCondition> findByUserIdAndMedicalConditionId(UUID userId, UUID medicalConditionId);
    UserMedicalCondition findLatestByUserIdAndMedicalConditionId(UUID userId, UUID medicalConditionId);
    
    // Métodos de utilidad
    boolean existsById(UUID id);
    boolean existsByUserIdAndMedicalConditionId(UUID userId, UUID medicalConditionId);
    int countByUserId(UUID userId);
    int countByMedicalConditionId(UUID medicalConditionId);
    int countBySeverity(String severity);
    
    // Métodos de eliminación
    void deleteByUserId(UUID userId);
    void deleteByMedicalConditionId(UUID medicalConditionId);
    void deleteByUserIdAndMedicalConditionId(UUID userId, UUID medicalConditionId);
    
    // Métodos de validación
    void saveWithValidation(UserMedicalCondition userMedicalCondition);
    void updateWithValidation(UserMedicalCondition userMedicalCondition);
    
    // Métodos de reportes
    List<UserMedicalCondition> findCriticalConditionsByUser(UUID userId);
    List<UserMedicalCondition> findAllWithNotes();
    List<UserMedicalCondition> findAllOrderByDiagnosedAtDesc();
}