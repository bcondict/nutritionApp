package edu.ucompensar.codigo.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

import edu.ucompensar.codigo.model.enums.ActivityLevel;
import edu.ucompensar.codigo.model.enums.Sex;

public class UserProfile {
    private UUID id;
    private UUID userId;
    private BigDecimal weightKg;
    private Integer heightCm;
    private Sex sex;
    private ActivityLevel activityLevel;
    private LocalDateTime measuredAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    // Constructor completo
    public UserProfile(
        UUID id,
        UUID userId,
        BigDecimal weightKg,
        Integer heightCm,
        Sex sex,
        ActivityLevel activityLevel,
        LocalDateTime measuredAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        this.id = id;
        this.userId = userId;
        this.weightKg = weightKg;
        this.heightCm = heightCm;
        this.sex = sex;
        this.activityLevel = activityLevel;
        this.measuredAt = measuredAt;
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

    public BigDecimal getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(BigDecimal weightKg) {
        this.weightKg = weightKg;
    }

    public Integer getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(Integer heightCm) {
        this.heightCm = heightCm;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public ActivityLevel getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(ActivityLevel activityLevel) {
        this.activityLevel = activityLevel;
    }

    public LocalDateTime getMeasuredAt() {
        return measuredAt;
    }

    public void setMeasuredAt(LocalDateTime measuredAt) {
        this.measuredAt = measuredAt;
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
    public BigDecimal getBmi() {
        if (weightKg == null || heightCm == null || heightCm <= 0) {
            return null;
        }
        BigDecimal heightInMeters = BigDecimal.valueOf(heightCm).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal bmi = weightKg.divide(heightInMeters.multiply(heightInMeters), 2, RoundingMode.HALF_UP);
        return bmi;
    }

    public String getBmiCategory() {
        BigDecimal bmi = getBmi();
        if (bmi == null) return "UNKNOWN";
        
        if (bmi.compareTo(new BigDecimal("18.5")) < 0) return "UNDERWEIGHT";
        if (bmi.compareTo(new BigDecimal("25")) < 0) return "NORMAL";
        if (bmi.compareTo(new BigDecimal("30")) < 0) return "OVERWEIGHT";
        return "OBESE";
    }


    // Método para calcular TMB (Tasa Metabólica Basal)
    public BigDecimal calculateBMR() {
        if (weightKg == null || heightCm == null || sex == null) {
            return null;
        }
        
        // Fórmula de Mifflin-St Jeor
        BigDecimal bmr;
        if (sex == Sex.MALE) {
            bmr = new BigDecimal("88.362")
                .add(weightKg.multiply(new BigDecimal("13.397")))
                .add(BigDecimal.valueOf(heightCm).multiply(new BigDecimal("4.799")));
        }
        else {
            bmr = new BigDecimal("447.593")
                .add(weightKg.multiply(new BigDecimal("9.247")))
                .add(BigDecimal.valueOf(heightCm).multiply(new BigDecimal("3.098")));
        }
        
        return bmr.setScale(2, RoundingMode.HALF_UP);
    }

    // Método para calcular calorías de mantenimiento según nivel de actividad
    public BigDecimal calculateMaintenanceCalories() {
        BigDecimal bmr = calculateBMR();
        if (bmr == null || activityLevel == null) return null;
        
        BigDecimal multiplier;
        switch (activityLevel) {
            case SEDENTARY:
                multiplier = new BigDecimal("1.2");
                break;
            case LIGHT:
                multiplier = new BigDecimal("1.375");
                break;
            case MODERATE:
                multiplier = new BigDecimal("1.55");
                break;
            case ACTIVE:
                multiplier = new BigDecimal("1.725");
                break;
            case VERY_ACTIVE:
                multiplier = new BigDecimal("1.9");
                break;
            default:
                return null;
        }
        
        return bmr.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "id=" + id +
                ", userId=" + userId +
                ", weightKg=" + weightKg +
                ", heightCm=" + heightCm +
                ", sex='" + sex + '\'' +
                ", activityLevel='" + activityLevel + '\'' +
                ", measuredAt=" + measuredAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
