package org.northernarc.loanmanagementproject.service;

import org.northernarc.loanmanagementproject.dto.response.DashboardDTO;

/**
 * DashboardService - Portfolio-level aggregate metrics for MANAGER/ADMIN.
 */
public interface DashboardService {

    DashboardDTO getDashboardData();
}
