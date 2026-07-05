package org.example.librarymgmt.service;

import java.util.List;
import java.util.Optional;
import org.example.librarymgmt.dto.BranchFineTotalDTO;
import org.example.librarymgmt.dto.DashboardOverviewDTO;
import org.example.librarymgmt.dto.MemberSummaryDTO;
import org.example.librarymgmt.entity.Book;
import org.example.librarymgmt.entity.FineTransaction;
import org.example.librarymgmt.entity.IssueRecord;
import org.example.librarymgmt.entity.Member;
import org.springframework.data.domain.Page;

public interface LibraryService {

    Member registerMember(Member member);

    Optional<Member> authenticateMember(String email, String password);

    Book addBook(Book book);

    void deleteBook(String isbn);

    Page<Book> getAllBooks(int page, int size);

    List<Book> getBooksByType(String bookType);

    List<Book> getBooksWithFineRateGreaterThan(double amount);

    IssueRecord issueBook(Long memberId, String isbn);

    IssueRecord returnBook(Long issueId);

    List<IssueRecord> getIssuesByMember(Long memberId);

    List<Member> getMembersByBranch(String branch);

    List<Member> getAvidReaders(long targetCount);

    List<BranchFineTotalDTO> getTotalFinesPaidPerBranch();

    List<Member> getMembersHoldingMultiGenreBookTypes(long minDistinctTypes);

    List<MemberSummaryDTO> getMemberSummaries();

    FineTransaction recordFine(String isbn, Double amount, String paymentType);

    List<FineTransaction> getFinesByBook(String isbn);

    List<FineTransaction> getFinesByPaymentType(String paymentType);

    Optional<FineTransaction> getLatestFinePayment();

    List<Book> getBooksWithNoOverdueHistory();

    int increaseDailyFineRates(List<String> bookTypes, double increment);

    DashboardOverviewDTO getDashboardOverview();
}

