package org.northernarc.customerproduct.repository;

import org.northernarc.customerproduct.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
}
