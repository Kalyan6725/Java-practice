package org.northernarc.loanmanagementproject.service;

import org.northernarc.loanmanagementproject.dto.request.AdminCustomerDTO;
import org.northernarc.loanmanagementproject.dto.request.CustomerUpdateRequest;
import org.northernarc.loanmanagementproject.dto.request.RegisterRequest;
import org.northernarc.loanmanagementproject.entity.Customer;

import java.util.List;

/**
 * CustomerService - Customer lifecycle: registration, admin management and
 * safe partial updates (password preserved, role/status guarded).
 */
public interface CustomerService {

    Customer register(RegisterRequest request);

    Customer createByAdmin(AdminCustomerDTO dto);

    Customer getById(Long customerId);

    Customer getByEmail(String email);

    List<Customer> getAll();

    /**
     * Partial update. When {@code allowRoleStatus} is false the role/status
     * fields on the request are ignored (only ADMIN may change them).
     */
    Customer update(CustomerUpdateRequest request, boolean allowRoleStatus);

    void delete(Long customerId);
}
