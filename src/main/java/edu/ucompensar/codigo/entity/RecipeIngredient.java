package edu.ucompensar.codigo.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

public class RecipeIngredient {
    private UUID id;
    private UUID recipeId;
    private UUID foodItemId;
    private BigDecimal quantityG;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    // Constructor completo
    public RecipeIngredient(
        UUID id,
        UUID recipeId,
        UUID foodItemId,
        BigDecimal quantityG,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        this.id = id;
        this.recipeId = recipeId;
        this.foodItemId = foodItemId;
        this.quantityG = quantityG;
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

    public UUID getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(UUID recipeId) {
        this.recipeId = recipeId;
    }

    public UUID getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(UUID foodItemId) {
        this.foodItemId = foodItemId;
    }

    public BigDecimal getQuantityG() {
        return quantityG;
    }

    public void setQuantityG(BigDecimal quantityG) {
        this.quantityG = quantityG;
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
    public boolean isValidQuantity() {
        return quantityG != null && quantityG.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean hasValidReferences() {
        return recipeId != null && foodItemId != null;
    }

    public BigDecimal getQuantityInKg() {
        if (quantityG == null) return BigDecimal.ZERO;
        return quantityG.divide(new BigDecimal("1000"), 3, RoundingMode.HALF_UP);
    }

    public BigDecimal getQuantityInOz() {
        if (quantityG == null) return BigDecimal.ZERO;
        // 1 gramo = 0.035274 onzas
        return quantityG.multiply(new BigDecimal("0.035274")).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getQuantityInLb() {
        if (quantityG == null) return BigDecimal.ZERO;
        // 1 gramo = 0.00220462 libras
        return quantityG.multiply(new BigDecimal("0.00220462")).setScale(3, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return "RecipeIngredient{" +
                "id=" + id +
                ", recipeId=" + recipeId +
                ", foodItemId=" + foodItemId +
                ", quantityG=" + quantityG +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredient that = (RecipeIngredient) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}