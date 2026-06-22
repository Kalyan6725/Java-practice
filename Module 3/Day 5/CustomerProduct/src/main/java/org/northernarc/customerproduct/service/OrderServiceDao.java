package org.northernarc.customerproduct.service;

import org.northernarc.customerproduct.model.Order;

public interface OrderServiceDao {
    public Order addOrder(Order order);
    public void deleteById(Long id);
    public Order getById(Long id);
    public Order updateOrder(Order order);
}
