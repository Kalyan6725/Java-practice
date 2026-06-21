package com.northernArc.RestDemo.dao;

import com.northernArc.RestDemo.entity.Book;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookDaoImpl implements BookDao {
    List<Book> books=new ArrayList<>();
    @PostConstruct
    public void init(){
        //dumby data
        books.add(new Book(1,"Book1","Author1",100.0));
        books.add(new Book(2,"Book2","Author2",200.0));
        books.add(new Book(3,"Book3","Author3",300.0));
    }
    @Override
    public void addBook(Book book) {
        books.add(book);
    }

    @Override
    public Book getBookById(int id) {
        for(Book book:books){
            if(book.getId()==id){
                return book;
            }
        }
        return null;
    }

    @Override
    public void updateBook(int id, Book book) {
        for(int i=0;i<books.size();i++){
            if(books.get(i).getId()==id){
                books.set(i,book);
                return;
            }
        }
    }

    @Override
    public void deleteBook(int id) {
        for(int i=0;i<books.size();i++){
            if(books.get(i).getId()==id){
                books.remove(i);
                return;
            }
        }
    }

    @Override
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }
}
