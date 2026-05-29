package edu.ucompensar.codigo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.ucompensar.codigo.database.DatabaseConnection;
import edu.ucompensar.codigo.entity.User;
import edu.ucompensar.codigo.model.interfaces.IUserDAO;

public class UserDAO implements IUserDAO {
    public void save(User user) {
        String sql = """
            INSERT INTO user (
                id,
                name,
                lastname,
                email,
                birthday,
                password_hash
            )
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            // values
            statement.setString(1, user.getId().toString());
            statement.setString(2, user.getName());
            statement.setString(3, user.getLastname());
            statement.setString(4, user.getEmail());
            if (user.getBirthday() != null) {
                statement.setDate(
                        5,
                        java.sql.Date.valueOf(user.getBirthday())
                );
            } else {
                statement.setDate(5, null);
            }
            statement.setString(6, user.getPasswordHash());

            statement.executeUpdate();

            System.out.println("Usuario guardado exitosamente");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findById(UUID id) {
        String sql = "SELECT * FROM user WHERE id = ?";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, id.toString());

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {

                User user = new User();

                user.setId(UUID.fromString(rs.getString("id")));
                user.setName(rs.getString("name"));
                user.setLastname(rs.getString("lastname"));
                user.setEmail(rs.getString("email"));
                user.setBirthday(rs.getDate("birthday").toLocalDate());
                user.setPasswordHash(rs.getString("password_hash"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        String sql = "SELECT * FROM user";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                User user = new User();

                user.setId(UUID.fromString(rs.getString("id")));
                user.setName(rs.getString("name"));
                user.setLastname(rs.getString("lastname"));
                user.setEmail(rs.getString("email"));
                user.setBirthday(rs.getDate("birthday").toLocalDate());
                user.setPasswordHash(rs.getString("password_hash"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }



    @Override
    public void update(User user) {
        String sql = """
                UPDATE user
                SET name = ?,
                    lastname = ?,
                    email = ?,
                    birthday = ?,
                    password_hash = ?
                WHERE id = ?
                """;

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getLastname());
            statement.setString(3, user.getEmail());

            if (user.getBirthday() != null) {
                statement.setDate(
                        4,
                        java.sql.Date.valueOf(user.getBirthday())
                );
            } else {
                statement.setDate(4, null);
            }

            statement.setString(5, user.getPasswordHash());
            statement.setString(6, user.getId().toString());

            statement.executeUpdate();

            System.out.println("Usuario actualizado");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM user WHERE id = ?";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, id.toString());

            statement.executeUpdate();

            System.out.println("Usuario eliminado");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";

        DatabaseConnection.getInstance();
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, email);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {

                User user = new User();

                user.setId(UUID.fromString(rs.getString("id")));
                user.setName(rs.getString("name"));
                user.setLastname(rs.getString("lastname"));
                user.setEmail(rs.getString("email"));
                user.setBirthday(rs.getDate("birthday").toLocalDate());
                user.setPasswordHash(rs.getString("password_hash"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
