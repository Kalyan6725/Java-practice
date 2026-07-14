package org.northernarc.customerproduct.service;

import org.northernarc.customerproduct.dto.OrderItemCreateRequestDTO;
import org.northernarc.customerproduct.dto.OrderItemResponseDTO;

public interface OrderItemServiceDao {
    public OrderItemResponseDTO addOrderItem(Long orderId, OrderItemCreateRequestDTO orderItemRequestDTO);
    public OrderItemResponseDTO getById(Long orderId, Long id);
    public OrderItemResponseDTO updateOrderItem(Long orderId, Long id, OrderItemCreateRequestDTO orderItemRequestDTO);
    public void deleteById(Long orderId, Long id);
}