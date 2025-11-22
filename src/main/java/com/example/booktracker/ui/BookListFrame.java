package com.example.booktracker.ui;

import com.example.booktracker.dao.BookDao;
import com.example.booktracker.models.Book;
import com.example.booktracker.models.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class BookListFrame extends JFrame {
    private final DefaultListModel<Book> model = new DefaultListModel<>();
    private final JList<Book> list = new JList<>(model);

    public BookListFrame(User user) {
        setTitle("BookTracker — " + user.name);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField search = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        JButton refresh = new JButton("Refresh");
        JComboBox<String> sort = new JComboBox<>(new String[] { "rating (high to low)", "author (A-Z)" });
        top.add(new JLabel("Search:"));
        top.add(search);
        top.add(searchBtn);
        top.add(new JLabel("Sort by:"));
        top.add(sort);
        top.add(refresh);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton addToList = new JButton("Add to My Reading List");
        JButton viewMyList = new JButton("View My Reading List");
        JButton recommend = new JButton("Get Recommendations");
        bottom.add(addToList);
        bottom.add(viewMyList);
        bottom.add(recommend);
        add(bottom, BorderLayout.SOUTH);

        refresh.addActionListener(e -> loadAll());
        searchBtn.addActionListener(e -> {
            try {
                load(BookDao.search(search.getText().trim()));
            } catch (SQLException ex) {
                showErr(ex);
            }
        });
        sort.addActionListener(e -> {
            try {
                String choice = (String) sort.getSelectedItem();
                String field = choice.contains("rating") ? "rating" : "author";
                load(BookDao.sortBy(field));
            } catch (SQLException ex) {
                showErr(ex);
            }
        });
        recommend.addActionListener(e -> recommendBySimpleLogic());
        addToList.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "✓ Feature coming soon!");
        });
        viewMyList.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "✓ Feature coming soon!");
        });

        loadAll();
    }

    private void loadAll() {
        try {
            load(BookDao.listAll());
        } catch (SQLException ex) {
            showErr(ex);
        }
    }

    private void load(List<Book> books) {
        model.clear();
        for (Book b : books)
            model.addElement(b);
    }

    private void recommendBySimpleLogic() {
        try {
            load(BookDao.sortBy("rating"));
            JOptionPane.showMessageDialog(this, "⭐ Showing top-rated books as recommendations.");
        } catch (SQLException ex) {
            showErr(ex);
        }
    }

    private void showErr(Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
}
