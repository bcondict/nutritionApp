package edu.ucompensar.codigo.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import edu.ucompensar.codigo.model.enums.DietaryPreferenceCategory;
import edu.ucompensar.codigo.model.enums.DietaryPreferenceCode;

public class DietaryPreference {
    private UUID id;
    private DietaryPreferenceCode code;
    private String label;
    private DietaryPreferenceCategory category;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    // Constructor vacío
    public DietaryPreference() {
    }

    // Constructor con parámetros principales
    public DietaryPreference(DietaryPreferenceCode code, String label, DietaryPreferenceCategory category, String description) {
        this.code = code;
        this.label = label;
        this.category = category;
        this.description = description;
    }

    // Constructor completo
    public DietaryPreference(
        UUID id,
        DietaryPreferenceCode code,
        String label,
        DietaryPreferenceCategory category,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        this.id = id;
        this.code = code;
        this.label = label;
        this.category = category;
        this.description = description;
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

    public DietaryPreferenceCode getCode() {
        return code;
    }

    public void setCode(DietaryPreferenceCode code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public DietaryPreferenceCategory getCategory() {
        return category;
    }

    public void setCategory(DietaryPreferenceCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public boolean isValidCode() {
        return code != null;
    }

    public boolean isValidLabel() {
        return label != null && !label.trim().isEmpty();
    }

    public boolean isRestriction() {
        return category == DietaryPreferenceCategory.RESTRICTION;
    }

    public boolean isAllergy() {
        return DietaryPreferenceCategory.ALLERGY == category;
    }

    public boolean isIntolerance() {
        return DietaryPreferenceCategory.INTOLERANCE == category;
    }

    public boolean isPhilosophy() {
        return DietaryPreferenceCategory.PHILOSOPHY == category;
    }

    public boolean isReligious() {
        return DietaryPreferenceCategory.RELIGIOUS == category;
    }

    public boolean isLifestyle() {
        return DietaryPreferenceCategory.LIFESTYLE == category;
    }

    public boolean isMedical() {
        return DietaryPreferenceCategory.MEDICAL == category;
    }

    public String getCategoryIcon() {
        if (category == null) return "❓";
        switch (category) {
            case RESTRICTION: return "🚫";
            case ALLERGY: return "⚠️";
            case INTOLERANCE: return "💔";
            case PHILOSOPHY: return "🌱";
            case RELIGIOUS: return "🕊️";
            case LIFESTYLE: return "💪";
            case MEDICAL: return "🏥";
            default: return "📌";
        }
    }

    @Override
    public String toString() {
        return "DietaryPreference{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", label='" + label + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DietaryPreference that = (DietaryPreference) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}