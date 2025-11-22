package com.example.booktracker.ui;

import com.example.booktracker.dao.UserDao;
import com.example.booktracker.models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    public interface LoginListener {
        void onLogin(User user);
    }

    public LoginFrame(LoginListener listener) {
        setTitle("BookTracker — Login / Signup");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        var panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JTextField nameF = new JTextField();
        JTextField emailF = new JTextField();
        JPasswordField passF = new JPasswordField();

        panel.add(new JLabel("Name (for signup):"));
        panel.add(nameF);
        panel.add(new JLabel("Email:"));
        panel.add(emailF);
        panel.add(new JLabel("Password:"));
        panel.add(passF);

        JButton signup = new JButton("Sign Up");
        JButton login = new JButton("Log In");
        panel.add(signup);
        panel.add(login);

        add(panel);

        signup.addActionListener((ActionEvent e) -> {
            String name = nameF.getText().trim();
            String email = emailF.getText().trim();
            String pass = new String(passF.getPassword());
            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all fields");
                return;
            }
            try {
                User u = UserDao.createUser(name, email, pass);
                JOptionPane.showMessageDialog(this, "✓ Signed up! Logging in...");
                listener.onLogin(u);
                dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        login.addActionListener((ActionEvent e) -> {
            String email = emailF.getText().trim();
            String pass = new String(passF.getPassword());
            try {
                User u = UserDao.findByEmailAndPassword(email, pass);
                if (u == null)
                    JOptionPane.showMessageDialog(this, "✗ Invalid credentials");
                else {
                    listener.onLogin(u);
                    dispose();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
    }
}
