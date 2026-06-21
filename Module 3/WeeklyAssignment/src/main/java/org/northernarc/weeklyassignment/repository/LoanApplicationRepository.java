package org.northernarc.weeklyassignment.repository;

import org.northernarc.weeklyassignment.entity.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface LoanApplicationRepository
        extends JpaRepository<LoanApplication, String> {
}

