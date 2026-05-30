package edu.ucompensar.codigo.DAO;

import edu.ucompensar.codigo.database.DatabaseConnection;
import edu.ucompensar.codigo.entity.DailyMenu;
import edu.ucompensar.codigo.model.interfaces.IDailyMenuDAO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DailyMenuDAO implements IDailyMenuDAO {

    @Override
    public void save(DailyMenu dailyMenu) {
        String sql = "INSERT INTO daily_menu (id, weekly_menu_id, day_of_week, total_calories, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        
        DatabaseConnection.getInstance();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            if (dailyMenu.getId() == null) {
                dailyMenu.setId(UUID.randomUUID());
            }
            
            LocalDateTime now = LocalDateTime.now();
            if (dailyMenu.getCreatedAt() == null) dailyMenu.setCreatedAt(now);
            if (dailyMenu.getUpdatedAt() == null) dailyMenu.setUpdatedAt(now);
            
            stmt.setObject(1, dailyMenu.getId());
            stmt.setObject(2, dailyMenu.getWeeklyMenuId());
            stmt.setString(3, dailyMenu.getDayOfWeek());
            stmt.setBigDecimal(4, dailyMenu.getTotalCalories());
            stmt.setObject(5, dailyMenu.getCreatedAt());
            stmt.setObject(6, dailyMenu.getUpdatedAt());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar DailyMenu: " + e.getMessage(), e);
        }
    }

    @Override
    public DailyMenu findById(UUID id) {
        String sql = "SELECT * FROM daily_menu WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar DailyMenu por ID: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<DailyMenu> findAll() {
        List<DailyMenu> list = new ArrayList<>();
        String sql = "SELECT * FROM daily_menu ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar DailyMenus: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public void update(DailyMenu dailyMenu) {
        String sql = "UPDATE daily_menu SET weekly_menu_id = ?, day_of_week = ?, total_calories = ?, updated_at = ? WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setObject(1, dailyMenu.getWeeklyMenuId());
            stmt.setString(2, dailyMenu.getDayOfWeek());
            stmt.setBigDecimal(3, dailyMenu.getTotalCalories());
            stmt.setObject(4, LocalDateTime.now());
            stmt.setObject(5, dailyMenu.getId());
            
            stmt.executeUpdate();
            dailyMenu.setUpdatedAt(LocalDateTime.now());
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar DailyMenu: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM daily_menu WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar DailyMenu: " + e.getMessage(), e);
        }
    }

    @Override
    public List<DailyMenu> findByWeeklyMenuId(UUID weeklyMenuId) {
        List<DailyMenu> list = new ArrayList<>();
        String sql = "SELECT * FROM daily_menu WHERE weekly_menu_id = ? ORDER BY FIELD(day_of_week, 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY')";
        
        DatabaseConnection.getInstance();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setObject(1, weeklyMenuId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar DailyMenus por weeklyMenuId: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public DailyMenu findByWeeklyMenuIdAndDayOfWeek(UUID weeklyMenuId, String dayOfWeek) {
        String sql = "SELECT * FROM daily_menu WHERE weekly_menu_id = ? AND day_of_week = ?";
        
        DatabaseConnection.getInstance();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setObject(1, weeklyMenuId);
            stmt.setString(2, dayOfWeek);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar DailyMenu por weeklyMenuId y dayOfWeek: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<DailyMenu> findByDayOfWeek(String dayOfWeek) {
        List<DailyMenu> list = new ArrayList<>();
        String sql = "SELECT * FROM daily_menu WHERE day_of_week = ?";
        
        DatabaseConnection.getInstance();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, dayOfWeek);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar DailyMenus por dayOfWeek: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public boolean existsByWeeklyMenuIdAndDayOfWeek(UUID weeklyMenuId, String dayOfWeek) {
        String sql = "SELECT COUNT(*) FROM daily_menu WHERE weekly_menu_id = ? AND day_of_week = ?";
        
        DatabaseConnection.getInstance();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setObject(1, weeklyMenuId);
            stmt.setString(2, dayOfWeek);
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
    public void deleteByWeeklyMenuId(UUID weeklyMenuId) {
        String sql = "DELETE FROM daily_menu WHERE weekly_menu_id = ?";
        
        DatabaseConnection.getInstance();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setObject(1, weeklyMenuId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar DailyMenus por weeklyMenuId: " + e.getMessage(), e);
        }
    }

    @Override
    public int countByWeeklyMenuId(UUID weeklyMenuId) {
        String sql = "SELECT COUNT(*) FROM daily_menu WHERE weekly_menu_id = ?";
        
        DatabaseConnection.getInstance();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setObject(1, weeklyMenuId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar DailyMenus: " + e.getMessage(), e);
        }
        return 0;
    }

    private DailyMenu mapResultSet(ResultSet rs) throws SQLException {
        DailyMenu dm = new DailyMenu(
                UUID.fromString(rs.getString("id")),
                UUID.fromString(rs.getString("weekly_menu_id")),
                rs.getString("day_of_week"),
                rs.getBigDecimal("total_calories"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime()
        );
        
        return dm;
    }
}