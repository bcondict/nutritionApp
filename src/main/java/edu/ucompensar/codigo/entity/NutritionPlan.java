package edu.ucompensar.codigo.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

public class NutritionPlan {
    private UUID id;
    private UUID userId;
    private UUID goalId;
    private BigDecimal targetCalories;
    private BigDecimal targetProteinPct;
    private BigDecimal targetCarbsPct;
    private BigDecimal targetFatPct;
    private Timestamp generatedAt;
    private boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Constructor vacío (requerido por frameworks como Spring, Hibernate)
    public NutritionPlan() {
    }

    // Constructor con parámetros principales (sin campos autogenerados)
    public NutritionPlan(
        UUID userId,
        UUID goalId,
        BigDecimal targetCalories,
        BigDecimal targetProteinPct,
        BigDecimal targetCarbsPct, 
        BigDecimal targetFatPct,
        boolean isActive
    ) {
        this.userId = userId;
        this.goalId = goalId;
        this.targetCalories = targetCalories;
        this.targetProteinPct = targetProteinPct;
        this.targetCarbsPct = targetCarbsPct;
        this.targetFatPct = targetFatPct;
        this.isActive = isActive;
    }

    // Constructor completo
    public NutritionPlan(
        UUID id,
        UUID userId,
        UUID goalId,
        BigDecimal targetCalories,
        BigDecimal targetProteinPct,
        BigDecimal targetCarbsPct,
        BigDecimal targetFatPct,
        Timestamp generatedAt,
        boolean isActive,
        Timestamp createdAt,
        Timestamp updatedAt
    ) {
        this.id = id;
        this.userId = userId;
        this.goalId = goalId;
        this.targetCalories = targetCalories;
        this.targetProteinPct = targetProteinPct;
        this.targetCarbsPct = targetCarbsPct;
        this.targetFatPct = targetFatPct;
        this.generatedAt = generatedAt;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getGoalId() {
        return goalId;
    }

    public void setGoalId(UUID goalId) {
        this.goalId = goalId;
    }

    public BigDecimal getTargetCalories() {
        return targetCalories;
    }

    public void setTargetCalories(BigDecimal targetCalories) {
        this.targetCalories = targetCalories;
    }

    public BigDecimal getTargetProteinPct() {
        return targetProteinPct;
    }

    public void setTargetProteinPct(BigDecimal targetProteinPct) {
        this.targetProteinPct = targetProteinPct;
    }

    public BigDecimal getTargetCarbsPct() {
        return targetCarbsPct;
    }

    public void setTargetCarbsPct(BigDecimal targetCarbsPct) {
        this.targetCarbsPct = targetCarbsPct;
    }

    public BigDecimal getTargetFatPct() {
        return targetFatPct;
    }

    public void setTargetFatPct(BigDecimal targetFatPct) {
        this.targetFatPct = targetFatPct;
    }

    public Timestamp getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Timestamp generatedAt) {
        this.generatedAt = generatedAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Método toString para fácil depuración
    @Override
    public String toString() {
        return "NutritionPlan{" +
                "id=" + id +
                ", userId=" + userId +
                ", goalId=" + goalId +
                ", targetCalories=" + targetCalories +
                ", targetProteinPct=" + targetProteinPct +
                ", targetCarbsPct=" + targetCarbsPct +
                ", targetFatPct=" + targetFatPct +
                ", generatedAt=" + generatedAt +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    // Método equals basado en el ID (clave primaria)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NutritionPlan that = (NutritionPlan) o;
        return id != null && id.equals(that.id);
    }

    // Método hashCode basado en el ID
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    // Método auxiliar para validar que los porcentajes sumen 100%
    public boolean isValidMacroDistribution() {
        if (targetProteinPct == null || targetCarbsPct == null || targetFatPct == null) {
            return false;
        }
        BigDecimal total = targetProteinPct.add(targetCarbsPct).add(targetFatPct);
        return total.compareTo(new BigDecimal("100")) == 0;
    }

    // Método auxiliar para obtener las calorías por macronutriente
    public BigDecimal getProteinCalories() {
        if (targetCalories == null || targetProteinPct == null) return BigDecimal.ZERO;
        return targetCalories.multiply(targetProteinPct).divide(new BigDecimal("100"));
    }

    public BigDecimal getCarbsCalories() {
        if (targetCalories == null || targetCarbsPct == null) return BigDecimal.ZERO;
        return targetCalories.multiply(targetCarbsPct).divide(new BigDecimal("100"));
    }

    public BigDecimal getFatCalories() {
        if (targetCalories == null || targetFatPct == null) return BigDecimal.ZERO;
        return targetCalories.multiply(targetFatPct).divide(new BigDecimal("100"));
    }
}