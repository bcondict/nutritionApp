package edu.ucompensar.codigo.model.interfaces;

import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.entity.MedicalCondition;

public interface IMedicalConditionDAO {
    
    // CRUD básico
    void save(MedicalCondition medicalCondition);
    MedicalCondition findById(UUID id);
    List<MedicalCondition> findAll();
    void update(MedicalCondition medicalCondition);
    void delete(UUID id);
    
    // Métodos específicos de búsqueda
    MedicalCondition findByCode(String code);
    List<MedicalCondition> findByLabelContaining(String label);
    List<MedicalCondition> findByDescriptionContaining(String keyword);
    
    // Métodos de utilidad
    boolean existsById(UUID id);
    boolean existsByCode(String code);
    int countAll();
    void deleteByCode(String code);
    
    // Métodos de validación
    void saveWithValidation(MedicalCondition medicalCondition);
    void updateWithValidation(MedicalCondition medicalCondition);
    
    // Métodos de búsqueda avanzada
    List<MedicalCondition> findAllOrderByCode();
    List<MedicalCondition> findAllOrderByLabel();
}