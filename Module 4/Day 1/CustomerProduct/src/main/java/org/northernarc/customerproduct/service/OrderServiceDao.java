package org.northernarc.customerproduct.service;

import org.northernarc.customerproduct.dto.OrderRequestDTO;
import org.northernarc.customerproduct.dto.OrderResponseDTO;

import java.util.List;

public interface OrderServiceDao {
    public OrderResponseDTO addOrder(OrderRequestDTO orderRequestDTO);
    public void deleteById(Long id);
    public OrderResponseDTO getById(Long id);
    public OrderResponseDTO updateOrder(int id,OrderRequestDTO orderRequestDTO);
    public List<OrderResponseDTO> getAllOrders();
}
