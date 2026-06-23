package org.northernarc.customerproduct.service;

import org.northernarc.customerproduct.dto.OrderRequestDTO;
import org.northernarc.customerproduct.dto.OrderResponseDTO;
import org.northernarc.customerproduct.exceptions.OrderNotFound;
import org.northernarc.customerproduct.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.northernarc.customerproduct.model.Order;

import java.util.List;

@Service
public class OrderServiceDaoImpl implements OrderServiceDao {
    @Autowired
    OrderRepository orderRepository;
     @Override
    public void deleteById(Long id) {
        if (!orderRepository.existsById(Math.toIntExact(id))) {
            throw new OrderNotFound("no order found with id " + id);
        }
        orderRepository.deleteById(Math.toIntExact(id));
    }

    @Override
    public Order getById(Long id) {
        return orderRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new OrderNotFound("no order found with id " + id));
    }

    @Override
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public OrderResponseDTO addOrder(OrderRequestDTO orderRequestDTO) {
        Order order = new Order();
        // Set properties from orderRequestDTO to order
        order.setOrderDate(orderRequestDTO.getOrderDate());
        order.setOrderItems(orderRequestDTO.getOrderItems());

        Order savedOrder = orderRepository.save(order);

        OrderResponseDTO orderResponseDTO = new OrderResponseDTO(savedOrder.getId(), savedOrder.getOrderDate(),savedOrder.getCustomer(), savedOrder.getOrderItems());

        return orderResponseDTO;
    }
}
