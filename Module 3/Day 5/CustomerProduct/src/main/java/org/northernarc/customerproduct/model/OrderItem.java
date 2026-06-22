package org.northernarc.customerproduct.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Data
@Table(name="orderitem_CP")
public class OrderItem {
    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    int id;

    @Positive
    @Min(1)
    int quantity;

    @ManyToOne
    @JsonIgnore
    private Order order;

    @ManyToOne
    @JsonIgnore
    private Product product;
}
