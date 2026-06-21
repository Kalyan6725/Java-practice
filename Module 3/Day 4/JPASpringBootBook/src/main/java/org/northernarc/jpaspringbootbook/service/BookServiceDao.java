package org.northernarc.jpaspringbootbook.service;

import org.northernarc.jpaspringbootbook.entity.Book;

import java.util.List;

public interface BookServiceDao {
    public void addBook(Book book);
    Book getBookById(int id);
    boolean updateBook(int id, Book book);
    void deleteBook(int id);
    List<Book> getAllBooks();
}
