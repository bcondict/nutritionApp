package edu.ucompensar.codigo.DAO;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.database.DatabaseConnection;
import edu.ucompensar.codigo.entity.WeeklyMenu;
import edu.ucompensar.codigo.model.interfaces.IWeeklyMenuDAO;

public class WeeklyMenuDAO implements IWeeklyMenuDAO {
    
    @Override
    public void save(WeeklyMenu weeklyMenu) {
        String sql = """
            INSERT INTO weekly_menu (
                id,
                nutrition_plan_id,
                week_start,
                week_end,
                generated_at,
                created_at,
                updated_at
            )
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            if (weeklyMenu.getId() == null) {
                weeklyMenu.setId(UUID.randomUUID());
            }
            
            LocalDateTime now = LocalDateTime.now();
            if (weeklyMenu.getCreatedAt() == null) {
                weeklyMenu.setCreatedAt(now);
            }
            if (weeklyMenu.getUpdatedAt() == null) {
                weeklyMenu.setUpdatedAt(now);
            }
            if (weeklyMenu.getGeneratedAt() == null) {
                weeklyMenu.setGeneratedAt(now);
            }
            
            statement.setString(1, weeklyMenu.getId().toString());
            statement.setString(2, weeklyMenu.getNutritionPlanId().toString());
            statement.setObject(3, weeklyMenu.getWeekStart());
            statement.setObject(4, weeklyMenu.getWeekEnd());
            statement.setObject(5, weeklyMenu.getGeneratedAt());
            statement.setObject(6, weeklyMenu.getCreatedAt());
            statement.setObject(7, weeklyMenu.getUpdatedAt());
            
            statement.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar WeeklyMenu: " + e.getMessage(), e);
        }
    }
    
    @Override
    public WeeklyMenu findById(UUID id) {
        String sql = "SELECT * FROM weekly_menu WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, id.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToWeeklyMenu(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar WeeklyMenu por ID: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public List<WeeklyMenu> findAll() {
        List<WeeklyMenu> menus = new ArrayList<>();
        String sql = "SELECT * FROM weekly_menu ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                menus.add(mapResultSetToWeeklyMenu(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar todos los WeeklyMenus: " + e.getMessage(), e);
        }
        
        return menus;
    }
    
    @Override
    public void update(WeeklyMenu weeklyMenu) {
        String sql = """
            UPDATE weekly_menu
            SET nutrition_plan_id = ?,
                week_start = ?,
                week_end = ?,
                generated_at = ?,
                updated_at = ?
            WHERE id = ?
        """;
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, weeklyMenu.getNutritionPlanId().toString());
            statement.setObject(2, weeklyMenu.getWeekStart());
            statement.setObject(3, weeklyMenu.getWeekEnd());
            statement.setObject(4, weeklyMenu.getGeneratedAt());
            statement.setObject(5, LocalDateTime.now());
            statement.setString(6, weeklyMenu.getId().toString());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró WeeklyMenu con ID: " + weeklyMenu.getId());
            }
            
            // Actualizar el objeto local
            weeklyMenu.setUpdatedAt(LocalDateTime.now());
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar WeeklyMenu: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM weekly_menu WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, id.toString());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró WeeklyMenu con ID: " + id);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar WeeklyMenu: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<WeeklyMenu> findByNutritionPlanId(UUID nutritionPlanId) {
        List<WeeklyMenu> menus = new ArrayList<>();
        String sql = "SELECT * FROM weekly_menu WHERE nutrition_plan_id = ? ORDER BY week_start DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, nutritionPlanId.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    menus.add(mapResultSetToWeeklyMenu(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar WeeklyMenus por nutritionPlanId: " + e.getMessage(), e);
        }
        
        return menus;
    }
    
    @Override
    public List<WeeklyMenu> findByNutritionPlanIdOrderByWeekStart(UUID nutritionPlanId) {
        List<WeeklyMenu> menus = new ArrayList<>();
        String sql = "SELECT * FROM weekly_menu WHERE nutrition_plan_id = ? ORDER BY week_start ASC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, nutritionPlanId.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    menus.add(mapResultSetToWeeklyMenu(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar WeeklyMenos ordenados por semana inicio: " + e.getMessage(), e);
        }
        
        return menus;
    }
    
    @Override
    public WeeklyMenu findCurrentWeekMenuByNutritionPlanId(UUID nutritionPlanId) {
        LocalDate today = LocalDate.now();
        String sql = "SELECT * FROM weekly_menu WHERE nutrition_plan_id = ? AND week_start <= ? AND week_end >= ? LIMIT 1";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, nutritionPlanId.toString());
            statement.setObject(2, today);
            statement.setObject(3, today);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToWeeklyMenu(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar WeeklyMenu actual por nutritionPlanId: " + e.getMessage(), e);
        }
        
        return null;
    }
    

    @Override
    public List<WeeklyMenu> findByNutritionPlanIdAndDateRange(UUID nutritionPlanId, LocalDate startDate, LocalDate endDate) {
        List<WeeklyMenu> menus = new ArrayList<>();
        String sql = "SELECT * FROM weekly_menu WHERE nutrition_plan_id = ? AND week_start BETWEEN ? AND ? ORDER BY week_start ASC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, nutritionPlanId.toString());
            statement.setObject(2, startDate);
            statement.setObject(3, endDate);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    menus.add(mapResultSetToWeeklyMenu(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar WeeklyMenus por rango de fechas: " + e.getMessage(), e);
        }
        
        return menus;
    }
    
    @Override
    public List<WeeklyMenu> findByWeekStart(LocalDate weekStart) {
        List<WeeklyMenu> menus = new ArrayList<>();
        String sql = "SELECT * FROM weekly_menu WHERE week_start = ? ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, weekStart);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    menus.add(mapResultSetToWeeklyMenu(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar WeeklyMenus por weekStart: " + e.getMessage(), e);
        }
        
        return menus;
    }
    
    @Override
    public List<WeeklyMenu> findByWeekEnd(LocalDate weekEnd) {
        List<WeeklyMenu> menus = new ArrayList<>();
        String sql = "SELECT * FROM weekly_menu WHERE week_end = ? ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, weekEnd);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    menus.add(mapResultSetToWeeklyMenu(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar WeeklyMenus por weekEnd: " + e.getMessage(), e);
        }
        
        return menus;
    }
    
    @Override
    public List<WeeklyMenu> findByWeekStartBetween(LocalDate startDate, LocalDate endDate) {
        List<WeeklyMenu> menus = new ArrayList<>();
        String sql = "SELECT * FROM weekly_menu WHERE week_start BETWEEN ? AND ? ORDER BY week_start ASC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, startDate);
            statement.setObject(2, endDate);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    menus.add(mapResultSetToWeeklyMenu(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar WeeklyMenus por rango de weekStart: " + e.getMessage(), e);
        }
        
        return menus;
    }
    
    @Override
    public List<WeeklyMenu> findCurrentWeekMenus() {
        List<WeeklyMenu> menus = new ArrayList<>();
        LocalDate today = LocalDate.now();
        String sql = "SELECT * FROM weekly_menu WHERE week_start <= ? AND week_end >= ? ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, today);
            statement.setObject(2, today);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    menus.add(mapResultSetToWeeklyMenu(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar WeeklyMenus actuales: " + e.getMessage(), e);
        }
        
        return menus;
    }
    
    @Override
    public List<WeeklyMenu> findFutureWeekMenus() {
        List<WeeklyMenu> menus = new ArrayList<>();
        LocalDate today = LocalDate.now();
        String sql = "SELECT * FROM weekly_menu WHERE week_start > ? ORDER BY week_start ASC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, today);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    menus.add(mapResultSetToWeeklyMenu(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar WeeklyMenus futuros: " + e.getMessage(), e);
        }
        
        return menus;
    }
    
    @Override
    public List<WeeklyMenu> findPastWeekMenus() {
        List<WeeklyMenu> menus = new ArrayList<>();
        LocalDate today = LocalDate.now();
        String sql = "SELECT * FROM weekly_menu WHERE week_end < ? ORDER BY week_start DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, today);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    menus.add(mapResultSetToWeeklyMenu(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar WeeklyMenus pasados: " + e.getMessage(), e);
        }
        
        return menus;
    }
    
    @Override
    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM weekly_menu WHERE id = ?";
        
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
            throw new RuntimeException("Error al verificar existencia de WeeklyMenu: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public boolean existsByNutritionPlanIdAndWeekStart(UUID nutritionPlanId, LocalDate weekStart) {
        String sql = "SELECT COUNT(*) FROM weekly_menu WHERE nutrition_plan_id = ? AND week_start = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, nutritionPlanId.toString());
            statement.setObject(2, weekStart);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia de WeeklyMenu por plan y fecha: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public boolean existsByNutritionPlanIdAndDateRange(UUID nutritionPlanId, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COUNT(*) FROM weekly_menu WHERE nutrition_plan_id = ? AND week_start BETWEEN ? AND ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, nutritionPlanId.toString());
            statement.setObject(2, startDate);
            statement.setObject(3, endDate);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia de WeeklyMenu por rango: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public int countByNutritionPlanId(UUID nutritionPlanId) {
        String sql = "SELECT COUNT(*) FROM weekly_menu WHERE nutrition_plan_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, nutritionPlanId.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar WeeklyMenus por plan nutricional: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public int countByNutritionPlanIdAndYear(UUID nutritionPlanId, int year) {
        String sql = "SELECT COUNT(*) FROM weekly_menu WHERE nutrition_plan_id = ? AND YEAR(week_start) = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, nutritionPlanId.toString());
            statement.setInt(2, year);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar WeeklyMenus por año: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public void deleteByNutritionPlanId(UUID nutritionPlanId) {
        String sql = "DELETE FROM weekly_menu WHERE nutrition_plan_id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, nutritionPlanId.toString());
            statement.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar WeeklyMenus por nutritionPlanId: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void deleteOldMenus(LocalDate beforeDate) {
        String sql = "DELETE FROM weekly_menu WHERE week_end < ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, beforeDate);
            
            int deletedCount = statement.executeUpdate();
            System.out.println("Menús semanales antiguos eliminados: " + deletedCount);
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar menús antiguos: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void deleteMenusByDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = "DELETE FROM weekly_menu WHERE week_start BETWEEN ? AND ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, startDate);
            statement.setObject(2, endDate);
            
            int deletedCount = statement.executeUpdate();
            System.out.println("Menús eliminados en el rango de fechas: " + deletedCount);
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar menús por rango de fechas: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void saveWithValidation(WeeklyMenu weeklyMenu) {
        // Validaciones
        if (weeklyMenu.getNutritionPlanId() == null) {
            throw new IllegalArgumentException("El nutritionPlanId no puede ser null");
        }
        
        if (weeklyMenu.getWeekStart() == null) {
            throw new IllegalArgumentException("La fecha de inicio de semana no puede ser null");
        }
        
        if (weeklyMenu.getWeekEnd() == null) {
            throw new IllegalArgumentException("La fecha de fin de semana no puede ser null");
        }
        
        if (!weeklyMenu.isValidWeekRange()) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
        
        if (!weeklyMenu.isSevenDayWeek()) {
            throw new IllegalArgumentException("El período debe ser exactamente de 7 días");
        }
        
        // Verificar si ya existe un menú para la misma semana
        if (existsByNutritionPlanIdAndWeekStart(weeklyMenu.getNutritionPlanId(), weeklyMenu.getWeekStart())) {
            throw new IllegalArgumentException("Ya existe un menú semanal para esta fecha de inicio");
        }
        
        // Verificar si hay solapamiento con otros menús
        if (hasOverlappingWeeks(weeklyMenu)) {
            throw new IllegalArgumentException("El rango de fechas se solapa con otro menú existente");
        }
        
        // Guardar
        save(weeklyMenu);
    }
    
    @Override
    public void updateWithValidation(WeeklyMenu weeklyMenu) {
        // Validaciones
        if (weeklyMenu.getId() == null) {
            throw new IllegalArgumentException("El ID no puede ser null para actualizar");
        }
        
        if (weeklyMenu.getNutritionPlanId() == null) {
            throw new IllegalArgumentException("El nutritionPlanId no puede ser null");
        }
        
        if (weeklyMenu.getWeekStart() == null) {
            throw new IllegalArgumentException("La fecha de inicio de semana no puede ser null");
        }
        
        if (weeklyMenu.getWeekEnd() == null) {
            throw new IllegalArgumentException("La fecha de fin de semana no puede ser null");
        }
        
        if (!weeklyMenu.isValidWeekRange()) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
        
        if (!weeklyMenu.isSevenDayWeek()) {
            throw new IllegalArgumentException("El período debe ser exactamente de 7 días");
        }
        
        // Actualizar
        update(weeklyMenu);
    }
    
    @Override
    public WeeklyMenu generateWeeklyMenuForDate(UUID nutritionPlanId, LocalDate date) {
        // Calcular el inicio de la semana (Lunes)
        LocalDate weekStart = date;
        while (weekStart.getDayOfWeek().getValue() != 1) { // 1 = Lunes
            weekStart = weekStart.minusDays(1);
        }
        
        // Calcular el fin de la semana (Domingo)
        LocalDate weekEnd = weekStart.plusDays(6);
        
        // Crear el menú semanal
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        WeeklyMenu weeklyMenu = new WeeklyMenu(
            id,
            nutritionPlanId,
            weekStart,
            weekEnd,
            now,
            now,
            now
        );
        return weeklyMenu;
    }
    
    @Override
    public List<WeeklyMenu> generateAllWeeklyMenusForPlan(UUID nutritionPlanId, LocalDate startDate, int numberOfWeeks) {
        List<WeeklyMenu> menus = new ArrayList<>();
        LocalDate currentDate = startDate;
        
        for (int i = 0; i < numberOfWeeks; i++) {
            WeeklyMenu menu = generateWeeklyMenuForDate(nutritionPlanId, currentDate);
            
            // Solo guardar si no existe ya
            if (!existsByNutritionPlanIdAndWeekStart(nutritionPlanId, menu.getWeekStart())) {
                saveWithValidation(menu);
                menus.add(menu);
            }
            
            // Avanzar a la siguiente semana
            currentDate = currentDate.plusWeeks(1);
        }
        
        return menus;
    }
    
    @Override
    public boolean hasOverlappingWeeks(WeeklyMenu weeklyMenu) {
        String sql = """
            SELECT COUNT(*) FROM weekly_menu
            WHERE nutrition_plan_id = ?
            AND (
                (week_start BETWEEN ? AND ?) OR
                (week_end BETWEEN ? AND ?) OR
                (week_start <= ? AND week_end >= ?)
            )
        """;
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, weeklyMenu.getNutritionPlanId().toString());
            statement.setObject(2, weeklyMenu.getWeekStart());
            statement.setObject(3, weeklyMenu.getWeekEnd());
            statement.setObject(4, weeklyMenu.getWeekStart());
            statement.setObject(5, weeklyMenu.getWeekEnd());
            statement.setObject(6, weeklyMenu.getWeekStart());
            statement.setObject(7, weeklyMenu.getWeekEnd());
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar solapamiento de semanas: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public List<WeeklyMenu> findAllOrderByWeekStartDesc() {
        List<WeeklyMenu> menus = new ArrayList<>();
        String sql = "SELECT * FROM weekly_menu ORDER BY week_start DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                menus.add(mapResultSetToWeeklyMenu(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar WeeklyMenus ordenados por semana inicio DESC: " + e.getMessage(), e);
        }
        
        return menus;
    }
    
    @Override
    public List<WeeklyMenu> findMenusGeneratedAfter(LocalDateTime date) {
        List<WeeklyMenu> menus = new ArrayList<>();
        String sql = "SELECT * FROM weekly_menu WHERE generated_at > ? ORDER BY generated_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, date);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    menus.add(mapResultSetToWeeklyMenu(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar menús generados después de fecha: " + e.getMessage(), e);
        }
        
        return menus;
    }
    
    @Override
    public List<WeeklyMenu> findMenusGeneratedBefore(LocalDateTime date) {
        List<WeeklyMenu> menus = new ArrayList<>();
        String sql = "SELECT * FROM weekly_menu WHERE generated_at < ? ORDER BY generated_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, date);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    menus.add(mapResultSetToWeeklyMenu(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar menús generados antes de fecha: " + e.getMessage(), e);
        }
        
        return menus;
    }
    
    // Método auxiliar para mapear ResultSet a WeeklyMenu
    private WeeklyMenu mapResultSetToWeeklyMenu(ResultSet rs) throws SQLException {
        WeeklyMenu menu = new WeeklyMenu(
            UUID.fromString(rs.getString("id")),
            UUID.fromString(rs.getString("nutrition_plan_id")),
            rs.getDate("week_start").toLocalDate(),
            rs.getDate("week_end").toLocalDate(),
            rs.getTimestamp("generated_at").toLocalDateTime(),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("updated_at").toLocalDateTime()
        );

        return menu;
    }
}