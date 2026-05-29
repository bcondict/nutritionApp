package edu.ucompensar.codigo.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.database.DatabaseConnection;
import edu.ucompensar.codigo.entity.NutritionPlan;
import edu.ucompensar.codigo.model.interfaces.INutritionPlanDAO;

public class NutritionPlanDAO implements INutritionPlanDAO {

    @Override
    public void save(NutritionPlan nutritionPlan) {
        String sql = """
            INSERT INTO nutrition_plan (
                id,
                user_id,
                goal_id,
                target_calories,
                target_protein_pct,
                target_carbs_pct,
                target_fat_pct,
                generated_at,
                is_active,
                created_at,
                updated_at
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            // Generar ID si es null
            if (nutritionPlan.getId() == null) {
                nutritionPlan.setId(UUID.randomUUID());
            }
            // Establecer parámetros
            statement.setObject(1, nutritionPlan.getId());
            statement.setObject(2, nutritionPlan.getUserId());
            statement.setObject(3, nutritionPlan.getGoalId());
            statement.setBigDecimal(4, nutritionPlan.getTargetCalories());
            statement.setBigDecimal(5, nutritionPlan.getTargetProteinPct());
            statement.setBigDecimal(6, nutritionPlan.getTargetCarbsPct());
            statement.setBigDecimal(7, nutritionPlan.getTargetFatPct());
            statement.setTimestamp(8, nutritionPlan.getGeneratedAt());
            statement.setBoolean(9, nutritionPlan.isActive());
            statement.setTimestamp(10, nutritionPlan.getCreatedAt());
            statement.setTimestamp(11, nutritionPlan.getUpdatedAt());
            
            statement.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar NutritionPlan: " + e.getMessage(), e);
        }
    }
    
    @Override
    public NutritionPlan findById(UUID id) {
        String sql = "SELECT * FROM nutrition_plan WHERE id = ?";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToNutritionPlan(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar NutritionPlan por ID: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public NutritionPlan findByUserId(UUID userId) {
        String sql = "SELECT * FROM nutrition_plan WHERE user_id = ? ORDER BY generated_at DESC LIMIT 1";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, userId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToNutritionPlan(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar NutritionPlan por UserId: " + e.getMessage(), e);
        }

        return null;
    }
    
    @Override
    public List<NutritionPlan> findAll() {
        List<NutritionPlan> nutritionPlans = new ArrayList<>();
        String sql = "SELECT * FROM nutrition_plan ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    nutritionPlans.add(mapResultSetToNutritionPlan(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar todos los NutritionPlans: " + e.getMessage(), e);
        }
        
        return nutritionPlans;
    }
    
    @Override
    public void update(NutritionPlan nutritionPlan) {
        String sql = """
            UPDATE nutrition_plan
            SET user_id = ?,
                goal_id = ?,
                target_calories = ?,
                target_protein_pct = ?,
                target_carbs_pct = ?,
                target_fat_pct = ?,
                generated_at = ?,
                is_active = ?,
                updated_at = ?
            WHERE id = ?
        """;

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            // Establecer parámetros
            statement.setObject(1, nutritionPlan.getUserId());
            statement.setObject(2, nutritionPlan.getGoalId());
            statement.setBigDecimal(3, nutritionPlan.getTargetCalories());
            statement.setBigDecimal(4, nutritionPlan.getTargetProteinPct());
            statement.setBigDecimal(5, nutritionPlan.getTargetCarbsPct());
            statement.setBigDecimal(6, nutritionPlan.getTargetFatPct());
            statement.setTimestamp(7, nutritionPlan.getGeneratedAt());
            statement.setBoolean(8, nutritionPlan.isActive());
            statement.setTimestamp(9, new Timestamp(System.currentTimeMillis())); // Actualizar timestamp
            statement.setObject(10, nutritionPlan.getId());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró NutritionPlan con ID: " + nutritionPlan.getId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar NutritionPlan: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM nutrition_plan WHERE id = ?";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró NutritionPlan con ID: " + id);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar NutritionPlan: " + e.getMessage(), e);
        }
    }

    // Método auxiliar para mapear ResultSet a objeto NutritionPlan
    private NutritionPlan mapResultSetToNutritionPlan(ResultSet rs) throws SQLException {
        NutritionPlan plan = new NutritionPlan();

        plan.setId((UUID) rs.getObject("id"));
        plan.setUserId((UUID) rs.getObject("user_id"));
        plan.setGoalId((UUID) rs.getObject("goal_id"));
        plan.setTargetCalories(rs.getBigDecimal("target_calories"));
        plan.setTargetProteinPct(rs.getBigDecimal("target_protein_pct"));
        plan.setTargetCarbsPct(rs.getBigDecimal("target_carbs_pct"));
        plan.setTargetFatPct(rs.getBigDecimal("target_fat_pct"));
        plan.setGeneratedAt(rs.getTimestamp("generated_at"));
        plan.setActive(rs.getBoolean("is_active"));
        plan.setCreatedAt(rs.getTimestamp("created_at"));
        plan.setUpdatedAt(rs.getTimestamp("updated_at"));

        return plan;
    }

    // Método adicional: Buscar todos los planes activos de un usuario
    public List<NutritionPlan> findPlansByUserId(UUID userId) {
        List<NutritionPlan> plans = new ArrayList<>();
        String sql = "SELECT * FROM nutrition_plan WHERE user_id = ? ORDER BY generated_at DESC";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    plans.add(mapResultSetToNutritionPlan(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar planes activos por userId: " + e.getMessage(), e);
        }
        
        return plans;
    }
    
    // Método adicional: Buscar planes por Goal ID
    public List<NutritionPlan> findByGoalId(UUID goalId) {
        List<NutritionPlan> plans = new ArrayList<>();
        String sql = "SELECT * FROM nutrition_plan WHERE goal_id = ? ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, goalId);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    plans.add(mapResultSetToNutritionPlan(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar planes por GoalId: " + e.getMessage(), e);
        }
        
        return plans;
    }
    
    // Método adicional: Desactivar todos los planes de un usuario
    public void deactivateAllUserPlans(UUID userId) {
        String sql = "UPDATE nutrition_plan SET is_active = false, updated_at = ? WHERE user_id = ? AND is_active = true";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            statement.setObject(2, userId);
            
            statement.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al desactivar planes del usuario: " + e.getMessage(), e);
        }
    }
    
    // Método adicional: Guardar con validación de porcentajes
    public void saveWithValidation(NutritionPlan nutritionPlan) {
        if (!nutritionPlan.isValidMacroDistribution()) {
            throw new IllegalArgumentException("Los porcentajes de macronutrientes deben sumar 100%");
        }
        
        // Establecer timestamps si son null
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (nutritionPlan.getCreatedAt() == null) {
            nutritionPlan.setCreatedAt(now);
        }
        if (nutritionPlan.getUpdatedAt() == null) {
            nutritionPlan.setUpdatedAt(now);
        }
        if (nutritionPlan.getGeneratedAt() == null) {
            nutritionPlan.setGeneratedAt(now);
        }
        
        save(nutritionPlan);
    }
    
    // Método adicional: Contar planes por usuario
    public int countByUserId(UUID userId) {
        String sql = "SELECT COUNT(*) FROM nutrition_plan WHERE user_id = ?";
        
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
            throw new RuntimeException("Error al contar planes por usuario: " + e.getMessage(), e);
        }
        
        return 0;
    }
}