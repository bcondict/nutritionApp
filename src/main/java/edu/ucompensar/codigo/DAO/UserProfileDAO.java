package edu.ucompensar.codigo.DAO;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.database.DatabaseConnection;
import edu.ucompensar.codigo.entity.UserProfile;
import edu.ucompensar.codigo.model.enums.ActivityLevel;
import edu.ucompensar.codigo.model.enums.Sex;
import edu.ucompensar.codigo.model.interfaces.IUserProfileDAO;

public class UserProfileDAO implements IUserProfileDAO {
    @Override
    public void save(UserProfile userProfile) {
        String sql = """
            INSERT INTO user_profile (
                id,
                user_id,
                weight_kg,
                height_cm,
                sex,
                activity_level,
                measured_at,
                created_at,
                updated_at
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            
            if (userProfile.getId() == null) {
                userProfile.setId(UUID.randomUUID());
            }
            
            LocalDateTime now = LocalDateTime.now();
            if (userProfile.getCreatedAt() == null) {
                userProfile.setCreatedAt(now);
            }
            if (userProfile.getUpdatedAt() == null) {
                userProfile.setUpdatedAt(now);
            }
            
            statement.setObject(1, userProfile.getId());
            statement.setObject(2, userProfile.getUserId());
            statement.setBigDecimal(3, userProfile.getWeightKg());
            statement.setInt(4, userProfile.getHeightCm());
            statement.setString(5, userProfile.getSex().name());
            statement.setString(6, userProfile.getActivityLevel().name());
            statement.setObject(7, userProfile.getMeasuredAt());
            statement.setObject(8, userProfile.getCreatedAt());
            statement.setObject(9, userProfile.getUpdatedAt());
            
            statement.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar UserProfile: " + e.getMessage(), e);
        }
    }
    
    @Override
    public UserProfile findById(UUID id) {
        String sql = "SELECT * FROM user_profile WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, id);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUserProfile(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar UserProfile por ID: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public List<UserProfile> findAll() {
        List<UserProfile> profiles = new ArrayList<>();
        String sql = "SELECT * FROM user_profile ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ) {
            
            while (rs.next()) {
                profiles.add(mapResultSetToUserProfile(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar todos los UserProfiles: " + e.getMessage(), e);
        }
        
        return profiles;
    }
    
    @Override
    public void update(UserProfile userProfile) {
        String sql = "UPDATE user_profile SET user_id = ?, weight_kg = ?, height_cm = ?, " +
                     "sex = ?, activity_level = ?, measured_at = ?, updated_at = ? " +
                     "WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            
            statement.setObject(1, userProfile.getUserId());
            statement.setBigDecimal(2, userProfile.getWeightKg());
            statement.setInt(3, userProfile.getHeightCm());
            statement.setString(4, userProfile.getSex().name());
            statement.setString(5, userProfile.getActivityLevel().name());
            statement.setObject(6, userProfile.getMeasuredAt());
            statement.setObject(7, LocalDateTime.now()); // Actualizar timestamp
            statement.setObject(8, userProfile.getId());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró UserProfile con ID: " + userProfile.getId());
            }
            
            // Actualizar el objeto local también
            userProfile.setUpdatedAt(LocalDateTime.now());
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar UserProfile: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM user_profile WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, id);
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró UserProfile con ID: " + id);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar UserProfile: " + e.getMessage(), e);
        }
    }
    
    @Override
    public UserProfile findLatestByUserId(UUID userId) {
        String sql = "SELECT * FROM user_profile WHERE user_id = ? ORDER BY measured_at DESC LIMIT 1";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, userId);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUserProfile(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar último UserProfile por userId: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public List<UserProfile> findByUserId(UUID userId) {
        List<UserProfile> profiles = new ArrayList<>();
        String sql = "SELECT * FROM user_profile WHERE user_id = ? ORDER BY measured_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, userId);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    profiles.add(mapResultSetToUserProfile(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar UserProfiles por userId: " + e.getMessage(), e);
        }
        
        return profiles;
    }
    
    @Override
    public List<UserProfile> findByUserIdAndDateRange(UUID userId, LocalDateTime startDate, LocalDateTime endDate) {
        List<UserProfile> profiles = new ArrayList<>();
        String sql = "SELECT * FROM user_profile WHERE user_id = ? AND measured_at BETWEEN ? AND ? ORDER BY measured_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, userId);
            statement.setObject(2, startDate);
            statement.setObject(3, endDate);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    profiles.add(mapResultSetToUserProfile(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar UserProfiles por rango de fechas: " + e.getMessage(), e);
        }
        
        return profiles;
    }
    
    @Override
    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM user_profile WHERE id = ?";
        
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
            throw new RuntimeException("Error al verificar existencia de UserProfile: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public int countByUserId(UUID userId) {
        String sql = "SELECT COUNT(*) FROM user_profile WHERE user_id = ?";
        
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
            throw new RuntimeException("Error al contar UserProfiles por usuario: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public void deleteByUserId(UUID userId) {
        String sql = "DELETE FROM user_profile WHERE user_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, userId);
            statement.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar UserProfiles por userId: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<UserProfile> findProfilesWithBmiBetween(BigDecimal minBmi, BigDecimal maxBmi) {
        // Este método requiere un cálculo en Java ya que BMI no está en la BD
        List<UserProfile> allProfiles = findAll();
        List<UserProfile> filteredProfiles = new ArrayList<>();
        
        for (UserProfile profile : allProfiles) {
            BigDecimal bmi = profile.getBmi();
            if (bmi != null && bmi.compareTo(minBmi) >= 0 && bmi.compareTo(maxBmi) <= 0) {
                filteredProfiles.add(profile);
            }
        }
        
        return filteredProfiles;
    }
    
    @Override
    public List<UserProfile> findProfilesBySex(String sex) {
        List<UserProfile> profiles = new ArrayList<>();
        String sql = "SELECT * FROM user_profile WHERE sex = ? ORDER BY measured_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, sex);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    profiles.add(mapResultSetToUserProfile(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar UserProfiles por sexo: " + e.getMessage(), e);
        }
        
        return profiles;
    }
    
    @Override
    public List<UserProfile> findProfilesByActivityLevel(String activityLevel) {
        List<UserProfile> profiles = new ArrayList<>();
        String sql = "SELECT * FROM user_profile WHERE activity_level = ? ORDER BY measured_at DESC";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, activityLevel);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    profiles.add(mapResultSetToUserProfile(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar UserProfiles por nivel de actividad: " + e.getMessage(), e);
        }
        
        return profiles;
    }

    // Método auxiliar para mapear ResultSet a UserProfile
    private UserProfile mapResultSetToUserProfile(ResultSet rs) throws SQLException {
        UserProfile profile = new UserProfile();
        
        profile.setId((UUID) rs.getObject("id"));
        profile.setUserId((UUID) rs.getObject("user_id"));
        profile.setWeightKg(rs.getBigDecimal("weight_kg"));
        profile.setHeightCm(rs.getInt("height_cm"));
        profile.setSex(Sex.valueOf(rs.getString("sex")));
        profile.setActivityLevel(ActivityLevel.valueOf(rs.getString("activity_level")));
        
        // Manejo de LocalDateTime
        Timestamp measuredAt = rs.getTimestamp("measured_at");
        if (measuredAt != null) {
            profile.setMeasuredAt(measuredAt.toLocalDateTime());
        }
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            profile.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            profile.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return profile;
    }
}