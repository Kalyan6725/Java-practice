package root.implmentation;

import root.entity.Book;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BookAssignment {

//    Time: 2 hours
//    Max Marks:200
//    Real-Time Digital Library Analytics Engine
//    Topics Tested:
//            •	Collections Framework
//•	Generics
//•	Comparable vs Comparator
//•	HashMap / TreeMap / LinkedHashMap
//•	TreeSet / PriorityQueue
//•	Stream API
//•	Custom Collectors
//•	String Manipulation
//•	Immutable Objects
//•	Functional Programming
//•	Data Structures Design
//            ________________________________________
//    Problem Statement
//    A digital library receives millions of book transactions daily.
//    Each transaction is represented as:
//    BOOK_ID|TITLE|AUTHOR|CATEGORY|BORROW_COUNT|RATING
//    Example:
//    B101|Clean Code|Robert Martin|Programming|145|4.8
//    B102|Effective Java|Joshua Bloch|Programming|180|4.9
//    B103|Atomic Habits|James Clear|SelfHelp|200|4.7
//    The system should maintain books efficiently and support complex analytical queries.
//    Unfortunately the implementation is incomplete.
//    Participants must complete the missing code.________________________________________
//    Provided Classes
//    ________________________________________
//
//

        private Map<String, Book> booksMap = new HashMap<>();

        public void loadBooks(List<String> books) {

            for (String book : books) {
                if (book == null || book.isBlank()) {
                    continue;
                }
                String[] splitted = book.split("\\|");
                if (splitted.length != 6) {
                    continue;
                }
                String id = splitted[0].trim();
                String name = splitted[1].trim();
                String author = splitted[2].trim();
                String category = splitted[3].trim();
                int borrowCount;
                double rating;
                try {
                    borrowCount = Integer.parseInt(splitted[4].trim());
                    rating = Double.parseDouble(splitted[5].trim());
                } catch (Exception e) {
                    continue;
                }
                if (id.isEmpty() || name.isEmpty()|| author.isEmpty()|| category.isEmpty()|| borrowCount < 0
                        || rating < 0 || rating > 5) { continue;}
                Book bookObj =
                        new Book(id, name, author, category, borrowCount, rating);
                if (!booksMap.containsKey(id)) {
                    booksMap.put(id, bookObj);
                } else {
                    Book existing = booksMap.get(id);
                    boolean override = false;
                    if (bookObj.getRating() > existing.getRating()) {
                        override = true;
                    } else if (bookObj.getRating() == existing.getRating()) {
                        if (bookObj.getBorrowCount() > existing.getBorrowCount()) {
                            override = true;
                        } else if (bookObj.getBorrowCount() == existing.getBorrowCount()) {
                            if (bookObj.getTitle().compareTo(existing.getTitle()) < 0) {
                                override = true;
                            }
                        }
                    }
                    if (override) {
                        booksMap.put(id, bookObj);
                    }
                }
            }
        }

        public List<Book> topRatedBooks(int n) {

            return booksMap.values()
                    .stream()
                    .sorted(
                            Comparator.comparing(Book::getRating).reversed()
                                    .thenComparing(
                                            Comparator.comparing(Book::getBorrowCount).reversed()
                                    )
                                    .thenComparing(
                                            Book::getTitle
                                    )
                    )
                    .limit(n)
                    .toList();
        }

        public Map<String, Double> averageRatingByCategory() {

            Map<String, Double> result = booksMap.values() .stream()
                    .collect(
                            Collectors.groupingBy(
                                    Book::getCategory,
                                    TreeMap::new,
                                    Collectors.averagingDouble(
                                            Book::getRating
                                    )
                            )
                    );
            result.replaceAll(
                    (k, v) -> Math.round(v * 100.0) / 100.0
            );
            return result;
        }

        public Optional<Book> mostBorrowedBook() {

            return booksMap.values()
                    .stream()
                    .max(
                            Comparator.comparing(Book::getBorrowCount)
                                    .thenComparing(
                                            Comparator.comparing(Book::getRating)
                                                    .thenComparing(
                                                            Comparator.comparing(Book::getTitle)
                                                                    .reversed()
                                                    )
                                    )
                    );
        }

        public Set<String> authorsWithMultipleCategories() {

            Map<String, Set<String>> authorMap =
                    booksMap.values()
                            .stream()
                            .collect(
                                    Collectors.groupingBy(
                                            Book::getAuthor,
                                            Collectors.mapping(
                                                    Book::getCategory,
                                                    Collectors.toSet()
                                            )
                                    )
                            );
            return authorMap.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().size() > 1)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toCollection(TreeSet::new));
        }

        public Map<String, List<Book>> groupBooksByAuthor() {

            return booksMap.values()
                    .stream()
                    .sorted(
                            Comparator.comparing(Book::getAuthor)
                                    .thenComparing(
                                            Comparator.comparing(
                                                    Book::getRating
                                            ).reversed()
                                    )
                                    .thenComparing(
                                            Comparator.comparing(
                                                    Book::getBorrowCount
                                            ).reversed()
                                    )
                    )
                    .collect(
                            Collectors.groupingBy(
                                    Book::getAuthor,
                                    LinkedHashMap::new,
                                    Collectors.toList()
                            )
                    );
        }

        public List<String> suspiciousBooks() {

            Map<String, Double> avgBorrowMap =
                    booksMap.values()
                            .stream()
                            .collect(
                                    Collectors.groupingBy(
                                            Book::getCategory,
                                            Collectors.averagingDouble(
                                                    Book::getBorrowCount
                                            )
                                    )
                            );

            Map<String, Double> avgRatingMap =
                    booksMap.values()
                            .stream()
                            .collect(
                                    Collectors.groupingBy(
                                            Book::getCategory,
                                            Collectors.averagingDouble(
                                                    Book::getRating
                                            )
                                    )
                            );

            return booksMap.values()
                    .stream()
                    .filter(book -> {

                        String title = book.getTitle();
                        String lowerTitle = title.toLowerCase();

                        boolean condition1 =
                                IntStream.range(0,
                                                lowerTitle.split("\\s+").length - 1)
                                        .anyMatch(i -> {
                                            String[] words =
                                                    lowerTitle.split("\\s+");
                                            return words[i]
                                                    .equals(words[i + 1]);
                                        });

                        boolean condition2 =
                                lowerTitle.contains(
                                        book.getAuthor()
                                                .toLowerCase()
                                );

                        boolean condition3 =
                                book.getBorrowCount()
                                        >
                                        3 * avgBorrowMap.get(
                                                book.getCategory()
                                        );

                        boolean condition4 =
                                book.getRating()
                                        <
                                        avgRatingMap.get(
                                                book.getCategory()
                                        )
                                        &&
                                        book.getBorrowCount()
                                                >
                                                avgBorrowMap.get(
                                                        book.getCategory()
                                                );

                        return condition1
                                || condition2
                                || condition3
                                || condition4;
                    })
                    .map(Book::getTitle)
                    .distinct()
                    .sorted()
                    .toList();
        }
        public Map<String,Map<String,Book>> categoryWiseTopRatedBookByEachAuthor() {

            return booksMap.values()
                    .stream().collect(Collectors.groupingBy(
                            Book::getCategory,
                            TreeMap::new,
                            Collectors.toMap(
                                    Book::getAuthor,
                                    b -> b,
                                    (b1, b2) -> {
                                        if (b1.getRating() > b2.getRating()) {
                                            return b1;
                                        }
                                        if (b2.getRating() > b1.getRating()) {
                                            return b2;
                                        }
                                        if (b1.getBorrowCount() >= b2.getBorrowCount()) {
                                            return b1;
                                        }
                                        return b2;
                                    },
                                    TreeMap::new)
                    ));
        }
    }



}
//    ________________________________________Business Rules
//    ________________________________________
//    Rule 1(marks:5)
//    While loading(adding records to map) records:
//    BOOK_ID must be unique.
//    If duplicate BOOK_ID found:
//    Keep only the record having:
//    Higher Rating
//
//    If ratings equal:
//    Higher Borrow Count
//
//    If both equal:
//    Lexicographically smaller Title
//            ________________________________________
//    Rule 2(marks:5)
//    Ignore records if:
//    Rating < 0
//    Rating > 5
//    or
//    BorrowCount < 0
//    or
//    Any field empty
//            ________________________________________
//
//    Rule 3(marks:20)
//    topRatedBooks(n)
//    Sort by:
//    Rating DESC
//    If rating is same then
//    Borrow Count DESC
//    If borrow count same then
//    Title ASC
//    Return first n books.
//    ________________________________________
//    Rule 4(marks:20)
//    averageRatingByCategory()
//    Return:
//    TreeMap
//    sorted alphabetically by category.
//    Average rounded to:
//            2 decimal places
//    ________________________________________
//    Rule 5(marks:20)
//    mostBorrowedBook()
//    Return book with:
//    Highest borrow count
//
//    If tie:
//    Highest rating
//

//    ________________________________________Business Rules
//    ________________________________________
//    Rule 1(marks:5)
//    While loading(adding records to map) records:
//    BOOK_ID must be unique.
//    If duplicate BOOK_ID found:
//    Keep only the record having:
//    Higher Rating
//
//    If ratings equal:
//    Higher Borrow Count
//
//    If both equal:
//    Lexicographically smaller Title
//            ________________________________________
//    Rule 2(marks:5)
//    Ignore records if:
//    Rating < 0
//    Rating > 5
//    or
//    BorrowCount < 0
//    or
//    Any field empty
//            ________________________________________
//
//    Rule 3(marks:20)
//    topRatedBooks(n)
//    Sort by:
//    Rating DESC
//    If rating is same then
//    Borrow Count DESC
//    If borrow count same then
//    Title ASC
//    Return first n books.
//    ________________________________________
//    Rule 4(marks:20)
//    averageRatingByCategory()
//    Return:
//    TreeMap
//    sorted alphabetically by category.
//    Average rounded to:
//            2 decimal places
//    ________________________________________
//    Rule 5(marks:20)
//    mostBorrowedBook()
//    Return book with:
//    Highest borrow count
//
//    If tie:
//    Highest rating
//
//    If tie:
//    Smallest bookId
//    ________________________________________
//    Rule 6(marks:10)
//    authorsWithMultipleCategories()
//    Return authors who have books in more than one category.
//    Output must be:
//    TreeSet
//            ________________________________________
//    Rule 7(marks:20)
//    groupBooksByAuthor()
//    Return:
//    LinkedHashMap
//    ordered by:
//    Author Name ASC
//    Within each list:
//    Rating DESC
//    Borrow Count DESC
//            ________________________________________
//    Rule 8(Marks: 40)
//    Implement:
//    suspiciousBooks()
//    A book is suspicious if ANY condition is true.
//    ________________________________________
//    Condition 1
//    Title contains repeated consecutive word.
//            Example:
//    Java Java Mastery
//    Clean Clean Code
//            ________________________________________
//    Condition 2
//    Author name appears in title.
//            Example:
//    James Clear Habits
//    Robert Martin Guide
//            ________________________________________
//    Condition 3
//    Borrow count greater than average borrow count of category by 300%.
//    Example:
//    Category Average:
//            100
//    Book Borrow:
//            301
//    Suspicious
//
//---
//
//    Condition 4
//
//    Rating is below category average but borrow count is above category average.
//
//            ---
//
//    Return:
//
//            ```java
//    Distinct Titles
//
//    Sorted alphabetically
//
//    No loops allowed
//    Must use:
//    Streams only
//    ________________________________________
//
//    Final Challenge(Marks: 60)
//    Without modifying existing methods, implement:
//    public Map<String, Map<String,Book>>  categoryWiseTopRatedBookByEachAuthor()
//    Output:
//    Programming
//    Joshua Bloch -> Effective Java
//
//    Programming
//    Robert Martin -> Clean Code
//    Using streams api.
//
//}
