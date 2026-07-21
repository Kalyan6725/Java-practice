package org.northernarc.loanmanagementproject.controller;

import org.northernarc.loanmanagementproject.dto.response.ApiResponse;
import org.northernarc.loanmanagementproject.dto.response.DashboardDTO;
import org.northernarc.loanmanagementproject.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DashboardController - Portfolio metrics for MANAGER/ADMIN.
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<ApiResponse<DashboardDTO>> getDashboard() {
        return ResponseEntity.ok(ApiResponse.success("Dashboard data retrieved successfully",
                dashboardService.getDashboardData()));
    }
}
