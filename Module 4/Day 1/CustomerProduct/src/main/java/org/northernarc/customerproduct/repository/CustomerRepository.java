package org.northernarc.customerproduct.repository;

import jakarta.transaction.Transactional;
import org.northernarc.customerproduct.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query("SELECT c FROM Customer c")
    List<Customer> findAllCustomers();

    @Modifying
    @Query("UPDATE Customer c SET c.name = :name WHERE c.id = :id")
    int updateCustomerName(@Param("id") Integer id, @Param("name") String name);
}
