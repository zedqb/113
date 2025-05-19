package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    String url = "jdbc:postgresql://localhost:5432/mydatabase";
    String user = "myuser";
    String password = "mypassword";
    Logger logger = LoggerFactory.getLogger(UserDaoJDBCImpl.class);

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS users (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    last_name VARCHAR(100) UNIQUE NOT NULL,
                    age INTEGER NOT NULL
                )""";

        try (Connection conn = Util.connect()) {
            Statement stmt = conn.createStatement();

            stmt.executeUpdate(createTableSQL);
            System.out.println("Таблица успешно создана.");

        } catch (SQLException e) {
            logger.error("Ошибка при создании таблицы", e);
        }
    }


    public void dropUsersTable() {
        String dropTableSQL = "DROP TABLE IF EXISTS users";

        try (Connection conn = Util.connect()) {
            Statement stmt = conn.createStatement();

            stmt.executeUpdate(dropTableSQL);
            System.out.println("Таблица успешно удалена.");

        } catch (SQLException e) {
            logger.error("Ошибка при удалении таблицы:", e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String insertSQL = "INSERT INTO users (name, last_name, age) VALUES (?, ?, ?)";

        try (Connection conn = Util.connect()) {
             PreparedStatement pstmt = conn.prepareStatement(insertSQL);

            pstmt.setString(1, name);
            pstmt.setString(2, lastName);
            pstmt.setByte(3, age);
            pstmt.executeUpdate();

            System.out.println("Все пользователи добавлены.");

        } catch (SQLException e) {
            logger.error("Ошибка при вставке пользователей:");
        }
    }

    public void removeUserById(long id) {
        String removeUser = "DELETE FROM users WHERE id = ?";

        try (Connection conn = Util.connect()) {
            PreparedStatement pstmt = conn.prepareStatement(removeUser);

            pstmt.setInt(1, (int) id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, name, last_name, age FROM users";

        try (Connection conn = Util.connect()) {
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String lastName = rs.getString("last_name");
                byte age = rs.getByte("age");

                users.add(new User(id, name, lastName, age));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public void cleanUsersTable() {
        String clearTableSQL = "TRUNCATE TABLE users RESTART IDENTITY";

        try (Connection conn = Util.connect()) {
             Statement stmt = conn.createStatement();

            stmt.executeUpdate(clearTableSQL);
            System.out.println("Таблица успешно очищена.");

        } catch (SQLException e) {
            logger.error("Ошибка при очистке таблицы:", e);
        }
    }
}



