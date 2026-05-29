package edu.ucompensar.codigo.DAO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.database.DatabaseConnection;
import edu.ucompensar.codigo.entity.Goal;
import edu.ucompensar.codigo.model.enums.GoalStatus;
import edu.ucompensar.codigo.model.enums.GoalType;
import edu.ucompensar.codigo.model.interfaces.IGoalDAO;

public class GoalDAO implements IGoalDAO {

    @Override
    public void save(Goal goal) {
        String sql = """
            INSERT INTO goal (id, user_id, type, status, started_at, ended_at, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            if (goal.getId() == null) {
                goal.setId(UUID.randomUUID());
            }

            LocalDateTime now = LocalDateTime.now();
            if (goal.getCreatedAt() == null) {
                goal.setCreatedAt(now);
            }
            if (goal.getUpdatedAt() == null) {
                goal.setUpdatedAt(now);
            }

            statement.setString(1, goal.getId().toString());
            statement.setString(2, goal.getUserId().toString());
            statement.setString(3, goal.getType().name());
            statement.setString(4, goal.getStatus().name());
            statement.setObject(5, goal.getStartedAt());
            statement.setObject(6, goal.getEndedAt());
            statement.setObject(7, goal.getCreatedAt());
            statement.setObject(8, goal.getUpdatedAt());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar Goal: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Goal findById(UUID id) {
        String sql = "SELECT * FROM goal WHERE id = ?";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, id.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGoal(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Goal por ID: " + e.getMessage(), e);
        }

        return null;
    }
    
    @Override
    public List<Goal> findAll() {
        List<Goal> goals = new ArrayList<>();
        String sql = "SELECT * FROM goal ORDER BY created_at DESC";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                goals.add(mapResultSetToGoal(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar todos los Goals: " + e.getMessage(), e);
        }
        
        return goals;
    }

    @Override
    public void update(Goal goal) {
        String sql ="""
            UPDATE goal
            SET user_id = ?,
                type = ?,
                status = ?,
                started_at = ?,
                ended_at = ?,
                updated_at = ?
            WHERE id = ?";   
        """;

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, goal.getUserId().toString());
            statement.setString(2, goal.getType().name());
            statement.setString(3, goal.getStatus().name());
            statement.setObject(4, goal.getStartedAt());
            statement.setObject(5, goal.getEndedAt());
            statement.setObject(6, LocalDateTime.now());
            statement.setString(7, goal.getId().toString());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró Goal con ID: " + goal.getId());
            }

            // Actualizar el objeto local
            goal.setUpdatedAt(LocalDateTime.now());

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar Goal: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM goal WHERE id = ?";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, id.toString());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró Goal con ID: " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar Goal: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Goal> findByUserId(UUID userId) {
        List<Goal> goals = new ArrayList<>();
        String sql = "SELECT * FROM goal WHERE user_id = ? ORDER BY started_at DESC, created_at DESC";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, userId.toString());

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    goals.add(mapResultSetToGoal(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Goals por userId: " + e.getMessage(), e);
        }

        return goals;
    }

    @Override
    public List<Goal> findByUserIdAndStatus(UUID userId, GoalStatus status) {
        List<Goal> goals = new ArrayList<>();
        String sql = "SELECT * FROM goal WHERE user_id = ? AND status = ? ORDER BY started_at DESC";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, userId.toString());
            statement.setString(2, status.name());

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    goals.add(mapResultSetToGoal(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Goals por userId y status: " + e.getMessage(), e);
        }

        return goals;
    }

    @Override
    public List<Goal> findByUserIdAndType(UUID userId, GoalType type) {
        List<Goal> goals = new ArrayList<>();
        String sql = "SELECT * FROM goal WHERE user_id = ? AND type = ? ORDER BY started_at DESC";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, userId.toString());
            statement.setString(2, type.name());

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    goals.add(mapResultSetToGoal(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Goals por userId y type: " + e.getMessage(), e);
        }

        return goals;
    }

    @Override
    public List<Goal> findActiveGoalsByUserId(UUID userId) {
        return findByUserIdAndStatus(userId, GoalStatus.ACTIVE);
    }

    @Override
    public List<Goal> findCompletedGoalsByUserId(UUID userId) {
        return findByUserIdAndStatus(userId, GoalStatus.COMPLETED);
    }
    
    @Override
    public List<Goal> findGoalsByUserIdInDateRange(UUID userId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Goal> goals = new ArrayList<>();
        String sql = "SELECT * FROM goal WHERE user_id = ? AND started_at BETWEEN ? AND ? ORDER BY started_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, userId.toString());
            statement.setObject(2, startDate);
            statement.setObject(3, endDate);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    goals.add(mapResultSetToGoal(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Goals por rango de fechas: " + e.getMessage(), e);
        }
        
        return goals;
    }

    @Override
    public List<Goal> findByStatus(GoalStatus status) {
        List<Goal> goals = new ArrayList<>();
        String sql = "SELECT * FROM goal WHERE status = ? ORDER BY created_at DESC";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, status.name());

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    goals.add(mapResultSetToGoal(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Goals por status: " + e.getMessage(), e);
        }

        return goals;
    }

    @Override
    public List<Goal> findByType(GoalType type) {
        List<Goal> goals = new ArrayList<>();
        String sql = "SELECT * FROM goal WHERE type = ? ORDER BY created_at DESC";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, type.name());

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    goals.add(mapResultSetToGoal(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Goals por type: " + e.getMessage(), e);
        }

        return goals;
    }

    @Override
    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM goal WHERE id = ?";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, id.toString());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia de Goal: " + e.getMessage(), e);
        }

        return false;
    }

    @Override
    public boolean existsActiveGoalByUserId(UUID userId) {
        String sql = "SELECT COUNT(*) FROM goal WHERE user_id = ? AND status = ?";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, userId.toString());
            statement.setString(2, GoalStatus.ACTIVE.name());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia de Goal activo: " + e.getMessage(), e);
        }

        return false;
    }
    
    @Override
    public int countByUserId(UUID userId) {
        String sql = "SELECT COUNT(*) FROM goal WHERE user_id = ?";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, userId.toString());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al contar Goals por usuario: " + e.getMessage(), e);
        }

        return 0;
    }

    @Override
    public int countByUserIdAndStatus(UUID userId, GoalStatus status) {
        String sql = "SELECT COUNT(*) FROM goal WHERE user_id = ? AND status = ?";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, userId.toString());
            statement.setString(2, status.name());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al contar Goals por usuario y status: " + e.getMessage(), e);
        }

        return 0;
    }

    @Override
    public int countByType(GoalType type) {
        String sql = "SELECT COUNT(*) FROM goal WHERE type = ?";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, type.name());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al contar Goals por tipo: " + e.getMessage(), e);
        }

        return 0;
    }

    @Override
    public void deleteByUserId(UUID userId) {
        String sql = "DELETE FROM goal WHERE user_id = ?";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, userId.toString());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar Goals por userId: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteAbandonedGoals() {
        String sql = "DELETE FROM goal WHERE status = ?";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, GoalStatus.CANCELLED.name());

            int deletedCount = statement.executeUpdate();
            System.out.println("Objetivos abandonados eliminados: " + deletedCount);

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar objetivos abandonados: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteCompletedGoalsOlderThan(LocalDateTime date) {
        String sql = "DELETE FROM goal WHERE status = ? AND updated_at < ?";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, GoalStatus.COMPLETED.name());
            statement.setObject(2, date);

            int deletedCount = statement.executeUpdate();
            System.out.println("Objetivos completados antiguos eliminados: " + deletedCount);

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar objetivos completados antiguos: " + e.getMessage(), e);
        }
    }

    @Override
    public void saveWithValidation(Goal goal) {
        // Validaciones
        if (goal.getUserId() == null) {
            throw new IllegalArgumentException("El userId no puede ser null");
        }

        if (!goal.isValidType()) {
            throw new IllegalArgumentException("Tipo de objetivo inválido. Valores permitidos: " +
                "WEIGHT_LOSS, WEIGHT_GAIN, MUSCLE_GAIN, MAINTENANCE, ENDURANCE, STRENGTH, HEALTH_IMPROVEMENT");
        }

        if (!goal.isValidStatus()) {
            throw new IllegalArgumentException("Estado inválido. Valores permitidos: " +
                "ACTIVE, COMPLETED, ABANDONED, PAUSED, PLANNED");
        }

        if (goal.getStartedAt() == null && goal.isActive()) {
            goal.setStartedAt(LocalDateTime.now());
        }

        if (goal.getEndedAt() != null && goal.getStartedAt() != null && 
            goal.getEndedAt().isBefore(goal.getStartedAt())) {
            throw new IllegalArgumentException("La fecha de finalización no puede ser anterior a la fecha de inicio");
        }

        // Guardar
        save(goal);
    }

    @Override
    public void updateWithValidation(Goal goal) {
        // Validaciones
        if (goal.getId() == null) {
            throw new IllegalArgumentException("El ID no puede ser null para actualizar");
        }

        if (goal.getUserId() == null) {
            throw new IllegalArgumentException("El userId no puede ser null");
        }

        if (!goal.isValidType()) {
            throw new IllegalArgumentException("Tipo de objetivo inválido");
        }

        if (!goal.isValidStatus()) {
            throw new IllegalArgumentException("Estado inválido");
        }

        if (goal.getEndedAt() != null && goal.getStartedAt() != null && 
            goal.getEndedAt().isBefore(goal.getStartedAt())) {
            throw new IllegalArgumentException("La fecha de finalización no puede ser anterior a la fecha de inicio");
        }

        // Si se está completando el objetivo y no tiene fecha de finalización
        if (goal.isCompleted() && goal.getEndedAt() == null) {
            goal.setEndedAt(LocalDateTime.now());
        }

        // Actualizar
        update(goal);
    }

    @Override
    public void completeGoal(UUID id) {
        Goal goal = findById(id);
        if (goal == null) {
            throw new RuntimeException("No se encontró el objetivo con ID: " + id);
        }

        goal.setStatus(GoalStatus.COMPLETED);
        goal.setEndedAt(LocalDateTime.now());
        updateWithValidation(goal);
    }

    @Override
    public void abandonGoal(UUID id) {
        Goal goal = findById(id);
        if (goal == null) {
            throw new RuntimeException("No se encontró el objetivo con ID: " + id);
        }

        goal.setStatus(GoalStatus.CANCELLED);
        goal.setEndedAt(LocalDateTime.now());
        updateWithValidation(goal);
    }

    @Override
    public void pauseGoal(UUID id) {
        Goal goal = findById(id);
        if (goal == null) {
            throw new RuntimeException("No se encontró el objetivo con ID: " + id);
        }

        if (!goal.isActive()) {
            throw new IllegalStateException("Solo se pueden pausar objetivos activos");
        }

        goal.setStatus(GoalStatus.PAUSED);
        updateWithValidation(goal);
    }

    @Override
    public void activateGoal(UUID id) {
        Goal goal = findById(id);
        if (goal == null) {
            throw new RuntimeException("No se encontró el objetivo con ID: " + id);
        }

        if (!goal.isPaused()) {
            throw new IllegalStateException("Solo se pueden activar objetivos en estado PLANNED o PAUSED");
        }

        goal.setStatus(GoalStatus.ACTIVE);
        if (goal.getStartedAt() == null) {
            goal.setStartedAt(LocalDateTime.now());
        }
        updateWithValidation(goal);
    }

    @Override
    public List<Goal> findAllActiveGoals() {
        return findByStatus(GoalStatus.ACTIVE);
    }

    @Override
    public List<Goal> findAllGoalsOrderByStartedAtDesc() {
        List<Goal> goals = new ArrayList<>();
        String sql = "SELECT * FROM goal ORDER BY started_at DESC NULLS LAST";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                goals.add(mapResultSetToGoal(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar Goals ordenadas por fecha de inicio: " + e.getMessage(), e);
        }

        return goals;
    }

    @Override
    public List<Goal> findGoalsThatShouldHaveEnded() {
        List<Goal> goals = new ArrayList<>();
        String sql = "SELECT * FROM goal WHERE status = ? AND ended_at < ?";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, GoalStatus.ACTIVE.name());
            statement.setObject(2, LocalDateTime.now());

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    goals.add(mapResultSetToGoal(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar objetivos que deberían haber terminado: " + e.getMessage(), e);
        }

        return goals;
    }

    // Método auxiliar para mapear ResultSet a Goal
    private Goal mapResultSetToGoal(ResultSet rs) throws SQLException {
        Goal goal = new Goal(
            UUID.fromString(rs.getString("id")),
            UUID.fromString(rs.getString("user_id")),
            GoalType.valueOf(rs.getString("type")),
            GoalStatus.valueOf(rs.getString("status")),
            rs.getTimestamp("started_at").toLocalDateTime(),
            rs.getTimestamp("ended_at").toLocalDateTime(),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("updated_at").toLocalDateTime()
        );

        return goal;
    }
}