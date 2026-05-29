package edu.ucompensar.codigo.service;

import edu.ucompensar.codigo.DAO.FoodItemDAO;
import edu.ucompensar.codigo.entity.FoodItem;
import edu.ucompensar.codigo.model.enums.FoodCategory;
import edu.ucompensar.codigo.model.interfaces.IFoodItemDAO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class FoodItemService {
    private final IFoodItemDAO foodItemDAO;

    public FoodItemService() {
        this.foodItemDAO = new FoodItemDAO();
    }

    // ==================== CRUD BÁSICO ====================
    
    public void save(FoodItem foodItem) {
        foodItemDAO.save(foodItem);
    }

    public FoodItem findById(UUID id) {
        return foodItemDAO.findById(id);
    }

    public List<FoodItem> findAll() {
        return foodItemDAO.findAll();
    }

    public void update(FoodItem foodItem) {
        foodItemDAO.update(foodItem);
    }

    public void delete(UUID id) {
        foodItemDAO.delete(id);
    }

    // ==================== BÚSQUEDA ====================
    
    public List<FoodItem> findByNameContaining(String name) {
        return foodItemDAO.findByNameContaining(name);
    }

    public List<FoodItem> findByCategory(FoodCategory category) {
        return foodItemDAO.findByCategory(category);
    }

    public List<String> findAllCategories() {
        return foodItemDAO.findAllCategories();
    }

    public List<FoodItem> findByCaloriesRange(BigDecimal minCalories, BigDecimal maxCalories) {
        return foodItemDAO.findByCaloriesRange(minCalories, maxCalories);
    }

    public List<FoodItem> findHighProteinFoods(BigDecimal minProtein) {
        return foodItemDAO.findHighProteinFoods(minProtein);
    }

    public List<FoodItem> findLowCarbFoods(BigDecimal maxCarbs) {
        return foodItemDAO.findLowCarbFoods(maxCarbs);
    }

    public List<FoodItem> findLowFatFoods(BigDecimal maxFat) {
        return foodItemDAO.findLowFatFoods(maxFat);
    }

    public List<FoodItem> searchFoodItems(String keyword, FoodCategory category, BigDecimal maxCalories, BigDecimal minProtein) {
        return foodItemDAO.searchFoodItems(keyword, category, maxCalories, minProtein);
    }

    // ==================== MÉTODOS DE UTILIDAD ====================
    
    public boolean existsById(UUID id) {
        return foodItemDAO.existsById(id);
    }

    public boolean existsByName(String name) {
        return foodItemDAO.existsByName(name);
    }

    public int countAll() {
        return foodItemDAO.countAll();
    }

    public int countByCategory(FoodCategory category) {
        return foodItemDAO.countByCategory(category);
    }

    public BigDecimal getAverageCalories() {
        return foodItemDAO.getAverageCalories();
    }

    // ==================== VALIDACIÓN ====================
    
    public void saveWithValidation(FoodItem foodItem) {
        foodItemDAO.saveWithValidation(foodItem);
    }

    public void updateWithValidation(FoodItem foodItem) {
        foodItemDAO.updateWithValidation(foodItem);
    }

    // ==================== REPORTES ====================
    
    public List<FoodItem> findAllOrderByName() {
        return foodItemDAO.findAllOrderByName();
    }

    public List<FoodItem> findAllOrderByCaloriesDesc() {
        return foodItemDAO.findAllOrderByCaloriesDesc();
    }

    public List<FoodItem> findMostCaloricFoods(int limit) {
        return foodItemDAO.findMostCaloricFoods(limit);
    }

    public List<FoodItem> findLeastCaloricFoods(int limit) {
        return foodItemDAO.findLeastCaloricFoods(limit);
    }
}