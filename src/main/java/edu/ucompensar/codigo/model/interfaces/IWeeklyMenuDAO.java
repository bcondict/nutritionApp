package edu.ucompensar.codigo.model.interfaces;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.entity.WeeklyMenu;

public interface IWeeklyMenuDAO {
    // CRUD básico
    void save(WeeklyMenu weeklyMenu);
    WeeklyMenu findById(UUID id);
    List<WeeklyMenu> findAll();
    void update(WeeklyMenu weeklyMenu);
    void delete(UUID id);

    // Métodos específicos de búsqueda por plan nutricional
    List<WeeklyMenu> findByNutritionPlanId(UUID nutritionPlanId);
    List<WeeklyMenu> findByNutritionPlanIdOrderByWeekStart(UUID nutritionPlanId);
    WeeklyMenu findCurrentWeekMenuByNutritionPlanId(UUID nutritionPlanId);
    List<WeeklyMenu> findByNutritionPlanIdAndDateRange(UUID nutritionPlanId, LocalDate startDate, LocalDate endDate);

    // Métodos de búsqueda por fechas
    List<WeeklyMenu> findByWeekStart(LocalDate weekStart);
    List<WeeklyMenu> findByWeekEnd(LocalDate weekEnd);
    List<WeeklyMenu> findByWeekStartBetween(LocalDate startDate, LocalDate endDate);
    List<WeeklyMenu> findCurrentWeekMenus();
    List<WeeklyMenu> findFutureWeekMenus();
    List<WeeklyMenu> findPastWeekMenus();

    // Métodos de utilidad
    boolean existsById(UUID id);
    boolean existsByNutritionPlanIdAndWeekStart(UUID nutritionPlanId, LocalDate weekStart);
    boolean existsByNutritionPlanIdAndDateRange(UUID nutritionPlanId, LocalDate startDate, LocalDate endDate);
    int countByNutritionPlanId(UUID nutritionPlanId);
    int countByNutritionPlanIdAndYear(UUID nutritionPlanId, int year);

    // Métodos de eliminación
    void deleteByNutritionPlanId(UUID nutritionPlanId);
    void deleteOldMenus(LocalDate beforeDate);
    void deleteMenusByDateRange(LocalDate startDate, LocalDate endDate);

    // Métodos de validación
    void saveWithValidation(WeeklyMenu weeklyMenu);
    void updateWithValidation(WeeklyMenu weeklyMenu);

    // Métodos de generación y utilidad
    WeeklyMenu generateWeeklyMenuForDate(UUID nutritionPlanId, LocalDate date);
    List<WeeklyMenu> generateAllWeeklyMenusForPlan(UUID nutritionPlanId, LocalDate startDate, int numberOfWeeks);
    boolean hasOverlappingWeeks(WeeklyMenu weeklyMenu);

    // Métodos de reportes
    List<WeeklyMenu> findAllOrderByWeekStartDesc();
    List<WeeklyMenu> findMenusGeneratedAfter(LocalDateTime date);
    List<WeeklyMenu> findMenusGeneratedBefore(LocalDateTime date);
}