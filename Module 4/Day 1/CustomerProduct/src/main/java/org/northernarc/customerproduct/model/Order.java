package org.northernarc.customerproduct.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.PastOrPresent;
import java.sql.Date;

import java.util.List;

@Entity
@Data
@Table(name="orders_CP")
public class Order {
    @Id
    @GeneratedValue()
    private int id;

    @PastOrPresent
    private Date orderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;
}
