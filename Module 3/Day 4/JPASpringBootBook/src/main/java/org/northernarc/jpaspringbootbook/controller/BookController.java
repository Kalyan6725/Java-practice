package org.northernarc.jpaspringbootbook.controller;

import org.northernarc.jpaspringbootbook.entity.Book;
import org.northernarc.jpaspringbootbook.service.BookServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    public BookServiceDao bookServiceDao;

    @GetMapping
    public ResponseEntity<List<Book>> getBooks() {
        return ResponseEntity.ok(bookServiceDao.getAllBooks());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") int id) {
        Book book = bookServiceDao.getBookById(id);
        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping
    public ResponseEntity<String> addBook(@RequestBody Book book) {
        bookServiceDao.addBook(book);
        return ResponseEntity.ok("Book added successfully");
    }
    @PutMapping("/{id}/{title}/{author}/{price}")
    public ResponseEntity<String> updateBook(@PathVariable("id") int id, @PathVariable("title") String title, @PathVariable("author") String author, @PathVariable("price") double price) {
        boolean updated = bookServiceDao.updateBook(id, new Book(id,title, author, price));
        if (!updated) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Book updated successfully");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable("id") int id) {
        bookServiceDao.deleteBook(id);
        return ResponseEntity.ok("Book deleted successfully");
    }

}
