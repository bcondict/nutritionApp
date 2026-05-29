package edu.ucompensar.codigo.DAO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.database.DatabaseConnection;
import edu.ucompensar.codigo.entity.UserDietaryPreference;
import edu.ucompensar.codigo.model.interfaces.IUserDietaryPreferenceDAO;

public class UserDietaryPreferenceDAO implements IUserDietaryPreferenceDAO {
    
    @Override
    public void save(UserDietaryPreference userDietaryPreference) {
        String sql = """
            INSERT INTO user_dietary_preference (
                id,
                user_id,
                dietary_preference_id,
                created_at,
                updated_at
            ) VALUES (?, ?, ?, ?, ?)""";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            if (userDietaryPreference.getId() == null) {
                userDietaryPreference.setId(UUID.randomUUID());
            }
            
            LocalDateTime now = LocalDateTime.now();
            if (userDietaryPreference.getCreatedAt() == null) {
                userDietaryPreference.setCreatedAt(now);
            }
            if (userDietaryPreference.getUpdatedAt() == null) {
                userDietaryPreference.setUpdatedAt(now);
            }
            
            statement.setObject(1, userDietaryPreference.getId());
            statement.setObject(2, userDietaryPreference.getUserId());
            statement.setObject(3, userDietaryPreference.getDietaryPreferenceId());
            statement.setObject(4, userDietaryPreference.getCreatedAt());
            statement.setObject(5, userDietaryPreference.getUpdatedAt());
            
            statement.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar UserDietaryPreference: " + e.getMessage(), e);
        }
    }
    
    @Override
    public UserDietaryPreference findById(UUID id) {
        String sql = "SELECT * FROM user_dietary_preference WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, id);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUserDietaryPreference(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar UserDietaryPreference por ID: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public List<UserDietaryPreference> findAll() {
        List<UserDietaryPreference> preferences = new ArrayList<>();
        String sql = "SELECT * FROM user_dietary_preference ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                preferences.add(mapResultSetToUserDietaryPreference(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar todos los UserDietaryPreferences: " + e.getMessage(), e);
        }
        
        return preferences;
    }
    
    @Override
    public void update(UserDietaryPreference userDietaryPreference) {
        String sql = """
            UPDATE user_dietary_preference
            SET user_id = ?,
                dietary_preference_id = ?,
                updated_at = ?
            WHERE id = ?
        """;
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, userDietaryPreference.getUserId());
            statement.setObject(2, userDietaryPreference.getDietaryPreferenceId());
            statement.setObject(3, LocalDateTime.now());
            statement.setObject(4, userDietaryPreference.getId());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró UserDietaryPreference con ID: " + userDietaryPreference.getId());
            }
            
            // Actualizar el objeto local
            userDietaryPreference.setUpdatedAt(LocalDateTime.now());
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar UserDietaryPreference: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM user_dietary_preference WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, id);
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró UserDietaryPreference con ID: " + id);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar UserDietaryPreference: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<UserDietaryPreference> findByUserId(UUID userId) {
        List<UserDietaryPreference> preferences = new ArrayList<>();
        String sql = "SELECT * FROM user_dietary_preference WHERE user_id = ? ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, userId);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    preferences.add(mapResultSetToUserDietaryPreference(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar UserDietaryPreferences por userId: " + e.getMessage(), e);
        }
        
        return preferences;
    }
    
    @Override
    public List<UserDietaryPreference> findByUserIdWithDetails(UUID userId) {
        List<UserDietaryPreference> preferences = new ArrayList<>();
        String sql = """
            SELECT udp.*, dp.code, dp.label, dp.category, dp.description 
            FROM user_dietary_preference udp JOIN dietary_preference dp
            ON udp.dietary_preference_id = dp.id 
            WHERE udp.user_id = ? ORDER BY udp.created_at DESC
        """;
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, userId);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    UserDietaryPreference pref = mapResultSetToUserDietaryPreference(rs);
                    // Aquí podrías agregar información adicional del DietaryPreference
                    preferences.add(pref);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar UserDietaryPreferences con detalles: " + e.getMessage(), e);
        }
        
        return preferences;
    }
    
    @Override
    public List<UUID> findDietaryPreferenceIdsByUserId(UUID userId) {
        List<UUID> preferenceIds = new ArrayList<>();
        String sql = "SELECT dietary_preference_id FROM user_dietary_preference WHERE user_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, userId);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    preferenceIds.add((UUID) rs.getObject("dietary_preference_id"));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener IDs de preferencias por userId: " + e.getMessage(), e);
        }
        
        return preferenceIds;
    }
    
    @Override
    public List<UserDietaryPreference> findByDietaryPreferenceId(UUID dietaryPreferenceId) {
        List<UserDietaryPreference> preferences = new ArrayList<>();
        String sql = "SELECT * FROM user_dietary_preference WHERE dietary_preference_id = ? ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, dietaryPreferenceId);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    preferences.add(mapResultSetToUserDietaryPreference(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar UserDietaryPreferences por dietaryPreferenceId: " + e.getMessage(), e);
        }
        
        return preferences;
    }
    
    @Override
    public List<UUID> findUserIdsByDietaryPreferenceId(UUID dietaryPreferenceId) {
        List<UUID> userIds = new ArrayList<>();
        String sql = "SELECT user_id FROM user_dietary_preference WHERE dietary_preference_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, dietaryPreferenceId);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    userIds.add((UUID) rs.getObject("user_id"));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener userIds por dietaryPreferenceId: " + e.getMessage(), e);
        }
        
        return userIds;
    }
    
    @Override
    public UserDietaryPreference findByUserIdAndDietaryPreferenceId(UUID userId, UUID dietaryPreferenceId) {
        String sql = "SELECT * FROM user_dietary_preference WHERE user_id = ? AND dietary_preference_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, userId);
            statement.setObject(2, dietaryPreferenceId);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUserDietaryPreference(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar UserDietaryPreference por userId y dietaryPreferenceId: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public boolean existsByUserIdAndDietaryPreferenceId(UUID userId, UUID dietaryPreferenceId) {
        String sql = "SELECT COUNT(*) FROM user_dietary_preference WHERE user_id = ? AND dietary_preference_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, userId);
            statement.setObject(2, dietaryPreferenceId);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia de UserDietaryPreference: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public int countByUserId(UUID userId) {
        String sql = "SELECT COUNT(*) FROM user_dietary_preference WHERE user_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, userId);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar preferencias por usuario: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public int countByDietaryPreferenceId(UUID dietaryPreferenceId) {
        String sql = "SELECT COUNT(*) FROM user_dietary_preference WHERE dietary_preference_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, dietaryPreferenceId);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar usuarios por preferencia: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public List<UUID> findUsersWithMultiplePreferences() {
        List<UUID> userIds = new ArrayList<>();
        String sql = """
            SELECT user_id, COUNT(*) as pref_count
            FROM user_dietary_preference 
            GROUP BY user_id
            HAVING COUNT(*) > 1
            ORDER BY pref_count DESC
        """;
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                userIds.add((UUID) rs.getObject("user_id"));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuarios con múltiples preferencias: " + e.getMessage(), e);
        }
        
        return userIds;
    }
    
    @Override
    public List<UUID> findUsersWithPreferenceCount(int minCount) {
        List<UUID> userIds = new ArrayList<>();
        String sql = """
            SELECT user_id, COUNT(*) as pref_count
            FROM user_dietary_preference 
            GROUP BY user_id
            HAVING COUNT(*) >= ?
            ORDER BY pref_count DESC
        """;
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, minCount);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    userIds.add((UUID) rs.getObject("user_id"));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuarios con mínimo de preferencias: " + e.getMessage(), e);
        }
        
        return userIds;
    }
    
    @Override
    public void deleteByUserId(UUID userId) {
        String sql = "DELETE FROM user_dietary_preference WHERE user_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, userId);
            
            int deletedCount = statement.executeUpdate();
            System.out.println("Preferencias eliminadas para usuario " + userId + ": " + deletedCount);
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar UserDietaryPreferences por userId: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void deleteByDietaryPreferenceId(UUID dietaryPreferenceId) {
        String sql = "DELETE FROM user_dietary_preference WHERE dietary_preference_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, dietaryPreferenceId);
            
            int deletedCount = statement.executeUpdate();
            System.out.println("Preferencias eliminadas para dietaryPreferenceId " + dietaryPreferenceId + ": " + deletedCount);
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar UserDietaryPreferences por dietaryPreferenceId: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void deleteByUserIdAndDietaryPreferenceId(UUID userId, UUID dietaryPreferenceId) {
        String sql = "DELETE FROM user_dietary_preference WHERE user_id = ? AND dietary_preference_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, userId);
            statement.setObject(2, dietaryPreferenceId);
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró UserDietaryPreference con userId: " + userId + " y dietaryPreferenceId: " + dietaryPreferenceId);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar UserDietaryPreference: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void saveWithValidation(UserDietaryPreference userDietaryPreference) {
        // Validaciones
        if (!userDietaryPreference.hasValidReferences()) {
            throw new IllegalArgumentException("userId y dietaryPreferenceId no pueden ser null");
        }
        
        // Verificar si ya existe la relación
        if (existsByUserIdAndDietaryPreferenceId(userDietaryPreference.getUserId(), userDietaryPreference.getDietaryPreferenceId())) {
            throw new IllegalArgumentException("El usuario ya tiene asociada esta preferencia dietética");
        }
        
        // Guardar
        save(userDietaryPreference);
    }
    
    @Override
    public void updateWithValidation(UserDietaryPreference userDietaryPreference) {
        // Validaciones
        if (userDietaryPreference.getId() == null) {
            throw new IllegalArgumentException("El ID no puede ser null para actualizar");
        }
        
        if (!userDietaryPreference.hasValidReferences()) {
            throw new IllegalArgumentException("userId y dietaryPreferenceId no pueden ser null");
        }
        
        // Actualizar
        update(userDietaryPreference);
    }
    
    @Override
    public void saveAll(List<UserDietaryPreference> userPreferences) {
        if (userPreferences == null || userPreferences.isEmpty()) {
            return;
        }
        
        String sql = """
            INSERT INTO user_dietary_preference (
                id,
                user_id,
                dietary_preference_id,
                created_at,
                updated_at
            ) VALUES (?, ?, ?, ?, ?)
        """;
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            connection.setAutoCommit(false);
            
            LocalDateTime now = LocalDateTime.now();
            
            for (UserDietaryPreference pref : userPreferences) {
                if (pref.getId() == null) {
                    pref.setId(UUID.randomUUID());
                }
                if (pref.getCreatedAt() == null) {
                    pref.setCreatedAt(now);
                }
                if (pref.getUpdatedAt() == null) {
                    pref.setUpdatedAt(now);
                }
                
                statement.setObject(1, pref.getId());
                statement.setObject(2, pref.getUserId());
                statement.setObject(3, pref.getDietaryPreferenceId());
                statement.setObject(4, pref.getCreatedAt());
                statement.setObject(5, pref.getUpdatedAt());
                
                statement.addBatch();
            }
            
            statement.executeBatch();
            connection.commit();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar múltiples UserDietaryPreferences: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void deleteAllByUserId(UUID userId) {
        deleteByUserId(userId);
    }
    
    @Override
    public List<UserDietaryPreference> findAllOrderByCreatedAtDesc() {
        List<UserDietaryPreference> preferences = new ArrayList<>();
        String sql = "SELECT * FROM user_dietary_preference ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                preferences.add(mapResultSetToUserDietaryPreference(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar preferencias ordenadas: " + e.getMessage(), e);
        }
        
        return preferences;
    }
    
    @Override
    public List<UserDietaryPreference> findUserPreferencesCreatedAfter(LocalDateTime date) {
        List<UserDietaryPreference> preferences = new ArrayList<>();
        String sql = "SELECT * FROM user_dietary_preference WHERE created_at > ? ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, date);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    preferences.add(mapResultSetToUserDietaryPreference(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar preferencias creadas después de: " + e.getMessage(), e);
        }
        
        return preferences;
    }
    
    @Override
    public List<UserDietaryPreference> findUserPreferencesCreatedBefore(LocalDateTime date) {
        List<UserDietaryPreference> preferences = new ArrayList<>();
        String sql = "SELECT * FROM user_dietary_preference WHERE created_at < ? ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, date);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    preferences.add(mapResultSetToUserDietaryPreference(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar preferencias creadas antes de: " + e.getMessage(), e);
        }
        
        return preferences;
    }
    
    @Override
    public List<UserDietaryPreference> findUserPreferencesByUserIdOrderByCreatedAt(UUID userId) {
        List<UserDietaryPreference> preferences = new ArrayList<>();
        String sql = "SELECT * FROM user_dietary_preference WHERE user_id = ? ORDER BY created_at ASC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, userId);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    preferences.add(mapResultSetToUserDietaryPreference(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar preferencias por userId ordenadas: " + e.getMessage(), e);
        }
        
        return preferences;
    }
    
    // Método auxiliar para mapear ResultSet a UserDietaryPreference
    private UserDietaryPreference mapResultSetToUserDietaryPreference(ResultSet rs) throws SQLException {
        UserDietaryPreference preference = new UserDietaryPreference();
        
        preference.setId((UUID) rs.getObject("id"));
        preference.setUserId((UUID) rs.getObject("user_id"));
        preference.setDietaryPreferenceId((UUID) rs.getObject("dietary_preference_id"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            preference.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            preference.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return preference;
    }
}