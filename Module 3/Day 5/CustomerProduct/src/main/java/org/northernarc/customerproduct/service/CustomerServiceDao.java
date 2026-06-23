package org.northernarc.customerproduct.service;

import org.northernarc.customerproduct.dto.CustomerRequestDTO;
import org.northernarc.customerproduct.dto.CustomerResponseDTO;
import org.northernarc.customerproduct.model.Customer;

import java.util.List;

public interface CustomerServiceDao {
    public CustomerResponseDTO addCustomer(CustomerRequestDTO customerRequestDTO);
    public Customer getById(Integer id);
    public void deleteById(Integer id);
    public List<CustomerResponseDTO> getAllCustomers();
}
