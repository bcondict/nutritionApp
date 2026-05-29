package edu.ucompensar.codigo.DAO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.database.DatabaseConnection;
import edu.ucompensar.codigo.entity.DietaryPreference;
import edu.ucompensar.codigo.model.enums.DietaryPreferenceCategory;
import edu.ucompensar.codigo.model.enums.DietaryPreferenceCode;
import edu.ucompensar.codigo.model.interfaces.IDietaryPreferenceDAO;

public class DietaryPreferenceDAO implements IDietaryPreferenceDAO {
    @Override
    public void save(DietaryPreference dietaryPreference) {
        String sql = """
            INSERT INTO dietary_preference (
                id,
                code,
                label,
                category,
                description,
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
            if (dietaryPreference.getId() == null) {
                dietaryPreference.setId(UUID.randomUUID());
            }
            
            LocalDateTime now = LocalDateTime.now();
            if (dietaryPreference.getCreatedAt() == null) {
                dietaryPreference.setCreatedAt(now);
            }
            if (dietaryPreference.getUpdatedAt() == null) {
                dietaryPreference.setUpdatedAt(now);
            }
            
            statement.setString(1, dietaryPreference.getId().toString());
            statement.setString(2, dietaryPreference.getCode().name());
            statement.setString(3, dietaryPreference.getLabel());
            statement.setString(4, dietaryPreference.getCategory().name());
            statement.setString(5, dietaryPreference.getDescription());
            statement.setObject(6, dietaryPreference.getCreatedAt());
            statement.setObject(7, dietaryPreference.getUpdatedAt());
            
            statement.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar DietaryPreference: " + e.getMessage(), e);
        }
    }
    
    @Override
    public DietaryPreference findById(UUID id) {
        String sql = "SELECT * FROM dietary_preference WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, id.toString());
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDietaryPreference(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar DietaryPreference por ID: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public List<DietaryPreference> findAll() {
        List<DietaryPreference> preferences = new ArrayList<>();
        String sql = "SELECT * FROM dietary_preference ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                preferences.add(mapResultSetToDietaryPreference(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar todos los DietaryPreferences: " + e.getMessage(), e);
        }
        
        return preferences;
    }
    
    @Override
    public void update(DietaryPreference dietaryPreference) {
        String sql = """
            UPDATE dietary_preference
            SET code = ?,
                label = ?,
                category = ?,
                description = ?,
                updated_at = ?
            WHERE id = ?
        """;

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, dietaryPreference.getCode().name());
            statement.setString(2, dietaryPreference.getLabel());
            statement.setString(3, dietaryPreference.getCategory().name());
            statement.setString(4, dietaryPreference.getDescription());
            statement.setObject(5, LocalDateTime.now());
            statement.setString(6, dietaryPreference.getId().toString());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró DietaryPreference con ID: " + dietaryPreference.getId());
            }
            
            // Actualizar el objeto local
            dietaryPreference.setUpdatedAt(LocalDateTime.now());
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar DietaryPreference: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM dietary_preference WHERE id = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, id.toString());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró DietaryPreference con ID: " + id);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar DietaryPreference: " + e.getMessage(), e);
        }
    }
    
    @Override
    public DietaryPreference findByCode(DietaryPreferenceCode code) {
        String sql = "SELECT * FROM dietary_preference WHERE code = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, code.name());
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDietaryPreference(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar DietaryPreference por código: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public List<DietaryPreference> findByCodeContaining(DietaryPreferenceCode code) {
        List<DietaryPreference> preferences = new ArrayList<>();
        String sql = "SELECT * FROM dietary_preference WHERE LOWER(code) LIKE LOWER(?) ORDER BY code";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, "%" + code.name() + "%");
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    preferences.add(mapResultSetToDietaryPreference(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar DietaryPreferences por código: " + e.getMessage(), e);
        }
        
        return preferences;
    }
    
    @Override
    public List<DietaryPreference> findByLabelContaining(String label) {
        List<DietaryPreference> preferences = new ArrayList<>();
        String sql = "SELECT * FROM dietary_preference WHERE LOWER(label) LIKE LOWER(?) ORDER BY label";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, "%" + label + "%");
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    preferences.add(mapResultSetToDietaryPreference(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar DietaryPreferences por etiqueta: " + e.getMessage(), e);
        }
        
        return preferences;
    }
    
    @Override
    public List<DietaryPreference> findByLabelExact(String label) {
        List<DietaryPreference> preferences = new ArrayList<>();
        String sql = "SELECT * FROM dietary_preference WHERE label = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, label);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    preferences.add(mapResultSetToDietaryPreference(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar DietaryPreference por etiqueta exacta: " + e.getMessage(), e);
        }
        
        return preferences;
    }
    
    @Override
    public List<DietaryPreference> findByCategory(DietaryPreferenceCategory category) {
        List<DietaryPreference> preferences = new ArrayList<>();
        String sql = "SELECT * FROM dietary_preference WHERE category = ? ORDER BY label";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, category.name());
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    preferences.add(mapResultSetToDietaryPreference(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar DietaryPreferences por categoría: " + e.getMessage(), e);
        }
        
        return preferences;
    }
    
    @Override
    public List<DietaryPreference> findByCategoryOrderByLabel(DietaryPreferenceCategory category) {
        List<DietaryPreference> preferences = new ArrayList<>();
        String sql = "SELECT * FROM dietary_preference WHERE category = ? ORDER BY label ASC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, category.name());
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    preferences.add(mapResultSetToDietaryPreference(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar DietaryPreferences por categoría ordenados: " + e.getMessage(), e);
        }
        
        return preferences;
    }
    
    @Override
    public List<String> findAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM dietary_preference ORDER BY category";
        
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
    public List<DietaryPreference> findByCategoryAndCodeContaining(DietaryPreferenceCategory category, DietaryPreferenceCode code) {
        List<DietaryPreference> preferences = new ArrayList<>();
        String sql = "SELECT * FROM dietary_preference WHERE category = ? AND LOWER(code) LIKE LOWER(?) ORDER BY code";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, category.name());
            statement.setString(2, "%" + code.name() + "%");
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    preferences.add(mapResultSetToDietaryPreference(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar DietaryPreferences por categoría y código: " + e.getMessage(), e);
        }
        
        return preferences;
    }
    
    @Override
    public List<DietaryPreference> searchDietaryPreferences(String keyword, DietaryPreferenceCategory category) {
        List<DietaryPreference> preferences = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM dietary_preference WHERE 1=1");
        List<Object> params = new ArrayList<>();
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (LOWER(code) LIKE LOWER(?) OR LOWER(label) LIKE LOWER(?) OR LOWER(description) LIKE LOWER(?))");
            String likePattern = "%" + keyword + "%";
            params.add(likePattern);
            params.add(likePattern);
            params.add(likePattern);
        }
        
        if (category != null) {
            sql.append(" AND category = ?");
            params.add(category.name());
        }
        
        sql.append(" ORDER BY label");
        
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
                    preferences.add(mapResultSetToDietaryPreference(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar DietaryPreferences: " + e.getMessage(), e);
        }
        
        return preferences;
    }
    
    @Override
    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM dietary_preference WHERE id = ?";
        
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
            throw new RuntimeException("Error al verificar existencia de DietaryPreference: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public boolean existsByCode(DietaryPreferenceCode code) {
        String sql = "SELECT COUNT(*) FROM dietary_preference WHERE code = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, code.name());
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia de código: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public boolean existsByLabel(String label) {
        String sql = "SELECT COUNT(*) FROM dietary_preference WHERE label = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, label);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia de etiqueta: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM dietary_preference";
        
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
            throw new RuntimeException("Error al contar DietaryPreferences: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public int countByCategory(DietaryPreferenceCategory category) {
        String sql = "SELECT COUNT(*) FROM dietary_preference WHERE category = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, category.name());
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar DietaryPreferences por categoría: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public List<DietaryPreference> getCommonPreferences() {
        // Retorna las preferencias más comunes (hardcoded o desde configuración)
        List<DietaryPreferenceCode> commonCodes = List.of(
            DietaryPreferenceCode.VEGETARIAN,
            DietaryPreferenceCode.VEGAN,
            DietaryPreferenceCode.GLUTEN_FREE,
            DietaryPreferenceCode.LACTOSE_FREE,
            DietaryPreferenceCode.KETO,
            DietaryPreferenceCode.PALEO
        );
        
        List<DietaryPreference> preferences = new ArrayList<>();
        for (DietaryPreferenceCode code : commonCodes) {
            DietaryPreference pref = findByCode(code);
            if (pref != null) {
                preferences.add(pref);
            }
        }
        
        return preferences;
    }
    
    @Override
    public void deleteByCode(DietaryPreferenceCode code) {
        String sql = "DELETE FROM dietary_preference WHERE code = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, code.name());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró DietaryPreference con código: " + code);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar DietaryPreference por código: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void deleteByCategory(DietaryPreferenceCategory category) {
        String sql = "DELETE FROM dietary_preference WHERE category = ?";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, category.name()  );
            
            int deletedCount = statement.executeUpdate();
            System.out.println("Preferencias dietéticas eliminadas por categoría: " + deletedCount);
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar DietaryPreferences por categoría: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void saveWithValidation(DietaryPreference dietaryPreference) {
        // Validaciones
        if (!dietaryPreference.isValidCode()) {
            throw new IllegalArgumentException("El código no puede estar vacío");
        }
        
        if (!dietaryPreference.isValidLabel()) {
            throw new IllegalArgumentException("La etiqueta no puede estar vacía");
        }
        
        // Verificar si ya existe un código duplicado
        if (existsByCode(dietaryPreference.getCode())) {
            throw new IllegalArgumentException("Ya existe una preferencia dietética con el código: " + dietaryPreference.getCode());
        }
        
        // Verificar si ya existe una etiqueta duplicada
        if (existsByLabel(dietaryPreference.getLabel())) {
            throw new IllegalArgumentException("Ya existe una preferencia dietética con la etiqueta: " + dietaryPreference.getLabel());
        }
        
        // Guardar
        save(dietaryPreference);
    }
    
    @Override
    public void updateWithValidation(DietaryPreference dietaryPreference) {
        // Validaciones
        if (dietaryPreference.getId() == null) {
            throw new IllegalArgumentException("El ID no puede ser null para actualizar");
        }
        
        if (!dietaryPreference.isValidCode()) {
            throw new IllegalArgumentException("El código no puede estar vacío");
        }
        
        if (!dietaryPreference.isValidLabel()) {
            throw new IllegalArgumentException("La etiqueta no puede estar vacía");
        }
        
        // Verificar si existe otro registro con el mismo código
        DietaryPreference existing = findByCode(dietaryPreference.getCode());
        if (existing != null && !existing.getId().equals(dietaryPreference.getId())) {
            throw new IllegalArgumentException("Ya existe otra preferencia dietética con el código: " + dietaryPreference.getCode());
        }
        
        // Verificar si existe otro registro con la misma etiqueta
        List<DietaryPreference> existingByLabel = findByLabelExact(dietaryPreference.getLabel());
        if (!existingByLabel.isEmpty() && !existingByLabel.get(0).getId().equals(dietaryPreference.getId())) {
            throw new IllegalArgumentException("Ya existe otra preferencia dietética con la etiqueta: " + dietaryPreference.getLabel());
        }
        
        // Actualizar
        update(dietaryPreference);
    }
    
    @Override
    public List<DietaryPreference> findAllOrderByCode() {
        List<DietaryPreference> preferences = new ArrayList<>();
        String sql = "SELECT * FROM dietary_preference ORDER BY code";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                preferences.add(mapResultSetToDietaryPreference(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar DietaryPreferences ordenadas por código: " + e.getMessage(), e);
        }
        
        return preferences;
    }
    
    @Override
    public List<DietaryPreference> findAllOrderByLabel() {
        List<DietaryPreference> preferences = new ArrayList<>();
        String sql = "SELECT * FROM dietary_preference ORDER BY label";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                preferences.add(mapResultSetToDietaryPreference(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar DietaryPreferences ordenadas por etiqueta: " + e.getMessage(), e);
        }
        
        return preferences;
    }
    
    @Override
    public List<DietaryPreference> findAllOrderByCategory() {
        List<DietaryPreference> preferences = new ArrayList<>();
        String sql = "SELECT * FROM dietary_preference ORDER BY category, label";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {
            while (rs.next()) {
                preferences.add(mapResultSetToDietaryPreference(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar DietaryPreferences ordenadas por categoría: " + e.getMessage(), e);
        }
        
        return preferences;
    }
    
    @Override
    public List<DietaryPreference> findPreferencesCreatedAfter(LocalDateTime date) {
        List<DietaryPreference> preferences = new ArrayList<>();
        String sql = "SELECT * FROM dietary_preference WHERE created_at > ? ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, date);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    preferences.add(mapResultSetToDietaryPreference(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar preferencias creadas después de: " + e.getMessage(), e);
        }
        
        return preferences;
    }
    
    @Override
    public List<DietaryPreference> findPreferencesCreatedBefore(LocalDateTime date) {
        List<DietaryPreference> preferences = new ArrayList<>();
        String sql = "SELECT * FROM dietary_preference WHERE created_at < ? ORDER BY created_at DESC";
        
        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, date);
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    preferences.add(mapResultSetToDietaryPreference(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar preferencias creadas antes de: " + e.getMessage(), e);
        }
        
        return preferences;
    }
    
    @Override
    public List<DietaryPreference> findPreferencesByCategoryWithCount(DietaryPreferenceCategory category) {
        // Este método retorna las preferencias de una categoría con información adicional
        // (el count se puede obtener por separado con countByCategory)
        return findByCategory(category);
    }
    
    // Método auxiliar para mapear ResultSet a DietaryPreference
    private DietaryPreference mapResultSetToDietaryPreference(ResultSet rs) throws SQLException {
        UUID newId = UUID.fromString(rs.getString("id"));
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();
        DietaryPreference preference = new DietaryPreference(
            newId,
            DietaryPreferenceCode.valueOf(rs.getString("code")),
            rs.getString("label"),
            DietaryPreferenceCategory.valueOf(rs.getString("category")),
            rs.getString("description"),
            createdAt,
            updatedAt
        );

        return preference;
    }
}