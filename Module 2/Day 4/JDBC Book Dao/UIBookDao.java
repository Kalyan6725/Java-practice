import java.util.Scanner;
import java.util.List;

class UIBookDao {
    public static void main(String[] args) {
        BookDao bookDao = new BookDaoImpl();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Enter choice to perform:\n1. Add Book\n2. Update Book\n3. Delete Book\n4. Get Book By Id\n5. Get All Books\n6. Find By Author\n7. Find By Genre\n8. Find By Title\n9. Exit");
            int choice = sc.nextInt();
            sc.nextLine();

        switch(choice){
            case 1:
                System.out.println("Enter book details (id(int), title(String), author(String), genre(String)):");
                int id = sc.nextInt();
                sc.nextLine();
                String title = sc.nextLine();
                String author = sc.nextLine();
                String genre = sc.nextLine();
                Book book = new Book(id, title, author, genre);
                bookDao.addBook(book);
                break;
            case 2:
                System.out.println("Enter book ID to update:");
                int updateId = sc.nextInt();
                sc.nextLine();
                System.out.println("Enter new book details (title(String), author(String), genre(String)):");
                String newTitle = sc.nextLine();
                String newAuthor = sc.nextLine();
                String newGenre = sc.nextLine();
                Book updatedBook = new Book(updateId, newTitle, newAuthor, newGenre);
                bookDao.updateBook(updateId, updatedBook);
                break;
            case 3:
                System.out.println("Enter book ID to delete:");
                int deleteId = sc.nextInt();
                bookDao.deleteBook(deleteId);
                break;
            case 4:
                System.out.println("Enter book ID to retrieve:");
                int retrieveId = sc.nextInt();
                System.out.println(bookDao.getBookById(retrieveId));
                break;
            case 5:
                System.out.println(bookDao.getAllBooks());
                break;
            case 6:
                System.out.println("Enter author name:");
                String authorName = sc.nextLine();
                System.out.println(bookDao.findByAuthor(authorName));
                break;
            case 7:
                System.out.println("Enter genre:");
                String genreName = sc.nextLine();
                System.out.println(bookDao.findByGenre(genreName));
                break;
            case 8:
                System.out.println("Enter title:");
                String titleName = sc.nextLine();
                System.out.println(bookDao.findByTitle(titleName));
                break;
            case 9:
                System.out.println("Exiting...");
                return;
            default:
                System.out.println("Invalid choice");
        }
        }
    }
}