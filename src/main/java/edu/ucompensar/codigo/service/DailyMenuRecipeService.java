package edu.ucompensar.codigo.service;

import edu.ucompensar.codigo.DAO.DailyMenuRecipeDAO;
import edu.ucompensar.codigo.entity.DailyMenuRecipe;
import edu.ucompensar.codigo.model.enums.DailyMenuSlot;
import edu.ucompensar.codigo.model.interfaces.IDailyMenuRecipeDAO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class DailyMenuRecipeService {
    private final IDailyMenuRecipeDAO dailyMenuRecipeDAO;

    public DailyMenuRecipeService() {
        this.dailyMenuRecipeDAO = new DailyMenuRecipeDAO();
    }

    public void save(DailyMenuRecipe dailyMenuRecipe) {
        dailyMenuRecipeDAO.save(dailyMenuRecipe);
    }

    public DailyMenuRecipe findById(UUID id) {
        return dailyMenuRecipeDAO.findById(id);
    }

    public List<DailyMenuRecipe> findAll() {
        return dailyMenuRecipeDAO.findAll();
    }

    public void update(DailyMenuRecipe dailyMenuRecipe) {
        dailyMenuRecipeDAO.update(dailyMenuRecipe);
    }

    public void delete(UUID id) {
        dailyMenuRecipeDAO.delete(id);
    }

    public List<DailyMenuRecipe> findByDailyMenuId(UUID dailyMenuId) {
        return dailyMenuRecipeDAO.findByDailyMenuId(dailyMenuId);
    }

    public List<DailyMenuRecipe> findByRecipeId(UUID recipeId) {
        return dailyMenuRecipeDAO.findByRecipeId(recipeId);
    }

    public List<DailyMenuRecipe> findByDailyMenuIdAndSlot(UUID dailyMenuId, DailyMenuSlot slot) {
        return dailyMenuRecipeDAO.findByDailyMenuIdAndSlot(dailyMenuId, slot);
    }

    public List<DailyMenuRecipe> findBySlot(DailyMenuSlot slot) {
        return dailyMenuRecipeDAO.findBySlot(slot);
    }

    public boolean existsByDailyMenuIdAndRecipeId(UUID dailyMenuId, UUID recipeId) {
        return dailyMenuRecipeDAO.existsByDailyMenuIdAndRecipeId(dailyMenuId, recipeId);
    }

    public void deleteByDailyMenuId(UUID dailyMenuId) {
        dailyMenuRecipeDAO.deleteByDailyMenuId(dailyMenuId);
    }

    public void deleteByRecipeId(UUID recipeId) {
        dailyMenuRecipeDAO.deleteByRecipeId(recipeId);
    }

    public int countByDailyMenuId(UUID dailyMenuId) {
        return dailyMenuRecipeDAO.countByDailyMenuId(dailyMenuId);
    }

    public void addRecipeToDailyMenu(UUID dailyMenuId, UUID recipeId, String slot) {
        if (!existsByDailyMenuIdAndRecipeId(dailyMenuId, recipeId)) {
            LocalDateTime now = LocalDateTime.now();
            DailyMenuRecipe dmr = new DailyMenuRecipe(
                UUID.randomUUID(),
                dailyMenuId,
                recipeId,
                DailyMenuSlot.valueOf(slot),
                now,
                now
            );
            save(dmr);
        }
    }

    public void addRecipesToDailyMenu(UUID dailyMenuId, List<UUID> recipeIds, String slot) {
        for (UUID recipeId : recipeIds) {
            addRecipeToDailyMenu(dailyMenuId, recipeId, slot);
        }
    }

    public void removeRecipeFromDailyMenu(UUID dailyMenuId, UUID recipeId) {
        List<DailyMenuRecipe> items = findByDailyMenuId(dailyMenuId);
        for (DailyMenuRecipe item : items) {
            if (item.getRecipeId().equals(recipeId)) {
                delete(item.getId());
                break;
            }
        }
    }
}