package edu.ucompensar.codigo.service;

import edu.ucompensar.codigo.DAO.WeeklyMenuDAO;
import edu.ucompensar.codigo.entity.DailyMenu;
import edu.ucompensar.codigo.entity.DailyMenuRecipe;
import edu.ucompensar.codigo.entity.Recipe;
import edu.ucompensar.codigo.entity.WeeklyMenu;
import edu.ucompensar.codigo.model.enums.DailyMenuSlot;
import edu.ucompensar.codigo.model.enums.MealType;
import edu.ucompensar.codigo.model.interfaces.IWeeklyMenuDAO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class WeeklyMenuService {
    private final IWeeklyMenuDAO weeklyMenuDAO;
    private final RecipeService recipeService;
    private final DailyMenuService dailyMenuService;
    private final DailyMenuRecipeService dailyMenuRecipeService;

    public WeeklyMenuService() {
        this.weeklyMenuDAO = new WeeklyMenuDAO();
        this.recipeService = new RecipeService();
        this.dailyMenuService = new DailyMenuService();
        this.dailyMenuRecipeService = new DailyMenuRecipeService();
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
        return weeklyMenuDAO.findByNutritionPlanId(nutritionPlanId);
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
                        now);
                save(menu);
                String[] days = { "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY" };

                List<Recipe> breakfasts = recipeService.findByMealType(MealType.BREAKFAST);
                List<Recipe> lunches = recipeService.findByMealType(MealType.LUNCH);
                List<Recipe> dinners = recipeService.findByMealType(MealType.DINNER);
                List<Recipe> snacks = recipeService.findByMealType(MealType.SNACK);

                Random random = new Random();

                for (String day : days) {
                    UUID dailyMenuId = UUID.randomUUID();
                    LocalDateTime nowDaily = LocalDateTime.now();
                    DailyMenu dailyMenu = new DailyMenu(dailyMenuId, menu.getId(), day, BigDecimal.ZERO, nowDaily, nowDaily);
                    dailyMenuService.save(dailyMenu);

                    Map<DailyMenuSlot, List<Recipe>> slotRecipes = new LinkedHashMap<>();
                    slotRecipes.put(DailyMenuSlot.BREAKFAST, breakfasts);
                    slotRecipes.put(DailyMenuSlot.LUNCH, lunches);
                    slotRecipes.put(DailyMenuSlot.DINNER, dinners);
                    slotRecipes.put(DailyMenuSlot.SNACK, snacks);

                    for (Map.Entry<DailyMenuSlot, List<Recipe>> entry : slotRecipes.entrySet()) {
                        List<Recipe> pool = entry.getValue();
                        if (pool.isEmpty())
                            continue;

                        Recipe selected = pool.get(random.nextInt(pool.size()));
                        UUID dmrId = UUID.randomUUID();
                        LocalDateTime nowDmr = LocalDateTime.now();
                        DailyMenuRecipe dmr = new DailyMenuRecipe(
                            dmrId,
                            dailyMenuId,
                            selected.getId(),
                            entry.getKey(),
                            nowDmr,
                            nowDmr
                        );
                        dailyMenuRecipeService.save(dmr);
                    }
                }
            }

            currentDate = currentDate.plusWeeks(1);
        }
    }
}