package edu.ucompensar.codigo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class DailyMenu {
    private UUID id;
    private UUID weeklyMenuId;
    private String dayOfWeek;
    private BigDecimal totalCalories;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor completo
    public DailyMenu(UUID id, UUID weeklyMenuId, String dayOfWeek, BigDecimal totalCalories, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.weeklyMenuId = weeklyMenuId;
        this.dayOfWeek = dayOfWeek;
        this.totalCalories = totalCalories;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getWeeklyMenuId() { return weeklyMenuId; }
    public void setWeeklyMenuId(UUID weeklyMenuId) { this.weeklyMenuId = weeklyMenuId; }

    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public BigDecimal getTotalCalories() { return totalCalories; }
    public void setTotalCalories(BigDecimal totalCalories) { this.totalCalories = totalCalories; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "DailyMenu{id=" + id + ", weeklyMenuId=" + weeklyMenuId + ", dayOfWeek='" + dayOfWeek + 
               "', totalCalories=" + totalCalories + "}";
    }
}