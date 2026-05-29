package edu.ucompensar.codigo.model.interfaces;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.entity.Recipe;
import edu.ucompensar.codigo.model.enums.MealType;
import edu.ucompensar.codigo.model.enums.RecipeDifficulty;

public interface IRecipeDAO {
    // CRUD básico
    void save(Recipe recipe);
    Recipe findById(UUID id);
    List<Recipe> findAll();
    void update(Recipe recipe);
    void delete(UUID id);

    // Métodos de búsqueda por nombre
    List<Recipe> findByNameContaining(String name);
    List<Recipe> findByNameExact(String name);

    // Métodos de búsqueda por tipo de comida
    List<Recipe> findByMealType(MealType mealType);
    List<Recipe> findByMealTypeOrderByName(MealType mealType);

    // Métodos de búsqueda por dificultad
    List<Recipe> findByDifficulty(RecipeDifficulty difficulty);
    List<Recipe> findByDifficultyOrderByPrepTime(RecipeDifficulty difficulty);

    // Métodos de búsqueda combinada
    List<Recipe> findByMealTypeAndDifficulty(MealType mealType, RecipeDifficulty difficulty);
    List<Recipe> findByPrepTimeLessThan(int maxMinutes);
    List<Recipe> findByPrepTimeBetween(int minMinutes, int maxMinutes);
    List<Recipe> findByServingsGreaterThan(int minServings);
    List<Recipe> findByServingsLessThan(int maxServings);

    // Métodos de búsqueda avanzada
    List<Recipe> searchRecipes(String keyword, MealType mealType, RecipeDifficulty difficulty, Integer maxPrepTime);
    List<Recipe> findQuickRecipes(int maxMinutes);
    List<Recipe> findBeginnerFriendlyRecipes();

    // Métodos de utilidad
    boolean existsById(UUID id);
    boolean existsByName(String name);
    int countAll();
    int countByMealType(MealType mealType);
    int countByDifficulty(RecipeDifficulty difficulty);
    double getAveragePrepTime();
    int getTotalServings();

    // Métodos de eliminación
    void deleteByName(String name);
    void deleteByMealType(MealType mealType);

    // Métodos de validación
    void saveWithValidation(Recipe recipe);
    void updateWithValidation(Recipe recipe);

    // Métodos de reportes
    List<Recipe> findAllOrderByName();
    List<Recipe> findAllOrderByPrepTimeAsc();
    List<Recipe> findAllOrderByPrepTimeDesc();
    List<Recipe> findMostPopularRecipes(int limit);
    List<Recipe> findRecipesCreatedAfter(LocalDateTime date);
    List<Recipe> findRecipesCreatedBefore(LocalDateTime date);
}