package com.example.booktracker;

import java.sql.*;

public class Database {
    private static final String URL = "jdbc:sqlite:booktracker.db";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL);
    }

    public static void init() {
        try (Connection c = getConnection();
                Statement s = c.createStatement()) {

            s.execute("PRAGMA foreign_keys = ON;");

            s.execute("""
                        CREATE TABLE IF NOT EXISTS users (
                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                          name TEXT NOT NULL,
                          email TEXT NOT NULL UNIQUE,
                          password TEXT NOT NULL
                        );
                    """);

            s.execute("""
                        CREATE TABLE IF NOT EXISTS books (
                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                          title TEXT NOT NULL,
                          author TEXT,
                          rating REAL,
                          genre TEXT,
                          pages INTEGER
                        );
                    """);

            s.execute("""
                        CREATE TABLE IF NOT EXISTS reading_list (
                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                          user_id INTEGER NOT NULL,
                          book_id INTEGER NOT NULL,
                          pages_read INTEGER DEFAULT 0,
                          status TEXT DEFAULT 'Not Started',
                          FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
                          FOREIGN KEY(book_id) REFERENCES books(id) ON DELETE CASCADE
                        );
                    """);

            ResultSet rs = s.executeQuery("SELECT COUNT(*) AS cnt FROM books");
            if (rs.next() && rs.getInt("cnt") == 0) {
                s.execute("INSERT INTO books (title,author,rating,genre,pages) VALUES " +
                        "('Introduction to Algorithms','Cormen',4.5,'Algorithms',1312)," +
                        "('Cracking the Coding Interview','Gayle Laakmann McDowell',4.4,'Interview',706)," +
                        "('Algorithms','Sedgewick',4.1,'Algorithms',992)," +
                        "('Clean Code','Robert C. Martin',4.6,'Software',464)," +
                        "('Design Patterns','Gamma et al.',4.2,'Software',395)");
            }

            System.out.println("✓ Database initialized.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
