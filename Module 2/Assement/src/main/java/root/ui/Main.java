package root.ui;

import root.entity.Book;
import root.implmentation.BookAssignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BookAssignment bookAssignment = new BookAssignment();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter the option: ");
            System.out.println("1. loadBooks");
            System.out.println("2. topRatedBooks");
            System.out.println("3. averageRatingByCategory");
            System.out.println("4. mostBorrowedBook");
            System.out.println("5. authorsWithMultipleCategories");
            System.out.println("6. groupBooksByAuthor");
            System.out.println("7. suspiciousBooks");
            System.out.println("8. ");
            System.out.println("9. exit");

            int choice = readInt(scanner);
            switch (choice) {
                case 1:
                    loadBooksFromInput(bookAssignment, scanner);
                    break;
                case 2:
                    System.out.print("Enter n: ");
                    int n = readInt(scanner);
                    printBooks(bookAssignment.topRatedBooks(n));
                    break;
                case 3:
                    Map<String, Double> avgRating = bookAssignment.averageRatingByCategory();
                    avgRating.forEach((category, rating) ->
                            System.out.println(category + " -> " + rating));
                    break;
                case 4:
                    Optional<Book> mostBorrowed = bookAssignment.mostBorrowedBook();
                    if (mostBorrowed.isPresent()) {
                        printBook(mostBorrowed.get());
                    } else {
                        System.out.println("No books available.");
                    }
                    break;
                case 5:
                    bookAssignment.authorsWithMultipleCategories()
                            .forEach(System.out::println);
                    break;
                case 6:
                    bookAssignment.groupBooksByAuthor().forEach((author, books) -> {
                        System.out.println(author + ":");
                        printBooks(books);
                    });
                    break;
                case 7:
                    bookAssignment.suspiciousBooks();
                    bookAssignment.suspiciousBooks().forEach(System.out::println);
                    break;

                case 8:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static int readInt(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

    private static void loadBooksFromInput(BookAssignment bookAssignment, Scanner scanner) {
        System.out.print("Enter number of book records: ");
        int count = readInt(scanner);
        List<String> records = new ArrayList<>();

        System.out.println("Enter records in format BOOK_ID|TITLE|AUTHOR|CATEGORY|BORROW_COUNT|RATING");
        for (int i = 0; i < count; i++) {
            records.add(scanner.nextLine());
        }

        bookAssignment.loadBooks(records);
        System.out.println("Books loaded successfully.");
    }

    private static void printBooks(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }

        books.forEach(Main::printBook);
    }

    private static void printBook(Book book) {
        System.out.println(
                book.getBookId() + " | "
                        + book.getTitle() + " | "
                        + book.getAuthor() + " | "
                        + book.getCategory() + " | "
                        + book.getBorrowCount() + " | "
                        + book.getRating()
        );
    }
}
