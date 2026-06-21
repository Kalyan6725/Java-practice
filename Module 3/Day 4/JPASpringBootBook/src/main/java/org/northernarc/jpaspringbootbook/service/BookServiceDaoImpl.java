package org.northernarc.jpaspringbootbook.service;

import org.northernarc.jpaspringbootbook.entity.Book;
import org.northernarc.jpaspringbootbook.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class BookServiceDaoImpl implements BookServiceDao{

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void addBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public Book getBookById(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public boolean updateBook(int id, Book book) {
        Book existingPerson = bookRepository.findById(id).orElse(null);
        if (existingPerson == null) {
            return false;
        }

        existingPerson.setTitle(book.getTitle());
        existingPerson.setAuthor(book.getAuthor());
        existingPerson.setPrice(book.getPrice());
        bookRepository.save(existingPerson);
        return true;
    }

    @Override
    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}
