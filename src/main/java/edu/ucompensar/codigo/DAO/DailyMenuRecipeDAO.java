package edu.ucompensar.codigo.DAO;

import edu.ucompensar.codigo.database.DatabaseConnection;
import edu.ucompensar.codigo.entity.DailyMenuRecipe;
import edu.ucompensar.codigo.model.enums.DailyMenuSlot;
import edu.ucompensar.codigo.model.interfaces.IDailyMenuRecipeDAO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DailyMenuRecipeDAO implements IDailyMenuRecipeDAO {

    @Override
    public void save(DailyMenuRecipe dailyMenuRecipe) {
        String sql = "INSERT INTO daily_menu_recipe (id, daily_menu_id, recipe_id, slot, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        
        DatabaseConnection.getInstance();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            if (dailyMenuRecipe.getId() == null) {
                dailyMenuRecipe.setId(UUID.randomUUID());
            }
            
            LocalDateTime now = LocalDateTime.now();
            if (dailyMenuRecipe.getCreatedAt() == null) dailyMenuRecipe.setCreatedAt(now);
            if (dailyMenuRecipe.getUpdatedAt() == null) dailyMenuRecipe.setUpdatedAt(now);
            
            stmt.setObject(1, dailyMenuRecipe.getId());
            stmt.setObject(2, dailyMenuRecipe.getDailyMenuId());
            stmt.setObject(3, dailyMenuRecipe.getRecipeId());
            stmt.setString(4, dailyMenuRecipe.getSlot().name());
            stmt.setObject(5, dailyMenuRecipe.getCreatedAt());
            stmt.setObject(6, dailyMenuRecipe.getUpdatedAt());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar DailyMenuRecipe: " + e.getMessage(), e);
        }
    }

    @Override
    public DailyMenuRecipe findById(UUID id) {
        String sql = "SELECT * FROM daily_menu_recipe WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar DailyMenuRecipe por ID: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<DailyMenuRecipe> findAll() {
        List<DailyMenuRecipe> list = new ArrayList<>();
        String sql = "SELECT * FROM daily_menu_recipe ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar DailyMenuRecipes: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public void update(DailyMenuRecipe dailyMenuRecipe) {
        String sql = "UPDATE daily_menu_recipe SET daily_menu_id = ?, recipe_id = ?, slot = ?, updated_at = ? WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setObject(1, dailyMenuRecipe.getDailyMenuId());
            stmt.setObject(2, dailyMenuRecipe.getRecipeId());
            stmt.setString(3, dailyMenuRecipe.getSlot().name());
            stmt.setObject(4, LocalDateTime.now());
            stmt.setObject(5, dailyMenuRecipe.getId());
            
            stmt.executeUpdate();
            dailyMenuRecipe.setUpdatedAt(LocalDateTime.now());
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar DailyMenuRecipe: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM daily_menu_recipe WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar DailyMenuRecipe: " + e.getMessage(), e);
        }
    }

    @Override
    public List<DailyMenuRecipe> findByDailyMenuId(UUID dailyMenuId) {
        List<DailyMenuRecipe> list = new ArrayList<>();
        String sql = "SELECT * FROM daily_menu_recipe WHERE daily_menu_id = ?";
        
        DatabaseConnection.getInstance();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setObject(1, dailyMenuId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar DailyMenuRecipes por dailyMenuId: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<DailyMenuRecipe> findByRecipeId(UUID recipeId) {
        List<DailyMenuRecipe> list = new ArrayList<>();
        String sql = "SELECT * FROM daily_menu_recipe WHERE recipe_id = ?";
        
        DatabaseConnection.getInstance();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setObject(1, recipeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar DailyMenuRecipes por recipeId: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<DailyMenuRecipe> findByDailyMenuIdAndSlot(UUID dailyMenuId, DailyMenuSlot slot) {
        List<DailyMenuRecipe> list = new ArrayList<>();
        String sql = "SELECT * FROM daily_menu_recipe WHERE daily_menu_id = ? AND slot = ?";
        
        DatabaseConnection.getInstance();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setObject(1, dailyMenuId);
            stmt.setString(2, slot.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar DailyMenuRecipes por dailyMenuId y slot: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<DailyMenuRecipe> findBySlot(DailyMenuSlot slot) {
        List<DailyMenuRecipe> list = new ArrayList<>();
        String sql = "SELECT * FROM daily_menu_recipe WHERE slot = ?";
        
        DatabaseConnection.getInstance();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, slot.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar DailyMenuRecipes por slot: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public boolean existsByDailyMenuIdAndRecipeId(UUID dailyMenuId, UUID recipeId) {
        String sql = "SELECT COUNT(*) FROM daily_menu_recipe WHERE daily_menu_id = ? AND recipe_id = ?";
        
        DatabaseConnection.getInstance();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setObject(1, dailyMenuId);
            stmt.setObject(2, recipeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia: " + e.getMessage(), e);
        }
        return false;
    }

    @Override
    public void deleteByDailyMenuId(UUID dailyMenuId) {
        String sql = "DELETE FROM daily_menu_recipe WHERE daily_menu_id = ?";
        
        DatabaseConnection.getInstance();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setObject(1, dailyMenuId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar DailyMenuRecipes por dailyMenuId: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteByRecipeId(UUID recipeId) {
        String sql = "DELETE FROM daily_menu_recipe WHERE recipe_id = ?";
        
        DatabaseConnection.getInstance();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setObject(1, recipeId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar DailyMenuRecipes por recipeId: " + e.getMessage(), e);
        }
    }

    @Override
    public int countByDailyMenuId(UUID dailyMenuId) {
        String sql = "SELECT COUNT(*) FROM daily_menu_recipe WHERE daily_menu_id = ?";
        
        DatabaseConnection.getInstance();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setObject(1, dailyMenuId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar DailyMenuRecipes: " + e.getMessage(), e);
        }
        return 0;
    }

    private DailyMenuRecipe mapResultSet(ResultSet rs) throws SQLException {
        DailyMenuRecipe dmr = new DailyMenuRecipe(
            UUID.fromString(rs.getString("id")),
            UUID.fromString(rs.getString("daily_menu_id")),
            UUID.fromString(rs.getString("recipe_id")),
            DailyMenuSlot.valueOf(rs.getString("slot")),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("updated_at").toLocalDateTime()
        );

        return dmr;
    }
}