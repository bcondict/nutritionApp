package edu.ucompensar.codigo.DAO;

import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.database.DatabaseConnection;
import edu.ucompensar.codigo.entity.FoodItem;
import edu.ucompensar.codigo.model.enums.FoodCategory;
import edu.ucompensar.codigo.model.interfaces.IFoodItemDAO;

public class FoodItemDAO implements IFoodItemDAO {
    
    @Override
    public void save(FoodItem foodItem) {
        String sql = """
            INSERT INTO food_item (
                id,
                name,
                category,
                calories_per_100g,
                protein_per_100g,
                carbs_per_100g,
                fat_per_100g,
                fiber_per_100g,
                sodium_per_100mg,
                created_at,
                updated_at
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            if (foodItem.getId() == null) {
                foodItem.setId(UUID.randomUUID());
            }
            
            LocalDateTime now = LocalDateTime.now();
            if (foodItem.getCreatedAt() == null) {
                foodItem.setCreatedAt(now);
            }
            if (foodItem.getUpdatedAt() == null) {
                foodItem.setUpdatedAt(now);
            }
            
            statement.setObject(1, foodItem.getId());
            statement.setString(2, foodItem.getName());
            statement.setString(3, foodItem.getCategory());
            statement.setBigDecimal(4, foodItem.getCaloriesPer100g());
            statement.setBigDecimal(5, foodItem.getProteinPer100g());
            statement.setBigDecimal(6, foodItem.getCarbsPer100g());
            statement.setBigDecimal(7, foodItem.getFatPer100g());
            statement.setBigDecimal(8, foodItem.getFiberPer100g());
            statement.setBigDecimal(9, foodItem.getSodiumPer100mg());
            statement.setObject(10, foodItem.getCreatedAt());
            statement.setObject(11, foodItem.getUpdatedAt());
            
            statement.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar FoodItem: " + e.getMessage(), e);
        }
    }
    
    @Override
    public FoodItem findById(UUID id) {
        String sql = "SELECT * FROM food_item WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, id);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFoodItem(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar FoodItem por ID: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public List<FoodItem> findAll() {
        List<FoodItem> foodItems = new ArrayList<>();
        String sql = "SELECT * FROM food_item ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                foodItems.add(mapResultSetToFoodItem(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar todos los FoodItems: " + e.getMessage(), e);
        }
        
        return foodItems;
    }
    
    @Override
    public void update(FoodItem foodItem) {
        String sql = "UPDATE food_item SET name = ?, category = ?, calories_per_100g = ?, " +
                     "protein_per_100g = ?, carbs_per_100g = ?, fat_per_100g = ?, " +
                     "fiber_per_100g = ?, sodium_per_100mg = ?, updated_at = ? " +
                     "WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, foodItem.getName());
            statement.setString(2, foodItem.getCategory());
            statement.setBigDecimal(3, foodItem.getCaloriesPer100g());
            statement.setBigDecimal(4, foodItem.getProteinPer100g());
            statement.setBigDecimal(5, foodItem.getCarbsPer100g());
            statement.setBigDecimal(6, foodItem.getFatPer100g());
            statement.setBigDecimal(7, foodItem.getFiberPer100g());
            statement.setBigDecimal(8, foodItem.getSodiumPer100mg());
            statement.setObject(9, LocalDateTime.now());
            statement.setObject(10, foodItem.getId());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró FoodItem con ID: " + foodItem.getId());
            }
            
            // Actualizar el objeto local
            foodItem.setUpdatedAt(LocalDateTime.now());
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar FoodItem: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM food_item WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, id);
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró FoodItem con ID: " + id);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar FoodItem: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<FoodItem> findByNameContaining(String name) {
        List<FoodItem> foodItems = new ArrayList<>();
        String sql = "SELECT * FROM food_item WHERE LOWER(name) LIKE LOWER(?) ORDER BY name";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, "%" + name + "%");
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    foodItems.add(mapResultSetToFoodItem(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar FoodItems por nombre: " + e.getMessage(), e);
        }
        
        return foodItems;
    }
    
    @Override
    public List<FoodItem> findByNameExact(String name) {
        List<FoodItem> foodItems = new ArrayList<>();
        String sql = "SELECT * FROM food_item WHERE name = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, name);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    foodItems.add(mapResultSetToFoodItem(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar FoodItem por nombre exacto: " + e.getMessage(), e);
        }
        
        return foodItems;
    }
    
    @Override
    public List<FoodItem> findByCategory(FoodCategory category) {
        List<FoodItem> foodItems = new ArrayList<>();
        String sql = "SELECT * FROM food_item WHERE category = ? ORDER BY name";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, category.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    foodItems.add(mapResultSetToFoodItem(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar FoodItems por categoría: " + e.getMessage(), e);
        }
        
        return foodItems;
    }
    
    @Override
    public List<FoodItem> findByCategoryOrderByName(FoodCategory category) {
        List<FoodItem> foodItems = new ArrayList<>();
        String sql = "SELECT * FROM food_item WHERE category = ? ORDER BY name ASC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, category.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    foodItems.add(mapResultSetToFoodItem(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar FoodItems por categoría ordenados: " + e.getMessage(), e);
        }
        
        return foodItems;
    }
    
    @Override
    public List<String> findAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM food_item ORDER BY category";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar categorías: " + e.getMessage(), e);
        }
        
        return categories;
    }
    
    @Override
    public List<FoodItem> findByCaloriesRange(BigDecimal minCalories, BigDecimal maxCalories) {
        List<FoodItem> foodItems = new ArrayList<>();
        String sql = "SELECT * FROM food_item WHERE calories_per_100g BETWEEN ? AND ? ORDER BY calories_per_100g";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setBigDecimal(1, minCalories);
            statement.setBigDecimal(2, maxCalories);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    foodItems.add(mapResultSetToFoodItem(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar FoodItems por rango de calorías: " + e.getMessage(), e);
        }
        
        return foodItems;
    }
    
    @Override
    public List<FoodItem> findByProteinRange(BigDecimal minProtein, BigDecimal maxProtein) {
        List<FoodItem> foodItems = new ArrayList<>();
        String sql = "SELECT * FROM food_item WHERE protein_per_100g BETWEEN ? AND ? ORDER BY protein_per_100g DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setBigDecimal(1, minProtein);
            statement.setBigDecimal(2, maxProtein);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    foodItems.add(mapResultSetToFoodItem(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar FoodItems por rango de proteínas: " + e.getMessage(), e);
        }
        
        return foodItems;
    }
    
    @Override
    public List<FoodItem> findByCarbsRange(BigDecimal minCarbs, BigDecimal maxCarbs) {
        List<FoodItem> foodItems = new ArrayList<>();
        String sql = "SELECT * FROM food_item WHERE carbs_per_100g BETWEEN ? AND ? ORDER BY carbs_per_100g";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setBigDecimal(1, minCarbs);
            statement.setBigDecimal(2, maxCarbs);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    foodItems.add(mapResultSetToFoodItem(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar FoodItems por rango de carbohidratos: " + e.getMessage(), e);
        }
        
        return foodItems;
    }
    
    @Override
    public List<FoodItem> findByFatRange(BigDecimal minFat, BigDecimal maxFat) {
        List<FoodItem> foodItems = new ArrayList<>();
        String sql = "SELECT * FROM food_item WHERE fat_per_100g BETWEEN ? AND ? ORDER BY fat_per_100g";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setBigDecimal(1, minFat);
            statement.setBigDecimal(2, maxFat);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    foodItems.add(mapResultSetToFoodItem(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar FoodItems por rango de grasas: " + e.getMessage(), e);
        }
        
        return foodItems;
    }
    
    @Override
    public List<FoodItem> findByFiberRange(BigDecimal minFiber, BigDecimal maxFiber) {
        List<FoodItem> foodItems = new ArrayList<>();
        String sql = "SELECT * FROM food_item WHERE fiber_per_100g BETWEEN ? AND ? ORDER BY fiber_per_100g DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setBigDecimal(1, minFiber);
            statement.setBigDecimal(2, maxFiber);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    foodItems.add(mapResultSetToFoodItem(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar FoodItems por rango de fibra: " + e.getMessage(), e);
        }
        
        return foodItems;
    }
    
    @Override
    public List<FoodItem> findBySodiumRange(BigDecimal minSodium, BigDecimal maxSodium) {
        List<FoodItem> foodItems = new ArrayList<>();
        String sql = "SELECT * FROM food_item WHERE sodium_per_100mg BETWEEN ? AND ? ORDER BY sodium_per_100mg";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setBigDecimal(1, minSodium);
            statement.setBigDecimal(2, maxSodium);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    foodItems.add(mapResultSetToFoodItem(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar FoodItems por rango de sodio: " + e.getMessage(), e);
        }
        
        return foodItems;
    }
    
    @Override
    public List<FoodItem> searchFoodItems(String keyword, FoodCategory category, BigDecimal maxCalories, BigDecimal minProtein) {
        List<FoodItem> foodItems = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM food_item WHERE 1=1");
        List<Object> params = new ArrayList<>();
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND LOWER(name) LIKE LOWER(?)");
            params.add("%" + keyword + "%");
        }
        
        if (category != null) {
            sql.append(" AND category = ?");
            params.add(category.toString());
        }
        
        if (maxCalories != null) {
            sql.append(" AND calories_per_100g <= ?");
            params.add(maxCalories);
        }
        
        if (minProtein != null) {
            sql.append(" AND protein_per_100g >= ?");
            params.add(minProtein);
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
                    foodItems.add(mapResultSetToFoodItem(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar FoodItems: " + e.getMessage(), e);
        }
        
        return foodItems;
    }
    
    @Override
    public List<FoodItem> findHighProteinFoods(BigDecimal minProtein) {
        return findByProteinRange(minProtein, new BigDecimal("9999"));
    }
    
    @Override
    public List<FoodItem> findLowCarbFoods(BigDecimal maxCarbs) {
        return findByCarbsRange(BigDecimal.ZERO, maxCarbs);
    }
    
    @Override
    public List<FoodItem> findLowFatFoods(BigDecimal maxFat) {
        return findByFatRange(BigDecimal.ZERO, maxFat);
    }
    
    @Override
    public List<FoodItem> findHighFiberFoods(BigDecimal minFiber) {
        return findByFiberRange(minFiber, new BigDecimal("9999"));
    }
    
    @Override
    public List<FoodItem> findLowSodiumFoods(BigDecimal maxSodium) {
        return findBySodiumRange(BigDecimal.ZERO, maxSodium);
    }
    
    @Override
    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM food_item WHERE id = ?";
        
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
            throw new RuntimeException("Error al verificar existencia de FoodItem: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public boolean existsByName(String name) {
        String sql = "SELECT COUNT(*) FROM food_item WHERE name = ?";
        
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
            throw new RuntimeException("Error al verificar existencia de FoodItem por nombre: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM food_item";
        
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
            throw new RuntimeException("Error al contar FoodItems: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public int countByCategory(FoodCategory category) {
        String sql = "SELECT COUNT(*) FROM food_item WHERE category = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, category.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar FoodItems por categoría: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public BigDecimal getAverageCalories() {
        String sql = "SELECT AVG(calories_per_100g) FROM food_item";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al calcular promedio de calorías: " + e.getMessage(), e);
        }
        
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getAverageProtein() {
        String sql = "SELECT AVG(protein_per_100g) FROM food_item";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al calcular promedio de proteínas: " + e.getMessage(), e);
        }
        
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getAverageCarbs() {
        String sql = "SELECT AVG(carbs_per_100g) FROM food_item";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al calcular promedio de carbohidratos: " + e.getMessage(), e);
        }
        
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getAverageFat() {
        String sql = "SELECT AVG(fat_per_100g) FROM food_item";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al calcular promedio de grasas: " + e.getMessage(), e);
        }
        
        return BigDecimal.ZERO;
    }
    
    @Override
    public void deleteByName(String name) {
        String sql = "DELETE FROM food_item WHERE name = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, name);
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró FoodItem con nombre: " + name);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar FoodItem por nombre: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void deleteByCategory(FoodCategory category) {
        String sql = "DELETE FROM food_item WHERE category = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, category.toString());
            
            int deletedCount = statement.executeUpdate();
            System.out.println("Alimentos eliminados por categoría: " + deletedCount);
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar FoodItems por categoría: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void saveWithValidation(FoodItem foodItem) {
        // Validaciones
        if (!foodItem.isValidName()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        
        if (!foodItem.hasValidNutritionalValues()) {
            throw new IllegalArgumentException("Todos los valores nutricionales deben ser mayores o iguales a 0");
        }
        
        // Verificar si ya existe un alimento con el mismo nombre
        if (existsByName(foodItem.getName())) {
            throw new IllegalArgumentException("Ya existe un alimento con el nombre: " + foodItem.getName());
        }
        
        // Guardar
        save(foodItem);
    }
    
    @Override
    public void updateWithValidation(FoodItem foodItem) {
        // Validaciones
        if (foodItem.getId() == null) {
            throw new IllegalArgumentException("El ID no puede ser null para actualizar");
        }
        
        if (!foodItem.isValidName()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        
        if (!foodItem.hasValidNutritionalValues()) {
            throw new IllegalArgumentException("Todos los valores nutricionales deben ser mayores o iguales a 0");
        }
        
        // Verificar si el nombre no está siendo usado por otro alimento
        List<FoodItem> existing = findByNameExact(foodItem.getName());
        if (!existing.isEmpty() && !existing.get(0).getId().equals(foodItem.getId())) {
            throw new IllegalArgumentException("Ya existe otro alimento con el nombre: " + foodItem.getName());
        }
        
        // Actualizar
        update(foodItem);
    }
    
    @Override
    public List<FoodItem> findAllOrderByName() {
        List<FoodItem> foodItems = new ArrayList<>();
        String sql = "SELECT * FROM food_item ORDER BY name ASC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                foodItems.add(mapResultSetToFoodItem(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar FoodItems ordenados por nombre: " + e.getMessage(), e);
        }
        
        return foodItems;
    }
    
    @Override
    public List<FoodItem> findAllOrderByCaloriesDesc() {
        List<FoodItem> foodItems = new ArrayList<>();
        String sql = "SELECT * FROM food_item ORDER BY calories_per_100g DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                foodItems.add(mapResultSetToFoodItem(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar FoodItems por calorías descendente: " + e.getMessage(), e);
        }
        
        return foodItems;
    }
    
    @Override
    public List<FoodItem> findAllOrderByProteinDesc() {
        List<FoodItem> foodItems = new ArrayList<>();
        String sql = "SELECT * FROM food_item ORDER BY protein_per_100g DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                foodItems.add(mapResultSetToFoodItem(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar FoodItems por proteínas descendente: " + e.getMessage(), e);
        }
        
        return foodItems;
    }
    
    @Override
    public List<FoodItem> findMostCaloricFoods(int limit) {
        List<FoodItem> foodItems = new ArrayList<>();
        String sql = "SELECT * FROM food_item ORDER BY calories_per_100g DESC LIMIT ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, limit);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    foodItems.add(mapResultSetToFoodItem(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar alimentos más calóricos: " + e.getMessage(), e);
        }
        
        return foodItems;
    }
    
    @Override
    public List<FoodItem> findLeastCaloricFoods(int limit) {
        List<FoodItem> foodItems = new ArrayList<>();
        String sql = "SELECT * FROM food_item ORDER BY calories_per_100g ASC LIMIT ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, limit);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    foodItems.add(mapResultSetToFoodItem(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar alimentos menos calóricos: " + e.getMessage(), e);
        }
        
        return foodItems;
    }
    
    @Override
    public List<FoodItem> findFoodsCreatedAfter(LocalDateTime date) {
        List<FoodItem> foodItems = new ArrayList<>();
        String sql = "SELECT * FROM food_item WHERE created_at > ? ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, date);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    foodItems.add(mapResultSetToFoodItem(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar alimentos creados después de: " + e.getMessage(), e);
        }
        
        return foodItems;
    }
    
    @Override
    public List<FoodItem> findFoodsCreatedBefore(LocalDateTime date) {
        List<FoodItem> foodItems = new ArrayList<>();
        String sql = "SELECT * FROM food_item WHERE created_at < ? ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, date);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    foodItems.add(mapResultSetToFoodItem(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar alimentos creados antes de: " + e.getMessage(), e);
        }
        
        return foodItems;
    }
    
    // Método auxiliar para mapear ResultSet a FoodItem
    private FoodItem mapResultSetToFoodItem(ResultSet rs) throws SQLException {
        FoodItem foodItem = new FoodItem();
        
        foodItem.setId((UUID) rs.getObject("id"));
        foodItem.setName(rs.getString("name"));
        foodItem.setCategory(rs.getString("category"));
        foodItem.setCaloriesPer100g(rs.getBigDecimal("calories_per_100g"));
        foodItem.setProteinPer100g(rs.getBigDecimal("protein_per_100g"));
        foodItem.setCarbsPer100g(rs.getBigDecimal("carbs_per_100g"));
        foodItem.setFatPer100g(rs.getBigDecimal("fat_per_100g"));
        foodItem.setFiberPer100g(rs.getBigDecimal("fiber_per_100g"));
        foodItem.setSodiumPer100mg(rs.getBigDecimal("sodium_per_100mg"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            foodItem.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            foodItem.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return foodItem;
    }
}