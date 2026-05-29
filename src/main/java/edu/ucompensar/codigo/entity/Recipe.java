package edu.ucompensar.codigo.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import edu.ucompensar.codigo.model.enums.MealType;
import edu.ucompensar.codigo.model.enums.RecipeDifficulty;

public class Recipe {
    private UUID id;
    private String name;
    private String description;
    private MealType mealType;
    private Integer prepTimeMinutes;
    private RecipeDifficulty difficulty;
    private Integer servings;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constantes para tipos de comida
    // public static final String MEAL_TYPE_BREAKFAST = "BREAKFAST";
    // public static final String MEAL_TYPE_LUNCH = "LUNCH";
    // public static final String MEAL_TYPE_DINNER = "DINNER";
    // public static final String MEAL_TYPE_SNACK = "SNACK";
    // public static final String MEAL_TYPE_DESSERT = "DESSERT";
    // public static final String MEAL_TYPE_BEVERAGE = "BEVERAGE";
    
    // // Constantes para niveles de dificultad
    // public static final String DIFFICULTY_EASY = "EASY";
    // public static final String DIFFICULTY_MEDIUM = "MEDIUM";
    // public static final String DIFFICULTY_HARD = "HARD";
    // public static final String DIFFICULTY_EXPERT = "EXPERT";

    // Constructor vacío
    public Recipe() {
    }

    // Constructor con parámetros principales
    public Recipe(
        String name,
        String description,
        MealType mealType,
        Integer prepTimeMinutes,
        RecipeDifficulty difficulty,
        Integer servings
    ) {
        this.name = name;
        this.description = description;
        this.mealType = mealType;
        this.prepTimeMinutes = prepTimeMinutes;
        this.difficulty = difficulty;
        this.servings = servings;
    }

    // Constructor completo
    public Recipe(
        UUID id,
        String name,
        String description,
        MealType mealType,
        Integer prepTimeMinutes,
        RecipeDifficulty difficulty,
        Integer servings,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.mealType = mealType;
        this.prepTimeMinutes = prepTimeMinutes;
        this.difficulty = difficulty;
        this.servings = servings;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MealType getMealType() {
        return mealType;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
    }

    public Integer getPrepTimeMinutes() {
        return prepTimeMinutes;
    }

    public void setPrepTimeMinutes(Integer prepTimeMinutes) {
        this.prepTimeMinutes = prepTimeMinutes;
    }

    public RecipeDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(RecipeDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
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
    public boolean isValidMealType() {
        return mealType != null && (
            mealType == MealType.BREAKFAST ||
            mealType == MealType.LUNCH ||
            mealType == MealType.DINNER ||
            mealType == MealType.SNACK
        );
    }

    public boolean isValidDifficulty() {
        return difficulty != null && (
            difficulty == RecipeDifficulty.EASY ||
            difficulty == RecipeDifficulty.MEDIUM ||
            difficulty == RecipeDifficulty.HARD
        );
    }

    public boolean isValidName() {
        return name != null && !name.trim().isEmpty();
    }

    public boolean isValidPrepTime() {
        return prepTimeMinutes != null && prepTimeMinutes > 0;
    }

    public boolean isValidServings() {
        return servings != null && servings > 0;
    }

    public String getPrepTimeFormatted() {
        if (prepTimeMinutes == null) return "N/A";
        int hours = prepTimeMinutes / 60;
        int minutes = prepTimeMinutes % 60;
        
        if (hours > 0) {
            return String.format("%d hora(s) y %d minuto(s)", hours, minutes);
        }
        return String.format("%d minuto(s)", minutes);
    }

    public String getDifficultyIcon() {
        if (difficulty == null) return "❓";
        switch (difficulty) {
            case EASY: return "🟢";
            case MEDIUM: return "🟡";
            case HARD: return "🔴";
            default: return "❓";
        }
    }

    public String getMealTypeIcon() {
        if (mealType == null) return "🍽️";
        switch (mealType) {
            case BREAKFAST: return "🌅";
            case LUNCH: return "☀️";
            case DINNER: return "🌙";
            case SNACK: return "🍎";
            default: return "🍽️";
        }
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", mealType='" + mealType + '\'' +
                ", prepTimeMinutes=" + prepTimeMinutes +
                ", difficulty='" + difficulty + '\'' +
                ", servings=" + servings +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return id != null && id.equals(recipe.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}