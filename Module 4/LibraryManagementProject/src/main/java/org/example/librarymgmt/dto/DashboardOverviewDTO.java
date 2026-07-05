package org.example.librarymgmt.dto;

public record DashboardOverviewDTO(
        Long totalMembers,
        Long totalBooks,
        Double totalFinesCollected,
        String topBranch,
        String highestFinePayingMember) {
}

