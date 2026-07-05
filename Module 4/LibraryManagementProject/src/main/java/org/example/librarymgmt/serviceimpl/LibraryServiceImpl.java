package org.example.librarymgmt.serviceimpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.librarymgmt.dto.BranchFineTotalDTO;
import org.example.librarymgmt.dto.DashboardOverviewDTO;
import org.example.librarymgmt.dto.MemberSummaryDTO;
import org.example.librarymgmt.entity.Book;
import org.example.librarymgmt.entity.FineTransaction;
import org.example.librarymgmt.entity.IssueRecord;
import org.example.librarymgmt.entity.Member;
import org.example.librarymgmt.exception.BookNotFoundException;
import org.example.librarymgmt.exception.MemberNotFoundException;
import org.example.librarymgmt.exception.ValidationException;
import org.example.librarymgmt.repository.BookRepository;
import org.example.librarymgmt.repository.FineTransactionRepository;
import org.example.librarymgmt.repository.IssueRecordRepository;
import org.example.librarymgmt.repository.MemberRepository;
import org.example.librarymgmt.service.LibraryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final IssueRecordRepository issueRecordRepository;
    private final FineTransactionRepository fineTransactionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member registerMember(Member member) {
        memberRepository.findByEmail(member.getEmail()).ifPresent(existing -> {
            throw new ValidationException("Email is already registered");
        });
        if (member.getRole() == null || member.getRole().isBlank()) {
            member.setRole("USER");
        }
        member.setRole(member.getRole().trim().toUpperCase());
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member);
    }

    @Override
    public Optional<Member> authenticateMember(String email, String password) {
        return memberRepository.findByEmail(email)
                .filter(member -> passwordEncoder.matches(password, member.getPassword()));
    }

    @Override
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(String isbn) {
        Book book = bookRepository.findById(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
        bookRepository.delete(book);
    }

    @Override
    public Page<Book> getAllBooks(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dailyFineRate"));
        return bookRepository.findAll(pageRequest);
    }

    @Override
    public List<Book> getBooksByType(String bookType) {
        return bookRepository.findByBookType(bookType);
    }

    @Override
    public List<Book> getBooksWithFineRateGreaterThan(double amount) {
        return bookRepository.findByDailyFineRateGreaterThan(amount);
    }

    @Override
    public IssueRecord issueBook(Long memberId, String isbn) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));
        Book book = bookRepository.findById(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        IssueRecord issueRecord = IssueRecord.builder()
                .issueDate(LocalDate.now())
                .status("ISSUED")
                .member(member)
                .book(book)
                .build();
        return issueRecordRepository.save(issueRecord);
    }

    @Override
    public IssueRecord returnBook(Long issueId) {
        IssueRecord issueRecord = issueRecordRepository.findById(issueId)
                .orElseThrow(() -> new ValidationException("Issue record not found"));

        issueRecord.setReturnDate(LocalDate.now());
        issueRecord.setStatus("RETURNED");
        return issueRecordRepository.save(issueRecord);
    }

    @Override
    public List<IssueRecord> getIssuesByMember(Long memberId) {
        return issueRecordRepository.findByMemberMemberId(memberId);
    }

    @Override
    public List<Member> getMembersByBranch(String branch) {
        return memberRepository.findByMembershipBranch(branch);
    }

    @Override
    public List<Member> getAvidReaders(long targetCount) {
        return memberRepository.findAvidReaders(targetCount);
    }

    @Override
    public List<BranchFineTotalDTO> getTotalFinesPaidPerBranch() {
        return memberRepository.findTotalFinesPaidPerBranch();
    }

    @Override
    public List<Member> getMembersHoldingMultiGenreBookTypes(long minDistinctTypes) {
        return memberRepository.findMembersHoldingMultiGenreBookTypes(minDistinctTypes);
    }

    @Override
    public List<MemberSummaryDTO> getMemberSummaries() {
        return memberRepository.findMemberSummaries();
    }

    @Override
    public FineTransaction recordFine(String isbn, Double amount, String paymentType) {
        Book book = bookRepository.findById(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        FineTransaction fineTransaction = FineTransaction.builder()
                .amount(amount)
                .paymentType(paymentType)
                .paymentDate(LocalDate.now())
                .book(book)
                .build();
        return fineTransactionRepository.save(fineTransaction);
    }

    @Override
    public List<FineTransaction> getFinesByBook(String isbn) {
        return fineTransactionRepository.findByBookIsbn(isbn);
    }

    @Override
    public List<FineTransaction> getFinesByPaymentType(String paymentType) {
        return fineTransactionRepository.findByPaymentType(paymentType);
    }

    @Override
    public Optional<FineTransaction> getLatestFinePayment() {
        List<FineTransaction> latestTransactions = fineTransactionRepository.findLatestFinePayment(PageRequest.of(0, 1));
        return latestTransactions.stream().findFirst();
    }

    @Override
    public List<Book> getBooksWithNoOverdueHistory() {
        return bookRepository.findBooksWithNoOverdueHistory();
    }

    @Override
    public int increaseDailyFineRates(List<String> bookTypes, double increment) {
        return bookRepository.increaseDailyFineRates(bookTypes, increment);
    }

    @Override
    public DashboardOverviewDTO getDashboardOverview() {
        long totalMembers = memberRepository.count();
        long totalBooks = bookRepository.count();
        Double totalFinesCollected = fineTransactionRepository.getTotalFinesCollected();

        String topBranch = memberRepository.findTopBranchByFineCollection(PageRequest.of(0, 1)).stream()
                .findFirst()
                .orElseGet(() -> memberRepository.findTopBranchByMemberCount(PageRequest.of(0, 1)).stream()
                        .findFirst()
                        .orElse("N/A"));

        String highestFinePayingMember = memberRepository.findHighestFinePayingMember(PageRequest.of(0, 1)).stream()
                .findFirst()
                .orElse("N/A");

        return new DashboardOverviewDTO(
                totalMembers,
                totalBooks,
                totalFinesCollected,
                topBranch,
                highestFinePayingMember);
    }
}

