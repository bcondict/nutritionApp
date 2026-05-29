package edu.ucompensar.codigo.model.interfaces;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.entity.RecipeIngredient;

public interface IRecipeIngredientDAO {
    // CRUD básico
    void save(RecipeIngredient recipeIngredient);
    RecipeIngredient findById(UUID id);
    List<RecipeIngredient> findAll();
    void update(RecipeIngredient recipeIngredient);
    void delete(UUID id);
    
    // Métodos de búsqueda por receta
    List<RecipeIngredient> findByRecipeId(UUID recipeId);
    List<RecipeIngredient> findByRecipeIdWithDetails(UUID recipeId);
    List<RecipeIngredient> findByRecipeIdOrderByQuantityDesc(UUID recipeId);
    
    // Métodos de búsqueda por alimento
    List<RecipeIngredient> findByFoodItemId(UUID foodItemId);
    List<UUID> findRecipeIdsByFoodItemId(UUID foodItemId);
    
    // Métodos de búsqueda combinada
    RecipeIngredient findByRecipeIdAndFoodItemId(UUID recipeId, UUID foodItemId);
    boolean existsByRecipeIdAndFoodItemId(UUID recipeId, UUID foodItemId);
    
    // Métodos de eliminación
    void deleteByRecipeId(UUID recipeId);
    void deleteByFoodItemId(UUID foodItemId);
    void deleteByRecipeIdAndFoodItemId(UUID recipeId, UUID foodItemId);
    
    // Métodos de utilidad
    int countByRecipeId(UUID recipeId);
    int countByFoodItemId(UUID foodItemId);
    BigDecimal getTotalQuantityByRecipeId(UUID recipeId);
    int getIngredientCountByRecipeId(UUID recipeId);
    
    // Métodos de validación
    void saveWithValidation(RecipeIngredient recipeIngredient);
    void updateWithValidation(RecipeIngredient recipeIngredient);
    
    // Métodos de bulk operations
    void saveAll(List<RecipeIngredient> ingredients);
    void updateQuantitiesByRecipeId(UUID recipeId, BigDecimal multiplier);
    
    // Métodos de reportes
    List<RecipeIngredient> findAllOrderByCreatedAtDesc();
    List<RecipeIngredient> findIngredientsCreatedAfter(LocalDateTime date);
    List<RecipeIngredient> findIngredientsCreatedBefore(LocalDateTime date);
    List<RecipeIngredient> findIngredientsWithQuantityGreaterThan(BigDecimal minQuantity);
}