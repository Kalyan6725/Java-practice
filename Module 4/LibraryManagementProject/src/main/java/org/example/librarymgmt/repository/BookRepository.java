package org.example.librarymgmt.repository;

import java.util.List;
import org.example.librarymgmt.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BookRepository extends JpaRepository<Book, String> {

	List<Book> findByBookType(String bookType);

	List<Book> findByDailyFineRateGreaterThan(double amount);

	@Query("SELECT b FROM Book b LEFT JOIN b.fineTransactions ft WHERE ft.transactionId IS NULL")
	List<Book> findBooksWithNoOverdueHistory();

	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Transactional
	@Query("UPDATE Book b SET b.dailyFineRate = b.dailyFineRate + :increment WHERE b.bookType IN :bookTypes")
	int increaseDailyFineRates(@Param("bookTypes") List<String> bookTypes, @Param("increment") double increment);
}

