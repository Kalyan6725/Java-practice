package org.northernarc.customerproduct.service;

import org.northernarc.customerproduct.dto.OrderItemRequestDTO;
import org.northernarc.customerproduct.dto.OrderItemResponseDTO;

import java.util.List;

public interface OrderItemServiceDao {
    public void addOrderItem(OrderItemRequestDTO orderItemRequestDTO);
    public OrderItemResponseDTO getById(Long id);
    public OrderItemResponseDTO updateOrderItem(int id, OrderItemRequestDTO orderItemRequestDTO);
    public void deleteById(Long id);
    public List<OrderItemResponseDTO> getAllOrderItems();
}