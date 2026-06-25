package org.northernarc.customerproduct.repository;

import org.northernarc.customerproduct.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
