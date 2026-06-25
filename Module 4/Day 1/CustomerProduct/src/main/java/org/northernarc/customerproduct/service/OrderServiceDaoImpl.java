package org.northernarc.customerproduct.service;

import org.northernarc.customerproduct.dto.OrderRequestDTO;
import org.northernarc.customerproduct.dto.OrderResponseDTO;
import org.northernarc.customerproduct.exceptions.OrderNotFound;
import org.northernarc.customerproduct.repository.CustomerRepository;
import org.northernarc.customerproduct.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.northernarc.customerproduct.model.Order;

import java.util.List;

@Service
public class OrderServiceDaoImpl implements OrderServiceDao {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CustomerRepository customerRepository;
     @Override
    public void deleteById(Long id) {
        if (!orderRepository.existsById(Math.toIntExact(id))) {
            throw new OrderNotFound("no order found with id " + id);
        }
        orderRepository.deleteById(Math.toIntExact(id));
    }

    @Override
    public OrderResponseDTO getById(Long id) {
        Order order=orderRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new OrderNotFound("no order found with id " + id));
        return new OrderResponseDTO(order.getId(), order.getOrderDate(),order.getCustomer().getId(), order.getOrderItems());
     }

    @Override
    public OrderResponseDTO updateOrder(int id, OrderRequestDTO orderRequestDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFound("no order found with id " + id));
        // Update properties from orderRequestDTO to order
        order.setOrderDate(orderRequestDTO.getOrderDate());
        order.setOrderItems(orderRequestDTO.getOrderItems());

        Order updatedOrder = orderRepository.save(order);
        return new OrderResponseDTO(updatedOrder.getId(), updatedOrder.getOrderDate(), updatedOrder.getCustomer().getId(), updatedOrder.getOrderItems());
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> new OrderResponseDTO(order.getId(), order.getOrderDate(), order.getCustomer().getId(), order.getOrderItems()))
                .toList();
    }

    @Override
    public OrderResponseDTO addOrder(OrderRequestDTO orderRequestDTO) {
        Order order = new Order();
        // Set properties from orderRequestDTO to order
        order.setOrderDate(orderRequestDTO.getOrderDate());
        order.setOrderItems(orderRequestDTO.getOrderItems());

        order.setCustomer(customerRepository.findById(orderRequestDTO.getCustomer_id()).orElse(null));
        Order savedOrder = orderRepository.save(order);

        OrderResponseDTO orderResponseDTO = new OrderResponseDTO(savedOrder.getId(), savedOrder.getOrderDate(),savedOrder.getCustomer().getId(), savedOrder.getOrderItems());

        return orderResponseDTO;
    }
}
