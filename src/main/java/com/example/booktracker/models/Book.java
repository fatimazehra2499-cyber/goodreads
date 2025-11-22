package com.example.booktracker.models;

public class Book {
    public int id;
    public String title;
    public String author;
    public double rating;
    public String genre;
    public int pages;

    public Book(int id, String title, String author, double rating, String genre, int pages) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.rating = rating;
        this.genre = genre;
        this.pages = pages;
    }

    @Override
    public String toString() {
        return title + " — " + author + " ⭐" + rating;
    }
}
