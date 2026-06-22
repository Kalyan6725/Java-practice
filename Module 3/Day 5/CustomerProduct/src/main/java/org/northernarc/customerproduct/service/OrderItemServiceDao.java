package org.northernarc.customerproduct.service;

import org.northernarc.customerproduct.model.OrderItem;

import java.util.List;

public interface OrderItemServiceDao {
    public void addOrderItem(OrderItem orderItem);
    public OrderItem getById(Long id);
    public OrderItem updateOrderItem(OrderItem orderItem);
    public void deleteById(Long id);
    public List<OrderItem> getAllOrderItems();
}
