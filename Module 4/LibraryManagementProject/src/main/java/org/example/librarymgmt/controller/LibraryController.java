package org.example.librarymgmt.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.librarymgmt.dto.BranchFineTotalDTO;
import org.example.librarymgmt.dto.MemberSummaryDTO;
import org.example.librarymgmt.entity.Book;
import org.example.librarymgmt.entity.FineTransaction;
import org.example.librarymgmt.entity.IssueRecord;
import org.example.librarymgmt.entity.Member;
import org.example.librarymgmt.service.LibraryService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
@Validated
public class LibraryController {

    private final LibraryService libraryService;

    @PostMapping("/auth/register")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<Member> register(@Valid @RequestBody Member member) {
        Member saved = libraryService.registerMember(member);
        saved.setPassword(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/books")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryService.addBook(book));
    }

    @DeleteMapping("/books/{isbn}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable String isbn) {
        libraryService.deleteBook(isbn);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/books")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<Page<Book>> getBooks(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int size) {
        return ResponseEntity.ok(libraryService.getAllBooks(page, size));
    }

    @GetMapping("/books/type/{bookType}")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<List<Book>> getBooksByType(@PathVariable String bookType) {
        return ResponseEntity.ok(libraryService.getBooksByType(bookType));
    }

    @GetMapping("/books/fine-rate")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<List<Book>> getBooksByFineRate(@RequestParam @PositiveOrZero double amount) {
        return ResponseEntity.ok(libraryService.getBooksWithFineRateGreaterThan(amount));
    }

    @GetMapping("/books/no-overdue-history")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<List<Book>> getBooksWithNoOverdueHistory() {
        return ResponseEntity.ok(libraryService.getBooksWithNoOverdueHistory());
    }

    @PatchMapping("/books/fine-rate/increase")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Map<String, Integer>> increaseDailyFineRates(
            @RequestParam List<String> bookTypes,
            @RequestParam @Positive double increment) {
        int updatedCount = libraryService.increaseDailyFineRates(bookTypes, increment);
        Map<String, Integer> response = new HashMap<>();
        response.put("updatedBooks", updatedCount);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/issues")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<IssueRecord> issueBook(@RequestParam Long memberId, @RequestParam String isbn) {
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryService.issueBook(memberId, isbn));
    }

    @PutMapping("/issues/{issueId}/return")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<IssueRecord> returnBook(@PathVariable Long issueId) {
        return ResponseEntity.ok(libraryService.returnBook(issueId));
    }

    @GetMapping("/members/{memberId}/issues")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<List<IssueRecord>> getMemberIssues(@PathVariable Long memberId) {
        return ResponseEntity.ok(libraryService.getIssuesByMember(memberId));
    }

    @GetMapping("/members/branch/{branch}")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<List<Member>> getMembersByBranch(@PathVariable String branch) {
        return ResponseEntity.ok(libraryService.getMembersByBranch(branch));
    }

    @GetMapping("/members/avid-readers")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<List<Member>> getAvidReaders(@RequestParam(defaultValue = "3") @PositiveOrZero long targetCount) {
        return ResponseEntity.ok(libraryService.getAvidReaders(targetCount));
    }

    @GetMapping("/members/multi-genre")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<List<Member>> getMembersHoldingMultiGenreBookTypes(
            @RequestParam(defaultValue = "2") @Positive long minDistinctTypes) {
        return ResponseEntity.ok(libraryService.getMembersHoldingMultiGenreBookTypes(minDistinctTypes));
    }

    @GetMapping("/members/summary")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<List<MemberSummaryDTO>> getMemberSummaries() {
        return ResponseEntity.ok(libraryService.getMemberSummaries());
    }

    @GetMapping("/reports/fines/branch")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<List<BranchFineTotalDTO>> getTotalFinesPaidPerBranch() {
        return ResponseEntity.ok(libraryService.getTotalFinesPaidPerBranch());
    }

    @PostMapping("/fines")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<FineTransaction> recordFine(
            @RequestParam String isbn,
            @RequestParam @Positive Double amount,
            @RequestParam String paymentType) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(libraryService.recordFine(isbn, amount, paymentType));
    }

    @GetMapping("/books/{isbn}/fines")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<List<FineTransaction>> getBookFines(@PathVariable String isbn) {
        return ResponseEntity.ok(libraryService.getFinesByBook(isbn));
    }

    @GetMapping("/fines/payment-type/{paymentType}")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<List<FineTransaction>> getFinesByPaymentType(@PathVariable String paymentType) {
        return ResponseEntity.ok(libraryService.getFinesByPaymentType(paymentType));
    }

    @GetMapping("/fines/latest")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<FineTransaction> getLatestFinePayment() {
        return libraryService.getLatestFinePayment()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}

