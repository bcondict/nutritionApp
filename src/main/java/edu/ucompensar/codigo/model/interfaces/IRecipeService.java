package edu.ucompensar.codigo.model.interfaces;

import edu.ucompensar.codigo.entity.Recipe;
import edu.ucompensar.codigo.model.enums.MealType;
import edu.ucompensar.codigo.model.enums.RecipeDifficulty;
import edu.ucompensar.codigo.service.RecipeService.RecipeNutritionSummary;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IRecipeService {

    // ==================== CRUD BÁSICO ====================

    void save(Recipe recipe);

    Recipe findById(UUID id);

    List<Recipe> findAll();

    void update(Recipe recipe);

    void delete(UUID id);

    // ==================== BÚSQUEDA POR NOMBRE ====================

    List<Recipe> findByNameContaining(String name);

    Recipe findByNameExact(String name);

    // ==================== BÚSQUEDA POR TIPO DE COMIDA ====================

    List<Recipe> findByMealType(MealType mealType);

    List<Recipe> findByMealTypeOrderByName(MealType mealType);

    // ==================== BÚSQUEDA POR DIFICULTAD ====================

    List<Recipe> findByDifficulty(RecipeDifficulty difficulty);

    List<Recipe> findByDifficultyOrderByPrepTime(RecipeDifficulty difficulty);

    // ==================== BÚSQUEDA COMBINADA ====================

    List<Recipe> findByMealTypeAndDifficulty( MealType mealType, RecipeDifficulty difficulty);

    List<Recipe> findByPrepTimeLessThan(int maxMinutes);

    List<Recipe> findByPrepTimeBetween(int minMinutes, int maxMinutes);

    List<Recipe> findByServingsGreaterThan(int minServings);

    List<Recipe> findByServingsLessThan(int maxServings);

    // ==================== BÚSQUEDA AVANZADA ====================

    List<Recipe> searchRecipes(
        String keyword,
        MealType mealType,
        RecipeDifficulty difficulty,
        Integer maxPrepTime
    );

    List<Recipe> findQuickRecipes(int maxMinutes);

    List<Recipe> findBeginnerFriendlyRecipes();

    // ==================== MÉTODOS DE UTILIDAD ====================

    boolean existsById(UUID id);

    boolean existsByName(String name);

    int countAll();

    int countByMealType(MealType mealType);

    int countByDifficulty(RecipeDifficulty difficulty);

    double getAveragePrepTime();

    int getTotalServings();

    // ==================== ELIMINACIÓN ====================

    void deleteByName(String name);

    void deleteByMealType(MealType mealType);

    // ==================== VALIDACIÓN ====================

    void saveWithValidation(Recipe recipe);

    void updateWithValidation(Recipe recipe);

    // ==================== REPORTES ====================

    List<Recipe> findAllOrderByName();

    List<Recipe> findAllOrderByPrepTimeAsc();

    List<Recipe> findAllOrderByPrepTimeDesc();

    List<Recipe> findMostPopularRecipes(int limit);

    List<Recipe> findRecipesCreatedAfter(LocalDateTime date);

    List<Recipe> findRecipesCreatedBefore(LocalDateTime date);

    // ==================== MÉTODOS ADICIONALES ====================

    List<Recipe> getRecommendedRecipesForMeal(MealType mealType, int limit);

    List<Recipe> getQuickRecipesForMeal(String mealType, int maxMinutes);

    RecipeNutritionSummary getRecipeNutritionSummary(UUID recipeId);
}
