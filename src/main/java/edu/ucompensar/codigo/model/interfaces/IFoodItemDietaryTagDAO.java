package edu.ucompensar.codigo.model.interfaces;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.entity.FoodItemDietaryTag;

public interface IFoodItemDietaryTagDAO {
    // CRUD básico
    void save(FoodItemDietaryTag foodItemDietaryTag);
    FoodItemDietaryTag findById(UUID id);
    List<FoodItemDietaryTag> findAll();
    void update(FoodItemDietaryTag foodItemDietaryTag);
    void delete(UUID id);
    
    // Métodos de búsqueda por food item
    List<FoodItemDietaryTag> findByFoodItemId(UUID foodItemId);
    List<FoodItemDietaryTag> findByFoodItemIdWithDetails(UUID foodItemId);
    List<UUID> findDietaryPreferenceIdsByFoodItemId(UUID foodItemId);
    
    // Métodos de búsqueda por dietary preference
    List<FoodItemDietaryTag> findByDietaryPreferenceId(UUID dietaryPreferenceId);
    List<FoodItemDietaryTag> findByDietaryPreferenceIdWithDetails(UUID dietaryPreferenceId);
    List<UUID> findFoodItemIdsByDietaryPreferenceId(UUID dietaryPreferenceId);
    
    // Métodos de búsqueda combinada
    FoodItemDietaryTag findByFoodItemIdAndDietaryPreferenceId(UUID foodItemId, UUID dietaryPreferenceId);
    boolean existsByFoodItemIdAndDietaryPreferenceId(UUID foodItemId, UUID dietaryPreferenceId);
    
    // Métodos de búsqueda por código/label
    List<FoodItemDietaryTag> findFoodItemsByDietaryPreferenceCode(String code);
    List<FoodItemDietaryTag> findFoodItemsByDietaryPreferenceLabel(String label);
    
    // Métodos de utilidad
    int countByFoodItemId(UUID foodItemId);
    int countByDietaryPreferenceId(UUID dietaryPreferenceId);
    List<FoodItemDietaryTag> findFoodItemsWithMultipleTags();
    List<FoodItemDietaryTag> findFoodItemsWithTagCount(int minCount);
    
    // Métodos de eliminación
    void deleteByFoodItemId(UUID foodItemId);
    void deleteByDietaryPreferenceId(UUID dietaryPreferenceId);
    void deleteByFoodItemIdAndDietaryPreferenceId(UUID foodItemId, UUID dietaryPreferenceId);
    
    // Métodos de validación
    void saveWithValidation(FoodItemDietaryTag foodItemDietaryTag);
    void updateWithValidation(FoodItemDietaryTag foodItemDietaryTag);
    
    // Métodos de bulk operations
    void saveAll(List<FoodItemDietaryTag> foodItemTags);
    void deleteAllByFoodItemId(UUID foodItemId);
    void addTagsToFoodItem(UUID foodItemId, List<UUID> dietaryPreferenceIds);
    
    // Métodos de reportes
    List<FoodItemDietaryTag> findAllOrderByCreatedAtDesc();
    List<FoodItemDietaryTag> findTagsCreatedAfter(LocalDateTime date);
    List<FoodItemDietaryTag> findTagsCreatedBefore(LocalDateTime date);
    List<FoodItemDietaryTag> findTagsByFoodItemIdOrderByCreatedAt(UUID foodItemId);
}