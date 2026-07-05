package org.example.librarymgmt.controller;

import lombok.RequiredArgsConstructor;
import org.example.librarymgmt.dto.DashboardOverviewDTO;
import org.example.librarymgmt.service.LibraryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DashboardController {

    private final LibraryService libraryService;

    @GetMapping({"/dashboard", "/api/dashboard"})
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<DashboardOverviewDTO> getDashboardOverview() {
        return ResponseEntity.ok(libraryService.getDashboardOverview());
    }
}

