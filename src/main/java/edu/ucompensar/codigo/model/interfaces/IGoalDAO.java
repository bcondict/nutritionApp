package edu.ucompensar.codigo.model.interfaces;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.entity.Goal;
import edu.ucompensar.codigo.model.enums.GoalStatus;
import edu.ucompensar.codigo.model.enums.GoalType;

public interface IGoalDAO {

    // CRUD básico
    void save(Goal goal);
    Goal findById(UUID id);
    List<Goal> findAll();
    void update(Goal goal);
    void delete(UUID id);

    // Métodos específicos de búsqueda por usuario
    List<Goal> findByUserId(UUID userId);
    List<Goal> findByUserIdAndStatus(UUID userId, GoalStatus status);
    List<Goal> findByUserIdAndType(UUID userId, GoalType type);
    List<Goal> findActiveGoalsByUserId(UUID userId);
    List<Goal> findCompletedGoalsByUserId(UUID userId);
    List<Goal> findGoalsByUserIdInDateRange(UUID userId, LocalDateTime startDate, LocalDateTime endDate);

    // Métodos de búsqueda por estado
    List<Goal> findByStatus(GoalStatus status);
    List<Goal> findByType(GoalType type);

    // Métodos de utilidad
    boolean existsById(UUID id);
    boolean existsActiveGoalByUserId(UUID userId);
    int countByUserId(UUID userId);
    int countByUserIdAndStatus(UUID userId, GoalStatus status);
    int countByType(GoalType type);

    // Métodos de eliminación
    void deleteByUserId(UUID userId);
    void deleteAbandonedGoals();
    void deleteCompletedGoalsOlderThan(LocalDateTime date);

    // Métodos de validación
    void saveWithValidation(Goal goal);
    void updateWithValidation(Goal goal);

    // Métodos de gestión de objetivos
    void completeGoal(UUID id);
    void abandonGoal(UUID id);
    void pauseGoal(UUID id);
    void activateGoal(UUID id);

    // Métodos de reportes
    List<Goal> findAllActiveGoals();
    List<Goal> findAllGoalsOrderByStartedAtDesc();
    List<Goal> findGoalsThatShouldHaveEnded();
}