package edu.ucompensar.codigo.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import edu.ucompensar.codigo.model.enums.GoalStatus;
import edu.ucompensar.codigo.model.enums.GoalType;

public class Goal {
    private UUID id;
    private UUID userId;
    private GoalType type;
    private GoalStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor vacío
    public Goal() {
    }

    // Constructor con parámetros principales
    public Goal(UUID userId, GoalType type, GoalStatus status, LocalDateTime startedAt, LocalDateTime endedAt) {
        this.userId = userId;
        this.type = type;
        this.status = status;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    // Constructor completo
    public Goal(
        UUID id,
        UUID userId,
        GoalType type,
        GoalStatus status,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.status = status;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
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

    public GoalType getType() {
        return type;
    }

    public void setType(GoalType type) {
        this.type = type;
    }

    public GoalStatus getStatus() {
        return status;
    }

    public void setStatus(GoalStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
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
    public boolean isValidType() {
        return type != null && (
            type == GoalType.LOSE_WEIGHT ||
            type == GoalType.GAIN_MUSCLE ||
            type == GoalType.MAINTAIN_WEIGHT ||
            type == GoalType.IMPROVE_HEALTH
        );
    }

    public boolean isValidStatus() {
        return status != null && (
            status == GoalStatus.ACTIVE ||
            status == GoalStatus.COMPLETED ||
            status == GoalStatus.CANCELLED ||
            status == GoalStatus.PAUSED
        );
    }

    public boolean isActive() {
        return GoalStatus.ACTIVE == status;
    }

    public boolean isCompleted() {
        return GoalStatus.COMPLETED == status;
    }

    public boolean isAbandoned() {
        return GoalStatus.CANCELLED == status;
    }

    public boolean isPaused() {
        return GoalStatus.PAUSED == status;
    }

    public boolean hasStarted() {
        return startedAt != null && startedAt.isBefore(LocalDateTime.now());
    }

    public boolean hasEnded() {
        return endedAt != null && endedAt.isBefore(LocalDateTime.now());
    }

    public boolean isInProgress() {
        return isActive() && hasStarted() && !hasEnded();
    }

    @Override
    public String toString() {
        return "Goal{" +
                "id=" + id +
                ", userId=" + userId +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", startedAt=" + startedAt +
                ", endedAt=" + endedAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goal goal = (Goal) o;
        return id != null && id.equals(goal.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}