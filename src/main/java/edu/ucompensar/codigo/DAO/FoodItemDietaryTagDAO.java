package edu.ucompensar.codigo.DAO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.database.DatabaseConnection;
import edu.ucompensar.codigo.entity.FoodItemDietaryTag;
import edu.ucompensar.codigo.model.interfaces.IFoodItemDietaryTagDAO;

public class FoodItemDietaryTagDAO implements IFoodItemDietaryTagDAO {
    
    @Override
    public void save(FoodItemDietaryTag foodItemDietaryTag) {
        String sql = """
            INSERT INTO food_item_dietary_tag (
                id,
                food_item_id,
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
            if (foodItemDietaryTag.getId() == null) {
                foodItemDietaryTag.setId(UUID.randomUUID());
            }
            
            LocalDateTime now = LocalDateTime.now();
            if (foodItemDietaryTag.getCreatedAt() == null) {
                foodItemDietaryTag.setCreatedAt(now);
            }
            if (foodItemDietaryTag.getUpdatedAt() == null) {
                foodItemDietaryTag.setUpdatedAt(now);
            }
            
            statement.setObject(1, foodItemDietaryTag.getId());
            statement.setObject(2, foodItemDietaryTag.getFoodItemId());
            statement.setObject(3, foodItemDietaryTag.getDietaryPreferenceId());
            statement.setObject(4, foodItemDietaryTag.getCreatedAt());
            statement.setObject(5, foodItemDietaryTag.getUpdatedAt());
            
            statement.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar FoodItemDietaryTag: " + e.getMessage(), e);
        }
    }
    
    @Override
    public FoodItemDietaryTag findById(UUID id) {
        String sql = "SELECT * FROM food_item_dietary_tag WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, id);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFoodItemDietaryTag(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar FoodItemDietaryTag por ID: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public List<FoodItemDietaryTag> findAll() {
        List<FoodItemDietaryTag> tags = new ArrayList<>();
        String sql = "SELECT * FROM food_item_dietary_tag ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                tags.add(mapResultSetToFoodItemDietaryTag(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar todos los FoodItemDietaryTags: " + e.getMessage(), e);
        }
        
        return tags;
    }
    
    @Override
    public void update(FoodItemDietaryTag foodItemDietaryTag) {
        String sql = """
            UPDATE food_item_dietary_tag
            SET food_item_id = ?,
                dietary_preference_id = ?,
                updated_at = ?
            WHERE id = ?
        """;
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, foodItemDietaryTag.getFoodItemId());
            statement.setObject(2, foodItemDietaryTag.getDietaryPreferenceId());
            statement.setObject(3, LocalDateTime.now());
            statement.setObject(4, foodItemDietaryTag.getId());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró FoodItemDietaryTag con ID: " + foodItemDietaryTag.getId());
            }
            
            // Actualizar el objeto local
            foodItemDietaryTag.setUpdatedAt(LocalDateTime.now());
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar FoodItemDietaryTag: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM food_item_dietary_tag WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, id);
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró FoodItemDietaryTag con ID: " + id);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar FoodItemDietaryTag: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<FoodItemDietaryTag> findByFoodItemId(UUID foodItemId) {
        List<FoodItemDietaryTag> tags = new ArrayList<>();
        String sql = "SELECT * FROM food_item_dietary_tag WHERE food_item_id = ? ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, foodItemId);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    tags.add(mapResultSetToFoodItemDietaryTag(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar FoodItemDietaryTags por foodItemId: " + e.getMessage(), e);
        }
        
        return tags;
    }
    
    @Override
    public List<FoodItemDietaryTag> findByFoodItemIdWithDetails(UUID foodItemId) {
        List<FoodItemDietaryTag> tags = new ArrayList<>();
        String sql = """
            SELECT fit.*, dp.code, dp.label, dp.category, dp.description
            FROM food_item_dietary_tag fit JOIN dietary_preference dp
            ON fit.dietary_preference_id = dp.id
            WHERE fit.food_item_id = ? ORDER BY fit.created_at DESC
        """;
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, foodItemId);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    FoodItemDietaryTag tag = mapResultSetToFoodItemDietaryTag(rs);
                    // Aquí podrías agregar información adicional del DietaryPreference
                    tags.add(tag);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar FoodItemDietaryTags con detalles: " + e.getMessage(), e);
        }
        
        return tags;
    }
    
    @Override
    public List<UUID> findDietaryPreferenceIdsByFoodItemId(UUID foodItemId) {
        List<UUID> preferenceIds = new ArrayList<>();
        String sql = "SELECT dietary_preference_id FROM food_item_dietary_tag WHERE food_item_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, foodItemId);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    preferenceIds.add((UUID) rs.getObject("dietary_preference_id"));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener IDs de preferencias por foodItemId: " + e.getMessage(), e);
        }
        
        return preferenceIds;
    }
    
    @Override
    public List<FoodItemDietaryTag> findByDietaryPreferenceId(UUID dietaryPreferenceId) {
        List<FoodItemDietaryTag> tags = new ArrayList<>();
        String sql = "SELECT * FROM food_item_dietary_tag WHERE dietary_preference_id = ? ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, dietaryPreferenceId);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    tags.add(mapResultSetToFoodItemDietaryTag(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar FoodItemDietaryTags por dietaryPreferenceId: " + e.getMessage(), e);
        }
        
        return tags;
    }
    
    @Override
    public List<FoodItemDietaryTag> findByDietaryPreferenceIdWithDetails(UUID dietaryPreferenceId) {
        List<FoodItemDietaryTag> tags = new ArrayList<>();
        String sql = """
            SELECT fit.*, fi.name as food_name, fi.category as food_category
            FROM food_item_dietary_tag fit JOIN food_item fi
            ON fit.food_item_id = fi.id
            WHERE fit.dietary_preference_id = ?
            ORDER BY fit.created_at DESC
        """;

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, dietaryPreferenceId);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    FoodItemDietaryTag tag = mapResultSetToFoodItemDietaryTag(rs);
                    // Aquí podrías agregar información adicional del FoodItem
                    tags.add(tag);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar FoodItemDietaryTags con detalles: " + e.getMessage(), e);
        }
        
        return tags;
    }
    
    @Override
    public List<UUID> findFoodItemIdsByDietaryPreferenceId(UUID dietaryPreferenceId) {
        List<UUID> foodItemIds = new ArrayList<>();
        String sql = "SELECT food_item_id FROM food_item_dietary_tag WHERE dietary_preference_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, dietaryPreferenceId);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    foodItemIds.add((UUID) rs.getObject("food_item_id"));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener foodItemIds por dietaryPreferenceId: " + e.getMessage(), e);
        }
        
        return foodItemIds;
    }
    
    @Override
    public FoodItemDietaryTag findByFoodItemIdAndDietaryPreferenceId(UUID foodItemId, UUID dietaryPreferenceId) {
        String sql = "SELECT * FROM food_item_dietary_tag WHERE food_item_id = ? AND dietary_preference_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, foodItemId);
            statement.setObject(2, dietaryPreferenceId);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFoodItemDietaryTag(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar FoodItemDietaryTag por foodItemId y dietaryPreferenceId: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public boolean existsByFoodItemIdAndDietaryPreferenceId(UUID foodItemId, UUID dietaryPreferenceId) {
        String sql = "SELECT COUNT(*) FROM food_item_dietary_tag WHERE food_item_id = ? AND dietary_preference_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, foodItemId);
            statement.setObject(2, dietaryPreferenceId);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia de FoodItemDietaryTag: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public List<FoodItemDietaryTag> findFoodItemsByDietaryPreferenceCode(String code) {
        List<FoodItemDietaryTag> tags = new ArrayList<>();
        String sql = """
            SELECT fit.*
            FROM food_item_dietary_tag fit JOIN dietary_preference dp
            ON fit.dietary_preference_id = dp.id
            WHERE dp.code = ? ORDER BY fit.created_at DESC
        """;
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, code);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    tags.add(mapResultSetToFoodItemDietaryTag(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar FoodItems por código de preferencia: " + e.getMessage(), e);
        }
        
        return tags;
    }
    
    @Override
    public List<FoodItemDietaryTag> findFoodItemsByDietaryPreferenceLabel(String label) {
        List<FoodItemDietaryTag> tags = new ArrayList<>();
        String sql = """
            SELECT fit.*
            FROM food_item_dietary_tag fit JOIN dietary_preference dp
            ON fit.dietary_preference_id = dp.id
            WHERE dp.label = ?
            ORDER BY fit.created_at DESC
        """;
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, label);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    tags.add(mapResultSetToFoodItemDietaryTag(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar FoodItems por etiqueta de preferencia: " + e.getMessage(), e);
        }
        
        return tags;
    }
    
    @Override
    public int countByFoodItemId(UUID foodItemId) {
        String sql = "SELECT COUNT(*) FROM food_item_dietary_tag WHERE food_item_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, foodItemId);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar tags por foodItemId: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public int countByDietaryPreferenceId(UUID dietaryPreferenceId) {
        String sql = "SELECT COUNT(*) FROM food_item_dietary_tag WHERE dietary_preference_id = ?";
        
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
            throw new RuntimeException("Error al contar tags por dietaryPreferenceId: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public List<FoodItemDietaryTag> findFoodItemsWithMultipleTags() {
        List<FoodItemDietaryTag> tags = new ArrayList<>();
        String sql = """
            SELECT fit.* FROM food_item_dietary_tag fit
            WHERE fit.food_item_id IN (
                SELECT food_item_id
                FROM food_item_dietary_tag
                GROUP BY food_item_id
                HAVING COUNT(*) > 1
            )
            ORDER BY fit.food_item_id, fit.created_at DESC
        """;
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                tags.add(mapResultSetToFoodItemDietaryTag(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar alimentos con múltiples tags: " + e.getMessage(), e);
        }
        
        return tags;
    }
    
    @Override
    public List<FoodItemDietaryTag> findFoodItemsWithTagCount(int minCount) {
        List<FoodItemDietaryTag> tags = new ArrayList<>();
        String sql = """
            SELECT fit.* FROM food_item_dietary_tag fit
            WHERE fit.food_item_id IN (
                SELECT food_item_id FROM food_item_dietary_tag
                GROUP BY food_item_id
                HAVING COUNT(*) >= ?
            )
            ORDER BY fit.food_item_id, fit.created_at DESC
        """;
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, minCount);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    tags.add(mapResultSetToFoodItemDietaryTag(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar alimentos con mínimo de tags: " + e.getMessage(), e);
        }
        
        return tags;
    }
    
    @Override
    public void deleteByFoodItemId(UUID foodItemId) {
        String sql = "DELETE FROM food_item_dietary_tag WHERE food_item_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, foodItemId);
            
            int deletedCount = statement.executeUpdate();
            System.out.println("Tags eliminados para foodItem " + foodItemId + ": " + deletedCount);
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar FoodItemDietaryTags por foodItemId: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void deleteByDietaryPreferenceId(UUID dietaryPreferenceId) {
        String sql = "DELETE FROM food_item_dietary_tag WHERE dietary_preference_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, dietaryPreferenceId);
            
            int deletedCount = statement.executeUpdate();
            System.out.println("Tags eliminados para dietaryPreference " + dietaryPreferenceId + ": " + deletedCount);
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar FoodItemDietaryTags por dietaryPreferenceId: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void deleteByFoodItemIdAndDietaryPreferenceId(UUID foodItemId, UUID dietaryPreferenceId) {
        String sql = "DELETE FROM food_item_dietary_tag WHERE food_item_id = ? AND dietary_preference_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, foodItemId);
            statement.setObject(2, dietaryPreferenceId);
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró FoodItemDietaryTag con foodItemId: " + foodItemId + " y dietaryPreferenceId: " + dietaryPreferenceId);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar FoodItemDietaryTag: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void saveWithValidation(FoodItemDietaryTag foodItemDietaryTag) {
        // Validaciones
        if (!foodItemDietaryTag.hasValidReferences()) {
            throw new IllegalArgumentException("foodItemId y dietaryPreferenceId no pueden ser null");
        }
        
        // Verificar si ya existe la relación
        if (existsByFoodItemIdAndDietaryPreferenceId(foodItemDietaryTag.getFoodItemId(), foodItemDietaryTag.getDietaryPreferenceId())) {
            throw new IllegalArgumentException("El alimento ya tiene este tag dietético asociado");
        }
        
        // Guardar
        save(foodItemDietaryTag);
    }
    
    @Override
    public void updateWithValidation(FoodItemDietaryTag foodItemDietaryTag) {
        // Validaciones
        if (foodItemDietaryTag.getId() == null) {
            throw new IllegalArgumentException("El ID no puede ser null para actualizar");
        }
        
        if (!foodItemDietaryTag.hasValidReferences()) {
            throw new IllegalArgumentException("foodItemId y dietaryPreferenceId no pueden ser null");
        }
        
        // Actualizar
        update(foodItemDietaryTag);
    }
    
    @Override
    public void saveAll(List<FoodItemDietaryTag> foodItemTags) {
        if (foodItemTags == null || foodItemTags.isEmpty()) {
            return;
        }
        
        String sql = """
            INSERT INTO food_item_dietary_tag (
                id,
                food_item_id,
                dietary_preference_id, created_at,
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
            
            for (FoodItemDietaryTag tag : foodItemTags) {
                if (tag.getId() == null) {
                    tag.setId(UUID.randomUUID());
                }
                if (tag.getCreatedAt() == null) {
                    tag.setCreatedAt(now);
                }
                if (tag.getUpdatedAt() == null) {
                    tag.setUpdatedAt(now);
                }
                
                statement.setObject(1, tag.getId());
                statement.setObject(2, tag.getFoodItemId());
                statement.setObject(3, tag.getDietaryPreferenceId());
                statement.setObject(4, tag.getCreatedAt());
                statement.setObject(5, tag.getUpdatedAt());
                
                statement.addBatch();
            }
            
            statement.executeBatch();
            connection.commit();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar múltiples FoodItemDietaryTags: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void deleteAllByFoodItemId(UUID foodItemId) {
        deleteByFoodItemId(foodItemId);
    }
    
    @Override
    public void addTagsToFoodItem(UUID foodItemId, List<UUID> dietaryPreferenceIds) {
        List<FoodItemDietaryTag> tags = new ArrayList<>();
        for (UUID preferenceId : dietaryPreferenceIds) {
            FoodItemDietaryTag tag = new FoodItemDietaryTag();
            tag.setFoodItemId(foodItemId);
            tag.setDietaryPreferenceId(preferenceId);
            tags.add(tag);
        }
        saveAll(tags);
    }
    
    @Override
    public List<FoodItemDietaryTag> findAllOrderByCreatedAtDesc() {
        List<FoodItemDietaryTag> tags = new ArrayList<>();
        String sql = "SELECT * FROM food_item_dietary_tag ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                tags.add(mapResultSetToFoodItemDietaryTag(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar tags ordenados: " + e.getMessage(), e);
        }
        
        return tags;
    }
    
    @Override
    public List<FoodItemDietaryTag> findTagsCreatedAfter(LocalDateTime date) {
        List<FoodItemDietaryTag> tags = new ArrayList<>();
        String sql = "SELECT * FROM food_item_dietary_tag WHERE created_at > ? ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, date);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    tags.add(mapResultSetToFoodItemDietaryTag(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar tags creados después de: " + e.getMessage(), e);
        }
        
        return tags;
    }
    
    @Override
    public List<FoodItemDietaryTag> findTagsCreatedBefore(LocalDateTime date) {
        List<FoodItemDietaryTag> tags = new ArrayList<>();
        String sql = "SELECT * FROM food_item_dietary_tag WHERE created_at < ? ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, date);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    tags.add(mapResultSetToFoodItemDietaryTag(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar tags creados antes de: " + e.getMessage(), e);
        }
        
        return tags;
    }
    
    @Override
    public List<FoodItemDietaryTag> findTagsByFoodItemIdOrderByCreatedAt(UUID foodItemId) {
        List<FoodItemDietaryTag> tags = new ArrayList<>();
        String sql = "SELECT * FROM food_item_dietary_tag WHERE food_item_id = ? ORDER BY created_at ASC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, foodItemId);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    tags.add(mapResultSetToFoodItemDietaryTag(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar tags por foodItemId ordenados: " + e.getMessage(), e);
        }
        
        return tags;
    }
    
    // Método auxiliar para mapear ResultSet a FoodItemDietaryTag
    private FoodItemDietaryTag mapResultSetToFoodItemDietaryTag(ResultSet rs) throws SQLException {
        FoodItemDietaryTag tag = new FoodItemDietaryTag();
        
        tag.setId((UUID) rs.getObject("id"));
        tag.setFoodItemId((UUID) rs.getObject("food_item_id"));
        tag.setDietaryPreferenceId((UUID) rs.getObject("dietary_preference_id"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            tag.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            tag.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return tag;
    }
}