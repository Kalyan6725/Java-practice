package com.northernArc.RestDemo.service;

import com.northernArc.RestDemo.entity.Book;

import java.util.List;

public interface BookService {
    void addBook(int id, String title, String author, double price);
    Book getBookById(int id);
    void updateBook(int id, String title, String author, double price);
    void deleteBook(int id);
    List<Book> getAllBooks();
}
