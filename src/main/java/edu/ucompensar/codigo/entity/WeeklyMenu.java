package edu.ucompensar.codigo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class WeeklyMenu {
    private UUID id;
    private UUID nutritionPlanId;
    private LocalDate weekStart;
    private LocalDate weekEnd;
    private LocalDateTime generatedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    // Constructor completo
    public WeeklyMenu(
        UUID id,
        UUID nutritionPlanId,
        LocalDate weekStart,
        LocalDate weekEnd,
        LocalDateTime generatedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        this.id = id;
        this.nutritionPlanId = nutritionPlanId;
        this.weekStart = weekStart;
        this.weekEnd = weekEnd;
        this.generatedAt = generatedAt;
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

    public UUID getNutritionPlanId() {
        return nutritionPlanId;
    }

    public void setNutritionPlanId(UUID nutritionPlanId) {
        this.nutritionPlanId = nutritionPlanId;
    }

    public LocalDate getWeekStart() {
        return weekStart;
    }

    public void setWeekStart(LocalDate weekStart) {
        this.weekStart = weekStart;
    }

    public LocalDate getWeekEnd() {
        return weekEnd;
    }

    public void setWeekEnd(LocalDate weekEnd) {
        this.weekEnd = weekEnd;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Métodos auxiliares
    public boolean isValidWeekRange() {
        if (weekStart == null || weekEnd == null) {
            return false;
        }
        return !weekEnd.isBefore(weekStart);
    }

    public boolean isCurrentWeek() {
        if (weekStart == null || weekEnd == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        return ( today.isEqual(weekStart) || today.isAfter(weekStart)) && ( today.isEqual(weekEnd) || today.isBefore(weekEnd));
    }

    public boolean isFutureWeek() {
        if (weekStart == null) {
            return false;
        }
        return weekStart.isAfter(LocalDate.now());
    }

    public boolean isPastWeek() {
        if (weekEnd == null) {
            return false;
        }
        return weekEnd.isBefore(LocalDate.now());
    }

    public int getWeekDurationInDays() {
        if (weekStart == null || weekEnd == null) {
            return 0;
        }
        return (int) (weekEnd.toEpochDay() - weekStart.toEpochDay()) + 1;
    }

    public boolean isSevenDayWeek() {
        return getWeekDurationInDays() == 7;
    }

    @Override
    public String toString() {
        return "WeeklyMenu{" +
                "id=" + id +
                ", nutritionPlanId=" + nutritionPlanId +
                ", weekStart=" + weekStart +
                ", weekEnd=" + weekEnd +
                ", generatedAt=" + generatedAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeeklyMenu that = (WeeklyMenu) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}