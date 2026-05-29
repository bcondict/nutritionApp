package edu.ucompensar.codigo.service;

import edu.ucompensar.codigo.DAO.RecipeDAO;
import edu.ucompensar.codigo.entity.Recipe;
import edu.ucompensar.codigo.model.interfaces.IRecipeDAO;
import edu.ucompensar.codigo.model.enums.MealType;
import edu.ucompensar.codigo.model.enums.RecipeDifficulty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class RecipeService {
    private final IRecipeDAO recipeDAO;

    public RecipeService() {
        this.recipeDAO = new RecipeDAO();
    }

    // ==================== CRUD BÁSICO ====================
    
    public void save(Recipe recipe) {
        recipeDAO.save(recipe);
    }

    public Recipe findById(UUID id) {
        return recipeDAO.findById(id);
    }

    public List<Recipe> findAll() {
        return recipeDAO.findAll();
    }

    public void update(Recipe recipe) {
        recipeDAO.update(recipe);
    }

    public void delete(UUID id) {
        recipeDAO.delete(id);
    }

    // ==================== BÚSQUEDA POR NOMBRE ====================
    
    public List<Recipe> findByNameContaining(String name) {
        return recipeDAO.findByNameContaining(name);
    }

    public Recipe findByNameExact(String name) {
        List<Recipe> results = recipeDAO.findByNameExact(name);
        return results.isEmpty() ? null : results.get(0);
    }

    // ==================== BÚSQUEDA POR TIPO DE COMIDA ====================

    public List<Recipe> findByMealType(MealType mealType) {
        return recipeDAO.findByMealType(mealType);
    }

    public List<Recipe> findByMealTypeOrderByName(MealType mealType) {
        return recipeDAO.findByMealTypeOrderByName(mealType);
    }

    // ==================== BÚSQUEDA POR DIFICULTAD ====================
    
    public List<Recipe> findByDifficulty(RecipeDifficulty difficulty) {
        return recipeDAO.findByDifficulty(difficulty);
    }

    public List<Recipe> findByDifficultyOrderByPrepTime(RecipeDifficulty difficulty) {
        return recipeDAO.findByDifficultyOrderByPrepTime(difficulty);
    }

    // ==================== BÚSQUEDA COMBINADA ====================

    public List<Recipe> findByMealTypeAndDifficulty(MealType mealType, RecipeDifficulty difficulty) {
        return recipeDAO.findByMealTypeAndDifficulty(mealType, difficulty);
    }

    public List<Recipe> findByPrepTimeLessThan(int maxMinutes) {
        return recipeDAO.findByPrepTimeLessThan(maxMinutes);
    }

    public List<Recipe> findByPrepTimeBetween(int minMinutes, int maxMinutes) {
        return recipeDAO.findByPrepTimeBetween(minMinutes, maxMinutes);
    }

    public List<Recipe> findByServingsGreaterThan(int minServings) {
        return recipeDAO.findByServingsGreaterThan(minServings);
    }

    public List<Recipe> findByServingsLessThan(int maxServings) {
        return recipeDAO.findByServingsLessThan(maxServings);
    }

    // ==================== BÚSQUEDA AVANZADA ====================
    
    public List<Recipe> searchRecipes(String keyword, MealType mealType, RecipeDifficulty difficulty, Integer maxPrepTime) {
        return recipeDAO.searchRecipes(keyword, mealType, difficulty, maxPrepTime);
    }

    public List<Recipe> findQuickRecipes(int maxMinutes) {
        return recipeDAO.findQuickRecipes(maxMinutes);
    }

    public List<Recipe> findBeginnerFriendlyRecipes() {
        return recipeDAO.findBeginnerFriendlyRecipes();
    }

    // ==================== MÉTODOS DE UTILIDAD ====================
    
    public boolean existsById(UUID id) {
        return recipeDAO.existsById(id);
    }

    public boolean existsByName(String name) {
        return recipeDAO.existsByName(name);
    }

    public int countAll() {
        return recipeDAO.countAll();
    }

    public int countByMealType(MealType mealType) {
        return recipeDAO.countByMealType(mealType);
    }

    public int countByDifficulty(RecipeDifficulty difficulty) {
        return recipeDAO.countByDifficulty(difficulty);
    }

    public double getAveragePrepTime() {
        return recipeDAO.getAveragePrepTime();
    }

    public int getTotalServings() {
        return recipeDAO.getTotalServings();
    }

    // ==================== ELIMINACIÓN ====================
    
    public void deleteByName(String name) {
        recipeDAO.deleteByName(name);
    }

    public void deleteByMealType(MealType mealType) {
        recipeDAO.deleteByMealType(mealType);
    }

    // ==================== VALIDACIÓN ====================
    
    public void saveWithValidation(Recipe recipe) {
        recipeDAO.saveWithValidation(recipe);
    }

    public void updateWithValidation(Recipe recipe) {
        recipeDAO.updateWithValidation(recipe);
    }

    // ==================== REPORTES ====================
    
    public List<Recipe> findAllOrderByName() {
        return recipeDAO.findAllOrderByName();
    }

    public List<Recipe> findAllOrderByPrepTimeAsc() {
        return recipeDAO.findAllOrderByPrepTimeAsc();
    }

    public List<Recipe> findAllOrderByPrepTimeDesc() {
        return recipeDAO.findAllOrderByPrepTimeDesc();
    }

    public List<Recipe> findMostPopularRecipes(int limit) {
        return recipeDAO.findMostPopularRecipes(limit);
    }

    public List<Recipe> findRecipesCreatedAfter(LocalDateTime date) {
        return recipeDAO.findRecipesCreatedAfter(date);
    }

    public List<Recipe> findRecipesCreatedBefore(LocalDateTime date) {
        return recipeDAO.findRecipesCreatedBefore(date);
    }

    // ==================== MÉTODOS ADICIONALES ÚTILES ====================
    
    /**
     * Obtiene recetas recomendadas para un tipo de comida específico
     */
    public List<Recipe> getRecommendedRecipesForMeal(MealType mealType, int limit) {
        List<Recipe> recipes = findByMealType(mealType);
        return recipes.size() > limit ? recipes.subList(0, limit) : recipes;
    }

    /**
     * Obtiene recetas rápidas para un tipo de comida específico
     */
    public List<Recipe> getQuickRecipesForMeal(String mealType, int maxMinutes) {
        List<Recipe> allQuick = findQuickRecipes(maxMinutes);
        return allQuick.stream()
                .filter(r -> r.getMealType().equals(mealType))
                .toList();
    }

    /**
     * Obtiene el resumen nutricional aproximado de una receta
     * Nota: Esto requiere integración con FoodItemService
     */
    public RecipeNutritionSummary getRecipeNutritionSummary(UUID recipeId) {
        Recipe recipe = findById(recipeId);
        if (recipe == null) {
            return null;
        }
        
        // Aquí se integraría con RecipeIngredientService y FoodItemService
        // Por ahora retornamos un resumen básico
        RecipeNutritionSummary summary = new RecipeNutritionSummary();
        summary.setRecipeName(recipe.getName());
        summary.setServings(recipe.getServings());
        
        return summary;
    }

    /**
     * Clase interna para resumen nutricional
     */
    public static class RecipeNutritionSummary {
        private String recipeName;
        private Integer servings;
        private BigDecimal totalCalories = BigDecimal.ZERO;
        private BigDecimal totalProtein = BigDecimal.ZERO;
        private BigDecimal totalCarbs = BigDecimal.ZERO;
        private BigDecimal totalFat = BigDecimal.ZERO;
        
        // Getters y Setters
        public String getRecipeName() { return recipeName; }
        public void setRecipeName(String recipeName) { this.recipeName = recipeName; }
        
        public Integer getServings() { return servings; }
        public void setServings(Integer servings) { this.servings = servings; }
        
        public BigDecimal getTotalCalories() { return totalCalories; }
        public void setTotalCalories(BigDecimal totalCalories) { this.totalCalories = totalCalories; }
        
        public BigDecimal getTotalProtein() { return totalProtein; }
        public void setTotalProtein(BigDecimal totalProtein) { this.totalProtein = totalProtein; }
        
        public BigDecimal getTotalCarbs() { return totalCarbs; }
        public void setTotalCarbs(BigDecimal totalCarbs) { this.totalCarbs = totalCarbs; }
        
        public BigDecimal getTotalFat() { return totalFat; }
        public void setTotalFat(BigDecimal totalFat) { this.totalFat = totalFat; }
        
        public BigDecimal getCaloriesPerServing() {
            if (servings == null || servings == 0) return BigDecimal.ZERO;
            return totalCalories.divide(BigDecimal.valueOf(servings), 2, java.math.RoundingMode.HALF_UP);
        }
        
        public BigDecimal getProteinPerServing() {
            if (servings == null || servings == 0) return BigDecimal.ZERO;
            return totalProtein.divide(BigDecimal.valueOf(servings), 2, java.math.RoundingMode.HALF_UP);
        }
        
        @Override
        public String toString() {
            return String.format("""
                === %s ===
                Porciones: %d
                Calorías totales: %.0f kcal
                Calorías por porción: %.0f kcal
                Proteínas: %.1f g (%.1f g/porción)
                """,
                recipeName, servings, totalCalories, getCaloriesPerServing(),
                totalProtein, getProteinPerServing());
        }
    }
}