package org.northernarc.customerproduct.service;

import org.northernarc.customerproduct.dto.CustomerRequestDTO;
import org.northernarc.customerproduct.dto.CustomerResponseDTO;
import org.northernarc.customerproduct.dto.RegisterRequestDTO;

import java.util.List;

public interface CustomerServiceDao {
    public CustomerResponseDTO addCustomer(CustomerRequestDTO customerRequestDTO);
    public CustomerResponseDTO registerCustomer(RegisterRequestDTO registerRequestDTO);
    public CustomerResponseDTO getById(Long id);
    public void deleteById(Long id);
    public List<CustomerResponseDTO> getAllCustomers();

    int updateCustomerNameById(Long id, String name);
}
