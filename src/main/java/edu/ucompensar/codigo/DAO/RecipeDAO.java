package edu.ucompensar.codigo.DAO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.database.DatabaseConnection;
import edu.ucompensar.codigo.entity.Recipe;
import edu.ucompensar.codigo.model.enums.MealType;
import edu.ucompensar.codigo.model.enums.RecipeDifficulty;
import edu.ucompensar.codigo.model.interfaces.IRecipeDAO;

public class RecipeDAO implements IRecipeDAO {
    
    @Override
    public void save(Recipe recipe) {
        String sql = """
            INSERT INTO recipe (
                id,
                name,
                description,
                meal_type,
                prep_time_minutes,
                difficulty,
                servings,
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
            if (recipe.getId() == null) {
                recipe.setId(UUID.randomUUID());
            }
            
            LocalDateTime now = LocalDateTime.now();
            if (recipe.getCreatedAt() == null) {
                recipe.setCreatedAt(now);
            }
            if (recipe.getUpdatedAt() == null) {
                recipe.setUpdatedAt(now);
            }
            
            statement.setString(1, recipe.getId().toString());
            statement.setString(2, recipe.getName());
            statement.setString(3, recipe.getDescription());
            statement.setString(4, recipe.getMealType().name());
            statement.setInt(5, recipe.getPrepTimeMinutes());
            statement.setString(6, recipe.getDifficulty().name());
            statement.setInt(7, recipe.getServings());
            statement.setObject(8, recipe.getCreatedAt());
            statement.setObject(9, recipe.getUpdatedAt());
            
            statement.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar Recipe: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Recipe findById(UUID id) {
        String sql = "SELECT * FROM recipe WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, id.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRecipe(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Recipe por ID: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public List<Recipe> findAll() {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipe ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                recipes.add(mapResultSetToRecipe(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar todas las Recipes: " + e.getMessage(), e);
        }
        
        return recipes;
    }
    
    @Override
    public void update(Recipe recipe) {
        String sql = """
            UPDATE recipe
            SET name = ?,
                description = ?,
                meal_type = ?,
                prep_time_minutes = ?,
                difficulty = ?,
                servings = ?,
                updated_at = ?
            WHERE id = ?
        """;
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, recipe.getName());
            statement.setString(2, recipe.getDescription());
            statement.setString(3, recipe.getMealType().name());
            statement.setInt(4, recipe.getPrepTimeMinutes());
            statement.setString(5, recipe.getDifficulty().name());
            statement.setInt(6, recipe.getServings());
            statement.setObject(7, LocalDateTime.now());
            statement.setString(8, recipe.getId().toString());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró Recipe con ID: " + recipe.getId());
            }
            
            // Actualizar el objeto local
            recipe.setUpdatedAt(LocalDateTime.now());
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar Recipe: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM recipe WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, id.toString());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró Recipe con ID: " + id);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar Recipe: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Recipe> findByNameContaining(String name) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipe WHERE LOWER(name) LIKE LOWER(?) ORDER BY name";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, "%" + name + "%");
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapResultSetToRecipe(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Recipes por nombre: " + e.getMessage(), e);
        }
        
        return recipes;
    }
    
    @Override
    public List<Recipe> findByNameExact(String name) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipe WHERE name = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, name);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapResultSetToRecipe(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Recipe por nombre exacto: " + e.getMessage(), e);
        }
        
        return recipes;
    }
    
    @Override
    public List<Recipe> findByMealType(MealType mealType) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipe WHERE meal_type = ? ORDER BY name";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, mealType.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapResultSetToRecipe(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Recipes por tipo de comida: " + e.getMessage(), e);
        }
        
        return recipes;
    }
    
    @Override
    public List<Recipe> findByMealTypeOrderByName(MealType mealType) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipe WHERE meal_type = ? ORDER BY name ASC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, mealType.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapResultSetToRecipe(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Recipes por tipo de comida ordenadas: " + e.getMessage(), e);
        }
        
        return recipes;
    }
    
    @Override
    public List<Recipe> findByDifficulty(RecipeDifficulty difficulty) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipe WHERE difficulty = ? ORDER BY prep_time_minutes";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, difficulty.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapResultSetToRecipe(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Recipes por dificultad: " + e.getMessage(), e);
        }
        
        return recipes;
    }
    
    @Override
    public List<Recipe> findByDifficultyOrderByPrepTime(RecipeDifficulty difficulty) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipe WHERE difficulty = ? ORDER BY prep_time_minutes ASC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, difficulty.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapResultSetToRecipe(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Recipes por dificultad ordenadas: " + e.getMessage(), e);
        }
        
        return recipes;
    }
    
    @Override
    public List<Recipe> findByMealTypeAndDifficulty(MealType mealType, RecipeDifficulty difficulty) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipe WHERE meal_type = ? AND difficulty = ? ORDER BY name";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, mealType.toString());
            statement.setString(2, difficulty.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapResultSetToRecipe(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Recipes por tipo y dificultad: " + e.getMessage(), e);
        }
        
        return recipes;
    }
    
    @Override
    public List<Recipe> findByPrepTimeLessThan(int maxMinutes) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipe WHERE prep_time_minutes <= ? ORDER BY prep_time_minutes";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, maxMinutes);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapResultSetToRecipe(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Recipes con tiempo menor a: " + e.getMessage(), e);
        }
        
        return recipes;
    }
    
    @Override
    public List<Recipe> findByPrepTimeBetween(int minMinutes, int maxMinutes) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipe WHERE prep_time_minutes BETWEEN ? AND ? ORDER BY prep_time_minutes";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, minMinutes);
            statement.setInt(2, maxMinutes);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapResultSetToRecipe(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Recipes por rango de tiempo: " + e.getMessage(), e);
        }
        
        return recipes;
    }
    
    @Override
    public List<Recipe> findByServingsGreaterThan(int minServings) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipe WHERE servings >= ? ORDER BY servings";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, minServings);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapResultSetToRecipe(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Recipes con porciones mayores a: " + e.getMessage(), e);
        }
        
        return recipes;
    }
    
    @Override
    public List<Recipe> findByServingsLessThan(int maxServings) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipe WHERE servings <= ? ORDER BY servings DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, maxServings);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapResultSetToRecipe(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Recipes con porciones menores a: " + e.getMessage(), e);
        }
        
        return recipes;
    }
    
    @Override
    public List<Recipe> searchRecipes(String keyword, MealType mealType, RecipeDifficulty difficulty, Integer maxPrepTime) {
        List<Recipe> recipes = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM recipe WHERE 1=1");
        List<Object> params = new ArrayList<>();
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND LOWER(name) LIKE LOWER(?)");
            params.add("%" + keyword + "%");
        }
        
        if (mealType != null) {
            sql.append(" AND meal_type = ?");
            params.add(mealType.toString());
        }
        
        if (difficulty != null) {
            sql.append(" AND difficulty = ?");
            params.add(difficulty.toString());
        }
        
        if (maxPrepTime != null && maxPrepTime > 0) {
            sql.append(" AND prep_time_minutes <= ?");
            params.add(maxPrepTime);
        }
        
        sql.append(" ORDER BY name");
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql.toString())
        ) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapResultSetToRecipe(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar Recipes: " + e.getMessage(), e);
        }
        
        return recipes;
    }
    
    @Override
    public List<Recipe> findQuickRecipes(int maxMinutes) {
        return findByPrepTimeLessThan(maxMinutes);
    }
    
    @Override
    public List<Recipe> findBeginnerFriendlyRecipes() {
        return findByDifficulty(RecipeDifficulty.EASY);
    }
    
    @Override
    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM recipe WHERE id = ?";
        
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
            throw new RuntimeException("Error al verificar existencia de Recipe: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public boolean existsByName(String name) {
        String sql = "SELECT COUNT(*) FROM recipe WHERE name = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, name);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia de Recipe por nombre: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM recipe";
        
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
            throw new RuntimeException("Error al contar Recipes: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public int countByMealType(MealType mealType) {
        String sql = "SELECT COUNT(*) FROM recipe WHERE meal_type = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, mealType.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar Recipes por tipo de comida: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public int countByDifficulty(RecipeDifficulty difficulty) {
        String sql = "SELECT COUNT(*) FROM recipe WHERE difficulty = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, difficulty.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar Recipes por dificultad: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public double getAveragePrepTime() {
        String sql = "SELECT AVG(prep_time_minutes) FROM recipe";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al calcular tiempo promedio de preparación: " + e.getMessage(), e);
        }
        
        return 0.0;
    }
    
    @Override
    public int getTotalServings() {
        String sql = "SELECT SUM(servings) FROM recipe";
        
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
            throw new RuntimeException("Error al calcular total de porciones: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public void deleteByName(String name) {
        String sql = "DELETE FROM recipe WHERE name = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, name);
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró Recipe con nombre: " + name);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar Recipe por nombre: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void deleteByMealType(MealType mealType) {
        String sql = "DELETE FROM recipe WHERE meal_type = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, mealType.toString());
            
            int deletedCount = statement.executeUpdate();
            System.out.println("Recetas eliminadas por tipo de comida: " + deletedCount);
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar Recipes por tipo de comida: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void saveWithValidation(Recipe recipe) {
        // Validaciones
        if (!recipe.isValidName()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        
        if (!recipe.isValidMealType()) {
            throw new IllegalArgumentException("Tipo de comida inválido. Valores permitidos: " +
                "BREAKFAST, LUNCH, DINNER, SNACK, DESSERT, BEVERAGE");
        }
        
        if (!recipe.isValidDifficulty()) {
            throw new IllegalArgumentException("Dificultad inválida. Valores permitidos: " +
                "EASY, MEDIUM, HARD, EXPERT");
        }
        
        if (!recipe.isValidPrepTime()) {
            throw new IllegalArgumentException("El tiempo de preparación debe ser mayor a 0");
        }
        
        if (!recipe.isValidServings()) {
            throw new IllegalArgumentException("El número de porciones debe ser mayor a 0");
        }
        
        // Verificar si ya existe una receta con el mismo nombre
        if (existsByName(recipe.getName())) {
            throw new IllegalArgumentException("Ya existe una receta con el nombre: " + recipe.getName());
        }
        
        // Guardar
        save(recipe);
    }
    
    @Override
    public void updateWithValidation(Recipe recipe) {
        // Validaciones
        if (recipe.getId() == null) {
            throw new IllegalArgumentException("El ID no puede ser null para actualizar");
        }
        
        if (!recipe.isValidName()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        
        if (!recipe.isValidMealType()) {
            throw new IllegalArgumentException("Tipo de comida inválido");
        }
        
        if (!recipe.isValidDifficulty()) {
            throw new IllegalArgumentException("Dificultad inválida");
        }
        
        if (!recipe.isValidPrepTime()) {
            throw new IllegalArgumentException("El tiempo de preparación debe ser mayor a 0");
        }
        
        if (!recipe.isValidServings()) {
            throw new IllegalArgumentException("El número de porciones debe ser mayor a 0");
        }
        
        // Verificar si el nombre no está siendo usado por otra receta
        Recipe existing = findByNameExact(recipe.getName()).stream().findFirst().orElse(null);
        if (existing != null && !existing.getId().equals(recipe.getId())) {
            throw new IllegalArgumentException("Ya existe otra receta con el nombre: " + recipe.getName());
        }
        
        // Actualizar
        update(recipe);
    }
    
    @Override
    public List<Recipe> findAllOrderByName() {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipe ORDER BY name ASC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                recipes.add(mapResultSetToRecipe(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar Recipes ordenadas por nombre: " + e.getMessage(), e);
        }
        
        return recipes;
    }
    
    @Override
    public List<Recipe> findAllOrderByPrepTimeAsc() {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipe ORDER BY prep_time_minutes ASC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                recipes.add(mapResultSetToRecipe(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar Recipes por tiempo ascendente: " + e.getMessage(), e);
        }
        
        return recipes;
    }
    
    @Override
    public List<Recipe> findAllOrderByPrepTimeDesc() {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipe ORDER BY prep_time_minutes DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                recipes.add(mapResultSetToRecipe(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar Recipes por tiempo descendente: " + e.getMessage(), e);
        }
        
        return recipes;
    }
    
    @Override
    public List<Recipe> findMostPopularRecipes(int limit) {
        // Nota: Este es un ejemplo, podrías tener una tabla de "recipe_usage" para contar usos
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipe ORDER BY created_at DESC LIMIT ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, limit);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapResultSetToRecipe(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar recetas populares: " + e.getMessage(), e);
        }
        
        return recipes;
    }
    
    @Override
    public List<Recipe> findRecipesCreatedAfter(LocalDateTime date) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipe WHERE created_at > ? ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, date);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapResultSetToRecipe(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar recetas creadas después de: " + e.getMessage(), e);
        }
        
        return recipes;
    }
    
    @Override
    public List<Recipe> findRecipesCreatedBefore(LocalDateTime date) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipe WHERE created_at < ? ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, date);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapResultSetToRecipe(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar recetas creadas antes de: " + e.getMessage(), e);
        }
        
        return recipes;
    }
    
    // Método auxiliar para mapear ResultSet a Recipe
    private Recipe mapResultSetToRecipe(ResultSet rs) throws SQLException {
        Recipe recipe = new Recipe(
            UUID.fromString(rs.getString("id")),
            rs.getString("name"),
            rs.getString("description"),
            MealType.valueOf(rs.getString("meal_type")),
            rs.getInt("prep_time_minutes"),
            RecipeDifficulty.valueOf(rs.getString("difficulty")),
            rs.getInt("servings"),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("updated_at").toLocalDateTime()
        );

        return recipe;
    }
}