package org.northernarc.customerproduct.service;

import org.northernarc.customerproduct.model.OrderItem;
import org.northernarc.customerproduct.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceDaoImpl implements OrderItemServiceDao {
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public void addOrderItem(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }

    @Override
    public OrderItem getById(Long id) {
        return orderItemRepository.findById(Math.toIntExact(id)).orElse(null);
    }

}
