package com.northernArc.RestDemo.service;

import com.northernArc.RestDemo.dao.BookDao;
import com.northernArc.RestDemo.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookServiceImpl implements BookService{
    @Autowired
    private BookDao bookDao;

    @Override
    public void addBook(int id, String title, String author, double price) {
        Book book = new Book(id, title, author, price);
        bookDao.addBook(book);
    }

    @Override
    public Book getBookById(int id) {
        Book book = bookDao.getBookById(id);
        if (book != null) {
            return book;
        } else {
            return null;
        }
    }

    @Override
    public void updateBook(int id, String title, String author, double price) {
        Book book = new Book(id, title, author, price);
        bookDao.updateBook(id, book);
    }

    @Override
    public void deleteBook(int id) {
        bookDao.deleteBook(id);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDao.getAllBooks();
    }
}