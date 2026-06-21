package com.northernArc.RestDemo.dao;

import com.northernArc.RestDemo.entity.Book;

import java.util.List;

public interface BookDao {
    public void addBook(Book book);
    public Book getBookById(int id);
    public void updateBook(int id, Book book);
    public void deleteBook(int id);
    public List<Book> getAllBooks();
}
