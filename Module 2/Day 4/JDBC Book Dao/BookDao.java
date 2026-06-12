import java.util.List;

interface BookDao {
    void addBook(Book book);
    void updateBook(int id, Book book);
    void deleteBook(int id);
    Book getBookById(int id);
    List<Book> getAllBooks();
    List<Book> findByAuthor(String author);
    List<Book> findByGenre(String genre);
    List<Book> findByTitle(String title);
}