package org.example.librarymgmt.repository;

import java.util.List;
import org.example.librarymgmt.entity.FineTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FineTransactionRepository extends JpaRepository<FineTransaction, Long> {

    List<FineTransaction> findByBookIsbn(String isbn);

    List<FineTransaction> findByPaymentType(String paymentType);

    @Query("SELECT ft FROM FineTransaction ft ORDER BY ft.paymentDate DESC, ft.transactionId DESC")
    List<FineTransaction> findLatestFinePayment(Pageable pageable);

    @Query("SELECT COALESCE(SUM(ft.amount), 0) FROM FineTransaction ft")
    Double getTotalFinesCollected();
}

