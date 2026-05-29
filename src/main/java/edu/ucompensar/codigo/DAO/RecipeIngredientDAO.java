package edu.ucompensar.codigo.DAO;

import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.database.DatabaseConnection;
import edu.ucompensar.codigo.entity.RecipeIngredient;
import edu.ucompensar.codigo.model.interfaces.IRecipeIngredientDAO;

public class RecipeIngredientDAO implements IRecipeIngredientDAO {
    
    @Override
    public void save(RecipeIngredient recipeIngredient) {
        String sql = """
            INSERT INTO recipe_ingredient (
                id,
                recipe_id,
                food_item_id,
                quantity_g,
                created_at,
                updated_at
            ) VALUES (?, ?, ?, ?, ?, ?)
        """;
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            if (recipeIngredient.getId() == null) {
                recipeIngredient.setId(UUID.randomUUID());
            }
            
            LocalDateTime now = LocalDateTime.now();
            if (recipeIngredient.getCreatedAt() == null) {
                recipeIngredient.setCreatedAt(now);
            }
            if (recipeIngredient.getUpdatedAt() == null) {
                recipeIngredient.setUpdatedAt(now);
            }
            
            statement.setString(1, recipeIngredient.getId().toString());
            statement.setString(2, recipeIngredient.getRecipeId().toString());
            statement.setString(3, recipeIngredient.getFoodItemId().toString());
            statement.setBigDecimal(4, recipeIngredient.getQuantityG());
            statement.setObject(5, recipeIngredient.getCreatedAt());
            statement.setObject(6, recipeIngredient.getUpdatedAt());
            
            statement.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar RecipeIngredient: " + e.getMessage(), e);
        }
    }
    
    @Override
    public RecipeIngredient findById(UUID id) {
        String sql = "SELECT * FROM recipe_ingredient WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, id.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRecipeIngredient(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar RecipeIngredient por ID: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public List<RecipeIngredient> findAll() {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM recipe_ingredient ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                ingredients.add(mapResultSetToRecipeIngredient(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar todos los RecipeIngredients: " + e.getMessage(), e);
        }
        
        return ingredients;
    }
    
    @Override
    public void update(RecipeIngredient recipeIngredient) {
        String sql = """
            UPDATE recipe_ingredient
                SET recipe_id = ?,
                food_item_id = ?,
                quantity_g = ?,
                updated_at = ?
            WHERE id = ?
        """;
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, recipeIngredient.getRecipeId().toString());
            statement.setString(2, recipeIngredient.getFoodItemId().toString());
            statement.setBigDecimal(3, recipeIngredient.getQuantityG());
            statement.setObject(4, LocalDateTime.now());
            statement.setString(5, recipeIngredient.getId().toString());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró RecipeIngredient con ID: " + recipeIngredient.getId());
            }
            
            // Actualizar el objeto local
            recipeIngredient.setUpdatedAt(LocalDateTime.now());
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar RecipeIngredient: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM recipe_ingredient WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, id.toString());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró RecipeIngredient con ID: " + id);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar RecipeIngredient: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<RecipeIngredient> findByRecipeId(UUID recipeId) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM recipe_ingredient WHERE recipe_id = ? ORDER BY created_at ASC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, recipeId.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    ingredients.add(mapResultSetToRecipeIngredient(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar RecipeIngredients por recipeId: " + e.getMessage(), e);
        }
        
        return ingredients;
    }
    
    @Override
    public List<RecipeIngredient> findByRecipeIdWithDetails(UUID recipeId) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        String sql = """
            SELECT
                ri.*,
                fi.name as food_name,
                fi.calories_per_100g,
                fi.protein_per_100g,
                fi.carbs_per_100g,
                fi.fat_per_100g,
                fi.fiber_per_100g,
                fi.sodium_per_100mg
            FROM recipe_ingredient ri JOIN food_item fi
            ON ri.food_item_id = fi.id
            WHERE ri.recipe_id = ? ORDER BY ri.created_at ASC
        """;
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, recipeId.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    RecipeIngredient ingredient = mapResultSetToRecipeIngredient(rs);
                    // Aquí podrías agregar información adicional del FoodItem
                    // como nombre, valores nutricionales, etc.
                    ingredients.add(ingredient);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar RecipeIngredients con detalles: " + e.getMessage(), e);
        }
        
        return ingredients;
    }
    
    @Override
    public List<RecipeIngredient> findByRecipeIdOrderByQuantityDesc(UUID recipeId) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM recipe_ingredient WHERE recipe_id = ? ORDER BY quantity_g DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, recipeId.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    ingredients.add(mapResultSetToRecipeIngredient(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar RecipeIngredients ordenados por cantidad: " + e.getMessage(), e);
        }
        
        return ingredients;
    }
    
    @Override
    public List<RecipeIngredient> findByFoodItemId(UUID foodItemId) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM recipe_ingredient WHERE food_item_id = ? ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, foodItemId.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    ingredients.add(mapResultSetToRecipeIngredient(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar RecipeIngredients por foodItemId: " + e.getMessage(), e);
        }
        
        return ingredients;
    }
    
    @Override
    public List<UUID> findRecipeIdsByFoodItemId(UUID foodItemId) {
        List<UUID> recipeIds = new ArrayList<>();
        String sql = "SELECT DISTINCT recipe_id FROM recipe_ingredient WHERE food_item_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, foodItemId.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    recipeIds.add((UUID) rs.getObject("recipe_id"));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar recipeIds por foodItemId: " + e.getMessage(), e);
        }
        
        return recipeIds;
    }
    
    @Override
    public RecipeIngredient findByRecipeIdAndFoodItemId(UUID recipeId, UUID foodItemId) {
        String sql = "SELECT * FROM recipe_ingredient WHERE recipe_id = ? AND food_item_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, recipeId.toString());
            statement.setString(2, foodItemId.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRecipeIngredient(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar RecipeIngredient por recipeId y foodItemId: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public boolean existsByRecipeIdAndFoodItemId(UUID recipeId, UUID foodItemId) {
        String sql = "SELECT COUNT(*) FROM recipe_ingredient WHERE recipe_id = ? AND food_item_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, recipeId.toString());
            statement.setString(2, foodItemId.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia de RecipeIngredient: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public void deleteByRecipeId(UUID recipeId) {
        String sql = "DELETE FROM recipe_ingredient WHERE recipe_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, recipeId.toString());
            
            int deletedCount = statement.executeUpdate();
            System.out.println("Ingredientes eliminados para receta " + recipeId + ": " + deletedCount);
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar RecipeIngredients por recipeId: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void deleteByFoodItemId(UUID foodItemId) {
        String sql = "DELETE FROM recipe_ingredient WHERE food_item_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, foodItemId.toString());
            
            int deletedCount = statement.executeUpdate();
            System.out.println("Ingredientes eliminados para alimento " + foodItemId + ": " + deletedCount);
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar RecipeIngredients por foodItemId: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void deleteByRecipeIdAndFoodItemId(UUID recipeId, UUID foodItemId) {
        String sql = "DELETE FROM recipe_ingredient WHERE recipe_id = ? AND food_item_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, recipeId.toString());
            statement.setString(2, foodItemId.toString());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException(
                    "No se encontró RecipeIngredient con recipeId: " + recipeId + " y foodItemId: " + foodItemId
                );
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar RecipeIngredient: " + e.getMessage(), e);
        }
    }
    
    @Override
    public int countByRecipeId(UUID recipeId) {
        String sql = "SELECT COUNT(*) FROM recipe_ingredient WHERE recipe_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, recipeId.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar ingredientes por receta: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public int countByFoodItemId(UUID foodItemId) {
        String sql = "SELECT COUNT(*) FROM recipe_ingredient WHERE food_item_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, foodItemId.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar ingredientes por alimento: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public BigDecimal getTotalQuantityByRecipeId(UUID recipeId) {
        String sql = "SELECT SUM(quantity_g) FROM recipe_ingredient WHERE recipe_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, recipeId.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal(1);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al calcular cantidad total de ingredientes: " + e.getMessage(), e);
        }
        
        return BigDecimal.ZERO;
    }
    
    @Override
    public int getIngredientCountByRecipeId(UUID recipeId) {
        return countByRecipeId(recipeId);
    }
    
    @Override
    public void saveWithValidation(RecipeIngredient recipeIngredient) {
        // Validaciones
        if (!recipeIngredient.hasValidReferences()) {
            throw new IllegalArgumentException("recipeId y foodItemId no pueden ser null");
        }
        
        if (!recipeIngredient.isValidQuantity()) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        
        // Verificar si ya existe el mismo ingrediente en la receta
        if (existsByRecipeIdAndFoodItemId(recipeIngredient.getRecipeId(), recipeIngredient.getFoodItemId())) {
            throw new IllegalArgumentException("El alimento ya está agregado a esta receta");
        }
        
        // Guardar
        save(recipeIngredient);
    }
    
    @Override
    public void updateWithValidation(RecipeIngredient recipeIngredient) {
        // Validaciones
        if (recipeIngredient.getId() == null) {
            throw new IllegalArgumentException("El ID no puede ser null para actualizar");
        }
        
        if (!recipeIngredient.hasValidReferences()) {
            throw new IllegalArgumentException("recipeId y foodItemId no pueden ser null");
        }
        
        if (!recipeIngredient.isValidQuantity()) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        
        // Actualizar
        update(recipeIngredient);
    }
    
    @Override
    public void saveAll(List<RecipeIngredient> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            return;
        }
        
        String sql = """
            INSERT INTO recipe_ingredient (
                id,
                recipe_id,
                food_item_id,
                quantity_g,
                created_at,
                updated_at)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            connection.setAutoCommit(false);
            
            LocalDateTime now = LocalDateTime.now();
            
            for (RecipeIngredient ingredient : ingredients) {
                if (ingredient.getId() == null) {
                    ingredient.setId(UUID.randomUUID());
                }
                if (ingredient.getCreatedAt() == null) {
                    ingredient.setCreatedAt(now);
                }
                if (ingredient.getUpdatedAt() == null) {
                    ingredient.setUpdatedAt(now);
                }
                
                statement.setString(1, ingredient.getId().toString());
                statement.setString(2, ingredient.getRecipeId().toString());
                statement.setString(3, ingredient.getFoodItemId().toString());
                statement.setBigDecimal(4, ingredient.getQuantityG());
                statement.setObject(5, ingredient.getCreatedAt());
                statement.setObject(6, ingredient.getUpdatedAt());
                
                statement.addBatch();
            }
            
            statement.executeBatch();
            connection.commit();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar múltiples RecipeIngredients: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void updateQuantitiesByRecipeId(UUID recipeId, BigDecimal multiplier) {
        String sql = "UPDATE recipe_ingredient SET quantity_g = quantity_g * ?, updated_at = ? WHERE recipe_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setBigDecimal(1, multiplier);
            statement.setObject(2, LocalDateTime.now());
            statement.setString(3, recipeId.toString());
            
            int updatedCount = statement.executeUpdate();
            System.out.println("Cantidades actualizadas para " + updatedCount + " ingredientes de la receta " + recipeId);
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar cantidades por receta: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<RecipeIngredient> findAllOrderByCreatedAtDesc() {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM recipe_ingredient ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                ingredients.add(mapResultSetToRecipeIngredient(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar ingredientes ordenados: " + e.getMessage(), e);
        }
        
        return ingredients;
    }
    
    @Override
    public List<RecipeIngredient> findIngredientsCreatedAfter(LocalDateTime date) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM recipe_ingredient WHERE created_at > ? ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, date);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    ingredients.add(mapResultSetToRecipeIngredient(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar ingredientes creados después de: " + e.getMessage(), e);
        }
        
        return ingredients;
    }
    
    @Override
    public List<RecipeIngredient> findIngredientsCreatedBefore(LocalDateTime date) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM recipe_ingredient WHERE created_at < ? ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, date);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    ingredients.add(mapResultSetToRecipeIngredient(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar ingredientes creados antes de: " + e.getMessage(), e);
        }
        
        return ingredients;
    }
    
    @Override
    public List<RecipeIngredient> findIngredientsWithQuantityGreaterThan(BigDecimal minQuantity) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM recipe_ingredient WHERE quantity_g > ? ORDER BY quantity_g DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setBigDecimal(1, minQuantity);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    ingredients.add(mapResultSetToRecipeIngredient(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar ingredientes con cantidad mayor a: " + e.getMessage(), e);
        }
        
        return ingredients;
    }
    
    // Método auxiliar para mapear ResultSet a RecipeIngredient
    private RecipeIngredient mapResultSetToRecipeIngredient(ResultSet rs) throws SQLException {
        RecipeIngredient ingredient = new RecipeIngredient(
            UUID.fromString(rs.getString("id")),
            UUID.fromString(rs.getString("recipe_id")),
            UUID.fromString(rs.getString("food_item_id")),
            rs.getBigDecimal("quantity_g"),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("updated_at").toLocalDateTime()
        );

        return ingredient;
    }
}