package edu.ucompensar.codigo.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import edu.ucompensar.codigo.model.enums.MedicalConditionSeverity;

public class UserMedicalCondition {
    private UUID id;
    private UUID userId;
    private UUID medicalConditionId;
    private MedicalConditionSeverity severity;
    private LocalDateTime diagnosedAt;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor vacío
    public UserMedicalCondition() {
    }

    // Constructor con parámetros principales
    public UserMedicalCondition(
        UUID userId,
        UUID medicalConditionId,
        MedicalConditionSeverity severity,
        LocalDateTime diagnosedAt,
        String notes
    ) {
        this.userId = userId;
        this.medicalConditionId = medicalConditionId;
        this.severity = severity;
        this.diagnosedAt = diagnosedAt;
        this.notes = notes;
    }

    // Constructor completo
    public UserMedicalCondition(
        UUID id,
        UUID userId,
        UUID medicalConditionId,
        MedicalConditionSeverity severity,
        LocalDateTime diagnosedAt,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        this.id = id;
        this.userId = userId;
        this.medicalConditionId = medicalConditionId;
        this.severity = severity;
        this.diagnosedAt = diagnosedAt;
        this.notes = notes;
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

    public UUID getMedicalConditionId() {
        return medicalConditionId;
    }

    public void setMedicalConditionId(UUID medicalConditionId) {
        this.medicalConditionId = medicalConditionId;
    }

    public MedicalConditionSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(MedicalConditionSeverity severity) {
        this.severity = severity;
    }

    public LocalDateTime getDiagnosedAt() {
        return diagnosedAt;
    }

    public void setDiagnosedAt(LocalDateTime diagnosedAt) {
        this.diagnosedAt = diagnosedAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
    public boolean isValidSeverity() {
        return severity != null && (
            severity == MedicalConditionSeverity.LOW ||
            severity == MedicalConditionSeverity.MODERATE ||
            severity == MedicalConditionSeverity.HIGH ||
            severity == MedicalConditionSeverity.CRITICAL
        );
    }

    public boolean hasNotes() {
        return notes != null && !notes.trim().isEmpty();
    }

    public boolean isDiagnosed() {
        return diagnosedAt != null;
    }

    @Override
    public String toString() {
        return "UserMedicalCondition{" +
                "id=" + id +
                ", userId=" + userId +
                ", medicalConditionId=" + medicalConditionId +
                ", severity='" + severity + '\'' +
                ", diagnosedAt=" + diagnosedAt +
                ", notes='" + notes + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMedicalCondition that = (UserMedicalCondition) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}