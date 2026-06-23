package org.northernarc.customerproduct.service;

import org.northernarc.customerproduct.dto.OrderRequestDTO;
import org.northernarc.customerproduct.dto.OrderResponseDTO;
import org.northernarc.customerproduct.model.Order;

public interface OrderServiceDao {
    public OrderResponseDTO addOrder(OrderRequestDTO orderRequestDTO);
    public void deleteById(Long id);
    public Order getById(Long id);
    public Order updateOrder(Order order);
    public java.util.List<Order> getAllOrders();
}
