package org.northernarc.customerproduct.service;

import org.northernarc.customerproduct.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.northernarc.customerproduct.model.Order;

@Service
public class OrderServiceDaoImpl implements OrderServiceDao {
    @Autowired
    OrderRepository orderRepository;
     @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(Math.toIntExact(id));
    }

    @Override
    public Order getById(Long id) {
        return orderRepository.findById(Math.toIntExact(id)).orElse(null);
    }

    @Override
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }
    @Override
    public Order addOrder(Order order) {
        return orderRepository.save(order);
    }
}
