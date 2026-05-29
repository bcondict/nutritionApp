package edu.ucompensar.codigo.service;

import edu.ucompensar.codigo.DAO.RecipeIngredientDAO;
import edu.ucompensar.codigo.entity.RecipeIngredient;
import edu.ucompensar.codigo.entity.FoodItem;
import edu.ucompensar.codigo.model.interfaces.IRecipeIngredientDAO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

public class RecipeIngredientService {
    private final IRecipeIngredientDAO ingredientDAO;
    private final FoodItemService foodItemService;

    public RecipeIngredientService() {
        this.ingredientDAO = new RecipeIngredientDAO();
        this.foodItemService = new FoodItemService();
    }

    public void save(RecipeIngredient ingredient) {
        ingredientDAO.save(ingredient);
    }

    public List<RecipeIngredient> findByRecipeId(UUID recipeId) {
        return ingredientDAO.findByRecipeId(recipeId);
    }

    public List<RecipeIngredient> findByRecipeIdWithDetails(UUID recipeId) {
        return ingredientDAO.findByRecipeIdWithDetails(recipeId);
    }

    public void deleteByRecipeId(UUID recipeId) {
        ingredientDAO.deleteByRecipeId(recipeId);
    }

    public void saveAll(List<RecipeIngredient> ingredients) {
        ingredientDAO.saveAll(ingredients);
    }

    /**
     * Calcula el resumen nutricional completo de una receta
     */
    public RecipeService.RecipeNutritionSummary calculateRecipeNutrition(UUID recipeId) {
        List<RecipeIngredient> ingredients = findByRecipeIdWithDetails(recipeId);
        RecipeService.RecipeNutritionSummary summary = new RecipeService.RecipeNutritionSummary();
        
        BigDecimal totalCalories = BigDecimal.ZERO;
        BigDecimal totalProtein = BigDecimal.ZERO;
        BigDecimal totalCarbs = BigDecimal.ZERO;
        BigDecimal totalFat = BigDecimal.ZERO;
        
        for (RecipeIngredient ingredient : ingredients) {
            FoodItem foodItem = foodItemService.findById(ingredient.getFoodItemId());
            if (foodItem != null) {
                BigDecimal factor = ingredient.getQuantityG().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
                
                totalCalories = totalCalories.add(foodItem.getCaloriesPer100g().multiply(factor));
                totalProtein = totalProtein.add(foodItem.getProteinPer100g().multiply(factor));
                totalCarbs = totalCarbs.add(foodItem.getCarbsPer100g().multiply(factor));
                totalFat = totalFat.add(foodItem.getFatPer100g().multiply(factor));
            }
        }
        
        // Obtener información de la receta
        RecipeService recipeService = new RecipeService();
        edu.ucompensar.codigo.entity.Recipe recipe = recipeService.findById(recipeId);
        
        if (recipe != null) {
            summary.setRecipeName(recipe.getName());
            summary.setServings(recipe.getServings());
        }
        
        summary.setTotalCalories(totalCalories.setScale(0, RoundingMode.HALF_UP));
        summary.setTotalProtein(totalProtein.setScale(1, RoundingMode.HALF_UP));
        summary.setTotalCarbs(totalCarbs.setScale(1, RoundingMode.HALF_UP));
        summary.setTotalFat(totalFat.setScale(1, RoundingMode.HALF_UP));
        
        return summary;
    }
}