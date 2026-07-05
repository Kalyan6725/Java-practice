package org.example.librarymgmt.dto;

public record MemberSummaryDTO(
        String memberName,
        String membershipBranch,
        Long numberOfBorrowedBooks,
        Double totalFinesPaid) {
}

