package edu.ucompensar.codigo.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import edu.ucompensar.codigo.model.enums.DailyMenuSlot;

public class DailyMenuRecipe {
    private UUID id;
    private UUID dailyMenuId;
    private UUID recipeId;
    private DailyMenuSlot slot;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor completo
    public DailyMenuRecipe(UUID id, UUID dailyMenuId, UUID recipeId, DailyMenuSlot slot, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.dailyMenuId = dailyMenuId;
        this.recipeId = recipeId;
        this.slot = slot;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getDailyMenuId() { return dailyMenuId; }
    public void setDailyMenuId(UUID dailyMenuId) { this.dailyMenuId = dailyMenuId; }

    public UUID getRecipeId() { return recipeId; }
    public void setRecipeId(UUID recipeId) { this.recipeId = recipeId; }

    public DailyMenuSlot getSlot() { return slot; }
    public void setSlot(DailyMenuSlot slot) { this.slot = slot; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "DailyMenuRecipe{id=" + id + ", dailyMenuId=" + dailyMenuId + ", recipeId=" + recipeId + ", slot='" + slot + "'}";
    }
}