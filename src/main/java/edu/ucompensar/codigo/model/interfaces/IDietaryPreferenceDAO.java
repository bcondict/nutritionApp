package edu.ucompensar.codigo.model.interfaces;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.entity.DietaryPreference;
import edu.ucompensar.codigo.model.enums.DietaryPreferenceCategory;
import edu.ucompensar.codigo.model.enums.DietaryPreferenceCode;

public interface IDietaryPreferenceDAO {
    // CRUD básico
    void save(DietaryPreference dietaryPreference);
    DietaryPreference findById(UUID id);
    List<DietaryPreference> findAll();
    void update(DietaryPreference dietaryPreference);
    void delete(UUID id);
    
    // Métodos de búsqueda por código
    DietaryPreference findByCode(DietaryPreferenceCode code);
    List<DietaryPreference> findByCodeContaining(DietaryPreferenceCode code);
    
    // Métodos de búsqueda por etiqueta
    List<DietaryPreference> findByLabelContaining(String label);
    List<DietaryPreference> findByLabelExact(String label);
    
    // Métodos de búsqueda por categoría
    List<DietaryPreference> findByCategory(DietaryPreferenceCategory category);
    List<DietaryPreference> findByCategoryOrderByLabel(DietaryPreferenceCategory category);
    List<String> findAllCategories();
    
    // Métodos de búsqueda combinada
    List<DietaryPreference> findByCategoryAndCodeContaining(DietaryPreferenceCategory category, DietaryPreferenceCode code);
    List<DietaryPreference> searchDietaryPreferences(String keyword, DietaryPreferenceCategory category);
    
    // Métodos de utilidad
    boolean existsById(UUID id);
    boolean existsByCode(DietaryPreferenceCode code);
    boolean existsByLabel(String label);
    int countAll();
    int countByCategory(DietaryPreferenceCategory category);
    List<DietaryPreference> getCommonPreferences();
    
    // Métodos de eliminación
    void deleteByCode(DietaryPreferenceCode code);
    void deleteByCategory(DietaryPreferenceCategory category);
    
    // Métodos de validación
    void saveWithValidation(DietaryPreference dietaryPreference);
    void updateWithValidation(DietaryPreference dietaryPreference);
    
    // Métodos de reportes
    List<DietaryPreference> findAllOrderByCode();
    List<DietaryPreference> findAllOrderByLabel();
    List<DietaryPreference> findAllOrderByCategory();
    List<DietaryPreference> findPreferencesCreatedAfter(LocalDateTime date);
    List<DietaryPreference> findPreferencesCreatedBefore(LocalDateTime date);
    List<DietaryPreference> findPreferencesByCategoryWithCount(DietaryPreferenceCategory category);
}