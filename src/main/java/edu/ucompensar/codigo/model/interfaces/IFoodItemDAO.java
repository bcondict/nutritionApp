package edu.ucompensar.codigo.model.interfaces;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.entity.FoodItem;
import edu.ucompensar.codigo.model.enums.FoodCategory;

public interface IFoodItemDAO {
    // CRUD básico
    void save(FoodItem foodItem);
    FoodItem findById(UUID id);
    List<FoodItem> findAll();
    void update(FoodItem foodItem);
    void delete(UUID id);
    
    // Métodos de búsqueda por nombre
    List<FoodItem> findByNameContaining(String name);
    List<FoodItem> findByNameExact(String name);
    
    // Métodos de búsqueda por categoría
    List<FoodItem> findByCategory(FoodCategory category);
    List<FoodItem> findByCategoryOrderByName(FoodCategory category);
    List<String> findAllCategories();

    // Métodos de búsqueda por rango nutricional
    List<FoodItem> findByCaloriesRange(BigDecimal minCalories, BigDecimal maxCalories);
    List<FoodItem> findByProteinRange(BigDecimal minProtein, BigDecimal maxProtein);
    List<FoodItem> findByCarbsRange(BigDecimal minCarbs, BigDecimal maxCarbs);
    List<FoodItem> findByFatRange(BigDecimal minFat, BigDecimal maxFat);
    List<FoodItem> findByFiberRange(BigDecimal minFiber, BigDecimal maxFiber);
    List<FoodItem> findBySodiumRange(BigDecimal minSodium, BigDecimal maxSodium);

    // Métodos de búsqueda avanzada
    List<FoodItem> searchFoodItems(String keyword, FoodCategory category, BigDecimal maxCalories, BigDecimal minProtein);
    List<FoodItem> findHighProteinFoods(BigDecimal minProtein);
    List<FoodItem> findLowCarbFoods(BigDecimal maxCarbs);
    List<FoodItem> findLowFatFoods(BigDecimal maxFat);
    List<FoodItem> findHighFiberFoods(BigDecimal minFiber);
    List<FoodItem> findLowSodiumFoods(BigDecimal maxSodium);

    // Métodos de utilidad
    boolean existsById(UUID id);
    boolean existsByName(String name);
    int countAll();
    int countByCategory(FoodCategory category);
    BigDecimal getAverageCalories();
    BigDecimal getAverageProtein();
    BigDecimal getAverageCarbs();
    BigDecimal getAverageFat();

    // Métodos de eliminación
    void deleteByName(String name);
    void deleteByCategory(FoodCategory category);

    // Métodos de validación
    void saveWithValidation(FoodItem foodItem);
    void updateWithValidation(FoodItem foodItem);

    // Métodos de reportes
    List<FoodItem> findAllOrderByName();
    List<FoodItem> findAllOrderByCaloriesDesc();
    List<FoodItem> findAllOrderByProteinDesc();
    List<FoodItem> findMostCaloricFoods(int limit);
    List<FoodItem> findLeastCaloricFoods(int limit);
    List<FoodItem> findFoodsCreatedAfter(LocalDateTime date);
    List<FoodItem> findFoodsCreatedBefore(LocalDateTime date);
}