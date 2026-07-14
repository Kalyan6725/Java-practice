package org.northernarc.testingassignment.service.impl;

import org.northernarc.testingassignment.dto.CustomerRequest;
import org.northernarc.testingassignment.dto.CustomerResponse;
import org.northernarc.testingassignment.entity.Customer;
import org.northernarc.testingassignment.service.CustomerService;

import java.util.List;
import java.util.Collections;

public class CustomerServiceImpl implements CustomerService {

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        return new CustomerResponse();
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {
        return new CustomerResponse();
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return Collections.emptyList();
    }

    @Override
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        return new CustomerResponse();
    }

    @Override
    public void deleteCustomer(Long id) {
    }

    @Override
    public Customer findByEmail(String email) {
        return new Customer();
    }
}
