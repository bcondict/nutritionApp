package edu.ucompensar.codigo.service;

import edu.ucompensar.codigo.DAO.WeeklyMenuDAO;
import edu.ucompensar.codigo.entity.WeeklyMenu;
import edu.ucompensar.codigo.model.interfaces.IWeeklyMenuDAO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class WeeklyMenuService {
    private final IWeeklyMenuDAO weeklyMenuDAO;

    public WeeklyMenuService() {
        this.weeklyMenuDAO = new WeeklyMenuDAO();
    }

    public void save(WeeklyMenu menu) {
        weeklyMenuDAO.save(menu);
    }

    public WeeklyMenu findById(UUID id) {
        return weeklyMenuDAO.findById(id);
    }

    public List<WeeklyMenu> findByNutritionPlanId(UUID nutritionPlanId) {
        return weeklyMenuDAO.findByNutritionPlanId(nutritionPlanId);
    }

    public WeeklyMenu findCurrentWeekMenu(UUID nutritionPlanId) {
        return weeklyMenuDAO.findCurrentWeekMenuByNutritionPlanId(nutritionPlanId);
    }

    public List<WeeklyMenu> findMenuByDate(UUID nutritionPlanId, LocalDate date) {
        return weeklyMenuDAO.findByNutritionPlanId(nutritionPlanId );
    }

    public void update(WeeklyMenu menu) {
        weeklyMenuDAO.update(menu);
    }

    public void delete(UUID id) {
        weeklyMenuDAO.delete(id);
    }

    public void generateWeeklyMenus(UUID nutritionPlanId, LocalDate startDate, int numberOfWeeks) {
        LocalDate currentDate = startDate;
        
        for (int i = 0; i < numberOfWeeks; i++) {
            LocalDate weekStart = currentDate;
            while (weekStart.getDayOfWeek().getValue() != 1) {
                weekStart = weekStart.minusDays(1);
            }
            LocalDate weekEnd = weekStart.plusDays(6);
            
            // Verificar si ya existe
            if (!weeklyMenuDAO.existsByNutritionPlanIdAndWeekStart(nutritionPlanId, weekStart)) {
                UUID id = UUID.randomUUID();
                LocalDateTime now = LocalDateTime.now();
                WeeklyMenu menu = new WeeklyMenu(
                    id,
                    nutritionPlanId,
                    weekStart,
                    weekEnd,
                    LocalDateTime.now(),
                    now,
                    now
                );
                save(menu);
            }
            
            currentDate = currentDate.plusWeeks(1);
        }
    }
}