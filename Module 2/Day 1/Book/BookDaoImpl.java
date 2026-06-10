import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Comparator;
import java.util.List;

class BookDaoImpl implements BookDao {
    List<Book> books;
    public BookDaoImpl() {
        this.books = new ArrayList<>();
    }
    @Override
    public void save(Book book) {
        books.add(book);
    }
    public Book findById(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
        }
        System.out.println("Book not found with id: " + id);
        return null;
    }

    @Override
    public Iterable<Book> findAllBooks() {
        return books;
    }

    @Override
    public void deleteById(int id) {
        Iterator<Book> iterator = books.iterator();
        while (iterator.hasNext()) {
            Book book = iterator.next();
            if (book.getId() == id) {
                iterator.remove();
                System.out.println("Book with id " + id + " has been deleted.");
                return;
            }
        }
        System.out.println("Book not found with id: " + id);
    }

    @Override
    public void update(Book book) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == book.getId()) {
                books.set(i, book);
                System.out.println("Book with id " + book.getId() + " has been updated.");
                return;
            }
        }
        System.out.println("Book not found with id: " + book.getId());
    }

    @Override
    public void deleteAll() {
        books.clear();
    }

    @Override
    public Iterable<Book> findByAuthor(String author) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getAuthor().equalsIgnoreCase(author)) {
                result.add(book);
            }
        }
        return result;
    }

    @Override
    public Iterable<Book> findByTitle(String title) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                result.add(book);
            }
        }
        return result;
    }

    @Override
    public Iterable<Book> sortByTitleAsc() {
        List<Book> result = new ArrayList<>(books);
        result.sort(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER));
        return result;
    }

    @Override
    public Iterable<Book> sortByTitleDesc() {
        List<Book> result = new ArrayList<>(books);
        result.sort(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER).reversed());
        return result;
    }
}