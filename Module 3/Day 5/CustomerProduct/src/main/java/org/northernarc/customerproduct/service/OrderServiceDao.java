package org.northernarc.customerproduct.service;

import org.northernarc.customerproduct.dto.OrderRequestDTO;
import org.northernarc.customerproduct.dto.OrderResponseDTO;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface OrderServiceDao {
    @PreAuthorize("hasRole('USER')")
    public OrderResponseDTO addOrder(OrderRequestDTO orderRequestDTO);
    public void deleteById(Long id);
    public OrderResponseDTO getById(Long id);
    public OrderResponseDTO updateOrder(int id,OrderRequestDTO orderRequestDTO);
    @PreAuthorize("hasRole('USER')")
    public List<OrderResponseDTO> getCustomerOrders(int id);
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponseDTO> getAllOrders();
}
