package com.example.booktracker.dao;

import com.example.booktracker.Database;
import com.example.booktracker.models.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDao {
    public static List<Book> listAll() throws SQLException {
        try (Connection c = Database.getConnection();
                PreparedStatement p = c.prepareStatement("SELECT * FROM books")) {
            ResultSet rs = p.executeQuery();
            List<Book> out = new ArrayList<>();
            while (rs.next())
                out.add(map(rs));
            return out;
        }
    }

    public static List<Book> search(String q) throws SQLException {
        try (Connection c = Database.getConnection();
                PreparedStatement p = c
                        .prepareStatement("SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR genre LIKE ?")) {
            String like = "%" + q + "%";
            p.setString(1, like);
            p.setString(2, like);
            p.setString(3, like);
            ResultSet rs = p.executeQuery();
            List<Book> out = new ArrayList<>();
            while (rs.next())
                out.add(map(rs));
            return out;
        }
    }

    public static List<Book> sortBy(String field) throws SQLException {
        if (!field.equals("rating") && !field.equals("author"))
            field = "rating";
        String sql = "SELECT * FROM books ORDER BY " + field + (field.equals("rating") ? " DESC" : " ASC");
        try (Connection c = Database.getConnection();
                PreparedStatement p = c.prepareStatement(sql)) {
            ResultSet rs = p.executeQuery();
            List<Book> out = new ArrayList<>();
            while (rs.next())
                out.add(map(rs));
            return out;
        }
    }

    private static Book map(ResultSet rs) throws SQLException {
        return new Book(rs.getInt("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getDouble("rating"),
                rs.getString("genre"),
                rs.getInt("pages"));
    }
}
