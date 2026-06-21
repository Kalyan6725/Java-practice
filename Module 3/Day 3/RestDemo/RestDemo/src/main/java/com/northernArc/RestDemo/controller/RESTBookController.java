package com.northernArc.RestDemo.controller;

import com.northernArc.RestDemo.entity.Book;
import com.northernArc.RestDemo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Component
@RestController
@RequestMapping("/books")
public class RESTBookController {
    @Autowired
    private BookService bookService;

    @GetMapping("")
    public ResponseEntity<List<Book>> getBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") int id) {
        Book book = bookService.getBookById(id);
        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{id}/{title}/{author}/{price}")
    public ResponseEntity<String> updateBook(@PathVariable("id") int id, @PathVariable("title") String title, @PathVariable("author") String author, @PathVariable("price") double price) {
        bookService.updateBook(id, title, author, price);
        return ResponseEntity.ok("Book updated successfully");
    }
    @PostMapping
    public ResponseEntity<String> addBook(@RequestBody Book book){
        bookService.addBook(book.getId(), book.getTitle(), book.getAuthor(), book.getPrice());
        return ResponseEntity.ok("Book added successfully");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable("id") int id){
        bookService.deleteBook(id);
        return ResponseEntity.ok("Book deleted successfully");
    }
}
