package edu.ucompensar.codigo.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class FoodItemDietaryTag {
    private UUID id;
    private UUID foodItemId;
    private UUID dietaryPreferenceId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    // Constructor completo
    public FoodItemDietaryTag(
        UUID id,
        UUID foodItemId,
        UUID dietaryPreferenceId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        this.id = id;
        this.foodItemId = foodItemId;
        this.dietaryPreferenceId = dietaryPreferenceId;
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

    public UUID getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(UUID foodItemId) {
        this.foodItemId = foodItemId;
    }

    public UUID getDietaryPreferenceId() {
        return dietaryPreferenceId;
    }

    public void setDietaryPreferenceId(UUID dietaryPreferenceId) {
        this.dietaryPreferenceId = dietaryPreferenceId;
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
    public boolean hasValidReferences() {
        return foodItemId != null && dietaryPreferenceId != null;
    }

    @Override
    public String toString() {
        return "FoodItemDietaryTag{" +
                "id=" + id +
                ", foodItemId=" + foodItemId +
                ", dietaryPreferenceId=" + dietaryPreferenceId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodItemDietaryTag that = (FoodItemDietaryTag) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}