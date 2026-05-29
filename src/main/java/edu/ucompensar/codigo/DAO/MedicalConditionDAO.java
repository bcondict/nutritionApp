package edu.ucompensar.codigo.DAO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.database.DatabaseConnection;
import edu.ucompensar.codigo.entity.MedicalCondition;
import edu.ucompensar.codigo.model.interfaces.IMedicalConditionDAO;

public class MedicalConditionDAO implements IMedicalConditionDAO {
    
    @Override
    public void save(MedicalCondition medicalCondition) {
        String sql = """
            INSERT INTO medical_condition (id, code, label, description, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            if (medicalCondition.getId() == null) {
                medicalCondition.setId(UUID.randomUUID());
            }
            
            LocalDateTime now = LocalDateTime.now();
            if (medicalCondition.getCreatedAt() == null) {
                medicalCondition.setCreatedAt(now);
            }
            if (medicalCondition.getUpdatedAt() == null) {
                medicalCondition.setUpdatedAt(now);
            }
            
            statement.setObject(1, medicalCondition.getId());
            statement.setString(2, medicalCondition.getCode());
            statement.setString(3, medicalCondition.getLabel());
            statement.setString(4, medicalCondition.getDescription());
            statement.setObject(5, medicalCondition.getCreatedAt());
            statement.setObject(6, medicalCondition.getUpdatedAt());
            
            statement.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar MedicalCondition: " + e.getMessage(), e);
        }
    }
    
    @Override
    public MedicalCondition findById(UUID id) {
        String sql = "SELECT * FROM medical_condition WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, id);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMedicalCondition(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar MedicalCondition por ID: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public List<MedicalCondition> findAll() {
        List<MedicalCondition> conditions = new ArrayList<>();
        String sql = "SELECT * FROM medical_condition ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                conditions.add(mapResultSetToMedicalCondition(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar todos los MedicalConditions: " + e.getMessage(), e);
        }
        
        return conditions;
    }
    
    @Override
    public void update(MedicalCondition medicalCondition) {
        String sql = "UPDATE medical_condition SET code = ?, label = ?, description = ?, updated_at = ? WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, medicalCondition.getCode());
            statement.setString(2, medicalCondition.getLabel());
            statement.setString(3, medicalCondition.getDescription());
            statement.setObject(4, LocalDateTime.now());
            statement.setObject(5, medicalCondition.getId());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró MedicalCondition con ID: " + medicalCondition.getId());
            }
            
            // Actualizar el objeto local
            medicalCondition.setUpdatedAt(LocalDateTime.now());
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar MedicalCondition: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM medical_condition WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, id);
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró MedicalCondition con ID: " + id);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar MedicalCondition: " + e.getMessage(), e);
        }
    }
    
    @Override
    public MedicalCondition findByCode(String code) {
        String sql = "SELECT * FROM medical_condition WHERE code = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, code);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMedicalCondition(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar MedicalCondition por código: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public List<MedicalCondition> findByLabelContaining(String label) {
        List<MedicalCondition> conditions = new ArrayList<>();
        String sql = "SELECT * FROM medical_condition WHERE LOWER(label) LIKE LOWER(?) ORDER BY label";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, "%" + label + "%");
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    conditions.add(mapResultSetToMedicalCondition(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar MedicalCondition por label: " + e.getMessage(), e);
        }
        
        return conditions;
    }
    
    @Override
    public List<MedicalCondition> findByDescriptionContaining(String keyword) {
        List<MedicalCondition> conditions = new ArrayList<>();
        String sql = "SELECT * FROM medical_condition WHERE LOWER(description) LIKE LOWER(?) ORDER BY code";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, "%" + keyword + "%");
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    conditions.add(mapResultSetToMedicalCondition(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar MedicalCondition por descripción: " + e.getMessage(), e);
        }
        
        return conditions;
    }
    
    @Override
    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM medical_condition WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, id);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia de MedicalCondition: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public boolean existsByCode(String code) {
        String sql = "SELECT COUNT(*) FROM medical_condition WHERE code = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, code);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia de código: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM medical_condition";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar MedicalConditions: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public void deleteByCode(String code) {
        String sql = "DELETE FROM medical_condition WHERE code = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, code);
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró MedicalCondition con código: " + code);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar MedicalCondition por código: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void saveWithValidation(MedicalCondition medicalCondition) {
        // Validaciones
        if (!medicalCondition.isValidCode()) {
            throw new IllegalArgumentException("El código no puede estar vacío");
        }
        
        if (!medicalCondition.isValidLabel()) {
            throw new IllegalArgumentException("La etiqueta no puede estar vacía");
        }
        
        // Verificar si ya existe un código duplicado
        if (existsByCode(medicalCondition.getCode())) {
            throw new IllegalArgumentException("Ya existe una condición médica con el código: " + medicalCondition.getCode());
        }
        
        // Guardar
        save(medicalCondition);
    }
    
    @Override
    public void updateWithValidation(MedicalCondition medicalCondition) {
        // Validaciones
        if (medicalCondition.getId() == null) {
            throw new IllegalArgumentException("El ID no puede ser null para actualizar");
        }
        
        if (!medicalCondition.isValidCode()) {
            throw new IllegalArgumentException("El código no puede estar vacío");
        }
        
        if (!medicalCondition.isValidLabel()) {
            throw new IllegalArgumentException("La etiqueta no puede estar vacía");
        }
        
        // Verificar si existe otro registro con el mismo código
        MedicalCondition existing = findByCode(medicalCondition.getCode());
        if (existing != null && !existing.getId().equals(medicalCondition.getId())) {
            throw new IllegalArgumentException("Ya existe otra condición médica con el código: " + medicalCondition.getCode());
        }
        
        // Actualizar
        update(medicalCondition);
    }
    
    @Override
    public List<MedicalCondition> findAllOrderByCode() {
        List<MedicalCondition> conditions = new ArrayList<>();
        String sql = "SELECT * FROM medical_condition ORDER BY code";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                conditions.add(mapResultSetToMedicalCondition(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar MedicalConditions ordenadas por código: " + e.getMessage(), e);
        }
        
        return conditions;
    }
    
    @Override
    public List<MedicalCondition> findAllOrderByLabel() {
        List<MedicalCondition> conditions = new ArrayList<>();
        String sql = "SELECT * FROM medical_condition ORDER BY label";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                conditions.add(mapResultSetToMedicalCondition(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar MedicalConditions ordenadas por etiqueta: " + e.getMessage(), e);
        }
        
        return conditions;
    }
    
    // Método auxiliar para mapear ResultSet a MedicalCondition
    private MedicalCondition mapResultSetToMedicalCondition(ResultSet rs) throws SQLException {
        MedicalCondition condition = new MedicalCondition();
        
        condition.setId((UUID) rs.getObject("id"));
        condition.setCode(rs.getString("code"));
        condition.setLabel(rs.getString("label"));
        condition.setDescription(rs.getString("description"));
        
        // Manejo de LocalDateTime
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            condition.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            condition.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return condition;
    }
}