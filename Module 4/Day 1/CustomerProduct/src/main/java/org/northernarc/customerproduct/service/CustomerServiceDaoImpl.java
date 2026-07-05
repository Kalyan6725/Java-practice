package org.northernarc.customerproduct.service;

import jakarta.transaction.Transactional;
import org.northernarc.customerproduct.dto.CustomerRequestDTO;
import org.northernarc.customerproduct.dto.CustomerResponseDTO;
import org.northernarc.customerproduct.dto.OrderItemResponseDTO;
import org.northernarc.customerproduct.dto.OrderSummaryDTO;
import org.northernarc.customerproduct.exceptions.CustomerNotFound;
import org.northernarc.customerproduct.model.Customer;
import org.northernarc.customerproduct.model.Order;
import org.northernarc.customerproduct.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceDaoImpl implements CustomerServiceDao {
    @Autowired
    private CustomerRepository customerRepository;
     @Override
    public CustomerResponseDTO addCustomer(CustomerRequestDTO customerRequestDTO) {
         Customer customer = new Customer();
         // Set properties from customerRequestDTO to customer
         customer.setName(customerRequestDTO.getName());
         customer.setOrders(customerRequestDTO.getOrders());

         Customer savedCustomer = customerRepository.save(customer);

         CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO(savedCustomer.getId(), savedCustomer.getName(), mapToOrderResponse(savedCustomer.getOrders()));

         return customerResponseDTO;
     }

    @Override
    public CustomerResponseDTO getById(Integer id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFound("no customer found with id " + id));
        return new CustomerResponseDTO(customer.getId(), customer.getName(), mapToOrderResponse(customer.getOrders()));
    }

    @Override
    public void deleteById(Integer id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFound("no customer found with id " + id);
        }
        customerRepository.deleteById(id);
    }

    @Override
    public List<CustomerResponseDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers
                .stream()
                .map(customer -> new CustomerResponseDTO(customer.getId(), customer.getName(), mapToOrderResponse(customer.getOrders())))
                .toList();
    }

    @Override
    @Transactional
    public int updateCustomerNameById(int id, String name) {
        return customerRepository.updateCustomerName(id, name);
    }

    private List<OrderSummaryDTO> mapToOrderResponse(
            List<Order> orders) {
        return orders.stream()
                .map(order -> new OrderSummaryDTO(order.getId(), order.getOrderDate(),
                        order.getOrderItems().stream()
                                .map(item -> new OrderItemResponseDTO(
                                        item.getId(),
                                        item.getQuantity(),
                                        order.getId(),
                                        item.getProduct().getId()))
                                .toList()))
                .toList();
    }
}