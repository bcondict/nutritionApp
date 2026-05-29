package edu.ucompensar.codigo.service;

import edu.ucompensar.codigo.DAO.GoalDAO;
import edu.ucompensar.codigo.entity.Goal;
import edu.ucompensar.codigo.model.interfaces.IGoalDAO;
import edu.ucompensar.codigo.model.enums.GoalType;
import edu.ucompensar.codigo.model.enums.GoalStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class GoalService {
    private final IGoalDAO goalDAO;

    public GoalService() {
        this.goalDAO = new GoalDAO();
    }

    // ==================== CRUD BÁSICO ====================
    
    public void save(Goal goal) {
        goalDAO.save(goal);
    }

    public Goal findById(UUID id) {
        return goalDAO.findById(id);
    }

    public List<Goal> findAll() {
        return goalDAO.findAll();
    }

    public void update(Goal goal) {
        goalDAO.update(goal);
    }

    public void delete(UUID id) {
        goalDAO.delete(id);
    }

    // ==================== BÚSQUEDA POR USUARIO ====================
    
    public List<Goal> findByUserId(UUID userId) {
        return goalDAO.findByUserId(userId);
    }

    public List<Goal> findByUserIdAndStatus(UUID userId, GoalStatus status) {
        return goalDAO.findByUserIdAndStatus(userId, status);
    }

    public List<Goal> findByUserIdAndType(UUID userId, GoalType type) {
        return goalDAO.findByUserIdAndType(userId, type);
    }

    public List<Goal> findActiveGoalsByUserId(UUID userId) {
        return goalDAO.findActiveGoalsByUserId(userId);
    }

    public List<Goal> findCompletedGoalsByUserId(UUID userId) {
        return goalDAO.findCompletedGoalsByUserId(userId);
    }

    public List<Goal> findGoalsByUserIdInDateRange(UUID userId, LocalDateTime startDate, LocalDateTime endDate) {
        return goalDAO.findGoalsByUserIdInDateRange(userId, startDate, endDate);
    }

    // ==================== BÚSQUEDA POR ESTADO/TIPO ====================
    
    public List<Goal> findByStatus(GoalStatus status) {
        return goalDAO.findByStatus(status);
    }

    public List<Goal> findByType(GoalType type) {
        return goalDAO.findByType(type);
    }

    // ==================== MÉTODOS DE UTILIDAD ====================
    
    public boolean existsById(UUID id) {
        return goalDAO.existsById(id);
    }

    public boolean existsActiveGoalByUserId(UUID userId) {
        return goalDAO.existsActiveGoalByUserId(userId);
    }

    public int countByUserId(UUID userId) {
        return goalDAO.countByUserId(userId);
    }

    public int countByUserIdAndStatus(UUID userId, GoalStatus status) {
        return goalDAO.countByUserIdAndStatus(userId, status);
    }

    public int countByType(GoalType type) {
        return goalDAO.countByType(type);
    }

    // ==================== GESTIÓN DE OBJETIVOS ====================
    
    public void completeGoal(UUID id) {
        goalDAO.completeGoal(id);
    }

    public void abandonGoal(UUID id) {
        goalDAO.abandonGoal(id);
    }

    public void pauseGoal(UUID id) {
        goalDAO.pauseGoal(id);
    }

    public void activateGoal(UUID id) {
        goalDAO.activateGoal(id);
    }

    /**
     * Crea un nuevo objetivo para un usuario
     */
    public Goal createGoal(UUID userId, GoalType type, LocalDateTime startedAt, LocalDateTime endedAt) {
        UUID goalId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Goal goal = new Goal(
            goalId,
            userId,
            type,
            GoalStatus.ACTIVE,
            startedAt,
            endedAt,
            now,
            now
        );

        save(goal);
        return goal;
    }
    public Goal createGoal(UUID userId, GoalType type) {
        UUID goalId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Goal goal = new Goal(
            goalId,
            userId,
            type,
            GoalStatus.ACTIVE,
            now,
            now,
            now,
            now
        );

        save(goal);
        return goal;
    }

    /**
     * Obtiene el objetivo activo actual de un usuario
     */
    public Goal getCurrentActiveGoal(UUID userId) {
        List<Goal> activeGoals = findActiveGoalsByUserId(userId);
        if (activeGoals.isEmpty()) {
            return null;
        }
        // Retorna el más reciente (por fecha de inicio)
        return activeGoals.get(0);
    }

    /**
     * Verifica si un usuario tiene un objetivo activo
     */
    public boolean hasActiveGoal(UUID userId) {
        return getCurrentActiveGoal(userId) != null;
    }

    /**
     * Desactiva todos los objetivos activos de un usuario
     */
    public void deactivateAllGoals(UUID userId) {
        List<Goal> activeGoals = findActiveGoalsByUserId(userId);
        for (Goal goal : activeGoals) {
            if (goal.isActive()) {
                goal.setStatus(GoalStatus.CANCELLED);
                goal.setEndedAt(LocalDateTime.now());
                update(goal);
            }
        }
    }

    // ==================== ELIMINACIÓN ====================
    
    public void deleteByUserId(UUID userId) {
        goalDAO.deleteByUserId(userId);
    }

    public void deleteAbandonedGoals() {
        goalDAO.deleteAbandonedGoals();
    }

    public void deleteCompletedGoalsOlderThan(LocalDateTime date) {
        goalDAO.deleteCompletedGoalsOlderThan(date);
    }

    // ==================== VALIDACIÓN ====================
    
    public void saveWithValidation(Goal goal) {
        goalDAO.saveWithValidation(goal);
    }

    public void updateWithValidation(Goal goal) {
        goalDAO.updateWithValidation(goal);
    }

    // ==================== REPORTES ====================
    
    public List<Goal> findAllActiveGoals() {
        return goalDAO.findAllActiveGoals();
    }

    public List<Goal> findAllGoalsOrderByStartedAtDesc() {
        return goalDAO.findAllGoalsOrderByStartedAtDesc();
    }

    public List<Goal> findGoalsThatShouldHaveEnded() {
        return goalDAO.findGoalsThatShouldHaveEnded();
    }

    // ==================== ESTADÍSTICAS AVANZADAS ====================
    
    /**
     * Obtiene el porcentaje de objetivos completados por un usuario
     */
    public double getCompletionRateByUserId(UUID userId) {
        int total = countByUserId(userId);
        if (total == 0) return 0.0;
        
        int completed = countByUserIdAndStatus(userId, GoalStatus.COMPLETED);
        return (double) completed / total * 100;
    }

    /**
     * Obtiene la duración promedio de los objetivos completados (en días)
     */
    public double getAverageGoalDuration(UUID userId) {
        List<Goal> completedGoals = findCompletedGoalsByUserId(userId);
        if (completedGoals.isEmpty()) return 0.0;
        
        long totalDays = 0;
        for (Goal goal : completedGoals) {
            if (goal.getStartedAt() != null && goal.getEndedAt() != null) {
                long days = java.time.Duration.between(goal.getStartedAt(), goal.getEndedAt()).toDays();
                totalDays += days;
            }
        }
        return (double) totalDays / completedGoals.size();
    }

    /**
     * Obtiene los objetivos por tipo con conteos para un usuario
     */
    public List<GoalTypeSummary> getGoalTypeSummaries(UUID userId) {
        List<Goal> userGoals = findByUserId(userId);
        
        return java.util.Arrays.stream(GoalType.values())
            .map(type -> {
                long count = userGoals.stream()
                    .filter(g -> g.getType().equals(type.name()))
                    .count();
                long completed = userGoals.stream()
                    .filter(g -> g.getType().equals(type.name()) && g.isCompleted())
                    .count();
                return new GoalTypeSummary(type, (int) count, (int) completed);
            })
            .filter(s -> s.getCount() > 0)
            .collect(Collectors.toList());
    }

    /**
     * Clase interna para resumen de tipos de objetivo
     */
    public static class GoalTypeSummary {
        private final GoalType type;
        private final int count;
        private final int completed;

        public GoalTypeSummary(GoalType type, int count, int completed) {
            this.type = type;
            this.count = count;
            this.completed = completed;
        }

        public GoalType getType() { return type; }
        public int getCount() { return count; }
        public int getCompleted() { return completed; }
        public int getInProgress() { return count - completed; }
        public double getCompletionPercentage() { 
            return count == 0 ? 0 : (double) completed / count * 100; 
        }

    }

    // ==================== MÉTODOS PARA LA VISTA ====================
    

    /**
     * Valida si un objetivo puede ser creado (no hay otro activo del mismo tipo)
     */
    public boolean canCreateGoal(UUID userId, String type) {
        List<Goal> activeGoals = findActiveGoalsByUserId(userId);
        return activeGoals.stream().noneMatch(g -> g.getType().equals(type));
    }
}