package com.example.booktracker.dao;

import com.example.booktracker.Database;
import com.example.booktracker.models.User;

import java.sql.*;

public class UserDao {
    public static User createUser(String name, String email, String password) throws SQLException {
        try (Connection c = Database.getConnection();
                PreparedStatement p = c.prepareStatement("INSERT INTO users(name,email,password) VALUES(?,?,?)",
                        Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, name);
            p.setString(2, email);
            p.setString(3, password);
            p.executeUpdate();
            ResultSet rs = p.getGeneratedKeys();
            if (rs.next())
                return new User(rs.getInt(1), name, email);
            return null;
        }
    }

    public static User findByEmailAndPassword(String email, String password) throws SQLException {
        try (Connection c = Database.getConnection();
                PreparedStatement p = c
                        .prepareStatement("SELECT id,name,email FROM users WHERE email=? AND password=?")) {
            p.setString(1, email);
            p.setString(2, password);
            ResultSet rs = p.executeQuery();
            if (rs.next())
                return new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"));
            return null;
        }
    }
}
