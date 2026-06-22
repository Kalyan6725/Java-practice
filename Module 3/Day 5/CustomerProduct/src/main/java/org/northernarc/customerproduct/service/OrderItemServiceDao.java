package org.northernarc.customerproduct.service;

import org.northernarc.customerproduct.model.OrderItem;

public interface OrderItemServiceDao {
    public void addOrderItem(OrderItem orderItem);
    public OrderItem getById(Long id);
}
