package com.example.booktracker;

import com.example.booktracker.models.User;
import com.example.booktracker.ui.BookListFrame;
import com.example.booktracker.ui.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Database.init();

        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame(user -> {
                SwingUtilities.invokeLater(() -> {
                    BookListFrame bf = new BookListFrame(user);
                    bf.setVisible(true);
                });
            });
            login.setVisible(true);
        });
    }
}
