package org.northernarc.customerproduct.service;

import org.northernarc.customerproduct.model.Customer;

import java.util.List;

public interface CustomerServiceDao {
    public Customer addCustomer(Customer customer);
    public Customer getById(Integer id);
    public void deleteById(Integer id);
    public List<Customer> getAllCustomers();
}
