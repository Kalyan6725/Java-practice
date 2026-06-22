package org.northernarc.customerproduct.service;

import org.northernarc.customerproduct.exceptions.CustomerNotFound;
import org.northernarc.customerproduct.model.Customer;
import org.northernarc.customerproduct.repository.CustomerRepository;
import org.northernarc.customerproduct.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceDaoImpl implements CustomerServiceDao {
    @Autowired
    private CustomerRepository customerRepository;
     @Override
    public Customer addCustomer(Customer customer) {
         return customerRepository.save(customer);
     }

    @Override
    public Customer getById(Integer id) {
        return customerRepository.findById(id).orElseThrow(()->new CustomerNotFound("no customer found with id "+id));
    }

    @Override
    public void deleteById(Integer id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFound("no customer found with id " + id);
        }
        customerRepository.deleteById(id);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
