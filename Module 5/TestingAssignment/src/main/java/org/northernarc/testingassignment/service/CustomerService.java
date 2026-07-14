package org.northernarc.testingassignment.service;

import org.northernarc.testingassignment.dto.CustomerRequest;
import org.northernarc.testingassignment.dto.CustomerResponse;
import org.northernarc.testingassignment.entity.Customer;
import java.util.List;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequest request);
    CustomerResponse getCustomerById(Long id);
    List<CustomerResponse> getAllCustomers();
    CustomerResponse updateCustomer(Long id, CustomerRequest request);
    void deleteCustomer(Long id);
    Customer findByEmail(String email);
}
