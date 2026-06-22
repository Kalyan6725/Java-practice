package org.northernarc.customerproduct.repository;

import org.northernarc.customerproduct.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
