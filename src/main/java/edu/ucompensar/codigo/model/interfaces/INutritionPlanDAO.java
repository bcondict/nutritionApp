package edu.ucompensar.codigo.model.interfaces;

import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.entity.NutritionPlan;

public interface INutritionPlanDAO {
    void save(NutritionPlan nutritionPlan);
    NutritionPlan findById(UUID id);
    NutritionPlan findByUserId(UUID userId);
    List<NutritionPlan> findAll();
    void update(NutritionPlan nutritionPlan);
    void delete(UUID id);

    public List<NutritionPlan> findPlansByUserId(UUID userId);
    public List<NutritionPlan> findByGoalId(UUID goalId);
}
