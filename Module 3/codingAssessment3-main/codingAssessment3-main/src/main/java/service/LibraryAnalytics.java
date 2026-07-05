package service;

import entity.Book;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LibraryAnalytics {

    private Map<String, Book> booksMap = new HashMap<>();

    public void loadBooks(List<String> books) {
        books.size();

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
        if (n <= 0) {
            return Collections.emptyList();
        }

        return booksMap.values()
                .stream()
                .sorted(
                        Comparator.comparing(Book::getRating).reversed()
                                .thenComparing(
                                        Comparator.comparing(Book::getBorrowCount).reversed()
                                )
                                .thenComparing(
                                        Book::getBookId
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
