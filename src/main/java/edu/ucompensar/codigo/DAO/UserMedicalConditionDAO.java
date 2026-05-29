package edu.ucompensar.codigo.DAO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.database.DatabaseConnection;
import edu.ucompensar.codigo.entity.UserMedicalCondition;
import edu.ucompensar.codigo.model.enums.MedicalConditionSeverity;
import edu.ucompensar.codigo.model.interfaces.IUserMedicalConditionDAO;

public class UserMedicalConditionDAO implements IUserMedicalConditionDAO {
    
    @Override
    public void save(UserMedicalCondition userMedicalCondition) {
        String sql = """
            INSERT INTO user_medical_condition (
                id,
                user_id,
                medical_condition_id,
                severity,
                diagnosed_at,
                notes,
                created_at,
                updated_at
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            if (userMedicalCondition.getId() == null) {
                userMedicalCondition.setId(UUID.randomUUID());
            }
            
            LocalDateTime now = LocalDateTime.now();
            if (userMedicalCondition.getCreatedAt() == null) {
                userMedicalCondition.setCreatedAt(now);
            }
            if (userMedicalCondition.getUpdatedAt() == null) {
                userMedicalCondition.setUpdatedAt(now);
            }
            
            statement.setString(1, userMedicalCondition.getId().toString());
            statement.setString(2, userMedicalCondition.getUserId().toString());
            statement.setString(3, userMedicalCondition.getMedicalConditionId().toString());
            statement.setString(4, userMedicalCondition.getSeverity().name());
            statement.setObject(5, userMedicalCondition.getDiagnosedAt());
            statement.setString(6, userMedicalCondition.getNotes());
            statement.setObject(7, userMedicalCondition.getCreatedAt());
            statement.setObject(8, userMedicalCondition.getUpdatedAt());
            
            statement.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar UserMedicalCondition: " + e.getMessage(), e);
        }
    }
    
    @Override
    public UserMedicalCondition findById(UUID id) {
        String sql = "SELECT * FROM user_medical_condition WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, id.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUserMedicalCondition(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar UserMedicalCondition por ID: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public List<UserMedicalCondition> findAll() {
        List<UserMedicalCondition> conditions = new ArrayList<>();
        String sql = "SELECT * FROM user_medical_condition ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                conditions.add(mapResultSetToUserMedicalCondition(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar todos los UserMedicalConditions: " + e.getMessage(), e);
        }
        
        return conditions;
    }
    
    @Override
    public void update(UserMedicalCondition userMedicalCondition) {
        String sql = "UPDATE user_medical_condition SET user_id = ?, medical_condition_id = ?, " +
                     "severity = ?, diagnosed_at = ?, notes = ?, updated_at = ? WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, userMedicalCondition.getUserId().toString());
            statement.setString(2, userMedicalCondition.getMedicalConditionId().toString());
            statement.setString(3, userMedicalCondition.getSeverity().name());
            statement.setObject(4, userMedicalCondition.getDiagnosedAt());
            statement.setString(5, userMedicalCondition.getNotes());
            statement.setObject(6, LocalDateTime.now());
            statement.setString(7, userMedicalCondition.getId().toString());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró UserMedicalCondition con ID: " + userMedicalCondition.getId());
            }
            
            // Actualizar el objeto local
            userMedicalCondition.setUpdatedAt(LocalDateTime.now());
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar UserMedicalCondition: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM user_medical_condition WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, id.toString());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró UserMedicalCondition con ID: " + id);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar UserMedicalCondition: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<UserMedicalCondition> findByUserId(UUID userId) {
        List<UserMedicalCondition> conditions = new ArrayList<>();
        String sql = "SELECT * FROM user_medical_condition WHERE user_id = ? ORDER BY diagnosed_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, userId.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    conditions.add(mapResultSetToUserMedicalCondition(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar UserMedicalConditions por userId: " + e.getMessage(), e);
        }
        
        return conditions;
    }
    
    @Override
    public List<UserMedicalCondition> findByUserIdWithSeverity(UUID userId, MedicalConditionSeverity severity) {
        List<UserMedicalCondition> conditions = new ArrayList<>();
        String sql = "SELECT * FROM user_medical_condition WHERE user_id = ? AND severity = ? ORDER BY diagnosed_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, userId.toString());
            statement.setString(2, severity.name());
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    conditions.add(mapResultSetToUserMedicalCondition(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar UserMedicalConditions por userId y severidad: " + e.getMessage(), e);
        }
        
        return conditions;
    }
    
    @Override
    public List<UserMedicalCondition> findByUserIdDiagnosedAfter(UUID userId, LocalDateTime date) {
        List<UserMedicalCondition> conditions = new ArrayList<>();
        String sql = "SELECT * FROM user_medical_condition WHERE user_id = ? AND diagnosed_at > ? ORDER BY diagnosed_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, userId.toString());
            statement.setObject(2, date);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    conditions.add(mapResultSetToUserMedicalCondition(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar UserMedicalConditions diagnosticadas después de fecha: " + e.getMessage(), e);
        }
        
        return conditions;
    }
    
    @Override
    public List<UserMedicalCondition> findByUserIdDiagnosedBefore(UUID userId, LocalDateTime date) {
        List<UserMedicalCondition> conditions = new ArrayList<>();
        String sql = "SELECT * FROM user_medical_condition WHERE user_id = ? AND diagnosed_at < ? ORDER BY diagnosed_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, userId.toString());
            statement.setObject(2, date);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    conditions.add(mapResultSetToUserMedicalCondition(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar UserMedicalConditions diagnosticadas antes de fecha: " + e.getMessage(), e);
        }
        
        return conditions;
    }
    
    @Override
    public List<UserMedicalCondition> findByMedicalConditionId(UUID medicalConditionId) {
        List<UserMedicalCondition> conditions = new ArrayList<>();
        String sql = "SELECT * FROM user_medical_condition WHERE medical_condition_id = ? ORDER BY diagnosed_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, medicalConditionId.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    conditions.add(mapResultSetToUserMedicalCondition(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar UserMedicalConditions por medicalConditionId: " + e.getMessage(), e);
        }
        
        return conditions;
    }
    
    @Override
    public List<UserMedicalCondition> findByMedicalConditionIdWithSeverity(UUID medicalConditionId, MedicalConditionSeverity severity) {
        List<UserMedicalCondition> conditions = new ArrayList<>();
        String sql = "SELECT * FROM user_medical_condition WHERE medical_condition_id = ? AND severity = ? ORDER BY diagnosed_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, medicalConditionId.toString());
            statement.setString(2, severity.name());
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    conditions.add(mapResultSetToUserMedicalCondition(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar UserMedicalConditions por medicalConditionId y severidad: " + e.getMessage(), e);
        }
        
        return conditions;
    }
    
    @Override
    public List<UserMedicalCondition> findByUserIdAndMedicalConditionId(UUID userId, UUID medicalConditionId) {
        List<UserMedicalCondition> conditions = new ArrayList<>();
        String sql = "SELECT * FROM user_medical_condition WHERE user_id = ? AND medical_condition_id = ? ORDER BY diagnosed_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, userId.toString());
            statement.setString(2, medicalConditionId.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    conditions.add(mapResultSetToUserMedicalCondition(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar UserMedicalConditions por userId y medicalConditionId: " + e.getMessage(), e);
        }
        
        return conditions;
    }
    
    @Override
    public UserMedicalCondition findLatestByUserIdAndMedicalConditionId(UUID userId, UUID medicalConditionId) {
        String sql = """
            SELECT * FROM user_medical_condition WHERE user_id = ? AND medical_condition_id = ?
            ORDER BY diagnosed_at DESC LIMIT 1
        """; 
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, userId.toString());
            statement.setString(2, medicalConditionId.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUserMedicalCondition(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar último UserMedicalCondition: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM user_medical_condition WHERE id = ?";
        
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
            throw new RuntimeException("Error al verificar existencia de UserMedicalCondition: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public boolean existsByUserIdAndMedicalConditionId(UUID userId, UUID medicalConditionId) {
        String sql = "SELECT COUNT(*) FROM user_medical_condition WHERE user_id = ? AND medical_condition_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, userId.toString());
            statement.setString(2, medicalConditionId.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia de UserMedicalCondition: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public int countByUserId(UUID userId) {
        String sql = "SELECT COUNT(*) FROM user_medical_condition WHERE user_id = ?";
        
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
            throw new RuntimeException("Error al contar UserMedicalConditions por usuario: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public int countByMedicalConditionId(UUID medicalConditionId) {
        String sql = "SELECT COUNT(*) FROM user_medical_condition WHERE medical_condition_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, medicalConditionId.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar UserMedicalConditions por condición médica: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public int countBySeverity(String severity) {
        String sql = "SELECT COUNT(*) FROM user_medical_condition WHERE severity = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, severity);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar UserMedicalConditions por severidad: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public void deleteByUserId(UUID userId) {
        String sql = "DELETE FROM user_medical_condition WHERE user_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, userId.toString());
            statement.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar UserMedicalConditions por userId: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void deleteByMedicalConditionId(UUID medicalConditionId) {
        String sql = "DELETE FROM user_medical_condition WHERE medical_condition_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, medicalConditionId.toString());
            statement.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar UserMedicalConditions por medicalConditionId: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void deleteByUserIdAndMedicalConditionId(UUID userId, UUID medicalConditionId) {
        String sql = "DELETE FROM user_medical_condition WHERE user_id = ? AND medical_condition_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, userId.toString());
            statement.setString(2, medicalConditionId.toString());
            statement.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar UserMedicalConditions por userId y medicalConditionId: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void saveWithValidation(UserMedicalCondition userMedicalCondition) {
        // Validaciones
        if (userMedicalCondition.getUserId() == null) {
            throw new IllegalArgumentException("El userId no puede ser null");
        }
        
        if (userMedicalCondition.getMedicalConditionId() == null) {
            throw new IllegalArgumentException("El medicalConditionId no puede ser null");
        }
        
        if (!userMedicalCondition.isValidSeverity()) {
            throw new IllegalArgumentException("Severidad inválida. Valores permitidos: MILD, MODERATE, SEVERE, CRITICAL");
        }
        
        if (userMedicalCondition.getDiagnosedAt() == null) {
            userMedicalCondition.setDiagnosedAt(LocalDateTime.now());
        }
        
        // Verificar si ya existe una condición médica duplicada para este usuario
        if (existsByUserIdAndMedicalConditionId(userMedicalCondition.getUserId(), 
                                                userMedicalCondition.getMedicalConditionId())) {
            throw new IllegalArgumentException("El usuario ya tiene registrada esta condición médica");
        }
        
        // Guardar
        save(userMedicalCondition);
    }
    
    @Override
    public void updateWithValidation(UserMedicalCondition userMedicalCondition) {
        // Validaciones
        if (userMedicalCondition.getId() == null) {
            throw new IllegalArgumentException("El ID no puede ser null para actualizar");
        }
        
        if (userMedicalCondition.getUserId() == null) {
            throw new IllegalArgumentException("El userId no puede ser null");
        }
        
        if (userMedicalCondition.getMedicalConditionId() == null) {
            throw new IllegalArgumentException("El medicalConditionId no puede ser null");
        }
        
        if (!userMedicalCondition.isValidSeverity()) {
            throw new IllegalArgumentException("Severidad inválida. Valores permitidos: MILD, MODERATE, SEVERE, CRITICAL");
        }
        
        // Actualizar
        update(userMedicalCondition);
    }
    
    @Override
    public List<UserMedicalCondition> findCriticalConditionsByUser(UUID userId) {
        return findByUserIdWithSeverity(userId, MedicalConditionSeverity.CRITICAL);
    }
    
    @Override
    public List<UserMedicalCondition> findAllWithNotes() {
        List<UserMedicalCondition> conditions = new ArrayList<>();
        String sql = "SELECT * FROM user_medical_condition WHERE notes IS NOT NULL AND notes != '' ORDER BY diagnosed_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                conditions.add(mapResultSetToUserMedicalCondition(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar UserMedicalConditions con notas: " + e.getMessage(), e);
        }
        
        return conditions;
    }
    
    @Override
    public List<UserMedicalCondition> findAllOrderByDiagnosedAtDesc() {
        List<UserMedicalCondition> conditions = new ArrayList<>();
        String sql = "SELECT * FROM user_medical_condition ORDER BY diagnosed_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                conditions.add(mapResultSetToUserMedicalCondition(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar UserMedicalConditions ordenadas por fecha de diagnóstico: " + e.getMessage(), e);
        }
        
        return conditions;
    }
    
    // Método auxiliar para mapear ResultSet a UserMedicalCondition
    private UserMedicalCondition mapResultSetToUserMedicalCondition(ResultSet rs) throws SQLException {
        UserMedicalCondition condition = new UserMedicalCondition(
            UUID.fromString(rs.getString("id")),
            UUID.fromString(rs.getString("user_id")),
            UUID.fromString(rs.getString("medical_condition_id")),
            MedicalConditionSeverity.valueOf(rs.getString("severity")),
            rs.getTimestamp("diagnosed_at").toLocalDateTime(),
            rs.getString("notes"),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("updated_at").toLocalDateTime()
        );

        return condition;
    }
}