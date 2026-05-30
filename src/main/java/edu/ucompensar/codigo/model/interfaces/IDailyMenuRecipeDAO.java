package edu.ucompensar.codigo.model.interfaces;

import edu.ucompensar.codigo.entity.DailyMenuRecipe;
import edu.ucompensar.codigo.model.enums.DailyMenuSlot;

import java.util.List;
import java.util.UUID;

public interface IDailyMenuRecipeDAO {
    void save(DailyMenuRecipe dailyMenuRecipe);
    DailyMenuRecipe findById(UUID id);
    List<DailyMenuRecipe> findAll();
    void update(DailyMenuRecipe dailyMenuRecipe);
    void delete(UUID id);
    
    List<DailyMenuRecipe> findByDailyMenuId(UUID dailyMenuId);
    List<DailyMenuRecipe> findByRecipeId(UUID recipeId);
    List<DailyMenuRecipe> findByDailyMenuIdAndSlot(UUID dailyMenuId, DailyMenuSlot slot);
    List<DailyMenuRecipe> findBySlot(DailyMenuSlot slot);
    boolean existsByDailyMenuIdAndRecipeId(UUID dailyMenuId, UUID recipeId);
    void deleteByDailyMenuId(UUID dailyMenuId);
    void deleteByRecipeId(UUID recipeId);
    int countByDailyMenuId(UUID dailyMenuId);
}