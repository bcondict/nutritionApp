package edu.ucompensar.codigo.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserDietaryPreference {
    private UUID id;
    private UUID userId;
    private UUID dietaryPreferenceId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor vacío
    public UserDietaryPreference() {
    }

    // Constructor con parámetros principales
    public UserDietaryPreference(UUID userId, UUID dietaryPreferenceId) {
        this.userId = userId;
        this.dietaryPreferenceId = dietaryPreferenceId;
    }

    // Constructor completo
    public UserDietaryPreference(
        UUID id,
        UUID userId,
        UUID dietaryPreferenceId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        this.id = id;
        this.userId = userId;
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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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
        return userId != null && dietaryPreferenceId != null;
    }

    @Override
    public String toString() {
        return "UserDietaryPreference{" +
                "id=" + id +
                ", userId=" + userId +
                ", dietaryPreferenceId=" + dietaryPreferenceId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDietaryPreference that = (UserDietaryPreference) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}