package edu.ucompensar.codigo.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

public class FoodItem {
    private UUID id;
    private String name;
    private String category;
    private BigDecimal caloriesPer100g;
    private BigDecimal proteinPer100g;
    private BigDecimal carbsPer100g;
    private BigDecimal fatPer100g;
    private BigDecimal fiberPer100g;
    private BigDecimal sodiumPer100mg;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constantes para categorías de alimentos
    // public static final String CATEGORY_FRUITS = "FRUITS";
    // public static final String CATEGORY_VEGETABLES = "VEGETABLES";
    // public static final String CATEGORY_GRAINS = "GRAINS";
    // public static final String CATEGORY_LEGUMES = "LEGUMES";
    // public static final String CATEGORY_MEAT = "MEAT";
    // public static final String CATEGORY_POULTRY = "POULTRY";
    // public static final String CATEGORY_FISH = "FISH";
    // public static final String CATEGORY_DAIRY = "DAIRY";
    // public static final String CATEGORY_EGGS = "EGGS";
    // public static final String CATEGORY_NUTS = "NUTS";
    // public static final String CATEGORY_SEEDS = "SEEDS";
    // public static final String CATEGORY_OILS = "OILS";
    // public static final String CATEGORY_BEVERAGES = "BEVERAGES";
    // public static final String CATEGORY_PROCESSED = "PROCESSED";
    // public static final String CATEGORY_OTHER = "OTHER";


    // Constructor completo
    public FoodItem(
        UUID id,
        String name,
        String category,
        BigDecimal caloriesPer100g,
        BigDecimal proteinPer100g,
        BigDecimal carbsPer100g,
        BigDecimal fatPer100g,
        BigDecimal fiberPer100g,
        BigDecimal sodiumPer100mg,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.caloriesPer100g = caloriesPer100g;
        this.proteinPer100g = proteinPer100g;
        this.carbsPer100g = carbsPer100g;
        this.fatPer100g = fatPer100g;
        this.fiberPer100g = fiberPer100g;
        this.sodiumPer100mg = sodiumPer100mg;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getCaloriesPer100g() {
        return caloriesPer100g;
    }

    public void setCaloriesPer100g(BigDecimal caloriesPer100g) {
        this.caloriesPer100g = caloriesPer100g;
    }

    public BigDecimal getProteinPer100g() {
        return proteinPer100g;
    }

    public void setProteinPer100g(BigDecimal proteinPer100g) {
        this.proteinPer100g = proteinPer100g;
    }

    public BigDecimal getCarbsPer100g() {
        return carbsPer100g;
    }

    public void setCarbsPer100g(BigDecimal carbsPer100g) {
        this.carbsPer100g = carbsPer100g;
    }

    public BigDecimal getFatPer100g() {
        return fatPer100g;
    }

    public void setFatPer100g(BigDecimal fatPer100g) {
        this.fatPer100g = fatPer100g;
    }

    public BigDecimal getFiberPer100g() {
        return fiberPer100g;
    }

    public void setFiberPer100g(BigDecimal fiberPer100g) {
        this.fiberPer100g = fiberPer100g;
    }

    public BigDecimal getSodiumPer100mg() {
        return sodiumPer100mg;
    }

    public void setSodiumPer100mg(BigDecimal sodiumPer100mg) {
        this.sodiumPer100mg = sodiumPer100mg;
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


    public boolean isValidName() {
        return name != null && !name.trim().isEmpty();
    }

    public boolean hasValidNutritionalValues() {
        return caloriesPer100g != null && caloriesPer100g.compareTo(BigDecimal.ZERO) >= 0 &&
               proteinPer100g != null && proteinPer100g.compareTo(BigDecimal.ZERO) >= 0 &&
               carbsPer100g != null && carbsPer100g.compareTo(BigDecimal.ZERO) >= 0 &&
               fatPer100g != null && fatPer100g.compareTo(BigDecimal.ZERO) >= 0 &&
               fiberPer100g != null && fiberPer100g.compareTo(BigDecimal.ZERO) >= 0 &&
               sodiumPer100mg != null && sodiumPer100mg.compareTo(BigDecimal.ZERO) >= 0;
    }

    // Métodos para calcular valores para porciones específicas
    public BigDecimal getCaloriesForGrams(BigDecimal grams) {
        if (caloriesPer100g == null || grams == null) return BigDecimal.ZERO;
        return caloriesPer100g.multiply(grams).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getProteinForGrams(BigDecimal grams) {
        if (proteinPer100g == null || grams == null) return BigDecimal.ZERO;
        return proteinPer100g.multiply(grams).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getCarbsForGrams(BigDecimal grams) {
        if (carbsPer100g == null || grams == null) return BigDecimal.ZERO;
        return carbsPer100g.multiply(grams).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getFatForGrams(BigDecimal grams) {
        if (fatPer100g == null || grams == null) return BigDecimal.ZERO;
        return fatPer100g.multiply(grams).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getFiberForGrams(BigDecimal grams) {
        if (fiberPer100g == null || grams == null) return BigDecimal.ZERO;
        return fiberPer100g.multiply(grams).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getSodiumForGrams(BigDecimal grams) {
        if (sodiumPer100mg == null || grams == null) return BigDecimal.ZERO;
        return sodiumPer100mg.multiply(grams).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    // Métodos para calcular porcentajes calóricos de macronutrientes
    public BigDecimal getProteinCaloriesPercentage() {
        if (caloriesPer100g == null || caloriesPer100g.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        BigDecimal proteinCalories = proteinPer100g.multiply(new BigDecimal("4"));
        return proteinCalories.multiply(new BigDecimal("100")).divide(caloriesPer100g, 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getCarbsCaloriesPercentage() {
        if (caloriesPer100g == null || caloriesPer100g.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        BigDecimal carbsCalories = carbsPer100g.multiply(new BigDecimal("4"));
        return carbsCalories.multiply(new BigDecimal("100")).divide(caloriesPer100g, 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getFatCaloriesPercentage() {
        if (caloriesPer100g == null || caloriesPer100g.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        BigDecimal fatCalories = fatPer100g.multiply(new BigDecimal("9"));
        return fatCalories.multiply(new BigDecimal("100")).divide(caloriesPer100g, 2, RoundingMode.HALF_UP);
    }

    // Método para obtener la densidad calórica
    public String getCaloricDensity() {
        if (caloriesPer100g == null) return "UNKNOWN";
        if (caloriesPer100g.compareTo(new BigDecimal("50")) < 0) return "VERY_LOW";
        if (caloriesPer100g.compareTo(new BigDecimal("100")) < 0) return "LOW";
        if (caloriesPer100g.compareTo(new BigDecimal("200")) < 0) return "MEDIUM";
        if (caloriesPer100g.compareTo(new BigDecimal("300")) < 0) return "HIGH";
        return "VERY_HIGH";
    }

    @Override
    public String toString() {
        return "FoodItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", caloriesPer100g=" + caloriesPer100g +
                ", proteinPer100g=" + proteinPer100g +
                ", carbsPer100g=" + carbsPer100g +
                ", fatPer100g=" + fatPer100g +
                ", fiberPer100g=" + fiberPer100g +
                ", sodiumPer100mg=" + sodiumPer100mg +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodItem foodItem = (FoodItem) o;
        return id != null && id.equals(foodItem.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}