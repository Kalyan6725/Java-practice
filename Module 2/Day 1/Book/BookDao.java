public interface BookDao {
    public void save(Book book);
    public Book findById(int id);
    public void deleteById(int id);
    public void deleteAll();
    public Iterable<Book> findAllBooks();
    public void update(Book book);
    public Iterable<Book> findByAuthor(String author);
    public Iterable<Book> findByTitle(String title);
    public Iterable<Book> sortByTitleAsc();
    public Iterable<Book> sortByTitleDesc();
}