package edu.ucompensar.codigo.service;

// import java.math.BigDecimal;
// import java.math.RoundingMode;
// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.UUID;

// import edu.ucompensar.codigo.DAO.NutritionPlanDAO;
// import edu.ucompensar.codigo.model.interfaces.INutritionPlanDAO;

// public class NutritionPlanService {
//     private static NutritionPlanService instance;
//     private final INutritionPlanDAO nutritionPlanDAO;
//     private final UserProfileService profileService;

//     private NutritionPlanService() {
//         this.nutritionPlanDAO = new NutritionPlanDAO();
//         this.profileService = UserProfileService.getInstance();
//     }

//     public static NutritionPlanService getInstance() {
//         if (instance == null) {
//             instance = new NutritionPlanService();
//         }
//         return instance;
//     }

//     public void save(NutritionPlan plan) {
//         nutritionPlanDAO.save(plan);
//     }

//     public NutritionPlan findById(UUID id) {
//         return nutritionPlanDAO.findById(id);
//     }

//     public NutritionPlan findByUserId(UUID userId) {
//         return nutritionPlanDAO.findByUserId(userId);
//     }

//     public List<NutritionPlan> findAll() {
//         return nutritionPlanDAO.findAll();
//     }

//     public void update(NutritionPlan plan) {
//         nutritionPlanDAO.update(plan);
//     }

//     public void delete(UUID id) {
//         nutritionPlanDAO.delete(id);
//     }

//     public boolean hasActivePlan(UUID userId) {
//         NutritionPlan plan = findByUserId(userId);
//         return plan != null && plan.isActive();
//     }

//     public NutritionPlan createPlanForUser(UUID userId, GoalType goalType) {
//         UserProfile profile = profileService.getLatestProfile(userId);
//         if (profile == null) {
//             throw new IllegalStateException("El usuario debe completar su perfil antes de crear un plan nutricional");
//         }

//         BigDecimal maintenanceCalories = calculateMaintenanceCalories(profile);
//         BigDecimal targetCalories = adjustCaloriesForGoal(maintenanceCalories, goalType);
//         BigDecimal[] macros = calculateMacros(goalType, targetCalories);

//         NutritionPlan plan = new NutritionPlan();
//         plan.setUserId(userId);
//         plan.setTargetCalories(targetCalories);
//         plan.setTargetProteinPct(macros[0]);
//         plan.setTargetCarbsPct(macros[1]);
//         plan.setTargetFatPct(macros[2]);
//         plan.setActive(true);
//         plan.setGeneratedAt(LocalDateTime.now());

//         // Desactivar planes anteriores
//         deactivateAllUserPlans(userId);
        
//         save(plan);
//         return plan;
//     }

//     private BigDecimal calculateMaintenanceCalories(UserProfile profile) {
//         BigDecimal bmr = calculateBMR(profile);
//         ActivityLevel level = ActivityLevel.valueOf(profile.getActivityLevel());
//         return bmr.multiply(level.getMultiplier()).setScale(0, RoundingMode.HALF_UP);
//     }

//     private BigDecimal calculateBMR(UserProfile profile) {
//         BigDecimal weight = profile.getWeightKg();
//         BigDecimal height = new BigDecimal(profile.getHeightCm());
        
//         if (profile.getSex().equals("MALE")) {
//             // Fórmula de Mifflin-St Jeor para hombres
//             return new BigDecimal("88.362")
//                 .add(weight.multiply(new BigDecimal("13.397")))
//                 .add(height.multiply(new BigDecimal("4.799")))
//                 .setScale(0, RoundingMode.HALF_UP);
//         } else {
//             // Fórmula de Mifflin-St Jeor para mujeres
//             return new BigDecimal("447.593")
//                 .add(weight.multiply(new BigDecimal("9.247")))
//                 .add(height.multiply(new BigDecimal("3.098")))
//                 .setScale(0, RoundingMode.HALF_UP);
//         }
//     }

//     private BigDecimal adjustCaloriesForGoal(BigDecimal maintenance, GoalType goalType) {
//         switch (goalType) {
//             case WEIGHT_LOSS:
//                 return maintenance.multiply(new BigDecimal("0.85")).setScale(0, RoundingMode.HALF_UP);
//             case WEIGHT_GAIN:
//                 return maintenance.multiply(new BigDecimal("1.15")).setScale(0, RoundingMode.HALF_UP);
//             case MUSCLE_GAIN:
//                 return maintenance.multiply(new BigDecimal("1.10")).setScale(0, RoundingMode.HALF_UP);
//             default:
//                 return maintenance;
//         }
//     }

//     private BigDecimal[] calculateMacros(GoalType goalType, BigDecimal calories) {
//         BigDecimal proteinPct, carbsPct, fatPct;
        
//         switch (goalType) {
//             case WEIGHT_LOSS:
//                 proteinPct = new BigDecimal("35");
//                 carbsPct = new BigDecimal("35");
//                 fatPct = new BigDecimal("30");
//                 break;
//             case MUSCLE_GAIN:
//                 proteinPct = new BigDecimal("35");
//                 carbsPct = new BigDecimal("40");
//                 fatPct = new BigDecimal("25");
//                 break;
//             case WEIGHT_GAIN:
//                 proteinPct = new BigDecimal("30");
//                 carbsPct = new BigDecimal("45");
//                 fatPct = new BigDecimal("25");
//                 break;
//             default:
//                 proteinPct = new BigDecimal("30");
//                 carbsPct = new BigDecimal("40");
//                 fatPct = new BigDecimal("30");
//         }
        
//         return new BigDecimal[]{proteinPct, carbsPct, fatPct};
//     }

//     private void deactivateAllUserPlans(UUID userId) {
//         NutritionPlan currentPlan = findByUserId(userId);
//         if (currentPlan != null) {
//             currentPlan.setActive(false);
//             update(currentPlan);
//         }
//     }
// }

import edu.ucompensar.codigo.DAO.NutritionPlanDAO;
import edu.ucompensar.codigo.entity.NutritionPlan;
import edu.ucompensar.codigo.entity.UserProfile;
import edu.ucompensar.codigo.model.enums.ActivityLevel;
import edu.ucompensar.codigo.model.enums.GoalType;
import edu.ucompensar.codigo.model.enums.Sex;
import edu.ucompensar.codigo.model.interfaces.INutritionPlanDAO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public class NutritionPlanService {
    private final INutritionPlanDAO nutritionPlanDAO;
    private final UserProfileService profileService;

    public NutritionPlanService() {
        this.nutritionPlanDAO = new NutritionPlanDAO();
        this.profileService = new UserProfileService();
    }

    public void save(NutritionPlan plan) {
        nutritionPlanDAO.save(plan);
    }

    public NutritionPlan findById(UUID id) {
        return nutritionPlanDAO.findById(id);
    }

    public NutritionPlan findByUserId(UUID userId) {
        return nutritionPlanDAO.findByUserId(userId);
    }

    public void update(NutritionPlan plan) {
        nutritionPlanDAO.update(plan);
    }

    public boolean hasActivePlan(UUID userId) {
        NutritionPlan plan = findByUserId(userId);
        return plan != null && plan.isActive();
    }

    public NutritionPlan createPlanForUser(UUID userId, String goalTypeCode) {
        UserProfile profile = profileService.getLatestProfile(userId);
        if (profile == null) {
            throw new IllegalStateException("Debes completar tu perfil primero");
        }

        GoalType goalType = GoalType.valueOf(goalTypeCode);
        BigDecimal maintenanceCalories = calculateMaintenanceCalories(profile);
        BigDecimal targetCalories = adjustCaloriesForGoal(maintenanceCalories, goalType);
        BigDecimal[] macros = calculateMacros(goalType, targetCalories);

        NutritionPlan plan = new NutritionPlan();
        plan.setUserId(userId);
        plan.setTargetCalories(targetCalories);
        plan.setTargetProteinPct(macros[0]);
        plan.setTargetCarbsPct(macros[1]);
        plan.setTargetFatPct(macros[2]);
        plan.setActive(true);
        plan.setGeneratedAt(Timestamp.valueOf(LocalDateTime.now()));

        // Desactivar plan anterior si existe
        NutritionPlan existing = findByUserId(userId);
        if (existing != null) {
            existing.setActive(false);
            update(existing);
        }
        
        save(plan);
        return plan;
    }

    private BigDecimal calculateMaintenanceCalories(UserProfile profile) {
        BigDecimal bmr = calculateBMR(profile);
        // Obtener el multiplicador según el nivel de actividad
        BigDecimal multiplier = getActivityMultiplier(profile.getActivityLevel());
        return bmr.multiply(multiplier).setScale(0, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateBMR(UserProfile profile) {
        BigDecimal weight = profile.getWeightKg();
        BigDecimal height = new BigDecimal(profile.getHeightCm());
        
        if (Sex.MALE == profile.getSex()) {
            // Mifflin-St Jeor para hombres
            return new BigDecimal("88.362")
                .add(weight.multiply(new BigDecimal("13.397")))
                .add(height.multiply(new BigDecimal("4.799")))
                .setScale(0, RoundingMode.HALF_UP);
        } else {
            // Mifflin-St Jeor para mujeres
            return new BigDecimal("447.593")
                .add(weight.multiply(new BigDecimal("9.247")))
                .add(height.multiply(new BigDecimal("3.098")))
                .setScale(0, RoundingMode.HALF_UP);
        }
    }

    private BigDecimal getActivityMultiplier(ActivityLevel activityLevel) {
        switch (activityLevel) {
            case SEDENTARY: return new BigDecimal("1.2");
            case LIGHT: return new BigDecimal("1.375");
            case MODERATE: return new BigDecimal("1.55");
            case ACTIVE: return new BigDecimal("1.725");
            case VERY_ACTIVE: return new BigDecimal("1.9");
            default: return new BigDecimal("1.2");
        }
    }

    private BigDecimal adjustCaloriesForGoal(BigDecimal maintenance, GoalType goalType) {
        switch (goalType) {
            case LOSE_WEIGHT:
                return maintenance.multiply(new BigDecimal("0.85")).setScale(0, RoundingMode.HALF_UP);
            case MAINTAIN_WEIGHT:
                return maintenance.multiply(new BigDecimal("1.15")).setScale(0, RoundingMode.HALF_UP);
            case GAIN_MUSCLE:
                return maintenance.multiply(new BigDecimal("1.10")).setScale(0, RoundingMode.HALF_UP);
            default:
                return maintenance;
        }
    }

    private BigDecimal[] calculateMacros(GoalType goalType, BigDecimal calories) {
        BigDecimal proteinPct, carbsPct, fatPct;
        
        switch (goalType) {
            case LOSE_WEIGHT:
                proteinPct = new BigDecimal("35");
                carbsPct = new BigDecimal("35");
                fatPct = new BigDecimal("30");
                break;
            case GAIN_MUSCLE:
                proteinPct = new BigDecimal("35");
                carbsPct = new BigDecimal("40");
                fatPct = new BigDecimal("25");
                break;
            case MAINTAIN_WEIGHT:
                proteinPct = new BigDecimal("30");
                carbsPct = new BigDecimal("40");
                fatPct = new BigDecimal("30");
                break;
            default:
                proteinPct = new BigDecimal("30");
                carbsPct = new BigDecimal("40");
                fatPct = new BigDecimal("30");
        }
        
        return new BigDecimal[]{proteinPct, carbsPct, fatPct};
    }
}